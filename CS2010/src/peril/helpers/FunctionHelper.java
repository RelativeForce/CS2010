package peril.helpers;

import java.io.IOException;

import peril.concurrent.Action;
import peril.controllers.GameController;

/**
 * 
 * This hold the functionality of every {@link Button} in the
 * {@link GameController}.
 * 
 * @author Mohammad_ali_Sayed_Ackbar
 *
 */
public class FunctionHelper {

	/**
	 * The {@link GameController} this {@link FunctionHelper} is a part of.
	 */
	private final GameController game;

	/**
	 * 
	 * Constructs a new {@link FunctionHelper}
	 * 
	 * @param game
	 *            The {@link GameController} this {@link FunctionHelper} is a part
	 *            of.
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
			return loadGameController();
		case 8:
			return reAssignCountries();
		case 9:
			return showWarMenu();
		case 10:
			return exitGameController();
		case 11:
			return saveGameController();
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
		case 22:
			return leavePlayerSelect();
		case 23:
			return openWebsite();
		}
		throw new IllegalArgumentException(code + " is not a valid function code.");
	}

	/**
	 * Retrieves a {@link Action} that opens the strategic goats web site.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> openWebsite() {
		return new Action<GameController>(game, game -> {
			String url = "http://strategicgoats.github.io";
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
	 * Retrieves the {@link Action} that will add one unit to a {@link SlickCountry}
	 * and remove one unit from the current
	 * {@link SlickPlayer#getDistributableArmySize()}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> reinforceCountry() {
		return new Action<GameController>(game, game -> game.getReinforce().reinforce(game));
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the
	 * {@link GameController} from {@link GameController#reinforcement} to
	 * {@link GameController#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterCombat() {
		return new Action<GameController>(game, game -> game.confirmReinforce());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the
	 * {@link GameController} from {@link GameController#combat} to
	 * {@link GameController#movement}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterMovement() {
		return new Action<GameController>(game, game -> game.confirmCombat());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the
	 * {@link GameController} from {@link GameController#movement} to
	 * {@link GameController#reinforcement}, moving tp the next player in the
	 * process.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterReinforment() {
		return new Action<GameController>(game, game -> game.confirmMovement());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the
	 * {@link GameController} from {@link GameController#setup} to
	 * {@link GameController#reinforcement}, checking the ownership of
	 * {@link SlickContinent}s in the process.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> leaveSetUp() {
		return new Action<GameController>(game, game -> game.confirmSetup());
	}

	/**
	 * Retrieves the {@link Action} that will move one unit from one country to
	 * another as long as the countries are own by the same player.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> fortifyCountry() {
		return new Action<GameController>(game, game -> game.fortify());
	}

	/**
	 * Retrieves the {@link Action} that will perform one round of the
	 * {@link WarMenu#fight(SlickCountry, SlickCountry, int)} on the two countries
	 * selected in the {@link GameController#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> excuteCombat() {
		return new Action<GameController>(game, game -> game.getView().attack());
	}

	/**
	 * Retrieves the {@link Action} that will move the {@link GameController} from
	 * the {@link GameController#mainMenu} to the {@link GameController#setup} state
	 * and load the {@link ModelBoard} specified by the
	 * {@link GameController#mainMenu}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> loadGameController() {
		return new Action<GameController>(game, game -> {
			try {
				game.getView().loadGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Retrieves the {@link Action} that will re-assign the {@link SlickCountry}s to
	 * new {@link SlickPlayer}s.
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
	 * Retrieves the {@link Action} that will move the {@link GameController} from
	 * the {@link GameController#pauseMenu} to the {@link GameController#mainMenu}
	 * state
	 */
	private Action<?> enterMainMenu() {
		return new Action<GameController>(game, game -> game.getView().enterMainMenu());
	}

	/**
	 * Retrieves the {@link Action} that will exit the {@link GameController}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> exitGameController() {
		return new Action<GameController>(game, game -> game.getView().exit());
	}

	/**
	 * Retrieves the {@link Action} that causes the {@link PauseMenu} to save the
	 * current state of the game.
	 * 
	 * @return
	 */
	private Action<?> saveGameController() {
		return new Action<GameController>(game, game -> game.getView().save());
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showHelp() {
		return new Action<GameController>(game, game -> game.getView().toggleHelpMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hideHelp() {
		return new Action<GameController>(game, game -> game.getView().toggleHelpMenu(false));
	}
}
