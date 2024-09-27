package com.carefoot.puckpicks.gui;


import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.SkaterRequest;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Leaderboard extends PPScene {
	
	private boolean displayPlayers;
	private final DataManager dataManager;
	
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
	}
	
	// Assembles scene content
	private Parent assembleContent() {
		VBox vbox = new VBox();
		HBox titleSection = buildTitleSection("Leaderboard");
		HBox buttonSection = buildButtonSection();
		Region spacer = PPGui.filler(false, 15);
		
		ListView<HBox> playerList = buildPlayerList("points", 20);
		
		vbox.getChildren().addAll(titleSection, buttonSection, spacer, playerList);
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
		Button viewGoalies = new Button("Goalie Leaderboard");
		PPAnimation.animateButtonHover(viewGoalies, 100);
		viewPlayers.getStyleClass().add("leaderboard-button");
		viewGoalies.getStyleClass().add("leaderboard-button");

		hbox.getChildren().addAll(viewPlayers, viewGoalies);	
		return hbox;
	}
	
	// Builds the skater ListView
	private ListView<HBox> buildPlayerList(String category, int limit) {
		ListView<HBox> list = new ListView<HBox>();		
		list.getItems().addAll(buildPlayerElements(category, limit));
		list.setId("player-list");
		VBox.setVgrow(list, Priority.ALWAYS);
		return list;
	}
	
	// Builds the player elements for use in the player list
	private List<HBox> buildPlayerElements(String category, int limit) {
		List<HBox> list = new ArrayList<>();
		JSONObject players = dataManager.submitRequest(new SkaterRequest(category, limit));

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
			ImageView headshot = renderHeadshot(player);

			hbox.getChildren().addAll(pos, headshot, namenumber, filler, stats);
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
	
	// Renders the skater list element headshot
	private ImageView renderHeadshot(HashMap<String, String> player) {
		ImageView headshot;
		try {
			headshot = new ImageView(new Image(new URI(player.get("headshot")).toURL().openStream()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		
		headshot.setFitHeight(75d);
		headshot.setFitWidth(75d);
		headshot.setPreserveRatio(true);
		return headshot;
	}

}
