package com.carefoot.puckpicks.data.stats;

import com.carefoot.puckpicks.data.paths.FilePath;

/**
 * Basic enum to evaluate a player's recent performance increase or decrease
 * @author jeremycarefoot
 */
public enum PlayerTrend {
	HEAVILY_DECREASING("Heavily Lacking", FilePath.TREND_HEAVY_DECREASE), 
	DECREASING("Lacking", FilePath.TREND_DECREASING), 
	NEUTRAL("Neutral", FilePath.TREND_NEUTRAL), 
	INCREASING("Improving", FilePath.TREND_INCREASING), 
	HEAVILY_INCREASING("Heavily Improving", FilePath.TREND_HEAVY_INCREASE), 
	NONE("N/A", null);
	
	public static final double NEUTRAL_SIG_LEVEL = 0.2;	// max percent difference for neutral evaluation
	public static final double HEAVILY_SIG_LEVEL = 2.0; 	// min percent difference for "heavily" evaluation
	
	private String displayName;
	private FilePath image;
	
	private PlayerTrend(String displayName, FilePath image) {
		this.displayName = displayName;
		this.image = image;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public FilePath getImagePath() {
		return image;
	}
}
