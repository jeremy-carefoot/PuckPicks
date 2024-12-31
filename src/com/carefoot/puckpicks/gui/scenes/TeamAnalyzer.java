package com.carefoot.puckpicks.gui.scenes;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.carefoot.puckpicks.data.AsyncTaskQueue;
import com.carefoot.puckpicks.data.exceptions.NotAuthenticatedException;
import com.carefoot.puckpicks.data.exceptions.PPServerException;
import com.carefoot.puckpicks.data.paths.FilePath;
import com.carefoot.puckpicks.data.requests.fantasy.*;
import com.carefoot.puckpicks.data.stats.PlayerTrend;
import com.carefoot.puckpicks.data.stats.RecentPerformanceTest;
import com.carefoot.puckpicks.gui.PPGui;
import com.carefoot.puckpicks.main.Log;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;

/**
 * TeamAnalyzer tool GUI scene
 * Note: The jargon of an error being "handled" means that the error has been presented on the client and no operations should continue.
 * (in the context of this class)
 * 
 * TODO better description here and logging
 * @author jeremycarefoot
 */
public class TeamAnalyzer extends PPScene {
	
	private static final String GENERIC_REQUEST_ERROR = "Could not fetch team information from Yahoo";	// error title when unable to complete a Yahoo request and cause is not known
	
	private VBox root;
	private int baseNodeCount;	 // how many nodes are in the scene when not loaded
	private Text loadStatus = null; 	// loading status text display
	private ComboBox<String> teamSelect = null; // Fantasy team dropdown menu
	private HashMap<String, Team> teamLookup = null; // Store league keys for each team in ComboBox
	private AsyncTaskQueue imageRenderer; 	// for async image loading
	
	/**
	 * Default constructor
	 * Constructs scene
	 */
	public TeamAnalyzer() {
		super("team_analyzer.css", true, true);
	}

	@Override
	public void build() {
		assembleContent();	// synchronous construction
		createScene(root, 550d, 500d);
		baseNodeCount = root.getChildren().size(); 	// update base node count after full scene construction; ready for async loading
		
		asyncLoadContent();
	}
	
	/**
	 * Builds main components of scene (all synchronous)
	 * @return Root node
	 */
	private void assembleContent() {
		root = new VBox();
		
		HBox titleSection = PPGui.buildTitleSection("Team Analyzer");
		
		root.getChildren().add(titleSection);
	}
	
	/**
	 * Load Fantasy-Team related content asynchronously and add to scene
	 * 
	 * @param teamKey Team to load in analysis display (leave null for default)
	 */
	private void asyncLoadContent() {
		enableLoading();
		
		new Thread(() -> {	
			if (buildDropdownMenu()) {// if the dropdown menu construction was successful (error would have been handled)
				ListView<HBox> playerList = loadPlayerList();
				
				if (playerList.getItems().size() != 0) {// means player list construction was successful (error would have been handled)
					HBox dropdownContainer = new HBox(teamSelect);	// basic alignment container for dropdown menu
					dropdownContainer.setAlignment(Pos.CENTER);

					/* update GUI */
					Platform.runLater(() -> {// on FX thread	
						clearLoadedNodes();
						root.getChildren().addAll(dropdownContainer, PPGui.filler(false, 20), playerList);
					});
				} 
			}
		}).start();
	}
	
	/**
	 * Constructs a list of players from the provided team key.
	 * If unsuccessful, returns an empty ListView node and handles errors
	 * 
	 * @param teamKey Key of team to grab player data
	 * @return ListView node with player data
	 */
	private ListView<HBox> loadPlayerList() {
		ListView<HBox> playerList = new ListView<>();
		playerList.setId("player-list");
		Team team = teamLookup.get(teamSelect.getValue());
		
		try {
			
			updateLoadStatus("Fetching player data");
			Roster roster = new Roster(team.getTeamKey());
			List<PlayerStatCollection> previousStats = 
					PlayerStatCollection.asyncGetPreviousStats(team, roster, RecentPerformanceTest.PREVIOUS_STAT_DAYS, 5000L); 	// get previous player stats
			
			if (previousStats != null) {// safety check (ensure stats were fetched accordingly)

				/* compute player performance data */
				updateLoadStatus("Analyzing player performance");
				RecentPerformanceTest analyzer = new RecentPerformanceTest(roster.getContent(), previousStats);
				
				/* update and build list content for GUI*/
				List<HBox> playerElements = buildPlayerElements(roster, analyzer);
				imageRenderer.flush(); 	// start rendering images async (image renders queued during list construction)
				playerList.getItems().addAll(playerElements);

				return playerList; // finally, return completed and concurrently loading list
				
			} else {
				Platform.runLater(() -> {// all GUI updates on Fx thread
					enableError("Something went wrong:", "Could not fetch player statistics", true);
				});
			}
			

		} catch (PPServerException | NotAuthenticatedException | IOException e) {
			handleDataGrabErrors(e);
		}
		
		return playerList;
	}
	
