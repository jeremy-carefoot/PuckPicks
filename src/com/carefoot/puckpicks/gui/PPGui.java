package com.carefoot.puckpicks.gui;

import java.io.InputStream;

import com.carefoot.puckpicks.main.AppLauncher;
import com.carefoot.puckpicks.main.PuckPicks;

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
	private static final String BACK_ARROW_STATIC = "back_arrow.png";
	private static final String BACK_ARROW_HOVER = "back_arrow_hover.png";
	
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
	 * Creates an ImageView from a file in resource folder
	 * 
	 * @param fileName Image file name
	 * @param height Fit height of image
	 * @param width Fit width of image
	 * @param aspectRatio Whether to preserve image aspect ratio
	 * @return ImageView object
	 */
	public static ImageView imageResource(String fileName, double height, double width, boolean aspectRatio) {
		ImageView image = new ImageView(new Image(PuckPicks.getImageResource(fileName)));
		image.setFitHeight(height);
		image.setFitWidth(width);
		image.setPreserveRatio(aspectRatio);
		return image;
	}
	
	/**
	 * Get ImageView of a generic grey error symbol
	 * 
	 * @param height Height of the symbol
	 * @param width Width of the symbol
	 * @return ImageView object of symbol
	 */
	public static ImageView errorSymbol(double height, double width) {
		ImageView image = imageResource(ERROR_SYMBOL_IMAGE, height, width, true);
		return image;
	}
	
	/**
	 * Get an interactable ImageView that functions as a back arrow in the application
	 * @param height ImageView height
	 * @param width ImageView width 
	 * @return ImageView with listener
	 */
	public static ImageView backArrow(double height, double width) {
		ImageView arrowView = imageResource(BACK_ARROW_STATIC, height, width, true); 	// constructed with static (non hover) image
		Image arrowHoverImage = new Image(PuckPicks.getImageResource(BACK_ARROW_HOVER)); 	// Image to dynamically switch ImageView on hover
		Image arrowStaticImage = new Image(PuckPicks.getImageResource(BACK_ARROW_STATIC)); 	// Image to dynamically switch ImageView when static
		
		/* add image hover animation */
//		FadeTransition fadeOut = PPAnimation.fade(arrowView, 200, 0);
//		FadeTransition fadeIn = PPAnimation.fade(arrowView, 200, 1);
		arrowView.setOnMouseEntered((e) -> {
			arrowView.setImage(arrowHoverImage);
//			fadeOut.play();
		});
		arrowView.setOnMouseExited((e)-> {
			arrowView.setImage(arrowStaticImage);
//			fadeIn.play();
		});
		
		/* add on-click action listener */
		arrowView.setOnMouseClicked((e) -> {
			AppLauncher.getApp().goBack();
		});
		return arrowView;
	}

}
