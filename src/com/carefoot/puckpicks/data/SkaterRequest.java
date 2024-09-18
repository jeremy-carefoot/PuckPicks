package com.carefoot.puckpicks.data;

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

}
