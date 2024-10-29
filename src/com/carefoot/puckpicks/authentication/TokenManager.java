package com.carefoot.puckpicks.authentication;

import java.util.ArrayList;
import java.util.List;

import com.carefoot.puckpicks.data.FileStorage;

/**
 * Storage class that is responsible for storing OAuthentication auth and refresh tokens.
 * @author jeremycarefoot
 * 
 */
public class TokenManager extends FileStorage {
	
	private String authToken;
	private String refreshToken;
	
	public TokenManager() {
		super("puckpicks.auth"); 	// storage file name
	}

	@Override
	public List<String> write() {
		List<String> content = new ArrayList<>();
		content.add(authToken);
		content.add(refreshToken);
		return content;
	}

	@Override
	public void load(List<String> content) {
		/* Load authToken from index 0 and refreshToken from index 1 if file content is present */
		if (content != null) {
			switch(content.size()) {
				case 1:
					authToken = content.get(0);
					refreshToken = null;
					break;
				case 2:
					authToken = content.get(0);
					refreshToken = content.get(1);
					break;
				default:
					authToken = null;
					refreshToken = null;
					break;
			}
		}
	}
	
	/**
	 * Set the OAuthentication token
	 * @param newToken Token as String
	 */
	public void setAuthToken(String newToken) {
		authToken = newToken;
	}
	
	/**
	 * Set the OAuthentication refresh token
	 * @param newToken Token as String
	 */
	public void setRefreshToken(String newToken) {
		refreshToken = newToken;
	}

	/**
	 * Get the stored OAuthentication token.
	 * If there is no token; returns null
	 * @return OAuth token as String
	 */
	public String getAuthToken() {
		return authToken;
	}
	
	/**
	 * Get the stored OAuthentication refresh token.
	 * If there is no token; returns null
	 * @return OAuth refresh token as String
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

}
