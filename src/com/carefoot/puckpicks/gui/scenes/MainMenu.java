package com.carefoot.puckpicks.gui.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * A scene used for application navigation; includes buttons for different features
 * 
 * Extends PPScene class
 * @author jeremycarefoot
 */
public class MainMenu extends PPScene {
	
	public MainMenu() {
		super("menu.css");
	}

	@Override
	public void build() {
		setScene(new Scene(assembleContent(), 500d, 500d));
	}
	
	private Parent assembleContent() {
		// TODO complete this scene
		return null;
	}

}
