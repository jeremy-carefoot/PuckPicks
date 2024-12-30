package com.carefoot.puckpicks.data.stats;

public class RecentPerformanceTest {
		
	public static final int PREVIOUS_STAT_DAYS = 14; 	// how many previous days should be analyzed when calculating recent player performance
	
	/*
	 * Player analysis plan: TODO remove
	 * 
	 * Stats from past n days
	 * Compute average fantasy points/game and rank players
	 * If average fantasy points is 0, exceptionally rank them somehow (likely injured or out)
	 * 
	 * Option:
	 * Take stats from past 28 days as well
	 * Compare performance (fp/game) to previous two weeks
	 * Display either uptrend or downtrend in UI 
	 * 
	 * For user:
	 * See ranking, player info
	 * See fantasy points in the last 14 days, and average fantasy points/game
	 * OPTION: uptrend or downtrend display in middle
	 */

}