	/**
	 * Builds all of the player elements for a list.
	 * Adds their headshot images to imageRenderer queue
	 * 
	 * @param roster Roster of players
	 * @param performanceData RecentPerformanceTest with player data
	 * @return List of HBox elements
	 */
	private List<HBox> buildPlayerElements(Roster roster, RecentPerformanceTest performanceData) {
		imageRenderer = new AsyncTaskQueue(5);
		List<HBox> elements = new ArrayList<>();
		
		/* execute performance test to sort players and get data */
		HashMap<String, Pair<Double, Double>> playerPointData = performanceData.sortPlayers();
		HashMap<String, PlayerTrend> playerTrends = performanceData.getPlayerTrends();
		
		int playerRank = 1;
		for (Player player : roster.getContent()) {
			String playerKey = player.getPlayerKey();
			HBox playerElement = buildPlayerElement(player, playerPointData.get(playerKey), playerTrends.get(playerKey), playerRank);
			elements.add(playerElement);
			playerRank++;
		}
		
		return elements;
	}
	
	/**
	 * Builds a player element HBox (for use in list)
	 * 
	 * @param player
	 * @param pointData Pair<Double, Double> in format (avg. fppg, total fp)
	 * @param trend PlayerTrend enum value
	 * @param rank Rank among other players in roster
	 * @return Constructed HBox
	 */
	private HBox buildPlayerElement(Player player, Pair<Double, Double> pointData, PlayerTrend trend, int rank) {
		HBox container = new HBox(20);
		container.setAlignment(Pos.CENTER_LEFT);
		
		Text rankText = PPGui.textWithStyle(Integer.toString(rank), "h2-dark");
		VBox nameNumber = buildPlayerBasicInfo(player);
		VBox trendNode = buildPlayerTrend(player, trend);
		VBox statNode = buildPlayerStatInfo(player, pointData, RecentPerformanceTest.PREVIOUS_STAT_DAYS);
		renderAsync(player.getHeadshotUrl(), container, 1);
		
		container.getChildren().addAll(rankText, nameNumber, PPGui.filler(true), trendNode, statNode);
		return container;
	}
	
	/**
	 * Builds a players basic information and puts in VBox
	 * @param player
	 * @return Constructed VBox
	 */
	private VBox buildPlayerBasicInfo(Player player) {
		VBox playerInfo = new VBox();
		playerInfo.setAlignment(Pos.CENTER_LEFT);
		
		String positions = player.getPositions().stream()
				.map(Enum::name)
				.collect(Collectors.joining(",")); 	// functionally list player positions in comma-seperated-lis
		String sweaterNumber = Integer.toString(player.getUniformNumber());

		Text playerName = PPGui.textWithID(player.getName(), "player-name");
		Text positionsText = PPGui.textWithStyle(positions + " - #" + sweaterNumber, "h2");
		Text team = PPGui.textWithStyle(player.getTeamAbbreviation(), "h2-dark");
		
		playerInfo.getChildren().addAll(playerName, positionsText, team);
		return playerInfo;
	}
	
	/**
	 * Builds a player trend information and puts in VBox
	 * @param player 
	 * @param trend PlayerTrend enum
	 * @return Constructed VBox
	 */
	private VBox buildPlayerTrend(Player player, PlayerTrend trend) {
		VBox playerTrend = new VBox();
		playerTrend.setAlignment(Pos.CENTER);
		
		Text title = PPGui.textWithStyle("Trend", "h4-darker");
		Text trendText = PPGui.textWithStyle(trend.getDisplayName(), "h3-bold");

		playerTrend.getChildren().addAll(title, trendText);
		
		if (trend != PlayerTrend.NONE)
			renderAsync(trend.getImagePath(), playerTrend, 1);
		else
			playerTrend.getChildren().add(1, PPGui.filler(false, 10));
		
		return playerTrend;
	}

