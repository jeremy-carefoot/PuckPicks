package com.carefoot.puckpicks.data.requests;

import com.carefoot.puckpicks.data.DataRequest;

/**
 * Basic class for making URL-based HTTP requests that do not require their own class 
 * @author jeremycarefoot
 */
public class URLRequest extends DataRequest {
	
	private String subUrl;

	/**
	 * Default constructor
	 * @param subUrl Portion of URL appended to base URL of DataManager
	 */
	public URLRequest(String baseUrl, String subUrl) {
		super(baseUrl);
		this.subUrl = subUrl;
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}

}
