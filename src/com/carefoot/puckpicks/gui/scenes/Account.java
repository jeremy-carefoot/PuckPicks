package com.carefoot.puckpicks.gui.scenes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import com.carefoot.puckpicks.authentication.OAuthentication;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Account extends PPScene {
	
	public Account() {
		super(null, true, true);
	}

	@Override
	public void build() {
		createScene(assembleContent(), 500d, 500d);
	}
	
	private Parent assembleContent() {
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		Button login = new Button("Login");
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
					if (newValue.contains(OAuthentication.SERVER_DNS)) {
						new Thread(() -> {
							try {
								Thread.sleep(3000);
								auth.fetchToken();
							} catch (Exception e) {
								e.printStackTrace();
							}	
						}).start();
					}
				}	
			});

		});
		
		root.getChildren().add(login);
		return root;
	}

}
