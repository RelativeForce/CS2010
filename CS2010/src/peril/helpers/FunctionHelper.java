package peril.helpers;

import java.io.IOException;

import peril.concurrent.Action;
import peril.controllers.GameController;
import peril.views.slick.components.menus.UnitMenu;

/**
 * 
 * This hold the functionality of every predefined {@link Button} in the game.
 * 
 * @author Mohammad_ali_Sayed_Ackbar, Joshua_Eddy, Joseph_Rolli
 * 
 * @version 1.01.02
 * @since 2018-02-20
 *
 */
public final class FunctionHelper {

	/**
	 * The {@link GameController} this {@link FunctionHelper} uses to interact with
	 * the game.
	 */
	private final GameController game;

	/**
	 * 
	 * Constructs a new {@link FunctionHelper}
	 * 
	 * @param game
	 *            The {@link GameController} this {@link FunctionHelper} uses to
	 *            interact with the game.
	 */
	public FunctionHelper(GameController game) {
		this.game = game;
	}

	/**
	 * Retrieves a predefined {@link Action} using a <code>int</code> code.
	 * 
	 * @param code
	 *            <code>int</code> denoting the {@link Action}
	 * @return {@link Action}
	 */
	public Action<?> get(int code) {

		// Switch between the function codes.
		switch (code) {

		case 0:
			return reinforceCountry();
		case 1:
			return enterCombat();
		case 2:
			return enterMovement();
		case 3:
			return enterReinforment();
		case 4:
			return leaveSetUp();
		case 5:
			return fortifyCountry();
		case 6:
			return excuteCombat();
		case 7:
			return loadGame();
		case 8:
			return reAssignCountries();
		case 9:
			return showWarMenu();
		case 10:
			return exitGame();
		case 11:
			return saveGame();
		case 12:
			return enterMainMenu();
		case 13:
			return showHelpMenu();
		case 14:
			return hideHelpMenu();
		case 15:
			return nextHelpPage();
		case 16:
			return previousHelpPage();
		case 17:
			return showPauseMenu();
		case 18:
			return hidePauseMenu();
		case 19:
			return hideWarMenu();
		case 20:
			return showChallengeMenu();
		case 21:
			return hideChallengeMenu();
		case 22:
			return leavePlayerSelect();
		case 23:
			return openWebsite();
		case 24:
			return showStatsMenu();
		case 25:
			return hideStatsMenu();
		case 26:
			return showPointsMenu();
		case 27:
			return hidePointsMenu();
		case 28:
			return showUpgradeMenu();
		case 29:
			return hideUpgradeMenu();
		case 30:
			return showUnitMenu();
		case 31:
			return hideUnitMenu();

		}

		// If no action was returned then the function code was invalid.
		throw new IllegalArgumentException(code + " is not a valid function code.");
	}

