package peril.views.slick.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Font;

/**
 * 
 * The {@link InteractiveState} of the {@link Game} where the instructions
 * of the entire game are displayed.
 * 
 * @author Adrian_Wong
 *
 */

public final class HelpState extends InteractiveState {
	
	/**
	 * The name of a specific {@link HelpState}.
	 */
	private static final String NAME = "Help";
	
	/**
	 * The {@link Font} that the entire {@link HelpState} will be displayed in.
	 */
	private final Font helpFont;
	
	/**
	 * The background music for this {@link HelpState}.
	 */
	private Music music;
	
	
	/**
	 * Constructs a new {@link HelpState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The id of this {@link Credits}.
	 * @throws SlickException 
	 */
	public HelpState(GameController game, int id) throws SlickException {
		super(game, NAME, id, HelpMenu.NULL_PAGE);
		helpFont = new Font("Arial", Color.black, 20);
	}
	
	/**
	 * Initialise the visual elements of the {@link HelpState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		//music = slick.music.read("..."); //add later

		// Initialise Fonts
		helpFont.init();
	}
	
	@Override
	public void update(GameContainer gc, int delta, Frame frame) {
		
		
	}

	/**
	 * Render the {@link HelpState}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		
		drawImages();
		drawButtons();
		
	}

	/**
	 * Retrieves the {@link Music} of this {@link HelpState}.
	 */
	@Override
	public Music getMusic() {
		return music;
	}

}
