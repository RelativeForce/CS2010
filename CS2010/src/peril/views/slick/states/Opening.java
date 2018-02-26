package peril.views.slick.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.SlickGame;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.io.ImageReader;
import peril.views.slick.util.Point;

/**
 * 
 * The opening splash screen of the game.
 * 
 * @author Joshua_Eddy
 *
 * @since 2018-02-26
 * @version 1.02.01
 *
 * @see InteractiveState
 *
 */
public final class Opening extends InteractiveState {

	/**
	 * The name of the {@link Opening}.
	 */
	private static final String NAME = "Opening";

	/**
	 * The number of seconds that this {@link Opening} will take to play out.
	 */
	private static final int DURATION = 4;

	/**
	 * The current {@link Image} of the splash screen.
	 */
	private Image splash;

	/**
	 * The initial {@link Image} of the splash screen.
	 */
	private Image initialSplash;

	/**
	 * The position of the splash screen.
	 */
	private Point splashPosition;
	
	/**
	 * The backgound {@link Image}.
	 */
	private Image background;

	/**
	 * The current scale of the {@link #splash} from the {@link #initialSplash}.
	 */
	private float splashScale;

	/**
	 * The maximum scale of the {@link #splash} before this state will enter the
	 * main menu..
	 */
	private float maxSplashScale;

	/**
	 * The initial scale of the {@link #splash}.
	 */
	private float initialSplashScale;

	/**
	 * The opacity value for the black box that covers the frame at the start.
	 */
	private float fadeFactor;

	/**
	 * The incrementing value used to determine {@link #fadeFactor} after each frame.
	 */
	private float fade;

	/**
	 * The increment of the fade.
	 */
	private float fadeIncrement;

	/**
	 * Whether or not the bah sound effect has played yet.
	 */
	private boolean bah;

	/**
	 * Constructs a new {@link Opening}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link Opening} to
	 *            query the state of the game.
	 * @param id
	 *            The id of this {@link InteractiveState}.
	 */
	public Opening(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);

		bah = false;
		fadeFactor = 255;
		fade = -1;
		fadeIncrement = (float) 2 / (DURATION * SlickGame.FPS);
	}

	/**
	 * Initialises the visual elements of the {@link Opening}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
		
		this.background = ImageReader.getImage(game.getDirectory().getSplashPath() + "background.jpg").getScaledCopy(gc.getWidth(), gc.getHeight());
		
		this.initialSplash = ImageReader.getImage(game.getDirectory().getSplashPath() + "logo.png");
		this.initialSplashScale = (float) (gc.getWidth() / 3) / initialSplash.getWidth();
		this.splashScale = initialSplashScale;
		this.maxSplashScale = (float) (gc.getWidth() * 9 / 10) / initialSplash.getWidth();
		this.splash = initialSplash.getScaledCopy(splashScale);
		this.splashPosition = getGoatPosition(gc);

	}

	/**
	 * Renders the {@link Opening} on screen.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		frame.draw(background,0, 0);
		frame.draw(splash, splashPosition.x, splashPosition.y);
		frame.fillRect(0, 0, gc.getWidth(), gc.getHeight());
	}

	/**
	 * Updates the {@link Opening} between frames.
	 */
	@Override
	public void update(GameContainer gc, int delta, Frame frame) {

		// If the splash is at full zoom
		if (splashScale >= maxSplashScale) {
			game.getView().enterMainMenu();
		} else {

			final float ratio = (((float) 1000f) / (float) SlickGame.FPS) / ((float) delta);

			// If the black box has not disappeared.
			if (fade <= 0) {
				this.fade += (fadeIncrement * ratio);
				this.fadeFactor = (float) (255 * Math.pow(fade, 2));

			} 
			// If the bah has not been played.
			else if (!bah) {
				try {
					new Sound(game.getDirectory().getMusicPath() + "bah.ogg").play();
					bah = true;
				} catch (SlickException e) {
					e.printStackTrace();
				}
			}

			frame.setColor(new Color(0, 0, 0, fadeFactor));

			this.splashScale += ((float) (maxSplashScale - initialSplashScale) / (DURATION * SlickGame.FPS)) / ratio;
			this.splash = initialSplash.getScaledCopy(splashScale);
			this.splashPosition = getGoatPosition(gc);

		}
	}

	/**
	 * Retrieves the music from the frame.
	 */
	@Override
	public Music getMusic() {
		return null;
	}

	/**
	 * Retrieves the position of the {@link #splash} based on its current
	 * dimensions.
	 * 
	 * @param gc
	 *            The {@link GameContainer} that displays the game.
	 * @return The new position of the {@link #splash}.
	 */
	private Point getGoatPosition(GameContainer gc) {
		final int x = (gc.getWidth() - splash.getWidth()) / 2;
		final int y = (gc.getHeight() - splash.getHeight()) / 2;
		return new Point(x, y);
	}

}
