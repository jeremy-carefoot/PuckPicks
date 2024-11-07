package com.carefoot.puckpicks.data.paths;

/**
 * Enum for storing Yahoo API URL paths
 * @author jeremycarefoot
 */
public enum PPServerUrlPath {
	
	OAUTH_INFO("oauth"), FETCH_ACCESS_TOKEN("access-code");

	private static final String BASE_URL = "https://grown-willingly-treefrog.ngrok-free.app/"; 		// TODO change in prod (when proper domain is obtained)
	private String path;
	
	private PPServerUrlPath(String path) {
		this.path = path;
	}
	
	public String path() {
		return path;
	}
	
	public static String getBaseURL() {
		return BASE_URL;
	}

}
