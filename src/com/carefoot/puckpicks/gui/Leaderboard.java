package com.carefoot.puckpicks.gui;

import com.carefoot.puckpicks.data.DataManager;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Leaderboard extends PPScene {
	
	private boolean displayPlayers;
	private final DataManager dataManager;
	
	public Leaderboard() {
		super("leaderboards.css");
		displayPlayers = true;
		setScene(new Scene(assembleContent(), 500d, 500d));		
		this.dataManager = new DataManager();
	}
	
	public Leaderboard(boolean displayPlayers) {
		super("leaderboards.css");
		this.displayPlayers = displayPlayers;
		setScene(new Scene(assembleContent(), 500d, 500d));
		this.dataManager = new DataManager();
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
		viewGoalies.getStyleClass()	.add("leaderboard-button");

		hbox.getChildren().addAll(viewPlayers, viewGoalies);	
		return hbox;
	}
	
	// TODO finish this
	private ListView<HBox> buildPlayerList() {
		ListView<HBox> list = new ListView<HBox>();		
		list.setId("player-list");
		return list;
	}

}
