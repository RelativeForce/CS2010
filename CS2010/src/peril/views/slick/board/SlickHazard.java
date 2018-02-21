package peril.views.slick.board;

import org.newdawn.slick.Image;
import peril.model.board.ModelHazard;
import peril.views.slick.io.ImageReader;

/**
 * The visual representation of a {@link ModelHazard}. Each value of
 * {@link ModelHazard} has a visual {@link SlickHazard}
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-19
 * @version 1.01.02
 * 
 * @see ModelHazard
 *
 */
public enum SlickHazard {

	/**
	 * The visual representation of {@link ModelHazard#VOLCANIC_ERUPTION}.
	 */
	VOLCANIC_ERUPTION(ModelHazard.VOLCANIC_ERUPTION) {

		/**
		 * The {@link Image} icon of an {@link SlickHazard#VOLCANIC_ERUPTION}.
		 */
		private Image icon;

		/**
		 * Retrieves the {@link Image} icon of an {@link SlickHazard#VOLCANIC_ERUPTION}.
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
			icon = ImageReader.getImage(uiPath + "volcanoIcon.png").getScaledCopy(WIDTH, HEIGHT);
		}

	},
	/**
	 * The visual representation of {@link ModelHazard#TORNADO}.
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
			icon = ImageReader.getImage(uiPath + "tornadoIcon.png").getScaledCopy(WIDTH, HEIGHT);
		}
	},
	/**
	 * The visual representation of {@link ModelHazard#HURRICANE}.
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
			icon = ImageReader.getImage(uiPath + "hurricaneIcon.png").getScaledCopy(WIDTH, HEIGHT);
		}
	},
	/**
	 * The visual representation of {@link ModelHazard#TSUNAMI}.
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
			icon = ImageReader.getImage(uiPath + "tsunamiIcon.png").getScaledCopy(WIDTH, HEIGHT);
		}
	};

	/**
	 * The width of the {@link SlickHazard} icons on screen.
	 */
	private static final int WIDTH = 60;

	/**
	 * The height of the {@link SlickHazard} icons on screen.
	 */
	private static final int HEIGHT = 60;

	/**
	 * The {@link ModelHazard} this {@link SlickHazard} displays to the user.
	 */
	public final ModelHazard model;

	/**
	 * Constructs an {@link SlickHazard}.
	 * 
	 * @param model
	 *            The {@link ModelHazard} this {@link SlickHazard} displays to the
	 *            user.
	 */
	private SlickHazard(ModelHazard model) {
		this.model = model;
	}

	/**
	 * Initialises all the {@link Image} icons of the {@link SlickHazard}s.
	 * 
	 * @param harazrdsPath
	 *            The path to the folder with the icon image files in.
	 */
	public static void initIcons(String harazrdsPath) {

		for (SlickHazard hazard : SlickHazard.values()) {
			hazard.initIcon(harazrdsPath);
		}

	}

	/**
	 * Retrieves the {@link Image} icon that represents this {@link SlickHazard}.
	 * 
	 * @return The {@link Image} icon that represents this {@link SlickHazard}
	 */
	public abstract Image getIcon();

	/**
	 * Initialises the {@link Image} icon for an {@link SlickHazard}.
	 * 
	 * @param uiPath
	 *            The path to the folder with the icon {@link Image} files in.
	 */
	protected abstract void initIcon(String uiPath);
}
