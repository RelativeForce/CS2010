package peril.controllers.api;

import java.util.Set;

/**
 * Provides information about a army consisting of {@link Unit}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.04
 * @since 2018-01-21
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
	 * Retrieves the strongest {@link Unit} in this {@link Army}.
	 * 
	 * @return Strongest {@link Unit} in this {@link Army}.
	 */
	Unit getStrongestUnit();

	/**
	 * Retrieves the weakest {@link Unit} in this {@link Army}.
	 * 
	 * @return Weakest {@link Unit} in this {@link Army}.
	 */
	Unit getWeakestUnit();

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
	 * Retrieves the number of {@link Unit}s in this {@link Army}
	 * 
	 * @return The number of {@link Unit}s.
	 */
	int getNumberOfUnits();

}
