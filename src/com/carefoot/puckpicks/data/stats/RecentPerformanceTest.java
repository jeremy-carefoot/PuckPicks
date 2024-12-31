package com.carefoot.puckpicks.data.stats;

import java.util.HashMap;
import java.util.List;

import com.carefoot.puckpicks.data.requests.fantasy.Player;
import com.carefoot.puckpicks.data.requests.fantasy.PlayerStat;
import com.carefoot.puckpicks.data.requests.fantasy.PlayerStatCollection;
import com.carefoot.puckpicks.data.requests.fantasy.Statistic;

import javafx.util.Pair;

/**
 * Class used to test the recent performance of a given set of players.
 * 
 * Computes the average fantasy points per game (FPPG) for each player, then sorts the list of provided players in descending order according to this data.
 * Requires YahooFantasy data for the previous N days, and will totals are computed with given data.
 * @author jeremycarefoot
 */
public class RecentPerformanceTest {
		
	public static final int PREVIOUS_STAT_DAYS = 14; 	// how many previous days should be analyzed when calculating recent player performance
	
	private List<Player> players;
	private List<PlayerStatCollection> statData;
	private HashMap<String, Double> playerTotalPoints;
	private HashMap<String, Double> playerAveragePoints;
	private HashMap<String, Integer> playerGamesPlayed;
	
	/**
	 * Create a new performance test.
	 * Note: List of players and player statistic data must concern the same players (or else inaccurate results)
	 * 
	 * @param players List of players to sort
	 * @param stats Statistic data to use in sorting
	 */
	public RecentPerformanceTest(List<Player> players, List<PlayerStatCollection> stats) {
		this.players = players;
		this.statData = stats;
		calculatePerformanceTotals();
	}	
	
	/**
	 * Sorts the provided list of players, and returns a HashMap with player stat data.
	 * Players are ranked by highest average fantasy points per game first
	 * 
	 * @return HashMap where key is the playerKey, and value is a Pair including avg. points per game and total points per game (avg, total)
	 */
	public HashMap<String, Pair<Double, Double>> sortPlayers() {	
		/* Fill the output HashMap with average and total point data */
		HashMap<String, Pair<Double, Double>> output = assembleSortOutputMap();
		
		/* Sort ArrayList by average points per game */
		players.sort((player1, player2) -> 
			Double.compare(playerAveragePoints.get(player2.getPlayerKey()), playerAveragePoints.get(player1.getPlayerKey()))); 	// sort by average fantasy points per game in descending order
		
		return output;
	}
	
	/**
	 * Get PlayerTrends for all players based on the provided data.
	 * A PlayerTrend is an indicator of improvement (or lack thereof) of a players performance.
	 * Analyzes performance of first half of provided dates and compares them to second half, assigning an enum PlayerTrend value accordingly.
	 * 
	 * @return HashMap with [playerKey, PlayerTrend]
	 */
	public HashMap<String, PlayerTrend> getPlayerTrends() {
		sortStatsByDate();	// sort to assess change in performance over time
		
		HashMap<String, PlayerTrend> output = new HashMap<>();
		int middleBreak = (int) (statData.size() / 2);

		HashMap<String, Double> recentTotalPoints = new HashMap<>();
		HashMap<String, Integer> recentGamesPlayed = new HashMap<>();
		HashMap<String, Double> olderTotalPoints = new HashMap<>();
		HashMap<String, Integer> olderGamesPlayed = new HashMap<>();
		
		/* Gather totals segregating old and recent games for average comparison */
		for (int i = 0; i < statData.size(); i++) {// stat data is stored in list descending from most recent
			PlayerStatCollection statCollection = statData.get(i);
			
			if (statCollection == null) 	// safety check in-case some dates were not able to be retrieved
				continue;

			for (PlayerStat stat : statCollection.getContent()) {
				String playerKey = stat.getPlayerKey();

				if (i <= middleBreak) {// recent data
					double points = recentTotalPoints.getOrDefault(playerKey, 0d);
					int gamesPlayed = recentGamesPlayed.getOrDefault(playerKey, 0);
					
					/* update totals */
					points += stat.getStats().getOrDefault(Statistic.FANTASY_POINTS.getId(), 0d);
					gamesPlayed += stat.getStats().getOrDefault(Statistic.GAMES_PLAYED.getId(), 0d);
					
					recentTotalPoints.put(playerKey, points);
					recentGamesPlayed.put(playerKey, gamesPlayed);
					
				} else {// older data

					double points = olderTotalPoints.getOrDefault(playerKey, 0d);
					int gamesPlayed = olderGamesPlayed.getOrDefault(playerKey, 0);
					
					/* update totals */
					points += stat.getStats().getOrDefault(Statistic.FANTASY_POINTS.getId(), 0d);
					gamesPlayed += stat.getStats().getOrDefault(Statistic.GAMES_PLAYED.getId(), 0d);
					
					olderTotalPoints.put(playerKey, points);
					olderGamesPlayed.put(playerKey, gamesPlayed);
					
				}
			}
		}
		
		/* Now compare old and new averages per player to assess a Trend*/
		for (Player player : players) {
			String playerKey = player.getPlayerKey();
			int recentGames = recentGamesPlayed.getOrDefault(playerKey, 0);
			int olderGames = olderGamesPlayed.getOrDefault(playerKey, 0);
			
			double recentAveragePoints = (recentGames != 0 ? recentTotalPoints.getOrDefault(playerKey, 0d) / recentGames : 0d);
			double olderAveragePoints = (olderGames != 0 ? olderTotalPoints.getOrDefault(playerKey, 0d) / olderGames : 0d);
			output.put(playerKey, assessPlayerTrend(recentAveragePoints, olderAveragePoints));
		}
		
		return output;
	}
	
