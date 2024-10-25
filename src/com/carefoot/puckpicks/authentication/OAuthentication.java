package com.carefoot.puckpicks.authentication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.requests.PostRequest;
import com.carefoot.puckpicks.data.requests.URLRequest;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * Object created to oversee a Yahoo Fantasy OAuthentication.
 * Communicates with the official PuckPicks server to retrieve OAuth information.
 * A new OAuthentication request <b>expires</b>
 * 
 * @author jeremycarefoot
 */
public class OAuthentication {
	
	/* DNS to reach the official PuckPicks server */
	public static final String SERVER_DNS = "https://grown-willingly-treefrog.ngrok-free.app/";
	/*
	 * Length that any authentication is valid
	 * I.E the time that a token request can be sent to server until expiry
	 */	
	private String clientID; 	// Yahoo application client ID
	private String authUrl; 		// URL for authentication login page
	private String tokenEndpoint; 	// Endpoint to fetch token after server processes request
	private String codeVerifier; 		// Code verifier for Yahoo PKCE
	private String codeChallenge; 	// Code challenge for Yahoo PKCE
	private String state; 		// Unique code for this OAuth request
	private DataManager serverCommunicator;
	
	/**
	 * Initialize a new OAuthentication.
	 * Submits/Transmits HTTP requests (could be handled asychronously)
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws NoSuchAlgorithmException 
	 */
	public OAuthentication() throws IOException, URISyntaxException, NoSuchAlgorithmException {
		serverCommunicator = new DataManager(SERVER_DNS);
		/* retrieve oauth information from server */
		JSONObject oauthInfo = serverCommunicator.submitRequest(new URLRequest("oauth"));
		
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
		urlParams.put("redirect_uri", SERVER_DNS);
		urlParams.put("language", "en-us");
		urlParams.put("response_type", "code");
		urlParams.put("code_challenge", codeChallenge);
		urlParams.put("code_challenge_method", PKCEHandler.CHALLENGE_METHOD);
		urlParams.put("state", state);
		
		return authUrl + PuckPicks.paramsToUrl(urlParams);
	}
	
	/**
	 * After login/authentication is completed by the user, fetch the Yahoo API token for the user.
	 * Tokens are held in the PuckPicks server for a fixed duration; if expired, will throw error
	 * @return JSON response of the Yahoo server (including token) in String format
	 */
	public String fetchToken() throws IOException {
		/* grab access code from PuckPicks server (key for token) */
		String accessCode = fetchAccessCode();
		
		if (accessCode != null) {
			DataManager tokenManager = new DataManager(tokenEndpoint);
			try {
				/* send POST request to Yahoo and wait for token in response */
				return tokenManager.submitPost(assembleTokenRequest(accessCode));
			} catch (Exception e) {
				e.printStackTrace();
				throw new IOException("Could not retrieve token from Yahoo");
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
			code_response = serverCommunicator.submitRequest(new URLRequest("access-code/" + state));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		} 
		
		return code_response.getString("access_code");
	}
	
	private PostRequest assembleTokenRequest(String authCode) {
		PostRequest req  = new PostRequest();
		req.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		/*
		 * PostRequest body is a url with parameters
		 * Assemble the parameters here
		 */
		HashMap<String, String> bodyParams = new HashMap<>();
		bodyParams.put("client_id", clientID);
		bodyParams.put("redirect_uri", SERVER_DNS);
		bodyParams.put("code", authCode);
		bodyParams.put("grant_type", "authorization_code");
		bodyParams.put("code_verifier", codeVerifier);
		
		req.setBody(PuckPicks.paramsToUrl(bodyParams).substring(1)); 		// substring to remove '?' in front of params
		return req;
	}
}
