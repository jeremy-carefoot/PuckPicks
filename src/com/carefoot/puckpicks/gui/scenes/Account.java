package com.carefoot.puckpicks.gui.scenes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import com.carefoot.puckpicks.authentication.OAuthentication;
import com.carefoot.puckpicks.authentication.TokenManager;
import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.paths.PPServerUrlPath;
import com.carefoot.puckpicks.data.requests.YahooUserInfoRequest;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Account extends PPScene {
	
	private TokenManager tm;
	
	public Account() {
		super(null, true, true);
		tm = new TokenManager();
	}

	@Override
	public void build() {
		createScene(assembleContent(), 500d, 500d);
	}
	
	private Parent assembleContent() {
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		Button login = new Button("Login");
		Button userInfo = new Button("Get User Info");
		OAuthentication auth;
		try {
			auth = new OAuthentication();
		} catch (NoSuchAlgorithmException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		login.setOnAction((e) -> {
			WebView webView = new WebView();
			WebEngine webEngine = webView.getEngine();
			webEngine.load(auth.getAuthenticationUrl());
			root.getChildren().add(webView);			
			webEngine.locationProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.contains(PPServerUrlPath.getBaseURL())) {
						new Thread(() -> {
							try {
								Thread.sleep(6000);
								JSONObject response = auth.fetchToken();
								tm.setAuthToken(response.getString("access_token"));
								tm.setRefreshToken(response.getString("refresh_token"));
							} catch (Exception e) {
								e.printStackTrace();
							}	
						}).start();
					}
				}	
			});

		});
		
		userInfo.setOnAction((e) -> {
			try {
				System.out.println(DataManager.submitRequest(new YahooUserInfoRequest(tm.getAuthToken())));
				System.out.println(auth.tempRefreshToken(tm.getRefreshToken()));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		});
		
		root.getChildren().addAll(login, userInfo);
		return root;
	}

}
