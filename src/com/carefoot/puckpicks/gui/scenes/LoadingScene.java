package com.carefoot.puckpicks.gui.scenes;

import com.carefoot.puckpicks.data.ResourcePath;
import com.carefoot.puckpicks.gui.PPAnimation;
import com.carefoot.puckpicks.gui.PPGui;

import javafx.animation.Animation;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LoadingScene extends PPScene {
	
	private Text loadStatus;
	
	public LoadingScene() {
		super("loading.css", false, false);
		loadStatus = null;
	}
	
	@Override
	public void build() {		
		createScene(assembleContent(), 500d, 500d);
	}
	
	/**
	 * Update the current loading status
	 * 
	 * @param status New loading status
	 */
	public void updateLoadStatus(String status) {
		if (loadStatus == null) loadStatus = buildLoadStatus(status);
		else loadStatus.setText(status);
	}
	
	/**
	 * Get the current load status
	 * @return Current load status
	 */
	public String getLoadStatus() {
		return loadStatus.getText();
	}
	
	/**
	 * Static method that builds the loading spinner image and animates it
	 * Useful for other areas in application where loading spinner needs use
	 * 
	 * @return JavaFX Image view object
	 */
	public static ImageView buildLoadingSpinner() {		
		ImageView spinner = PPGui.imageResource(ResourcePath.LOAD_SPINNER, 60, 60, true);
		PPAnimation.rotate(spinner, 360, 1180, Animation.INDEFINITE);
		return spinner;
	}
	
	// Builds main load scene 
	private Parent assembleContent() {
		StackPane sp = new StackPane();
		ImageView loadingSpinner = buildLoadingSpinner();
		updateLoadStatus("Loading...");
		
		sp.getChildren().addAll(loadingSpinner, loadStatus);
		return sp;
	}
	
	// Builds load status text
	private Text buildLoadStatus(String message) {
		Text text = new Text(message);
		text.setId("load-status");
		text.setTranslateY(50d);
		return text;
	}
}
