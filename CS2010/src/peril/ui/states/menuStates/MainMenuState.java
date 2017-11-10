package peril.ui.states.menuStates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.MusicListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
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
public class MainMenuState extends InteractiveState implements MusicListener{

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

	/**
	 * The {@link Font} used for displaying {@link MainMenuState#maps}.
	 */
	private Font mapFont;

	/**
	 * The {@link Font} for displaying the name of the game.
	 */
	private Font headingFont;
	
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
		maps = new VisualList<>(15, 90, 90, 22, 2);
		getMaps();

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		headingFont.draw(g, "Peril", 10, 5);
		maps.draw(g);
	}

	@Override
	public void parseClick(int button, Point click) {
		if (!super.clickButton(click)) {
			maps.click(click);
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
		// Move the highlight to the next map down
		else if (key == Input.KEY_DOWN) {
			maps.down();
		}
		// Move the highlight to the next map up
		else if (key == Input.KEY_UP) {
			maps.up();
		}
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		// Initialise the fonts;
		mapFont = new Font("Arial", Color.green, 20);
		headingFont = new Font("Arial", Color.red, 56);

		// Initialise the map and all its elements
		maps.setFont(mapFont);
		maps.init();
		
		background = new Music("C:\\Users\\Joshua\\Documents\\GitHub\\CS2010\\CS2010\\game_assets\\HumanMusic.ogg");
		
		Music intro = new Music("C:\\Users\\Joshua\\Documents\\GitHub\\CS2010\\CS2010\\game_assets\\HumanMusicIntro.ogg");
		intro.addListener(this);
		intro.play();

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

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
	 * Loads the {@link MainMenuState#mapFont} {@link VisualList#getSelected()} into
	 * the {@link Game} and re-sizes the window of the {@link Game}.
	 */
	private void loadMap() throws SlickException {

		Element<Map> element = maps.getSelected();

		if (element != null) {

			Map map = element.get();

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

	@Override
	public void musicEnded(Music music) {
		background.loop();
	}

	@Override
	public void musicSwapped(Music arg0, Music arg1) {
		// TODO Auto-generated method stub
		
	}

}
