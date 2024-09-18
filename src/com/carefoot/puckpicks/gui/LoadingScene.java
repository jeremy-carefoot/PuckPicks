package com.carefoot.puckpicks.gui;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LoadingScene extends PPScene {
	
	private final String SPINNER_FILE = "loading_spinner.png";
	private Text loadStatus;
	
	public LoadingScene() {
		super("loading.css");
		loadStatus = null;
		setScene(new Scene(assembleContent(), 500d, 500d));
	}
	
	// Safe update the current load status
	public void updateLoadStatus(String status) {
		if (loadStatus == null) loadStatus = buildLoadStatus(status);
		else loadStatus.setText(status);
	}
	
	public String getLoadStatus() {
		return loadStatus.getText();
	}
	
	// Builds main load scene 
	private Parent assembleContent() {
		StackPane sp = new StackPane();
		ImageView loadingSpinner = buildLoadingSpinner();
		updateLoadStatus("Loading...");
		
		sp.getChildren().addAll(loadingSpinner, loadStatus);
		return sp;
	}
	
	// Builds the loading spinner
	private ImageView buildLoadingSpinner() {		
		Image img = new Image(getClass().getClassLoader().getResourceAsStream(SPINNER_FILE));
		ImageView spinner = new ImageView(img);
		spinner.setPreserveRatio(true);
		spinner.setFitHeight(60d);
		spinner.setFitWidth(60d);
		rotate(spinner);
		return spinner;
	}
	
	// Builds load status text
	private Text buildLoadStatus(String message) {
		Text text = new Text(message);
		text.setId("load-status");
		text.setTranslateY(50d);
		return text;
	}
	
	// Animates the loading spinner image to rotate
	private void rotate(Node node) {
		RotateTransition rt = new RotateTransition(Duration.millis(1100), node);
		rt.setDelay(Duration.ZERO);
		rt.setByAngle(360);
		rt.setCycleCount(Animation.INDEFINITE);
		rt.play();
	}
}
