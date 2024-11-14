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
import com.carefoot.puckpicks.main.AppLauncher;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * A collection of Yahoo Fantasy Leagues
 * @author jeremycarefoot
 */
public class LeagueCollection {
	
	private League[] leagues;
	
	/**
	 * Create a new league collection (consisting of all the user's active leagues)
	 * 
	 * @throws IOException Connection exception
	 * @throws PPServerException Could not connect to PuckPicks server
	 * @throws NotAuthenticatedException Reauthentication is needed for API
	 */
	public LeagueCollection() throws PPServerException, NotAuthenticatedException, IOException {
		JSONObject response;
		try {
			response = DataManager.submitRequest(
				new YahooFantasyRequest(AppLauncher.getApp().getAuthHandler(), YahooUrlPath.GET_LEAGUE_LIST));
		} catch (URISyntaxException e) {
			e.printStackTrace(); 		// unchecked internal error
			return;
		}
		leagues = parseRequestResponse(response);
	}
	
	/**
	 * Get the array of leagues stored in this collection
	 * @return League array
	 */
	public League[] array() {
		return leagues;
	}
	
	/**
	 * String representation
	 */
	public String toString() {
		String output = "[";
		for (int i = 0; i < leagues.length; i++)
			output += leagues[i].toString() + (i < leagues.length-1 ? ", " : "");
		return output + "]";
	}
	
	/**
	 * Parse JSON response from API into League array
	 * @param response JSON API response
	 * @return Array of leagues
	 */
	private League[] parseRequestResponse(JSONObject response) {
		JSONArray seasons = (JSONArray) PuckPicks.dotNotation(response, "fantasy_content.users.user.games.game");
		JSONObject recentSeason = seasons.getJSONObject(seasons.length()-1); 	// most recent season is the last index in JSONarray
		JSONArray leagues = (JSONArray) PuckPicks.dotNotation(recentSeason, "leagues.league");
		
		League[] output = new League[ leagues.length() ];
		for (int i = 0; i < output.length; i++) {
			JSONObject o = leagues.getJSONObject(i);

			/* Create new league object from JSON data */
			output[i] = new League(
					o.getString("name"),
					o.getInt("league_id"),
					o.getString("league_key"),
					o.getString("logo_url"),
					o.getString("felo_tier"),
					o.getInt("num_teams"),
					o.getInt("current_week"),
					o.getInt("end_week")
					);
		}
		return output;
	}

}
