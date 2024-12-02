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
 * A collection of Yahoo Fantasy Teams
 * @author jeremycarefoot
 */
public class TeamCollection extends YahooCollection<Team> {
	
	/**
	 * Fetch all the currently authenticated user's teams
	 * 
	 * @throws PPServerException
	 * @throws NotAuthenticatedException
	 * @throws IOException
	 */
	public TeamCollection() throws PPServerException, NotAuthenticatedException, IOException {
		try {
			JSONObject response = DataManager.submitRequest(new YahooFantasyRequest(handler, YahooUrlPath.GET_TEAM_LIST));
			parseRequestResponse(response);
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// unchecked internal error
		}
	}
	
	private void parseRequestResponse(JSONObject response) {
		JSONArray seasons = (JSONArray) PuckPicks.dotNotation(response, "fantasy_content.users.user.games.game");
		
		/* in case there is no teams*/
		if (seasons.length() == 0) 
			return;
		
		JSONObject recentSeason = seasons.getJSONObject(seasons.length()-1);	// most recent season is last in array
		JSONArray teams = (JSONArray) PuckPicks.dotNotation(recentSeason, "teams.team");
		
		for (int i = 0; i < teams.length(); i++) {
			JSONObject o  = (JSONObject) teams.get(i);
			
			Team team = new Team(
					o.getString("name"),
					o.getString("team_key"),
					o.getInt("team_id"),
					o.getInt("number_of_moves"),
					o.getInt("number_of_trades"),
					(Integer) PuckPicks.dotNotation(o, "roster_adds.value")
					);
			addContent(team);
		}
	}

}
