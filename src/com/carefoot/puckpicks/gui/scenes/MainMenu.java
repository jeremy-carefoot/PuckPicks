package com.carefoot.puckpicks.gui.scenes;

import com.carefoot.puckpicks.gui.PPAnimation;
import com.carefoot.puckpicks.gui.PPGui;
import com.carefoot.puckpicks.main.AppLauncher;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A scene used for application navigation; includes buttons for different features
 * 
 * Extends PPScene class
 * @author jeremycarefoot
 */
public class MainMenu extends PPScene {
	
	private static final String LOGO_FILE = "logo.png"; 		// image file of primary logo
	
	private GridPane buttons;
	
	public MainMenu() {
		super("menu.css", true);
		buttons = new GridPane();
	}

	@Override
	public void build() {
		createScene(assembleContent(), 500d, 500d);
	}
	
	/**
	 * Builds all content in the scene
	 */
	private Parent assembleContent() {
		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		
		ImageView logo = PPGui.imageResource(LOGO_FILE, 125, 250, true);
		Text welcome = PPGui.textWithStyle("Welcome back!", "h2-dark");
		Text getStarted = PPGui.textWithStyle("Select a tool to get started:", "h2-dark");
		Region spacer = PPGui.filler(false, 30);
		buildButtonGrid();
		
		root.getChildren().addAll(logo, welcome, getStarted, spacer, buttons);
		return root;
	}
	
	/**
	 * Constructs the button grid menu 
	 */
	private void buildButtonGrid() {
		buttons.setAlignment(Pos.TOP_CENTER);
		
		/*
		 * Add all buttons below here
		 */
		addButtonToGrid("Leaderboards", new Leaderboard(), 0, 0);
	}
	
	/**
	 * Adds a button to the button-grid menu
	 * 
	 * @param label Label for the button
	 * @param loadScene PPScene to be loaded on-click
	 * @param row Button row position
	 * @param column Button column position
	 */
	private void addButtonToGrid(String label, PPScene loadScene, int row, int column) {
		Button button = new Button(label);
		PPAnimation.animateHover(button, 100);
		button.setOnAction((e) -> {
			AppLauncher.getApp().setScene(loadScene, false);
		});
		buttons.add(button, column, row);
	}

}
