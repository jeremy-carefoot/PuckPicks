package com.carefoot.puckpicks.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class GoalieRequest implements DataRequest {
	
	public static final String DEFAULT_CATEGORY = "Wins"; 

	private static final Map<String, String> CATEGORIES = Map.of(
			"Wins", "wins",
			"Shutouts", "shutouts",
			"Save %", "savePctg"
			); // Possible categories for GoalieRequest (display text, category key)

	private String subUrl;
	
	/*
	 * All constructors below
	 */
	
	public GoalieRequest() {
		subUrl = getGoalieSubUrl(DEFAULT_CATEGORY, DEFAULT_LIMIT);
	}

	public GoalieRequest(int limit) {
		subUrl = getGoalieSubUrl(DEFAULT_CATEGORY, limit);
	}

	public GoalieRequest(String category) {
		subUrl = getGoalieSubUrl(category, DEFAULT_LIMIT);
	}

	public GoalieRequest(String category, int limit) {
		subUrl = getGoalieSubUrl(category, limit);
	}

	// Return sub-url query string given a category and a limit
	private String getGoalieSubUrl(String category, int limit) {
		// category map is used to convert category display name to url path
		return "v1/goalie-stats-leaders/current?categories=" + CATEGORIES.get(category) + "&limit=" + Integer.toString(limit); 
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}
	
	/**
	 * Returns all possible categories for this request type 
	 * @return String[] of possible categories
	 */
	public static String[] categories() {
		return (String[]) CATEGORIES.keySet().toArray(new String[0]);
	}

	/**
	 * Parses goalies from JSON format into a list of HashMaps each with keys for data 
	 * @param parse JSONObject to parse
	 * @return List of HashMaps where each HashMap corresponds to a different goalie
	 */
	public static List<HashMap<String, String>> parseJSONResponse(JSONObject parse) {
		return SkaterRequest.parseJSONResponse(parse);
	}

}
