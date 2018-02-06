package peril.helpers;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import peril.Challenge;
import peril.Game;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;

/**
 * A helper class for game which encapsulates the behaviours of the list of
 * {@link ModelPlayer}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.01
 * @since 2018-02-06
 *
 */
public class PlayerHelper {

	/**
	 * The maximum number of {@link ModelPlayer}s in the game at any given time.
	 */
	public static final int MAX_PLAYERS = 4;

	/**
	 * The minimum number of {@link ModelPlayer}s in the game at any given time.
	 */
	public static final int MIN_PLAYERS = 2;

	/**
	 * Contains all the {@link Challenge}s that a {@link ModelPlayer} can complete
	 * in the game.
	 */
	public final List<Challenge> challenges;

	/**
	 * Holds all the {@link ModelPlayer}s in this game.
	 */
	private final Map<Integer, ModelPlayer> playing;

	/**
	 * Holds all the {@link ModelPlayer}s that have been eliminated from the game in
	 * the order they were eliminated.
	 */
	private final List<ModelPlayer> losers;

	/**
	 * The {@link Game} this {@link PlayerHelper} helps.
	 */
	private final Game game;

	/**
	 * The {@link ModelPlayer} who's turn it is.
	 */
	private Iterator<Integer> index;

	/**
	 * The current {@link ModelPlayer}.
	 */
	private ModelPlayer current;

	/**
	 * Constructs a new {@link PlayerHelper}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link PlayerHelper} helps.
	 */
	public PlayerHelper(Game game) {
		this.game = game;

		this.playing = new LinkedHashMap<>();
		this.challenges = new LinkedList<>();
		this.losers = new LinkedList<>();

		this.index = playing.keySet().iterator();
	}

	/**
	 * Gets the number of {@link ModelPlayer}s currently in the game.
	 * 
	 * @return <code>int</code>
	 */
	public int numberOfPlayers() {
		return playing.size();
	}

	/**
	 * Performs the specified task on each {@link ModelPlayer} in the game.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	public void forEach(Consumer<ModelPlayer> task) {
		playing.values().forEach(task);
	}

	/**
	 * Adds a {@link Challenge} for this game.
	 * 
	 * @param challenge
	 *            NOT NULL
	 */
	public void addChallenge(Challenge challenge) {

		if (challenge == null) {
			throw new NullPointerException("Challenge cannot be null.");
		}

		// Add the challenge then update the views collection of challenges.
		challenges.add(challenge);
		game.view.updateChallenges();
	}

	/**
	 * Iterates thought all the available {@link Challenge}s to see if the current
	 * {@link ModelPlayer} has completed them or not.
	 */
	public void checkChallenges() {

		// Holds the completed challenges
		List<Challenge> toRemove = new LinkedList<>();

		// Iterate though all the objectives to see if the the current player has
		// completed them.
		challenges.forEach(challenge -> {

			// If the current player has completed the challenge remove it from the list of
			// available challenges.
			if (challenge.hasCompleted(getCurrent(), game.board)) {
				toRemove.add(challenge);
				game.view.showToolTip(challenge.completed());
			}
		});

		// Remove the completed challenges.
		toRemove.forEach(challenge -> challenges.remove(challenge));

		// Refresh the challenge view
		game.view.updateChallenges();
	}

	/**
	 * Sets a specified {@link ModelPlayer} as a loser.
	 * 
	 * @param player
	 *            {@link ModelPlayer} that has lost.
	 */
	public void setLoser(ModelPlayer player) {

		// Add the defending player to the list of losers
		losers.add(player);

		// Remove the player from the losers.
		playing.remove(player.number);

		index = playing.keySet().iterator();

		// Bring the iterator through the lit until it finds the player.
		while (index.hasNext()) {
			if (index.next() == current.number) {
				break;
			}
		}

	}

	/**
	 * Retrieves the {@link ModelPlayer} by number.
	 * 
	 * @param player
	 *            Number of the player.
	 * @return {@link ModelPlayer}
	 */
	public ModelPlayer getPlayer(int player) {
		return playing.get(player);
	}

	/**
	 * Retrieves whether or not a player specified by number is in this game. If the
	 * player has lost then this will return false.
	 * 
	 * @param model
	 *            {@link ModelPlayer} number
	 * @return <code>boolean</code>
	 */
	public boolean isPlaying(int number) {
		return playing.containsKey(number);
	}

	/**
	 * Retrieves the {@link ModelPlayer} who's current turn it is.
	 * 
	 * @return {@link ModelPlayer}
	 */
	public ModelPlayer getCurrent() {
		return current;
	}

	/**
	 * Set the current {@link ModelPlayer}.
	 * 
	 * @param player
	 *            {@link ModelPlayer}
	 */
	public void setCurrent(ModelPlayer player) {

		if (!playing.containsKey(player.number)) {
			throw new IllegalArgumentException("Player " + player.number + " is not playing.");
		}

		current = player;

	}

	/**
	 * Iterates to the next {@link ModelPlayer} in the sequence.
	 */
	public void nextPlayer() {

		// Move to the next player
		if (!index.hasNext()) {
			index = playing.keySet().iterator();
			game.endRound();
		}

		// Set the index player as the current.
		current = playing.get(index.next());

		// Reinforce the current.
		reinforceCurrent();

		// Check the challenges going into the next round
		checkChallenges();
	}

	/**
	 * Empties the {@link List} of playing {@link SlickPlayer}s.
	 */
	public void emptyPlaying() {
		playing.clear();
		index = playing.keySet().iterator();
	}

	/**
	 * Adds a {@link ModelPlayer} to the set of {@link ModelPlayer}s that are
	 * playing.
	 * 
	 * @param player
	 */
	public void addPlayer(ModelPlayer player) {

		// If the player is not already playing
		if (!playing.containsKey(player.number)) {

			playing.put(player.number, player);
			index = playing.keySet().iterator();
			current = playing.get(index.next());

		}
	}

	/**
	 * Retrieves a random {@link ModelPlayer} that is currently playing.
	 * 
	 * @return {@link ModelPlayer}
	 */
	public ModelPlayer getRandomPlayer() {

		// A random position in the set of players.
		int pos = new Random().nextInt(playing.size());

		// Iterate over each player.
		int index = 0;
		for (ModelPlayer player : playing.values()) {

			// If the index is the same then the current is the random player.
			if (pos == index) {
				return player;
			}

			index++;
		}

		throw new IllegalStateException("There are no currently active players.");
	}

	/**
	 * Gives the current {@link ModelPlayer} reinforcements based on the number of
	 * countries they own.
	 * 
	 * @param model
	 *            {@link ModelPlayer}
	 */
	public void reinforceCurrent() {

		ModelPlayer player = getCurrent();

		// Scale reinforcements with round progression.
		final int roundScale = game.getRoundNumber() != 0 ? game.getRoundNumber() * 2 : 1;

		// The factor is based on player progression.
		final int factor = (player.getCountriesRuled() < 12 ? 9 : player.getCountriesRuled()) / 3;

		player.distributableArmy.add(ModelArmy.generateUnits(factor * roundScale));

	}

}
