package com.carefoot.puckpicks.data.exceptions;

import com.carefoot.puckpicks.main.Log;

/**
 * Exception thrown when a connection to the PuckPicks server cannot be established
 * @author jeremycarefoot
 */
public class PPServerException extends Exception {
	
	private static final long serialVersionUID = 1L; 	// default UID
	private static final String errorMessage = "Could not establish connection to PuckPicks server: ";

	public PPServerException(String error) {
		super(errorMessage + error);
		Log.log(errorMessage + error, Log.ERROR);
	}

}
