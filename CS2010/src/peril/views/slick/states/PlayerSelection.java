package peril.views.slick.states;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.ai.AI;
import peril.controllers.GameController;
import peril.helpers.PlayerHelper;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.VisualList;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.io.ImageReader;
import peril.views.slick.util.Clickable;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * The {@link InteractiveState} that allows the user to select the number of
 * {@link SlickPlayer}s and the {@link AI} that control those
 * {@link SlickPlayer}s. The user may also select the the speed of the
 * {@link AI}s.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-18
 * @version 1.01.02
 * 
 * @see InteractiveState
 * @see AI
 * @see MainMenu
 *
 */
public final class PlayerSelection extends InteractiveState {

	/**
	 * The name of this {@link PlayerSelection}.
	 */
	private static final String NAME = "Select";

	/**
	 * The text before the name of the AI.
	 */
	private static final String AI_TEXT = "AI: ";

	/**
	 * The {@link VisualList} containing all the {@link AI}s in the game.
	 */
	private final VisualList<AI> aiList;

	/**
	 * The {@link VisualList} of the number of {@link SlickPlayer}s that can be in
	 * the game.
	 */
	private final VisualList<Integer> numberOfPlayers;

	/**
	 * The {@link VisualList} containing the different {@link AI} speeds available
	 * for the user to select.
	 */
	private final VisualList<Integer> aiSpeeds;

	/**
	 * The {@link List} of {@link Player}s that will be used to construct the game.
	 */
	private final List<Player> players;

	/**
	 * The {@link Point} position of the top player on the screen. All the other
	 * players will be displayed below this position.
	 */
	private final Point topPlayer;

	/**
	 * The {@link Font} used to display text on the {@link PlayerSelection} screen.
	 */
	private final Font textFont;

	/**
	 * The {@link Player} that the user is currently selecting an {@link AI} for.
	 */
	private Player selected;

	/**
	 * The width of the map that the page will load after the players have been set.
	 */
	private int width;

	/**
	 * The height of the map that the page will load after the players have been
	 * set.
	 */
	private int height;

	/**
	 * Constructs a new {@link PlayerSelection}.
	 * 
	 * @param game
	 *            {@link GameController} this {@link PlayerSelection} is a part of.
	 * @param id
	 *            The id of this {@link PlayerSelection}.
	 */
	public PlayerSelection(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);

		this.width = 100;
		this.height = 100;
		this.topPlayer = new Point((MainMenu.WIDTH / 3), MainMenu.HEIGHT / 3);
		this.players = new LinkedList<>();
		this.selected = null;

		final Font listFont = new Font("Arial", Color.black, 19);

		// Define the AI list
		this.aiList = new VisualList<>(new Point(400, 100), 100, 24, 5, 5);
		this.aiList.setFont(listFont);
		populateAIList();

		// Define the number of players list
		this.numberOfPlayers = new VisualList<>(new Point(100, topPlayer.y), 20, 24, 3, 5);
		this.numberOfPlayers.setFont(listFont);
		populatePlayers();

		// Define the AI speeds list
		this.aiSpeeds = new VisualList<>(new Point(100, topPlayer.y + 110), 50, 24, 4, 5);
		this.aiSpeeds.setFont(listFont);
		populateAISpeeds();

		textFont = new Font("Arial", Color.white, 20);

