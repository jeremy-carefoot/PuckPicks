package com.carefoot.puckpicks.gui;

import java.awt.Taskbar;
import java.awt.Taskbar.Feature;
import java.awt.Toolkit;

import com.carefoot.puckpicks.authentication.AuthenticationHandler;
import com.carefoot.puckpicks.data.paths.FilePath;
import com.carefoot.puckpicks.gui.components.PPTaskbar;
import com.carefoot.puckpicks.gui.scenes.LoadingScene;
import com.carefoot.puckpicks.gui.scenes.PPScene;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Primary application instance class. Acts as a wrapper for JavaFX Stage object
 * 
 * @author jeremycarefoot
 *
 */
public class PPApplication {
	
	private final Stage stage;
	private final LoadingScene loading;
	private final StateManager stateManager; 	// StateManager for logging scenes and going back in app
	private final PPTaskbar taskbar; 	// taskbar at top of application
	private final AuthenticationHandler authHandler;
	private PPScene current;
	
	public PPApplication(Stage stage) {
		this.stage = stage;
		current = null;
		/* taskbar should be constructed first as other scenes are dependent on it */
		taskbar = new PPTaskbar();
		stateManager = new StateManager();
		loading = new LoadingScene();
		authHandler = new AuthenticationHandler();

		/* Constructing loading scene and enabling until first scene is set */
		loading.build();
		stage.setScene(loading.scene()); // don't use loading() method here as app is starting up and we dont need async update

		/* Configuring application window */
		configureOSWindow();
	}
	
	/**
	 * Configures properties of the window such as icon, title, etc.
	 * Properties are OS-dependent
	 */
	private void configureOSWindow() {		
		/*
		 * Update properties that are not OS-dependent first
		 */
		setTitle("PuckPicks");
		setIcon(new Image(PuckPicks.getImageResource(FilePath.APP_ICON)));
		
		if (Taskbar.isTaskbarSupported()) {// configure macOS and Linux taskbar (if applicable)
			Taskbar bar = Taskbar.getTaskbar();
			if (bar.isSupported(Feature.ICON_IMAGE))
				bar.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(FilePath.APP_ICON.toString())));
		}
	}
	
	/**
	 * Opens the application window
	 */
	public void open() {
		stage.show();
	}
	
	/**
	 * Closes the application window
	 */
	public void close() {
		stage.close();
	}
	
	/**
	 * Enables the loading screen until scene is updated
	 */
	public void loading() {
		Platform.runLater(() -> stage.setScene(loading.scene()));
	}

	/**
	 * Sets the title of the application window
	 * @param title New window title
	 */
	public void setTitle(String title) {
		stage.setTitle(title);
	}
	
	/**
	 * Sets the icon of the application window
	 * @param image Image object
	 */
	public void setIcon(Image image) {
		stage.getIcons().add(image);
	}

	/**
	 * Sets a new scene in the application.
	 * Loads the scene asynchronously if it hasn't been built yet.
	 * If the scene is built, immediately updates the application.
	 * @param scene PPScene object (does not have to be initialized)
	 * @param goingBack Whether this scene change is due to back arrow (prevent infinite loops)
	 */
	public void setScene(PPScene scene, boolean goingBack) {
		/* If the (now) previous scene is considered navigable, log it for access with navigation back arrow */
		if (current != null && current.isNavigable() && !goingBack)
			stateManager.logState(current);

		/* Build and update the scene */
		if (!scene.initialized()) {
			loading();
			new Thread(() -> {
				scene.build();
				scene.attachTaskbar();
				Platform.runLater(() -> stage.setScene(scene.scene()));
			}).start();
		} else {
			scene.attachTaskbar();
			stage.setScene(scene.scene());
		}		

		current = scene; 	// update the current PPScene instance
	}
	
	/**
	 * Adds a node to the top of the current scene's StackPane.
	 * (If the scene is changed, this node is lost).
	 * @param node Node to add
	 */
	public void addNodeToScene(Node node) {
		current.addNode(node);
	}
	
	/**
	 * Returns true if there is a previous state currently available within the StateManager
	 * (i.e if there is somewhere a back arrow can take you)
	 * @param current The current PPScene instance
	 * @return True or false
	 */
	public boolean availablePreviousState() {
		return !stateManager.isEmpty();
	}
	
	/**
	 * Sends the application back to the previously navigated scene.
	 * Removes this scene from the scene stack
	 */
	public void goBack() {
		PPScene previous = stateManager.getAndEjectPrevious();
		if (previous != null) {// do nothing if no valid previous scene
			setScene(previous, true);
			taskbar.update(previous);
		}
	}
	
	/**
	 * Get the current window title
	 * @return Window title 
	 */
	public String getTitle() {
		return stage.getTitle();
	}
	
	/**
	 * Gets instance of current JavaFX scene
	 * @return Scene object
	 */
	public Scene getScene() {
		return stage.getScene();
	}
	
	/**
	 * Gets the taskbar instance (top of application)
	 * @return PPTaskbar object
	 */
	public PPTaskbar getTaskbar() {
		return taskbar;
	}
	
	/**
	 * Gets the application AuthenticationHandler instance
	 * @return AuthenticationHandler object
	 */
	public AuthenticationHandler getAuthHandler() {
		return authHandler;
	}
	
	/**
	 * Gets instance of current PPScene wrapper
	 * @return PPScene object
	 */
	public PPScene getPPScene() {
		return current;
	}

}
