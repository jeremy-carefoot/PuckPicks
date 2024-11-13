package com.carefoot.puckpicks.gui;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/*
 * This is a static class with methods used for animating GUI features
 */

public class PPAnimation {
	
	/**
	 * Rotates a node with provided parameters
	 * 
	 * @param node Node to rotate
	 * @param angle Angle to rotate through
	 * @param durationMillis Duration of the animation in milliseconds
	 * @param cycleCount How many times the animation will run
	 * @return RotateTransition object attached to provided node
	 */
	public static RotateTransition rotate(Node node, double angle, double durationMillis, int cycleCount) {
		RotateTransition rt = new RotateTransition(Duration.millis(durationMillis), node);
		rt.setByAngle(angle);
		rt.setCycleCount(cycleCount);
		rt.play();
		return rt;
	}
	
	/**
	 * Gives a node hover animation to provided node
	 * 
	 * @param button Button to animate
	 * @param durationMillis Duration of the animation in milliseconds
	 */
	public static void animateHover(Node node, double durationMillis) {
		FadeTransition fadeOut = fade(node, durationMillis, 0.5);
		node.setOnMouseEntered(e -> fadeOut.playFromStart());
		
		FadeTransition fadeIn = fade(node, durationMillis, 1);
		node.setOnMouseExited(e -> fadeIn.playFromStart());
	}
	
	/**
	 * Constructs a FadeTransition with provided parameters 
	 * @param node Node for transition
	 * @param durationMillis Duration of the transition in ms
	 * @param opacity What opacity to fade to
	 * @return FadeTransition object
	 */
	public static FadeTransition fade(Node node, double durationMillis, double opacity) {
		FadeTransition transition = new FadeTransition(Duration.millis(durationMillis));
		transition.setNode(node);
		transition.setToValue(opacity);
		return transition;
	}
	
	/**
	 * Given two image states, gives a hover animation that fades between the two states on hover.
	 * Returns both image states in a StackPane container.
	 * Effect is accomplished by stacking the two images, and exposing one on hover 
	 * @param staticImage Regular (static) appearance of the ImageView node
	 * @param hoverImage Another ImageView that will be exposed/shown on hover
	 * @param fadeTimeMillis Duration of the fade animation (ms)
	 * @return StackPaner container with the images
	 */
	public static StackPane dualStateImageHover(ImageView staticImage, ImageView hoverImage, double fadeTimeMillis) {
		StackPane container = new StackPane();
		FadeTransition fadeOut = fade(staticImage, fadeTimeMillis, 0);
		FadeTransition fadeIn = fade(staticImage, fadeTimeMillis, 1);
		
		/* on hover, modify opacity of static image to expose hover image*/
		staticImage.setOnMouseEntered((e) -> {
			fadeOut.play();
		});
		staticImage.setOnMouseExited((e) -> {
			fadeIn.play();
		});
		
		container.getChildren().addAll(hoverImage, staticImage);
		return container;
		
	}
	
	/**
	 * Create a horizontal translation animation that moves the provided node
	 * @param node Node to move
	 * @param durationMillis Animation duration
	 * @param newX new X position of the node
	 * @return TranslateTransition object
	 */
	public static TranslateTransition horizontalTranslation(Node node, double durationMillis, double newX) {
		TranslateTransition transition = new TranslateTransition(Duration.millis(durationMillis), node);
		transition.setToX(newX);
		return transition;
	}

}
