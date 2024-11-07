package com.carefoot.puckpicks.data.requests;

import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.paths.PPServerUrlPath;

/**
 * Send a request to the PPServer to retrieve OAuth access code for token exchange.
 * Should be sent after login.
 * @author jeremycarefoot
 */
public class PPAccessCodeRequest extends DataRequest {
	
	private String stateId;
	
	/**
	 * Default constructor
	 * @param stateId Client state session ID to exchange for code
	 */
	public PPAccessCodeRequest(String stateId) {
		super(PPServerUrlPath.getBaseURL());
		this.stateId = stateId;
	}

	@Override
	public String requestSubUrl() {
		return PPServerUrlPath.FETCH_ACCESS_TOKEN.path() + "/" + stateId;
	}

}