	/**
	 * Retrieves a {@link Action} that opens the strategic goats web site.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> openWebsite() {
		return new Action<GameController>(game, game -> {

			// The url of the website.
			final String url = "http://strategicgoats.github.io";

			// Attempt to open the website in the default browser.
			try {
				java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Retrieves the {@link Action} that causes the {@link GameController} to exit
	 * the player selection screen.
	 * 
	 * @return
	 */
	private Action<?> leavePlayerSelect() {
		return new Action<GameController>(game, game -> {

			// Attempt to load the game.
			try {
				game.getView().loadGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Retrieves the {@link Action} that hides the challenge menu.
	 * 
	 * @return
	 */
	private Action<?> hideChallengeMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleChallengeMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that shows the challenge menu.
	 * 
	 * @return
	 */
	private Action<?> showChallengeMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleChallengeMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that hides the war menu.
	 * 
	 * @return
	 */
	private Action<?> hideWarMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleWarMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that hides the pause menu.
	 * 
	 * @return
	 */
	private Action<?> hidePauseMenu() {
		return new Action<GameController>(game, game -> game.getView().togglePauseMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that shows the pause menu.
	 * 
	 * @return
	 */
	private Action<?> showPauseMenu() {
		return new Action<GameController>(game, game -> game.getView().togglePauseMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that moves the {@link HelpMenu} to the previous
	 * page.
	 * 
	 * @return
	 */
	private Action<?> previousHelpPage() {
		return new Action<GameController>(game, game -> game.getView().previousHelpPage());
	}

	/**
	 * Retrieves the {@link Action} that moves the {@link HelpMenu} to the next
	 * page.
	 * 
	 * @return
	 */
	private Action<?> nextHelpPage() {
		return new Action<GameController>(game, game -> game.getView().nextHelpPage());
	}

	/**
	 * Retrieves a function that show the {@link WarMenu} between visible and
	 * invisible.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> showWarMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleWarMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that will reinforce the currently selected
	 * country.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> reinforceCountry() {
		return new Action<GameController>(game, game -> game.getReinforce().reinforce(game));
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the game from
	 * reinforce to attack.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterCombat() {
		return new Action<GameController>(game, game -> game.confirmReinforce());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the game from
	 * attack to fortify.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterMovement() {
		return new Action<GameController>(game, game -> game.confirmCombat());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the game from
	 * fortify to reinforce of the next player.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterReinforment() {
		return new Action<GameController>(game, game -> game.confirmMovement());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the game from
	 * setup to reinforce.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> leaveSetUp() {
		return new Action<GameController>(game, game -> {
			// Only confirm setup if all players have at least 1 country assigned.

			int playerID = 0;

			for (int playerIndex = 1; playerIndex <= PlayerHelper.MAX_PLAYERS; playerIndex++) {
				if (game.isPlaying(playerIndex)) {
					if (game.getModelPlayer(playerIndex).getCountriesRuled() == 0) {
						playerID = playerIndex;
					}
				}
			}

			if (playerID == 0) {
				game.confirmSetup();
			} else {
				game.getView().showToolTip("Player " + playerID + " does not have any countries assigned.");
			}
		});

	}

	/**
	 * Retrieves the {@link Action} that will move one unit from one country to
	 * another as long as the countries are own by the same player.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> fortifyCountry() {
		return new Action<GameController>(game, game -> game.getFortify().fortify());
	}

	/**
	 * Retrieves the {@link Action} that will perform one round of the combat
	 * between the two selected countries.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> excuteCombat() {
		return new Action<GameController>(game, game -> game.getView().attack());
	}

	/**
	 * Retrieves the {@link Action} that will load the game.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> loadGame() {
		return new Action<GameController>(game, game -> {

			// Attempt to load the game.
			try {
				game.getView().loadGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Retrieves the {@link Action} that will randomly re-assign the countries to
	 * new players.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> reAssignCountries() {
		return new Action<GameController>(game, game -> {

			// Remove selected
			game.getSetup().deselectAll();

			// Assign the countries
			game.autoDistributeCountries();
		});

	}

	/**
	 * Retrieves the {@link Action} that will return the game back to the main menu.
	 */
	private Action<?> enterMainMenu() {
		return new Action<GameController>(game, game -> game.getView().enterMainMenu());
	}

	/**
	 * Retrieves the {@link Action} that will close the game.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> exitGame() {
		return new Action<GameController>(game, game -> game.getView().exit());
	}

	/**
	 * Retrieves the {@link Action} that will save the game.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> saveGame() {
		return new Action<GameController>(game, game -> game.getView().save());
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showHelpMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleHelpMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hideHelpMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleHelpMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link StatsMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showStatsMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleStatsMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link StatsMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hideStatsMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleStatsMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link PointsMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showPointsMenu() {
		return new Action<GameController>(game, game -> game.getView().togglePointsMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link PointsMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hidePointsMenu() {
		return new Action<GameController>(game, game -> game.getView().togglePointsMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link UnitMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showUnitMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleUnitMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link UnitMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hideUnitMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleUnitMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link UpgradeMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showUpgradeMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleUpgradeMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link UpgradeMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hideUpgradeMenu() {
		return new Action<GameController>(game, game -> game.getView().toggleUpgradeMenu(false));
	}
}