	/**
	 * Builds a players stat information and puts in VBox
	 * @param player
	 * @param data Pair (avg. fppg, total points) with point data
	 * @param days How many days is this data from
	 * @return Constructed VBox
	 */
	private VBox buildPlayerStatInfo(Player player, Pair<Double, Double> data, int days) {
		VBox statInfo = new VBox();
		statInfo.setAlignment(Pos.CENTER);

		Text timelineTitle = PPGui.textWithID("Past " + Integer.toString(days) + " Days", "timeline-title");
		Text avgTitle = PPGui.textWithStyle("Avg. FP Per Game:", "h4-dark");
		Text avgPoints = PPGui.textWithStyle(String.format("%.2f", data.getKey()), "h3-bold");
		Text totalTitle = PPGui.textWithStyle("Total FP: " + String.format("%.2f", data.getValue()) , "h4-dark");
		
		statInfo.getChildren().addAll(timelineTitle, avgTitle, avgPoints, totalTitle);
		return statInfo;
	}
	
	/**
	 * Add an image rendering to the imageRenderer 
	 * @param url Image URL
	 * @param container Image container
	 * @param insertIndex Container position to insert image
	 */
	private void renderAsync(String url, Pane container, int insertIndex) {
		imageRenderer.add(() -> {// add image rendering to queue
			ImageView image;
			try {
				
				image = PPGui.image(new URI(url).toURL().openStream(), 75, 75, true);
			} catch (Exception e) {
				Log.log("Could not render player headshot: " + e.getMessage(), Log.ERROR);
				return;
			}		
			
			Platform.runLater(() -> {// when done rendering use JavaFX main thread to update GUI		
				container.getChildren().add(insertIndex, image);
			});
		});
	}

	/**
	 * Add an image rendering to the imageRenderer 
	 * @param url Image URL
	 * @param container Image container
	 * @param insertIndex Container position to insert image
	 */
	private void renderAsync(FilePath url, Pane container, int insertIndex) {
		imageRenderer.add(() -> {// add image rendering to queue
			ImageView image;
			try {
				
				image = PPGui.imageResource(url, 50, 50, true);
			} catch (Exception e) {
				Log.log("Could not render player headshot: " + e.getMessage(), Log.ERROR);
				return;
			}		
			
			Platform.runLater(() -> {// when done rendering use JavaFX main thread to update GUI		
				container.getChildren().add(insertIndex, image);
			});
		});
	}
	
	/**
	 * Constructs the dropdown menu if it is not initialized.
	 * In case of a handled error, returns false
	 * @return Whether the dropdown menu is successfully built
	 */
	private boolean buildDropdownMenu() {		
		if (teamSelect == null) {
			teamSelect = new ComboBox<>();
			teamLookup = new HashMap<>();

			updateLoadStatus("Fetching team data");
			Pair<TeamCollection, LeagueCollection> response = getDropdownTeamInfo();
			if (response == null) // if a handled error occurred
				return false;

			TeamCollection teams = response.getKey();
			LeagueCollection leagues = response.getValue();
			
			if (teams.size() == 0) {// throw error message if there are no teams found
				enableError("No teams found!", "", false);
				return false;
			}
			
			teams.getContent().forEach((team) -> {	
				String itemEntry = team.getName();

				for (League league : leagues.getContent()) {// if a league for the team is found, we will include league name in the dropdown menu entry (this should always happen)
					if (league.getLeagueKey().equals(team.getLeagueKey())) {
						itemEntry += " - " + league.getName();
						break;
					}
				}
				
				teamSelect.getItems().add(itemEntry);
				teamLookup.put(itemEntry, team);
			});
			
			teamSelect.setValue(teamSelect.getItems().get(0)); 	// set default dropdown value
			teamSelect.setOnAction((e) -> {// listener for dropdown menu change
				asyncLoadContent();
			});
		}
		
		return true;
	}
	
