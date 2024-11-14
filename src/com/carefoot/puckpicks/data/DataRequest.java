package com.carefoot.puckpicks.data;

import java.util.HashMap;

/**
 * Methods required for a valid DataRequest.
 * <p>DataRequests are submitted by a DataManager object; they are used to encapsulate HTTP API requests.
 * Different APIs require different DataRequests, and multiple DataRequests can be used for different calls in one API.
 * Typically each DataRequest object will represent a different type of data retrieval, and produce a valid URL path for that data.</p>
 * @author jeremycarefoot
 */
public abstract class DataRequest {
	
	/**
	 * Connection Type for HTTP request 
	 */
	public enum ConnectionType {
		GET, POST;
	}
	
	/**
	 * Raw format of the HTTP response
	 * Only JSON/XML currently supported
	 */
	public enum ResponseFormat {
		JSON, XML;
	}
	
	public static final int DEFAULT_LIMIT = 5; 		// for requests involving multiple elements; this is the default fetched limit
	
	private HashMap<String, String> headers;
	private ConnectionType type;
	private ResponseFormat responseFormat;
	private String baseUrl;
	
	/**
	 * @param baseUrl The base URL where the subURL will be appended
	 */
	public DataRequest(String baseUrl) {
		/*
		 * Safety check to ensure that the baseUrl ends with a '/'
		 * This is to keep a consistent convention where the subUrl is supplied with no leading backslash
		 */
		if (baseUrl.charAt(baseUrl.length()-1) != '/')
			baseUrl += '/';

		type = ConnectionType.GET;
		responseFormat = ResponseFormat.JSON;
		this.baseUrl = baseUrl;
		this.headers = new HashMap<>();
	}

	/**
	 * @param baseUrl The base URL where the subURL will be appended
	 * @param type The type of HTTP request (Get, Post, etc.)
	 */
	public DataRequest(String baseUrl, ConnectionType type) {
		/*
		 * Safety check to ensure that the baseUrl ends with a '/'
		 * This is to keep a consistent convention where the subUrl is supplied with no leading backslash
		 */
		if (baseUrl.charAt(baseUrl.length()-1) != '/')
			baseUrl += '/';

		responseFormat = ResponseFormat.JSON;
		this.type = type;
		this.baseUrl = baseUrl;
		this.headers = new HashMap<>();
	}

	/**
	 * Set the HTTP raw response format
	 * @param format ResponseFormat as enum value
	 */
	public void setResponseFormat(ResponseFormat format) {
		responseFormat = format;
	}
	
	/**
	 * Get the complete URL for this data HTTP request
	 * @return HTTP request URL
	 */
	public String getUrl() {
		return baseUrl + requestSubUrl();
	}
	
	/**
	 * Get custom HTTP request headers, if specified
	 * @return HashMap containing headers (can be empty)
	 */
	public HashMap<String, String> getHeaders() {
		return headers;
	}
	
	/**
	 * Get the HTTP request type. (Get, Post, etc.)
	 * @return HTTP request type as enum value
	 */
	public ConnectionType getConnectionType() {
		return type;
	}
	
	/**
	 * Get the format of the raw HTTP response 
	 * @return HTTP response format enum value
	 */
	public ResponseFormat getResponseFormat() {
		return responseFormat;
	}
	
	/**
	 * Add a custom HTTP header for the request
	 * @param key Header title
	 * @param value Header value
	 */
	protected void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	/**
	 * The sub-URL path for this request type.
	 * Appended to the base URL specified by superclass
	 * @return sub-URL path without leading '/' (e.g: api/v2/data)
	 */
	public abstract String requestSubUrl(); 		

}