		super.addComponent(aiList);
		super.addComponent(numberOfPlayers);
		super.addComponent(aiSpeeds);

	}

	/**
	 * Performs the operations required to initialise the {@link PlayerSelection}
	 * state.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		textFont.init();

		// Load the icons for the screen actions.
		final Image changeIcon = ImageReader.getImage(game.getDirectory().getButtonsPath() + "editButton.png")
				.getScaledCopy(50, 50);
		final Image confirmIcon = ImageReader.getImage(game.getDirectory().getButtonsPath() + "tickButton.png")
				.getScaledCopy(50, 50);

		// Iterate over each player and initialise it.
		players.forEach(p -> p.init(changeIcon, confirmIcon));

	}

	/**
	 * Enter the {@link PlayerSelection} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);

		aiList.hide();

		resetPlayers();

		aiSpeeds.setSelected((Integer) AI.MAX_SPEED);
		changeAISpeed();

	}

	/**
	 * Renders this {@link PlayerSelection}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		drawImages();
		drawButtons();

		drawAIList(frame);

		drawNumberOfPlayers(frame);

		drawPlayers(frame);

		drawAISpeeds(frame);

	}

	/**
	 * Retrieves the {@link Music} that plays during {@link PlayerSelection}.
	 */
	@Override
	public Music getMusic() {
		return null;
	}

	/**
	 * Updates this {@link PlayerSelection}.
	 */
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
		for (Player player : players) {

			if (player.inPlay) {

				// The colour assigned to that player.
				final Color color = slick.getColor(player.number);

				// Set the player with the AI that the user selected.
				final SlickPlayer slickPlayer = new SlickPlayer(player.number, color, player.ai);

				// Set the player Icon for that player.
				slickPlayer.replaceImage(slick.getPlayerIcon(player.number));

				game.addPlayer(slickPlayer.model);
				slick.modelView.addPlayer(slickPlayer);
			}
		}

		// Load the game
		slick.reSize(width, height);
		slick.enterState(slick.states.loadingScreen);
	}

	/**
	 * Assigns the size the window will resize to after the player has selected the
	 * players.
	 * 
	 * @param width
	 *            The width of the map that the page will load after the players
	 *            have been set.
	 * @param height
	 *            The height of the map that the page will load after the players
	 *            have been set.
	 */
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
	 * Configures which {@link PlayerSelection#selectors} are visible based on the
	 * selected value of {@link PlayerSelection#numberOfPlayers}.
	 */
	private void resetPlayers() {

		players.forEach(player -> {
			player.inPlay = player.number <= PlayerHelper.MIN_PLAYERS;
			player.ai = AI.USER;
		});

	}

	/**
	 * Populates the {@link PlayerSelection#players} with all the players that can
	 * be in the game at once.
	 */
	private void populatePlayers() {

		for (int index = 1; index <= PlayerHelper.MAX_PLAYERS; index++) {
			players.add(new Player(index));

			if (index >= PlayerHelper.MIN_PLAYERS) {
				numberOfPlayers.add(Integer.toString(index), index);
			}
		}

	}

	/**
	 * Draws the {@link PlayerSelection#aiList} on screen using the specified
	 * {@link Frame}.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display the list to the user.
	 */
	private void drawAIList(Frame frame) {

		if (!aiList.isVisible()) {
			return;
		}

		frame.draw(aiList, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {

				// Click the list
				aiList.click(mouse);

				// If there is a selected player change it's AI.
				changeAI();
			}

			@Override
			public void draw(Frame frame) {
				aiList.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {

				if (aiList.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						aiList.up();
						changeAI();
					} else if (key == Input.KEY_DOWN) {
						aiList.down();
						changeAI();
					}
				}

			}

		});
	}

	/**
	 * Changes the {@link AI} of the currently selected {@link Player}.
	 */
	private void changeAI() {

		if (selected != null) {
			selected.ai = aiList.getSelected();
		}

	}

	/**
	 * Draws the {@link PlayerSelection#numberOfPlayers} on screen using the
	 * specified {@link Frame}.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display the list to the user.
	 */
	private void drawNumberOfPlayers(Frame frame) {

		final String text = "Number of Players:";

		frame.draw(textFont, text, numberOfPlayers.getPosition().x,
				numberOfPlayers.getPosition().y - textFont.getHeight(text));

		frame.draw(numberOfPlayers, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int button) {
				numberOfPlayers.click(mouse);
				configurePlayers();
			}

			@Override
			public void buttonPress(int key, Point mouse) {

				if (numberOfPlayers.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						numberOfPlayers.up();
						configurePlayers();
					} else if (key == Input.KEY_DOWN) {
						numberOfPlayers.down();
						configurePlayers();
					}
				}

			}

			@Override
			public void draw(Frame frame) {
				numberOfPlayers.draw(frame);
			}
		});
	}

	/**
	 * Draws the {@link PlayerSelection#aiSpeeds} on screen using the specified
	 * {@link Frame}.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display the list to the user.
	 */
	private void drawAISpeeds(Frame frame) {

		final String text = "AI speed:";

		frame.draw(textFont, text, aiSpeeds.getPosition().x, aiSpeeds.getPosition().y - textFont.getHeight(text));

		frame.draw(aiSpeeds, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				aiSpeeds.click(mouse);
				changeAISpeed();
			}

			@Override
			public void draw(Frame frame) {
				aiSpeeds.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {

				if (aiSpeeds.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						aiSpeeds.up();
						changeAISpeed();
					} else if (key == Input.KEY_DOWN) {
						aiSpeeds.down();
						changeAISpeed();
					}
				}
			}

		});

	}

	/**
	 * Changes the speed of all the {@link AI}s in the game to the speed selected in
	 * {@link PlayerSelection#aiSpeeds}.
	 */
	private void changeAISpeed() {

		final int newSpeed = aiSpeeds.getSelected();

		game.getAIs().forEach(ai -> ai.setSpeed(newSpeed));
	}

	/**
	 * Draws all the {@link Player} on screen that are currently in the game.
	 * 
	 * @param frame
	 *            The {@link Frame} that displays the {@link Player}s to the user.
	 */
	private void drawPlayers(Frame frame) {

		for (Player player : players) {

			if (player.inPlay) {

				frame.draw(player, new EventListener() {

					@Override
					public void mouseHover(Point mouse, int delta) {
					}

					@Override
					public void mouseClick(Point mouse, int mouseButton) {

					}

					@Override
					public void draw(Frame frame) {
						frame.draw(player.getImage(), player.getPosition().x, player.getPosition().y);

						final String text = AI_TEXT + (selected == player ? "" : player.ai.name);
						final int x = player.getPosition().x + player.getWidth() + 10;
						final int y = player.getPosition().y + (player.getHeight() / 2)
								- (textFont.getHeight(AI_TEXT) / 2);

						frame.draw(textFont, text, x, y);

						drawChangeIcon(frame);

					}

					@Override
					public void buttonPress(int key, Point mouse) {

					}

					private void drawChangeIcon(Frame frame) {

						frame.draw(player.change, new EventListener() {

							@Override
							public void mouseHover(Point mouse, int delta) {
								// Do nothing
							}

							@Override
							public void mouseClick(Point mouse, int mouseButton) {
								changeSelected(selected == player ? null : player);
							}

							@Override
							public void draw(Frame frame) {

								final Clickable icon = selected != player ? player.change : player.confirm;

								frame.draw(icon.getImage(), icon.getPosition().x, icon.getPosition().y);
							}

							@Override
							public void buttonPress(int key, Point mouse) {
								// Do nothing
							}
						});

					}
				});

			}

		}

	}

	/**
	 * Populates the {@link PlayerSelection#aiSpeeds} with all the {@link AI} speeds
	 * that is available for the user to select.
	 */
	private void populateAISpeeds() {

		// The increment of speeds.
		final int factor = 200;

		for (int index = 0; index < 4; index++) {

			final int speed = AI.MAX_SPEED + (index * factor);

			aiSpeeds.add(Integer.toString(speed), speed);

		}

	}

	/**
	 * Changes the selected {@link Player}.
	 * 
	 * @param player
	 *            The new selected {@link Player} that the user is currently
	 *            selecting an {@link AI} for.
	 */
	private void changeSelected(Player player) {
		selected = player;

		// If the selected is null then hide the AI list, otherwise move the AI list so
		// that it looks like the user has opened a drop down.
		if (selected == null) {
			aiList.hide();
		} else {

			final int x = player.getPosition().x + player.getWidth() + textFont.getWidth(AI_TEXT) + 10;
			final int y = player.getPosition().y + (player.getHeight() / 2) - (aiList.getHeight() / 2);

			aiList.setSelected(player.ai);

			aiList.setPosition(new Point(x, y));
			aiList.show();
		}

	}

	/**
	 * Sets all the number of {@link Player} specified by the
	 * {@link PlayerSelection#numberOfPlayers} list.
	 */
	private void configurePlayers() {
		players.forEach(player -> {
			player.inPlay = player.number <= numberOfPlayers.getSelected();
		});
	}

	/**
	 * Populates the {@link PlayerSelection#aiList} with all the {@link AI}s in the
	 * game using {@link GameController#getAIs()}.
	 */
	private void populateAIList() {

		for (AI ai : game.getAIs()) {
			aiList.add(ai.name, ai);
		}
	}

	/**
	 * Encapsulates the details about a player that is displayed on the
	 * {@link PlayerSelection} screen. This also stores the core details about the
	 * player so that a {@link SlickPlayer} can be constructed when the game is
	 * loaded.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-02-18
	 * @version 1.01.01
	 * 
	 * @see Clickable
	 * @see SlickPlayer
	 *
	 */
	private final class Player extends Clickable {

		/**
		 * The number of the player.
		 */
		public final int number;

		/**
		 * The {@link AI} controlling this {@link Player}.
		 */
		public AI ai;

		/**
		 * Whether or not this {@link Player} will be added to the game or not.
		 */
		public boolean inPlay;

		/**
		 * The {@link Clickable} change icon.
		 */
		public Clickable change;

		/**
		 * The {@link Clickable} confirm icon.
		 */
		public Clickable confirm;

		/**
		 * Constructs a new {@link Player} with default {@link AI#USER}.
		 * 
		 * @param playerNumber
		 *            The number of the player.
		 */
		public Player(int playerNumber) {
			this.number = playerNumber;
			this.ai = AI.USER;
			this.inPlay = false;
		}

		/**
		 * Initialises the visual elements of this {@link Player}.
		 * 
		 * @param changeIcon
		 *            The {@link Image} of the change icon.
		 * @param confirmIcon
		 *            The {@link Image} of the confirm icon.
		 */
		public void init(Image changeIcon, Image confirmIcon) {

			final Image playerIcon = slick.getPlayerIcon(number);

			final int x = topPlayer.x;
			final int y = topPlayer.y + ((number - 1) * playerIcon.getHeight()) + 20;

			final int playerIconY = y - (playerIcon.getHeight() / 2);

			final int changeIconX = x + playerIcon.getWidth() + textFont.getWidth(AI_TEXT) + 15 + aiList.getWidth();
			final int changeIconY = y - (changeIcon.getHeight() / 2);

			this.change = new Clickable(changeIcon);
			this.change.setPosition(new Point(changeIconX, changeIconY));

			this.confirm = new Clickable(confirmIcon);
			this.confirm.setPosition(new Point(changeIconX, changeIconY));

			final Point position = new Point(x, playerIconY);

			super.replaceImage(playerIcon);
			super.setPosition(position);

		}
	}

}
