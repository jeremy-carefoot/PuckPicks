package com.carefoot.puckpicks.data.requests.fantasy;

import java.util.ArrayList;

import com.carefoot.puckpicks.authentication.AuthenticationHandler;
import com.carefoot.puckpicks.main.AppLauncher;

/**
 * Collection of Yahoo Fantasy Resources
 * @author jeremycarefoot
 *
 * @param <T> Type of Object to store
 */
public abstract class YahooCollection<T> {
	
	private ArrayList<T> array;
	protected AuthenticationHandler handler;
	
	public YahooCollection() {
		array = new ArrayList<>();
		handler = AppLauncher.getApp().getAuthHandler();
	}
	
	/**
	 * Add items to ArrayList
	 * Can only be done by subclasses
	 * @param entry item to add
	 */
	protected void addContent(T entry) {
		array.add(entry);
	}
	
	/**
	 * Get an element 
	 * @param index of element
	 * @return element
	 */
	public T get(int index) {
		return array.get(index);
	}
	
	/**
	 * Get the size of the array (number of elements)
	 * @return size
	 */
	public int size() {
		return array.size();
	}
	
	/**
	 * Fetch ArrayList of contents
	 * @return
	 */
	public ArrayList<T> getContent() {
		return array;
	}
	
	/**
	 * String representation
	 */
	public String toString() {
		return array.toString();
	}

}
