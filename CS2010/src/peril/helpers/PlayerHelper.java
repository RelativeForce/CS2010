package peril.helpers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import peril.Challenge;
import peril.Game;
import peril.Player;
import peril.ui.states.gameStates.CoreGameState;
import peril.ui.states.menuStates.EndState;

/**
 * A helper class for {@link Game} which encapsulates the behaviours of the list
 * of {@link Player}s.
 * 
 * @author Joshua_Eddy
 *
 */
public class PlayerHelper {

	/**
	 * Holds all the {@link Player}s in this {@link Game}.
	 */
	private List<Player> players;

	/**
	 * The {@link Player} who's turn it is.
	 */
	private int currentPlayerIndex;

	/**
	 * Contains all the objectives that a {@link Player} can attain in the game.
	 */
	private List<Challenge> challenges;

	/**
	 * The {@link Game} this {@link PlayerHelper} helps.
	 */
	private Game game;

	/**
	 * Constructs a new {@link PlayerHelper}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link PlayerHelper} helps.
	 */
	public PlayerHelper(Game game) {
		this.game = game;
		this.currentPlayerIndex = 0;
		this.players = new ArrayList<>();
	}

	/**
	 * Gets the number of {@link Player}s in the {@link Game}.
	 * 
	 * @return <code>int</code>
	 */
	public int size() {
		return players.size();
	}

	/**
	 * Performs the specified task on each {@link Player} in the {@link Game}.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	public void forEach(Consumer<Player> task) {
		players.forEach(task);
	}

	/**
	 * Set the {@link List} of {@link Challenge}s for this {@link Game}.
	 * 
	 * @param challenges
	 *            {@link List} NOT NULL
	 */
	public void setChallenges(List<Challenge> challenges) {

		if (challenges == null) {
			throw new NullPointerException("Challenge list cannot be null.");
		}

		this.challenges = challenges;
	}

	/**
	 * Iterates thought all the available {@link Challenge}s to see if the specified
	 * {@link Player} has completed them or not.
	 * 
	 * @param currentPlayer
	 *            {@link Player}
	 */
	public void checkChallenges(Player currentPlayer) {

		// Holds the completed challenges
		List<Challenge> toRemove = new LinkedList<>();

		// Iterate though all the objectives to see if the the current player has
		// completed them.
		for (Challenge challenge : challenges) {

			// If the current player has completed the challenge remove it from the list of
			// available challenges.
			if (challenge.hasCompleted(currentPlayer, game.board)) {
				toRemove.add(challenge);

				if (game.getCurrentState() instanceof CoreGameState) {
					((CoreGameState) game.getCurrentState()).show(challenge);
				}
			}
		}

		// Remove the completed challenges.
		toRemove.forEach(challenge -> challenges.remove(challenge));
	}

	/**
	 * Sets a specified {@link Player} as a loser which removes it from the
	 * {@link Game#players} and adds it to the podium in the {@link EndState}.
	 * 
	 * @param player
	 *            {@link Player} that has lost.
	 */
	public void setLoser(Player player) {

		// If the loser player is before the current player in the list, reduce the
		// player index to account for the player being removed and the list's size
		// changing.
		if (this.currentPlayerIndex > players.indexOf(player)) {
			this.currentPlayerIndex--;
		}

		// Add the player to the podium and remove it from the players in play.
		game.states.end.addPlayerToPodium(player);
		players.remove(player);

	}

	/**
	 * Retrieves whether or not a specified player is in this {@link Game}.
	 * 
	 * @param player
	 *            {@link Player}
	 * @return <code>boolean</code>
	 */
	public boolean isPlaying(Player player) {

		// Iterate through all the players in the game.
		for (Player currentPlayer : players) {

			// If the player is playing then return true.
			if (currentPlayer.equals(player)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Assigns an array of {@link Player} to this {@link Game}.
	 * 
	 * @param players
	 *            {@link Player}s in the {@link Game}. NOT EMPTY
	 */
	public void set(List<Player> players) {

		if (players == null || players.isEmpty()) {
			throw new IllegalArgumentException("players array cannot be empty");
		}

		this.players = players;
		this.currentPlayerIndex = 0;
	}

	/**
	 * Adds a {@link Player} to this {@link PlayerHelper}. Will do nothing if the
	 * {@link Player} is already contained in the {@link PlayerHelper}.
	 * 
	 * @param player
	 */
	public void add(Player player) {
		if (!players.contains(player)) {
			players.add(player);
		}
	}

	/**
	 * Retrieves the {@link Player} who's current turn it is.
	 * 
	 * @return {@link Player}
	 */
	public Player getCurrent() {

		if (players == null) {
			throw new NullPointerException("List of playing Players is null!");
		}

		return players.get(currentPlayerIndex);
	}

	/**
	 * Iterates to the next player.
	 */
	public void nextPlayer() {

		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

		if (game.getRoundNumber() > 0) {
			checkChallenges(getCurrent());
		}

		reinforce(getCurrent());

		if (currentPlayerIndex == 0) {
			game.endRound();
		}
	}

	/**
	 * Get the {@link Player} at the specified index from the {@link List} of
	 * {@link Player}s.
	 * 
	 * @param index
	 *            <code>int</code>
	 * @return {@link Player}
	 */
	public Player get(int index) {
		return players.get(index);
	}

	/**
	 * Resets all the countries owned and army size of all the {@link Player}s to
	 * zero;
	 */
	public void reset() {
		// Reset the all players number of countries to zero.
		players.forEach(player -> {
			player.setCountriesRuled(0);
			player.setTotalArmySize(0);
		});
	}

	/**
	 * Gives the specified {@link Player} reinforcements based on the number of
	 * countries they own.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void reinforce(Player player) {

		if (player.getCountriesRuled() < 12) {
			player.setDistributableArmySize(player.getDistributableArmySize() + 3);
		} else {
			player.setDistributableArmySize(player.getDistributableArmySize() + (player.getCountriesRuled() / 3));
		}
	}

}
