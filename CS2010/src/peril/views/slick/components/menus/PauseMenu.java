package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Game;
import peril.controllers.GameController;
import peril.io.MapWriter;
import peril.io.SaveFile;
import peril.views.slick.Button;
import peril.views.slick.Font;
import peril.views.slick.Frame;
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
		super(NAME, game, new Region(600, 600, position));

		this.saveButton = "save";
		this.showSaveOption = false;
		
		final Font toggleFont = new Font("Arial", Color.black, 20);
		
		// Construct music toggle
		this.toggleMusic = new VisualList<>(new Point(position.x + (getWidth() / 4), position.y + (getHeight() / 4)),
				60, 30, 2, 10);
		this.toggleMusic.add(Toggle.ON.toString, Toggle.ON);
		this.toggleMusic.add(Toggle.OFF.toString, Toggle.OFF);
		this.toggleMusic.setFont(toggleFont);

		// Construct all links toggle
		this.toggleAllLinks = new VisualList<>(
				new Point(position.x + ((getWidth() * 3) / 4), position.y + (getHeight() / 4)), 60, 30, 2, 10);
		this.toggleAllLinks.add(Toggle.OFF.toString, Toggle.OFF);
		this.toggleAllLinks.add(Toggle.ON.toString, Toggle.ON);
		this.toggleAllLinks.setFont(toggleFont);

		this.textFont = new Font("Arial", Color.black, 20);

		// Construct save file list
		this.saveFiles = new VisualList<>(new Point(position.x + (getWidth() / 2), position.y + 250), 180, 30, 3, 10);
		this.saveFiles.setFont(toggleFont);
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
	public void draw(Frame frame) {

		super.draw(frame);

		if (isVisible()) {

			frame.setColor(Color.white);

			if (showSaveOption) {
				drawSaveOption(frame);
			}

			drawMusicToggle(frame);

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
	 * @param frametextFont
	 *            {@link Graphics}
	 */
	private void drawMusicToggle(Frame frame) {

		String music = "Music:";

		String links = "Links:";

		frame.draw(textFont, music, toggleMusic.getPosition().x - textFont.getWidth(music) - 5,
				toggleMusic.getPosition().y);
		frame.draw(textFont, links, toggleAllLinks.getPosition().x - textFont.getWidth(links) - 5,
				toggleAllLinks.getPosition().y);

		toggleMusic.draw(frame);
		toggleAllLinks.draw(frame);
	}

	/**
	 * Draws the save option menu on the {@link PauseMenu}.
	 * 
	 * @param frame
	 *            {@link Graphics}
	 */
	private void drawSaveOption(Frame frame) {

		String save = "Save Game:";

		frame.draw(textFont, save, getPosition().x - textFont.getWidth(save) + (getWidth() / 2) - 10, getPosition().y + 240);

		saveFiles.draw(frame);
	}

	/**
	 * Adds a save file to {@link PauseMenu#saveFiles}.
	 * 
	 * @param file
	 *            {@link SaveFile}
	 */
	private void addSaveFile(SaveFile file) {

		final String mapPath = game.getDirectory().asMapPath(game.getModelBoard().getName());

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
