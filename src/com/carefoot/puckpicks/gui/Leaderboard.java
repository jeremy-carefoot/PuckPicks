package com.carefoot.puckpicks.gui;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Leaderboard extends PPScene {
	
	private boolean displayPlayers;
	
	public Leaderboard() {
		super("leaderboards.css");
		displayPlayers = true;
		setScene(new Scene(assembleContent(), 500d, 500d));
	}
	
	public Leaderboard(boolean displayPlayers) {
		super("leaderboards.css");
		this.displayPlayers = displayPlayers;
		setScene(new Scene(assembleContent(), 500d, 500d));
	}
	
	private Parent assembleContent() {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		Text title = buildTitleText("Leaderboard");
		
		vbox.getChildren().add(title);
		return vbox;
	}
	
	private Text buildTitleText(String text) {
		Text title = new Text(text.toUpperCase());
		title.setId("title-text");
		title.setTextAlignment(TextAlignment.CENTER);
		return title;
	}

}
