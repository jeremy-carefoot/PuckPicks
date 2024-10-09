package com.carefoot.puckpicks.gui.scenes;

import javafx.scene.Scene;

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
	protected void setScene(Scene scene) {
		if (scene != null) {
			this.scene = scene;
			initialized = true;
		} else {
			throw new IllegalArgumentException("Scene cannot be null");
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
