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
import com.carefoot.puckpicks.data.requests.fantasy.Player.Position;
import com.carefoot.puckpicks.main.AppLauncher;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * Wrapper class for fetching players from the Yahoo Fantasy API
 * @author jeremycarefoot
 */
public class PlayerCollection {
	
	private Player[] players;
	
	/**
	 * Fetch a collection of players from the provided league (with provided filters). <br> 
	 * <br
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
		JSONObject response;
		String subUrl = YahooUrlPath.GET_PLAYERS.toString().replace("%leagueid%", leagueId) + ";" + filter;
		try {
			response = DataManager.submitRequest(new YahooFantasyRequest(AppLauncher.getApp().getAuthHandler(), subUrl));
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// internal error
			return;
		}

		players = parseRequestResponse(response);
	}
	
	/**
	 * Get array collection
	 * @return Collection of Players
	 */
	public Player[] array() {
		return players;
	}

	private Player[] parseRequestResponse(JSONObject response) {
		JSONArray players = (JSONArray) PuckPicks.dotNotation(response, "fantasy_content.league.players.player");
		
		Player[] output = new Player[ players.length() ];
		for (int i = 0; i < output.length; i++) {
			JSONObject o = players.getJSONObject(i);

			output[i] = new Player(
					(String) PuckPicks.dotNotation(o, "name.full"),
					o.getString("player_key"),
					o.getString("editorial_team_key"),
					o.getString("image_url"),
					o.getInt("player_id"),
					o.getInt("uniform_number"),
					o.getString("editorial_team_abbr"),
					parsePositions(o.getString("display_position"))
					);
		}
		return output;
	}
	
	/**
	 * Parse a string of positions into a Position enum array
	 * @param positions String to parse
	 * @return Array of Position enum
	 */
	private Position[] parsePositions(String positions) {
		String[] split = positions.split(",");
		Position[] output = new Position[ split.length ];

		for (int i = 0; i < split.length; i++)
			output[i] = Position.valueOf(split[i]);
		
		return output;
	}
}