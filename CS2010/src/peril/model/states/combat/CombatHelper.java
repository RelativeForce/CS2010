package peril.model.states.combat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.stream.Collectors;

import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * Helps the game by performing a round of combat denoted by a
 * {@link CombatRound} passed to {@link #fight(CombatRound)}. The results of
 * this round of combat can are {@link #view}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-22
 * @version 1.01.01
 *
 * @see Observable
 * @see CombatRound
 * @see CombatView
 * @see ModelSquad
 * @see ModelSquadMember
 */
public final class CombatHelper extends Observable {

	/**
	 * The maximum size of an attacking squad.
	 */
	public final static int MAX_ATTACK_SQUAD_SIZE = 3;

	/**
	 * The maximum size of an defending squad.
	 */
	public final static int MAX_DEFEND_SQUAD_SIZE = 2;

	/**
	 * The {@link Random} that generated the dice rolls.
	 */
	private final Random random;

	/**
	 * The {@link CombatView} that denotes the results of the previous round of
	 * combat.
	 */
	public CombatView view;

	/**
	 * Constructs an new {@link CombatHelper}.
	 */
	public CombatHelper() {
		this.random = new Random();
	}

	/**
	 * Performs a round of combat between the {@link ModelCountry}s and
	 * {@link ModelSquad}s defined by the parameter {@link CombatRound}. The results
	 * of this {@link CombatRound} will be stored in {@link #view} until the
	 * {@link CombatHelper} is {@link #clear()} or the next
	 * {@link #fight(CombatRound)}.
	 * 
	 * @param round
	 *            The {@link CombatRound} specifying the details of this fight.
	 */
	public void fight(CombatRound round) {

		// Check the attacking squad.
		final int attackSquadSize = round.attackerSquad.getAliveUnits();
		if (attackSquadSize > MAX_ATTACK_SQUAD_SIZE || attackSquadSize == 0) {
			throw new IllegalArgumentException(attackSquadSize + " is not a valid attacking squad size.");
		}

		// Check the defending squad.
		final int defendSquadSize = round.defenderSquad.getAliveUnits();
		if (defendSquadSize > MAX_DEFEND_SQUAD_SIZE || defendSquadSize == 0) {
			throw new IllegalArgumentException(defendSquadSize + " is not a valid defending squad size.");
		}

		// Get the dice rolls for the attackers and defenders.
		final Integer[] attackerDiceRolls = getDiceRolls(attackSquadSize);
		final Integer[] defenderDiceRolls = getDiceRolls(defendSquadSize);

		// Compare the dice that were rolled.
		compareDiceRolls(round, attackerDiceRolls, defenderDiceRolls);

		// Update the view
		this.view = new CombatView(round, attackerDiceRolls, defenderDiceRolls);

		setChanged();
		notifyObservers(this.view);

	}

	/**
	 * Clears this {@link CombatHelper}.
	 */
	public void clear() {
		this.view = null;
	}

	/**
	 * Compares the specified dice rolls and performs the combat on the elements of
	 * the specified {@link CombatRound}.
	 * 
	 * @param round
	 *            The {@link CombatRound} specifying the details of this fight.
	 * @param attackerDiceRolls
	 *            The dice rolls of the attacker.
	 * @param defenderDiceRolls
	 *            The dice rolls of the defender.
	 */
	private void compareDiceRolls(CombatRound round, Integer[] attackerDiceRolls, Integer[] defenderDiceRolls) {

		// Get the size of the smaller set of dice.
		final int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
				: attackerDiceRolls.length;

		// Copy the squads to these holding lists so the squads can be modified.
		final List<ModelSquadMember> attackingSqd = round.attackerSquad.stream().collect(Collectors.toList());
		final List<ModelSquadMember> defendingSqd = round.defenderSquad.stream().collect(Collectors.toList());

		// The index of the current units that will fight.
		int index = 0;

		// Holds whether the combat is in a valid state to continue. For example combat
		// cannot continue if the defender has no units.
		boolean finished = false;

		// Compare each attacking dice roll against the defending dice roll unless
		// finished is flagged.
		while (index < diceToCheck && !finished) {

			// The units that will fight
			final ModelUnit attackingUnit = attackingSqd.get(index).unit;
			final ModelUnit defendingUnit = defendingSqd.get(index).unit;

			// Attacker won
			if (attackerDiceRolls[index] > defenderDiceRolls[index]) {
				finished = attackerWon(round, attackingUnit);
			}
			// Attacker lost
			else {
				finished = attackerLost(round, defendingUnit);
			}

			index++;
		}
	}

	/**
	 * Processes a fight where the attacker won and the strength of the specified
	 * attacking {@link ModelUnit} is dealt as damage to the defenders
	 * {@link ModelArmy} and {@link ModelSquad}.
	 * 
	 * @param round
	 *            The {@link CombatRound} specifying the details of this fight.
	 * @param attackingUnit
	 *            The unit that will damage the defenders {@link ModelArmy} and
	 *            {@link ModelSquad}.
	 * @return Whether this {@link ModelUnit}'s damage has caused the round to end.
	 */
	private boolean attackerWon(CombatRound round, ModelUnit attackingUnit) {

		final ModelPlayer defender = round.defending.getRuler();
		final ModelPlayer attacker = round.attacking.getRuler();
		final ModelArmy defendingArmy = round.defending.getArmy();
		final int totalStrength = defendingArmy.getStrength() + round.defenderSquad.geStrength();

		// If the army of the defending country is of size on then this victory will
		// conquer the country. Otherwise just kill unit from the defending army.
		if (attackingUnit.strength >= totalStrength) {
			resetArmyToWeakest(round.defenderSquad, defendingArmy, defender);

			round.defending.setRuler(attacker);
			return true;

		} else {
			removeUnitFromArmy(round.defenderSquad, defendingArmy, defender, attackingUnit);

			// If the defending army was cleared.
			if (defendingArmy.getNumberOfUnits() == 0) {
				defendingArmy.setWeakest();
				round.defending.setRuler(attacker);
				return true;
			}

			return false;
		}
	}

	/**
	 * Processes a fight where the defender won and the strength of the specified
	 * defending {@link ModelUnit} is dealt as damage to the attackers
	 * {@link ModelArmy} and {@link ModelSquad}.
	 * 
	 * @param round
	 *            The {@link CombatRound} specifying the details of this fight.
	 * @param defendingUnit
	 *            The unit that will damage the attacker {@link ModelArmy} and
	 *            {@link ModelSquad}.
	 * @return Whether this {@link ModelUnit}'s damage has caused the round to end.
	 */
	private boolean attackerLost(CombatRound round, ModelUnit defendingUnit) {

		final ModelPlayer attacker = round.attacking.getRuler();
		final ModelArmy attackingArmy = round.attacking.getArmy();
		final int totalStrength = attackingArmy.getStrength() + round.attackerSquad.geStrength();

		if (defendingUnit.strength >= totalStrength) {
			resetArmyToWeakest(round.attackerSquad, attackingArmy, attacker);
			return true;
		} else {
			removeUnitFromArmy(round.attackerSquad, attackingArmy, attacker, defendingUnit);

			// If the attacking army was cleared.
			if (attackingArmy.getNumberOfUnits() == 1) {
				attackingArmy.setWeakest();
				return true;
			}

			return false;
		}
	}

	/**
	 * Returns a specified {@link ModelArmy} to its weakest state while also
	 * emptying the specified {@link ModelSquad} and removing the appropriate
	 * strength from the specified {@link ModelPlayer}'s total {@link ModelArmy}.
	 * 
	 * @param squad
	 *            The {@link ModelSquad} that will be returned to the specified
	 *            {@link ModelArmy}.
	 * @param army
	 *            The {@link ModelArmy} that will be reset to its weakest (non
	 *            empty) state.
	 * @param player
	 *            The {@link ModelPlayer} that owns the specified {@link ModelArmy}.
	 */
	private void resetArmyToWeakest(ModelSquad squad, ModelArmy army, ModelPlayer player) {

		// The weakest unit in the game.
		final ModelUnit weakestUnit = UnitHelper.getInstance().getWeakest();

		squad.returnSquadToArmy(army);

		// The difference between the current strength of the army and the new strength.
		final int toRemove = army.getStrength() - weakestUnit.strength;

		// If the player is not null then remove the difference from their total army.
		if (player != null) {
			player.totalArmy.remove(toRemove);
		}

		army.setWeakest();

	}

	/**
	 * First attempts to remove the specified enemy {@link ModelUnit} from the
	 * specified {@link ModelSquad} and if that is not possible if returns the
	 * {@link ModelSquadMember}s back to the specified {@link ModelArmy} then remove
	 * the enemy {@link ModelUnit} from the {@link ModelArmy}.
	 * 
	 * @param squad
	 *            The {@link ModelSquad} that will be returned to the specified
	 *            {@link ModelArmy}.
	 * @param army
	 *            The {@link ModelArmy} that the {@link ModelSquad} will be returned
	 *            to if the {@link ModelUnit} is not removed from it.
	 * @param player
	 *            The {@link ModelPlayer} that owns the {@link ModelArmy}.
	 * @param enemyUnit
	 *            The {@link ModelUnit} that will be removed from the
	 *            {@link ModelSquad} or {@link ModelArmy}.
	 */
	private void removeUnitFromArmy(ModelSquad squad, ModelArmy army, ModelPlayer player, ModelUnit enemyUnit) {

		final boolean removedFromSquad = squad.killUnit(enemyUnit);

		// If the enemy unti's damage was not taken from the squad return the squad to
		// the army and then remove the damage from the army.
		if (!removedFromSquad) {
			squad.returnSquadToArmy(army);
			army.remove(enemyUnit);
		}

		// Remove the unit from the player total army.
		if (player != null) {
			player.totalArmy.remove(enemyUnit);
		}

	}

	/**
	 * Retrieves a {@link Integer}[] of random six sided dice rolls.
	 * 
	 * @param numberOfRolls
	 *            The {@link Integer}[] number of rolls
	 * @return The {@link Integer}[] dice rolls.
	 */
	private Integer[] getDiceRolls(int numberOfRolls) {

		final int maxDice = 6;

		// Holds the dice roles.
		final Integer[] rolls = new Integer[numberOfRolls];

		// Initialise dice rolls for the attacking army
		for (int rollIndex = 0; rollIndex < numberOfRolls; rollIndex++) {
			rolls[rollIndex] = random.nextInt(maxDice) + 1;
		}

		// Sort the dice roles into descending order.
		Arrays.sort(rolls, Collections.reverseOrder());

		return rolls;
	}

}
