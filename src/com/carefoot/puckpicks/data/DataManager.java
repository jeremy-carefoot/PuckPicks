package com.carefoot.puckpicks.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.JSONObject;

/**
 * Class used to submit HTTP requests for data.
 * A new instance can be constructed for a base-url, where different DataRequests are appended sub-urls.
 * Retrieved data is in JSON format.
 * 
 * @author jeremycarefoot
 */
public class DataManager {
	
	private String baseUrl;
	
	/**
	 * Default constructor.
	 * @param baseUrl base-URL used to submit DataRequests
	 */
	public DataManager(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	/**
	 * Submits an HTTP data request.
	 * Appends DataRequest subUrl to the DataManager baseUrl 
	 * 
	 * @param req DataRequest object containing request information
	 * @return JSONObject containing data from NHL API
	 */
	public JSONObject submitRequest(DataRequest req) throws Exception {
		JSONObject response = null;	

		URL url = new URI(baseUrl + req.requestSubUrl()).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");				

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			response  = new JSONObject(readInputStream(connection.getInputStream()));
		}
		return response;
	}
	
	/**
	 * Reads an input stream 
	 * 
	 * @param is InputStream to read
	 * @return String of read data
	 */
	private String readInputStream(InputStream is) {
		StringBuilder output  = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String line;
		try {
			while((line = in.readLine()) != null) {
				output.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return output.toString();
	}

}
