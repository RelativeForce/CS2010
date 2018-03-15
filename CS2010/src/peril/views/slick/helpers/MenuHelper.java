package peril.views.slick.helpers;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import peril.controllers.Directory;
import peril.io.TextFileReader;
import peril.model.board.ModelCountry;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.components.menus.*;
import peril.views.slick.util.Point;

/**
 * A helper class that holds all the {@link Menu}s for the {@link SlickGame}.
 * This ensures that only one {@link Menu} is visible at any given time.
 * 
 * @author Joshua_Eddy, Hannah_Miller
 * 
 * @since 2018-03-15
 * @version 1.02.03
 * 
 * @see Menu
 *
 */
public final class MenuHelper {

	/**
	 * The {@link Map} of {@link Menu}'s names to the {@link Menu}s.
	 */
	private final Map<String, Menu> menus;

	/**
	 * The currently visible menu.
	 */
	private volatile Menu visible;

	/**
	 * Constructs a new {@link MenuHelper}.
	 * 
	 * @param menus
	 *            The {@link Menu}s that this {@link MenuHelper} will track.
	 */
	public MenuHelper(Set<Menu> menus) {

		this.menus = new IdentityHashMap<>();

		// Add all the menus to the map.
		menus.forEach(menu -> this.menus.put(menu.getName(), menu));

		this.visible = null;
	}

	/**
	 * Moves all the {@link Menu}s so that they are centred based on the specified
	 * centre x and y.
	 * 
	 * @param centerX
	 *            The centre x of the screen.
	 * @param centerY
	 *            The centre y of the screen.
	 */
	public void center(int centerX, int centerY) {

		// Move all the menus accordingly.
		menus.forEach((name, menu) -> {
			menu.setPosition(new Point(centerX - (menu.getWidth() / 2), centerY - (menu.getHeight() / 2)));
		});

		hideVisible();
	}

	/**
	 * Initialises all the {@link Menu}s in this {@link MenuHelper}.
	 */
	public void initMenus() {
		menus.forEach((name, menu) -> menu.init());
	}

	/**
	 * Sets a {@link Menu} with a specified name as the visible menu.
	 * 
	 * @param menuName
	 *            The {@link Menu} name.
	 */
	public void show(String menuName) {

		final Menu menu = menus.get(menuName);

		// If no menu was found with the specified name.
		if (menu == null) {
			throw new NullPointerException(menuName + " is not a assigned to a menu in the menu helper.");
		}

		showMenu(menu);
	}

	/**
	 * Changes the help page on the {@link HelpMenu}.
	 * 
	 * @param id
	 *            The id of the {@link HelpMenu} page.
	 */
	public void changeHelpPage(int id) {

		final HelpMenu help = (HelpMenu) menus.get(HelpMenu.NAME);

		if (help == null) {
			throw new NullPointerException("There is no help menu in this menu helper.");
		}

		help.changePage(id);
	}

	/**
	 * Hides the currently visible {@link Menu}.
	 */
	public void hideVisible() {
		if (visible != null) {
			visible.hide();
			visible = null;
		}
	}

	/**
	 * Hides the {@link Menu} with the specified name provided that the {@link Menu}
	 * with the specified name is the currently visible {@link Menu}.
	 * 
	 * @param menuName
	 *            The {@link Menu}'s name.
	 */
	public void hide(String menuName) {

		if (visible != null && visible.getName().equals(menuName)) {
			hideVisible();
		}

	}

	/**
	 * Saves the current state of the game.
	 */
	public void save() {

		final PauseMenu pause = (PauseMenu) menus.get(PauseMenu.NAME);

		if (pause == null) {
			throw new NullPointerException("There is no pause menu in this menu helper.");
		}

		pause.save();
	}

	/**
	 * Retrieves whether or not there is a {@link Menu} currently visible.
	 * 
	 * @return Whether or not there is a {@link Menu} currently visible.
	 */
	public boolean menuVisible() {
		return visible != null && visible.isVisible();
	}

