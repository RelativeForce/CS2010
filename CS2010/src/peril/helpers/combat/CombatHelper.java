package peril.helpers.combat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import peril.helpers.UnitHelper;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;

public class CombatHelper {

	public final static int MAX_ATTACK_SQUAD_SIZE = 3;

	public final static int MAX_DEFEND_SQUAD_SIZE = 2;

	/**
	 * {@link Random} object for the random number generator.
	 * 
	 */
	private final Random random;

	private Integer[] attackerDiceRolls;

	private Integer[] defenderDiceRolls;
	
	private CombatRound round;

	public CombatHelper() {
		this.random = new Random();
	}

	public void fight(CombatRound round) {

		this.round = round;
		
		final int attackSquadSize = round.attackerSquad.getAliveUnits();
		if (attackSquadSize > MAX_ATTACK_SQUAD_SIZE || attackSquadSize == 0) {
			throw new IllegalArgumentException(attackSquadSize + " is not a valid attacking squad size.");
		}

		final int defendSquadSize = round.defenderSquad.getAliveUnits();
		if (defendSquadSize > MAX_DEFEND_SQUAD_SIZE || defendSquadSize == 0) {
			throw new IllegalArgumentException(defendSquadSize + " is not a valid defending squad size.");
		}

		// Get the dice rolls for the attackers and defenders.
		final Integer[] attackerDiceRolls = getDiceRolls(attackSquadSize);

		final Integer[] defenderDiceRolls = getDiceRolls(defendSquadSize);

		// Compare the dice that were rolled.
		compareDiceRolls(attackerDiceRolls, defenderDiceRolls, round);

	}

	private void compareDiceRolls(Integer[] attackerDiceRolls, Integer[] defenderDiceRolls, CombatRound round) {

		// Get the size of the smaller set of dice.
		final int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
				: attackerDiceRolls.length;

		// Copy the squads to these holding lists so the squads can be modified.
		final List<ModelSquadMember> attackingSqd = round.attackerSquad.stream().collect(Collectors.toList());
		final List<ModelSquadMember> defendingSqd = round.defenderSquad.stream().collect(Collectors.toList());

		// The index of the current set of current units.
		int unitIndex = 0;

		// Holds whether the combat is in a valid state to continue. For example combat
		// cannot continue if the defender has no units.
		boolean finished = false;

		// Compare each attacking dice roll against the defending dice roll
		while (unitIndex < diceToCheck && !finished) {

			// The units that will fight
			final ModelUnit attackingUnit = attackingSqd.get(unitIndex).unit;
			final ModelUnit defendingUnit = defendingSqd.get(unitIndex).unit;

			// Attacker won
			if (attackerDiceRolls[unitIndex] > defenderDiceRolls[unitIndex]) {
				finished = attackerWon(round, attackingUnit);
			}
			// Attacker lost
			else {
				finished = attackerLost(round, defendingUnit);
			}

			unitIndex++;
		}
	}

	private boolean attackerWon(CombatRound round, ModelUnit attackingUnit) {

		final ModelPlayer defender = round.defending.getRuler();
		final ModelPlayer attacker = round.attacking.getRuler();
		final ModelArmy defendingArmy = round.defending.getArmy();
		final int totalStrength = defendingArmy.getStrength() + round.defenderSquad.geStrength();

		// If the army of the defending country is of size on then this victory will
		// conquer the country. Otherwise just kill unit from the defending army.
		if (attackingUnit.strength >= totalStrength) {
			resetArmyToWeakest(round.defenderSquad, defendingArmy, defender, attackingUnit);

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

	private boolean attackerLost(CombatRound round, ModelUnit defendingUnit) {

		final ModelPlayer attacker = round.attacking.getRuler();
		final ModelArmy attackingArmy = round.attacking.getArmy();
		final int totalStrength = attackingArmy.getStrength() + round.attackerSquad.geStrength();

		if (defendingUnit.strength >= totalStrength) {
			resetArmyToWeakest(round.attackerSquad, attackingArmy, attacker, defendingUnit);
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

	private void resetArmyToWeakest(ModelSquad squad, ModelArmy army, ModelPlayer player, ModelUnit enemyUnit) {
		final ModelUnit weakestUnit = UnitHelper.getInstance().getWeakest();

		squad.returnSquadToArmy(army);

		final int toRemove = enemyUnit.strength - weakestUnit.strength;

		if (player != null) {
			player.totalArmy.remove(toRemove);
		}

		army.setWeakest();

	}

	private void removeUnitFromArmy(ModelSquad squad, ModelArmy army, ModelPlayer player, ModelUnit enemyUnit) {

		boolean removedFromSquad = squad.killUnit(enemyUnit);

		// If the enemy unti's damage was not taken from the squad return the squad to
		// the army and then remove the damage from the army.
		if (!removedFromSquad) {
			squad.returnSquadToArmy(army);
			army.remove(enemyUnit);
		}

		if (player != null) {
			player.totalArmy.remove(enemyUnit);
		}

	}

	/**
	 * Gets an <code>int[]</code> of {@link Random#nextInt(int)} with bounds of 1 -
	 * 6
	 * 
	 * @param numberOfRolls
	 *            <code>int</code> number of rolls
	 * @return <code>int[]</code>
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
