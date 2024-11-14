package com.carefoot.puckpicks.data.requests.fantasy;

/**
 * Yahoo Fantasy League object
 * @author jeremycarefoot
 */
public class League {
	
	/**
	 * Different user league tiers
	 */
	public enum LeagueTier {
		DIAMOND, PLATINUM, GOLD, SILVER, BRONZE;
	}
	
	private int endWeek;
	private int currentWeek;
	private int teamCount;
	private int leagueId;
	private String name;
	private String leagueKey;
	private String logoURL;
	private LeagueTier tier;
	
	/**
	 * Create a new League object
	 * @param name League name
	 * @param leagueId Yahoo league ID
	 * @param leagueKey Yahoo league key ([game id].l.[league key])
	 * @param logoURL League logo URL
	 * @param tier User tier
	 * @param teamCount Number of teams in league
	 * @param currentWeek Current week of the league
	 * @param endWeek Final week of the league
	 */
	public League(String name, int leagueId, String leagueKey, String logoURL, String tier, int teamCount, int currentWeek, int endWeek) {
		this.name = name;
		this.leagueId = leagueId;
		this.leagueKey = leagueKey;
		this.logoURL = logoURL;
		this.tier = LeagueTier.valueOf(tier.toUpperCase());
		this.teamCount = teamCount;
		this.currentWeek = currentWeek;
		this.endWeek = endWeek;
	}
	
	public int getEndWeek() {
		return endWeek;
	}
	public int getCurrentWeek() {
		return currentWeek;
	}
	public int getTeamCount() {
		return teamCount;
	}
	public int getLeagueId() {
		return leagueId;
	}
	public String getName() {
		return name;
	}
	public String getLeagueKey() {
		return leagueKey;
	}
	public String getLogoURL() {
		return logoURL;
	}
	public LeagueTier getTier() {
		return tier;
	}
	
	public String toString() {
		return "{name: " + name + ", league_key:" + leagueKey + "}";
	}

}
