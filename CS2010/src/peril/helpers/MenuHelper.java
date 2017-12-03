package peril.helpers;

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
	 * Constructs a new {@link MenuHelper}.
	 * 
	 * @param pauseMenu
	 *            The {@link PauseMenu} for the {@link Game}
	 * @param warMenu
	 *            The {@link WarMenu} that processes all of the game's combat.
	 * @param helpMenu
	 *            The {@link HelpMenu} that holds the help information for the
	 *            {@link Game}.
	 */
	public MenuHelper(PauseMenu pauseMenu, WarMenu warMenu, HelpMenu helpMenu) {
		this.helpMenu = helpMenu;
		this.warMenu = warMenu;
		this.pauseMenu = pauseMenu;
	}

	public void createHelpPages(Game game) {
		
		int setupPage = game.states.setup.getID();
		
		// Reinforcement state help page id
		int reinforcementPage =  game.states.reinforcement.getID();
		
		int combatPage = game.states.combat.getID();
		
		int movementPage = game.states.movement.getID();
		
		// EnvironmentalHazards Page id
		int hazardPage = 30;
		
		// Assign the pages to each other
		helpMenu.addPage(reinforcementPage, hazardPage, HelpMenu.NULL_PAGE, "Help: Reinforcment");
		helpMenu.addPage(hazardPage, HelpMenu.NULL_PAGE, reinforcementPage, "Help: Environmental Hazards");
		helpMenu.addPage(setupPage, HelpMenu.NULL_PAGE, HelpMenu.NULL_PAGE, "Help: Setup");
		
		
		// Setup
		helpMenu.addText(setupPage,"In this phase of the game the players select which players own"
				+ " which countries at the start of the game.");
		helpMenu.addText(setupPage,"To set a player as the ruler of a country, simply click on the country then press "
				+ "the number button that coresponds to the number of the player."
				+ " For example: '1' corresponds to 'Player 1'. 'Space' makes the country neutral.");
		helpMenu.addText(setupPage,"Click 'Auto' to randomly assign an equal number of countries to each player.");
		helpMenu.addText(setupPage,"Once you have assigned the countries as you desire click 'Play' to start the game.");
		
		
		// Environmental Hazards
		helpMenu.addText(hazardPage,"After the first round environmental hazards may occur in any country. "
				+ "These hazards will kill a random percentage of the army stationed at "
				+ "the country the hazard occured up to a maximum value.");
		for (EnvironmentalHazard hazard : EnvironmentalHazard.values()) {
			helpMenu.addText(hazardPage ,hazard.name + ": " + hazard.maxCasualties + "% at " + hazard.chance + "% chance");
		}
		
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
	}
}
