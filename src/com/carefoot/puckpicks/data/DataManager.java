package com.carefoot.puckpicks.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.requests.PostRequest;

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
	public JSONObject submitRequest(DataRequest req) throws IOException, URISyntaxException {
		JSONObject response = null;	

		URL url = new URI(baseUrl + req.requestSubUrl()).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		/* read the HTTP response (plaintext) */
		String raw_response = readInputStream(connection.getInputStream());

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			response  = new JSONObject(raw_response);
		} else {
			throw new IOException(raw_response);
		}
		
		return response;
	}
	
	/**
	 * Submits an HTTP Post request.
	 * Returns the response
	 * @param req Valid PostRequest
	 * @return Response in String format
	 * @throws URISyntaxException URL could not be parsed
	 * @throws MalformedURLException Invalid protocol or URL could not be parsed
	 * @throws IOException An I/O error occurred (could be communication with server)
	 */
	public String submitPost(PostRequest req) throws URISyntaxException, MalformedURLException, IOException {
		URL url = new URI(baseUrl + req.requestSubUrl()).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		/* setup headers */
		for (Entry<String, String> header : req.getHeaders().entrySet()) {
			connection.setRequestProperty(header.getKey(), header.getValue());
		}
		
		/* write content */
		try (OutputStream os = connection.getOutputStream()) {
			byte[] output  = req.getBody().getBytes("utf-8");
			os.write(output, 0, output.length);
		}
		
		/* return HTTP response, if applicable */
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			String response = readInputStream(connection.getInputStream());
			return response;
		} else {
			throw new IOException("Request failed: " + Integer.toString(connection.getResponseCode()));
		}
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			/* Close BufferedReader */
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return output.toString();
	}

}
