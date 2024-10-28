package com.carefoot.puckpicks.data;

/**
 * Enum for storing relative filepath to different resource files
 * @author jeremycarefoot
 */
public enum ResourcePath {
	
	LOAD_SPINNER("loading_spinner.png"),
	APP_LOGO("logo.png"),
	APP_ICON("icon.png"),
	ERROR_ICON("error.png"),
	BACK_ARROW("back_arrow.png"),
	BACK_ARROW_HOVER("back_arrow_hover.png"),
	ACCOUNT_ICON("account_icon.png"),
	ACCOUNT_ICON_HOVER("account_icon_hover.png");
	
	private String path;
	
	private ResourcePath(String path) {
		this.path = path;
	}
	
	public String path() {
		return path;
	}
}
