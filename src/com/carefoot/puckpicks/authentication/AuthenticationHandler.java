package com.carefoot.puckpicks.authentication;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.paths.PPServerUrlPath;
import com.carefoot.puckpicks.data.requests.URLRequest;
import com.carefoot.puckpicks.data.requests.YahooOAuthRefreshRequest;
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
			} catch (URISyntaxException e) {// indicates some sort of internal error
				Log.log("Invalid Yahoo URL provided! Please report this error", Log.ERROR);
				return null;
			} 
		}
		
		/* Now if there is a stored refreshToken, we will try it */
		
		/* First, retrieve oauth information from PPServer */
		JSONObject oauthInfo;
		try {
			oauthInfo = DataManager.submitRequest(new URLRequest(PPServerUrlPath.getBaseURL(), PPServerUrlPath.OAUTH_INFO.path()));
		} catch (Exception e) {
			Log.log("Could not communicate with PuckPicks server to retrieve OAuth information: " + e.getMessage(), Log.ERROR);
			return null;
		}
		
		/* Attempt to refresh token */
		try {
			JSONObject response = DataManager.submitRequest(new YahooOAuthRefreshRequest(
					oauthInfo.getString("token_endpoint"), oauthInfo.getString("client_id"), tokens.getRefreshToken()));
			tokens.setAuthToken(response.getString("access_token"));
			tokens.setRefreshToken(response.getString("refresh_token"));
		} catch (IOException | JSONException e) {
			Log.log("Could not renew access token for refresh token; revalidation may be required", Log.INFO);
		} catch (URISyntaxException e) {// indicates some sort of internal error
			Log.log("Invalid Yahoo URL provided! Please report this error", Log.ERROR);
		} 
		return null;
	}

}