	/**
	 * Retrieves whether or not ALL the links are visible on the map.
	 * 
	 * @return Whether or not ALL the links are visible on the map.
	 */
	public boolean linksVisible() {

		final PauseMenu pause = (PauseMenu) menus.get(PauseMenu.NAME);

		if (pause == null) {
			throw new NullPointerException("There is no pause menu in this menu helper.");
		}

		return pause.showAllLinks();
	}

	/**
	 * Displays the save option on the {@link PauseMenu}.
	 */
	public void showSaveOption() {

		final PauseMenu pause = (PauseMenu) menus.get(PauseMenu.NAME);

		if (pause == null) {
			throw new NullPointerException("There is no pause menu in this menu helper.");
		}

		pause.showSaveOption();
	}

	/**
	 * Hides the save option on the {@link PauseMenu}.
	 */
	public void hideSaveOption() {

		final PauseMenu pause = (PauseMenu) menus.get(PauseMenu.NAME);

		if (pause == null) {
			throw new NullPointerException("There is no pause menu in this menu helper.");
		}

		pause.hideSaveOption();
	}

	/**
	 * Moves the {@link HelpMenu} to the next help page.
	 */
	public void nextHelpPage() {

		final HelpMenu help = (HelpMenu) menus.get(HelpMenu.NAME);

		if (help == null) {
			throw new NullPointerException("There is no help menu in this menu helper.");
		}

		help.nextPage();
	}

	/**
	 * Moves the {@link HelpMenu} to the previous help page.
	 */
	public void previousHelpPage() {

		final HelpMenu help = (HelpMenu) menus.get(HelpMenu.NAME);

		if (help == null) {
			throw new NullPointerException("There is no help menu in this menu helper.");
		}

		help.previousPage();
	}

	/**
	 * Clears all the information stored on the {@link Menu}s.
	 */
	public void clearMenus() {

		final WarMenu war = (WarMenu) menus.get(WarMenu.NAME);

		if (war == null) {
			throw new NullPointerException("There is no war menu in this menu helper.");
		}

		war.clear();
	}

	/**
	 * Refreshes the challenges displayed on the {@link ChallengeMenu}.
	 */
	public void refreshChallenges() {

		final ChallengeMenu challenge = (ChallengeMenu) menus.get(ChallengeMenu.NAME);

		if (challenge == null) {
			throw new NullPointerException("There is no challenge menu in this menu helper.");
		}

		challenge.refreshChallenges();
	}

	/**
	 * Causes the {@link WarMenu} to display a round of combat to the user.
	 */
	public void attack() {

		final WarMenu war = (WarMenu) menus.get(WarMenu.NAME);

		if (war == null) {
			throw new NullPointerException("There is no war menu in this menu helper.");
		}

		war.attack();
	}

	/**
	 * Causes the {@link WarMenu} to display a round of AI controlled combat to the
	 * user.
	 */
	public void autoAttack() {

		final WarMenu war = (WarMenu) menus.get(WarMenu.NAME);

		if (war == null) {
			throw new NullPointerException("There is no war menu in this menu helper.");
		}

		show(WarMenu.NAME);
		war.selectMaxUnits();
		war.attack();

	}

	/**
	 * Retrieves whether or not the {@link PauseMenu} is on screen and therefore the
	 * game is paused.
	 * 
	 * @return Whether or not the game is paused.
	 */
	public boolean isPaused() {

		if (visible == null) {
			return false;
		}

		final PauseMenu pause = (PauseMenu) menus.get(PauseMenu.NAME);

		if (pause == null) {
			throw new NullPointerException("There is no pause menu in this menu helper.");
		}

		return visible == pause;
	}

