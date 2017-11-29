package peril.ui.states.menuStates;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.io.SaveFile;
import peril.io.fileParsers.MapReader;
import peril.io.fileReaders.TextFileReader;
import peril.ui.Font;
import peril.ui.components.lists.VisualList;
import peril.ui.states.InteractiveState;

/**
 * Encapsulates the behaviour of the main menu.
 * 
 * @author Joshua_Eddy
 * 
 * @see InteractiveState
 *
 */
public class MainMenuState extends InteractiveState {

	/**
	 * The name of a specific {@link InteractiveState}.
	 */
	private static final String STATE_NAME = "Main Menu";

	/**
	 * The width of the main menu
	 */
	private static final int WIDTH = 400;

	/**
	 * The height of the main menu
	 */
	private static final int HEIGHT = 300;

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
	private VisualList<Map> maps;

	/**
	 * The {@link VisualList} of the number of {@link Player}s that can be in the
	 * {@link Game}.
	 */
	private VisualList<Integer> players;

	/**
	 * The {@link VisualList} of the saved games read from the saved file.
	 */
	private VisualList<SaveFile> saves;

	/**
	 * The {@link Font} of normal text on the {@link MainMenuState}.
	 */
	private Font textFont;

	/**
	 * The music that plays in the background of this {@link MainMenuState}.
	 */
	private Music background;

	/**
	 * Constructs a new {@link MainMenuState}
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param id
	 *            The ID of this {@link MainMenuState}.
	 */
	public MainMenuState(Game game, int id) {
		super(game, STATE_NAME, id);

		uiLoaded = false;

		mapsFile = TextFileReader.scanFile(game.assets.maps, "maps.txt");

		maps = new VisualList<>(15, 220, 110, 22, 3, 10);
		players = new VisualList<>(130, 220, 20, 22, 3, 5);
		saves = new VisualList<>(190, 220, 80, 22, 3, 10);

		getMaps();
		getPlayers();

		// Initialise the fonts;
		Font listFont = new Font("Arial", Color.black, 18);
		textFont = new Font("Calibri", Color.red, 18);

		// Assign list fonts
		maps.setFont(listFont);
		players.setFont(listFont);
		saves.setFont(listFont);

		super.addComponent(maps);
		super.addComponent(players);
		super.addComponent(saves);

	}

	/**
	 * Renders this {@link MainMenuState} on screen.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		g.setBackground(Color.black);

		drawImages(g);

		drawButtons(g);

		if (saves.getSelected() == SaveFile.DEFAULT) {
			textFont.draw(g, "Players: ", 130, 200);
			players.draw(g);
		}

		textFont.draw(g, "Map: ", 15, 200);
		maps.draw(g);

		textFont.draw(g, "Load: ", 190, 200);
		saves.draw(g);

	}

	/**
	 * Processed a mouse click on this {@link MainMenuState}.
	 */
	@Override
	public void parseClick(int button, Point click) {

		if (!super.clickedButton(click)) {
			if (!maps.click(click)) {
				if (!saves.click(click)) {
					players.click(click);
				}
			} else {
				checkSaves();
			}
		}
	}

	/**
	 * Processes a button press on this {@link MainMenuState}.
	 */
	@Override
	public void parseButton(int key, char c, Point mousePosition) {

		if (key == Input.KEY_ENTER) {
			// Attempt to load the map
			try {
				loadMap();
			} catch (SlickException e) {

			}
		} else if (key == Input.KEY_UP) {
			if (maps.isClicked(mousePosition)) {
				maps.up();
				checkSaves();
			} else if (players.isClicked(mousePosition)) {
				players.up();
			} else if (saves.isClicked(mousePosition)) {
				saves.up();
			}
		} else if (key == Input.KEY_DOWN) {
			if (maps.isClicked(mousePosition)) {
				maps.down();
				checkSaves();
			} else if (players.isClicked(mousePosition)) {
				players.down();
			} else if (saves.isClicked(mousePosition)) {
				saves.down();
			}
		}
	}

	/**
	 * Initialises the visual elements of this {@link MainMenuState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);

		textFont.init();

		// Set the music that will be repeated by this state
		background = getGame().io.musicHelper.read("menu");

		checkSaves();
	}

	/**
	 * Enters the {@link MainMenuState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {

		// If the music was muted in game start reset it back to being on.
		gc.setMusicVolume(1f);

		changeMusic(gc);
		try {
			getGame().reSize(WIDTH, HEIGHT);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates this {@link MainMenuState}. When this state is first activated all
	 * the visual assets of the main menu are loaded in from memory.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

		if (!getGame().io.mainMenuLoader.isFinished()) {
			getGame().io.mainMenuLoader.parseLine();
		}

	}

	/**
	 * Leaves this {@link MainMenuState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		// TODO depending on load implementation
	}

	/**
	 * Retrieves the {@link MainMenuState} {@link Music}.
	 */
	@Override
	public Music getMusic() {
		return background;
	}

	/**
	 * Loads the {@link MainMenuState#listFont} {@link VisualList#getSelected()}
	 * into the {@link Game} and re-sizes the window of the {@link Game}.
	 */
	public void loadMap() throws SlickException {

		Map map = maps.getSelected();

		// Check width
		if (map.width <= 0) {
			throw new IllegalArgumentException("Width must greater than zero.");
		}

		// Check height
		if (map.height <= 0) {
			throw new IllegalArgumentException("Height must be greater than zero.");
		}

		// Loads the game assets and move into the set up state
		if (saves.getSelected() == SaveFile.DEFAULT) {
			getGame().players.setInitialPlayers(players.getSelected());
		}

		int screenWidth = getGame().getContainer().getScreenWidth();
		int screenHeight = getGame().getContainer().getScreenHeight();

		int width = (map.width > screenWidth) ? screenWidth : map.width;
		int height = (map.height > screenHeight) ? screenHeight : map.height;

		getGame().reSize(width, height);
		getGame().board.setName(map.name);

		// Only load the ui elements for the game on the first load
		if (!uiLoaded) {
			getGame().states.loadingScreen.addReader(getGame().io.gameLoader);
			uiLoaded = true;
		}

		MapReader mapLoader = new MapReader(getGame().assets.maps + File.separatorChar + map.name, getGame(),
				saves.getSelected());

		getGame().states.loadingScreen.addReader(mapLoader);
		getGame().enterState(getGame().states.loadingScreen);

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
	 * The visual representation of the list of players on screen.
	 */
	private void getPlayers() {
		players.add("2", 2);
		players.add("3", 3);
		players.add("4", 4);
	}

	private void checkSaves() {
		String mapName = maps.getSelected().name;
		saves.clear();
		for (SaveFile file : SaveFile.values()) {
			if (file.existsIn(getGame().assets.maps + File.separatorChar + mapName)) {
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
