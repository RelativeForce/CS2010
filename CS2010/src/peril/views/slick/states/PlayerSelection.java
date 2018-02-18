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

import peril.Game;
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
 * {@link SlickPlayer}s.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-18
 * @version 1.01.02
 * 
 * @see InteractiveState
 *
 */
public final class PlayerSelection extends InteractiveState {

	/**
	 * The name of this {@link PlayerSelection}.
	 */
	private static final String NAME = "Select";
	
	private static final String AI_TEXT = "AI: ";

	private final VisualList<AI> aiList;

	/**
	 * The {@link VisualList} of the number of {@link SlickPlayer}s that can be in
	 * the game.
	 */
	private final VisualList<Integer> numberOfPlayers;

	private final List<Player> players;

	private final Point topPlayer;

	private final Font textFont;

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
	 *            {@link Game} this {@link PlayerSelection} is a part of.
	 * @param id
	 *            The id of this {@link PlayerSelection}.
	 */
	public PlayerSelection(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);

		this.width = 100;
		this.height = 100;
		this.topPlayer = new Point((MainMenu.WIDTH / 4), MainMenu.HEIGHT / 3);
		this.players = new LinkedList<>();
		this.selected = null;

		final Font listFont = new Font("Arial", Color.black, 19);

		this.aiList = new VisualList<>(new Point(400, 100), 100, 24, 5, 5);
		this.aiList.setFont(listFont);
		populateAIList();

		this.numberOfPlayers = new VisualList<>(new Point(200, 130), 20, 24, 3, 5);
		this.numberOfPlayers.setFont(listFont);
		populatePlayers();

		textFont = new Font("Arial", Color.white, 20);

		super.addComponent(aiList);
		super.addComponent(numberOfPlayers);

	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		textFont.init();

		final Image icon = ImageReader.getImage(game.getDirectory().getButtonsPath() + "change.png").getScaledCopy(50,
				50);

		players.forEach(p -> {

			final Image playerIcon = slick.getPlayerIcon(p.number);

			final int x = topPlayer.x;

			final int y = topPlayer.y + ((p.number - 1) * playerIcon.getHeight()) + 20;

			final int playerIconY = y - (playerIcon.getHeight() / 2);

			final Point position = new Point(x, playerIconY);

			final int changeIconX = x + playerIcon.getWidth() + textFont.getWidth(AI_TEXT) + 15 + aiList.getWidth();

			final int changeIconY = y - (icon.getHeight() / 2);

			p.change = new Clickable(icon);
			p.change.setPosition(new Point(changeIconX, changeIconY));

			p.confirm = new Clickable(icon);
			p.confirm.setPosition(new Point(changeIconX, changeIconY));

			p.replaceImage(playerIcon);
			p.setPosition(position);
		});

	}

	/**
	 * Enter the {@link PlayerSelection} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		aiList.hide();

		// numberOfPlayers.setPosition(new Point((gc.getWidth() / 6) -
		// numberOfPlayers.getWidth() - 10, gc.getHeight() / 2));

		resetPlayers();
	}

	private void populatePlayers() {

		for (int index = 1; index <= PlayerHelper.MAX_PLAYERS; index++) {
			players.add(new Player(index));

			if (index >= PlayerHelper.MIN_PLAYERS) {
				numberOfPlayers.add(Integer.toString(index), index);
			}
		}

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

	}

	private void drawAIList(Frame frame) {

		if (!aiList.isVisible()) {
			return;
		}

		frame.draw(aiList, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {

				aiList.click(mouse);

				if (selected != null) {
					selected.ai = aiList.getSelected();
				}
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
					} else if (key == Input.KEY_DOWN) {
						aiList.down();
					}
				}

			}
		});
	}

	private void drawNumberOfPlayers(Frame frame) {

		final String text = "Number of Players:";

		frame.draw(textFont, text, numberOfPlayers.getPosition().x - textFont.getWidth(text) - 5,
				numberOfPlayers.getPosition().y);

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

	private void changeSelected(Player player) {
		selected = player;

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

	private void configurePlayers() {
		players.forEach(player -> {
			player.inPlay = player.number <= numberOfPlayers.getSelected();
		});
	}

	/**
	 * Populates the {@link VisualList} selector with all the elements of
	 * {@link PlayerSelection#allAI}.
	 * 
	 * @param selector
	 *            {@link VisualList} selector.
	 */
	private void populateAIList() {

		for (AI ai : game.getAIs()) {
			aiList.add(ai.name, ai);
		}
	}

	private final class Player extends Clickable {

		public final int number;

		public AI ai;

		public boolean inPlay;

		public Clickable change;

		public Clickable confirm;

		public Player(int playerNumber) {
			this.number = playerNumber;
			this.ai = AI.USER;
			this.inPlay = false;
		}

	}

}
