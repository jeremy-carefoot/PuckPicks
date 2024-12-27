package com.carefoot.puckpicks.data.requests.fantasy;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.exceptions.NotAuthenticatedException;
import com.carefoot.puckpicks.data.exceptions.PPServerException;
import com.carefoot.puckpicks.data.paths.YahooUrlPath;
import com.carefoot.puckpicks.data.requests.YahooFantasyRequest;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * Wrapper class for fetching player statistics from the Yahoo API
 * @author jeremycarefoot
 */
public class PlayerStatCollection extends YahooCollection<PlayerStat> {
	
	private String leagueKey;
	
	/**
	 * Get a collection of player statistics from the provided fantasy league for the provided players (all time)
	 * @param leagueId Fantasy league ID
	 * @param playerKeys (one or many) Player keys; will fetch stats for each player
	 * @throws IOException
	 * @throws PPServerException
	 * @throws NotAuthenticatedException
	 */
	public PlayerStatCollection(String leagueKey, String... playerKeys) throws IOException, PPServerException, NotAuthenticatedException {
		String subUrl = YahooUrlPath.GET_PLAYER_STATS.toString()
				.replace("{leagueid}", leagueKey)
				.replace("{players}", PuckPicks.commaSeparatedList(playerKeys));

		this.leagueKey = leagueKey;
		
		try {
			JSONObject response = DataManager.submitRequest(new YahooFantasyRequest(handler, subUrl));
			parseRequestResponse(response);
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// internal error
		}
	}

	/**
	 * Get a collection of player statistics from the provided fantasy league for the provided players (on a specific date)
	 * @param leagueId Fantasy league ID
	 * @param date Date in string form (YYYY-MM-DD)
	 * @param playerKeys (one or many) Player keys; will fetch stats for each player
	 * @throws IOException
	 * @throws PPServerException
	 * @throws NotAuthenticatedException
	 */
	public PlayerStatCollection(String leagueKey, String date, String... playerKeys) throws IOException, PPServerException, NotAuthenticatedException {
		String subUrl = YahooUrlPath.GET_PLAYER_STATS.toString()
				.replace("{leagueid}", leagueKey)
				.replace("{players}", PuckPicks.commaSeparatedList(playerKeys))
				.concat(";type=date;date=" + date);

		this.leagueKey = leagueKey;
		
		try {
			JSONObject response = DataManager.submitRequest(new YahooFantasyRequest(handler, subUrl));
			parseRequestResponse(response);
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// internal error
		}
	}
	
	private void parseRequestResponse(JSONObject response) {
		JSONArray players = (JSONArray) PuckPicks.dotNotation(response, "fantasy_content.league.players.player");

		for (int i = 0; i < players.length(); i++) {
			JSONObject player = players.getJSONObject(i);
			PlayerStat playerStats = new PlayerStat(player.getString("player_key"));
			JSONArray stats = (JSONArray) PuckPicks.dotNotation(player, "player_stats.stats.stat");			

			/* adding player total fantasy points (league-dependent) */
			JSONObject fantasyPoints = player.getJSONObject("player_points");
			playerStats.addStat(Statistic.FANTASY_POINTS.getId(), fantasyPoints.optDouble("total", 0));

			/* adding remaining game stats */
			for (int j = 0; j < stats.length(); j++) {
				JSONObject statObj = stats.getJSONObject(j);
				playerStats.addStat(statObj.getInt("stat_id"), statObj.optDouble("value", 0));
			}

			addContent(playerStats);
		}
	}
	
	public String getLeagueKey() {
		return leagueKey;
	}

}
