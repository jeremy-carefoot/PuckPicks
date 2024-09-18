package com.carefoot.puckpicks.gui;

import javafx.scene.Scene;

public abstract class PPScene {
	
	private Scene scene;
	private String css;
	
	public PPScene(String css) {
		scene = null;
		this.css = css;
	}
	
	public void setScene(Scene scene) {
		if (scene != null) {
			this.scene = scene;
		} else {
			throw new IllegalArgumentException("Scene cannot be null");
		}
		
		if (css != null)
			this.scene.getStylesheets().add(PPScene.class.getClassLoader().getResource("style/" + css).toExternalForm());
	}
	
	public Scene scene() {
		return scene;
	}

}
