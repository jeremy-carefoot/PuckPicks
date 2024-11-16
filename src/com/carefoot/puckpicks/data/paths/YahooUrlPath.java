package com.carefoot.puckpicks.data.paths;

/**
 * Enum for storing Yahoo API URL paths
 * @author jeremycarefoot
 */
public enum YahooUrlPath {
	
	USER_INFO("openid/v1/userinfo"), 
	GET_LEAGUE_LIST("users;use_login=1/games;game_key=nhl/leagues"),
	GET_PLAYERS("league/%leagueid%/players"),
	FANTASY_API_URL("https://fantasysports.yahooapis.com/fantasy/v2"),
	LOGIN_DOMAIN("api.login.yahoo.com"),
	LOGIN_URL("https://api.login.yahoo.com/");	
	
	private String path;
	
	private YahooUrlPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return path;
	}

}
