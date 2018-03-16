package peril.views.slick.components.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;

import peril.GameController;
import peril.ai.AI;
import peril.io.MapWriter;
import peril.io.SaveFile;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.components.Component;
import peril.views.slick.components.VisualList;
import peril.views.slick.states.PlayerSelection;
import peril.views.slick.util.Button;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Encapsulates the behaviour of a Pause Menu.
 * 
 * @author Ezekiel_Trinidad, Joshua_Eddy
 * 
 * @since 2018-03-15
 * @version 1.01.05
 * 
 * @see Menu
 *
 */
public final class PauseMenu extends Menu {

	/**
	 * Holds the name of this {@link PauseMenu}.
	 */
	public static final String NAME = "Pause Menu";

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
	 * The {@link VisualList} containing the different {@link AI} speeds available
	 * for the user to select.
	 */
	private final VisualList<Integer> aiSpeeds;

	/**
	 * Holds the id of the save {@link Button}.
	 */
	private final String saveButton;

	/**
	 * The {@link Font} for the text of the text of the {@link PauseMenu}.
	 */
	private final Font textFont;

	/**
	 * The {@link EventListener} that handles the events that occur to the
	 * {@link #toggleMusic} list.
	 */
	private final EventListener musicToggleListener;

	/**
	 * The {@link EventListener} that handles the events that occur to the
	 * {@link #toggleAllLinks} list.
	 */
	private final EventListener linksToggleListener;

	/**
	 * The {@link EventListener} that handles the events that occur to the
	 * {@link #saveFiles} list.
	 */
	private final EventListener savesListener;

	/**
	 * The {@link EventListener} that handles the events that occur to the
	 * {@link #aiSpeeds} list.
	 */
	private final EventListener aiSpeedsListener;

	/**
	 * Denotes whether or not the save option will be displayed to the user.
	 */
	private boolean showSaveOption;

	/**
	 * Constructs a new {@link PauseMenu}.
	 * 
	 * @param position
	 *            {@link Point} position of the {@link PauseMenu}.
	 * @param game
	 *            The {@link GameController} the {@link PauseMenu} is associated
	 *            with.
	 */
	public PauseMenu(Point position, GameController game) {
		super(NAME, game, new Region(600, 600, position));

		this.saveButton = "save";
		this.showSaveOption = false;

		final Font toggleFont = new Font("Arial", Color.black, 20);

		// Construct music toggle
		this.toggleMusic = new VisualList<>(new Point(position.x + (getWidth() / 4), position.y + (getHeight() / 4)),
				60, 30, 2, 10);
		this.toggleMusic.add(Toggle.ON.name, Toggle.ON);
		this.toggleMusic.add(Toggle.OFF.name, Toggle.OFF);
		this.toggleMusic.setFont(toggleFont);

		// Construct all links toggle
		this.toggleAllLinks = new VisualList<>(new Point(position.x + 350, position.y + (getHeight() / 4)), 60, 30, 2,
				10);
		this.toggleAllLinks.add(Toggle.OFF.name, Toggle.OFF);
		this.toggleAllLinks.add(Toggle.ON.name, Toggle.ON);
		this.toggleAllLinks.setFont(toggleFont);

		this.textFont = new Font("Arial", Color.black, 20);

		// Construct save file list
		this.saveFiles = new VisualList<>(new Point(position.x + 350, position.y + 250), 180, 30, 3, 10);
		this.saveFiles.setFont(toggleFont);

		// Define the AI speeds list
		this.aiSpeeds = new VisualList<>(new Point(position.x + 150, position.y + 250), 50, 24, 4, 5);
		this.aiSpeeds.setFont(toggleFont);
		populateAISpeeds();

		this.linksToggleListener = new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				toggleAllLinks.click(mouse);
			}

