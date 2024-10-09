package com.carefoot.puckpicks.gui;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Static class for constructing reusable GUI elements
 * 
 * @author jeremycarefoot
 *
 */
public class PPGui {
	
	private static final String ERROR_SYMBOL_IMAGE = "error.png";
	
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
	
	/**
	 * Creates an ImageView
	 * 
	 * @param stream InputStream of image
	 * @param height Fit height of image
	 * @param width Fit width of image
	 * @param aspectRatio Whether to preserve image aspect ratio
	 * @return ImageView object
	 */
	public static ImageView image(InputStream stream, double height, double width, boolean aspectRatio) {
		ImageView image = new ImageView(new Image(stream));
		image.setFitHeight(height);
		image.setFitWidth(width);
		image.setPreserveRatio(aspectRatio);
		return image;
	}
	
	/**
	 * Get ImageView of a generic grey error symbol
	 * 
	 * @param width Width of the symbol
	 * @param height Height of the symbol
	 * @return ImageView object of symbol
	 */
	public static ImageView errorSymbol(double width, double height) {
		Image image = new Image(PPGui.class.getClassLoader().getResourceAsStream(ERROR_SYMBOL_IMAGE));
		ImageView view = new ImageView(image);
		view.setFitWidth(width);
		view.setFitHeight(height);
		return view;
	}

}
