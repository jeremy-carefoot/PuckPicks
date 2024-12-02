package com.carefoot.puckpicks.data.requests.fantasy;

/**
 * Yahoo Fantasy Team object
 * @author jeremycarefoot
 */
public class Team {
	
	private String name;
	private String teamKey;
	private int teamId;
	private int numberOfMoves;
	private int numberOfTrades;
	private int addsThisWeek;
	
	public Team(String name, String teamKey, int teamId, int numberOfMoves, int numberOfTrades, int addsThisWeek) {
		this.name = name;
		this.teamKey = teamKey;
		this.teamId = teamId;
		this.numberOfMoves = numberOfMoves;
		this.numberOfTrades = numberOfTrades;
		this.addsThisWeek = addsThisWeek;
	}

	public String toString() {
		return "{name: " + name + ", team_key: " + teamKey + "}";
	}

	public String getName() {
		return name;
	}

	public String getTeamKey() {
		return teamKey;
	}

	public int getTeamId() {
		return teamId;
	}

	public int getNumberOfMoves() {
		return numberOfMoves;
	}

	public int getNumberOfTrades() {
		return numberOfTrades;
	}

	public int getUsedWeeklyAdds() {
		return addsThisWeek;
	}
	
}
