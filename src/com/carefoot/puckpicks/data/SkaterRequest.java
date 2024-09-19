package com.carefoot.puckpicks.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SkaterRequest implements DataRequest {
	
	private static final String DEFAULT_CATEGORY = "points";
	private static final int DEFAULT_LIMIT = 20;
	
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
		return "v1/skater-stats-leaders/current?categories=" + category + "&limit=" + Integer.toString(limit);
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}
	
	/**
	 * Parses skaters from JSON format into a list of HashMaps each with keys for data
	 * 
	 * @param parse JSONObject to parse
	 * @return List of HashMaps where each HashMap corresponds to a different skater
	 */
	public static List<HashMap<String, String>> parseJSONResponse(JSONObject parse) {
		ArrayList<HashMap<String, String>> elements = new ArrayList<>();
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
