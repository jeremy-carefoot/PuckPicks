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
 * Yahoo Fantasy Roster object
 * Similar to a PlayerCollection, but allows data to be obtained for a specific teamKey
 * @author jeremycarefoot
 */
public class Roster extends YahooCollection<Player> {
	
	private String teamKey;
	
	/**
	 * Get the roster (player collection) of the provided team by team key
	 * @param teamKey Team to fetch players from
	 * @throws PPServerException
	 * @throws NotAuthenticatedException
	 * @throws IOException
	 */
	public Roster(String teamKey) throws PPServerException, NotAuthenticatedException, IOException {
		String subUrl = YahooUrlPath.GET_PLAYERS_BY_TEAM.toString().replace("{teamid}", teamKey);
		this.teamKey = teamKey;
		
		try {
			JSONObject response = DataManager.submitRequest(new YahooFantasyRequest(handler, subUrl));
			parseRequestResponse(response);
		} catch(URISyntaxException e) {
			e.printStackTrace(); 		// unchecked internal error
		}
	}
	
	public String getTeamKey() {
		return teamKey;
	}
	
	private void parseRequestResponse(JSONObject response) {
		JSONArray players =  (JSONArray) PuckPicks.dotNotation(response, "fantasy_content.team.roster.players.player");
		
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
