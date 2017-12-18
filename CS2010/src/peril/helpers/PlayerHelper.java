package peril.helpers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.Challenge;
import peril.Game;
import peril.Player;
import peril.io.fileReaders.ImageReader;
import peril.ui.states.EndState;
import peril.ui.states.gameStates.CoreGameState;

/**
 * A helper class for {@link Game} which encapsulates the behaviours of the list
 * of {@link Player}s.
 * 
 * @author Joshua_Eddy
 *
 */
public class PlayerHelper {

	/**
	 * The maximum number of {@link Player}s in the game at any given time.
	 */
	public static final int MAX_PLAYERS = 4;

	/**
	 * The minimum number of {@link Player}s in the game at any given time.
	 */
	public static final int MIN_PLAYERS = 2;

	/**
	 * Contains all the objectives that a {@link Player} can attain in the game.
	 */
	public final List<Challenge> challenges;

	/**
	 * Holds all the {@link Player}s in this {@link Game}.
	 */
	private final Map<Integer, Player> playing;

	/**
	 * Holds all the {@link Player}'s {@link Image} icons in this {@link Game}.
	 */
	private final Map<Integer, Image> playerIcons;

	/**
	 * The {@link Color}s of all the {@link Player}s.
	 */
	private final Color[] colors;

	/**
	 * The {@link Game} this {@link PlayerHelper} helps.
	 */
	private final Game game;

	/**
	 * The {@link Player} who's turn it is.
	 */
	private Iterator<Integer> index;

	/**
	 * The current {@link Player}.
	 */
	private Player current;

	/**
	 * Constructs a new {@link PlayerHelper}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link PlayerHelper} helps.
	 */
	public PlayerHelper(Game game) {
		this.game = game;
		this.playerIcons = new HashMap<>();
		this.playing = new LinkedHashMap<>();
		this.index = playing.keySet().iterator();
		this.challenges = new LinkedList<>();
		this.colors = new Color[] { Color.red, Color.blue, Color.green, Color.pink.multiply(Color.pink) };
	}

	/**
	 * Gets the number of {@link Player}s in the {@link Game}.
	 * 
	 * @return <code>int</code>
	 */
	public int numberOfPlayers() {
		return playing.size();
	}

	/**
	 * Performs the specified task on each {@link Player} in the {@link Game}.
	 * 
	 * @param task
	 *            {@link Consumer}
	 */
	public void forEach(Consumer<Player> task) {
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
		game.menus.challengeMenu.refreshChallenges();
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
				currentState.showToolTip(challenge.completed());
			}
		});

		// Remove the completed challenges.
		toRemove.forEach(challenge -> challenges.remove(challenge));

		// Refresh the challenge view
		game.menus.challengeMenu.refreshChallenges();
	}

	/**
	 * Sets a specified {@link Player} as a loser which removes it from the
	 * {@link Game#players} and adds it to the podium in the {@link EndState}.
	 * 
	 * @param player
	 *            {@link Player} number that has lost.
	 */
	public void setLoser(Player player) {

		// Add the player to the podium and remove it from the players in play.
		game.states.end.addToTop(player);

		playing.remove(player.number);

		index = playing.keySet().iterator();

		// Bring the iterator through the lit until it finds the player.
		while (index.hasNext()) {
			if (index.next() == current.number) {
				break;
			}
		}

	}

	public Player getPlayer(int player) {
		return playing.get(player);
	}

	/**
	 * Retrieves whether or not a specified player is in this {@link Game}.
	 * 
	 * @param player
	 *            {@link Player} number
	 * @return <code>boolean</code>
	 */
	public boolean isPlaying(int number) {
		return playing.containsKey(number);
	}

	/**
	 * Retrieves the {@link Player} who's current turn it is.
	 * 
	 * @return {@link Player}
	 */
	public Player getCurrent() {
		return current;
	}

	/**
	 * Set the current {@link Player} in this {@link PlayerHelper}.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void setCurrent(Player player) {

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
		checkChallenges(game.states.reinforcement);
	}

	/**
	 * Empties the {@link List} of playing {@link Player}s.
	 */
	public void emptyPlaying() {
		playing.clear();
		index = playing.keySet().iterator();
	}

	/**
	 * Adds a {@link Player} to the set of {@link Player}s that are playing.
	 * 
	 * @param player
	 */
	public void addPlayer(Player player) {

		if (!playing.containsKey(player.number)) {

			playing.put(player.number, player);
			index = playing.keySet().iterator();
			current = playing.get(index.next());

		}
	}

	/**
	 * Retrieves a random {@link Player} that is currently playing.
	 * 
	 * @return
	 */
	public Player getRandomPlayer() {

		int pos = new Random().nextInt(playing.size());

		int i = 0;
		for (Player p : playing.values()) {

			if (pos == i) {
				return p;
			}

			i++;
		}

		return null;
	}

	/**
	 * Gives the current {@link Player} reinforcements based on the number of
	 * countries they own.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void reinforceCurrent() {

		Player player = getCurrent();

		// Scale reinforcements with round progression.
		int roundScale = game.getRoundNumber() != 0 ? game.getRoundNumber() * 2 : 1;

		if (player.getCountriesRuled() < 12) {
			player.distributableArmy.add(3 * roundScale);
		} else {
			player.distributableArmy.add((player.getCountriesRuled() * roundScale) / 3);
		}
	}

	/**
	 * Retrieves the {@link Image} icon for a {@link Player}
	 * 
	 * @param player
	 *            {@link Player}
	 * @return {@link Image} icon.
	 */
	public Image getPlayerIcon(int player) {
		return playerIcons.get(player);
	}

	/**
	 * Initialises the images of a {@link Player}.
	 * 
	 * @param uiPath
	 *            The path to the folder with the image files in.
	 */
	public void init(String uiPath) {

		for (int index = 1; index <= PlayerHelper.MAX_PLAYERS; index++) {

			String path = uiPath + "player" + index + "Icon.png";

			playerIcons.put(index, ImageReader.getImage(path).getScaledCopy(90, 40));

		}

	}

	/**
	 * Retrieves the {@link Color} associated with a {@link Player}
	 * 
	 * @param playerNumber
	 * @return
	 */
	public Color getColor(int playerNumber) {
		return colors[playerNumber - 1];
	}
}
