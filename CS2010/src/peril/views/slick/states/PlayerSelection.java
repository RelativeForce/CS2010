package peril.views.slick.states;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.ai.AI;
import peril.controllers.GameController;
import peril.helpers.PlayerHelper;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.VisualList;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * The {@link InteractiveState} that allows the user to select the number of
 * {@link SlickPlayer}s and the {@link AI} that control those
 * {@link SlickPlayer}s.
 * 
 * @author Joshua_Eddy
 *
 */
public final class PlayerSelection extends InteractiveState {

	/**
	 * The name of this {@link PlayerSelection}.
	 */
	private static final String NAME = "Select";

	/**
	 * The {@link Map} of {@link SlickPlayer} number to there associated
	 * {@link VisualList} that selects the {@link AI} for that player.
	 */
	private final Map<Integer, VisualList<AI>> selectors;

	/**
	 * The string id of the play button.
	 */
	private final String playButton;

	/**
	 * The {@link VisualList} of the number of {@link SlickPlayer}s that can be in
	 * the {@link Game}.
	 */
	private final VisualList<Integer> players;

	private int width;

	private int height;

	/**
	 * Constructs a new {@link PlayerSelection}.
	 * 
	 * @param game
	 *            {@link Game} this {@link PlayerSelection} is a part of.
	 * @param id
	 *            The id of this {@link PlayerSelection}.
	 */
	public PlayerSelection(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);

		this.playButton = "play";
		this.selectors = new HashMap<>();
		this.players = new VisualList<>(new Point(100, 100), 20, 24, 3, 5);
		this.width = 100;
		this.height = 100;

		players.setFont(new Font("Arial", Color.black, 19));

		addSelectors();

