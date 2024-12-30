package com.carefoot.puckpicks.gui.components;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.carefoot.puckpicks.authentication.AuthenticationHandler;
import com.carefoot.puckpicks.authentication.OAuthentication;
import com.carefoot.puckpicks.authentication.PPUser;
import com.carefoot.puckpicks.data.exceptions.PPServerException;
import com.carefoot.puckpicks.data.exceptions.TokenNotFoundException;
import com.carefoot.puckpicks.data.paths.FilePath;
import com.carefoot.puckpicks.gui.PPAnimation;
import com.carefoot.puckpicks.gui.PPGui;
import com.carefoot.puckpicks.gui.scenes.LoadingScene;
import com.carefoot.puckpicks.main.AppLauncher;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * The AccountMenu is the side menu that slides-in and handles the UI required to log in 
 * @author jeremycarefoot
 */
public class AccountMenu {

	private final double menuWidth = 250d; 		// width of the side menu
	
	private AnchorPane container;
	private VBox contents;
	private AuthenticationHandler authHandler;
	
	/**
	 * Create a new Account sliding menu.
	 * Menu must be added to top of current scene's children stack for proper visibility.
	 * @param authHandler AuthenticationHandler instance
	 */
	public AccountMenu(AuthenticationHandler authHandler) {
		container = new AnchorPane();
		contents = new VBox();
		container.getStylesheets().add(PuckPicks.getFilePath(FilePath.DEFAULT_STYLESHEET));
		this.authHandler = authHandler;
		buildMenu();
	}
	
	/**
	 * Construct the menu contents/components
	 */
	private void buildMenu() {
		contents.setAlignment(Pos.TOP_CENTER);
		contents.setMaxWidth(menuWidth);
		contents.setMinWidth(menuWidth);
		contents.getStyleClass().add("account-menu-container");
		
		/* anchor the menu so it takes up the entire right-hand of the window */
		AnchorPane.setRightAnchor(contents, 0d);	
		AnchorPane.setTopAnchor(contents, 0d);
		AnchorPane.setBottomAnchor(contents, 0d);
		
		container.setTranslateX(container.getLayoutX()+menuWidth);	// initially pane is off screen
		container.getStyleClass().add("account-menu-scene");
		container.getChildren().add(contents);
		
		addContents();
	}
	
	/**
	 * Adds the menu contents to the provided parent VBox node
	 * @param node Node to contain menu contents
	 */
	private void addContents() {
		/* add button to remove menu */
		StackPane cancel = PPGui.cancelIcon(25d, 25d);
		HBox cancelWrapper = new HBox(cancel);
		cancelWrapper.setAlignment(Pos.CENTER_LEFT);

		cancel.getChildren().get(1).setOnMouseClicked((e) -> {// button listener
			AppLauncher.getApp().getPPScene().popNode(); 	// removes the account menu
		});

		contents.getChildren().addAll(cancelWrapper, LoadingScene.buildLoadingSpinner());
		
		new Thread(() ->	{// perform server communications async
			try {

				PPUser userInfo = authHandler.isLoggedIn();
				if (userInfo != null) {
					loadUserInfo(userInfo, "Logged in as:");
				} else {
					loadLoginButton("You are not signed in");
				}

			} catch (PPServerException e) {// can't communicate with PuckPicks server
				loadErrorMessage(PPServerException.displayErrorTitle + "\n" + PPServerException.displayErrorSubtitle);
			}
		}).start();
	}
	
	/**
	 * Load error message into the menu.
	 * Pops one node off of the account menu stack (to account for load spinner)
	 */
	private void loadErrorMessage(String message) {
		ImageView errorSymbol = PPGui.errorSymbol(45d, 45d);

		Text errorText = PPGui.textWithStyle(message, "h3-dark");
		errorText.setWrappingWidth(menuWidth);
		errorText.setTextAlignment(TextAlignment.CENTER);
		
		Platform.runLater(() -> {// modify container on javafx thread
			clearMenuContents();
			contents.getChildren().addAll(errorSymbol, PPGui.filler(false, 15d), errorText);
		});
	}
	
	/**
	 * Load user information into account menu (for when user is successfully logged in)
	 * @param user User information 
	 * @param titleText Title of side menu 
	 */
	private void loadUserInfo(PPUser user, String titleText) {
		Text title = PPGui.textWithStyle(titleText, "h1");
		Text userEmail = PPGui.textWithStyle(user.getEmail(), "h2-dark");
		Text userId = PPGui.textWithStyle("(ID: " + user.getUniqueID() + ")", "h4-darker");

		/* setup sign-out button */
		Button signOut = new Button("Sign Out");
		signOut.setId("sign-out-button");
		PPAnimation.animateHover(signOut, 100);
		signOut.setOnMouseClicked((e) -> {
			signOut();
		});
		
		
		Platform.runLater(() -> {
			clearMenuContents();
			contents.getChildren().addAll(title, userEmail, userId, PPGui.filler(false, 15), signOut);
		});
	}
	
