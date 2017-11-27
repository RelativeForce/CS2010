package peril.ui.states.menuStates;

import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.ui.Font;
import peril.ui.states.InteractiveState;

/**
 * 
 * The final {@link InteractiveState} of the {@link Game} where the winner and
 * losers are displayed.
 * 
 * @author Joshua_Eddy
 *
 */
public class EndState extends InteractiveState {

	/**
	 * The name of a specific {@link EndState}.
	 */
	private static final String STATE_NAME = "EndState";

	/**
	 * The {@link Font} that the winning {@link Player} will be displayed in.
	 */
	private Font winnerFont;

	/**
	 * The ordered {@link Player}s finishing positions. {@link Player} at the front
	 * of the {@link LinkedList} is 1st place.
	 */
	private LinkedList<Player> podium;

	/**
	 * The {@link Font} that the losing {@link Player}(s) will be displayed in.
	 */
	private Font loserFont;

	/**
	 * Constructs a new {@link EndState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The id of this {@link EndState}.
	 */
	public EndState(Game game, int id) {
		super(game, STATE_NAME, id);
		podium = new LinkedList<>();
	}

	/**
	 * Adds a {@link Player} to the top of the podium. If there is already players
	 * at the top they are moved one position down.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void addPlayerToPodium(Player player) {

		if (player == null) {
			throw new NullPointerException("Player cannot be null.");
		}

		podium.push(player);
	}

	/**
	 * Processes a mouse click at a {@link Point} position on this {@link EndState}.
	 */
	@Override
	public void parseClick(int button, Point click) {
		super.clickedButton(click);
	}

	/**
	 * Processes a button press on this {@link EndState}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {
		// Do NOTHING
	}

	/**
	 * Render the {@link EndState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		drawImages(g);
		drawButtons(g);

		drawPodium(g, gc.getWidth(), gc.getHeight());
	}

	/**
	 * Initialise the visual elements of the {@link EndState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		// Initialise Fonts
		winnerFont = new Font("Arial", Color.yellow, 50);
		loserFont = new Font("Arial", Color.red, 30);
	}

	/**
	 * Enters the {@link EndState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		if (podium.isEmpty()) {
			throw new IllegalStateException("There must be at least one player on the podium for the game to end.");
		}
	}

	/**
	 * Performs the exit state operations specific to the {@link EndState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		
		// Empty the podium
		podium.clear();
	}

	/**
	 * Retrieves the {@link Music} of this {@link EndState}.
	 */
	@Override
	public Music getMusic() {
		// TODO victory music
		return null;
	}

	/**
	 * Draws the podium on the screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param width
	 *            Width of the {@link GameContainer}
	 * @param height
	 *            Height of the {@link GameContainer}
	 */
	private void drawPodium(Graphics g, int width, int height) {

		// Holds the position of the current player.
		int position = 1;

		// Initial height of the podium.
		int y = height / 3;

		// Iterate through each player on the podium.
		for (Player player : podium) {

			// The text string that will be displayed
			String text = position + ". " + player.toString();

			// If the current player is first use the winner font otherwise use the loser
			// font.
			if (position == 1) {
				winnerFont.draw(g, text, (width / 2) - (winnerFont.getWidth(text) / 2), y);
				y += winnerFont.getHeight();
			} else {
				loserFont.draw(g, text, (width / 2) - (loserFont.getWidth(text) / 2), y);
				y += loserFont.getHeight();
			}

			position++;
		}

	}

}
