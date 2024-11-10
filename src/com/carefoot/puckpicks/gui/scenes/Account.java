package com.carefoot.puckpicks.gui.scenes;

import com.carefoot.puckpicks.authentication.AuthenticationHandler;
import com.carefoot.puckpicks.authentication.OAuthentication;
import com.carefoot.puckpicks.data.paths.PPServerUrlPath;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Account extends PPScene {
	
	private AuthenticationHandler authHandler;
	
	public Account() {
		super(null, true, true);
		authHandler = new AuthenticationHandler();
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
			auth = new OAuthentication(authHandler);
		} catch (Exception e) {
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
								auth.completeAuthentication();
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
				System.out.println(authHandler.isLoggedIn());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		});
		
		root.getChildren().addAll(login, userInfo);
		return root;
	}

}
