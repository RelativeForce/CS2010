package peril.board;

import java.util.Random;

/**
 * These may occur once a turn and will kill a random percentage of the army
 * stationed an {@link Country}. Each type of EnvironmentalHazard has a
 * percentage chance that wit will occur in a given turn.
 * 
 * @author Joshua_Eddy
 *
 */
public enum EnvironmentalHazard {

	VOLCANIC(20, 0.1), TORNADO(30, 0.6), HURICANE(10, 0.2), TSUNAMI(40, 0.2);

	/**
	 * The seed used by the {@link GENERATOR}.
	 * 
	 * @see Random
	 */
	private final int SEED = 5;

	/**
	 * The {@link Random} used to provide chance to
	 * {@link EnvironmentalHazard}s.
	 */
	private final Random GENERATOR = new Random(SEED);

	/**
	 * Holds the maximum percentage of the army stationed an {@link Country}
	 * that this {@link EnvironmentalHazard} will kill.
	 */
	private final int maxCausualties;

	/**
	 * Holds the percentage chance that wit will occur in a given turn.
	 */
	private final double chance;

	/**
	 * Constructs an {@link EnvironmentalHazard}.
	 * 
	 * @param maxCausualties
	 *            The maximum percentage of the army stationed an
	 *            {@link Country} that this {@link EnvironmentalHazard} will
	 *            kill.
	 * @param chance
	 *            Percentage chance that wit will occur in a given turn.
	 */
	private EnvironmentalHazard(int maxCausualties, double chance) {
		this.maxCausualties = maxCausualties;
		this.chance = chance;
	}

	/**
	 * Enacts the {@link EnvironmentalHazard} on an {@link Army}.
	 * 
	 * @param army
	 *            The {@link Army} that will be effected by the
	 *            {@link EnvironmentalHazard}.
	 */
	public void act(Army army) {
		// Kill a percentage of the army.

	}

}
