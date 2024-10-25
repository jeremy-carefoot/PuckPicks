package com.carefoot.puckpicks.gui.scenes;

import com.carefoot.puckpicks.gui.PPTaskbar;
import com.carefoot.puckpicks.main.AppLauncher;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public abstract class PPScene {
	
	private static final String DEFAULT_STYLESHEET_PATH = "style/default.css"; 	// path to default CSS stylesheet (app-wide)
	
	private Scene scene;
	private String css;
	private boolean initialized; 		// whether the scene has been constructed (or initialized)
	private boolean navigable; 	// whether the scene should be logged for navigation (for use with back arrow)
	private boolean includeTaskbar; 	// whether the application taskbar should be shown at the top of the screen
	
	/**
	 * Default constructor for a scene in the PuckPicks application
	 * @param css (optional) Name of scene-specific CSS file <i>(If not applicable, set to null)</i>
	 * @param navigable Whether this scene should be supported by app navigation (back arrow etc.)
	 * @param includeTaskbar Whether the application taskbar should be included at the top of the scene
	 */
	public PPScene(String css, boolean navigable, boolean includeTaskbar) {
		scene = null;
		initialized = false;
		this.css = css;
		this.navigable = navigable;
		this.includeTaskbar = includeTaskbar;
	}
	
	/**
	 * Sets the JavaFX scene object, marks scene as initialized, and loads scene CSS
	 * @param scene JavaFX scene object
	 */
	protected void createScene(Node node, double width, double height) {
		if (node != null) {
			/*
			 * Create scene object and append the application taskbar to the top
			 */
			StackPane basePane = new StackPane();
			basePane.getChildren().add(node);

			// TODO debug and fix taskbar
			if (AppLauncher.getApp() != null && includeTaskbar()) {// if application is initialized and taskbar should be shown
				PPTaskbar taskbar = AppLauncher.getApp().getTaskbar();

				/* Update the taskbar to display relevant scene information) */
				taskbar.update(this);

				basePane.getChildren().add(taskbar.getContainer());
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
	 * Returns whether or not the taskbar should be included in the top of the scene
	 * @return True or false
	 */
	public boolean includeTaskbar() {
		return includeTaskbar;
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
