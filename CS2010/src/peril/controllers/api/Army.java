package peril.controllers.api;

import java.util.Set;

/**
 * Provides information about a army consisting of {@link Unit}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.02
 * @since 2018-01-13
 *
 */
public interface Army {

	/**
	 * Retrieves the {@link Unit}s from this army.
	 * 
	 * @return {@link Set}&lt;{@link Unit}&gt;
	 */
	Set<? extends Unit> getUnits();

	/**
	 * Retrieves the strength of the {@link Army}.
	 * 
	 * @return Strength
	 */
	int getStrength();

	/**
	 * Retrieves a number of a specific type of {@link Unit} that are currently in
	 * this {@link Army}.
	 * 
	 * @param unit
	 *            Type of {@link Unit}
	 * @return Number of that {@link Unit}
	 */
	int getNumberOf(Unit unit);

	/**
	 * Retrieves a whether a specific type of {@link Unit} is currently in this
	 * {@link Army}.
	 * 
	 * @param unit
	 *            {@link Army}
	 * @return Whether a specific type of {@link Unit} is currently in this
	 *         {@link Army}.
	 */
	boolean hasUnit(Unit unit);

	/**
	 * Converts as many of the {@link Unit}s from this {@link Army} into the
	 * {@link Unit} above in terms of strength. If no {@link Unit}s were traded then
	 * this method will return false;
	 * 
	 * @param unit
	 *            Specified {@link Unit} to trade up.
	 * @return Whether or not any {@link Unit}s were traded up.
	 */
	boolean tradeUnitsUp();

}
