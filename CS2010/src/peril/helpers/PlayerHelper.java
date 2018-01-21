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

/**
 * A helper class for {@link Game} which encapsulates the behaviours of the list
 * of {@link SlickPlayer}s.
 * 
 * @author Joshua_Eddy
 *
 */
public class PlayerHelper {

	/**
	 * The maximum number of {@link SlickPlayer}s in the game at any given time.
	 */
	public static final int MAX_PLAYERS = 4;

	/**
	 * The minimum number of {@link SlickPlayer}s in the game at any given time.
	 */
	public static final int MIN_PLAYERS = 2;

	/**
	 * Contains all the objectives that a {@link SlickPlayer} can attain in the
	 * game.
	 */
	public final List<Challenge> challenges;

	/**
	 * Holds all the {@link SlickPlayer}s in this {@link Game}.
	 */
	private final Map<Integer, ModelPlayer> playing;
	
	private final List<ModelPlayer> losers;

	/**
	 * The {@link Game} this {@link PlayerHelper} helps.
	 */
	private final Game game;

	/**
	 * The {@link SlickPlayer} who's turn it is.
	 */
	private Iterator<Integer> index;

	/**
	 * The current {@link SlickPlayer}.
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
		this.index = playing.keySet().iterator();
		this.challenges = new LinkedList<>();	
		this.losers = new LinkedList<>();
	}

	/**
	 * Gets the number of {@link SlickPlayer}s in the {@link Game}.
	 * 
	 * @return <code>int</code>
	 */
	public int numberOfPlayers() {
		return playing.size();
	}

	/**
	 * Performs the specified task on each {@link SlickPlayer} in the {@link Game}.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	public void forEach(Consumer<ModelPlayer> task) {
		playing.values().forEach(task);
	}

	/**
	 * Adds a {@link Challenge} for this {@link Game}.
	 * 
	 * @param challenge
	 *            NOT NULL
	 */
	public void addChallenge(Challenge challenge) {

		if (challenge == null) {
			throw new NullPointerException("Challenge cannot be null.");
		}

		this.challenges.add(challenge);
		game.view.updateChallenges();
	}

	/**
	 * Iterates thought all the available {@link Challenge}s to see if the specified
	 * {@link SlickPlayer} has completed them or not.
	 * 
	 * @param currentState
	 *            The current {@link CoreGameState} of the {@link Game}.
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
	 * Sets a specified {@link SlickPlayer} as a loser which removes it from the
	 * {@link Game#players} and adds it to the podium in the {@link EndState}.
	 * 
	 * @param defendingPlayer
	 *            {@link SlickPlayer} number that has lost.
	 */
	public void setLoser(ModelPlayer defendingPlayer) {

		// Add the player to the podium and remove it from the players in play.
		game.view.addLoser(defendingPlayer);
		
		losers.add(defendingPlayer);

		playing.remove(defendingPlayer.number);

		index = playing.keySet().iterator();

		// Bring the iterator through the lit until it finds the player.
		while (index.hasNext()) {
			if (index.next() == current.number) {
				break;
			}
		}

	}

	public ModelPlayer getPlayer(int player) {
		return playing.get(player);
	}

	/**
	 * Retrieves whether or not a specified player is in this {@link Game}.
	 * 
	 * @param model
	 *            {@link SlickPlayer} number
	 * @return <code>boolean</code>
	 */
	public boolean isPlaying(int number) {
		return playing.containsKey(number);
	}

	/**
	 * Retrieves the {@link SlickPlayer} who's current turn it is.
	 * 
	 * @return {@link SlickPlayer}
	 */
	public ModelPlayer getCurrent() {
		return current;
	}

	/**
	 * Set the current {@link SlickPlayer} in this {@link PlayerHelper}.
	 * 
	 * @param player
	 *            {@link SlickPlayer}
	 */
	public void setCurrent(ModelPlayer player) {

		if (!playing.containsKey(player.number)) {
			throw new IllegalArgumentException("Player " + player.number + " is not playing.");
		}

		current = player;

	}

	/**
	 * Iterates to the next player.
	 */
	public void nextPlayer() {

		if (!index.hasNext()) {
			index = playing.keySet().iterator();
			game.endRound();
		}

		current = playing.get(index.next());

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
	 * Adds a {@link SlickPlayer} to the set of {@link SlickPlayer}s that are
	 * playing.
	 * 
	 * @param player
	 */
	public void addPlayer(ModelPlayer player) {

		if (!playing.containsKey(player.number)) {

			playing.put(player.number, player);
			index = playing.keySet().iterator();
			current = playing.get(index.next());

		}
	}

	/**
	 * Retrieves a random {@link SlickPlayer} that is currently playing.
	 * 
	 * @return
	 */
	public ModelPlayer getRandomPlayer() {

		int pos = new Random().nextInt(playing.size());

		int i = 0;
		for (ModelPlayer p : playing.values()) {

			if (pos == i) {
				return p;
			}

			i++;
		}

		return null;
	}

	/**
	 * Gives the current {@link SlickPlayer} reinforcements based on the number of
	 * countries they own.
	 * 
	 * @param model
	 *            {@link SlickPlayer}
	 */
	public void reinforceCurrent() {

		ModelPlayer player = getCurrent();

		// Scale reinforcements with round progression.
		int roundScale = game.getRoundNumber() != 0 ? game.getRoundNumber() * 2 : 1;

		if (player.getCountriesRuled() < 12) {
			player.distributableArmy.add(3 * roundScale);
		} else {
			player.distributableArmy.add((player.getCountriesRuled() * roundScale) / 3);
		}
	}

}
