package com.carefoot.puckpicks.data.requests;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.paths.NHLUrlPath;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * Request to NHL API to grab Goalie Leaders with provided parameters
 * @author jeremycarefoot
 */
public class GoalieRequest extends DataRequest {
	
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
		super(NHLUrlPath.getBaseUrl());
		subUrl = getGoalieSubUrl(DEFAULT_CATEGORY, DEFAULT_LIMIT);
	}

	public GoalieRequest(int limit) {
		super(NHLUrlPath.getBaseUrl());
		subUrl = getGoalieSubUrl(DEFAULT_CATEGORY, limit);
	}

	public GoalieRequest(String category) {
		super(NHLUrlPath.getBaseUrl());
		subUrl = getGoalieSubUrl(category, DEFAULT_LIMIT);
	}

	public GoalieRequest(String category, int limit) {
		super(NHLUrlPath.getBaseUrl());
		subUrl = getGoalieSubUrl(category, limit);
	}

	// Return sub-url query string given a category and a limit
	private String getGoalieSubUrl(String category, int limit) {
		Map<String, String> params = new HashMap<>();
		params.put("categories", CATEGORIES.get(category)); 	// CATEGORIES map converts category name to URL path
		params.put("limit", Integer.toString(limit));
		return NHLUrlPath.GOALIE_LEADERS.path() + PuckPicks.paramsToUrl(params); 
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
	 * @param category Category used to obtain JSONObject
	 * @return List of HashMaps where each HashMap corresponds to a different goalie
	 */
	public static List<HashMap<String, String>> parseJSONResponse(JSONObject parse, String category) {
		List<HashMap<String, String>> output = SkaterRequest.parseJSONResponse(parse);
		
		if (CATEGORIES.get(category) == "savePctg") {// if we are parsing goalies sorted by save %
			/*
			 * Parse save percentage value to 4 decimal places
			 */
			DecimalFormat df = new DecimalFormat("#.0000");
			output.forEach((goalie) -> {
				Double decimalSavePctg = Double.parseDouble(goalie.get("value"));
				goalie.put("value", df.format(decimalSavePctg));
			});
		}
		
		return output;
	}
	
	@Override
	public String requestSubUrl() {
		return subUrl;
	}

}
