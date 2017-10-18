package peril.board;

import java.util.Random;

import peril.ui.Viewable;
import peril.ui.VisualRepresenation;

/**
 * These may occur once a turn and will kill a random percentage of the army
 * stationed an {@link Country}. Each type of EnvironmentalHazard has a
 * percentage chance that wit will occur in a given turn.
 * 
 * @author Joshua_Eddy
 *
 */
public enum EnvironmentalHazard implements Viewable {

	/**
	 * A VOLCANIC_ERUPTION has a 10% chance of occurring and may kill up to 20% of
	 * the units in an {@link Army}.
	 */
	VOLCANIC_ERUPTION(20, 0.1, "Volcanic Eruption") {

		/**
		 * Holds the visual representation of the {@link VOLCANIC_ERUPTION}.
		 * 
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 */
		private final Visual visual = new Visual();

		/**
		 * Encapsulates the behaviours of displaying an {@link VOLCANIC_ERUPTION} on the
		 * {@link UserInterface}.
		 * 
		 * @author Joshua_Eddy
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 *
		 */
		final class Visual extends EnvironmentalHazardVisualRepresenation {

			// TODO Create visual representation for VOLCANIC_ERUPTION
		}

		@Override
		public VisualRepresenation getVisual() {
			return visual;
		}

	},
	/**
	 * A TORNADO has a 12% chance of occurring and may kill up to 30% of the units
	 * in an {@link Army}.
	 */
	TORNADO(30, 0.12, "Tornado") {

		/**
		 * Holds the visual representation of the {@link TORNADO}.
		 * 
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 */
		private final Visual visual = new Visual();

		/**
		 * Encapsulates the behaviours of displaying an {@link TORNADO} on the
		 * {@link UserInterface}.
		 * 
		 * @author Joshua_Eddy
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 *
		 */
		final class Visual extends EnvironmentalHazardVisualRepresenation {

			// TODO Create visual representation for TORNADO
		}

		@Override
		public VisualRepresenation getVisual() {
			return visual;
		}

	},
	/**
	 * A HURRICANE has a 20% chance of occurring and may kill up to 10% of the units
	 * in an {@link Army}.
	 */
	HURRICANE(10, 0.2, "Hurrincane") {

		/**
		 * Holds the visual representation of the {@link HURRICANE}.
		 * 
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 */
		private final Visual visual = new Visual();

		/**
		 * Encapsulates the behaviours of displaying an {@link HURRICANE} on the
		 * {@link UserInterface}.
		 * 
		 * @author Joshua_Eddy
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 *
		 */
		final class Visual extends EnvironmentalHazardVisualRepresenation {

			// TODO Create visual representation for HURRICANE
		}

		@Override
		public VisualRepresenation getVisual() {
			return visual;
		}
	},
	/**
	 * A TSUNAMI has a 17% chance of occurring and may kill up to 40% of the units
	 * in an {@link Army}.
	 */
	TSUNAMI(40, 0.17, "Tsunami") {

		/**
		 * Holds the visual representation of the {@link TSUNAMI}.
		 * 
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 */
		private final Visual visual = new Visual();

		/**
		 * Encapsulates the behaviours of displaying an {@link TSUNAMI} on the
		 * {@link UserInterface}.
		 * 
		 * @author Joshua_Eddy
		 * @see VisualRepresenation
		 * @see EnvironmentalHazardVisualRepresenation
		 *
		 */
		final class Visual extends EnvironmentalHazardVisualRepresenation {

			// TODO Create visual representation for TSUNAMI
		}

		@Override
		public VisualRepresenation getVisual() {
			return visual;
		}
	};

	/**
	 * The seed used by the {@link GENERATOR}.
	 * 
	 * @see Random
	 */
	private final int SEED = 5;

	/**
	 * The {@link Random} used to provide chance to {@link EnvironmentalHazard}s.
	 */
	private final Random GENERATOR = new Random(SEED);

	/**
	 * Holds the maximum percentage of the army stationed an {@link Country} that
	 * this {@link EnvironmentalHazard} will kill.
	 */
	private final int maxCasualties;

	/**
	 * The <code>String</code> representation of the {@link EnvironmentalHazard}.
	 */
	private final String name;

	/**
	 * Holds the percentage chance that wit will occur in a given turn.
	 */
	private final double chance;

	/**
	 * Constructs an {@link EnvironmentalHazard}.
	 * 
	 * @param maxCasualties
	 *            The maximum percentage of the army stationed an {@link Country}
	 *            that this {@link EnvironmentalHazard} will kill.
	 * @param chance
	 *            Percentage chance that wit will occur in a given turn.
	 * @param name
	 *            The <code>String</code> representation of the
	 *            {@link EnvironmentalHazard}.
	 */
	private EnvironmentalHazard(int maxCasualties, double chance, String name) {
		this.maxCasualties = maxCasualties;
		this.chance = chance;
		this.name = name;
	}

	/**
	 * Retrieves the <code>String</code> representation of the
	 * {@link EnvironmentalHazard}.
	 */
	@Override
	public String toString() {
		return name;
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

		boolean occur = (chance * 100) < GENERATOR.nextInt(100);

		// If the environmental hazard occurs.
		if (occur) {

			int currentSize = army.getSize();

			// Holds the minimum casualties the hazard must cause. A quarter of
			// the maximum
			int minCasualties = maxCasualties / 4;

			// Generate a random proportion of the army to kill.
			int casualties = (currentSize * (minCasualties + GENERATOR.nextInt(maxCasualties - minCasualties))) / 100;

			// Remove the dead regiments.
			army.setSize(currentSize - casualties);

		}

	}

	@Override
	public abstract VisualRepresenation getVisual();

	/**
	 * Encapsulates the behaviours of displaying an {@link EnvironmentalHazard} on
	 * the {@link UserInterface}.
	 * 
	 * @author Joshua_Eddy
	 * @see VisualRepresenation
	 */
	private abstract class EnvironmentalHazardVisualRepresenation extends VisualRepresenation {

		// TODO Create visual representation for EnvironmentalHazard
	}

}
