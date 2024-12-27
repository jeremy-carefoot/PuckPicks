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
 * Wrapper class for fetching players from the Yahoo Fantasy API
 * @author jeremycarefoot
 */
public class PlayerCollection extends YahooCollection<Player> {
	
	private String leagueId;

	/**
	 * Fetch a collection of players from the provided league (with provided filters). <br> 
	 * <br>
	 * <b>Known Filters:</b>
	 * <ul>
	 *<li>count (int) - # of players to retrieve (maximum 25) </li> 
	 *<li>start (int) - given filtered context, the numbered position to start retrieving the count of players</li>
	 *<li>status (string) - status of players (e.g A for available)</li>
	 *<li>sort (string) - method of sorting (e.g AR for actual fantasy rank)</li>
	 * </ul> 
	 * @param leagueId
	 * @param filter
	 * @throws IOException
	 * @throws PPServerException
	 * @throws NotAuthenticatedException
	 */
	public PlayerCollection(String leagueId, String filter) throws IOException, PPServerException, NotAuthenticatedException {
		String subUrl = YahooUrlPath.GET_PLAYERS_BY_LEAGUE.toString().replace("{leagueid}", leagueId) + ";" + filter;
		this.leagueId =  leagueId;

		try {
			JSONObject response = DataManager.submitRequest(new YahooFantasyRequest(handler, subUrl));
			parseRequestResponse(response);
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// internal error
		}
	}
	
	public String getLeagueId() {
		return leagueId;
	}

	private void parseRequestResponse(JSONObject response) {
		JSONArray players = (JSONArray) PuckPicks.dotNotation(response, "fantasy_content.league.players.player");
		
		for (int i = 0; i < players.length(); i++) {
			JSONObject o = players.getJSONObject(i);

			Player player = new Player(
					(String) PuckPicks.dotNotation(o, "name.full"),
					o.getString("player_key"),
					o.getString("editorial_team_key"),
					o.getString("image_url"),
					o.getInt("player_id"),
					o.getInt("uniform_number"),
					o.getString("editorial_team_abbr"),
					Player.parsePositions(o.getString("display_position"))
					);
			addContent(player);
		}
	}
}