	/**
	 * Load a login button into the menu
	 * @param titleText Title displayed on top of button
	 */
	private void loadLoginButton(String titleText) {
		Text title = PPGui.textWithStyle(titleText, "h2-dark");
		ImageView button = PPGui.imageResource(FilePath.YAHOO_LOGIN_BUTTON, 200, 200, true);
		PPAnimation.animateHover(button, 100);
		button.setOnMouseClicked((e) -> {
			newLogin();
		});
	
		Platform.runLater(() -> {
			clearMenuContents();
			contents.getChildren().addAll(title, PPGui.filler(false, 15d), button);
		});
	}

	/**
	 * Begin a new login process (log a user into their Yahoo account).
	 * Should only be started by a click on Yahoo login button.
	 */
	private void newLogin() {
		enableLoading();
		new Thread(() -> {
			try {

				OAuthentication oauth = new OAuthentication(authHandler);
				Platform.runLater(() -> openLoginWindow(oauth));

			} catch (PPServerException e) {
				loadErrorMessage(PPServerException.displayErrorTitle + "\n" + PPServerException.displayErrorSubtitle);
			} catch (NoSuchAlgorithmException e) {
				loadErrorMessage("Internal error: " + e.getMessage());
			}		
		}).start();
	}
	
	/**
	 * Open a login window that redirects to the Yahoo authorization URL.
	 * Configures a listener for when the login is complete.
	 * @param oauth OAuthentication request
	 */
	private void openLoginWindow(OAuthentication oauth) {
		BorderPane windowContainer = new BorderPane();
		StackPane cancel = PPGui.cancelIcon(25d, 25d);
		HBox cancelWrapper = new HBox(cancel);
		cancelWrapper.setStyle("-fx-background-color: rgb(50, 50, 50);");
		cancelWrapper.setAlignment(Pos.CENTER_LEFT);
		
		/* setup listener for when login is complete */
		cancel.getChildren().get(1).setOnMouseClicked((e) -> {
			AppLauncher.getApp().getPPScene().popNode();
			completeLogin(oauth);
		});
		
		/* setup webview */
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		webEngine.load(oauth.getAuthenticationUrl());
		
		windowContainer.setTop(cancelWrapper);
		windowContainer.setCenter(webView);
		AppLauncher.getApp().addNodeToScene(windowContainer);
	}
	
	/**
	 * Complete a started OAuthentication request.
	 * Should be called in listener for openLoginWindow()
	 * @param auth OAuthentication request
	 */
	private void completeLogin(OAuthentication auth) {
		try {
			auth.completeAuthentication();
			loadUserInfo(authHandler.isLoggedIn(), "Login Successful");
		} catch (TokenNotFoundException e) {
			loadErrorMessage("Could not complete login\n(Do not close the window until login is complete)");
		} catch (PPServerException | IOException e) {
			loadErrorMessage("Login failure: " + e.getMessage());
		}	
	}
	
	/**
	 * Sign a user out of their Yahoo account.
	 * Should be called when the "Sign Out" button is clicked
	 */
	private void signOut() {		
		enableLoading();
		authHandler.signOut();
		loadLoginButton("Signed out successfully");
	}
	
	/**
	 * Clears all contents/nodes out of the side menu,
	 * <b>EXCEPT</b> for the top menubar node
	 */
	private void clearMenuContents() {
		int size = contents.getChildren().size();
		for (int i = size-1; i > 0; i--) {
			contents.getChildren().remove(i);
		}
	}
	
	/**
	 * Enables the loading spinner inside of the side menu.
	 * Does not change until contents are updated
	 */
	private void enableLoading() {
		clearMenuContents();
		contents.getChildren().add(LoadingScene.buildLoadingSpinner());
	}
	
	/**
	 * Translates the menu onscreen (plays an animation) that renders the menu visible
	 */
	public void showMenu() {
		TranslateTransition translation = PPAnimation.horizontalTranslation(container, 300d, container.getLayoutX()); 		// move back to proper onscreen location
		translation.play();
	}
	
	/**
	 * Get the container node of the menu
	 * @return AnchorPane node
	 */
	public AnchorPane getNode() {
		return container;
	}
}
