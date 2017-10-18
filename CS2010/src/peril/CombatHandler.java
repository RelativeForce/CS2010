package peril;

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
	 * When a {@link Player} clicks on another {@link Country} to fight. This pits 2
	 * {@link Army}'s against each other. This emulates 1 turn of combat between 2 players.
	 * 
	 * @param armyAtk
	 *            This is the {@link Army} that the {@link Player} uses to attack a
	 *            {@link Country}.
	 * @param armydef
	 *            This is the {@link Army} that defend against the {@link Player}'s
	 *            attacking {@link Army}.
	 */
	public void fight(Army armyAtk, Army armydef) {

	}

}
