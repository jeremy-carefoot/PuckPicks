package com.carefoot.gui;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class LoadingScene {
	
	private Scene scene;
	private final String SPINNER_FILE = "loading_spinner.png";
	
	public LoadingScene() {
		this.scene = new Scene(assembleContent(), 500d, 500d);
//		scene.getStylesheets().add(getClass().getClassLoader().getResource("loading.css").getPath());
	}
	
	private Parent assembleContent() {
		StackPane sp = new StackPane();

		Image img = new Image(getClass().getClassLoader().getResourceAsStream(SPINNER_FILE));
		ImageView spinner = new ImageView(img);
		spinner.setPreserveRatio(true);
		spinner.setFitHeight(60d);
		spinner.setFitWidth(60d);
		rotate(spinner);
		
		sp.getChildren().add(spinner);
		return sp;
	}
	
	private void rotate(Node node) {
		RotateTransition rt = new RotateTransition(Duration.millis(1100), node);
		rt.setDelay(Duration.ZERO);
		rt.setByAngle(360);
		rt.setCycleCount(Animation.INDEFINITE);
		rt.play();
	}
	
	public Scene scene() {
		return this.scene;
	}

}
