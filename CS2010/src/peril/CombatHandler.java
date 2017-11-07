package peril;

import java.util.Arrays;
import java.util.Random;

import peril.board.Army;
import peril.board.Country;

/**
 * Encapsulates all the game combat logic.
 * 
 * @author Joshua_Eddy
 * @author Ezekiel_Trinidad
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
	 * @param armyAtk
	 *            This is the {@link Army} that the {@link Player} uses to attack a
	 *            {@link Country}.
	 * @param armyDef
	 *            This is the {@link Army} that defend against the {@link Player}'s
	 *            attacking {@link Army}.
	 * @param atkSquadSize
	 *            Amount of units (dice) the attacking {@link Army} wants to pit
	 *            against the defending {@link Army}
	 */
	public void fight(Army armyAtk, Army armyDef, int atkSquadSize) {

		int[] atkDiceRolls = new int[atkSquadSize];
		int[] defDiceRolls = new int[2]; // maximum of 2 defending armies for the defending player

		// initialise dice rolls for the attacking army
		for (int i = 0; i < atkSquadSize; i++) {
			int atkDiceRoll = random.nextInt(6);
			atkDiceRolls[i] = atkDiceRoll;
		}
		Arrays.sort(atkDiceRolls); // sort array into ascending order (can't sort it as descending order for some
									// reason)
		int[] sortedAtkDiceRolls = new int[atkSquadSize];
		int counterAtkLoop = 0;

		// changes the order from ascending to descending
		for (int i = atkDiceRolls.length - 1; i >= 0; i--) // loops starting from the end of the new sorted array
		{
			sortedAtkDiceRolls[counterAtkLoop] = atkDiceRolls[i];
			counterAtkLoop++;
		}

		// initialise dice rolls for the defending army
		if (armyDef.getSize() > 1) {
			defDiceRolls[0] = random.nextInt();
		} else {
			defDiceRolls[0] = random.nextInt();
			defDiceRolls[1] = random.nextInt();
		}

		int[] sortedDefDiceRolls = new int[2];
		int counterDefLoop = 0;
		for (int i = defDiceRolls.length - 1; i >= 0; i--) {
			sortedDefDiceRolls[counterDefLoop] = defDiceRolls[i];
			counterDefLoop++;
		}

		// compares each attacking dice roll against the defending dice roll
		for (int i = 0; i < atkDiceRolls.length - 1; i++) {
			if (sortedAtkDiceRolls[i] <= sortedDefDiceRolls[i]) {
				int woundedArmy = armyAtk.getSize() - 1;
				armyAtk.setSize(woundedArmy);
			}

			else {
				int woundedArmy = armyDef.getSize() - 1;
				armyDef.setSize(woundedArmy);
			}
		}

	}

}
