package com.carefoot.puckpicks.data.requests.fantasy;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * Yahoo Fantasy Player object
 * @author jeremycarefoot
 */
public class Player {
	
	/**
	 * Position enum for valid Player fantasy positions
	 */
	public enum Position {
		G, LW, RW, C, D, W;
	}
	
	private EnumSet<Position> positions;
	private String headshotUrl;
	private String playerKey;
	private String teamKey;
	private int playerId;
	private int uniformNumber;
	private String teamAbbreviation;
	private String name;
	
	public Player(String name, String playerKey, String teamKey, String headshotUrl, int playerId, int uniformNumber, String teamAbbreviation, Position... positions) {
		this.name = name;
		this.playerKey = playerKey;
		this.headshotUrl = headshotUrl;
		this.playerId = playerId;
		this.uniformNumber = uniformNumber;
		this.teamAbbreviation = teamAbbreviation;
		this.teamKey = teamKey;
		this.positions = EnumSet.copyOf(Arrays.asList(positions));
	}
		
	/**
	 * Parse a string of positions into a Position enum array
	 * @param positions String to parse
	 * @return Array of Position enum
	 */
	public static Position[] parsePositions(String positions) {
		String[] split = positions.split(",");
		Position[] output = new Position[ split.length ];

		for (int i = 0; i < split.length; i++)
			output[i] = Position.valueOf(split[i]);
		
		return output;
	}
	
	public String toString() {
		return "{name: " + name + ", " + "team: " + teamAbbreviation + ", positions: " + positions.toString() + ", key=" + playerKey + "}";
	}

	public EnumSet<Position> getPositions() {
		return positions;
	}

	public String getHeadshotUrl() {
		return headshotUrl;
	}

	public String getPlayerKey() {
		return playerKey;
	}

	public String getTeamKey() {
		return teamKey;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getUniformNumber() {
		return uniformNumber;
	}

	public String getTeamAbbreviation() {
		return teamAbbreviation;
	}

	public String getName() {
		return name;
	}
	
	
	
	
}
