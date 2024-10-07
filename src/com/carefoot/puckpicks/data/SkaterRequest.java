package com.carefoot.puckpicks.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SkaterRequest implements DataRequest {
	
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
		subUrl = getSkaterSubUrl(DEFAULT_CATEGORY, DEFAULT_LIMIT);
	}
	
	public SkaterRequest(int limit) {
		subUrl = getSkaterSubUrl(DEFAULT_CATEGORY, limit);
	}

	public SkaterRequest(String category) {
		subUrl = getSkaterSubUrl(category, DEFAULT_LIMIT);
	}
	
	public SkaterRequest(String category, int limit) {
		subUrl = getSkaterSubUrl(category, limit);
	}

	// Returns sub-URL query string given a category and limit
	private String getSkaterSubUrl(String category, int limit) {
		// categories map used to convert from category display name to url path
		return "v1/skater-stats-leaders/current?categories=" + CATEGORIES.get(category) + "&limit=" + Integer.toString(limit);
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
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

}
