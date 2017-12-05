package peril.helpers;

import peril.Challenge;
import peril.Game;
import peril.Point;
import peril.board.EnvironmentalHazard;
import peril.ui.components.menus.*;

/**
 * A helper class that holds all the {@link Menu}s for the {@link Game}.
 * 
 * @author Joshua_Eddy
 *
 */
public final class MenuHelper {

	/**
	 * The {@link PauseMenu} for the {@link Game}
	 */
	public final PauseMenu pauseMenu;

	/**
	 * The {@link WarMenu} that processes all of the game's combat.
	 */
	public final WarMenu warMenu;

	/**
	 * The {@link HelpMenu} that holds the help information for the {@link Game}.
	 */
	public final HelpMenu helpMenu;

	/**
	 * The {@link ChallengeMenu} that displays the {@link Challenge}s to the user.
	 */
	public final ChallengeMenu challengeMenu;

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
	public MenuHelper(PauseMenu pauseMenu, WarMenu warMenu, HelpMenu helpMenu, ChallengeMenu challengeMenu) {
		this.helpMenu = helpMenu;
		this.warMenu = warMenu;
		this.pauseMenu = pauseMenu;
		this.challengeMenu = challengeMenu;
	}

	/**
	 * Moves all the {@link Menu}s so that they are centred based on the specified
	 * centre x and y.
	 * 
	 * @param centerX
	 * @param centerY
	 */
	public void center(int centerX, int centerY) {
		pauseMenu.setPosition(new Point(centerX - (pauseMenu.getWidth() / 2), centerY - (pauseMenu.getHeight() / 2)));
		helpMenu.setPosition(new Point(centerX - (helpMenu.getWidth() / 2), centerY - (helpMenu.getHeight() / 2)));
		warMenu.setPosition(new Point(centerX - (warMenu.getWidth() / 2), centerY - (warMenu.getHeight() / 2)));
		challengeMenu.setPosition(
				new Point(centerX - (challengeMenu.getWidth() / 2), centerY - (challengeMenu.getHeight() / 2)));
	}

	/**
	 * Initialises all the {@link Menu}s in this {@link MenuHelper}.
	 */
	public void initMenus() {
		helpMenu.init();
		pauseMenu.init();
		warMenu.init();
		challengeMenu.init();
	}

	/**
	 * Creates the {@link HelpMenu}'s pages.
	 * 
	 * @param game
	 *            {@link Game}
	 */
	public void createHelpPages(Game game) {

		int setupPage = game.states.setup.getID();

		int reinforcementPage = game.states.reinforcement.getID();

		int combatPage = game.states.combat.getID();

		int movementPage = game.states.movement.getID();

		// EnvironmentalHazards Page id
		int hazardPage = 30;

		// War menu page
		int warPage1 = 31;
		int warPage2 = 32;

		helpMenu.addPage(movementPage, HelpMenu.NULL_PAGE, HelpMenu.NULL_PAGE, "Help: Movement");

		// Setup state
		helpMenu.addPage(setupPage, HelpMenu.NULL_PAGE, HelpMenu.NULL_PAGE, "Help: Setup");

		// Reinforcement state pages
		helpMenu.addPage(reinforcementPage, hazardPage, HelpMenu.NULL_PAGE, "Help: Reinforcment");
		helpMenu.addPage(hazardPage, HelpMenu.NULL_PAGE, reinforcementPage, "Help: Environmental Hazards");

		// Combat state pages
		helpMenu.addPage(combatPage, warPage1, HelpMenu.NULL_PAGE, "Help: Combat");
		helpMenu.addPage(warPage1, warPage2, combatPage, "Help: War pt.1");
		helpMenu.addPage(warPage2, HelpMenu.NULL_PAGE, warPage1, "Help: War pt.2");

		reinforcementPage(reinforcementPage);
		setupPage(setupPage);
		environmentalHazardPage(hazardPage);
		movementPage(movementPage);
		combatPage(combatPage);
		warPage1(warPage1);
		warPage2(warPage2);
	}

