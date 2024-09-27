package com.carefoot.puckpicks.main;

public class PuckPicks {
	
	/*
	 * Class with static utilities methods
	 */
	
	/**
	 * Capitalize the first letter in a string (lowercases rest)
	 * @param str string
	 * @return capitalized string
	 */
	public static String capitalizeFirst(String str) {
		if (str == null || str.isEmpty()) 
            return str; // Return original string if null or empty
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

}
