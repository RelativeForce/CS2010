package peril.ui.states.menuStates;

import java.util.ArrayList;
import java.util.List;

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
import peril.io.fileParsers.MapReader;
import peril.io.fileReaders.TextFileReader;
import peril.ui.components.Font;
import peril.ui.components.VisualList;
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

	private static final int WIDTH = 400;

	private static final int HEIGHT = 300;

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
	private VisualList<PlayerArray> players;

	/**
	 * The {@link VisualList} of the saved games read from the saved file.
	 */
	private VisualList<Load> load;

	/**
	 * The {@link Font} used for displaying {@link MainMenuState#maps}.
	 */
	private Font listFont;

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
	public MainMenuState(Game game, int id, String mapsFilePath) {

		super(game, STATE_NAME, id);
		mapsFile = TextFileReader.scanFile(mapsFilePath, "maps.txt");
		maps = new VisualList<>(15, 220, 110, 22, 3, 10);
		players = new VisualList<>(160, 220, 20, 22, 3, 5);
		load = new VisualList<>(220, 220, 65, 22, 3, 10);
		getMaps();
		getPlayers();

	}

	/**
	 * Renders this {@link MainMenuState} on screen.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		g.setBackground(Color.black);

		drawImages(g);

		drawButtons(g);

		textFont.draw(g, "Players: ", 160, 200);
		textFont.draw(g, "Map: ", 15, 200);
		textFont.draw(g, "Load: ", 220, 200);

		maps.draw(g);
		players.draw(g);
		load.draw(g);
	}

	/**
	 * Processed a mouse click on this {@link MainMenuState}.
	 */
	@Override
	public void parseClick(int button, Point click) {

		if (!super.clickedButton(click)) {
			if (!maps.click(click)) {
				players.click(click);
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
			} else if (players.isClicked(mousePosition)) {
				players.up();
			}
		} else if (key == Input.KEY_DOWN) {
			if (maps.isClicked(mousePosition)) {
				maps.down();
			} else if (players.isClicked(mousePosition)) {
				players.down();
			}
		}
	}

	/**
	 * Initialises the visual elements of this {@link MainMenuState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		// Initialise the fonts;
		listFont = new Font("Arial", Color.black, 18);
		textFont = new Font("Arial", Color.blue, 15);

		// Initialise the maps list and all its elements
		maps.init();
		maps.setFont(listFont);

		// Initialise the players list and all its elements
		players.init();
		players.setFont(listFont);

		// Set the music that will be repeated by this state
		background = getGame().io.musicHelper.read("menu");

	}

	/**
	 * Enters the {@link MainMenuState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
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
		PlayerArray playersArray = players.getSelected();

		// Check width
		if (map.width <= 0) {
			throw new IllegalArgumentException("Width must greater than zero.");
		}

		// Check height
		if (map.height <= 0) {
			throw new IllegalArgumentException("Height must be greater than zero.");
		}

		// Loads the game assets and move into the set up state
		getGame().players.set(playersArray.players);

		int screenWidth = getGame().getContainer().getScreenWidth();
		int screenHeight = getGame().getContainer().getScreenHeight();

		int width = (map.width > screenWidth) ? screenWidth : map.width;
		int height = (map.height > screenHeight) ? screenHeight : map.height;

		getGame().reSize(width, height);
		getGame().board.setName(map.name);
		getGame().states.loadingScreen.addReader(getGame().io.gameLoader);
		getGame().states.loadingScreen.addReader(new MapReader(getGame().mapsDirectory + map.name, getGame().board));
		getGame().states.loadingScreen.addReader(getGame().io.challengeLoader);
		getGame().enterState(getGame().states.loadingScreen.getID());

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
		players.add("2", new PlayerArray(2));
		players.add("3", new PlayerArray(3));
		players.add("4", new PlayerArray(4));
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

	/**
	 * Holds an array of {@link Player}s in {@link MainMenuState#players}.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private class PlayerArray {

		/**
		 * An array of {@link players}.
		 */
		public final List<Player> players;

		/**
		 * Constructs a new {@link PlayerArray}.
		 * 
		 * @param numberOfPlayers
		 *            The number of {@link Player}s in the {@link PlayerArray}.
		 */
		public PlayerArray(int numberOfPlayers) {

			players = new ArrayList<Player>();

			switch (numberOfPlayers) {
			case 1:
				players.add(Player.ONE);
				break;
			case 2:
				players.add(Player.ONE);
				players.add(Player.TWO);
				break;
			case 3:
				players.add(Player.ONE);
				players.add(Player.TWO);
				players.add(Player.THREE);
				break;
			case 4:
				players.add(Player.ONE);
				players.add(Player.TWO);
				players.add(Player.THREE);
				players.add(Player.FOUR);
				break;
			default:
				throw new IllegalArgumentException(
						numberOfPlayers + " is not a valid number of players, it should be 1 to 4. ");
			}

		}
	}

	/**
	 * 
	 * Holds an array of saved games in {@link MainMenuState}
	 * 
	 * @author Mohammad ali Sayed Ackbar
	 *
	 */
	private class Load {
		// TODO Implementation to hold saved games
	}

}
