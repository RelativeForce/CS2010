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
import peril.views.slick.util.Button;
import peril.views.slick.util.Font;
import peril.views.slick.util.Viewable;

/**
 * 
 * The {@link InteractiveState} of the {@link Game} where the credits
 * are displayed.
 * 
 * @author Adrian_Wong
 *
 */
public final class Credits extends InteractiveState {
	
	/**
	 * The name of a specific {@link Credits}.
	 */
	private static final String NAME = "Credits";
	
	/**
	 * The {@link Font} that the entire {@link Credits} will be displayed in.
	 */
	private final Font creditsFont;
	
	/**
	 * The {@link Button} that will cause the {@link Credits} to return the the
	 * main menu.
	 */
	private final String menuButton;
	
	/**
	 * The background {@link Viewable} of the {@link Credits}.
	 */
	private Viewable background;
	
	/**
	 * The background music for this {@link Credits}.
	 */
	private Music music;
	
	/**
	 * Constructs a new {@link Credits}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The id of this {@link Credits}.
	 */
	public Credits(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);
		// TODO Auto-generated constructor stub
		creditsFont = new Font("Arial", Color.black, 20);
		menuButton = "menu";
		
	}	
	
	/**
	 * Initialise the visual elements of the {@link Credits}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		music = slick.music.read("credits");

		// Initialise Fonts
		creditsFont.init();
	}
	
	/**
	 * Enters the {@link Credits} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		changeMusic(gc);
		
		// Scale the background image to fill the screen.
		background.scale(gc.getWidth(), gc.getHeight());
	}

	@Override
	public void update(GameContainer gc, int delta, Frame frame) {
		// Do nothing
	}

	/**
	 * Render the {@link Credits}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		// TODO Auto-generated method stub	
		drawImages();
		drawButtons();
		
	}
	
	/**
	 * Retrieves the {@link Music} of this {@link Credits}.
	 */
	@Override
	public Music getMusic() {
		return music;
	}

}
