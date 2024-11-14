package com.carefoot.puckpicks.data.requests;

import com.carefoot.puckpicks.authentication.AuthenticationHandler;
import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.exceptions.NotAuthenticatedException;
import com.carefoot.puckpicks.data.exceptions.PPServerException;
import com.carefoot.puckpicks.data.paths.YahooUrlPath;

/**
 * Request to the Yahoo Fantasy API.
 * Requires the user to be logged in and authenticated
 * @author jeremycarefoot
 */
public class YahooFantasyRequest extends DataRequest {
	
	private String subUrl;
	
	/**
	 * Create a new Fantasy API request
	 * @param authHandler AuthenticationHandler for current session
	 * @param subUrl subURL endpoint of the request
	 * @throws NotAuthenticatedException If the user could not be authenticated
	 * @throws PPServerException If the PuckPicks server cannot be reached for validation
	 */
	public YahooFantasyRequest(AuthenticationHandler authHandler, YahooUrlPath subUrl) throws NotAuthenticatedException, PPServerException {
		super(YahooUrlPath.FANTASY_API_URL.toString());
		setResponseFormat(ResponseFormat.XML);
		this.subUrl = subUrl.toString();
		
		/* verify authentication */
		if (authHandler.isLoggedIn() != null ) {
			addHeader("Authorization", "Bearer " + authHandler.getAuthToken());
		} else {
			throw new NotAuthenticatedException();
		}
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}

}
