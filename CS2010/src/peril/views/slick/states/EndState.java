package peril.views.slick.states;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Button;
import peril.views.slick.util.Point;

/**
 * 
 * The final {@link InteractiveState} where the winner and losers are displayed.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-26
 * @version 1.01.01
 * 
 * @see InteractiveState
 *
 */
public final class EndState extends InteractiveState {

	/**
	 * The name of a specific {@link EndState}.
	 */
	private static final String STATE_NAME = "End";

	/**
	 * The ordered {@link SlickPlayer}s finishing positions. {@link SlickPlayer} at
	 * the front of the {@link LinkedList} is 1st place.
	 */
	private final LinkedList<SlickPlayer> podium;

	/**
	 * The {@link Button} that will cause the {@link EndState} to return the the
	 * main menu.
	 */
	private final String menuButton;

	/**
	 * The {@link Button} that will cause the {@link EndState} to exit the game.
	 */
	private final String exitButton;

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
	 *            The {@link GameController} which allows this {@link EndState} to
	 *            query the state of the game.
	 * @param id
	 *            The id of this {@link EndState}.
	 */
	public EndState(GameController game, int id) {
		super(game, STATE_NAME, id, HelpMenu.NULL_PAGE);
		podium = new LinkedList<>();
		exitButton = "exit";
		menuButton = "menu";
		podiumPositions = new HashMap<>();
	}

	/**
	 * Adds a {@link SlickPlayer} to the top of the podium. If there is already
	 * players at the top they are moved one position down.
	 * 
	 * @param player
	 *            {@link SlickPlayer}
	 */
	public void addToTop(SlickPlayer player) {

		if (player == null) {
			throw new NullPointerException("Player cannot be null.");
		}

		podium.push(player);
	}

	/**
	 * Render the {@link EndState}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		drawImages();
		drawButtons();

		drawPodium(frame, gc.getWidth(), gc.getHeight());

	}

	/**
	 * Initialise the visual elements of the {@link EndState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		music = slick.music.read("victory");
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
	 * Updated the visual elements of the {@link EndState}.
	 */
	@Override
	public void update(GameContainer gc, int delta, Frame frame) {
		// Do Nothing
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
	 * Performs a {@link Consumer} task on every {@link ModelPlayer} that has lost.
	 * 
	 * @param task
	 *            The task to be performed on every lecture.
	 */
	public void forEachLoser(Consumer<ModelPlayer> task) {
		podium.forEach(loser -> task.accept(loser.model));
	}

	/**
	 * Retrieves the {@link Music} of this {@link EndState}.
	 */
	@Override
	public Music getMusic() {
		return music;
	}

	/**
	 * Retrieves the podium of this {@link EndState}.
	 * 
	 * @return {@link List} of {@link SlickPlayer}s.
	 */
	public List<SlickPlayer> getPodium() {
		return podium;
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
	 * @param frame
	 *            The {@link Frame} that displays this {@link EndState} to the user.
	 * @param width
	 *            Width of the {@link GameContainer}
	 * @param height
	 *            Height of the {@link GameContainer}
	 */
	private void drawPodium(Frame frame, int width, int height) {

		// Iterate through each player on the podium.
		for (int index = 0; index < podium.size(); index++) {
			drawPlayer(frame, podium.get(index), podiumPositions.get(index + 1));
		}

	}

	/**
	 * Draws a {@link SlickPlayer} at a position on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays this {@link EndState} to the user.
	 * @param player
	 *            {@link SlickPlayer} to be drawn
	 * @param position
	 *            {@link Point} position the player will be drawn
	 */
	private void drawPlayer(Frame frame, SlickPlayer player, Point position) {
		frame.draw(player.getImage(), position.x - (player.getWidth() / 2), position.y - (player.getHeight() / 2));
	}

}
