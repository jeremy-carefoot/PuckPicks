package com.carefoot.puckpicks.gui.scenes;


import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.AsyncTaskQueue;
import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.requests.NHLGoalieRequest;
import com.carefoot.puckpicks.data.requests.NHLSkaterRequest;
import com.carefoot.puckpicks.gui.PPAnimation;
import com.carefoot.puckpicks.gui.PPGui;
import com.carefoot.puckpicks.main.Log;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * A scene that displays a leaderboard with leading skaters in a selected category.
 * 
 * 	Contains buttons to select either Goalie or Player leaderboards.
 * 	Contains dropdowns to select the leading category and how many skaters to display.
 * 
 * Extends the PPScene class for use with PPApplication instance.
 * 
 * @author jeremycarefoot
 *
 */
public class Leaderboard extends PPScene {
	
	private static final String[] listLimits = {// Player element display limits for dropdown menu
			"5",
			"10",
			"15",
			"20",
			"50",
			"100",
			"500"
	};

	private boolean displayPlayers;
	private AsyncTaskQueue imageRenderer = null; 		// instance of an AsyncTaskQueue used for rendering images in the list
	private ComboBox<String> categorySelect = null; 
	private ComboBox<String> limitSelect = null;
	private ListView<HBox> list = new ListView<>(); 	// nodes cannot be changed once scene is created, therefor we dynamically modify one ListView node
	
	/**
	 * Default constructor; builds a leaderboard that will display players by default
	 */
	public Leaderboard() {
		super("leaderboards.css", true, true);
		displayPlayers = true;
	}
	
	/**
	 * Constructor that specifies whether to display players or goalies by default
	 * @param displayPlayers true to display players, false to display goalies
	 */
	public Leaderboard(boolean displayPlayers) {
		super("leaderboards.css", true, true);
		this.displayPlayers = displayPlayers;
	}
	
	@Override
	public void build() {		
		createScene(assembleContent(), 500d, 500d);		
		buildSkaterList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
	}
	
	/**
	 * Builds the scene content and returns a Parent node for the Scene object.
	 * All components are constructed here (except for the player list).
	 * @return Parent node with scene content
	 */
	private Parent assembleContent() {	
		VBox vbox = new VBox();
		HBox titleSection = PPGui.buildTitleSection("Leaderboards");
		HBox buttonSection = buildButtonSection();
		HBox dropdownSection = buildDropdownSection();
		Region spacer1 = PPGui.filler(false, 15);
		Region spacer2 = PPGui.filler(false, 15);
		
		vbox.getChildren().addAll(titleSection, buttonSection, spacer1, dropdownSection, spacer2, list);
		return vbox;
	}
	
	/**
	 * Builds the section with player/goalie selection buttons
	 */
	private HBox buildButtonSection() {
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.CENTER);
		
		Button viewPlayers = new Button("Player Leaderboard");
		PPAnimation.animateHover(viewPlayers, 150);
		viewPlayers.setOnAction((e) -> {
			onButtonPress(viewPlayers);
		});

		Button viewGoalies = new Button("Goalie Leaderboard");
		PPAnimation.animateHover(viewGoalies, 150);
		viewGoalies.setOnAction((e) -> {
			onButtonPress(viewGoalies);
		});

		viewPlayers.getStyleClass().add("leaderboard-button");
		viewGoalies.getStyleClass().add("leaderboard-button");