		super.addComponent(players);

	}

	/**
	 * Enter the {@link PlayerSelection} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);

		players.setPosition(new Point((gc.getWidth() / 6) - players.getWidth() - 10, gc.getHeight() / 2));

		// Set the positions and visibility of the selectors.
		positionSelectors(gc);
		confingureSelectors();

		// Set the position of the play button.
		getButton(playButton).setPosition(new Point(gc.getWidth() - 10 - getButton(playButton).getWidth(),
				gc.getHeight() - 10 - getButton(playButton).getHeight()));

	}

	/**
	 * Processes a button press on this {@link PlayerSelection}.
	 */
	@Override
	public void parseButton(Frame frame, int key, Point mousePosition) {
		frame.pressButton(key, mousePosition);
	}

	/**
	 * Renders this {@link PlayerSelection}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		drawImages();
		drawButtons();

		frame.draw(players, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int button) {
				players.click(mouse);
				confingureSelectors();
			}

			@Override
			public void buttonPress(int key, Point mouse) {

				if (players.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						players.up();
						confingureSelectors();
					} else if (key == Input.KEY_DOWN) {
						players.down();
						confingureSelectors();
					}
				}

			}

			@Override
			public void draw(Frame frame) {
				players.draw(frame);
			}
		});

		selectors.forEach((playerNumber, selector) -> {

			if (selector.isVisible()) {

				frame.draw(selector, new EventListener() {

					@Override
					public void mouseHover(Point mouse, int delta) {
						// Do nothing
					}

					@Override
					public void mouseClick(Point mouse, int button) {
						selector.click(mouse);
					}

					@Override
					public void buttonPress(int key, Point mouse) {
						
						if (selector.isClicked(mouse)) {

							if (key == Input.KEY_UP) {
								selector.up();
							} else if (key == Input.KEY_DOWN) {
								selector.down();
							}
						}
					}

					@Override
					public void draw(Frame frame) {
						selector.draw(frame);
					}
				});

				Image playerIcon = slick.getPlayerIcon(playerNumber);

				final int x = selector.getPosition().x + (selector.getWidth() / 2) - (playerIcon.getWidth() / 2);

				final int y = selector.getPosition().y - playerIcon.getHeight() - 10;

				frame.draw(playerIcon, x, y);
			}

		});

	}

	/**
	 * Retrieves the {@link Music} that plays during {@link PlayerSelection}.
	 */
	@Override
	public Music getMusic() {
		return null;
	}

	@Override
	public void update(GameContainer gc, int delta, Frame frame) {
		// Do nothing
	}
	
	/**
	 * Load the game based on the current state of the elements in this
	 * {@link PlayerSelection}.
	 */
	public void loadGame() throws SlickException {

		// Reset the board
		game.resetGame();
		game.getModelBoard().reset();

		// Iterate through the number of players the player has selected
		for (int index = 1; index <= players.getSelected(); index++) {

			// The colour assigned to that player.
			Color color = slick.getColor(index);

			// Set the player with the AI that the user selected.
			SlickPlayer player = new SlickPlayer(index, color, selectors.get(index).getSelected());

			// Set the player Icon for that player.
			player.replaceImage(slick.getPlayerIcon(index));

			game.addPlayer(player.model);
			slick.modelView.addPlayer(player);

		}

		// Load the game
		slick.reSize(width, height);
		slick.enterState(slick.states.loadingScreen);
	}

	public void setScreenSize(int width, int height) {

		// Check width
		if (width <= 0) {
			throw new IllegalArgumentException("Width must greater than zero.");
		}

		// Check height
		if (height <= 0) {
			throw new IllegalArgumentException("Height must be greater than zero.");
		}

		this.width = width;
		this.height = height;

	}

	/**
	 * Adds the {@link VisualList} selectors to {@link PlayerSelection#selectors}
	 * that allow the user to select which {@link AI} {@link SlickPlayer}s have.
	 */
	private void addSelectors() {

		
		for(int playerIndex = 2; playerIndex <= PlayerHelper.MAX_PLAYERS; playerIndex++) {
			players.add(Integer.toString(playerIndex), playerIndex);
		}
		
		for (int index = 1; index <= PlayerHelper.MAX_PLAYERS; index++) {

			VisualList<AI> selector = new VisualList<>(new Point(100, 100), 100, 24, 5, 5);

			populateSelector(selector);

			selectors.put(index, selector);
			super.addComponent(selector);

		}

	}

	/**
	 * Configures which {@link PlayerSelection#selectors} are visible based on the
	 * selected value of {@link PlayerSelection#players}.
	 */
	private void confingureSelectors() {

		selectors.forEach((playerNumber, selector) -> {

			if (playerNumber <= players.getSelected()) {
				selector.show();
			} else {
				selector.hide();
			}

		});

	}

	/**
	 * Sets the position of the {@link PlayerSelection#selectors} based on the
	 * dimensions of the specified {@link GameContainer}.
	 * 
	 * @param gc
	 *            {@link GameContainer}
	 */
	private void positionSelectors(GameContainer gc) {
		selectors.forEach((playerNumber, selector) -> selector.setPosition(getSelectorPosition(playerNumber, gc)));
	}

	/**
	 * Retrieves the {@link Point} position of a {@link PlayerSelection#selectors}
	 * element based on the specified {@link SlickPlayer} number and the
	 * {@link GameContainer}.
	 * 
	 * @param playerNumber
	 *            {@link SlickPlayer} number
	 * @param gc
	 *            {@link GameContainer}
	 * @return {@link Point}
	 */
	private Point getSelectorPosition(int playerNumber, GameContainer gc) {

		int width = gc.getWidth();
		int height = gc.getHeight();

		// All the selectors have the same Y
		int selectorY = height / 2;

		return new Point((width * (playerNumber)) / (PlayerHelper.MAX_PLAYERS + 2), selectorY);

	}

	/**
	 * Populates the {@link VisualList} selector with all the elements of
	 * {@link PlayerSelection#allAI}.
	 * 
	 * @param selector
	 *            {@link VisualList} selector.
	 */
	private void populateSelector(VisualList<AI> selector) {

		for (AI ai : game.getAIs()) {
			selector.add(ai.name, ai);
		}
	}

}