	/**
	 * Add the text of the reinforcement state.
	 * 
	 * @param id
	 *            The id of the reinforcement page.
	 */
	private void reinforcementPage(int id) {

		helpMenu.addText(id, "In this phase the current player should distribute"
				+ " their available units to any of their countries.");
		helpMenu.addText(id, "The available units to be distributed are shown in the top left corner, above units.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "How to:");
		helpMenu.addText(id, "1. Select the country you wish to reinforce.");
		helpMenu.addText(id, "2. Click the rienforment button to add a unit yo the selected country");

	}

	/**
	 * Add the text of the set up state
	 * 
	 * @param id
	 *            The id of the setup page.
	 */
	private void setupPage(int id) {

		helpMenu.addText(id, "In this phase of the game the players select which "
				+ "players own which countries at the start of the game.");

		helpMenu.addText(id, "To set a player as the ruler of a country, simply"
				+ " click on the country then press the number button that coresponds to the number of the player."
				+ " For example: '1' corresponds to 'Player 1'. 'Space' makes the country neutral.");

		helpMenu.addText(id, "Click 'Auto' to randomly assign an equal number of countries to each player.");
		helpMenu.addText(id, "Once you have assigned the countries as you desire click 'Play' to start the game.");
	}

	/**
	 * Add the text of the environmental hazard page.
	 * 
	 * @param id
	 *            The id of the environmental hazard page.
	 */
	private void environmentalHazardPage(int id) {

		// Environmental Hazards
		helpMenu.addText(id,
				"After the first round environmental hazards may occur in any country. "
						+ "These hazards will kill a random percentage of the army stationed at "
						+ "the country the hazard occured up to a maximum value.");
		helpMenu.addText(id, "These hazards appear as icons over the countries they have effected.");
		helpMenu.addText(id, "");
		for (EnvironmentalHazard hazard : EnvironmentalHazard.values()) {
			helpMenu.addText(id, hazard.name + ": " + hazard.maxCasualties + "% at " + hazard.chance + "% chance");
		}

	}

	/**
	 * Add the text of the movement page.
	 * 
	 * @param id
	 *            The id of the movement page.
	 */
	private void movementPage(int id) {

		helpMenu.addText(id,
				"In this phase, the current player can move"
						+ " units from all countries they own to any of their countries which are connected."
						+ " Countries are connected by a link or through many owned lands connected to each other.");
		helpMenu.addText(id, "");
		helpMenu.addText(id,
				"A valid re-distributuion of the troops will be shown by a line linking the selected countries.");
		helpMenu.addText(id, "How to:");
		helpMenu.addText(id, "1. Select the country with 1< of units you wish to move.");
		helpMenu.addText(id,
				"2. Select the country you want the units to move to. A link will be shown, should this is be a valid movement");
		helpMenu.addText(id, "3. Select the amount of units to be moved.");

	}

	/**
	 * Add the text of the combat page.
	 * 
	 * @param id
	 *            The id of the combat page.
	 */
	private void combatPage(int id) {

		helpMenu.addText(id, "In this phase if the current player can start a war with an adjacent enemy country.");
		helpMenu.addText(id, "Valid targets will be shown by a line linking the selected countries.");
		helpMenu.addText(id, "");
		helpMenu.addText(id, "How to:");
		helpMenu.addText(id,
				"1. Select the country with more than 1 unit "
						+ "that you wish to attack with. A link will be shown to the enemy "
						+ "countries which can be attacked.");
		helpMenu.addText(id, "2. Select the adjacent enemy country you want to attack.");
		helpMenu.addText(id, "3. Click 'Attack' to start a war and open the war menu.");

	}

	/**
	 * Add the text of the war page 1.
	 * 
	 * @param id
	 *            The id of the war page 1.
	 */
	private void warPage1(int id) {

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
	private void warPage2(int id) {
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
