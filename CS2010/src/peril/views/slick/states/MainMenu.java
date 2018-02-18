package peril.views.slick.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.io.SaveFile;
import peril.io.TextFileReader;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.components.VisualList;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * Encapsulates the behaviour of the main menu.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see InteractiveState
 *
 */
public final class MainMenu extends InteractiveState {

	/**
	 * The name of a specific {@link InteractiveState}.
	 */
	private static final String STATE_NAME = "Main Menu";

	/**
	 * The width of the main menu window.
	 */
	private static final int WIDTH = 620;

	/**
	 * The height of the main menu window.
	 */
	private static final int HEIGHT = 500;

	/**
	 * Holds the contents of the maps.txt file.
	 */
	private final String[] mapsFile;

	/**
	 * The {@link VisualList} of the maps read from the map file.
	 */
	private final VisualList<Map> maps;

	/**
	 * The {@link VisualList} of the saved games read from the saved file.
	 */
	private final VisualList<SaveFile> saves;

	/**
	 * The {@link Font} of normal text on the {@link MainMenu}.
	 */
	private final Font textFont;

	/**
	 * The music that plays in the background of this {@link MainMenu}.
	 */
	private Music background;

	/**
	 * Whether or not the user interface elements have been loaded from memory or
	 * not.
	 */
	private boolean uiLoaded;

	/**
	 * Constructs a new {@link MainMenu}.
	 * 
	 * @param game
	 *            The {@link GameController} this state is a part of.
	 * @param id
	 *            The ID of this {@link MainMenu}.
	 */
	public MainMenu(GameController game, int id) {
		super(game, STATE_NAME, id, HelpMenu.NULL_PAGE);

		uiLoaded = false;

		// Read the maps file containing the pre-load details of each map.
		mapsFile = TextFileReader.scanFile(game.getDirectory().getMapsPath(), "maps.txt");

		// Holds the y of all the menus
		final int menuY = 415;

		maps = new VisualList<>(new Point(15, menuY), 110, 24, 3, 10);
		saves = new VisualList<>(new Point(130, menuY), 80, 18, 4, 10);

		// Populate the visual lists.
		getMaps();

		// Initialise the fonts;
		final Font listFont = new Font("Arial", Color.black, 19);
		final Font savesFont = new Font("Arial", Color.black, 14);
		textFont = new Font("Calibri", Color.red, 18);

		// Assign list fonts
		maps.setFont(listFont);
		saves.setFont(savesFont);

		// Add components so they are initialised when the state is.
		super.addComponent(maps);
		super.addComponent(saves);

	}

	/**
	 * Renders this {@link MainMenu} on screen.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		frame.setBackground(Color.black);
		frame.setLineWidth(3f);

		drawImages();
		drawButtons();

		// Draw the maps list.
		frame.draw(textFont, "Map: ", maps.getPosition().x, maps.getPosition().y - textFont.getHeight());
		frame.draw(maps, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int button) {
				maps.click(mouse);
				checkSaves();
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				if (maps.isClicked(mouse)) {
					if (key == Input.KEY_UP) {
						maps.up();
						checkSaves();
					} else if (key == Input.KEY_DOWN) {
						maps.down();
						checkSaves();
					}
				}
			}

			@Override
			public void draw(Frame frame) {
				maps.draw(frame);
			}
		});

		// Draw save list.
		frame.draw(textFont, "Load: ", saves.getPosition().x, saves.getPosition().y - textFont.getHeight());
		frame.draw(saves, new EventListener() {

			@Override
			public void mouseHover(Point mouse, int delta) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int button) {
				saves.click(mouse);
			}

			@Override
			public void buttonPress(int key, Point mouse) {

				if (saves.isClicked(mouse)) {
					if (key == Input.KEY_UP) {
						saves.up();
						checkSaves();
					} else if (key == Input.KEY_DOWN) {
						saves.down();
						checkSaves();
					}
				}
			}

			@Override
			public void draw(Frame frame) {
				saves.draw(frame);
			}
		});
	}

	/**
	 * Processes a button press on this {@link MainMenu}.
	 */
	@Override
	public void parseButton(int key, Point mousePosition) {
		super.parseButton(key, mousePosition);

		if (key == Input.KEY_ENTER) {
			// Attempt to load the map
			try {
				loadGame();
			} catch (SlickException e) {

			}
		}
	}

