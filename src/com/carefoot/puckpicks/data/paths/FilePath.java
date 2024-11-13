package com.carefoot.puckpicks.data.paths;

/**
 * Enum for storing relative filepath to different resource files
 * @author jeremycarefoot
 */
public enum FilePath {
	
	LOAD_SPINNER("loading_spinner.png"),
	APP_LOGO("logo.png"),
	APP_ICON("icon.png"),
	ERROR_ICON("error.png"),
	BACK_ARROW("back_arrow.png"),
	BACK_ARROW_HOVER("back_arrow_hover.png"),
	ACCOUNT_ICON("account_icon.png"),
	ACCOUNT_ICON_HOVER("account_icon_hover.png"),
	CANCEL_ICON("cancel_icon.png"),
	CANCEL_ICON_HOVER("cancel_icon_hover.png"),
	YAHOO_LOGIN_BUTTON("yahoo_signin_button.png"),
	DEFAULT_STYLESHEET("style/default.css"),
	DATA_DIRECTORY(System.getProperty("user.home") + "/.puckpicks");
	
	private String path;
	
	private FilePath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return path;
	}
}
