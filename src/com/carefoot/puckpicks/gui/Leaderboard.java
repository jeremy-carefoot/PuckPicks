package com.carefoot.puckpicks.gui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.SkaterRequest;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
		ListView<HBox> playerList = buildPlayerList();
		
		vbox.getChildren().addAll(titleSection, buttonSection, playerList);
		return vbox;
	}
	
	// Constructs title 
	private HBox buildTitleSection(String text) {
		Text title = new Text(text.toUpperCase());
		title.setId("title-text");
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
	
	// TODO finish this
	private ListView<HBox> buildPlayerList() {
		ListView<HBox> list = new ListView<HBox>();		
		list.getItems().addAll(buildPlayerElements("points", 20));
		list.setId("player-list");
		VBox.setVgrow(list, Priority.ALWAYS);
		return list;
	}
	
	// TODO finish this
	private List<HBox> buildPlayerElements(String category, int limit) {
		List<HBox> list = new ArrayList<>();
		JSONObject players = dataManager.submitRequest(new SkaterRequest(category, limit));

		for (HashMap<String, String> player : SkaterRequest.parseJSONResponse(players)) {
			HBox hbox = new HBox();
			VBox namenumber = buildNameNumber(player);
			ImageView headshot = renderHeadshot(player);

			hbox.getChildren().addAll(headshot, namenumber);
			list.add(hbox);
		}
		
		return list;
	}
	
	// Builds the player name and number column in list view
	private VBox buildNameNumber(HashMap<String, String> player) {
		VBox namenumber = new VBox();
		namenumber.setAlignment(Pos.CENTER_LEFT);
		Text playerName = new Text(player.get("firstName") + " " + player.get("lastName"));
		Text playerNumber = new Text("#" + player.get("sweaterNumber"));
		playerName.setId("player-name");
		playerNumber.setId("player-no");
		namenumber.getChildren().addAll(playerName, playerNumber);
		return namenumber;
	}
	
	// Renders the player headshot
	private ImageView renderHeadshot(HashMap<String, String> player) {		
		ImageView headshot = new ImageView(new Image(player.get("headshot")));
		headshot.setFitHeight(75d);
		headshot.setFitWidth(75d);
		headshot.setPreserveRatio(true);
		return headshot;
	}

}
