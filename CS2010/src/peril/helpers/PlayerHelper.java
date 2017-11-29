package peril.helpers;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import org.newdawn.slick.Color;

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
	 * The default set of {@link Player}s.
	 */
	private final Map<Integer, Player> defaultPlayers;

	/**
	 * Holds all the {@link Player}s in this {@link Game}.
	 */
	private List<Player> playing;

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
		this.playing = new ArrayList<>();

		this.defaultPlayers = new IdentityHashMap<Integer, Player>();
		defaultPlayers.put(1, new Player(1, Color.red));
		defaultPlayers.put(2, new Player(2, Color.blue));
		defaultPlayers.put(3, new Player(3, Color.green));
		defaultPlayers.put(4, new Player(4, Color.pink.multiply(Color.pink)));
	}

	/**
	 * Gets the number of {@link Player}s in the {@link Game}.
	 * 
	 * @return <code>int</code>
	 */
	public int size() {
		return playing.size();
	}

	/**
	 * Performs the specified task on each {@link Player} in the {@link Game}.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	public void forEach(Consumer<Player> task) {
		playing.forEach(task);
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
	 * Get the {@link Challenge} at the specified {@link List}.
	 * 
	 * @return {@link challenges}
	 */
	public List<Challenge> getChallenges() {
		return challenges;
	}

	/**
	 * Iterates thought all the available {@link Challenge}s to see if the specified
	 * {@link Player} has completed them or not.
	 * 
	 * @param currentState
	 *            The current {@link CoreGameState} of the {@link Game}.
	 */
	public void checkChallenges(CoreGameState currentState) {

		// Holds the completed challenges
		List<Challenge> toRemove = new LinkedList<>();

		// Iterate though all the objectives to see if the the current player has
		// completed them.
		challenges.forEach(challenge -> {

			// If the current player has completed the challenge remove it from the list of
			// available challenges.
			if (challenge.hasCompleted(getCurrent(), game.board)) {
				toRemove.add(challenge);
				currentState.show(challenge.toString());
			}
		});

		// Remove the completed challenges.
		toRemove.forEach(challenge -> challenges.remove(challenge));
	}

	/**
	 * Sets a specified {@link Player} as a loser which removes it from the
	 * {@link Game#players} and adds it to the podium in the {@link EndState}.
	 * 
	 * @param player
	 *            {@link Player} number that has lost.
	 */
	public void setLoser(int number) {

		Player player = defaultPlayers.get(number);

		// If the loser player is before the current player in the list, reduce the
		// player index to account for the player being removed and the list's size
		// changing.
		if (this.currentPlayerIndex > playing.indexOf(player)) {
			this.currentPlayerIndex--;
		}

		// Add the player to the podium and remove it from the players in play.
		game.states.end.addToTop(player);
		playing.remove(player);

	}

	/**
	 * Retrieves whether or not a specified player is in this {@link Game}.
	 * 
	 * @param player
	 *            {@link Player} number
	 * @return <code>boolean</code>
	 */
	public boolean isPlaying(int number) {
		return playing.contains(defaultPlayers.get(number));
	}

	/**
	 * Retrieves the {@link Player} who's current turn it is.
	 * 
	 * @return {@link Player}
	 */
	public Player getCurrent() {

		if (playing == null) {
			throw new NullPointerException("List of playing Players is null!");
		}

		return playing.get(currentPlayerIndex);
	}

	/**
	 * Set the current {@link Player} in this {@link PlayerHelper}.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void setCurrent(int number) {
		int index = playing.indexOf(defaultPlayers.get(number));
		currentPlayerIndex = index != -1 ? index : currentPlayerIndex;
	}

	/**
	 * Iterates to the next player.
	 */
	public void nextPlayer() {

		currentPlayerIndex = (currentPlayerIndex + 1) % playing.size();

		reinforce();

		if (currentPlayerIndex == 0) {
			game.endRound();
		}

		// Check the challenges going into the next round
		checkChallenges(game.states.reinforcement);
	}

	/**
	 * Retrieves a {@link Player} using the number assigned to that {@link Player}
	 * from the default set of players.
	 * 
	 * @param number
	 * @return
	 */
	public Player get(int number) {
		return defaultPlayers.get(number);
	}

	/**
	 * Resets all the countries owned and army size of all the {@link Player}s to
	 * zero;
	 */
	public void resetAll() {
		defaultPlayers.forEach((number, player) -> player.reset());
	}

	/**
	 * Empties the {@link List} of playing {@link Player}s.
	 */
	public void emptyPlaying() {
		playing.clear();
	}

	/**
	 * Initialises all the {@link Player} images.
	 * 
	 * @param uiPath
	 *            Path to the
	 */
	public void initAll() {
		defaultPlayers.forEach((number, player) -> player.init(game.assets.ui));
	}

	/**
	 * Uses the set of default {@link Players} to populate the playing
	 * {@link Player}s.
	 * 
	 * @param number
	 *            The number of {@link Player}s to be added.
	 */
	public void defaultPlayers(int number) {

		playing.clear();

		for (int index = 1; index <= number; index++) {
			setPlaying(index);
		}
	}

	/**
	 * Retrieves a random {@link Player} that is currently playing.
	 * 
	 * @return
	 */
	public Player getRandom() {
		return playing.get(new Random().nextInt(playing.size()));
	}

	/**
	 * Set a {@link Player} as playing using the number assigned to that player.
	 * 
	 * @param number
	 *            assigned to a {@link Player}.
	 */
	public void setPlaying(int number) {

		Player player = defaultPlayers.get(number);

		if (!playing.contains(player)) {
			playing.add(player);
		}
	}

	/**
	 * Gives the current {@link Player} reinforcements based on the number of
	 * countries they own.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void reinforce() {

		Player player = getCurrent();

		// Scale reinforcements with round progression.
		int roundScale = game.getRoundNumber() != 0 ? game.getRoundNumber() * 2 : 1;

		if (player.getCountriesRuled() < 12) {
			player.distributableArmy.add(3 * roundScale);
		} else {
			player.distributableArmy.add((player.getCountriesRuled() * roundScale) / 3);
		}
	}

}
