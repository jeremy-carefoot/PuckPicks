package com.carefoot.puckpicks.gui;

import javafx.scene.Scene;

public abstract class PPScene {
	
	private Scene scene;
	private String css;
	private boolean initialized;
	
	public PPScene(String css) {
		scene = null;
		initialized = false;
		this.css = css;
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
		
		if (css != null)
			this.scene.getStylesheets().add(PPScene.class.getClassLoader().getResource("style/" + css).toExternalForm());
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

	public abstract void build(); // initialization method for subclasses

}
