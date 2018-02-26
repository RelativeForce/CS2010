package peril.views.slick.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Font;

/**
 * 
 * The {@link InteractiveState} of the {@link Game} where the instructions of
 * the entire game are displayed.
 * 
 * @author Adrian_Wong
 * 
 * @since 2018-02-26
 * @version 1.01.01
 * 
 * @see InteractiveState
 *
 */

public final class InstructionsState extends InteractiveState {

	/**
	 * The name of a specific {@link InstructionsState}.
	 */
	private static final String NAME = "Instructions";

	/**
	 * The {@link Font} that the entire {@link InstructionsState} will be displayed
	 * in.
	 */
	private final Font helpFont;

	/**
	 * Constructs a new {@link InstructionsState}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this
	 *            {@link InstructionsState} to query the state of the game.
	 * @param id
	 *            The id of this {@link Credits}.
	 */
	public InstructionsState(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);
		helpFont = new Font("Arial", Color.black, 20);
	}

	/**
	 * Initialise the visual elements of the {@link InstructionsState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
		
		// Initialise Fonts
		helpFont.init();
	}

	@Override
	public void update(GameContainer gc, int delta, Frame frame) {

	}

	/**
	 * Render the {@link InstructionsState}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		drawImages();
		drawButtons();

	}

	/**
	 * Retrieves the {@link Music} of this {@link InstructionsState}.
	 */
	@Override
	public Music getMusic() {
		return null;
	}

}
