package peril.views.slick.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Button;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
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
	 * The {@link Button} that will cause the {@link Credits} to exit the
	 * {@link Game}.
	 */
	private final String exitButton;
	
	
	/**
	 * The background music for this {@link Credits}.
	 */
	private Music music;
	
	/**
	 * The image for this {@link Credits}.
	 */
	private Image credits;
	
	/**
	 * The position for this {@link Credits}.
	 */
	private float creditsPosX = 0;
	
	/**
	 * The position for this {@link Credits}.
	 */
	private float creditsPosY = 0;
	
	/**
	 * This {@link Credits} does not run at the start.
	 */
	public static boolean creditsRunning = false;
	
	/**
	 * Holds the contents of the credits.txt file.
	 */
	//private final String[] creditsFile;
	
	/**
	 * Constructs a new {@link Credits}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The id of this {@link Credits}.
	 * @throws SlickException 
	 */
	public Credits(GameController game, int id) throws SlickException {
		super(game, NAME, id, HelpMenu.NULL_PAGE);	
		creditsFont = new Font("Arial", Color.black, 20);
		menuButton = "menu";
		exitButton = "exit";
	    credits = new Image("assets/ui/credits.png");
		
	}	
	
	/**
	 * Initialise the visual elements of the {@link Credits}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		music = slick.music.read("credits"); //add later

		// Initialise Fonts
		creditsFont.init();
	}
	
	/**
	 * Enters the {@link Credits} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		super.changeMusic(gc);

		int padding = 20;

		// Reposition menu button based on screen size.
		int menuX = gc.getWidth() - getButton(menuButton).getWidth() - padding;
		int menuY = gc.getHeight() - getButton(menuButton).getHeight() - padding;
		getButton(menuButton).setPosition(new Point(menuX, menuY));

		// Reposition exit button based on screen size.
		int exitX = padding;
		int exitY = gc.getHeight() - getButton(exitButton).getHeight() - padding;
		getButton(exitButton).setPosition(new Point(exitX, exitY));
			
	}

	@Override
	public void update(GameContainer gc, int delta, Frame frame) {	
		
		//makes the scrolling effect (if text cant fit on screen)
		if (creditsRunning == true) {
			creditsPosY -= delta * 0.5f;
			if (creditsPosY < 500) {
				creditsPosY += delta * 0.5f;			
			}
		}
	}

	/**
	 * Render the {@link Credits}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		// TODO Auto-generated method stub	
		drawImages();
		drawButtons();
		
		credits.draw(creditsPosX, creditsPosY);
		
	}
	
	/**
	 * Performs the exit state operations specific to the {@link Credits}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

	}
	
	/**
	 * Retrieves the {@link Music} of this {@link Credits}.
	 */
	@Override
	public Music getMusic() {
		return music;
	}

}
