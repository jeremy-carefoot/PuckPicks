package com.carefoot.puckpicks.gui;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
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

}
