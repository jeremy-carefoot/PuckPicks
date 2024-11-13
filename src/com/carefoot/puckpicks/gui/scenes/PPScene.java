package com.carefoot.puckpicks.gui.scenes;

import com.carefoot.puckpicks.data.paths.FilePath;
import com.carefoot.puckpicks.gui.components.PPTaskbar;
import com.carefoot.puckpicks.main.AppLauncher;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public abstract class PPScene {
	
	private Scene scene;
	private StackPane root; 	// keep instance of root node so children can be concurrently modified
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
		root = null;
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

			if (AppLauncher.getApp() != null && includeTaskbar()) {// if application is initialized and taskbar should be shown
				PPTaskbar taskbar = AppLauncher.getApp().getTaskbar();

				/* Update the taskbar to display relevant scene information) */
				taskbar.update(this);

				basePane.getChildren().add(taskbar.getContainer());
			}
			
			this.scene = new Scene(basePane, width, height);
			this.root = basePane;
			initialized = true;
		} else {
			throw new IllegalArgumentException("Base node cannot be null!");
		}
		
		if (css != null) // add scene-specific css if available
			this.scene.getStylesheets().add(PPScene.class.getClassLoader().getResource("style/" + css).toExternalForm());
		
		/* Add the global default CSS file */
		this.scene.getStylesheets().add(PuckPicks.getFilePath(FilePath.DEFAULT_STYLESHEET));
	}
	
	/**
	 * Attaches the application taskbar instance to the top of the scene.
	 * Note: When the taskbar is attached to a scene, it is silently removed from all other scenes.
	 * If taskbar is not initialized or scene does not support the taskbar: do nothing
	 */
	public void attachTaskbar() {
		if (AppLauncher.getApp() != null && includeTaskbar()) {
			PPTaskbar taskbar = AppLauncher.getApp().getTaskbar();
			
			/* Update the taskbar to display relevant information*/
			taskbar.update(this);

			if (!root.getChildren().contains(taskbar.getContainer())) 	// add taskbar if not already in scene
				root.getChildren().add(taskbar.getContainer());
		}
	}
	
	/**
	 * Adds a node to the top of this scene's StackPane
	 * @param node Node to add
	 */
	public void addNode(Node node) {
		root.getChildren().add(node);
	}
	
	/**
	 * Pops the top children node off of this scene's GUI stack (handy for removing menus etc.)
	 */
	public void popNode() {
		root.getChildren().remove(root.getChildren().size()-1);
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