			@Override
			public void draw(Frame frame) {
				toggleAllLinks.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				if (toggleAllLinks.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						toggleAllLinks.up();
					} else if (key == Input.KEY_DOWN) {
						toggleAllLinks.down();
					}
				}

			}
		};

		this.musicToggleListener = new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				toggleMusic.click(mouse);
				checkMusicState();
			}

			@Override
			public void draw(Frame frame) {
				toggleMusic.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				if (toggleMusic.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						toggleMusic.up();
						checkMusicState();
					} else if (key == Input.KEY_DOWN) {
						toggleMusic.down();
						checkMusicState();
					}

				}

			}

			private void checkMusicState() {
				game.getView().toggleMusic(toggleMusic.getSelected().toggle);
			}
		};

		this.savesListener = new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				saveFiles.click(mouse);
			}

			@Override
			public void draw(Frame frame) {
				saveFiles.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				if (saveFiles.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						saveFiles.up();
					} else if (key == Input.KEY_DOWN) {
						saveFiles.down();
					}
				}

			}
		};

		this.aiSpeedsListener = new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {
				aiSpeeds.click(mouse);

				// If the user has selected the max AI speed warn them.
				if (aiSpeeds.getSelected() == AI.MAX_SPEED) {
					showAISpeedWarning();
				}

				changeAISpeed();
			}

			@Override
			public void draw(Frame frame) {
				aiSpeeds.draw(frame);
			}

			@Override
			public void buttonPress(int key, Point mouse) {

				if (aiSpeeds.isClicked(mouse)) {

					if (key == Input.KEY_UP) {
						aiSpeeds.up();

						// If the user has selected the max AI speed warn them.
						if (aiSpeeds.getSelected() == AI.MAX_SPEED) {
							showAISpeedWarning();
						}

						changeAISpeed();
					} else if (key == Input.KEY_DOWN) {
						aiSpeeds.down();

						// If the user has selected the max AI speed warn them.
						if (aiSpeeds.getSelected() == AI.MAX_SPEED) {
							showAISpeedWarning();
						}

						changeAISpeed();
					}
				}
			}

		};

	}

	/**
	 * Sets this {@link PauseMenu} as visible.
	 */
	@Override
	public void show() {
		super.show();
		toggleMusic.setSelected(slick.isMusicOn() ? Toggle.ON : Toggle.OFF);
		getCurrentAISpeed();
	}

	/**
	 * Draws the {@link PauseMenu} on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that draws the {@link PauseMenu} on screen.
	 */
	public void draw(Frame frame) {

		super.draw(frame);

		frame.setColor(Color.white);

		if (showSaveOption) {
			drawSaveOption(frame);
		}

		drawMusicToggle(frame);

		drawLinksToggle(frame);

		drawAISpeeds(frame);

	}

	/**
	 * Initialises all the visual elements off {@link PauseMenu}.
	 */
	public void init() {

		textFont.init();
		toggleAllLinks.init();
		toggleMusic.init();
		saveFiles.init();
		aiSpeeds.init();

	}

	/**
	 * Moves all the components in this {@link PauseMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {
		moveComponent(toggleMusic, vector);
		moveComponent(toggleAllLinks, vector);
		moveComponent(saveFiles, vector);
		moveComponent(aiSpeeds, vector);
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
	 * Saves the current state of the game.
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
	 * @return Whether the user has selected to show all the links between
	 *         countries.
	 */
	public boolean showAllLinks() {
		return toggleAllLinks.getSelected() == Toggle.ON;
	}

	/**
	 * Retrieves the current speed that the AIs are set to.
	 */
	private void getCurrentAISpeed() {

		final int currentSpeed = game.getAIs().iterator().next().getSpeed();

		aiSpeeds.setSelected((Integer) currentSpeed);

	}

	/**
	 * Populates the {@link PauseMenu#aiSpeeds} with all the {@link AI} speeds that
	 * is available for the user to select.
	 */
	private void populateAISpeeds() {

		final int multipler = 4;

		for (int index = 0; index < 4; index++) {

			final int speed = (int) (AI.MAX_SPEED * Math.pow(2, index));
			final String text = Integer.toString(multipler - index) + "x";

			aiSpeeds.add(text, speed);

		}

	}

	/**
	 * Moves a {@link Component} along a specified point vector.
	 * 
	 * @param component
	 *            The {@link Component} to be moved.
	 * @param vector
	 *            The {@link Point} vector that the {@link Component} will be moves
	 *            along.
	 */
	private void moveComponent(Component component, Point vector) {
		final Point current = component.getPosition();
		component.setPosition(new Point(current.x + vector.x, current.y + vector.y));
	}

	/**
	 * Changes the speed of all the {@link AI}s in the game to the speed selected in
	 * {@link PlayerSelection#aiSpeeds}.
	 */
	private void changeAISpeed() {

		final int newSpeed = aiSpeeds.getSelected();
		game.getAIs().forEach(ai -> ai.setSpeed(newSpeed));

	}

	/**
	 * Displays a tool tip that warns the user that the faster the {@link AI} are
	 * the worse the game performance will be.
	 */
	private void showAISpeedWarning() {

		// Display warning
		final String message = "Increasing AI speed, reduces game perfromance.";
		final int x = aiSpeeds.getPosition().x + aiSpeeds.getWidth();
		final int y = aiSpeeds.getPosition().y + (aiSpeeds.getHeight() / 2);

		slick.showToolTip(message, new Point(x, y));

	}

	/**
	 * Draws the AI speeds list on the {@link PauseMenu}.
	 * 
	 * @param frame
	 *            The {@link Frame} that draws the {@link PauseMenu} on screen.
	 */
	private void drawAISpeeds(Frame frame) {

		final String text = "AI speed:";

		frame.draw(textFont, text, aiSpeeds.getPosition().x, aiSpeeds.getPosition().y - textFont.getHeight(text) - 5);

		frame.draw(aiSpeeds, aiSpeedsListener);

	}

	/**
	 * Draws the links toggle on the {@link PauseMenu}.
	 * 
	 * @param frame
	 *            The {@link Frame} that draws the {@link PauseMenu} on screen.
	 */
	private void drawLinksToggle(Frame frame) {

		String links = "Links:";

		frame.draw(textFont, links, toggleAllLinks.getPosition().x,
				toggleAllLinks.getPosition().y - textFont.getHeight(links) - 5);

		// Draw the links toggle.
		frame.draw(toggleAllLinks, linksToggleListener);

	}

	/**
	 * Draws the music toggle on the {@link PauseMenu}.
	 * 
	 * @param frame
	 *            The {@link Frame} that draws the {@link PauseMenu} on screen.
	 */
	private void drawMusicToggle(Frame frame) {

		final String music = "Music:";
		final int y = toggleMusic.getPosition().y - textFont.getHeight(music) - 5;
		final int x = toggleMusic.getPosition().x;

		frame.draw(textFont, music, x, y);

		// Draw the music toggle.
		frame.draw(toggleMusic, musicToggleListener);
	}

	/**
	 * Draws the save option menu on the {@link PauseMenu}.
	 * 
	 * @param frame
	 *            The {@link Frame} that draws the {@link PauseMenu} on screen.
	 */
	private void drawSaveOption(Frame frame) {

		String save = "Save Game:";

		frame.draw(textFont, save, saveFiles.getPosition().x, saveFiles.getPosition().y - textFont.getHeight(save) - 5);

		frame.draw(saveFiles, savesListener);
	}

	/**
	 * Adds a save file to {@link PauseMenu#saveFiles}.
	 * 
	 * @param file
	 *            The {@link SaveFile}
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
	 * @since 2018-02-25
	 * @version 1.01.01
	 *
	 */
	private enum Toggle {

		ON("On", true), OFF("Off", false);

		/**
		 * The state of the {@link Toggle}.
		 */
		public final boolean toggle;

		/**
		 * The string representation of the {@link Toggle}.
		 */
		public final String name;

		/**
		 * Constructs a new {@link Toggle}.
		 * 
		 * @param name
		 *            The string representation of the {@link Toggle}.
		 * @param toggle
		 *            <code>boolean</code> state of the {@link Toggle}.
		 */
		private Toggle(String name, boolean toggle) {
			this.toggle = toggle;
			this.name = name;
		}
	}
}
