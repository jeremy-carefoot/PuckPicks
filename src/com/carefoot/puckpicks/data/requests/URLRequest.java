package com.carefoot.puckpicks.data.requests;

import com.carefoot.puckpicks.data.DataRequest;

/**
 * Basic class for making URL-based HTTP requests with a DataManager
 * @author jeremycarefoot
 */
public class URLRequest implements DataRequest {
	
	private String subUrl;
	
	/**
	 * Default constructor
	 * @param subUrl Portion of URL appended to base URL of DataManager
	 */
	public URLRequest(String subUrl) {
		this.subUrl = subUrl;
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}

}
