package peril.views.slick.helpers;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import peril.model.board.ModelHazard;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.components.menus.*;
import peril.views.slick.util.Point;

/**
 * A helper class that holds all the {@link Menu}s for the {@link SlickGame}.
 * This ensures that only one {@link Menu} is visible at any given time.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
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
	private Menu visible;

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
		return visible != null;
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

		final PauseMenu pause = (PauseMenu) menus.get(PauseMenu.NAME);

		if (pause == null) {
			throw new NullPointerException("There is no pause menu in this menu helper.");
		}

		return pause.isVisible();
	}

	/**
	 * Creates the {@link HelpMenu}'s pages.
	 * 
	 * @param states
	 *            The {@link StateHelper} that contains all the game states.
	 */
	public void createHelpPages(StateHelper states) {

		int setupPage = states.setup.getID();
		int reinforcementPage = states.reinforcement.getID();
		int combatPage = states.combat.getID();
		int movementPage = states.movement.getID();

		// EnvironmentalHazards Page id
		int hazardPage = 30;

		// War menu page ids
		int warPage1 = 31;
		int warPage2 = 32;

		HelpMenu helpMenu = (HelpMenu) menus.get(HelpMenu.NAME);

		helpMenu.addPage(movementPage, HelpMenu.NULL_PAGE, HelpMenu.NULL_PAGE, "Help: Movement");

		// Setup state
		helpMenu.addPage(setupPage, HelpMenu.NULL_PAGE, HelpMenu.NULL_PAGE, "Help: Setup");

		// Reinforcement state pages
		helpMenu.addPage(reinforcementPage, hazardPage, HelpMenu.NULL_PAGE, "Help: Reinforce");
		helpMenu.addPage(hazardPage, HelpMenu.NULL_PAGE, reinforcementPage, "Help: Environmental Hazards");

		// Combat state pages
		helpMenu.addPage(combatPage, warPage1, HelpMenu.NULL_PAGE, "Help: Combat");
		helpMenu.addPage(warPage1, warPage2, combatPage, "Help: War pt.1");
		helpMenu.addPage(warPage2, HelpMenu.NULL_PAGE, warPage1, "Help: War pt.2");

		// Add the text to the pages
		reinforcementPage(reinforcementPage, helpMenu);
		setupPage(setupPage, helpMenu);
		hazardPage(hazardPage, helpMenu);
		movementPage(movementPage, helpMenu);
		combatPage(combatPage, helpMenu);
		warPage1(warPage1, helpMenu);
		warPage2(warPage2, helpMenu);
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
	 * Add the text of the reinforcement state.
	 * 
	 * @param id
	 *            The id of the reinforcement page.
	 * @param helpMenu
	 *            The {@link HelpMenu} that displays the help pages.
	 */
	private void reinforcementPage(int id, HelpMenu helpMenu) {

		helpMenu.addText(id, "In Reinforce, the current player distributes their"
				+ " available units to their countries. The available units are shown in the top left corner.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "The number of units given each round is dependent on "
				+ "the round and the number of countries owned. More can be earned through challenges.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "How to:");
		helpMenu.addText(id, "1. Select the country you wish to reinforce.");
		helpMenu.addText(id, "2. Click 'Reinforce' to add a unit to the selected country.");

	}

	/**
	 * Add the text of the set up state
	 * 
	 * @param id
	 *            The id of the setup page.
	 * @param helpMenu
	 *            The {@link HelpMenu} that displays the help pages.
	 */
	private void setupPage(int id, HelpMenu helpMenu) {

		helpMenu.addText(id, "In Setup, the players select who initially owns which countries.");

		helpMenu.addText(id, "To set the ruler of a country, simply"
				+ " click on the country then press the number button that corresponds to the number of the player."
				+ " For example: '1' corresponds to 'Player 1'. 'Space' makes the country neutral.");

		helpMenu.addText(id, "Click 'Auto' to randomly divide all the countries equally, between all the players.");

		helpMenu.addText(id, "Once you have assigned the countries as you desire click 'Play' to start the game.");
	}

	/**
	 * Add the text of the hazard page.
	 * 
	 * @param id
	 *            The id of the hazard page.
	 * @param helpMenu
	 *            The {@link HelpMenu} that displays the help pages.
	 */
	private void hazardPage(int id, HelpMenu helpMenu) {

		// Hazards
		helpMenu.addText(id,
				"After the first round, environmental hazards may occur in any country. "
						+ "These hazards will kill a random percentage of the army stationed at "
						+ "the country the hazard occured in, up to a maximum value.");

		helpMenu.addText(id, "These hazards appear as icons over the countries they have affected.");

		helpMenu.addText(id, "");

		for (ModelHazard hazard : ModelHazard.values()) {
			helpMenu.addText(id, hazard.name + ": " + hazard.maxCasualties + "% at " + hazard.chance + "% chance");
		}

	}

	/**
	 * Add the text of the movement page.
	 * 
	 * @param id
	 *            The id of the movement page.
	 * @param helpMenu
	 *            The {@link HelpMenu} that displays the help pages.
	 */
	private void movementPage(int id, HelpMenu helpMenu) {

		helpMenu.addText(id,
				"In Movement, the current player can move units from one country to another, provided that both countries are;");
		helpMenu.addText(id, "- Ruled by the same player.");
		helpMenu.addText(id, "- Connected by a chain of countries ruled by that player.");
		helpMenu.addText(id, "A valid troop movement will be shown by a line linking the selected countries.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "How to:");
		helpMenu.addText(id, "1. Select the origin country. Must have more than one unit.");
		helpMenu.addText(id, "2. Select the destination country. If the movement is valid, a link will be shown.");
		helpMenu.addText(id, "3. Click 'Fortify' to move one unit.");

	}

	/**
	 * Add the text of the combat page.
	 * 
	 * @param id
	 *            The id of the combat page.
	 * @param helpMenu
	 *            The {@link HelpMenu} that displays the help pages.
	 */
	private void combatPage(int id, HelpMenu helpMenu) {

		helpMenu.addText(id,
				"In Combat, the current player can attack adjacent enemy countries using their counrties.");
		helpMenu.addText(id, "Valid targets will be shown by a line linking them to the selected country.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "How to:");
		helpMenu.addText(id,
				"1. Select the country with more than 1 unit "
						+ "that you wish to attack with. A link will be shown to the enemy "
						+ "countries that can be attacked.");
		helpMenu.addText(id, "2. Select the adjacent enemy country you want to attack.");
		helpMenu.addText(id, "3. Click 'Attack' to start a war.");

	}

	/**
	 * Add the text of the war page 1.
	 * 
	 * @param id
	 *            The id of the war page 1.
	 * @param helpMenu
	 *            The {@link HelpMenu} that displays the help pages.
	 */
	private void warPage1(int id, HelpMenu helpMenu) {

		helpMenu.addText(id,
				"The war menu displays a war between the two highlighted countries."
						+ " The attacking army must be larger than one unit, as one unit is "
						+ "required for the defense of the country at all times.");
		helpMenu.addText(id, "");
		helpMenu.addText(id,
				"The attacker will have a choice of 1 to 3 dices to attack with, depending on the size of their army. "
						+ "The more dices rolled will result in a higher chance of winning.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "The number of dice corresponds to the "
				+ "number of units of the army that will be sent to attack the enemy player's army.");
		helpMenu.addText(id, "The defending army can roll a maximum of 2 dice but the defending"
				+ " unit will win if the dice rolled are the same.");

	}

	/**
	 * Add the text of the war page 2.
	 * 
	 * @param id
	 *            The id of the war page 2.
	 * @param helpMenu
	 *            The {@link HelpMenu} that displays the help pages.
	 */
	private void warPage2(int id, HelpMenu helpMenu) {
		helpMenu.addText(id,
				"In the situation where the attacking and defending armies are not equal,"
						+ " all the dice will be rolled but only the highest dice"
						+ " will be used in the battle, the extra dice will discarded.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "For example: If the attacker has 3 units but the defender only has 1."
				+ " The best dice of the attacker's 3 will be pit against the defender's only dice.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "If the player has successfully conqueured "
				+ "the enemy country, the last defending unit of the enemy country will desert and join the player's army.");

	}

}
