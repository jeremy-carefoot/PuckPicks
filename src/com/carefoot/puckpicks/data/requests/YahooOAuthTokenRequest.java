package com.carefoot.puckpicks.data.requests;

import java.util.HashMap;
import java.util.Map;

import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.PostRequest;
import com.carefoot.puckpicks.data.paths.PPServerUrlPath;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * An HTTP Post request to the Yahoo token endpoint for access code/token exchange.
 * Requires an access code retrieved from the PuckPicks server.
 * @author jeremycarefoot
 */
public class YahooOAuthTokenRequest extends DataRequest implements PostRequest {
	
	private String accessCode;
	private String clientID;
	private String codeVerifier;
	
	/**
	 * Create a new OAuth access code to access token exchange for Yahoo
	 * @param tokenEndpoint URL to send POST request
	 * @param accessCode Access code for exchange (from PuckPicks server)
	 * @param clientID The public clientID for the OAuth request (PuckPicks specific)
	 * @param codeVerifier The code verifier for PKCE authentication
	 */
	public YahooOAuthTokenRequest(String tokenEndpoint, String accessCode, String clientID, String codeVerifier) {
		super(tokenEndpoint, ConnectionType.POST);
		addHeader("Content-Type", "application/x-www-form-urlencoded");
		this.accessCode = accessCode;
		this.clientID = clientID;
		this.codeVerifier = codeVerifier;
	}

	@Override
	public String body() {
		Map<String, String> bodyParams = new HashMap<>();
		
		bodyParams.put("client_id", clientID);
		bodyParams.put("redirect_uri", PPServerUrlPath.getBaseURL());
		bodyParams.put("code", accessCode);
		bodyParams.put("grant_type", "authorization_code");
		bodyParams.put("code_verifier", codeVerifier);
		
		return PuckPicks.paramsToUrl(bodyParams).substring(1); 	// substring to remove '?' in front of params
	}

	@Override
	public String requestSubUrl() {
		return "";
	}

}
