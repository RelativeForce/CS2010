package peril.io;

import java.io.File;

import org.newdawn.slick.Image;

import peril.CombatHandler;
import peril.Player;
import peril.Point;
import peril.board.Army;
import peril.board.Country;
import peril.multiThread.Action;
import peril.ui.Button;
import peril.ui.states.InteractiveState;
import peril.ui.states.gameStates.CoreGameState;
import peril.ui.states.gameStates.multiSelectState.CombatState;
import peril.ui.states.gameStates.multiSelectState.MovementState;
import peril.ui.visual.Clickable;
import peril.ui.visual.Viewable;
public abstract class FunctionHandler {
	
	private Action<?> getAction0()
	{
		return new Action<CoreGameState>((CoreGameState) state, actionState -> {

			// Holds the currently highlighted country
			Country highlightedCountry = actionState.getHighlightedCountry();

			// Holds the current player.
			Player player = actionState.getGame().getCurrentPlayer();

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
						actionState.getGame().checkChallenges(actionState.getGame().getCurrentPlayer());

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
	private Action<?> getAction1()
	{
		if (!(state instanceof CoreGameState)) {
			throw new IllegalStateException("Function code: 1 is not permitted with the '" + state.getStateName()
					+ "' state. It is only permitted with 'CoreGame' states.");
		}

		return new Action<CoreGameState>((CoreGameState) state, actionState -> {

			actionState.unhighlightCountry(actionState.getHighlightedCountry());
			actionState.highlightCountry(null);
			actionState.getGame().enterState(actionState.getGame().combat.getID());
		});
	}
	private Action<?> getAction2()
	{
		if (!(state instanceof CoreGameState)) {
			throw new IllegalStateException("Function code: 2 is not permitted with the '" + state.getStateName()
					+ "' state. It is only permitted with 'CoreGame' states.");
		}

		return new Action<CoreGameState>((CoreGameState) state, actionState -> {

			actionState.unhighlightCountry(actionState.getHighlightedCountry());
			actionState.highlightCountry(null);
			actionState.getGame().enterState(actionState.getGame().movement.getID());
		});
	}
	private Action<?> getAction3()
	{
		if (!(state instanceof CoreGameState)) {
			throw new IllegalStateException("Function code: 3 is not permitted with the '" + state.getStateName()
					+ "' state. It is only permitted with 'CoreGame' states.");
		}

		return new Action<CoreGameState>((CoreGameState) state, actionState -> {

			actionState.unhighlightCountry(actionState.getHighlightedCountry());
			actionState.highlightCountry(null);
			actionState.getGame().enterState(actionState.getGame().reinforcement.getID());
			actionState.getGame().nextPlayer();
		});
	}
	private Action<?> getAction4()
	{
		if (!(state instanceof CoreGameState)) {
			throw new IllegalStateException("Function code: 0 is not permitted with the '" + state.getStateName()
					+ "' state. It is only permitted with 'CoreGame' states.");
		}

		return new Action<CoreGameState>((CoreGameState) state, actionState -> {

			actionState.unhighlightCountry(actionState.getHighlightedCountry());
			actionState.highlightCountry(null);

			// For every continent on the board.
			actionState.getGame().getBoard().getContinents().forEach(continent -> {

				// If the continents is ruled by one player add on to the players ruled
				// continents
				if (continent.isRuled()) {
					continent.getRuler().setContinentsRuled(continent.getRuler().getContinentsRuled() + 1);
				}

				// For every country in the continent
				continent.getCountries().forEach(country -> {

					// If the country has a ruler
					if (country.getRuler() != null) {

						// Increment the number of countries that player rules.
						country.getRuler().setCountriesRuled(country.getRuler().getCountriesRuled() + 1);

						// Add the size of the countries army to the total size of the players army.
						country.getRuler().setTotalArmySize(
								country.getRuler().getTotalArmySize() + country.getArmy().getSize());

					}
				});
			});

			// Check the challenges of the first player.
			actionState.getGame().enterState(actionState.getGame().reinforcement.getID());
		});
	}
	private Action<?> getAction5()
	{
		if (!(state instanceof MovementState)) {
			throw new IllegalStateException("Function code: 5 is not permitted with the '" + state.getStateName()
					+ "' state. It is only permitted with 'Movement' state.");
		}

		return new Action<MovementState>((MovementState) state, actionState -> {

			Country primary = actionState.getHighlightedCountry();
			Country target = actionState.getTargetCountry();

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
	private Action<?> getAction6()
	{
		if (!(state instanceof CombatState)) {
			throw new IllegalStateException("Function code: 6 is not permitted with the '"
					+ state.getStateName() + "' state. It is only permitted with 'Combat' state.");
		}
		
		return new Action<CombatState>((CombatState)state, actionState -> {

			CombatHandler combathandler = actionState.getGame().getCombatHandler();

			Country attacking = actionState.getHighlightedCountry();
			Country defending = actionState.getEnemyCountry();

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
							defendingPlayer.setTotalArmySize(defendingPlayer.getTotalArmySize() - 1);
						}

						attackingPlayer.setCountriesRuled(attackingPlayer.getCountriesRuled() + 1);
						attackingPlayer.setTotalArmySize(attackingPlayer.getTotalArmySize() + 1);

						actionState.setPostCombat();
						actionState.highlightCountry(defending);
						actionState.getGame().checkChallenges(actionState.getGame().getCurrentPlayer());
					}
				}
			}
		});
	}
	public Action<?> getActionByCode(int code, InteractiveState state) {
		switch (code) {

		case 0:

			// TODO change implementation to remove instances of casting.
			return getAction0();

		

		// Enter combat state.
		case 1:

			// TODO change implementation to remove instances of casting.
			return getAction1();
			
		// Enter movement state.
		case 2:

			// TODO change implementation to remove instances of casting.
			return getAction2();

		// Enter reinforcement state.
		case 3:

			// TODO change implementation to remove instances of casting.
			return getAction3();
			
		// Leave set up state
		case 4:

			// TODO change implementation to remove instances of casting.
			return getAction4();
			
		// Fortify another country by moving one troop to the new country.
		case 5:

			// TODO change implementation to remove instances of casting.
			return getAction5();
			
		// Execute a combat turn.
		case 6:
			
			// TODO change implementation to remove instances of casting.
			return getAction6();

		}
		return null;
	}
	
}
