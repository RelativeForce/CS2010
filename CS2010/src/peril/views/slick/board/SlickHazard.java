package peril.views.slick.board;

import org.newdawn.slick.Image;
import peril.model.board.ModelHazard;
import peril.io.fileReaders.ImageReader;

/**
 * These may occur once a turn and will kill a random percentage of the army
 * stationed an {@link SlickContinent}. Each type of EnvironmentalHazard has a
 * percentage chance that wit will occur in a given turn.
 * 
 * @author Joshua_Eddy
 *
 */
public enum SlickHazard {

	/**
	 * A VOLCANIC_ERUPTION has a 10% chance of occurring and may kill up to 20% of
	 * the units in an {@link Army}.
	 */
	VOLCANIC_ERUPTION(ModelHazard.VOLCANIC_ERUPTION) {

		/**
		 * The {@link Image} icon of an {@link SlickHazard#VOLCANIC_ERUPTION}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an
		 * {@link SlickHazard#VOLCANIC_ERUPTION}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link SlickHazard#VOLCANIC_ERUPTION} icon.
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
	TORNADO(ModelHazard.TORNADO) {

		/**
		 * The {@link Image} icon of an {@link SlickHazard#TORNADO}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an {@link SlickHazard#TORNADO}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link SlickHazard#TORNADO} icon.
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
	HURRICANE(ModelHazard.HURRICANE) {
		/**
		 * The {@link Image} icon of an {@link SlickHazard#HURRICANE}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an {@link SlickHazard#HURRICANE}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link SlickHazard#HURRICANE} icon.
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
	TSUNAMI(ModelHazard.TSUNAMI) {

		/**
		 * The {@link Image} icon of an {@link SlickHazard#TSUNAMI}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an {@link SlickHazard#TSUNAMI}.
		 */
		@Override
		public Image getIcon() {

			if (icon == null) {
				throw new IllegalStateException(this.name() + "Icon has not been initialised.");
			}

			return icon;
		}

		/**
		 * Initialises the {@link SlickHazard#TSUNAMI} icon.
		 */
		@Override
		protected void initIcon(String uiPath) {
			icon = ImageReader.getImage(uiPath + "tsunamiIcon.png");
		}
	};

	public final ModelHazard model;

	/**
	 * Constructs an {@link SlickHazard}.
	 * 
	 * @param maxCasualties
	 *            The maximum percentage of a {@link Amry} that this
	 *            {@link SlickHazard} will kill.
	 * @param chance
	 *            Percentage chance that wit will occur in a given turn.
	 * @param name
	 *            The <code>String</code> representation of the
	 *            {@link SlickHazard}.
	 */
	private SlickHazard(ModelHazard model) {
		this.model = model;
	}

	/**
	 * Retrieves the {@link Image} icon that represents this
	 * {@link SlickHazard}.
	 * 
	 * @return <code>boolean</code>
	 */
	public abstract Image getIcon();
	
	/**
	 * Initialises all the {@link Image} icons of the {@link SlickHazard}s.
	 * 
	 * @param uiPath
	 *            The path to the folder with the icon image files in.
	 */
	public static void initIcons(String uiPath) {

		for (SlickHazard hazard : SlickHazard.values()) {
			hazard.initIcon(uiPath);
		}

	}

	/**
	 * Initialises the {@link Image} icon for an {@link SlickHazard}.
	 * 
	 * @param uiPath
	 *            The path to the folder with the icon image files in.
	 */
	protected abstract void initIcon(String uiPath);
}
