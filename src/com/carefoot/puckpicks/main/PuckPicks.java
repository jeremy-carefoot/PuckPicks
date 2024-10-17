package com.carefoot.puckpicks.main;

import java.io.InputStream;

/**
 * Static class with basic utility methods
 * 
 * @author jeremycarefoot
 */
public class PuckPicks {
	
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
	
	/**
	 * Grabs an image from resource folder as an input stream
	 * @param image_name Name of the image file
	 * @return Image as InputStream
	 */
	public static InputStream getImageResource(String image_name) {
		return PuckPicks.class.getClassLoader().getResourceAsStream(image_name);
	}

}
