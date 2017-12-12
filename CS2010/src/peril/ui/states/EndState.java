package peril.ui.states;

import java.util.LinkedList;
import java.util.List;

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
	 * The {@link Button} that will cause the {@link EndState} to return the the
	 * main menu.
	 */
	private Button menuButton;

	/**
	 * The {@link Button} that will cause the {@link EndState} to exit the
	 * {@link Game}.
	 */
	private Button exitButton;

	private Viewable background;

	private Point first;

	private Point second;

	private Point third;

	private Point fourth;
	
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
	 * Adds a {@link Button} to this {@link EndState}.
	 */
	@Override
	public void addButton(Button button) {
		super.addButton(button);

		// Determine special buttons
		if (button.getId().equals("menu")) {
			menuButton = button;
		} else if (button.getId().equals("exit")) {
			exitButton = button;
		}

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
	}

	/**
	 * Initialise the visual elements of the {@link EndState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
		
		music = getGame().io.musicHelper.read("victory");
		
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
		background.setImage(background.getPosition(),
				background.getImage().getScaledCopy(gc.getWidth(), gc.getHeight()));
		menuButton.setPosition(new Point(gc.getWidth() - menuButton.getWidth() - padding,
				gc.getHeight() - menuButton.getHeight() - padding));
		exitButton.setPosition(new Point(padding, gc.getHeight() - exitButton.getHeight() - padding));

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

	private void setPodiumPositions(GameContainer gc) {

		int width = gc.getWidth();
		int height = gc.getHeight();

		first = new Point((width * 41) / 80, (height * 38) / 80);
		second = new Point((width * 13) / 40, (height * 6) / 10);
		third = new Point((width * 55) / 80, (height * 25) / 40);
		fourth = new Point((width * 41) / 80, (height * 33) / 40);

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

		// Iterate through each player on the podium.
		for (Player player : podium) {

			// The players position on screen is dependent on there position in the podium.
			switch (position) {
			case 1:
				drawPlayer(g, player, first);
				break;
			case 2:
				drawPlayer(g, player, second);
				break;
			case 3:
				drawPlayer(g, player, third);
				break;
			case 4:
				drawPlayer(g, player, fourth);
				break;
			}

			position++;
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
