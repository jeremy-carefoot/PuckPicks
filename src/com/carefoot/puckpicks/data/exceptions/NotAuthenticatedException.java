package com.carefoot.puckpicks.data.exceptions;

import com.carefoot.puckpicks.main.Log;

public class NotAuthenticatedException extends Exception {

	private static final long serialVersionUID = 1L; 	// default UID
	private static final String errorMessage = "Invalid access and refresh token: Please re-authenticate";

	public NotAuthenticatedException() {
		super(errorMessage);
		Log.log(errorMessage, Log.WARNING);
	}
}
