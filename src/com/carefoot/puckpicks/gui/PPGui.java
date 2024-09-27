package com.carefoot.puckpicks.gui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class PPGui {
	
	/*
	 * Class includes static methods for GUI construction
	 */
	
	/**
	 * Creates a Text node with an included CSS style class
	 * 
	 * @param text text content
	 * @param styleClass the CSS class name for style
	 * @return JavaFX Text object
	 */
	public static Text textWithStyle(String text, String styleClass) {
		Text output  = new Text(text);
		output.getStyleClass().add(styleClass);
		return output;
	}

	/**
	 * Creates a Text node with an attached ID (for CSS purpose)
	 * 
	 * @param text text content
	 * @param id id for the text object
	 * @return JavaFX Text object
	 */
	public static Text textWithID(String text, String id) {
		Text output  = new Text(text);
		output.setId(id);
		return output;
	}
	
	/**
	 * Creates a JavaFX blank region for filler use
	 * 
	 * @param grow whether or not the region should grow on window resize
	 * @return Region JavaFX object
	 */
	public static Region filler(boolean grow) {
		Region region = new Region();
		if (grow)
			HBox.setHgrow(region, Priority.ALWAYS);
		
		return region;
	}

	/**
	 * Creates a JavaFX blank region for filler use
	 * 
	 * @param grow whether or not the region should grow on window resize
	 * @param height minimum height for the filler space
	 * @return Region JavaFX object
	 */
	public static Region filler(boolean grow, double height) {
		Region region = new Region();
		region.setMinHeight(height);
		if (grow)
			HBox.setHgrow(region, Priority.ALWAYS);
		
		return region;
	}

}
