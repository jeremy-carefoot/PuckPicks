package com.carefoot.puckpicks.authentication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.paths.PPServerUrlPath;
import com.carefoot.puckpicks.data.requests.PPAccessCodeRequest;
import com.carefoot.puckpicks.data.requests.URLRequest;
import com.carefoot.puckpicks.data.requests.YahooOAuthRefreshRequest;
import com.carefoot.puckpicks.data.requests.YahooOAuthTokenRequest;
import com.carefoot.puckpicks.main.Log;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * Object created to oversee a Yahoo Fantasy OAuthentication.
 * Communicates with the official PuckPicks server to retrieve OAuth information.
 * Then login should be handled; once user login is complete, fetches an access token from the PuckPicks server.
 * Then request can be sent to Yahoo token endpoint for the access token
 * 
 * <b>A new OAuthentication request expires after user-login (typically 60 seconds)</b>
 * 
 * @author jeremycarefoot
 */
public class OAuthentication {
	
	private String clientID; 	// Yahoo application client ID
	private String authUrl; 		// URL for authentication login page
	private String tokenEndpoint; 	// Endpoint to fetch token after server processes request
	private String codeVerifier; 		// Code verifier for Yahoo PKCE
	private String codeChallenge; 	// Code challenge for Yahoo PKCE
	private String state; 		// Unique code for this OAuth request
	
	/**
	 * Initialize a new OAuthentication.
	 * Submits/Transmits HTTP requests (could be handled asychronously)
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws NoSuchAlgorithmException 
	 */
	public OAuthentication() throws IOException, URISyntaxException, NoSuchAlgorithmException {
		/* retrieve oauth information from server */
		JSONObject oauthInfo = DataManager.submitRequest(new URLRequest(PPServerUrlPath.getBaseURL(), PPServerUrlPath.OAUTH_INFO.path()));
		
		if (oauthInfo != null) {// communication successful
			clientID = oauthInfo.getString("client_id");
			authUrl = oauthInfo.getString("auth_base_url");
			tokenEndpoint = oauthInfo.getString("token_endpoint");
			state = UUID.randomUUID().toString();
			
			/* Generate PKCE Information*/
			codeVerifier = PKCEHandler.generateSecureCode(43); 		// 43 character minimum for code verifier
			codeChallenge = PKCEHandler.generateCodeChallenge(codeVerifier);
		} else { 
			throw new IOException("Could not establish communication with PuckPicks server"); 
		}
	}
	
	/**
	 * Assembles the complete URL for OAuth login
	 * @return URL where the user logs in to Yahoo services
	 */
	public String getAuthenticationUrl() {
		HashMap<String, String> urlParams = new HashMap<>();
		
		/* setup parameters */
		urlParams.put("client_id", clientID);
		urlParams.put("redirect_uri", PPServerUrlPath.getBaseURL());
		urlParams.put("language", "en-us");
		urlParams.put("response_type", "code");
		urlParams.put("code_challenge", codeChallenge);
		urlParams.put("code_challenge_method", PKCEHandler.CHALLENGE_METHOD);
		urlParams.put("state", state);
		
		return authUrl + PuckPicks.paramsToUrl(urlParams);
	}
	
	/**
	 * After login/authentication is completed by the user, fetch the Yahoo API token for the user.
	 * <p> Access code retrieval from PuckPicks server is required; they are held in the PuckPicks server for a fixed duration. 
	 * If access code is expired, token cannot be accessed from Yahoo API and method will throw error. </p>
	 * @return JSON response of the Yahoo server (including token) in String format
	 */
	public JSONObject fetchToken() throws IOException {
		/* grab access code from PuckPicks server (key for token) */
		String accessCode = fetchAccessCode();
		
		if (accessCode != null) {
			try {
				/* send POST request to Yahoo and wait for token in response */
				JSONObject response = DataManager.submitRequest(new YahooOAuthTokenRequest(tokenEndpoint, accessCode, clientID, codeVerifier));
				return response;
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
		} else {
			throw new IOException("Could not fetch token access code from server");
		}
	}
	
	/**
	 * Try to fetch the OAuth access code from the PuckPicks server
	 * @return Code as string, or null if the code could not be accessed.
	 */
	private String fetchAccessCode() {
		JSONObject code_response;
		try {
			code_response = DataManager.submitRequest(new PPAccessCodeRequest(state));
		} catch (IOException | URISyntaxException e) {
			Log.log("Could not get OAuthentication access code from server: " + e.getMessage(), Log.ERROR);
			return null;
		} 
		
		return code_response.getString("access_code");
	}
	
	public JSONObject tempRefreshToken(String refreshToken) throws Exception {
		JSONObject response = DataManager.submitRequest(new YahooOAuthRefreshRequest(tokenEndpoint, clientID, refreshToken));
		return response;
	}
}
