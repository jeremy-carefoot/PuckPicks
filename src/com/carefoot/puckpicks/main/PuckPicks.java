package com.carefoot.puckpicks.main;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

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
	
	/**
	 * Takes a collection of URL parameters in a HashMap format, and translates them into a URL-appendable String (with ?)
	 * @param params HashMap<String, String> of parameters, each in name:value format
	 * @return URL-appendable String
	 */
	public static String paramsToUrl(HashMap<String, String> params) {
		String output = "?";
		
		for (Entry<String, String> param : params.entrySet()) {
			output += param.getKey() + "=" + param.getValue() + "&";
		}
		return output.substring(0, output.length()-1); 		// Don't include the final ampersand
	} 
}
