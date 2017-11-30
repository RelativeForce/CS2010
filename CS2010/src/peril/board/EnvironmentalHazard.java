package peril.board;

import java.util.Random;

import org.newdawn.slick.Image;

import peril.io.fileReaders.ImageReader;

/**
 * These may occur once a turn and will kill a random percentage of the army
 * stationed an {@link Continent}. Each type of EnvironmentalHazard has a
 * percentage chance that wit will occur in a given turn.
 * 
 * @author Joshua_Eddy
 *
 */
public enum EnvironmentalHazard {

	/**
	 * A VOLCANIC_ERUPTION has a 10% chance of occurring and may kill up to 20% of
	 * the units in an {@link Army}.
	 */
	VOLCANIC_ERUPTION(20, 10, "Volcanic Eruption") {

		/**
		 * The {@link Image} icon of an {@link EnvironmentalHazard#VOLCANIC_ERUPTION}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an
		 * {@link EnvironmentalHazard#VOLCANIC_ERUPTION}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link EnvironmentalHazard#VOLCANIC_ERUPTION} icon.
		 */
		@Override
		protected void initIcon(String uiPath) {
			icon = ImageReader.getImage(uiPath + "volcanoIcon.png");
		}

	},
	/**
	 * A TORNADO has a 12% chance of occurring and may kill up to 30% of the units
	 * in an {@link Army}.
	 */
	TORNADO(30, 12, "Tornado") {

		/**
		 * The {@link Image} icon of an {@link EnvironmentalHazard#TORNADO}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an {@link EnvironmentalHazard#TORNADO}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link EnvironmentalHazard#TORNADO} icon.
		 */
		@Override
		protected void initIcon(String uiPath) {
			icon = ImageReader.getImage(uiPath + "tornadoIcon.png");
		}
	},
	/**
	 * A HURRICANE has a 20% chance of occurring and may kill up to 10% of the units
	 * in an {@link Army}.
	 */
	HURRICANE(10, 20, "Hurricane") {
		/**
		 * The {@link Image} icon of an {@link EnvironmentalHazard#HURRICANE}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an {@link EnvironmentalHazard#HURRICANE}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link EnvironmentalHazard#HURRICANE} icon.
		 */
		@Override
		protected void initIcon(String uiPath) {
			icon = ImageReader.getImage(uiPath + "hurricaneIcon.png");
		}
	},
	/**
	 * A TSUNAMI has a 17% chance of occurring and may kill up to 40% of the units
	 * in an {@link Army}.
	 */
	TSUNAMI(40, 17, "Tsunami") {

		/**
		 * The {@link Image} icon of an {@link EnvironmentalHazard#TSUNAMI}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an {@link EnvironmentalHazard#TSUNAMI}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link EnvironmentalHazard#TSUNAMI} icon.
		 */
		@Override
		protected void initIcon(String uiPath) {
			icon = ImageReader.getImage(uiPath + "tsunamiIcon.png");
		}
	};

	/**
	 * The {@link Random} used to provide chance to {@link EnvironmentalHazard}s.
	 */
	private final Random generator;

	/**
	 * Holds the maximum percentage of a {@link Army} that this
	 * {@link EnvironmentalHazard} will kill.
	 */
	private final int maxCasualties;

	/**
	 * The <code>String</code> representation of the {@link EnvironmentalHazard}.
	 */
	private final String name;

	/**
	 * Holds the percentage chance that this {@link EnvironmentalHazard} will occur
	 * on a give {@link Army}.
	 */
	private final int chance;

	/**
	 * Constructs an {@link EnvironmentalHazard}.
	 * 
	 * @param maxCasualties
	 *            The maximum percentage of a {@link Amry} that this
	 *            {@link EnvironmentalHazard} will kill.
	 * @param chance
	 *            Percentage chance that wit will occur in a given turn.
	 * @param name
	 *            The <code>String</code> representation of the
	 *            {@link EnvironmentalHazard}.
	 */
	private EnvironmentalHazard(int maxCasualties, int chance, String name) {
		this.maxCasualties = maxCasualties;
		this.chance = chance;
		this.name = name;
		generator = new Random();
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
	 * @return <code>boolean</code> whether or not this {@link EnvironmentalHazard}
	 *         occurred on this army.
	 */
	public boolean act(Army army) {

		// Calculate whether this hazard will occur.
		boolean occur = chance > generator.nextInt(100);

		// If the environmental hazard occurs.
		if (occur) {

			// Holds the current size of the army.
			int currentSize = army.getSize();

			// Holds the max amount of units this hazard can kill
			int maxCasualties = (this.maxCasualties * currentSize) / 100;

			// Generate a random proportion of the army to kill.
			int casualties = maxCasualties == 0 ? 1 : generator.nextInt(maxCasualties - (maxCasualties / 4)) + 1;

			// Check whether the army will be below the minimum size.
			if (currentSize - casualties < 1) {

				// Set the army to the minimum size.
				army.setSize(1);
			} else {

				// Remove the dead regiments.
				army.setSize(currentSize - casualties);
			}

		}

		return occur;
	}

	/**
	 * Retrieves the {@link Image} icon that represents this
	 * {@link EnvironmentalHazard}.
	 * 
	 * @return <code>boolean</code>
	 */
	public abstract Image getIcon();

	/**
	 * Retrieves the {@link EnvironmentalHazard} using the specified name. If there
	 * is no {@link EnvironmentalHazard} with that name then this returns
	 * <code>null</code>.
	 * 
	 * @param name
	 *            Name of the {@link EnvironmentalHazard}
	 * @return {@link EnvironmentalHazard} specified by the parameter name.
	 */
	public static EnvironmentalHazard getByName(String name) {

		/*
		 * Iterate through all the hazards in the game and if the hazard specified by
		 * the parameter name is the same as one from the game set the hazard of the new
		 * continent as that. Otherwise there will be NO hazard in the new continent.
		 */
		for (EnvironmentalHazard indexHazard : EnvironmentalHazard.values()) {
			if (indexHazard.toString().equals(name)) {
				return indexHazard;
			}
		}

		return null;
	}

	/**
	 * Initialises all the {@link Image} icons of the {@link EnvironmentalHazard}s.
	 * 
	 * @param uiPath
	 *            The path to the folder with the icon image files in.
	 */
	public static void initIcons(String uiPath) {

		for (EnvironmentalHazard hazard : EnvironmentalHazard.values()) {
			hazard.initIcon(uiPath);
		}

	}

	/**
	 * Initialises the {@link Image} icon for an {@link EnvironmentalHazard}.
	 * 
	 * @param uiPath
	 *            The path to the folder with the icon image files in.
	 */
	protected abstract void initIcon(String uiPath);
}
