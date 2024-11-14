package com.carefoot.puckpicks.data;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.json.XML;

import com.carefoot.puckpicks.data.DataRequest.ConnectionType;
import com.carefoot.puckpicks.data.DataRequest.ResponseFormat;
import com.carefoot.puckpicks.main.PuckPicks;

/**
 * Class used to submit HTTP requests for data.
 * Retrieved data is in JSON format.
 * 
 * @author jeremycarefoot
 */
public class DataManager {
	
	/**
	 * Submits an HTTP data request.
	 * Appends DataRequest subUrl to the DataManager baseUrl 
	 * 
	 * @param req DataRequest object containing request information
	 * @return JSONObject containing data from NHL API
	 */
	public static JSONObject submitRequest(DataRequest req) throws IOException, URISyntaxException {
		JSONObject response = null;	
		ConnectionType reqType = req.getConnectionType();

		URL url = new URI(req.getUrl()).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(reqType.toString());
		
		/* setup headers */
		for (Entry<String, String> header : req.getHeaders().entrySet()) {
			connection.addRequestProperty(header.getKey(), header.getValue());
		}

		/* If the request is a POST request, we need to write content to the connection output stream */
		if (reqType == ConnectionType.POST) {
			connection.setDoOutput(true);
			
			try (OutputStream os = connection.getOutputStream()) {
				byte[] output = ( (PostRequest) req).body().getBytes("utf-8");
				os.write(output);
			} catch (ClassCastException e) {					
				throw new IOException("Request specified as POST request but is missing content body! (Invalid request class implementation)");
			}	
		}

		/* read the HTTP response (plaintext) */
		String raw_response = PuckPicks.readInputStream(connection.getInputStream());

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			/* parse response into JSON using appropriate protocol*/
			if (req.getResponseFormat() == ResponseFormat.XML)
				response = XML.toJSONObject(raw_response);
			else
				response  = new JSONObject(raw_response);
		} else {
			throw new IOException(connection.getResponseMessage()); 		
		}
		
		return response;
	}

}
