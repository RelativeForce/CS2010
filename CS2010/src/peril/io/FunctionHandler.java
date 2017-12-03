package peril.io;

import peril.Game;
import peril.Player;
import peril.board.Continent;
import peril.board.Country;
import peril.multiThread.Action;
import peril.ui.Button;
import peril.ui.components.menus.HelpMenu;
import peril.ui.components.menus.PauseMenu;
import peril.ui.components.menus.WarMenu;

/**
 * 
 * This hold the functionality of every {@link Button} in the {@link Game}.
 * 
 * @author Mohammad_ali_Sayed_Ackbar
 *
 */
public class FunctionHandler {

	/**
	 * The {@link Game} this {@link FunctionHandler} is a part of.
	 */
	private final Game game;

	/**
	 * 
	 * Constructs a new {@link FunctionHandler}
	 * 
	 * @param game
	 *            The {@link Game} this {@link FunctionHandler} is a part of.
	 */
	public FunctionHandler(Game game) {
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
			return playGame();
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
			return showHelp();
		case 14:
			return hideHelp();
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
		}
		throw new IllegalArgumentException(code + " is not a valid function code.");
	}

	private Action<?> hideChallengeMenu() {
		return new Action<Game>(game, game -> game.menus.challengeMenu.hide());
	}

	private Action<?> showChallengeMenu() {
		return new Action<Game>(game, game -> game.menus.challengeMenu.show());
	}

	/**
	 * Retrieves the {@link Action} that hides the war menu.
	 * 
	 * @return
	 */
	private Action<?> hideWarMenu() {
		return new Action<Game>(game, game -> game.menus.warMenu.hide());
	}

	/**
	 * Retrieves the {@link Action} that hides the pause menu.
	 * 
	 * @return
	 */
	private Action<?> hidePauseMenu() {
		return new Action<Game>(game, game -> game.menus.pauseMenu.hide());
	}

	/**
	 * Retrieves the {@link Action} that shows the pause menu.
	 * 
	 * @return
	 */
	private Action<?> showPauseMenu() {
		return new Action<Game>(game, game -> game.menus.pauseMenu.show());
	}

	/**
	 * Retrieves the {@link Action} that moves the {@link HelpMenu} to the previous
	 * page.
	 * 
	 * @return
	 */
	private Action<?> previousHelpPage() {
		return new Action<Game>(game, game -> game.menus.helpMenu.previousPage());
	}

	/**
	 * Retrieves the {@link Action} that moves the {@link HelpMenu} to the next
	 * page.
	 * 
	 * @return
	 */
	private Action<?> nextHelpPage() {
		return new Action<Game>(game, game -> game.menus.helpMenu.nextPage());
	}

	/**
	 * Retrieves a function that show the {@link WarMenu} between visible and
	 * invisible.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> showWarMenu() {
		return new Action<Game>(game, game -> game.menus.warMenu.show());
	}

	/**
	 * Retrieves the {@link Action} that will add one unit to a {@link Country} and
	 * remove one unit from the current {@link Player#getDistributableArmySize()}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> reinforceCountry() {
		return new Action<Game>(game, game -> game.states.reinforcement.reinfoce());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#reinforcement} to {@link Game#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterCombat() {
		return new Action<Game>(game, game -> game.enterState(game.states.combat));
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#combat} to {@link Game#movement}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterMovement() {
		return new Action<Game>(game, game -> game.enterState(game.states.movement));
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#movement} to {@link Game#reinforcement}, moving tp the next
	 * player in the process.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterReinforment() {
		return new Action<Game>(game, game -> {
			game.enterState(game.states.reinforcement);
			game.players.nextPlayer();
		});
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#setup} to {@link Game#reinforcement}, checking the ownership
	 * of {@link Continent}s in the process.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> leaveSetUp() {
		return new Action<Game>(game, game -> {

			// checks the ownership of the continents
			game.checkContinentRulership();

			// Change the state of the game to reinforcement and give player one their units
			// based on the countries they own.
			game.players.reinforceCurrent();
			game.enterState(game.states.reinforcement);
		});
	}

	/**
	 * Retrieves the {@link Action} that will move one unit from one country to
	 * another as long as the countries are own by the same player.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> fortifyCountry() {
		return new Action<Game>(game, game -> game.states.movement.fortify());
	}

	/**
	 * Retrieves the {@link Action} that will perform one round of the
	 * {@link WarMenu#fight(Country, Country, int)} on the two countries selected in
	 * the {@link Game#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> excuteCombat() {
		return new Action<Game>(game, game -> game.states.combat.warMenu.attack());
	}

	/**
	 * Retrieves the {@link Action} that will move the {@link Game} from the
	 * {@link Game#mainMenu} to the {@link Game#setup} state and load the
	 * {@link Board} specified by the {@link Game#mainMenu}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> playGame() {
		return new Action<Game>(game, game -> {
			try {
				game.states.mainMenu.loadMap();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Retrieves the {@link Action} that will re-assign the {@link Country}s to new
	 * {@link Player}s.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> reAssignCountries() {
		return new Action<Game>(game, game -> {

			// Unhighlight the highlighted country
			game.states.setup.removeHighlightFrom(game.states.setup.getHighlightedCountry());
			game.states.setup.highlightCountry(null);

			// Assign the countries
			game.autoDistributeCountries();
		});

	}

	/**
	 * Retrieves the {@link Action} that will move the {@link Game} from the
	 * {@link Game#pauseMenu} to the {@link Game#mainMenu} state
	 */
	private Action<?> enterMainMenu() {
		return new Action<Game>(game, game -> {
			game.players.resetAll();
			game.enterState(game.states.mainMenu);
		});
	}

	/**
	 * Retrieves the {@link Action} that will exit the {@link Game}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> exitGame() {
		return new Action<Game>(game, game -> game.getContainer().exit());
	}

	/**
	 * Retrieves the {@link Action} that causes the {@link PauseMenu} to save the
	 * current state of the game.
	 * 
	 * @return
	 */
	private Action<?> saveGame() {
		return new Action<Game>(game, game -> game.menus.pauseMenu.save());
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showHelp() {
		return new Action<Game>(game, game -> game.menus.helpMenu.show());
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hideHelp() {
		return new Action<Game>(game, game -> game.menus.helpMenu.hide());
	}
}
