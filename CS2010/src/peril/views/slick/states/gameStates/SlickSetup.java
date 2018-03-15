package peril.views.slick.states.gameStates;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.views.slick.components.Component;
import peril.controllers.GameController;
import peril.model.board.ModelCountry;
import peril.model.states.Setup;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.util.Point;

/**
 * Displays the {@link Setup} state to the user.
 * 
 * @author Joshua_Eddy, Joseph_Rolli, Gurdeep_Pol
 * 
 * @since 2018-03-15
 * @version 1.01.02
 * 
 * @see CoreGameState
 * @see Setup
 * @see RulerSelector
 *
 */
public final class SlickSetup extends CoreGameState {

	/**
	 * The {@link RulerSelector} that displays the {@link SlickPlayer}s that the
	 * user may select to rule the currently selected {@link SlickCountry}.
	 */
	private final RulerSelector players;

	/**
	 * Constructs a new {@link SlickSetup}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link SlickSetup} to
	 *            query the state of the game.
	 * @param id
	 *            The ID of this {@link SlickSetup}
	 * @param model
	 *            The {@link Setup} state that this {@link SlickSetup} displays.
	 */
	public SlickSetup(GameController game, int id, Setup model) {
		super(game, model.getName(), id, model);
		this.players = new RulerSelector();
		model.addObserver(this);
	}

	/**
	 * Enters the {@link SlickSetup}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.hideSaveOption();
		slick.showToolTip("Press 'Auto' to evenly distrubute units, then press 'Play'");
		players.init();
	}

	/**
	 * Renders this {@link SlickSetup}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		super.render(gc, frame);

		super.drawAllLinks(frame);
		super.drawArmies(frame);
		super.drawImages();
		super.drawButtons();
		super.drawMiniMap(frame);

		players.draw(frame);
		menus.draw(frame);

	}

	/**
	 * Performs the leave operations of this {@link SlickSetup}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		// Return all the players to their default position.
		super.game.forEachModelPlayer(player -> {

			final SlickPlayer slickPlayer = slick.modelView.getVisual(player);
			slickPlayer.setPosition(new Point(20, 80));

		});

		players.hide();

	}

	/**
	 * Parses a button press on this {@link SlickSetup}.
	 */
	@Override
	public void parseButton(int key, Point mousePosition) {
		super.parseButton(key, mousePosition);

		final ModelCountry highlighted = model.getSelected(0);

		/*
		 * If the player has highlighted a county then parse the button presses that
		 * change the owner ship of a country.
		 */
		if (highlighted != null) {

			switch (key) {
			case Input.KEY_1:
				if (game.isPlaying(1)) {
					model.swapRuler(highlighted, game.getModelPlayer(1));
				}
				break;
			case Input.KEY_2:
				if (game.isPlaying(2)) {
					model.swapRuler(highlighted, game.getModelPlayer(2));
				}
				break;
			case Input.KEY_3:
				if (game.isPlaying(3)) {
					model.swapRuler(highlighted, game.getModelPlayer(3));
				}
				break;
			case Input.KEY_4:
				if (game.isPlaying(4)) {
					model.swapRuler(highlighted, game.getModelPlayer(4));
				}
				break;
			case Input.KEY_SPACE:
				model.swapRuler(highlighted, null);
				break;
			}
		}
	}

	/**
	 * Updates this {@link SlickSetup}.
	 */
	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		final ModelCountry selected = model.getSelected(0);