	/**
	 * Evaluates a recent average and an older average then determines a PlayerTrend based on pre-set epsilon values
	 * @param recentAverage
	 * @param olderAverage
	 * @return PlayerTrend enum value
	 */
	private PlayerTrend assessPlayerTrend(double recentAverage, double olderAverage) {
		double percentDifference = (Math.max(recentAverage, olderAverage) / Math.min(recentAverage, olderAverage)) - 1;
		
		if (Double.isNaN(percentDifference)) // indicates a 0/0 therefor no trend
			return PlayerTrend.NONE;
		
		if (percentDifference <= PlayerTrend.NEUTRAL_SIG_LEVEL) // neutral trend
			return PlayerTrend.NEUTRAL;
		else if (percentDifference >= PlayerTrend.HEAVILY_SIG_LEVEL) // heavily increasing/decreasing trend
			return recentAverage > olderAverage ? PlayerTrend.HEAVILY_INCREASING : PlayerTrend.HEAVILY_DECREASING;
		else	// just increasing or decreasing regularly
			return recentAverage > olderAverage ? PlayerTrend.INCREASING : PlayerTrend.DECREASING;
	}
	
	/**
	 * Sort the provided PlayerStatCollection data by date.
	 * Sorts the list in descending order in terms of occurrence (more recent first)
	 */
	private void sortStatsByDate() {
		this.statData.sort((collection1, collection2) -> collection2.getDate().compareTo(collection1.getDate()));
	}
	/**
	 * Calculate player total points, games played, and average points per game using provided data
	 */
	private void calculatePerformanceTotals() {
		playerTotalPoints = new HashMap<>();
		playerAveragePoints = new HashMap<>();
		playerGamesPlayed = new HashMap<>();
				
		/* First, loop through all statistic data for players and obtain totals */
		for (PlayerStatCollection statCollection : statData) {// loop through stat collections (one for each date)
			
			if (statCollection == null) 	// safety check in-case some dates were not able to be retrieved
				continue;

			for (PlayerStat stat : statCollection.getContent()) {// loop through each player's stat data for that date
				String playerKey = stat.getPlayerKey();
				double points = playerTotalPoints.getOrDefault(playerKey, 0d);
				int gamesPlayed = playerGamesPlayed.getOrDefault(playerKey, 0);
				
				/* update totals */
				points += stat.getStats().getOrDefault(Statistic.FANTASY_POINTS.getId(), 0d);
				gamesPlayed += stat.getStats().getOrDefault(Statistic.GAMES_PLAYED.getId(), 0d);
				
				playerTotalPoints.put(playerKey, points);
				playerGamesPlayed.put(playerKey, gamesPlayed);
			}
		}
		
		/* Loop to compute player average point data */
		playerTotalPoints.entrySet().forEach((entry) -> {
			String playerKey = entry.getKey();
			double points = entry.getValue();
			int gamesPlayed = playerGamesPlayed.getOrDefault(playerKey, 0);
			
			if (gamesPlayed == 0) // if zero games played, then zero fantasy points
				playerAveragePoints.put(playerKey, 0d);
			else
				playerAveragePoints.put(playerKey, points / gamesPlayed);
		});
	}
	
	/**
	 * Creates a data HashMap including statistic data used in player analysis
	 * @param playerPoints Player point totals
	 * @param playerGamesPlayed Player games played totals 
	 * @return HashMap including average fantasy points per game and total fantasy points (playerKey, (avg FPPG, total FP))
	 */
	private HashMap<String, Pair<Double, Double>> assembleSortOutputMap() {		
		HashMap<String, Pair<Double, Double>> output = new HashMap<>();
		
		playerTotalPoints.entrySet().forEach((entry) -> {
			String playerKey = entry.getKey();
			double points = entry.getValue();
			double averagePoints = playerAveragePoints.getOrDefault(playerKey, 0d);
			output.put(playerKey, new Pair<Double, Double>(averagePoints, points));
		});
		
		return output;
	}
}
