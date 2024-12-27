package com.carefoot.puckpicks.data.requests.fantasy;

/**
 * Enum map of Yahoo fantasy statistic IDs
 * @author jeremycarefoot
 */
public enum Statistic {

	/* skater (forward/defense) stat categories */
	GAMES_PLAYED(0, "GP"),
	GOALS(1, "G"),
	ASSISTS(2, "A"),
	POINTS(3, "P"),
	PLUS_MINUS(4, "+/-"),
	PENALTY_MINS(5, "PIMS"),
	PP_GOALS(6, "PPG"),
	PP_ASSISTS(7, "PPA"),
	PP_POINTS(8, "PPP"),
	SHORTHANDED_GOALS(9, "SHG"),
	SHORTHANDED_ASSISTS(10, "SHA"),
	SHORTHANDED_POINTS(11, "SHP"),
	GAME_WINNING_GOALS(12, "GWG"),
	GAME_TYING_GOALS(13, "GTG"),
	SHOTS_ON_GOAL(14, "SOG"),
	SHOOTING_PCTG(15, "SH%"),
	FACEOFFS_WON(16, "FW"),
	FACEOFFS_LOST(17, "FL"),
	PLAYER_GAMES(29, "GP"),
	HITS(31, "HIT"),
	BLOCKS(32, "BLK"),
	PLAYER_TIME_ON_ICE(33, "TOI"),

	/* goaltender stat categories */
	GAMES_STARTED(18, "GS"),
	WINS(19, "W"),
	LOSSES(20, "L"),
	TIES(21, "T"),
	GOALS_AGAINST(22, "GA"),
	GOALS_AGAINST_AVG(23, "GAA"),
	SHOTS_AGAINST(24, "SA"),
	SAVES(25, "SV"),
	SAVE_PCTG(26, "SV%"),
	SHUTOUTS(27, "SHO"),
	GOALIE_TIME_ON_ICE(28, "TOI"),
	GOALIE_GAMES(30, "GP"),
	
	/* other stat categories */
	AVG_TIME_ON_ICE(34, "TOI/G"),
	
	/* custom defined stat categories */
	/* (not defined in Yahoo Fantasy */
	FANTASY_POINTS(100, "FP");
	
	private int id;
	private String displayAbrv;
	
	private Statistic(int id, String displayAbrv) {
		this.id = id;
		this.displayAbrv = displayAbrv;
	}
	
	public int getId() {
		return id;
	}
	
	public String getDisplayAbrv() {
		return displayAbrv;
	}
	
	/**
	 * Match a statistic enum given the numerical id.
	 * Returns null if no match
	 * @param id Yahoo stat integer id
	 * @return Statistic enum
	 */
	public Statistic matchStat(int id) {
		for (int i = 0; i < values().length; i++) {

			if (values()[i].getId() == id)
				return values()[i];

		}
		return null; 	// if nothing matched
	}
}
