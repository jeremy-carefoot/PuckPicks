package com.carefoot.puckpicks.data;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

public class GoalieRequest implements DataRequest {
	
	private static final String DEFAULT_CATEGORY = "wins";
	private static final int DEFAULT_LIMIT = 20;

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
		return "v1/goalie-stats-leaders/current?categories=" + category + "&limit=" + Integer.toString(limit);
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}

	/**
	 * Parses goalies from JSON format into a list of HashMaps each with keys for data
	 * 
	 * 
	 * @param parse JSONObject to parse
	 * @return List of HashMaps where each HashMap corresponds to a different goalie
	 */
	public static List<HashMap<String, String>> parseJSONResponse(JSONObject parse) {
		return SkaterRequest.parseJSONResponse(parse);
	}

}
