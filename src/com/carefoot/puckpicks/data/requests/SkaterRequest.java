package com.carefoot.puckpicks.data.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.paths.NHLUrlPath;
import com.carefoot.puckpicks.main.PuckPicks;

public class SkaterRequest extends DataRequest {
	
	public static final String DEFAULT_CATEGORY = "Points";
	
	private static final Map<String, String> CATEGORIES = Map.of(
			"Points", "points",
			"Goals", "goals",
			"Assists", "assists"
			); // Possible categories for SkaterRequest (display text, category key)
	
	private String subUrl;	
	
	/*
	 * All constructors below
	 */

	public SkaterRequest() {
		super(NHLUrlPath.getBaseUrl());
		subUrl = getSkaterSubUrl(DEFAULT_CATEGORY, DEFAULT_LIMIT);
	}
	
	public SkaterRequest(int limit) {
		super(NHLUrlPath.getBaseUrl());
		subUrl = getSkaterSubUrl(DEFAULT_CATEGORY, limit);
	}

	public SkaterRequest(String category) {
		super(NHLUrlPath.getBaseUrl());
		subUrl = getSkaterSubUrl(category, DEFAULT_LIMIT);
	}
	
	public SkaterRequest(String category, int limit) {
		super(NHLUrlPath.getBaseUrl());
		subUrl = getSkaterSubUrl(category, limit);
	}

	// Returns sub-URL query string given a category and limit
	private String getSkaterSubUrl(String category, int limit) {
		Map<String, String> params = new HashMap<>();
		params.put("categories", CATEGORIES.get(category)); 	// CATEGORIES map converts category title to url
		params.put("limit", Integer.toString(limit));
		return NHLUrlPath.PLAYER_LEADERS.path() + PuckPicks.paramsToUrl(params);
	}
	
	/**
	 * Returns all possible categories for this request type
	 * @return String[] of categories
	 */
	public static String[] categories() {
		return (String[]) CATEGORIES.keySet().toArray(new String[0]);
	}
	
	/**
	 * Parses skaters from JSON format into a list of HashMaps each with keys for data
	 * @param parse JSONObject to parse
	 * @return List of HashMaps where each HashMap corresponds to a different skater
	 */
	public static List<HashMap<String, String>> parseJSONResponse(JSONObject parse) {
		ArrayList<HashMap<String, String>> elements = new ArrayList<>();
		if (parse == null) 	// return empty list if invalid object to parse
			return elements;
		JSONArray array = parse.getJSONArray(parse.keys().next());
		for (Object o : array) {
			JSONObject json = (JSONObject) o;
			HashMap<String, String> element = new HashMap<>();
			for (String key : json.keySet()) {
				String value;
				try {
					value = json.getJSONObject(key).getString("default");
				} catch (JSONException e) {
					value = json.get(key).toString();
				}
				element.put(key, value);
			}
			elements.add(element);
		}
		
		return elements;
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}
	
}
