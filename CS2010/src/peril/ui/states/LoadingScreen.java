package peril.ui.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.io.FileParser;
import peril.ui.components.ProgressBar;
import peril.ui.components.Viewable;
import peril.ui.states.gameStates.SetupState;

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
	private Viewable background;

	/**
	 * The {@link Music} that will be played during the loading state.
	 */
	private Music music;

	/**
	 * THe {@link ProgressBar} that will be displayed on this loading screen.
	 */
	private ProgressBar progressBar;

	/**
	 * Constructs a new {@link LoadingScreen}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link LoadingScreen} is a part of.
	 * @param id
	 *            The is of this {@link LoadingScreen}.
	 */
	public LoadingScreen(Game game, int id) {
		super(game, NAME, id);
		this.index = 0;
		this.readers = new ArrayList<>();
		this.progressBar = new ProgressBar();
	}

	/**
	 * Adds a {@link Viewable} to this {@link LoadingScreen}. The last
	 * {@link Viewable} added will be the background {@link Image} of the
	 * {@link LoadingScreen} and will be scaled to fill the screen.
	 */
	@Override
	public void addImage(Viewable image) {
		background = image;
		super.addImage(image);
	}

	/**
	 * Enters the {@link LoadingScreen} state.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		// Scale the background image to fill the screen.
		background.setImage(background.getPosition(),
				background.getImage().getScaledCopy(gc.getWidth(), gc.getHeight()));

		changeMusic(gc);
		
		initProgressBar(gc.getWidth(), gc.getHeight());

		super.enter(gc, sbg);
	}

	/**
	 * Initialises this {@link LoadingScreen}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		// Set the music.
		music = getGame().musicHelper.read("loading");
		

	}

	/**
	 * Renders the {@link LoadingScreen}.S
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		progressBar.draw(g);

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
			getGame().autoDistributeCountries();
			getGame().enterState(getGame().setup.getID());
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
		if (getGame().getCurrentState().getID() == this.getID()) {
			throw new IllegalStateException("You cant add a file reader when the game is already loading.");
		}

		// Add the length of the reader to the total.
		progressBar.increaseTotal(reader.getLength());
		readers.add(reader);
	}

	/**
	 * Processes a mouse click on this {@link LoadingScreen}.
	 */
	@Override
	public void parseClick(int button, Point click) {
		super.clickedButton(click);
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
	 * Initialise the {@link LoadingScreen#progressBar}.
	 * 
	 * @param windowWidth
	 *            The width of the screen.
	 * @param windowHeight
	 *            The height of the screen.
	 */
	private void initProgressBar(int windowWidth, int windowHeight) {

		// Holds the loading bar padding.
		int windowPadding = (windowHeight / 10);

		// Holds the position of the loading bar
		Point barPosition = new Point(windowWidth / 20, windowHeight - windowPadding);

		// Holds the width of the loading bar.
		int barWidth = windowWidth - (windowHeight / 10) - windowPadding;

		progressBar.init(barPosition, barWidth, 40);
	}

}
