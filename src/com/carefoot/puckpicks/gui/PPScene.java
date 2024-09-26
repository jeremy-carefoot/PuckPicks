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
	
	public abstract void build();
	
	public Scene scene() {
		return scene;
	}
	
	public boolean initialized() {
		return initialized;
	}

}