	/**
	 * Get team and league information for the dropdown menu.
	 * Launches 2 separate async requests and waits until both are complete.
	 * Returns null if not successful. (handles error)
	 * 
	 * @return Pair with a TeamCollection and LeagueCollection
	 */
	private Pair<TeamCollection, LeagueCollection> getDropdownTeamInfo() {

		/*
		 * We will fetch league and team information in two separate requests asynchronously
		 * When both are complete we continue 
		 */	
		
		CompletableFuture<LeagueCollection> fetchLeagues = CompletableFuture.supplyAsync(() -> {
			try {
				return new LeagueCollection();
			} catch (PPServerException | NotAuthenticatedException | IOException e) {
				handleDataGrabErrors(e);
				return null;
			}
		});
		
		CompletableFuture<TeamCollection> fetchTeams = CompletableFuture.supplyAsync(() -> {
			try {
				return new TeamCollection();
			} catch (PPServerException | NotAuthenticatedException | IOException e) {
				handleDataGrabErrors(e);
				return null;
			}
		});
		
		/* wait for both requests to complete */
		CompletableFuture<Void> getBoth = CompletableFuture.allOf(fetchLeagues, fetchTeams);
		try {
			getBoth.get();
			LeagueCollection leagues = fetchLeagues.get();
			TeamCollection teams = fetchTeams.get();

			if (leagues != null && teams != null) {// pre-handled error occurred during requests				
				return new Pair<TeamCollection,LeagueCollection>(teams, leagues);
			}				
		} catch (InterruptedException | ExecutionException e) {
			enableError(GENERIC_REQUEST_ERROR + ":", e.getMessage(), true);
		}

		return null; // if not successful
	}
	
	/**
	 * Handles errors produced by Yahoo Fantasy data grab requests.
	 * Creates an error display in the application.
	 * 
	 * @param e Exception produced
	 */
	private void handleDataGrabErrors(Exception e) {		
		Platform.runLater(() -> {// update on FX thread
			if (e instanceof PPServerException) {
				enableError(PPServerException.displayErrorTitle, PPServerException.displayErrorSubtitle, true);
			} else if (e instanceof NotAuthenticatedException) {
				enableError("You are not signed in", "", false);
			} else {
				enableError(GENERIC_REQUEST_ERROR + ":" , e.getMessage(), true);
			}
		});
	}
	
	/**
	 * Clears scene and enables loading spinner
	 */
	private void enableLoading() {
		clearLoadedNodes();
		
		/* build spinner */
		VBox spinnerContainer = new VBox(LoadingScene.buildLoadingSpinner());
		VBox.setVgrow(spinnerContainer, Priority.ALWAYS);
		spinnerContainer.setAlignment(Pos.CENTER);
		this.loadStatus = PPGui.textWithStyle("", "h3");
		
		spinnerContainer.getChildren().add(loadStatus);
		root.getChildren().addAll(spinnerContainer);
	}
	
	/**
	 * Update the loading spinner status (text just beneath the spinner).
	 * If scene isn't loading, does nothing
	 * @param status New text for status (... is automatically appended to this)
	 */
	private void updateLoadStatus(String status) {
		Platform.runLater(() -> {// update status text on FX thread
			if (loadStatus != null)
				loadStatus.setText(status + "...");			
		});
	}
	
	/**
	 * Clears scene and enables an error display
	 * 
	 * @param title Error title
	 * @param message Error message
	 * @param retryButton whether to include a retry button
	 */
	private void enableError(String title, String message, boolean retryButton) {
		clearLoadedNodes();
		teamSelect = null; 	// reset dropdown menu
		teamLookup = null; // reset dropdown menu league key map
		
		/* setup container */
		VBox errorContainer = new VBox(PPGui.errorSymbol(50, 50));
		VBox.setVgrow(errorContainer, Priority.ALWAYS);
		errorContainer.setAlignment(Pos.CENTER);
		
		/* setup error title */
		Text errorTitle = PPGui.textWithStyle(title, "h2-dark");
		errorTitle.setWrappingWidth(root.getWidth());
		errorTitle.setTextAlignment(TextAlignment.CENTER);

		/* setup error message */
		Text errorMessage = PPGui.textWithStyle(message, "h3-dark");	
		errorMessage.setWrappingWidth(root.getWidth());
		errorMessage.setTextAlignment(TextAlignment.CENTER);

		errorContainer.getChildren().addAll(errorTitle, errorMessage);

		if (retryButton) {// if a "retry" button should be included in error
			Button button = PPGui.retryButton(() -> {
				asyncLoadContent();
			});
			errorContainer.getChildren().addAll(PPGui.filler(false, 10), button);
		}
		
		root.getChildren().add(errorContainer);
	}
	
	/**
	 * Clears loaded nodes (that are not base nodes)
	 */
	private void clearLoadedNodes() {		
		int nodeCount = root.getChildren().size();
		
		for (int i = nodeCount-1; i >= baseNodeCount; i--) {// loop backward, remove nodes until only base nodes remain
			root.getChildren().remove(i);
		}
	}

}
