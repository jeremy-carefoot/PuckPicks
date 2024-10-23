package com.carefoot.puckpicks.gui.scenes;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.carefoot.puckpicks.authentication.PKCEHandler;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Account extends PPScene {
	
	public static final String CLIENT_ID = "dj0yJmk9VjhjWWFrQzhoSWJXJmQ9WVdrOVpXRTRNMHhqUW1rbWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PWJl";
	public static final String AUTH_URL = "https://api.login.yahoo.com/oauth2/request_auth";
	public static final String REDIRECT_URL = "https://grown-willingly-treefrog.ngrok-free.app/";
	
	public Account() {
		super(null, true);
	}

	@Override
	public void build() {
		createScene(assembleContent(), 500d, 500d);
	}
	
	private Parent assembleContent() {
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		Button login = new Button("Login");
		
		login.setOnAction((e) -> {
			WebView webView = new WebView();
			WebEngine webEngine = webView.getEngine();
			webEngine.load(AUTH_URL + PuckPicks.paramsToUrl(authenticationParams()));
			System.out.println(AUTH_URL + PuckPicks.paramsToUrl(authenticationParams()));
			root.getChildren().add(webView);
		});
		
		root.getChildren().add(login);
		return root;
	}
	
	private HashMap<String, String> authenticationParams() {
		String codeVerifier = PKCEHandler.generateSecureCode(43);
		String stateCode = PKCEHandler.generateSecureCode(16);
		String codeChallenge;
		try {
			codeChallenge = PKCEHandler.generateCodeChallenge(codeVerifier);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		HashMap<String, String> params = new HashMap<>();
		params.put("client_id", CLIENT_ID);
		params.put("redirect_uri", REDIRECT_URL);
		params.put("language", "en-us");
		params.put("response_type", "code");
		params.put("code_challenge", codeChallenge);
		params.put("code_challenge_method", "S256");
		params.put("state", stateCode);
		
		
		return params;
		
	}

}
