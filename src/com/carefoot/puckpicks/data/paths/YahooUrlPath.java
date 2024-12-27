package com.carefoot.puckpicks.data.paths;

/**
 * Enum for storing Yahoo API URL paths
 * @author jeremycarefoot
 */
public enum YahooUrlPath {
	
	USER_INFO("openid/v1/userinfo"), 
	GET_LEAGUE_LIST("users;use_login=1/games;game_key=nhl/leagues"),
	GET_TEAM_LIST("users;use_login=1/games;game_key=nhl/teams"),
	GET_PLAYERS_BY_LEAGUE("league/{leagueid}/players"),
	GET_PLAYER_STATS("league/{leagueid}/players;player_keys={players}/stats"),
	GET_PLAYERS_BY_TEAM("team/{teamid}/roster"),
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
