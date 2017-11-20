package peril.io;

import peril.Game;
import peril.Player;
import peril.board.Army;
import peril.board.Continent;
import peril.board.Country;
import peril.multiThread.Action;
import peril.ui.Button;
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
	private Game game;

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
			return toggleWarMenu();
		case 10:
			return exitGame();
		case 11:
			return saveGame();
		}
		throw new IllegalArgumentException(code + " is not a valid function code.");
	}

	private Action<?> toggleWarMenu() {
		return new Action<Game>(game, game -> {
			game.states.combat.warMenu.visible = !game.states.combat.warMenu.visible;
		});
	}

	/**
	 * Retrieves the {@link Action} that will add one unit to a {@link Country} and
	 * remove one unit from the current {@link Player#getDistributableArmySize()}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> reinforceCountry() {
		return new Action<Game>(game, game -> {

			// Holds the currently highlighted country
			Country highlightedCountry = game.states.reinforcement.getHighlightedCountry();

			// Holds the current player.
			Player player = game.players.getCurrent();

			// Holds the size of the army that the player has to distribute.
			int armySize = player.getDistributableArmySize();

			// If there is a country highlighted.
			if (highlightedCountry != null) {

				// If the player has any units to place
				if (armySize > 0) {

					Player ruler = highlightedCountry.getRuler();

					// If the highlighted country has a ruler and it is that player
					if (ruler != null && ruler.equals(player)) {

						// Get that country's army and increase its size by one.
						Army army = highlightedCountry.getArmy();
						army.setSize(army.getSize() + 1);

						// Remove the unit from the list of units to place.
						player.setDistributableArmySize(armySize - 1);
						player.setTotalArmySize(player.getTotalArmySize() + 1);
						game.players.checkChallenges(player);

						if (player.getDistributableArmySize() == 0) {
							game.states.reinforcement.hideReinforceButton();
						}

					} else {
						System.out.println(player.toString() + " does not rule this country");
					}

				} else {
					System.out.println("No units to distribute.");
				}

			} else {
				System.out.println("No country selected.");
			}
		});

	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#reinforcement} to {@link Game#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterCombat() {
		return new Action<Game>(game, game -> {
			game.enterState(game.states.combat.getID());
		});
	}

	/**
	 * Retrieves the {@link Action} that will change the state of the {@link Game}
	 * from {@link Game#combat} to {@link Game#movement}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> enterMovement() {
		return new Action<Game>(game, game -> {
			game.enterState(game.states.movement.getID());
		});
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
			game.enterState(game.states.reinforcement.getID());
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
			game.players.reinforce(game.players.getCurrent());
			game.enterState(game.states.reinforcement.getID());
		});
	}

	/**
	 * Retrieves the {@link Action} that will move one unit from one country to
	 * another as long as the countries are own by the same player.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> fortifyCountry() {
		return new Action<Game>(game, game -> {

			Country primary = game.states.movement.getHighlightedCountry();
			Country target = game.states.movement.getTargetCountry();

			// If there is two countries highlighted
			if (primary != null && target != null) {

				// If the army of the primary highlighted country is larger that 1 unit in size
				if (primary.getArmy().getSize() > 1) {

					// Holds the army of the primary country
					Army primaryArmy = primary.getArmy();

					// Holds the army of the target country
					Army targetArmy = target.getArmy();

					// Move the unit.
					targetArmy.setSize(targetArmy.getSize() + 1);
					primaryArmy.setSize(primaryArmy.getSize() - 1);

					if (primaryArmy.getSize() == 1) {
						game.states.movement.hideFortifyButton();
					}
				} else {
					// DO NOTHING
				}

			} else {
				// DO NOTHING
			}
		});
	}

	/**
	 * Retrieves the {@link Action} that will perform one round of the
	 * {@link WarMenu#fight(Country, Country, int)} on the two countries selected in
	 * the {@link Game#combat}.
	 * 
	 * @return {@link Action}
	 */
	private Action<?> excuteCombat() {
		return new Action<Game>(game, game -> {

			WarMenu warMenu = game.states.combat.warMenu;

			Country attacking = game.states.combat.getHighlightedCountry();
			Country defending = game.states.combat.getEnemyCountry();

			// If there is two countries highlighted
			if (attacking != null && defending != null) {

				Player attackingPlayer = attacking.getRuler();
				Player defendingPlayer = defending.getRuler();

				// If the army of the primary highlighted country is larger that 1 unit in size
				if (attacking.getArmy().getSize() > 1) {

					// Execute the combat
					warMenu.fight(attacking, defending, 1);

					// If the country has been conquered
					if (attacking.getRuler().equals(defending.getRuler())) {

						// If there is a defending player
						if (defendingPlayer != null) {

							defendingPlayer.setCountriesRuled(defendingPlayer.getCountriesRuled() - 1);

							// If the player has no countries they have lost.
							if (defendingPlayer.getCountriesRuled() == 0) {
								game.players.setLoser(defendingPlayer);
								game.checkWinner();
							}
						}

						attackingPlayer.setCountriesRuled(attackingPlayer.getCountriesRuled() + 1);

						game.states.combat.setPostCombat();
						game.states.combat.highlightCountry(defending);

						game.checkContinentRulership();

						game.players.checkChallenges(attackingPlayer);

					} else {

						// If the attacking army is not large enough to attack again.
						if (attacking.getArmy().getSize() == 1) {
							game.states.combat.hideAttackButton();
						}
					}
				} else {
					game.states.combat.hideAttackButton();
				}
			} else {
				game.states.combat.hideAttackButton();
			}

		});
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
			game.states.setup.unhighlightCountry(game.states.setup.getHighlightedCountry());
			game.states.setup.highlightCountry(null);

			// Assign the countries
			game.autoDistributeCountries();
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
		return new Action<Game>(game, game -> game.pauseMenu.save());
	}
}
