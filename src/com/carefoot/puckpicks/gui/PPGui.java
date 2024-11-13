package com.carefoot.puckpicks.gui;

import java.io.InputStream;

import com.carefoot.puckpicks.data.paths.FilePath;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Static class for constructing reusable GUI elements
 * 
 * @author jeremycarefoot
 *
 */
public class PPGui {
	
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
	public static ImageView imageResource(FilePath path, double height, double width, boolean aspectRatio) {
		ImageView image = new ImageView(new Image(PuckPicks.getImageResource(path)));
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
		ImageView image = imageResource(FilePath.ERROR_ICON, height, width, true);
		return image;
	}
	
	/**
	 * Get a StackPane containing an animated back arrow for navigation in the application.
	 * No configured listener; must be added to node at index <b>1</b>
	 * 
	 * @param height ImageView height
	 * @param width ImageView width
	 * @return StackPane containing ImageView objects
	 */
	public static StackPane backArrow(double height, double width) {
		ImageView arrowStaticView = imageResource(FilePath.BACK_ARROW, height, width, true); 	// constructed with static (non hover) image
		ImageView arrowHoverView = imageResource(FilePath.BACK_ARROW_HOVER, height, width, true); 	// Image to dynamically switch ImageView on hover
		
		StackPane arrowContainer = PPAnimation.dualStateImageHover(arrowStaticView, arrowHoverView, 200);
		arrowContainer.getStyleClass().add("taskbar-back-arrow");
		
		return arrowContainer;
	}
	
	/**
	 * Get a StackPane containing an animated account icon for account management.
	 * No configured listener; must be added to node at index <b>1</b>
	 * 
	 * @param height ImageView height
	 * @param width ImageView width
	 * @return StackPane containing ImageView objects
	 */
	public static StackPane accountIcon(double height, double width) {
		ImageView iconStatic = imageResource(FilePath.ACCOUNT_ICON, height, width, true);
		ImageView iconHover = imageResource(FilePath.ACCOUNT_ICON_HOVER, height, width, true);
		
		StackPane iconContainer = PPAnimation.dualStateImageHover(iconStatic, iconHover, 200);
		iconContainer.getStyleClass().add("taskbar-account-icon");
		
		return iconContainer;	
	}

	/**
	 * Get a StackPane containing an animated cancel icon.
	 * No configured listener; must be added to node at index <b>1</b>
	 * 
	 * @param height ImageView height
	 * @param width ImageView width
	 * @return StackPane containing ImageView objects
	 */
	public static StackPane cancelIcon(double height, double width) {
		ImageView iconStatic = imageResource(FilePath.CANCEL_ICON, height, width, true);
		ImageView iconHover = imageResource(FilePath.CANCEL_ICON_HOVER, height, width, true);
		
		StackPane iconContainer = PPAnimation.dualStateImageHover(iconStatic, iconHover, 200);
		iconContainer.getStyleClass().add("cancel-icon");
		
		return iconContainer;	
	}

}
