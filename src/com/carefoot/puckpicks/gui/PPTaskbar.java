package com.carefoot.puckpicks.gui;

import com.carefoot.puckpicks.gui.scenes.PPScene;
import com.carefoot.puckpicks.main.AppLauncher;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

/**
 * Taskbar GUI component at top of application.
 * Taskbar is superimposed on top of scene below it
 * 
 * @author jeremycarefoot
 */
public class PPTaskbar {
	
	private StackPane taskbarContainer;
	private StackPane backArrow;
//	private StackPane account;
	
	public PPTaskbar() {
		taskbarContainer = buildContainer();
		backArrow = PPGui.backArrow(50, 50);
		buildTaskbar();
	}
	
	/**
	 * Complete the taskbar construction (should be called after other elements are built)
	 */
	private void buildTaskbar() {
		taskbarContainer.getChildren().add(backArrow);
	}
	
	/**
	 * Returns the taskbar container
	 * @return StackPane object
	 */
	public StackPane getContainer() {
		return taskbarContainer;
	}
	
	/**
	 * Updates the taskbar.
	 * Should only be called when a scene is changed
	 * @param scene New (or current) scene
	 */
	public void update(PPScene scene) {
		/* Update the back arrow visibility (if there is navigable states) */
		boolean showBackArrow = scene.isNavigable() && AppLauncher.getApp().availablePreviousState();
		backArrow.setVisible(showBackArrow);
		backArrow.setDisable(!showBackArrow); 	// notice the boolean is whether to set disabled (logical NOT)
	}
	
	/**
	 * Builds the container for the taskbar
	 * @return StackPane object with no dimensions
	 */
	private static StackPane buildContainer() {
		StackPane container = new StackPane();
		container.getStyleClass().add("pp-taskbar");
		
		/* Set dimensions to 0 so can be superimposed and not disturb lower z-levels */
		container.setMaxHeight(0);
		container.setMaxWidth(0);
		
		StackPane.setAlignment(container, Pos.TOP_LEFT);
		return container;
	}
	

}