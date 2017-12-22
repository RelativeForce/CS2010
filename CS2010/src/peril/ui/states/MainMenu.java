package peril.ui.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.io.SaveFile;
import peril.io.fileParsers.MapReader;
import peril.io.fileReaders.TextFileReader;
import peril.ui.Font;
import peril.ui.components.lists.VisualList;
import peril.ui.components.menus.HelpMenu;

/**
 * Encapsulates the behaviour of the main menu.
 * 
 * @author Joshua_Eddy
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
	 * The width of the main menu
	 */
	private static final int WIDTH = 620;

	/**
	 * The height of the main menu
	 */
	private static final int HEIGHT = 500;

	/**
	 * Whether or not the user interface elements have been loaded from memory or
	 * not.
	 */
	private boolean uiLoaded;

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
	 * Constructs a new {@link MainMenu}
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The ID of this {@link MainMenu}.
	 */
	public MainMenu(Game game, int id) {
		super(game, STATE_NAME, id, HelpMenu.NULL_PAGE);

		uiLoaded = false;

		// Read the maps file containing the pre-load details of each map.
		mapsFile = TextFileReader.scanFile(game.assets.maps, "maps.txt");

		// Holds the y of all the menus
		int menuY = 415;

		maps = new VisualList<>(new Point(15, menuY), 110, 24, 3, 10);
		saves = new VisualList<>(new Point(130, menuY), 80, 18, 4, 10);

		// Populate the visual lists.
		getMaps();

		// Initialise the fonts;
		Font listFont = new Font("Arial", Color.black, 19);
		Font savesFont = new Font("Arial", Color.black, 14);
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
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		g.setBackground(Color.black);
		g.setLineWidth(3f);

		drawImages(g);
		drawButtons(g);

		textFont.draw(g, "Map: ", maps.getPosition().x, maps.getPosition().y - textFont.getHeight());
		maps.draw(g);

		textFont.draw(g, "Load: ", saves.getPosition().x, saves.getPosition().y - textFont.getHeight());
		saves.draw(g);
		
		g.destroy();

	}

	/**
	 * Processed a mouse click on this {@link MainMenu}.
	 */
	@Override
	public void parseClick(int button, Point click) {

		if (!super.clickedButton(click)) {
			if (!maps.click(click)) {
				saves.click(click);
			} else {
				checkSaves();
			}
		}
	}

	/**
	 * Processes a button press on this {@link MainMenu}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {

		if (key == Input.KEY_ENTER) {
			// Attempt to load the map
			try {
				loadGame();
			} catch (SlickException e) {

			}
		} else if (key == Input.KEY_UP) {
			if (maps.isClicked(mousePosition)) {
				maps.up();
				checkSaves();
			} else if (saves.isClicked(mousePosition)) {
				saves.up();
			}
		} else if (key == Input.KEY_DOWN) {
			if (maps.isClicked(mousePosition)) {
				maps.down();
				checkSaves();
			} else if (saves.isClicked(mousePosition)) {
				saves.down();
			}
		}
	}

	/**
	 * Initialises the visual elements of this {@link MainMenu}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		getGame().menus.initMenus();

		textFont.init();

		// Set the music that will be repeated by this state
		background = getGame().music.read("menu");

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

		try {
			getGame().reSize(WIDTH, HEIGHT);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates this {@link MainMenu}. When this state is first activated all
	 * the visual assets of the main menu are loaded in from memory.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

		if (!getGame().io.mainMenuLoader.isFinished()) {
			getGame().io.mainMenuLoader.parseLine();
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
	 * Loads the {@link MainMenu#listFont} {@link VisualList#getSelected()}
	 * into the {@link Game} and re-sizes the window of the {@link Game}.
	 */
	public void loadGame() throws SlickException {

		Map map = maps.getSelected();

		// Check width
		if (map.width <= 0) {
			throw new IllegalArgumentException("Width must greater than zero.");
		}

		// Check height
		if (map.height <= 0) {
			throw new IllegalArgumentException("Height must be greater than zero.");
		}

		// Reset all the player game elements
		getGame().players.emptyPlaying();
		getGame().setRoundNumber(0);
		getGame().players.challenges.clear();

		getGame().board.setName(map.name);

		// Only load the ui elements for the game on the first load
		if (!uiLoaded) {
			getGame().states.loadingScreen.addReader(getGame().io.gameLoader);
			uiLoaded = true;
		}

		MapReader mapLoader = new MapReader(getGame().assets.maps + map.name, getGame(), saves.getSelected());

		getGame().states.loadingScreen.addReader(mapLoader);

		// Loads the game assets and move into the set up state
		if (saves.getSelected() == SaveFile.DEFAULT) {
			
			getGame().states.playerSelection.setScreenSize(map.width, map.height);
			getGame().enterState(getGame().states.playerSelection);
			
		} else {
			
			getGame().reSize(map.width, map.height);
			getGame().enterState(getGame().states.loadingScreen);
		}

	}

	/**
	 * The visual representation of a map on screen.
	 */
	private void getMaps() {

		// Iterate through each map in the file and add the map name to the list of
		// maps.
		for (String line : mapsFile) {

			String[] mapDetails = line.split(",");

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
			if (file.existsIn(getGame().assets.maps + mapName)) {
				saves.add(file.name, file);
			}
		}
		saves.init();
	}

	/**
	 * A wrapper for the details of a map in the {@link Game}.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private class Map {

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
		 *            of the {@link Map}.
		 * @param width
		 *            of the {@link Map}.
		 * @param height
		 *            of the {@link Map}.
		 */
		public Map(String name, int width, int height) {
			this.width = width;
			this.height = height;
			this.name = name;
		}

	}
}
