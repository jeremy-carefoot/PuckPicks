package com.carefoot.puckpicks.data.requests;

import java.util.HashMap;

import com.carefoot.puckpicks.data.DataRequest;

/**
 * Class for constructing HTTP Post requests.
 * @author jeremycarefoot
 */
public class PostRequest implements DataRequest {
	
	private HashMap<String, String> headers;
	private String body;
	private String subUrl;
	
	/**
	 * Default constructor (empty sub-URL)
	 */
	public PostRequest() {
		headers = new HashMap<>();
		subUrl = "";
		body = "";
	}

	/**
	 * Constructor with sub-URL
	 * @param subUrl URL-path to submit request
	 */
	public PostRequest(String subUrl) {
		headers = new HashMap<>();
		this.subUrl = subUrl;
		body = "";
	}
	
	/**
	 * Add a header to the request header content
	 * @param title Header entry title
	 * @param value Header entry value
	 */
	public void addHeader(String title, String value) {
		headers.put(title, value);
	}
	
	/**
	 * Set the body of the request
	 * @param content String of content
	 */
	public void setBody(String content) {
		body = content;
	}
	
	public String getBody() {
		return body;
	}
	
	public HashMap<String, String> getHeaders() {
		return headers;
	}

	@Override
	public String requestSubUrl() {
		return subUrl;
	}

}
