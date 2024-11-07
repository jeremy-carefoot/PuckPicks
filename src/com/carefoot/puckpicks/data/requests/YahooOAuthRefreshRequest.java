package com.carefoot.puckpicks.data.requests;

import java.util.HashMap;
import java.util.Map;

import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.PostRequest;
import com.carefoot.puckpicks.data.paths.PPServerUrlPath;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * An HTTP Post Request to the Yahoo token endpoint to exchange a refresh token for a new auth token.
 * If successful, renders previous auth token invalid.
 * @author jeremycarefoot
 */
public class YahooOAuthRefreshRequest extends DataRequest implements PostRequest {
	
	private String clientId;
	private String refreshToken;

	/**
	 * Default constructor
	 * @param tokenEndpoint Yahoo token endpoint URL
	 * @param clientId The public client ID for the OAuth request (PuckPicks specific)
	 * @param refreshToken The refresh token used for exchange
	 */
	public YahooOAuthRefreshRequest(String tokenEndpoint, String clientId, String refreshToken) {
		super(tokenEndpoint, ConnectionType.POST);
		this.clientId = clientId;
		this.refreshToken = refreshToken;
		addHeader("Content-Type", "application/x-www-form-urlencoded");
	}

	@Override
	public String body() {
		Map<String, String> bodyParams = new HashMap<>();

		bodyParams.put("client_id", clientId);
		bodyParams.put("redirect_uri", PPServerUrlPath.getBaseURL());
		bodyParams.put("refresh_token", refreshToken);
		bodyParams.put("grant_type", "refresh_token");
		
		return PuckPicks.paramsToUrl(bodyParams).substring(1); 	// substring to remove preceding "?"
	}

	@Override
	public String requestSubUrl() {
		return "";
	}

}
