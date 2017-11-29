package peril;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import peril.board.Army;
import peril.io.fileReaders.ImageReader;
import peril.ui.Viewable;

/**
 * The internal representation of a user of the system. This object will hold
 * all of the details about a users game such as the number of {@link Country}s
 * that user has.
 * 
 * @author Joshua_Eddy, Ezekiel_Trinidad
 *
 */
public final class Player extends Viewable {

	/**
	 * Static instance of the first {@link Player}.
	 * 
	 */
	public static final Player ONE = new Player(1, Color.red);

	/**
	 * Static instance of the second {@link Player}.
	 * 
	 */
	public static final Player TWO = new Player(2, Color.blue);

	/**
	 * Static instance of the third {@link Player}.
	 * 
	 */
	public static final Player THREE = new Player(3, Color.green);

	/**
	 * Static instance of the fourth {@link Player}.
	 * 
	 */
	public static final Player FOUR = new Player(4, Color.pink.multiply(Color.pink));

	/**
	 * Numerical Representation of the {@link Player}.
	 */
	private final int number;

	/**
	 * The {@link Color} of this {@link Player}s overlay on the
	 * {@link UserInterface}.
	 */
	private final Color color;

	/**
	 * The total {@link Army} of the {@link Player}.
	 * 
	 */
	private Army totalArmy;

	/**
	 * The {@link Army} to be distributed by the {@link Player} at the start of the
	 * next turn.
	 * 
	 */
	private Army distributableArmy;

	/**
	 * The number of the {@link Country}s the {@link Player} owns.
	 */
	private int countries;

	/**
	 * The number of {@link Continent}s the {@link Player} owns.
	 */
	private int continents;

	/**
	 * Constructs a new {@link Player}.
	 * 
	 * @param number
	 *            The number of this player
	 */
	private Player(int number, Color color) {
		this.countries = 0;
		this.continents = 0;
		this.color = color;
		this.distributableArmy = new Army(0);
		this.totalArmy = new Army(0);
		this.number = number;
		setPosition(new Point(10, 45));
	}

	/**
	 * Awards the {@link Player} an {@link Army}.
	 * 
	 * @param army
	 *            The army that will be awarded to the {@link Player}.
	 */
	public void award(Army army) {
		distributableArmy.setSize(distributableArmy.getSize() + army.getSize());
	}

	@Override
	public Image getImage() {

		if (!hasImage()) {

			// Volcanic Eruption icon file path
			StringBuilder playerPath = new StringBuilder(new File(System.getProperty("user.dir")).getPath());
			playerPath.append(File.separatorChar);
			playerPath.append("ui_assets");
			playerPath.append(File.separatorChar);
			playerPath.append("player" + number + "Icon.png");
			setImage(getPosition(), ImageReader.getImage(playerPath.toString()).getScaledCopy(90, 40));

		}

		return super.getImage();
	}

	/**
	 * Sets the size of the {@link Army} the {@link Player} will have to distribute
	 * at the start of the next turn.
	 * 
	 * @param size
	 *            <code>int</code> new size of the {@link Army}. Cannot be &lt; zero
	 */
	public void setDistributableArmySize(int size) {
		distributableArmy.setSize(size);
	}

	/**
	 * Sets the new size of this {@link Player}'s total {@link Army}s. This should
	 * represent the combined {@link Army} size of this {@link Player}.
	 * 
	 * @param size
	 *            <code>int</code> new size of the {@link Army}. Cannot be &lt; zero
	 */
	public void setTotalArmySize(int size) {
		totalArmy.setSize(size);
	}

	/**
	 * Retrieves the size of this {@link Player}'s total {@link Army}s. This should
	 * represent the combined {@link Army} size of this {@link Player}.
	 * 
	 * @return <code>int</code> size of the {@link Army}.
	 */
	public int getTotalArmySize() {
		return totalArmy.getSize();
	}

	/**
	 * Retrieves the size of the {@link Army} the {@link Player} will have to
	 * distribute at the start of the next turn.
	 * 
	 * @return
	 */
	public int getDistributableArmySize() {
		return distributableArmy.getSize();
	}

	/**
	 * Sets the {@link Country}s owned by the {@link Player}.
	 * 
	 * @param countriesOwned
	 *            The number of {@link Country}s the {@link Player} now owns.
	 */
	public void setCountriesRuled(int countriesOwned) {
		countries = countriesOwned;
	}

	/**
	 * Gets the number of {@link Country}s ruled by the {@link Player}.
	 * 
	 * @return countries Number of {@link Country}s.
	 */
	public int getCountriesRuled() {
		return countries;
	}

	/**
	 * Sets the {@link Continent}s ruled by the {@link Player}.
	 * 
	 * @param continentsRuled
	 *            The number of {@link Continent}s the {@link Player} now owns.
	 */
	public void setContinentsRuled(int continentsRuled) {
		continents = continentsRuled;
	}

	/**
	 * Gets the {@link Player}'s color
	 * 
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the number of {@link Continent}s owned by the {@link Player}.
	 * 
	 * @return continents Number of {@link Continent}s.
	 */
	public int getContinentsRuled() {
		return continents;
	}

	/**
	 * Gets the name of this {@link Player}.
	 */
	@Override
	public String toString() {
		return "Player " + number;
	}

	/**
	 * Resents this players fields back to their default values.
	 */
	public void reset() {

		totalArmy.setSize(0);
		distributableArmy.setSize(0);
		countries = 0;
		continents = 0;

	}

	/**
	 * Retrieves a {@link Player} by its name.
	 * 
	 * @param name
	 *            <code>String</code>
	 * @return {@link Player}
	 */
	public static Player getByName(String name) {

		if (Player.ONE.toString().equals(name)) {
			return Player.ONE;
		} else if (Player.TWO.toString().equals(name)) {
			return Player.TWO;
		} else if (Player.THREE.toString().equals(name)) {
			return Player.THREE;
		} else if (Player.FOUR.toString().equals(name)) {
			return Player.FOUR;
		} else if (name.equals("-")) {
			return null;
		}

		throw new NullPointerException(name + " is not a valid player");
	}
}
