package peril.ui.components.menus;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.io.MapWriter;
import peril.io.SaveFile;
import peril.ui.Button;
import peril.ui.components.Font;
import peril.ui.components.Region;
import peril.ui.components.VisualList;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of a Pause Menu
 * 
 * @author Ezekiel_Trinidad
 *
 */
public class PauseMenu extends Menu {

	/**
	 * Holds the name of this {@link PauseMenu}.
	 */
	private final static String NAME = "Pause Menu";

	/**
	 * {@link VisualList} of buttons to toggle the Music on and off.
	 * 
	 */
	private VisualList<Toggle> toggleMusic;

	/**
	 * Holds a list of all the save files that use can save the game into.
	 */
	private VisualList<SaveFile> saveFiles;

	/**
	 * Denotes whether or not the save option will be displayed to the user.
	 */
	private boolean showSaveOption;

	/**
	 * Holds the instance of the save {@link Button}.
	 */
	private Button saveButton;

	/**
	 * The {@link Font} for the text of the text of the {@link PauseMenu}.
	 */
	private Font textFont;

	/**
	 * The {@link Font} for the heading of the {@link PauseMenu}.
	 */
	private Font headingFont;

	/**
	 * Constructs a new {@link PauseMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link PauseMenu}.
	 * @param game
	 *            The {@link Game} the {@link PauseMenu} is associated with.
	 */
	public PauseMenu(Point position, Game game) {
		super(NAME, game, new Region(300, 300, position));

		showSaveOption = false;

		// Construct music toggle
		this.toggleMusic = new VisualList<>(position.x + (getWidth() / 2), position.y + 50, 30, 15, 2, 5);
		this.toggleMusic.add(Toggle.ON.toString, Toggle.ON);
		this.toggleMusic.add(Toggle.OFF.toString, Toggle.OFF);

		// Construct save file list
		this.saveFiles = new VisualList<>(position.x + (getWidth() / 2), position.y + 100, 90, 15, 3, 5);
	}

	/**
	 * Adds a button to this {@link PauseMenu}.
	 */
	@Override
	public void addButton(Button button) {
		super.addButton(button);

		if (button.getId().equals("save")) {
			saveButton = button;
			saveButton.hide();
		}
	}

	/**
	 * Draws the {@link PauseMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		if (isVisible()) {

			drawMenu(g);

			g.setColor(Color.white);

			if (showSaveOption) {
				drawSaveOption(g);
			}

			drawMusicToggle(g);

		}

	}

	/**
	 * Initialises all the visual elements off {@link PauseMenu}.
	 */
	public void init() {
		headingFont = new Font("Calibri", Color.cyan, 20);
		textFont = new Font("Arial", Color.pink, 10);

		toggleMusic.init();
		toggleMusic.setFont(new Font("Arial", Color.green, 10));

		saveFiles.init();
		saveFiles.setFont(new Font("Arial", Color.black, 10));

	}

	/**
	 * Processed the click on the toggle music buttons.
	 */
	public void parseClick(Point click) {

		if (!toggleMusic.click(click)) {
			if (!saveFiles.click(click)) {
				clickedButton(click);
			}
		} else {
			getGame().toggleMusic(toggleMusic.getSelected().toggle);
		}
	}

	/**
	 * Moves all the components in this {@link PauseMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

		toggleMusic
				.setPosition(new Point(toggleMusic.getPosition().x + vector.x, toggleMusic.getPosition().y + vector.y));
		saveFiles.setPosition(new Point(saveFiles.getPosition().x + vector.x, saveFiles.getPosition().y + vector.y));

	}

	/**
	 * Checks to see if all the save file in this {@link PauseMenu} are correct for
	 * the current map.
	 */
	public void refreshSaveFiles() {

		saveFiles.clear();

		addSaveFile(SaveFile.ONE);
		addSaveFile(SaveFile.TWO);
		addSaveFile(SaveFile.THREE);

		saveFiles.init();
		saveFiles.setFont(new Font("Arial", Color.black, 10));
	}

	/**
	 * Saves the current state of the {@link Game}.
	 */
	public void save() {

		// Holds the path of the current map
		String mapFolderPath = getGame().mapsDirectory + File.separatorChar + getGame().board.getName();

		// Save the current state of the game
		new MapWriter(getGame(), mapFolderPath, saveFiles.getSelected()).write();

		// Display to the user that the game was saved.
		((CoreGameState) getGame().getCurrentState()).show("Game Saved [" + saveFiles.getSelected().name + "]");

		refreshSaveFiles();

	}

	/**
	 * Shows the save option on the {@link PauseMenu}.
	 */
	public void showSaveOption() {
		showSaveOption = true;
		saveButton.show();
	}

	/**
	 * Hides the save option on the {@link PauseMenu}.
	 */
	public void hideSaveOption() {
		showSaveOption = false;
		saveButton.hide();
	}

	/**
	 * Draws the background and title of the {@link PauseMenu}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawMenu(Graphics g) {

		g.setColor(Color.black);

		super.draw(g);

		String pause = "PAUSE";

		headingFont.draw(g, pause, getPosition().x - (headingFont.getWidth(pause) / 2) + (getWidth() / 2),
				getPosition().y + 10);
	}

	/**
	 * Draws the music toggle on the {@link PauseMenu}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawMusicToggle(Graphics g) {

		String music = "Toggle Music:";

		textFont.draw(g, music, getPosition().x + (getWidth() / 2) - (headingFont.getWidth(music) / 2) - 15,
				getPosition().y + 50);

		toggleMusic.draw(g);
	}

	/**
	 * Draws the save option menu on the {@link PauseMenu}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawSaveOption(Graphics g) {

		String save = "Save Game:";

		textFont.draw(g, save, getPosition().x - (headingFont.getWidth(save) / 2) + (getWidth() / 2) - 15,
				getPosition().y + 100);

		saveFiles.draw(g);
	}

	/**
	 * Adds a save file to {@link PauseMenu#saveFiles}.
	 * 
	 * @param file
	 *            {@link SaveFile}
	 */
	private void addSaveFile(SaveFile file) {

		// If the save file does not currently exist display to the user that it is
		// empty
		boolean exists = file.existsIn(getGame().mapsDirectory + File.separatorChar + getGame().board.getName());
		String text = file.name + (exists ? "" : " - Empty");

		saveFiles.add(text, file);
	}

	/**
	 * Holds "ON" and "OFF" which has boolean value assigned to them.
	 * 
	 * @author Ezekiel_Trinidad, Joshua_Eddy
	 *
	 */
	private enum Toggle {

		ON("On", true), OFF("Off", false);

		/**
		 * <code>boolean</code> state of the {@link Toggle}.
		 */
		public final boolean toggle;

		/**
		 * The string representation of the {@link Toggle}.
		 */
		public final String toString;

		/**
		 * Constructs a new {@link Toggle}.
		 * 
		 * @param toString
		 *            The string representation of the {@link Toggle}.
		 * @param toggle
		 *            <code>boolean</code> state of the {@link Toggle}.
		 */
		private Toggle(String toString, boolean toggle) {
			this.toggle = toggle;
			this.toString = toString;
		}
	}
}
