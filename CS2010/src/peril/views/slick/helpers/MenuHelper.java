package peril.views.slick.helpers;

import java.util.IdentityHashMap;
import java.util.Map;

import org.newdawn.slick.Graphics;

import peril.Challenge;
import peril.Game;
import peril.model.board.ModelHazard;
import peril.views.slick.Point;
import peril.views.slick.components.menus.*;

/**
 * A helper class that holds all the {@link Menu}s for the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class MenuHelper {

	private final Map<String, Menu> menus;

	private Menu visible;

	/**
	 * Constructs a new {@link MenuHelper}.
	 * 
	 * @param pauseMenu
	 *            The {@link PauseMenu} for the {@link Game}
	 * @param warMenu
	 *            The {@link WarMenu} that processes all of the game's combat.
	 * @param helpMenu
	 *            The {@link HelpMenu} that holds the help information for the
	 *            {@link Game}.
	 * @param challengeMenu
	 *            The {@link ChallengeMenu} that displays the {@link Challenge}s to
	 *            the user.
	 */
	public MenuHelper(PauseMenu pauseMenu, WarMenu warMenu, HelpMenu helpMenu, ChallengeMenu challengeMenu, StatsMenu statsMenu, UnitMenu unitMenu, UpgradeMenu upgradeMenu, PointsMenu pointsmenu) {

		this.menus = new IdentityHashMap<>();
		this.menus.put(pauseMenu.getName(), pauseMenu);
		this.menus.put(warMenu.getName(), warMenu);
		this.menus.put(helpMenu.getName(), helpMenu);
		this.menus.put(challengeMenu.getName(), challengeMenu);
		this.menus.put(statsMenu.getName(), statsMenu);
		this.menus.put(unitMenu.getName(), unitMenu);
		this.menus.put(upgradeMenu.getName(), upgradeMenu);
		this.menus.put(pointsmenu.getName(), pointsmenu);

		this.visible = null;
	}

	/**
	 * Moves all the {@link Menu}s so that they are centred based on the specified
	 * centre x and y.
	 * 
	 * @param centerX
	 * @param centerY
	 */
	public void center(int centerX, int centerY) {
		menus.forEach((name, menu) -> {
			menu.setPosition(new Point(centerX - (menu.getWidth() / 2), centerY - (menu.getHeight() / 2)));
		});
	}

	/**
	 * Initialises all the {@link Menu}s in this {@link MenuHelper}.
	 */
	public void initMenus() {
		menus.forEach((name, menu) -> menu.init());
	}

	public void show(String menuName) {

		for (String name : menus.keySet()) {
			if (name.equals(menuName)) {
				showMenu(menus.get(name));
				break;
			}
		}
	}

	public void changeHelpPage(int id) {
		((HelpMenu) menus.get(HelpMenu.NAME)).changePage(id);
	}

	private void showMenu(Menu menu) {
		hideVisible();

		if (menu != null) {
			visible = menu;
			visible.show();
		}

	}

	public void hideVisible() {
		if (visible != null) {
			visible.hide();
			visible = null;
		}
	}

	public void hide(String menuName) {
		
		if(visible != null && visible.getName().equals(menuName)) {
			hideVisible();
		}
		
	}
	
	public void save() {
		((PauseMenu) menus.get(PauseMenu.NAME)).save();
	}
	
	public boolean menuVisible() {
		return visible != null;
	}

	public boolean linksVisible() {
		return ((PauseMenu) menus.get(PauseMenu.NAME)).showAllLinks();
	}

	/**
	 * If the currently visible menu has been clicked
	 * 
	 * @param click
	 * @return
	 */
	public boolean clicked(Point click) {
		
		// If there is no visible menu
		if(visible == null) {
			return false;
		}
		
		// If the visible menu is not clicked
		if(!visible.isVisible() || !visible.isClicked(click)) {
			return false;
		}
		
		visible.parseClick(click);
		
		return true;
	
	}
	
	public void showSaveOption() {
		((PauseMenu) menus.get(PauseMenu.NAME)).showSaveOption();
	}
	
	public void hideSaveOption() {
		((PauseMenu) menus.get(PauseMenu.NAME)).hideSaveOption();
	}
	
	public void nextHelpPage() {
		((HelpMenu) menus.get(HelpMenu.NAME)).nextPage();
	}
	
	public void previousHelpPage() {
		((HelpMenu) menus.get(HelpMenu.NAME)).previousPage();
	}
	
	public void clearMenus() {
		((WarMenu) menus.get(WarMenu.NAME)).clear();
	}

	/**
	 * Creates the {@link HelpMenu}'s pages.
	 * 
	 * @param game
	 *            {@link Game}
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
		environmentalHazardPage(hazardPage, helpMenu);
		movementPage(movementPage, helpMenu);
		combatPage(combatPage, helpMenu);
		warPage1(warPage1, helpMenu);
		warPage2(warPage2, helpMenu);
	}

	public void refreshSaveFiles() {
		((PauseMenu) menus.get(PauseMenu.NAME)).refreshSaveFiles();
	}

	/**
	 * Draws the currently visible {@link Menu}.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (visible != null) {
			visible.draw(g);
		}
	}

	/**
	 * Add the text of the reinforcement state.
	 * 
	 * @param id
	 *            The id of the reinforcement page.
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
	 * Add the text of the environmental hazard page.
	 * 
	 * @param id
	 *            The id of the environmental hazard page.
	 */
	private void environmentalHazardPage(int id, HelpMenu helpMenu) {

		// Environmental Hazards
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
	
	public void refreshChallenges() {
		((ChallengeMenu) menus.get(ChallengeMenu.NAME)).refreshChallenges();
	}
	
	public void attack() {
		WarMenu warMenu = (WarMenu) menus.get(WarMenu.NAME);
		warMenu.attack();
	}
	
	public void autoAttack() {
		
		WarMenu warMenu = (WarMenu) menus.get(WarMenu.NAME);
		show(WarMenu.NAME);
		warMenu.selectMaxUnits();
		warMenu.attack();
		
	}

	public boolean isPaused() {
		return ((PauseMenu) menus.get(PauseMenu.NAME)).isVisible();
	}

}
