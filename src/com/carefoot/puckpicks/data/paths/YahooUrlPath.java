package com.carefoot.puckpicks.data.paths;

/**
 * Enum for storing Yahoo API URL paths
 * @author jeremycarefoot
 */
public enum YahooUrlPath {
	
	USER_INFO("openid/v1/userinfo"), 
	HOST_DOMAIN("api.login.yahoo.com");

	private static final String BASE_URL = "https://api.login.yahoo.com/";
	private String path;
	
	private YahooUrlPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return path;
	}
	
	public static String getBaseURL() {
		return BASE_URL;
	}

}
