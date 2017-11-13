package peril.io;

import peril.CombatHandler;
import peril.Game;
import peril.Player;
import peril.board.Army;
import peril.board.Country;
import peril.multiThread.Action;
import peril.ui.Button;

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

		}
		return null;
	}

	private Action<?> reinforceCountry() {
		return new Action<Game>(game, game -> {

			// Holds the currently highlighted country
			Country highlightedCountry = game.reinforcement.getHighlightedCountry();

			// Holds the current player.
			Player player = game.getCurrentPlayer();

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
						game.checkChallenges(player);

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

	private Action<?> enterCombat() {
		return new Action<Game>(game, game -> {
			game.reinforcement.unhighlightCountry(game.reinforcement.getHighlightedCountry());
			game.reinforcement.highlightCountry(null);
			game.enterState(game.combat.getID());
		});
	}

	private Action<?> enterMovement() {
		return new Action<Game>(game, game -> {
			game.movement.unhighlightCountry(game.movement.getHighlightedCountry());
			game.movement.highlightCountry(null);
			game.enterState(game.reinforcement.getID());
			game.nextPlayer();
		});
	}

	private Action<?> enterReinforment() {
		return new Action<Game>(game, game -> {
			game.movement.unhighlightCountry(game.movement.getHighlightedCountry());
			game.movement.highlightCountry(null);
			game.enterState(game.reinforcement.getID());
			game.nextPlayer();
		});
	}

	private Action<?> leaveSetUp() {
		return new Action<Game>(game, game -> {

			game.setup.unhighlightCountry(game.setup.getHighlightedCountry());
			game.setup.highlightCountry(null);

			game.checkContinentRulership();

			game.reinforce(game.getCurrentPlayer());
			game.enterState(game.reinforcement.getID());
		});
	}

	private Action<?> fortifyCountry() {
		return new Action<Game>(game, game -> {

			Country primary = game.movement.getHighlightedCountry();
			Country target = game.movement.getTargetCountry();

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

				} else {
					// DO NOTHING
				}

			} else {
				// DO NOTHING
			}
		});
	}

	private Action<?> excuteCombat() {
		return new Action<Game>(game, game -> {

			CombatHandler combathandler = game.getCombatHandler();

			Country attacking = game.combat.getHighlightedCountry();
			Country defending = game.combat.getEnemyCountry();

			// If there is two countries highlighted
			if (attacking != null && defending != null) {

				Player attackingPlayer = attacking.getRuler();
				Player defendingPlayer = defending.getRuler();

				// If the army of the primary highlighted country is larger that 1 unit in size
				if (attacking.getArmy().getSize() > 1) {

					// Execute the combat
					combathandler.fight(attacking, defending, 1);

					// If the country has been conquered
					if (attacking.getRuler().equals(defending.getRuler())) {

						if (defendingPlayer != null) {

							defendingPlayer.setCountriesRuled(defendingPlayer.getCountriesRuled() - 1);

							if (defendingPlayer.getCountriesRuled() == 0) {
								game.setLoser(defendingPlayer);
								game.checkWinner();
							}
						}

						attackingPlayer.setCountriesRuled(attackingPlayer.getCountriesRuled() + 1);

						game.combat.setPostCombat();
						game.combat.highlightCountry(defending);

						game.checkContinentRulership();

						game.checkChallenges(attackingPlayer);

					}
				}
			}
		});
	}

	private Action<?> playGame() {
		return new Action<Game>(game, game -> {
			try {
				game.mainMenu.loadMap();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
