package peril.ai.api;

import java.util.Set;

/**
 * Provides information about a army consisting of {@link Unit}s.
 * 
 * @author Joshua_Eddy
 * 
 * @version 1.01.05
 * @since 2018-03-16
 *
 */
public interface Army {

	/**
	 * Retrieves the {@link Unit}s from this {@link Army}. This does not reflect the
	 * number of each {@link Unit} in the {@link Army}
	 * 
	 * @return The {@link Set}&lt;{@link Unit}&gt; of {@link Unit}s.
	 */
	Set<? extends Unit> getUnits();

	/**
	 * Retrieves the strongest {@link Unit} in this {@link Army}. If there are no
	 * {@link Unit}s in this {@link Army} this returns null.
	 * 
	 * @return Strongest {@link Unit} in this {@link Army}.
	 */
	Unit getStrongestUnit();

	/**
	 * Retrieves the weakest {@link Unit} in this {@link Army}. If there are no
	 * {@link Unit}s in this {@link Army} this returns null.
	 * 
	 * @return Weakest {@link Unit} in this {@link Army}.
	 */
	Unit getWeakestUnit();

	/**
	 * Retrieves the combined strength of all the {@link Unit}s in this
	 * {@link Army}.
	 * 
	 * @return The {@link Army}'s total strength.
	 */
	int getStrength();

	/**
	 * Retrieves a number of a specific type of {@link Unit} that are currently in
	 * this {@link Army}.
	 * 
	 * @param unit
	 *            The {@link Unit}.
	 * @return The number of the specified {@link Unit}.
	 */
	int getNumberOf(Unit unit);

	/**
	 * Retrieves a whether a specific type of {@link Unit} is currently in this
	 * {@link Army}.
	 * 
	 * @param unit
	 *            {@link Unit}
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