		// If there is a selected country display the player selector.
		if (selected != null) {
			players.setPosition(slick.modelView.getVisual(selected).getArmyPosition());
			players.show();
		} else {
			players.hide();
		}

	}

	/**
	 * Moves all the {@link Component}s of this {@link SlickSetup}.
	 */
	@Override
	protected void panElements(Point panVector) {
		players.pan(panVector);
	}

	/**
	 * This displays the {@link SlickPlayer}s that are currently in the game to the
	 * user so that they may be clicked in order to change the ruler of the selected
	 * {@link SlickCountry} to that of the clicked {@link SlickPlayer}.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @since 2018-03-15
	 * @version 1.01.01
	 * 
	 * @see SlickSetup
	 *
	 */
	private final class RulerSelector {

		/**
		 * The offset in the x direction that the {@link SlickPlayer} icons will be
		 * displayed around the selected {@link SlickCountry}'s army.
		 */
		private static final int PLAYER_X_OFFSET = 120;

		/**
		 * The offset in the y direction that the {@link SlickPlayer} icons will be
		 * displayed around the selected {@link SlickCountry}'s army.
		 */
		private static final int PLAYER_Y_OFFSET = 80;

		/**
		 * The {@link SlickPlayer}s that the user can select using this
		 * {@link RulerSelector}.
		 */
		private final List<SlickPlayer> players;

		/**
		 * The {@link Point} position of this {@link RulerSelector}.
		 */
		private Point position;

		/**
		 * Whether or not this {@link RulerSelector} is visible or not.
		 */
		private boolean visible;

		/**
		 * Constructs a new {@link RulerSelector}.
		 */
		public RulerSelector() {
			this.players = new LinkedList<>();
			this.position = new Point(0, 0);
			this.visible = false;
		}

		/**
		 * Initialises this {@link RulerSelector} refreshing the {@link SlickPlayer}s
		 * stored in it.
		 */
		public void init() {

			if (!players.isEmpty()) {
				players.clear();
			}

			game.forEachModelPlayer(player -> {

				final SlickPlayer slickPlayer = slick.modelView.getVisual(player);
				players.add(slickPlayer);

			});

		}

		/**
		 * Moves all the {@link SlickPlayer}s in this {@link RulerSelector} along the
		 * specified {@link Point} vector.
		 * 
		 * @param vector
		 *            The vector the {@link SlickPlayer}s will be moved along.
		 */
		public void pan(Point vector) {

			players.forEach(player -> {

				final int x = player.getPosition().x + vector.x;
				final int y = player.getPosition().y + vector.y;

				player.setPosition(new Point(x, y));

			});

		}

		/**
		 * Sets this {@link RulerSelector} as visible to the user.
		 */
		public void show() {
			visible = true;
		}

		/**
		 * Sets this {@link RulerSelector} as invisible to the user.
		 */
		public void hide() {
			visible = false;
		}

		/**
		 * Draws this {@link RulerSelector} on screen.
		 * 
		 * @param frame
		 *            The {@link Frame} used to display this {@link RulerSelector} to
		 *            the user.
		 */
		public void draw(Frame frame) {

			if (!visible)
				return;

			// Draw each player on screen.
			players.forEach(player -> {

				frame.draw(player, new EventListener() {

					@Override
					public void mouseHover(Point mouse) {
						// Do nothing
					}

					@Override
					public void mouseClick(Point mouse, int mouseButton) {
						final ModelCountry highlighted = model.getSelected(0);

						if (highlighted != null) {
							model.swapRuler(highlighted, player.model);
						}
					}

					@Override
					public void draw(Frame frame) {
						frame.draw(player.getImage(), player.getPosition().x, player.getPosition().y);
					}

					@Override
					public void buttonPress(int key, Point mouse) {
						// Do nothing
					}
				});

			});
		}

		/**
		 * Sets the {@link Point} position of this {@link RulerSelector} to the
		 * specified
		 * 
		 * @param position
		 *            The new position of this {@link RulerSelector}.
		 */
		public void setPosition(Point position) {
			this.position = position;

			repositionPlayers();
		}

		/**
		 * Repositions all the {@link SlickPlayer}s in this {@link RulerSelector} based
		 * on {@link #position}.
		 */
		private void repositionPlayers() {

			int index = 0;

			// Iterate over each player
			for (SlickPlayer player : players) {

				switch (index) {
				case 0:
					repositionPlayer(player, new Point(-PLAYER_X_OFFSET, -PLAYER_Y_OFFSET));
					break;
				case 1:
					repositionPlayer(player, new Point(PLAYER_X_OFFSET, -PLAYER_Y_OFFSET));
					break;
				case 2:
					repositionPlayer(player, new Point(-PLAYER_X_OFFSET, +PLAYER_Y_OFFSET));
					break;
				case 3:
					repositionPlayer(player, new Point(+PLAYER_X_OFFSET, +PLAYER_Y_OFFSET));
					break;

				}
				index++;

			}

		}

		/**
		 * Repositions a specified {@link SlickPlayer}.
		 * 
		 * @param player
		 *            The {@link SlickPlayer} to be repositioned.
		 * @param offset
		 *            The offset from the {@link #position}.
		 */
		private void repositionPlayer(SlickPlayer player, Point offset) {

			final int x = position.x + offset.x - (player.getWidth() / 2);
			final int y = position.y + offset.y - (player.getHeight() / 2);

			player.setPosition(new Point(x, y));
		}

	}
}
