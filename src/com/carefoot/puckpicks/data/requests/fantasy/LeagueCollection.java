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
 * A collection of Yahoo Fantasy Leagues
 * @author jeremycarefoot
 */
public class LeagueCollection extends YahooCollection<League> {
	
	/**
	 * Create a new league collection (consisting of all the user's active leagues)
	 * 
	 * @throws IOException Connection exception
	 * @throws PPServerException Could not connect to PuckPicks server
	 * @throws NotAuthenticatedException Reauthentication is needed for API
	 */
	public LeagueCollection() throws PPServerException, NotAuthenticatedException, IOException {
		try {
			JSONObject response = DataManager.submitRequest(
				new YahooFantasyRequest(handler, YahooUrlPath.GET_LEAGUE_LIST));
			parseRequestResponse(response);
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// unchecked internal error
		}
	}
	
	/**
	 * Parse JSON response from API into League array
	 * @param response JSON API response
	 * @return Array of leagues (null of no valid leagues)
	 */
	private void parseRequestResponse(JSONObject response) {
		JSONArray seasons = (JSONArray) PuckPicks.dotNotation(response, "fantasy_content.users.user.games.game");

		/* In case there is no leagues*/
		if (seasons.length() == 0)
			return; 

		JSONObject recentSeason = seasons.getJSONObject(seasons.length()-1); 	// most recent season is the last index in JSONArray
		JSONArray leagues = (JSONArray) PuckPicks.dotNotation(recentSeason, "leagues.league");
		
		for (int i = 0; i < leagues.length(); i++) {
			JSONObject o = leagues.getJSONObject(i);
			
			/* check to ensure the league is still active */
			if(!o.isNull("is_finished") && o.getInt("is_finished") == 1)
				continue;

			/* Create new league object from JSON data */
			League league = new League(
					o.getString("name"),
					o.getInt("league_id"),
					o.getString("league_key"),
					o.getString("logo_url"),
					o.getString("felo_tier"),
					o.getInt("num_teams"),
					o.getInt("current_week"),
					o.getInt("end_week")
					);

			addContent(league); 	// add to array
		}
	}

}
