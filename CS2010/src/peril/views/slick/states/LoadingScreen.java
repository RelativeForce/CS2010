package peril.views.slick.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.GameController;
import peril.io.FileParser;
import peril.views.slick.Font;
import peril.views.slick.Frame;
import peril.views.slick.Point;
import peril.views.slick.Viewable;
import peril.views.slick.components.ProgressBar;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.io.ImageReader;
import peril.views.slick.states.gameStates.CoreGameState;
import peril.views.slick.states.gameStates.SetupState;

/**
 * 
 * Encapsulates the behaviour of a loading screen. Using {@link FileParser}s
 * this object will load objects from memory.
 * 
 * @author Joshua_Eddy
 *
 */
public final class LoadingScreen extends InteractiveState {

	/**
	 * Holds the name of this {@link LoadingScreen}.
	 */
	private final static String NAME = "loading screen";

	/**
	 * Holds the first {@link CoreGameState} the game should load into if the map is
	 * loaded from a save.
	 */
	private CoreGameState firstState;

	/**
	 * Holds the {@link List} of {@link FileParser}s this {@link LoadingScreen} will
	 * load.
	 */
	private final List<FileParser> readers;

	/**
	 * The index of the current {@link FileParser} being parsed.
	 */
	private int index;

	/**
	 * The background {@link Viewable} of the {@link LoadingScreen}.
	 */
	private Image background;

	/**
	 * The {@link Music} that will be played during the loading state.
	 */
	private Music music;

	/**
	 * THe {@link ProgressBar} that will be displayed on this loading screen.
	 */
	private final ProgressBar progressBar;

	private final Font textFont;

	/**
	 * Constructs a new {@link LoadingScreen}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link LoadingScreen} is a part of.
	 * @param id
	 *            The is of this {@link LoadingScreen}.
	 */
	public LoadingScreen(GameController game, int id) {
		super(game, NAME, id, HelpMenu.NULL_PAGE);
		this.index = 0;
		this.readers = new ArrayList<>();
		this.progressBar = new ProgressBar(new Point(0, 0), 100, 20);
		super.addComponent(progressBar);
		this.textFont = new Font("Arial", Color.red, 25);
	}

	/**
	 * Enters the {@link LoadingScreen} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		// Scale the background image to fill the screen.
		background = ImageReader.getImage(game.getDirectory().getUIPath() + "perilLogo.png").getScaledCopy(gc.getWidth(), gc.getHeight());

		changeMusic(gc);

		reSizeProgressBar(gc.getWidth(), gc.getHeight());
	}

	/**
	 * Initialises this {@link LoadingScreen}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		// Set the music.
		music = slick.music.read("loading");

		textFont.init();

	}

	/**
	 * Renders the {@link LoadingScreen}.S
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		frame.draw(background, 0, 0);
		
		drawImages();
		drawButtons();

		progressBar.draw(frame);
		

		if (index == readers.size()) {

			final String text = "Finishing up";
			final int x = (gc.getWidth() / 2) - (textFont.getWidth(text) / 2);
			final int y = progressBar.getPosition().y + (progressBar.getHeight() / 2) - (textFont.getHeight() / 2);

			frame.draw(textFont, text, x, y);
		}
	}

	/**
	 * Parses the line from the current {@link FileParser}. Enters
	 * {@link SetupState} when all the {@link FileParser}s are finished.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		// If the index is equal to the size of the readers list then the last reader is
		// finished.
		if (index == readers.size()) {

			// Enter set up state
			slick.enterState(firstState != null ? firstState : slick.states.setup);
		}
		// Otherwise reader the current readers line.
		else {

			FileParser reader = readers.get(index);

			// If the reader is not finished parse its current line.
			if (!reader.isFinished()) {
				reader.parseLine();
				progressBar.increment();
			}
			// Otherwise move to the next reader.
			else {
				index++;
			}
		}

	}

	/**
	 * Adds a {@link FileParser} to the {@link LoadingScreen} to be parsed.
	 * 
	 * @param reader
	 *            {@link FileParser}
	 */
	public void addReader(FileParser reader) {

		if (reader == null) {
			throw new NullPointerException("Reader cannot be null.");
		}

		/**
		 * If the game is currently loading.
		 */
		if (slick.getCurrentState().getID() == this.getID()) {
			throw new IllegalStateException("You cant add a file reader when the game is already loading.");
		}

		// Add the length of the reader to the total.
		progressBar.increaseTotal(reader.getLength());
		readers.add(reader);
	}

	/**
	 * Performs the exit state operations specific to this {@link LoadingScreen}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		readers.clear();

		progressBar.reset();

		index = 0;

		// Turn off the loading music.
		container.setMusicOn(false);

		firstState = null;

		slick.menus.refreshSaveFiles();

		slick.centerBoard();

	}

	/**
	 * Processes a button press on this {@link LoadingScreen}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {
		// DO NOTHING
	}

	/**
	 * Retrieves the loading {@link Music}.
	 */
	@Override
	public Music getMusic() {
		return music;
	}

	/**
	 * Sets the {@link CoreGameState} that the game will go to once the game has
	 * finished loading.
	 * 
	 * @param saveState
	 *            {@link CoreGameState}
	 */
	public void setFirstState(CoreGameState saveState) {
		firstState = saveState;
	}

	/**
	 * Initialise the {@link LoadingScreen#progressBar}.
	 * 
	 * @param windowWidth
	 *            The width of the screen.
	 * @param windowHeight
	 *            The height of the screen.
	 */
	private void reSizeProgressBar(int windowWidth, int windowHeight) {

		// Holds the loading bar padding.
		int windowPaddingY = windowHeight / 10;
		int windowPaddingX = windowWidth / 10;

		// Holds the position of the loading bar
		Point barPosition = new Point(windowWidth / 20, windowHeight - windowPaddingY);

		// Holds the width of the loading bar.
		int barWidth = windowWidth - windowPaddingX;

		progressBar.setPosition(barPosition);
		progressBar.setWidth(barWidth);
		progressBar.setHeight(windowPaddingY / 2);
	}

}
