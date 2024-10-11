package com.carefoot.puckpicks.gui.scenes;

import com.carefoot.puckpicks.gui.PPGui;
import com.carefoot.puckpicks.main.AppLauncher;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public abstract class PPScene {
	
	private static final String DEFAULT_STYLESHEET_PATH = "style/default.css"; 	// path to default CSS stylesheet (app-wide)
	
	private Scene scene;
	private String css;
	private boolean initialized; 		// whether the scene has been constructed (or initialized)
	private boolean navigable; 	// whether the scene should be logged for navigation (for use with back arrow)
	
	public PPScene(String css, boolean navigable) {
		scene = null;
		initialized = false;
		this.css = css;
		this.navigable = navigable;
	}
	
	/**
	 * Sets the JavaFX scene object, marks scene as initialized, and loads scene CSS
	 * @param scene JavaFX scene object
	 */
	protected void createScene(Node node, double width, double height) {
		if (node != null) {
			/*
			 * Create scene object and include back arrow in corner (if PPScene is indicated as navigable)
			 */
			StackPane basePane = new StackPane();
			if (isNavigable() && AppLauncher.getApp().availablePreviousState()) {// if the PPScene is navigable and there is a previous state for back arrow
				StackPane backArrow = PPGui.backArrow(50, 50);
				StackPane.setAlignment(backArrow, Pos.TOP_LEFT); 	// set alignment of back arrow to top left of window
				
				basePane.getChildren().addAll(node, backArrow);
			} else {
				basePane.getChildren().add(node);
			}
			
			this.scene = new Scene(basePane, width, height);
			initialized = true;
		} else {
			throw new IllegalArgumentException("Base node cannot be null!");
		}
		
		if (css != null) // add scene-specific css if available
			this.scene.getStylesheets().add(PPScene.class.getClassLoader().getResource("style/" + css).toExternalForm());
		
		/* Add the global default CSS file */
		this.scene.getStylesheets().add(PPScene.class.getClassLoader().getResource(DEFAULT_STYLESHEET_PATH).toExternalForm());
	}
	
	/**
	 * Get JavaFX Scene object instance of scene
	 * @return JavaFX Scene object
	 */
	public Scene scene() {
		return scene;
	}
	
	/**
	 * Returns whether or not the scene has been initialized
	 * @return True or false
	 */
	public boolean initialized() {
		return initialized;
	}
	
	/**
	 * Returns whether or not the scene should be included in navigation state.
	 * For use with back arrow in the application.
	 * (E.g: A loading scene should not be navigable)
	 * @return True or false
	 */
	public boolean isNavigable() {
		return navigable;
	}

	public abstract void build(); // initialization method for subclasses

}
