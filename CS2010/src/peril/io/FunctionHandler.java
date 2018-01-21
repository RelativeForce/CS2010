package peril.io;

import java.io.IOException;

import peril.Game;
import peril.multiThread.Action;

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
		return new Action<Game>(game, game -> {
			String url = "http://strategicgoats.github.io";
			try {
				java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Retrieves the {@link Action} that causes the {@link Game} to exit the player
	 * selection screen.
	 * 
	 * @return
	 */
	private Action<?> leavePlayerSelect() {
		return new Action<Game>(game, game -> {
			try {
				game.view.loadGame();
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
		return new Action<Game>(game, game -> game.view.toggleChallengeMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that shows the challenge menu.
	 * 
	 * @return
	 */
	private Action<?> showChallengeMenu() {
		return new Action<Game>(game, game -> game.view.toggleChallengeMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that hides the war menu.
	 * 
	 * @return
	 */
	private Action<?> hideWarMenu() {
		return new Action<Game>(game, game -> game.view.toggleWarMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that hides the pause menu.
	 * 
	 * @return
	 */
	private Action<?> hidePauseMenu() {
		return new Action<Game>(game, game -> game.view.togglePauseMenu(false));
	}

	/**
	 * Retrieves the {@link Action} that shows the pause menu.
	 * 
	 * @return
	 */
	private Action<?> showPauseMenu() {
		return new Action<Game>(game, game -> game.view.togglePauseMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that moves the {@link HelpMenu} to the previous
	 * page.
	 * 
	 * @return
	 */
	private Action<?> previousHelpPage() {
		return new Action<Game>(game, game -> game.view.previousHelpPage());
	}

	/**
	 * Retrieves the {@link Action} that moves the {@link HelpMenu} to the next
	 * page.
	 * 
	 * @return
	 */
	private Action<?> nextHelpPage() {
		return new Action<Game>(game, game -> game.view.nextHelpPage());
	}

	/**
	 * Retrieves a function that show the {@link WarMenu} between visible and
	 * invisible.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> showWarMenu() {
		return new Action<Game>(game, game -> game.view.toggleWarMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that will add one unit to a {@link SlickCountry}
	 * and remove one unit from the current
	 * {@link SlickPlayer#getDistributableArmySize()}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> reinforceCountry() {
		return new Action<Game>(game, game -> game.states.reinforcement.reinforce(game.getGameController()));
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#reinforcement} to {@link Game#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterCombat() {
		return new Action<Game>(game, game -> game.confirmReinforce());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#combat} to {@link Game#movement}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterMovement() {
		return new Action<Game>(game, game -> game.confirmCombat());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#movement} to {@link Game#reinforcement}, moving tp the next
	 * player in the process.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterReinforment() {
		return new Action<Game>(game, game -> game.confirmMovement());
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#setup} to {@link Game#reinforcement}, checking the ownership
	 * of {@link SlickContinent}s in the process.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> leaveSetUp() {
		return new Action<Game>(game, game -> game.confirmSetup());
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
	 * {@link WarMenu#fight(SlickCountry, SlickCountry, int)} on the two countries
	 * selected in the {@link Game#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> excuteCombat() {
		return new Action<Game>(game, game -> game.view.attack());
	}

	/**
	 * Retrieves the {@link Action} that will move the {@link Game} from the
	 * {@link Game#mainMenu} to the {@link Game#setup} state and load the
	 * {@link ModelBoard} specified by the {@link Game#mainMenu}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> loadGame() {
		return new Action<Game>(game, game -> {
			try {
				game.view.loadGame();
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
		return new Action<Game>(game, game -> {

			// Remove selected
			game.states.setup.deselectAll();

			// Assign the countries
			game.autoDistributeCountries();
		});

	}

	/**
	 * Retrieves the {@link Action} that will move the {@link Game} from the
	 * {@link Game#pauseMenu} to the {@link Game#mainMenu} state
	 */
	private Action<?> enterMainMenu() {
		return new Action<Game>(game, game -> game.view.enterMainMenu());
	}

	/**
	 * Retrieves the {@link Action} that will exit the {@link Game}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> exitGame() {
		return new Action<Game>(game, game -> game.view.exit());
	}

	/**
	 * Retrieves the {@link Action} that causes the {@link PauseMenu} to save the
	 * current state of the game.
	 * 
	 * @return
	 */
	private Action<?> saveGame() {
		return new Action<Game>(game, game -> game.view.save());
	}

	/**
	 * Retrieves the {@link Action} that opens the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> showHelp() {
		return new Action<Game>(game, game -> game.view.toggleHelpMenu(true));
	}

	/**
	 * Retrieves the {@link Action} that closes the {@link HelpMenu} window.
	 * 
	 * @return
	 */
	private Action<?> hideHelp() {
		return new Action<Game>(game, game -> game.view.toggleHelpMenu(false));
	}
}
