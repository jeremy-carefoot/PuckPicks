package com.carefoot.puckpicks.gui;


import java.net.URI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.AsyncTaskQueue;
import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.DataRequest;
import com.carefoot.puckpicks.data.GoalieRequest;
import com.carefoot.puckpicks.data.SkaterRequest;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A scene that displays a leaderboard with leading skaters in a selected category
 * 
 * 	Contains buttons to select either Goalie or Player leaderboards
 * 	Contains dropdowns to select the leading category and how many skaters to display
 * 
 * Extends the PPScene class for use with PPApplication instance
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
			"100"
	};
	private final DataManager dataManager; 		// DataManager instance for getting skater data

	private boolean displayPlayers;
	private AsyncTaskQueue imageRenderer = null; 		// instance of an AsyncTaskQueue used for rendering images in the list
	private ComboBox<String> categorySelect = null; 
	private ComboBox<String> limitSelect = null;
	private ListView<HBox> list = new ListView<>(); 	// nodes cannot be changed once scene is created, therefor we dynamically modify one ListView node
	
	public Leaderboard() {
		super("leaderboards.css");
		this.dataManager = new DataManager();
		displayPlayers = true;
	}
	
	public Leaderboard(boolean displayPlayers) {
		super("leaderboards.css");
		this.dataManager = new DataManager();
		this.displayPlayers = displayPlayers;
	}
	
	@Override
	public void build() {		
		Scene scene = new Scene(assembleContent(), 500d, 500d);		
		setScene(scene);
		buildSkaterList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
	}
	
	// Assembles scene content
	private Parent assembleContent() {
		VBox vbox = new VBox();
		HBox titleSection = buildTitleSection("Leaderboard");
		HBox buttonSection = buildButtonSection();
		HBox dropdownSection = buildDropdownSection();
		Region spacer1 = PPGui.filler(false, 15);
		Region spacer2 = PPGui.filler(false, 15);
		
		vbox.getChildren().addAll(titleSection, buttonSection, spacer1, dropdownSection, spacer2, list);

		return vbox;
	}
	
	// Constructs title 
	private HBox buildTitleSection(String text) {
		Text title = PPGui.textWithStyle(text, "title");
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setId("title-box");
		hbox.getChildren().add(title);
		return hbox;
	}
	
	// Constructs horizontal button section
	private HBox buildButtonSection() {
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.CENTER);
		
		Button viewPlayers = new Button("Player Leaderboard");
		PPAnimation.animateButtonHover(viewPlayers, 100);
		viewPlayers.setOnAction((e) -> {
			onButtonPress(viewPlayers);
		});

		Button viewGoalies = new Button("Goalie Leaderboard");
		PPAnimation.animateButtonHover(viewGoalies, 100);
		viewGoalies.setOnAction((e) -> {
			onButtonPress(viewGoalies);
		});

		viewPlayers.getStyleClass().add("leaderboard-button");
		viewGoalies.getStyleClass().add("leaderboard-button");

		hbox.getChildren().addAll(viewPlayers, viewGoalies);	
		return hbox;
	}

	// Builds the code run when a button is clicked
	private void onButtonPress(Button b) {
		displayPlayers = (b.getText() == "Player Leaderboard"); // whether player or goalie leaderboard
		updateCategorySelect();
		updatePlayerList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
	}
	
	// Builds the menu section with DropDowns
	private HBox buildDropdownSection() {
		HBox hbox = new HBox(20);
		hbox.setAlignment(Pos.CENTER);
		
		categorySelect = new ComboBox<>();
		updateCategorySelect();
		categorySelect.setOnAction(e -> {
			updatePlayerList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
		});
		
		limitSelect = new ComboBox<>();
		limitSelect.getItems().addAll(listLimits);
		limitSelect.setValue(Integer.toString(DataRequest.DEFAULT_LIMIT));
		limitSelect.setOnAction(e -> {
			updatePlayerList(categorySelect.getValue(), Integer.parseInt(limitSelect.getValue()));
		});
		
		hbox.getChildren().addAll(categorySelect, limitSelect);
		return hbox;
	}
	
	// Updates category select to currently selected leaderboard type (players or goalies)
	private void updateCategorySelect() {
		EventHandler<ActionEvent> action = categorySelect.getOnAction();
		categorySelect.setOnAction(null);

		categorySelect.getItems().clear();
		categorySelect.getItems().addAll(displayPlayers ? SkaterRequest.categories() : GoalieRequest.categories());
		categorySelect.setValue(displayPlayers ? SkaterRequest.DEFAULT_CATEGORY : GoalieRequest.DEFAULT_CATEGORY);

		categorySelect.setOnAction(action);
		// TODO fix so setValue does not set off ActionEvent
	}
	
	// Builds the skater ListView (for scene initialization)
	private void buildSkaterList(String category, int limit) {
		list.setId("player-list");
		VBox.setVgrow(list, Priority.ALWAYS);
		updatePlayerList(category, limit);
	}

	// Updates the player elements in the list view
	// Assumes list has already been initialized and configured 
	// Runs async on another thread
	private void updatePlayerList(String category, int limit) {
		enableListLoading();
		imageRenderer = new AsyncTaskQueue(4);
		new Thread(() -> {	
			List<HBox> playerElements = buildPlayerElements(category, limit);
			imageRenderer.flush(); // start rendering images async
			Platform.runLater(() -> {
				list.getItems().clear();
				list.getItems().addAll(playerElements);
			});
		}).start();
	}
	
	// Builds the player elements for use in the player list
	private List<HBox> buildPlayerElements(String category, int limit) {
		List<HBox> list = new ArrayList<>();
		JSONObject players = dataManager.submitRequest(displayPlayers ? new SkaterRequest(category, limit) : new GoalieRequest(category, limit));

		int counter = 1;
		for (HashMap<String, String> player : SkaterRequest.parseJSONResponse(players)) {
			HBox hbox = buildSkaterElement(player, category, counter);
			list.add(hbox);
			counter++;
		}
		
		return list;
	}
	
	// Builds a list element for provided skater
	// rank is the integer ranking of the skater in the given category
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
	
	// Builds the skater list element name and number column 
	private VBox buildSkaterInfo(HashMap<String, String> player) {
		VBox playerinfo = new VBox();
		playerinfo.setAlignment(Pos.CENTER_LEFT);
		
		Text playerName = PPGui.textWithID(player.get("firstName") + " " + player.get("lastName"), "player-name");
		Text playerNumber = PPGui.textWithStyle("#" + player.get("sweaterNumber"), "h2");
		Text team = PPGui.textWithStyle(player.get("teamAbbrev"), "h2-dark-italic");
		
		playerinfo.getChildren().addAll(playerName, playerNumber, team);
		return playerinfo;
	}
	
	// Creates the skater list element stats column
	private VBox buildSkaterStats(HashMap<String, String> player, String category) {
		VBox stats = new VBox();
		stats.setId("player-element-stats");
		stats.setAlignment(Pos.CENTER);
		
		Text cat = PPGui.textWithStyle(PuckPicks.capitalizeFirst(category), "h2-dark");
		Text stat = PPGui.textWithStyle(player.get("value"), "title");
		
		stats.getChildren().addAll(cat, stat);
		return stats;
	}
	
	// Renders the skater list element headshot asynchronously
	// Adds the render to an async task queue
	private void renderHeadshotAsync(HashMap<String, String> player, Pane node, int index) {
		imageRenderer.add(() -> {			
			ImageView headshot;
			try {
				headshot = new ImageView(new Image(new URI(player.get("headshot")).toURL().openStream()));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}		
			
			headshot.setFitHeight(75d);
			headshot.setFitWidth(75d);
			headshot.setPreserveRatio(true);
			Platform.runLater(() -> {				
				node.getChildren().add(index, headshot);
			});
		});
	}
	
	// Renders a loading spinner in the skater list
	// For use while async loading 
	private void enableListLoading() {
		list.getItems().clear();
		HBox hbox = new HBox();
		hbox.setPrefHeight(list.getHeight());
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(LoadingScene.buildLoadingSpinner());
		list.getItems().add(hbox);
	}

}
