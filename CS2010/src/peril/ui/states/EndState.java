package peril.ui.states;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.ui.Button;
import peril.ui.Font;
import peril.ui.Viewable;
import peril.ui.components.menus.HelpMenu;

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
	private static final String STATE_NAME = "End";

	/**
	 * The {@link Font} that the winning {@link Player} will be displayed in.
	 */
	private final Font winnerFont;

	/**
	 * The ordered {@link Player}s finishing positions. {@link Player} at the front
	 * of the {@link LinkedList} is 1st place.
	 */
	private final LinkedList<Player> podium;

	/**
	 * The {@link Font} that the losing {@link Player}(s) will be displayed in.
	 */
	private final Font loserFont;

	/**
	 * The {@link Button} that will cause the {@link EndState} to return the the
	 * main menu.
	 */
	private final String menuButton;

	/**
	 * The {@link Button} that will cause the {@link EndState} to exit the
	 * {@link Game}.
	 */
	private final String exitButton;

	/**
	 * The background {@link Viewable} of the {@link EndState}.
	 */
	private Viewable background;

	/**
	 * Holds the {@link Point} position of the players in the {@link EndState}.
	 */
	private Map<Integer, Point> podiumPositions;

	/**
	 * The background music for this {@link EndState}.
	 */
	private Music music;

	/**
	 * Constructs a new {@link EndState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The id of this {@link EndState}.
	 */
	public EndState(Game game, int id) {
		super(game, STATE_NAME, id, HelpMenu.NULL_PAGE);
		podium = new LinkedList<>();
		winnerFont = new Font("Arial", Color.yellow, 50);
		loserFont = new Font("Arial", Color.red, 30);
		exitButton = "exit";
		menuButton = "menu";
		podiumPositions = new HashMap<>();
	}

	/**
	 * Adds a {@link Player} to the top of the podium. If there is already players
	 * at the top they are moved one position down.
	 * 
	 * @param player
	 *            {@link Player}
	 */
	public void addToTop(Player player) {

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
	 * Adds an {@link Viewable} image to this {@link EndState}.
	 */
	@Override
	public void addImage(Viewable image) {
		background = image;
		super.addImage(image);
	}

	/**
	 * Retrieves the podium of this {@link EndState}.
	 * 
	 * @return {@link List} of {@link Player}s.
	 */
	public List<Player> getPodium() {
		return podium;
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
		
		g.destroy();
	}

	/**
	 * Initialise the visual elements of the {@link EndState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		music = getGame().music.read("victory");

		// Initialise Fonts
		winnerFont.init();
		loserFont.init();
	}

	/**
	 * Enters the {@link EndState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		super.changeMusic(gc);

		if (podium.isEmpty()) {
			throw new IllegalStateException("There must be at least one player on the podium for the game to end.");
		}

		int padding = 20;

		// Scale background image based on screen size.
		background.scale(gc.getWidth(), gc.getHeight());

		// Reposition menu button based on screen size.
		int menuX = gc.getWidth() - getButton(menuButton).getWidth() - padding;
		int menuY = gc.getHeight() - getButton(menuButton).getHeight() - padding;
		getButton(menuButton).setPosition(new Point(menuX, menuY));

		// Reposition exit button based on screen size.
		int exitX = padding;
		int exitY = gc.getHeight() - getButton(exitButton).getHeight() - padding;
		getButton(exitButton).setPosition(new Point(exitX, exitY));

		// Sets the positions of the podium icons based on the size of the screen.
		setPodiumPositions(gc);
	}

	/**
	 * Performs the exit state operations specific to the {@link EndState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		// Empty the podium
		podium.clear();
	}

	/**
	 * Retrieves the {@link Music} of this {@link EndState}.
	 */
	@Override
	public Music getMusic() {
		return music;
	}

	/**
	 * Assigns the podium positions on screen based on the size of the screen.
	 * 
	 * @param gc
	 *            {@link GameContainer}
	 */
	private void setPodiumPositions(GameContainer gc) {

		int width = gc.getWidth();
		int height = gc.getHeight();

		podiumPositions.put(1, new Point((width * 41) / 80, (height * 38) / 80));
		podiumPositions.put(2, new Point((width * 13) / 40, (height * 6) / 10));
		podiumPositions.put(3, new Point((width * 55) / 80, (height * 25) / 40));
		podiumPositions.put(4, new Point((width * 41) / 80, (height * 33) / 40));

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

		// Iterate through each player on the podium.
		for (int index = 0; index < podium.size(); index++) {
			drawPlayer(g, podium.get(index), podiumPositions.get(index + 1));
		}

	}

	/**
	 * Draws a {@link Player} at a position on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param player
	 *            {@link Player} to be drawn
	 * @param position
	 *            {@link Point} position the player will be drawn
	 */
	private void drawPlayer(Graphics g, Player player, Point position) {
		g.drawImage(player.getImage(), position.x - (player.getWidth() / 2), position.y - (player.getHeight() / 2));
	}

}
