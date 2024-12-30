package com.carefoot.puckpicks.data.requests.fantasy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.carefoot.puckpicks.data.AsyncTaskQueue;
import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.exceptions.NotAuthenticatedException;
import com.carefoot.puckpicks.data.exceptions.PPServerException;
import com.carefoot.puckpicks.data.paths.YahooUrlPath;
import com.carefoot.puckpicks.data.requests.YahooFantasyRequest;
import com.carefoot.puckpicks.main.Log;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * Wrapper class for fetching player statistics from the Yahoo API
 * @author jeremycarefoot
 */
public class PlayerStatCollection extends YahooCollection<PlayerStat> {
	
	private String leagueKey;
	
	/**
	 * Get a collection of player statistics from the provided fantasy league for the provided players (all time)
	 * Also grabs player stats from outside of league context. (sends 2 requests synchronously)
	 * 
	 * @param leagueId Fantasy league ID
	 * @param playerKeys (one or many) Player keys; will fetch stats for each player
	 * @throws IOException
	 * @throws PPServerException
	 * @throws NotAuthenticatedException
	 */
	public PlayerStatCollection(String leagueKey, String... playerKeys) throws IOException, PPServerException, NotAuthenticatedException {
		this.leagueKey = leagueKey;
		String playerKeyList = PuckPicks.commaSeparatedList(playerKeys);
		
		String leagueContextSubUrl = YahooUrlPath.GET_PLAYER_STATS_LEAGUE.toString() 	// sub-url for fetching stats in league context (e.g fantasy points)
				.replace("{leagueid}", leagueKey)
				.replace("{players}", playerKeyList);
		
		String generalSubUrl = YahooUrlPath.GET_PLAYER_STATS.toString()		// sub-url for fetching general NHL stats
				.replace("{players}", playerKeyList);
		
		try {
			JSONObject leagueResponse = DataManager.submitRequest(new YahooFantasyRequest(handler, leagueContextSubUrl));
			JSONObject generalResponse = DataManager.submitRequest(new YahooFantasyRequest(handler, generalSubUrl));

			parseRequestResponse(leagueResponse, true);
			parseRequestResponse(generalResponse, false);
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// internal error
		}
	}

	/**
	 * Get a collection of player statistics from the provided fantasy league for the provided players (on a specific date).
	 * Also grabs player stats from outside of league context. (sends 2 requests synchronously)
	 * 
	 * @param leagueId Fantasy league ID
	 * @param date Date in string form (YYYY-MM-DD)
	 * @param playerKeys (one or many) Player keys; will fetch stats for each player
	 * @throws IOException
	 * @throws PPServerException
	 * @throws NotAuthenticatedException
	 */
	public PlayerStatCollection(String leagueKey, String date, String... playerKeys) throws IOException, PPServerException, NotAuthenticatedException {
		this.leagueKey = leagueKey;
		String playerKeyList = PuckPicks.commaSeparatedList(playerKeys);
		
		String leagueContextSubUrl = YahooUrlPath.GET_PLAYER_STATS_LEAGUE.toString() 	// sub-url for fetching stats in league context (e.g fantasy points)
				.replace("{leagueid}", leagueKey)
				.replace("{players}", playerKeyList)
				.concat(";type=date;date=" + date);
		
		String generalSubUrl = YahooUrlPath.GET_PLAYER_STATS.toString()		// sub-url for fetching general NHL stats
				.replace("{players}", playerKeyList)
				.concat(";type=date;date=" + date);
		
		try {
			JSONObject leagueResponse = DataManager.submitRequest(new YahooFantasyRequest(handler, leagueContextSubUrl));
			JSONObject generalResponse = DataManager.submitRequest(new YahooFantasyRequest(handler, generalSubUrl));

			parseRequestResponse(leagueResponse, true);
			parseRequestResponse(generalResponse, false);
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// internal error
		}
	}
	
	private void parseRequestResponse(JSONObject response, boolean leagueContext) {
		JSONArray players = (JSONArray) PuckPicks.dotNotation(response, 
				leagueContext ? "fantasy_content.league.players.player" : "fantasy_content.players.player");

		for (int i = 0; i < players.length(); i++) {
			JSONObject player = players.getJSONObject(i);
			PlayerStat playerStats = new PlayerStat(player.getString("player_key"), leagueContext);
			JSONArray stats = (JSONArray) PuckPicks.dotNotation(player, "player_stats.stats.stat");			

			/* adding player total fantasy points (league-dependent) */
			if (leagueContext) {
				JSONObject fantasyPoints = player.getJSONObject("player_points");
				playerStats.addStat(Statistic.FANTASY_POINTS.getId(), fantasyPoints.optDouble("total", 0));
			}

			/* adding remaining game stats */
			for (int j = 0; j < stats.length(); j++) {
				JSONObject statObj = stats.getJSONObject(j);
				playerStats.addStat(statObj.getInt("stat_id"), statObj.optDouble("value", 0));
			}

			addContent(playerStats);
		}
	}
	
	/**
	 * Get the previous stats from the past n days for the provided roster.
	 * Returns null if unsuccessful. List may contain null entries if data for a specific date could not be fetched.
	 * Runs asynchronously (#threads = #players)
	 * 
	 * @param team Team information for data grab
	 * @param roster Roster of players to fetch stats
	 * @param days will get stats from n previous days
	 * @param timeout Time to wait on threads to complete requests before timeout (in ms)
	 * 
	 * @throws IllegalArgumentException days and timeout must be both be > 0
	 * 
	 * @return List of PlayerStatCollection, where each collection is for one day (will have n entries)
	 */
	public static List<PlayerStatCollection> asyncGetPreviousStats(Team team, Roster roster, int days, Long timeout) {		
		if (days <= 0 || timeout <= 0) 		// safety check for days/timeout arguments
			throw new IllegalArgumentException("Timeout and days must both be > 0");

		List<PlayerStatCollection> previousStats = new ArrayList<>();
		AsyncTaskQueue requestTasks = new AsyncTaskQueue(days);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");	// dd is day of month (case sensitive)
		
		for (int i = 0; i < days; i++) {
			String leagueKey = team.getLeagueKey();
			String date = LocalDate.now().minusDays(i).format(formatter);
			
			requestTasks.add(() -> {
				try {
					previousStats.add(new PlayerStatCollection(leagueKey, date, roster.getPlayerKeys()));
				} catch (IOException | PPServerException | NotAuthenticatedException e) {
					previousStats.add(null); 	// add a null entry if some stats cannot be fetched (this should not happen)
					Log.log("Could not fetch stats for league " + leagueKey + " on date " + date + ": " + e.getMessage(), Log.WARNING);
				}
			});
		}
		
		if (requestTasks.flushAndWait(timeout)) 	// execute all requests and wait for completion
			return previousStats;
		
		return null; 	// if unsuccessful
	}
	
	public String toString() {
		return "{player_count = " + super.size() + "}";
	}
	
	public String getLeagueKey() {
		return leagueKey;
	}

}
