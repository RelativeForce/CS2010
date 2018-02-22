package peril.model.board;

import java.util.Random;

/**
 * These may occur once a turn and will kill a random percentage of the army
 * stationed an {@link ModelContinent}. Each type of hazard has a percentage
 * chance that wit will occur in a given turn.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-22
 * @version 1.01.02
 * 
 * @see ModelArmy
 *
 */
public enum ModelHazard {

	/**
	 * A VOLCANIC_ERUPTION has a 10% chance of occurring and may kill up to 20% of
	 * the units in an {@link ModelArmy}.
	 */
	VOLCANIC_ERUPTION(20, 10, "Volcanic Eruption"),
	/**
	 * A TORNADO has a 12% chance of occurring and may kill up to 30% of the units
	 * in an {@link ModelArmy}.
	 */
	TORNADO(30, 12, "Tornado"),
	/**
	 * A HURRICANE has a 20% chance of occurring and may kill up to 10% of the units
	 * in an {@link ModelArmy}.
	 */
	HURRICANE(10, 20, "Hurricane"),
	/**
	 * A TSUNAMI has a 17% chance of occurring and may kill up to 40% of the units
	 * in an {@link ModelArmy}.
	 */
	TSUNAMI(40, 17, "Tsunami");

	/**
	 * Holds the maximum percentage of a {@link ModelArmy} that this
	 * {@link ModelHazard} will kill.
	 */
	public final int maxCasualties;

	/**
	 * The <code>String</code> representation of the {@link ModelHazard}.
	 */
	public final String name;

	/**
	 * Holds the percentage chance that this {@link ModelHazard} will occur on a
	 * give {@link ModelArmy}.
	 */
	public final int chance;

	/**
	 * The {@link Random} used to provide chance to {@link ModelHazard}s.
	 */
	private final Random generator;

	/**
	 * Constructs an {@link ModelHazard}.
	 * 
	 * @param maxCasualties
	 *            The maximum percentage of a {@link ModelArmy} that this
	 *            {@link ModelHazard} will kill.
	 * @param chance
	 *            The percentage chance that wit will occur in a given turn.
	 * @param name
	 *            The <code>String</code> representation of the {@link ModelHazard}.
	 */
	private ModelHazard(int maxCasualties, int chance, String name) {
		this.maxCasualties = maxCasualties;
		this.chance = chance;
		this.name = name;
		this.generator = new Random();
	}

	/**
	 * Retrieves the {@link ModelHazard} using the specified name. If there is no
	 * {@link ModelHazard} with that name then this returns <code>null</code>.
	 * 
	 * @param name
	 *            Name of the {@link ModelHazard}
	 * @return {@link ModelHazard} specified by the parameter name.
	 */
	public static ModelHazard getByName(String name) {

		/*
		 * Iterate through all the hazards in the game and if the hazard specified by
		 * the parameter name is the same as one from the game set the hazard of the new
		 * continent as that. Otherwise there will be NO hazard in the new continent.
		 */
		for (ModelHazard indexHazard : ModelHazard.values()) {
			if (indexHazard.toString().equals(name)) {
				return indexHazard;
			}
		}

		return null;
	}

	/**
	 * Retrieves the <code>String</code> representation of the {@link ModelHazard}.
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Enacts the {@link ModelHazard} on an {@link ModelArmy}.
	 * 
	 * @param army
	 *            The {@link ModelArmy} that will be effected by the
	 *            {@link ModelHazard}.
	 * @return <code>boolean</code> whether or not this {@link ModelHazard} occurred
	 *         on this army.
	 */
	public boolean act(ModelArmy army) {

		// Calculate whether this hazard will occur.
		final boolean occur = chance > generator.nextInt(100);

		// If the environmental hazard occurs.
		if (occur) {

			// Holds the current size of the army.
			final int currentSize = army.getNumberOfUnits();

			// Holds the max amount of units this hazard can kill
			final int maxCasualties = (this.maxCasualties * currentSize) / 100;

			// Generate a random proportion of the army to kill.
			final int casualties = maxCasualties == 0 ? 1 : generator.nextInt(maxCasualties - (maxCasualties / 4)) + 1;

			// Check whether the army will be below the minimum size.
			if (currentSize - casualties <= 1) {

				// Set the army to the minimum size.
				army.setWeakest();
			} else {

				// Remove the dead regiments.
				for (int i = 0; i < casualties; i++) {
					army.removeRandomUnit();
				}

			}

		}

		return occur;
	}
}