	/**
	 * Creates the {@link HelpMenu}'s pages.
	 * 
	 * @param states
	 *            The {@link StateHelper} that contains all the game states.
	 */
	public void createHelpPages(StateHelper states, Directory directory) {

		final int setupPage = states.setup.getID();
		final int reinforcePage = states.reinforcement.getID();
		final int attackPage = states.combat.getID();
		final int fortifyPage1 = states.movement.getID();

		final int hazardPage1 = 30;
		final int hazardPage2 = 31;
		final int upgradePage1 = 32;
		final int upgradePage2 = 33;

		final int warPage1 = 40;
		final int warPage2 = 41;
		final int warPage3 = 42;
		
		final int fortifyPage2 = 50;

		// Set up pages
		addPage(setupPage, HelpMenu.NULL_PAGE, HelpMenu.NULL_PAGE, directory, "setup1.txt");

		// Reinforce pages
		addPage(reinforcePage, hazardPage1, HelpMenu.NULL_PAGE, directory, "reinforce1.txt");
		addPage(hazardPage1, hazardPage2, reinforcePage, directory, "reinforce2.txt");
		addPage(hazardPage2, upgradePage1, hazardPage1, directory, "reinforce3.txt");
		addPage(upgradePage1, upgradePage2, hazardPage2, directory, "reinforce4.txt");
		addPage(upgradePage2, HelpMenu.NULL_PAGE, upgradePage1, directory, "reinforce5.txt");

		// Attack pages
		addPage(attackPage, warPage1, HelpMenu.NULL_PAGE, directory, "attack1.txt");
		addPage(warPage1, warPage2, attackPage, directory, "attack2.txt");
		addPage(warPage2, warPage3, warPage1, directory, "attack3.txt");
		addPage(warPage3, HelpMenu.NULL_PAGE, warPage2, directory, "attack4.txt");

		// Fortify pages
		addPage(fortifyPage1, fortifyPage2, HelpMenu.NULL_PAGE, directory, "fortify1.txt");
		addPage(fortifyPage2, HelpMenu.NULL_PAGE, fortifyPage1, directory, "fortify2.txt");

	}

	/**
	 * Refreshes the save files displayed on the {@link PauseMenu}.
	 */
	public void refreshSaveFiles() {

		final PauseMenu pause = (PauseMenu) menus.get(PauseMenu.NAME);

		if (pause == null) {
			throw new NullPointerException("There is no pause menu in this menu helper.");
		}

		pause.refreshSaveFiles();
	}

	/**
	 * Causes the {@link UpgradeMenu} to block the link from one
	 * {@link ModelCountry} to another.
	 */
	public void blockLink() {

		final UpgradeMenu upgrade = (UpgradeMenu) menus.get(UpgradeMenu.NAME);

		if (upgrade == null) {
			throw new NullPointerException("There is no upgrade menu in this menu helper.");
		}

		upgrade.blockLink();

	}

	/**
	 * Draws the currently visible {@link Menu}.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display the {@link Menu} to the user.
	 */
	public void draw(Frame frame) {
		if (visible != null) {
			frame.draw(visible, new EventListener() {

				@Override
				public void mouseHover(Point mouse, int delta) {
					// Do nothing
				}

				@Override
				public void mouseClick(Point mouse, int button) {
					// Do nothing
				}

				@Override
				public void buttonPress(int key, Point mouse) {
					// Do nothing
				}

				@Override
				public void draw(Frame frame) {
					visible.draw(frame);
				}
			});
		}
	}

	/**
	 * Sets a {@link Menu} as the visible {@link Menu} an hides the currently
	 * visible {@link Menu}.
	 * 
	 * @param menu
	 *            The {@link Menu} to set visible.
	 */
	private void showMenu(Menu menu) {
		hideVisible();

		if (menu != null) {
			visible = menu;
			visible.show();
		}

	}

	/**
	 * Adds a page to the {@link HelpMenu} with the specified ID. The contents of
	 * the page will be retrieved from the text file specified.
	 * 
	 * @param id
	 *            The id of the help page.
	 * @param nextPage
	 *            The id of the next help page in the chain.
	 * @param previousPage
	 *            The id of the previous help page in the chain.
	 * @param directory
	 *            The directory that contains all the games assets.
	 * @param filename
	 *            The name of the file containing all the help pages content.
	 */
	private void addPage(int id, int nextPage, int previousPage, Directory directory, String filename) {

		final HelpMenu helpMenu = (HelpMenu) menus.get(HelpMenu.NAME);

		final String[] lines = TextFileReader.scanFile(directory.getTextPath(), filename);

		boolean firstLine = true;

		// iterate over all the lines in the text file.
		for (String line : lines) {

			// The first line is the title.
			if (firstLine) {
				helpMenu.addPage(id, nextPage, previousPage, line);

				firstLine = false;
			} else {
				helpMenu.addText(id, line);
			}

		}

	}

}
