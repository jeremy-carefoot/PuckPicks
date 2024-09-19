package com.carefoot.puckpicks.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

public class DataManager {
	
	private final String BASE_URL = "https://api-web.nhle.com/";
	
	// Submits an HTTP GET request using the NHL api as base url
	// Returns response as JSONObject
	public JSONObject submitRequest(DataRequest req) {
		JSONObject response = null;
		try {
			URL url = new URI(BASE_URL + req.requestSubUrl()).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");				
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				response  = new JSONObject(readInputStream(connection.getInputStream()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return response;
	}
	
	// Reads InputStream and returns as a string value
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
