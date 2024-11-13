package com.carefoot.puckpicks.data.exceptions;

import com.carefoot.puckpicks.main.Log;

/**
 * Exception thrown when an access token request is not found in the PuckPicks server
 * @author jeremycarefoot
 */
public class TokenNotFoundException extends Exception {

	private static final long serialVersionUID = 1L; 	// default UID
	private static final String errorMessage = "Access token not found in PuckPicks server";

	public TokenNotFoundException() {
		super(errorMessage);
		Log.log(errorMessage, Log.WARNING);
	}
}
