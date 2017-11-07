package peril;

import java.util.Arrays;
import java.util.Random;

import peril.board.Army;
import peril.board.Country;

/**
 * Encapsulates all the game combat logic.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public class CombatHandler {

	/**
	 * {@link Random} object for the random number generator
	 * 
	 */
	private Random random;

	/**
	 * Constructs a new {@link CombatHandler}.
	 * 
	 */
	public CombatHandler() {
		random = new Random();
	}

	/**
	 * Constructs a new {@link CombatHandler} with a SEED.
	 * 
	 * @param SEED
	 *            unique seed for the {@link Random} number generator
	 */
	public CombatHandler(long SEED) {
		random = new Random(SEED);
	}

	/**
	 * When a {@link Player} clicks on another {@link Country} to fight. This pits 2
	 * {@link Army}'s against each other. This emulates 1 turn of combat between 2
	 * {@link Player}s.
	 * 
	 * @param attacking
	 *            This is the {@link Country} that the {@link Player} uses to attack
	 *            a {@link Country}.
	 * @param defending
	 *            This is the {@link Country} that defend against the
	 *            {@link Player}'s attacking {@link Army}.
	 * @param atkSquadSize
	 *            Amount of units (dice) the attacking {@link Army} wants to pit
	 *            against the defending {@link Army}
	 */
	public void fight(Country attacking, Country defending, int atkSquadSize) {

		Army attackingArmy = attacking.getArmy();
		Army defendingArmy = defending.getArmy();

		// Check parameter
		if (atkSquadSize > 3 || atkSquadSize < 0) {
			throw new IllegalArgumentException(
					"The attacker cannot attact with more that 3 or less than 3 units at a time.");
		}

		// Get the dice rolls for the attackers and defenders.
		int[] attackerDiceRolls = getDiceRolls(atkSquadSize);
		int[] defenderDiceRolls = getDiceRolls(defendingArmy.getSize() > 1 ? 2 : 1);

		// Get the size of the smaller set of dice.
		int diceToCheck = attackerDiceRolls.length >= defenderDiceRolls.length ? defenderDiceRolls.length
				: attackerDiceRolls.length;

		// Compare each attacking dice roll against the defending dice roll
		for (int i = 0; i < diceToCheck; i++) {
			/*
			 * If the attackers dice is higher than the deffender's remove one unit from the
			 * defender's army and vice versa.
			 */
			if (attackerDiceRolls[i] > defenderDiceRolls[i]) {
				if (defendingArmy.getSize() == 1) {
					defending.setRuler(attacking.getRuler());
				}else {
					defendingArmy.setSize(defendingArmy.getSize() - 1);
					defending.getRuler().setTotalArmySize(defending.getRuler().getTotalArmySize() - 1);
				}

			} else {
				if(attackingArmy.getSize() == 1) {
					attacking.setRuler(defending.getRuler());
				}else {
					attackingArmy.setSize(attackingArmy.getSize() - 1);
					attacking.getRuler().setTotalArmySize(attacking.getRuler().getTotalArmySize() - 1);
				}
				
			}

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
	private int[] getDiceRolls(int numberOfRolls) {

		// Holds the dice roles.
		int[] rolls = new int[numberOfRolls];

		// initialise dice rolls for the attacking army
		for (int rollIndex = 0; rollIndex < numberOfRolls; rollIndex++) {
			rolls[rollIndex] = random.nextInt(6) + 1;
		}

		// Sort the dice roles into ascending order.
		Arrays.sort(rolls);

		return rolls;
	}

}
