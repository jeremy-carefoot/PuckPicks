package com.carefoot.puckpicks.authentication;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.requests.YahooUserInfoRequest;
import com.carefoot.puckpicks.main.Log;

public class AuthenticationHandler {
	
	private TokenManager tokens;
	
	public AuthenticationHandler() {
		tokens = new TokenManager();
	}
	
	/**
	 * Checks if the current auth or refresh token are valid.
	 * If both don't work, then the user will have to relogin the Yahoo portal.
	 * @return UserID on success; null on failure
	 */
	public String isLoggedIn() {
		String authToken = tokens.getAuthToken();
		String refreshToken = tokens.getRefreshToken();
	
		/* If there is a stored authToken, we will try it */
		if (authToken != null) {			
			try {
				
				JSONObject response = DataManager.submitRequest(new YahooUserInfoRequest(authToken));
				return response.getString("sub"); 		// sub is the unique user ID
				
			} catch (IOException | JSONException e) {
				Log.log("Could not grab user info with access token; attempting refresh token", Log.INFO);
			} catch (URISyntaxException e) {// indicates that Yahoo API has been updated 
				Log.log("Invalid Yahoo URL provided! Please report this error", Log.ERROR);
				return null;
			} 
		}
		
		/* Now if there is a stored refreshToken, we will try it */
		// TODO finish this
		return null;
	}

}
