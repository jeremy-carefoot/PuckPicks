package com.carefoot.puckpicks.data.requests;

import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.paths.YahooUrlPath;

/**
 * Request to grab user information from Yahoo using provided authentication token
 * @author jeremycarefoot
 */
public class YahooUserInfoRequest extends DataRequest {

	/**
	 * Default constructor
	 * @param authToken OAuthentication token to use to get the user information
	 */
	public YahooUserInfoRequest(String authToken) {
		super(YahooUrlPath.getBaseURL());
		addHeader("Authorization", "Bearer " + authToken); 		// add authentication header
		addHeader("Host", YahooUrlPath.HOST_DOMAIN.path());
	}

	@Override
	public String requestSubUrl() {	
		return YahooUrlPath.USER_INFO.path();
	}

}