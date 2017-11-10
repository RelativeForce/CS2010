package peril.ui.states.menuStates;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.io.TextFileReader;
import peril.ui.states.Font;
import peril.ui.states.InteractiveState;
import peril.ui.visual.Clickable;
import peril.ui.visual.Region;

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
	 * Holds a {@link List} of the maps read from the map.txt file.
	 */
	private final List<Map> maps;

	/**
	 * The {@link Font} used for displaying {@link MainMenuState#maps}.
	 */
	private Font mapFont;

	/**
	 * The {@link Font} for displaying the name of the game.
	 */
	private Font headingFont;

	/**
	 * The {@link Map} from the {@link List} of {@link Map}s that is currently
	 * highlight by the user.
	 */
	private Map selectedMap;

	/**
	 * Width of the {@link Map} {@link List} on screen.
	 */
	private int width;

	/**
	 * Height of the {@link Map} {@link List} on screen.
	 */
	private int height;

	/**
	 * The x coordinate of the {@link Map} {@link List} on screen.
	 */
	private int mapListX;

	/**
	 * The y coordinate of the {@link Map} {@link List} on screen.
	 */
	private int mapListY;

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
		maps = new LinkedList<>();
		width = 90;
		height = 25;
		mapListX = 15;
		mapListY = 90;
		getMaps();
		selectedMap = maps.get(0);

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		headingFont.draw(g, "Peril", 10, 5);
		drawMapList(g);
	}

	@Override
	public void parseClick(int button, Point click) {
		if (!super.clickButton(click)) {
			maps.forEach(currentMap -> {

				if (currentMap.isClicked(click)) {
					selectElement(currentMap);
				}

			});
		}
	}

	@Override
	public void parseButton(int key, char c) {
		if (key == Input.KEY_ENTER) {

			try {
				loadMap(selectedMap);
				getGame().enterState(getGame().setup.getID());
			} catch (SlickException e) {

			}
		}
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		// Initialise the fonts;
		mapFont = new Font("Arial", Color.green, 20);
		headingFont = new Font("Arial", Color.red, 56);

		// Set the coordinates of the map list.
		int y = mapListY;
		int x = mapListX;

		// Iterate through all the maps names and list them the spacing distance apart.
		for (Map map : maps) {
			map.init(x, y, width, height);
			y += height;
		}

	}

	/**
	 * Sets a specified {@link Map} as the new {@link MainMenuState#selectedMap}.
	 * 
	 * @param map
	 *            {@link Map}
	 */
	public void selectElement(Map map) {
		selectedMap = map;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
	}

	/**
	 * Draws {@link MainMenuState#maps} on the screen in
	 * {@link MainMenuState#mapFont}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param x
	 *            <code>int</code> x coordinate of the map list.
	 * @param y
	 *            <code>int</code> y coordinate of the map list.
	 */
	private void drawMapList(Graphics g) {

		g.fillRect(mapListX, mapListY, width, (maps.size() * height));
		g.drawImage(selectedMap.getImage(), selectedMap.getPosition().x, selectedMap.getPosition().y);
		maps.forEach(map -> map.draw(g));
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
				maps.add(new Map(new String[] { mapDetails[0], mapDetails[1], mapDetails[2] }));
			}

		}

	}

	private void loadMap(Map map) throws SlickException {

		for (String line : mapsFile) {

			String[] mapDetails = line.split(",");

			if (mapDetails[0].equals(map.element[0])) {

				int width;
				try {
					width = Integer.parseInt(mapDetails[1]);
				} catch (Exception e) {
					throw new IllegalArgumentException("Width must be an integer");
				}

				int height;
				try {
					height = Integer.parseInt(mapDetails[2]);
				} catch (Exception e) {
					throw new IllegalArgumentException("Height must be an integer");
				}

				getGame().loadAssets(map.getElement()[0], width, height);
			}

		}

	}

	private class Map extends Clickable {

		/**
		 * The string array of the details of the maps
		 */
		private String[] element;

		/**
		 * Constructs a new {@link Map}.
		 * 
		 * @param element
		 */
		public Map(String[] element) {
			super();
			this.element = element;
		}

		/**
		 * Retrieves the {@link Map#element}.
		 * 
		 * @return {@link String}[]
		 */
		public String[] getElement() {
			return element;
		}

		/**
		 * Initialises the {@link Map}.
		 * 
		 * @param x
		 *            <code>int</code> x coordinate of the {@link Map} on screen.
		 * @param y
		 *            <code>int</code> y coordinate of the {@link Map} on screen.
		 * @param width
		 *            <code>int</code> width of the {@link Map} on screen.
		 * @param height
		 *            <code>int</code> height of the {@link Map} on screen.
		 */
		public void init(int x, int y, int width, int height) {
			setRegion(new Region(width, height, new Point(x, y)));
			setImage(getRegion().getPosition(), getRegion().convert(Color.yellow));
		}

		/**
		 * Draws the name of the {@link Map} on screen.
		 * 
		 * @param g
		 */
		public void draw(Graphics g) {
			mapFont.draw(g, element[0], getPosition().x, getPosition().y);
		}
	}

}
