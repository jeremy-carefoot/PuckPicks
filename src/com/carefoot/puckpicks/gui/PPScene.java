package com.carefoot.gui;

import javafx.scene.Scene;

public abstract class PPScene {
	
	private Scene scene;
	
	public PPScene(Scene scene) {
		this.scene = scene;
	}
	
	public Scene scene() {
		return scene;
	}

}
