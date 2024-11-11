package com.carefoot.puckpicks.authentication;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.exceptions.PPServerException;
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
	 * @return PPUser object on success; null on failure
	 */
	public PPUser isLoggedIn() throws PPServerException {
		String authToken = tokens.getAuthToken();
		String refreshToken = tokens.getRefreshToken();
	
		/* If there is a stored authToken, we will try it */
		if (authToken != null) {			
			PPUser user = getYahooUserInfo(authToken);
			if (user != null) 	// if successful, return userId
				return user;
		}
		
		/* Now the auth token did not work; if there is a stored refreshToken, we will try it */
		if (refreshToken != null) {
			/* First, retrieve oauth information from PPServer */
			JSONObject oauthInfo = getOAuthInfo();
			
			/* Attempt to refresh token */
			try {
				JSONObject response = DataManager.submitRequest(new YahooOAuthRefreshRequest(
						oauthInfo.getString("token_endpoint"), oauthInfo.getString("client_id"), refreshToken));

				tokens.setAuthToken(response.getString("access_token"));
				tokens.setRefreshToken(response.getString("refresh_token"));
				
				return getYahooUserInfo(tokens.getAuthToken());
				
			} catch (IOException | JSONException e) {
				Log.log("Could not renew access token for refresh token; revalidation may be required", Log.INFO);
			} catch (URISyntaxException e) {
				Log.log(e.getMessage(), Log.ERROR); 	// internal error
			} 
		}
		
		return null;
	}
	
	/**
	 * Retrieve the basic PuckPicks OAuth information from the PuckPicks server.
	 * Includes information like the Yahoo Client ID, token endpoint, etc.
	 * @return JSON with information
	 * @throws PPServerException If connection can't be established to the PuckPicks server
	 */
	public JSONObject getOAuthInfo() throws PPServerException {
		try {
			return DataManager.submitRequest(new URLRequest(PPServerUrlPath.getBaseURL(), PPServerUrlPath.OAUTH_INFO.toString()));
		} catch (Exception e) {
			throw new PPServerException(e.getMessage());
		}
	}
	
	/**
	 * Update the current stored authentication token
	 * @param authToken new token
	 */
	public void updateAuthToken(String authToken) {
		tokens.setAuthToken(authToken);
	}

	/**
	 * Update the current stored refresh token
	 * @param authToken new token
	 */
	public void updateRefreshToken(String refreshToken) {
		tokens.setRefreshToken(refreshToken);
	}
	
	/**
	 * Send a request for a users Yahoo ID using provided auth token
	 * @param authToken user's auth token
	 * @return UserID if successful; otherwise null
	 */
	private PPUser getYahooUserInfo(String authToken) {		
		try {
			
			JSONObject response = DataManager.submitRequest(new YahooUserInfoRequest(authToken));
			return new PPUser(response.getString("sub"), response.getString("email")); 		// sub is the unique user ID
			
		} catch (IOException | JSONException e) {
			Log.log("Could not grab user info with access token; please attempt refresh token", Log.INFO);
		} catch (URISyntaxException e) {// indicates some sort of internal error
			Log.log("Invalid Yahoo URL provided! Please report this error", Log.ERROR);
		} 

		return null;
	}

}