	/**
	 * Initialises the visual elements of this {@link MainMenu}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		slick.menus.initMenus();

		textFont.init();

		// Set the music that will be repeated by this state
		background = slick.music.read("menu");

		checkSaves();
	}

	/**
	 * Enters the {@link MainMenu}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		// If the music was muted in game start reset it back to being on.
		gc.setMusicVolume(1f);

		checkSaves();

		changeMusic(gc);

		slick.modelView.clear();
		game.getModelBoard().reset();

		try {
			slick.reSize(WIDTH, HEIGHT);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates this {@link MainMenu}. When this state is first activated all the
	 * visual assets of the main menu are loaded in from memory.
	 */
	@Override
	public void update(GameContainer gc, int delta, Frame frame) {

		// Load Assets
		if (!slick.io.mainMenuLoader.isFinished()) {
			slick.io.mainMenuLoader.parseLine();
		}

	}

	/**
	 * Retrieves the {@link MainMenu} {@link Music}.
	 */
	@Override
	public Music getMusic() {
		return background;
	}

	/**
	 * Loads the {@link MainMenu#maps} {@link VisualList#getSelected()} into the
	 * {@link GameController} and re-sizes the window of the game.
	 */
	public void loadGame() throws SlickException {

		final Map map = maps.getSelected();

		// Check width
		if (map.width <= 0) {
			throw new IllegalArgumentException("Width must greater than zero.");
		}

		// Check height
		if (map.height <= 0) {
			throw new IllegalArgumentException("Height must be greater than zero.");
		}

		// Reset all the player game elements
		game.resetGame();
		game.setBoardName(map.name);

		// Only load the ui elements for the game on the first load
		if (!uiLoaded) {
			slick.states.loadingScreen.addReader(slick.io.gameLoader);
			uiLoaded = true;
		}

		slick.states.loadingScreen.addReader(slick.getMapLoader(map.name, saves.getSelected()));

		// Loads the game assets and move into the set up state
		if (saves.getSelected() == SaveFile.DEFAULT) {

			slick.states.playerSelection.setScreenSize(map.width, map.height);
			slick.enterState(slick.states.playerSelection);

		} else {
			// Reset the board
			game.getModelBoard().reset();
			slick.reSize(map.width, map.height);
			slick.enterState(slick.states.loadingScreen);
		}

	}

	/**
	 * Populates the maps visual list of all the map options.
	 */
	private void getMaps() {

		// Iterate through each map in the file and add the map name to the list of
		// maps.
		for (String line : mapsFile) {

			final String[] mapDetails = line.split(",");

			if (!mapDetails[0].isEmpty() && mapDetails.length == 3) {

				// Parse width
				int width;
				try {
					width = Integer.parseInt(mapDetails[1]);
				} catch (Exception e) {
					throw new IllegalArgumentException("Width must be an integer");
				}

				// Parse height
				int height;
				try {
					height = Integer.parseInt(mapDetails[2]);
				} catch (Exception e) {
					throw new IllegalArgumentException("Height must be an integer");
				}

				maps.add(mapDetails[0], new Map(mapDetails[0], width, height));
			}

		}

	}

	/**
	 * Checks for present saves for the currently selected map.
	 */
	private void checkSaves() {

		String mapName = maps.getSelected().name;

		saves.clear();

		// Iterate through each save and check if it exists in the current maps
		// directory, If it does then add it to the saves list.
		for (SaveFile file : SaveFile.values()) {
			if (file.existsIn(game.getDirectory().asMapPath(mapName))) {
				saves.add(file.name, file);
			}
		}
		saves.init();
	}

	/**
	 * A wrapper for the details of a map in the {@link MainMenu#maps} list.
	 * 
	 * @author Joshua_Eddy
	 *
	 * @since 2018-02-17
	 * @version 1.01.01
	 */
	private final class Map {

		/**
		 * The name of the {@link Map}.
		 */
		public final String name;

		/**
		 * The width of the {@link Map}.
		 */
		public final int width;

		/**
		 * The height of the {@link Map}.
		 */
		public final int height;

		/**
		 * Constructs a new {@link Map}.
		 * 
		 * @param name
		 *            The name of the {@link Map}.
		 * @param width
		 *            The width of the {@link Map}.
		 * @param height
		 *            The height of the {@link Map}.
		 */
		public Map(String name, int width, int height) {
			this.width = width;
			this.height = height;
			this.name = name;
		}

	}
}