		hbox.getChildren().addAll(viewPlayers, viewGoalies);	
		return hbox;
	}

	/**
	 * Method executed when the Player/Goalie selection buttons are clicked in the scene.
	 * @param b Button object clicked
	 */
	private void onButtonPress(Button b) {
		displayPlayers = (b.getText() == "Player Leaderboard"); // whether player or goalie leaderboard
		updateCategorySelect();
		updatePlayerList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
	}
	
	/**
	 *  Builds the section containing the dropdowns
	 */
	private HBox buildDropdownSection() {
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.CENTER);
		
		categorySelect = new ComboBox<>();
		updateCategorySelect(); 		// ensure category select has correct select options
		categorySelect.setOnAction(e -> {// when new value is selected, update the player list
			updatePlayerList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
		});
		
		limitSelect = new ComboBox<>();
		limitSelect.getItems().addAll(listLimits);
		limitSelect.setValue(Integer.toString(DataRequest.DEFAULT_LIMIT));
		limitSelect.setOnAction(e -> {// when new value is selected, update the player list
			updatePlayerList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
		});
		
		hbox.getChildren().addAll(categorySelect, limitSelect);
		return hbox;
	}
	
	/**
	 * Updates the scene category select dropdown menu.
	 * Ensures that the correct selection options are visible depending on whether Player or Goalie leaderboard is selected.
	 */
	private void updateCategorySelect() {
		EventHandler<ActionEvent> action = categorySelect.getOnAction();
		/*
		 * we temporarily disable the dropdown action event because setValue() triggers it
		 * we don't want to trigger the event while changing the dropdown selection options
		 */
		categorySelect.setOnAction(null); 		

		categorySelect.getItems().clear();
		categorySelect.getItems().addAll(displayPlayers ? NHLSkaterRequest.categories() : NHLGoalieRequest.categories());
		categorySelect.setValue(displayPlayers ? NHLSkaterRequest.DEFAULT_CATEGORY : NHLGoalieRequest.DEFAULT_CATEGORY);

		categorySelect.setOnAction(action); 		// re-enable action event
	}
	
	/**
	 * Builds the skater list object
	 * Should only be run when scene is initially built
	 */
	private void buildSkaterList(String category, int limit) {
		list.setId("player-list");
		VBox.setVgrow(list, Priority.ALWAYS);
		updatePlayerList(category, limit);
	}

	/**
	 * Updates the skater list to contain skaters relevent to the provided category/limit values.
	 * Performed asynchronously.
	 * 
	 * @param category Category to submit in the skater data request
	 * @param limit How many skaters to fetch
	 */
	private void updatePlayerList(String category, int limit) {
		enableListLoading();	
		imageRenderer = new AsyncTaskQueue(4);
		new Thread(() -> {	
			List<HBox> playerElements = buildPlayerElements(category, limit);
			imageRenderer.flush(); 	// start rendering images async
			Platform.runLater(() -> {// update GUI on main JavaFX thread
				list.getItems().clear();
				list.getItems().addAll(playerElements);
			});
		}).start();
	}
	
	/**
	 * Prepares a List of HBox objects that are constructed skater gui list elements
	 * 
	 * @param category Category to submit in the skater data request
	 * @param limit How many players to fetch
	 * @return List of HBox, where each HBox represents a skater
	 */
	private List<HBox> buildPlayerElements(String category, int limit) {
		List<HBox> list = new ArrayList<>();
		JSONObject players;
		try {// attempt to grab data
			players = DataManager.submitRequest(displayPlayers ? new NHLSkaterRequest(category, limit) : new NHLGoalieRequest(category, limit));
		} catch (Exception e) {
			/*
			 * If there is an error, return the list with an error message and log
			 */
			Log.log("Could not grab player data: " + e.getMessage(), Log.ERROR);
			list.add(getErrorDisplay());
			return list;
		}

		int rank = 1;
		for (HashMap<String, String> player : displayPlayers ? 
				NHLSkaterRequest.parseJSONResponse(players) : NHLGoalieRequest.parseJSONResponse(players, category)) {// parse JSON response use appropriate method 
			HBox hbox = buildSkaterElement(player, category, rank);
			list.add(hbox);
			rank++;
		}
		
		return list;
	}
	
	/**
	 * Builds an HBox object that is a correctly formatted list element for the skater list
	 * 
	 * @param player HashMap with skater details
	 * @param category Category for display on list element
	 * @param rank Ranking of skater
	 * @return Formatted HBox object
	 */
	private HBox buildSkaterElement(HashMap<String, String> player, String category, int rank) {		
			HBox hbox = new HBox(20);
			hbox.setAlignment(Pos.CENTER_LEFT);
			
			Text pos = PPGui.textWithStyle(Integer.toString(rank), "h2-dark");
			VBox namenumber = buildSkaterInfo(player);
			Region filler = PPGui.filler(true);
			VBox stats = buildSkaterStats(player, category);
			renderHeadshotAsync(player, hbox, 1);

			hbox.getChildren().addAll(pos, namenumber, filler, stats);
			return hbox;
	}
	
	/**
	 * Constructs column of player info for individual list elements
	 * @param player HashMap of player information 
	 * @return Correctly formatted VBox object
	 */
	private VBox buildSkaterInfo(HashMap<String, String> player) {
		VBox playerinfo = new VBox();
		playerinfo.setAlignment(Pos.CENTER_LEFT);
		String position = player.get("position");
		
		Text playerName = PPGui.textWithID(player.get("firstName") + " " + player.get("lastName"), "player-name");
		Text playerNumber = PPGui.textWithStyle("#" + player.get("sweaterNumber")
			+ " - " + position + (position.equalsIgnoreCase("R") || position.equalsIgnoreCase("L") ? "W" : ""), "h2"); 	// if position is R or L, append W to indicate winger
		Text team = PPGui.textWithStyle(player.get("teamAbbrev"), "h2-dark");

		playerinfo.getChildren().addAll(playerName, playerNumber, team);
		return playerinfo;
	}
	
	/**
	 * Builds a VBox column of the players numerical stat for individual list elements
	 * @param player HashMap of player information
	 * @param category Category the stat is respective too
	 * @return Correctly formatted VBox object
	 */
	private VBox buildSkaterStats(HashMap<String, String> player, String category) {
		VBox stats = new VBox();
		stats.setId("player-element-stats");
		stats.setAlignment(Pos.CENTER);
		
		Text cat = PPGui.textWithStyle(category, "h2-dark");
		Text stat = PPGui.textWithStyle(player.get("value"), "title");
		
		stats.getChildren().addAll(cat, stat);
		return stats;
	}
	
	/**
	 * Renders a player headshot image asychronously, and adds into specified JavaFX node when completed
	 * 
	 * @param player HashMap with player information
	 * @param node JavaFX node where image is added when rendering is complete
	 * @param index Index of node to insert the image at
	 */
	private void renderHeadshotAsync(HashMap<String, String> player, Pane node, int index) {
		imageRenderer.add(() -> {// image rendering task added to a queue
			ImageView headshot;
			try {
				headshot = PPGui.image(new URI(player.get("headshot")).toURL().openStream(), 75, 75, true);
			} catch (Exception e) {
				Log.log("Could not render player headshot: " + e.getMessage(), Log.ERROR);
				return;
			}		
			
			Platform.runLater(() -> {// when done rendering use JavaFX main thread to update GUI		
				node.getChildren().add(index, headshot);
			});
		});
	}
	
	/**
	 * Enables the loading spinner on the skater list part of the scene.
	 * Lifetime of the loading spinner is until the list is updated.
	 */
	private void enableListLoading() {
		/*
		 * Loading spinner is essentially a list element with a height of the entire list itself.
		 * This is because we can't change layout of Root scene, so we must concurrently modify list
		 */
		list.getItems().clear();
		HBox hbox = new HBox();
		hbox.setPrefHeight(list.getHeight());
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(LoadingScene.buildLoadingSpinner());
		list.getItems().add(hbox);
	}
	
	/**
	 * Builds an HBox object with an error message
	 * @return Formatted HBox object
	 */
	private HBox getErrorDisplay() {
		HBox hbox = new HBox();
		VBox vbox = new VBox();

		vbox.setPrefHeight(list.getHeight());
		vbox.setAlignment(Pos.CENTER);
		hbox.setAlignment(Pos.CENTER);

		vbox.getChildren().addAll(PPGui.errorSymbol(70, 70), PPGui.textWithStyle("Error fetching data", "h2-dark"));
		hbox.getChildren().add(vbox);
		return hbox;
	}

}
