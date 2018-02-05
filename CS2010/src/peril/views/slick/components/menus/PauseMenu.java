package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.controllers.GameController;
import peril.io.MapWriter;
import peril.io.SaveFile;
import peril.views.slick.Button;
import peril.views.slick.Font;
import peril.views.slick.Point;
import peril.views.slick.Region;
import peril.views.slick.components.lists.VisualList;

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
	public final static String NAME = "Pause Menu";

	/**
	 * {@link VisualList} of buttons to toggle the Music on and off.
	 * 
	 */
	private final VisualList<Toggle> toggleMusic;

	/**
	 * {@link VisualList} of buttons to toggle the Music on and off.
	 * 
	 */
	private final VisualList<Toggle> toggleAllLinks;

	/**
	 * Holds a list of all the save files that use can save the game into.
	 */
	private final VisualList<SaveFile> saveFiles;

	/**
	 * Denotes whether or not the save option will be displayed to the user.
	 */
	private boolean showSaveOption;

	/**
	 * Holds the id of the save {@link Button}.
	 */
	private final String saveButton;

	/**
	 * The {@link Font} for the text of the text of the {@link PauseMenu}.
	 */
	private final Font textFont;

	/**
	 * Constructs a new {@link PauseMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link PauseMenu}.
	 * @param game
	 *            The {@link Game} the {@link PauseMenu} is associated with.
	 */
	public PauseMenu(Point position, GameController game) {
		super(NAME, game, new Region(300, 300, position));

		this.saveButton = "save";
		this.showSaveOption = false;

		// Construct music toggle
		this.toggleMusic = new VisualList<>(new Point(position.x + (getWidth() / 4), position.y + 70), 30, 15, 2, 5);
		this.toggleMusic.add(Toggle.ON.toString, Toggle.ON);
		this.toggleMusic.add(Toggle.OFF.toString, Toggle.OFF);
		this.toggleMusic.setFont(new Font("Arial", Color.black, 10));

		// Construct all links toggle
		this.toggleAllLinks = new VisualList<>(new Point(position.x + ((getWidth() * 3) / 4), position.y + 70), 30, 15,
				2, 5);
		this.toggleAllLinks.add(Toggle.OFF.toString, Toggle.OFF);
		this.toggleAllLinks.add(Toggle.ON.toString, Toggle.ON);
		this.toggleAllLinks.setFont(new Font("Arial", Color.black, 10));

		this.textFont = new Font("Arial", Color.black, 10);

		// Construct save file list
		this.saveFiles = new VisualList<>(new Point(position.x + (getWidth() / 2), position.y + 120), 90, 15, 3, 5);
		this.saveFiles.setFont(new Font("Arial", Color.black, 10));
	}

	/**
	 * Sets this {@link PauseMenu} a visible.
	 */
	@Override
	public void show() {
		super.show();
		toggleMusic.setSelected(slick.isMusicOn() ? Toggle.ON : Toggle.OFF);
	}

	/**
	 * Draws the {@link PauseMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void draw(Graphics g) {

		super.draw(g);

		if (isVisible()) {

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

		textFont.init();
		toggleAllLinks.init();
		toggleMusic.init();
		saveFiles.init();

	}

	/**
	 * Processed the click on the toggle music buttons.
	 */
	public void parseClick(Point click) {

		if (!toggleMusic.click(click)) {
			if (!toggleAllLinks.click(click)) {
				if (!saveFiles.click(click)) {
					clickedButton(click);
				}
			}
		} else {
			slick.toggleMusic(toggleMusic.getSelected().toggle);
		}
	}

	/**
	 * Moves all the components in this {@link PauseMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

		toggleMusic
				.setPosition(new Point(toggleMusic.getPosition().x + vector.x, toggleMusic.getPosition().y + vector.y));
		toggleAllLinks.setPosition(
				new Point(toggleAllLinks.getPosition().x + vector.x, toggleAllLinks.getPosition().y + vector.y));
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

	}

	/**
	 * Saves the current state of the {@link Game}.
	 */
	public void save() {

		// Save the current state of the game
		new MapWriter(game, saveFiles.getSelected()).write();

		slick.showToolTip("Game Saved [" + saveFiles.getSelected().name + "]");

		refreshSaveFiles();

	}

	/**
	 * Shows the save option on the {@link PauseMenu}.
	 */
	public void showSaveOption() {
		showSaveOption = true;
		getButton(saveButton).show();
	}

	/**
	 * Hides the save option on the {@link PauseMenu}.
	 */
	public void hideSaveOption() {
		showSaveOption = false;
		getButton(saveButton).hide();
	}

	/**
	 * Retrieves whether the the user has toggled show all links on.
	 * 
	 * @return <code>boolean</code>
	 */
	public boolean showAllLinks() {
		return toggleAllLinks.getSelected() == Toggle.ON;
	}

	/**
	 * Draws the music toggle on the {@link PauseMenu}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawMusicToggle(Graphics g) {

		String music = "Music:";

		String links = "Links:";

		textFont.draw(g, music, toggleMusic.getPosition().x - textFont.getWidth(music) - 5,
				toggleMusic.getPosition().y);
		textFont.draw(g, links, toggleAllLinks.getPosition().x - textFont.getWidth(links) - 5,
				toggleAllLinks.getPosition().y);

		toggleMusic.draw(g);
		toggleAllLinks.draw(g);
	}

	/**
	 * Draws the save option menu on the {@link PauseMenu}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawSaveOption(Graphics g) {

		String save = "Save Game:";

		textFont.draw(g, save, getPosition().x - textFont.getWidth(save) + (getWidth() / 2) - 5, getPosition().y + 120);

		saveFiles.draw(g);
	}

	/**
	 * Adds a save file to {@link PauseMenu#saveFiles}.
	 * 
	 * @param file
	 *            {@link SaveFile}
	 */
	private void addSaveFile(SaveFile file) {

		final String mapPath = game.getDirectory().getMapsPath() + game.getModelBoard().getName();
		
		// If the save file does not currently exist display to the user that it is
		// empty
		final String text = file.name + (file.existsIn(mapPath) ? "" : " - Empty");

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
