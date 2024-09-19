package com.carefoot.puckpicks.gui;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.Duration;

/*
 * This is a static class with methods used for animating GUI features
 */

public class PPAnimation {
	
	/**
	 * Rotates a node with provided params
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
	 * Gives a button hover animation to provided button
	 * 
	 * @param button Button to animate
	 * @param durationMillis Duration of the animation in milliseconds
	 */
	public static void animateButtonHover(Button button, double durationMillis) {
		FadeTransition fadeOut = new FadeTransition(Duration.millis(durationMillis));
		fadeOut.setNode(button);
		fadeOut.setToValue(0.5);
		button.setOnMouseEntered(e -> fadeOut.playFromStart());
		
		FadeTransition fadeIn = new FadeTransition(Duration.millis(durationMillis));
		fadeIn.setNode(button);
		fadeIn.setToValue(1);
		button.setOnMouseExited(e -> fadeIn.playFromStart());
	}

}
