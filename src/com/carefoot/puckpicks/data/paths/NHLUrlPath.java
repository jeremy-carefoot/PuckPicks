package com.carefoot.puckpicks.data.paths;

/**
 * Enum for storing NHL API URL paths
 * @author jeremycarefoot
 */
public enum NHLUrlPath {
	
	GOALIE_LEADERS("v1/goalie-stats-leaders/current"),
	PLAYER_LEADERS("v1/skater-stats-leaders/current");
	
	private static final String BASE_URL = "https://api-web.nhle.com/";
	
	private String path;
	
	private NHLUrlPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return path;
	}
	
	public static String getBaseUrl() {
		return BASE_URL;
	}

}
