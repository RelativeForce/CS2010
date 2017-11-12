package peril.ui.states.menuStates;

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
import peril.io.TextFileReader;
import peril.ui.states.InteractiveState;
import peril.ui.visual.Element;
import peril.ui.visual.Font;
import peril.ui.visual.VisualList;

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
	 * Holds the contents of the maps.txt file.
	 */
	private final String[] mapsFile;

	/**
	 * The {@link VisualList} of the maps read from the map file.
	 */
	private VisualList<Map> maps;

	private VisualList<PlayerArray> players;

	/**
	 * The {@link Font} used for displaying {@link MainMenuState#maps}.
	 */
	private Font mapFont;

	/**
	 * The {@link Font} for displaying the name of the game.
	 */
	private Font headingFont;
	
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
		maps = new VisualList<>(15, 90, 90, 22, 2, 10);
		players = new VisualList<>(200, 90, 20, 22, 3, 5);
		getMaps();
		getPlayers();

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		g.setBackground(Color.lightGray);
		
		super.render(gc, sbg, g);
		
		textFont.draw(g, "Players: ", 200, 70);
		textFont.draw(g, "Map: ", 15, 70);
		headingFont.draw(g, "Peril", 10, 5);
		
		maps.draw(g);
		players.draw(g);
	}

	@Override
	public void parseClick(int button, Point click) {
		if (!super.clickButton(click)) {
			if (!maps.click(click)) {
				players.click(click);
			}
		}
	}

	@Override
	public void parseButton(int key, char c) {

		if (key == Input.KEY_ENTER) {
			// Attempt to load the map
			try {
				loadMap();
			} catch (SlickException e) {

			}
		}
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		// Initialise the fonts;
		mapFont = new Font("Arial", Color.green, 20);
		headingFont = new Font("Arial", Color.red, 56);
		textFont = new Font("Arial", Color.blue, 15);

		// Initialise the maps list and all its elements
		maps.setFont(mapFont);
		maps.init();

		// Initialise the players list and all its elements
		players.setFont(mapFont);
		players.init();

		// Start the music intro
		getGame().musicHelper.read("HumanMusicIntro").play();

		// Set the music that will be repeated when the intro finishes
		background = getGame().musicHelper.read("HumanMusic");

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	@Override
	public Music getMusic() {
		return background;
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

				maps.add(new Element<Map>(mapDetails[0], new Map(mapDetails[0], width, height)));
			}

		}

	}

	/**
	 * The visual representation of the list of players on screen.
	 */
	private void getPlayers() {
		players.add(new Element<PlayerArray>("2", new PlayerArray(2)));
		players.add(new Element<PlayerArray>("3", new PlayerArray(3)));
		players.add(new Element<PlayerArray>("4", new PlayerArray(4)));
	}

	/**
	 * Loads the {@link MainMenuState#mapFont} {@link VisualList#getSelected()} into
	 * the {@link Game} and re-sizes the window of the {@link Game}.
	 */
	private void loadMap() throws SlickException {

		Element<Map> mapElement = maps.getSelected();
		Element<PlayerArray> playersElement = players.getSelected();

		if (mapElement != null && playersElement != null) {

			Map map = mapElement.get();
			PlayerArray playersArray = playersElement.get();

			// Check width
			if (map.width <= 0) {
				throw new IllegalArgumentException("Width must greater than zero.");
			}

			// Check height
			if (map.height <= 0) {
				throw new IllegalArgumentException("Height must be greater than zero.");
			}

			// Loads the game assets and move into the set up state
			getGame().loadAssets(map.name, map.width, map.height);
			getGame().setPlayers(playersArray.players);
			getGame().enterState(getGame().setup.getID());
		}

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
	 * Holds an array of {@link Player}s used as the payload for the
	 * {@link Element}s in {@link MainMenuState#players}.
	 * 
	 * @author Joshua_Eddy
	 *
	 */
	private class PlayerArray {

		/**
		 * An array of {@link players}.
		 */
		public final Player[] players;

		/**
		 * Constructs a new {@link PlayerArray}.
		 * 
		 * @param numberOfPlayers
		 *            The number of {@link Player}s in the {@link PlayerArray}.
		 */
		public PlayerArray(int numberOfPlayers) {

			switch (numberOfPlayers) {
			case 1:
				players = new Player[] { Player.PLAYERONE };
				break;
			case 2:
				players = new Player[] { Player.PLAYERONE, Player.PLAYERTWO };
				break;
			case 3:
				players = new Player[] { Player.PLAYERONE, Player.PLAYERTWO, Player.PLAYERTHREE };
				break;
			case 4:
				players = new Player[] { Player.PLAYERONE, Player.PLAYERTWO, Player.PLAYERTHREE, Player.PLAYERFOUR };
				break;
			default:
				throw new IllegalArgumentException(
						numberOfPlayers + " is not a valid number of players, it should be 1 to 4. ");
			}

		}
	}
}
