package com.carefoot.puckpicks.gui;

import java.util.Stack;

import com.carefoot.puckpicks.gui.scenes.PPScene;

/**
 * Manages state of the application.
 * Keeps track of previous states/scenes and navigation order
 * 
 * @author jeremycarefoot
 */
public class StateManager {
	
	private Stack<PPScene> previousScenes;
	
	public StateManager() {
		previousScenes = new Stack<>();
	}
	
	/**
	 * Will return the previous navigated scene, and eject it from stack.
	 * (For use with a back arrow in application)
	 * @return Previous PPScene; if no scene exists, then returns null
	 */
	protected PPScene getAndEjectPrevious() {
		PPScene previous = null;
		/* if a previous scene exists, don't return null */
		if (!previousScenes.empty()) 
			previous = previousScenes.pop();
		
		return previous;
	}
	
	/**
	 * Will return the previous navigated scene
	 * (For use with back arrow in application)
	 * @return Previous PPScene; if no scene exists, then returns null
	 */
	protected PPScene getPrevious() {
		PPScene previous = null;
		/* if a previous scene exists, don't return null */
		if (!previousScenes.empty())
			previous = previousScenes.peek();
		
		return previous;
	}
	
	/**
	 * Log a state and add it to the scene stack
	 * @param scene PPScene to add to stack
	 */
	protected void logState(PPScene scene) {
		previousScenes.push(scene);
	}
	
	/**
	 * Returns true if there is an available logged state
	 * @return True or false
	 */
	protected boolean isEmpty() {
		return previousScenes.empty();
	}

}
