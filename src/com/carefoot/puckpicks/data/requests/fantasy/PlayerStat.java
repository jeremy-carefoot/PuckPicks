package com.carefoot.puckpicks.data.requests.fantasy;

import java.util.HashMap;

/**
 * Class for storing player statistic data
 * @author jeremycarefoot
 */
public class PlayerStat {	

	private String playerKey;
	private HashMap<Integer, Double> stats;
	
	/**
	 * Create a new PlayerStat object
	 * @param playerKey Yahoo Player key
	 */
	public PlayerStat(String playerKey) {
		this.playerKey = playerKey;
		stats = new HashMap<>();
	}
	
	/**
	 * Add a statistic
	 * @param id Yahoo statistic ID
	 * @param value Value of stat
	 */
	public void addStat(int id, double value) {
		stats.put(id, value);
	}
	
	public String getPlayerKey() {
		return playerKey;
	}
	
	/**
	 * Get HashMap of player stats.
	 * @return Map of player stats [stat, value]
	 */
	public HashMap<Integer, Double> getStats() {
		return stats;
	}
	
	public String toString() {
		return "{player: " + playerKey + ", " + stats.toString() + "}";
	}
}
