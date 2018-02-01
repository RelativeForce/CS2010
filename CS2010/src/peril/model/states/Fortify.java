package peril.model.states;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import peril.controllers.GameController;
import peril.controllers.api.Player;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

public class Fortify extends ModelState {

	@Override
	public boolean select(ModelCountry country, GameController api) {

		final boolean selectPrimary = selectPrimary(country, api);
		final boolean selectSecondary = selectSecondary(country, api);

		// If it a valid primary or secondary country
		if (selectPrimary || selectSecondary) {

			// If the country is a valid primary then the old primary is de-highlighted
			if (selectPrimary && !selectSecondary) {
				deselectAt(0);
				addSelected(country, 0);
			}
			// Otherwise the the secondary is de-highlighted and then the country is
			// highlighted.
			else {
				deselectAt(1);
				addSelected(country, 1);
			}

			return true;
		}

		deselectAll();
		return false;
	}

	/**
	 * Retrieves the secondary selected {@link ModelCountry}.
	 * 
	 * @return Secondary selected {@link ModelCountry}
	 */
	public ModelCountry getSecondary() {
		return getSelected(1);
	}

	/**
	 * Retrieves the secondary selected {@link ModelCountry}.
	 * 
	 * @return Primary selected {@link ModelCountry}
	 */
	public ModelCountry getPrimary() {
		return getSelected(0);
	}

	/**
	 * Retrieves the path between two path between two {@link ModelCountry}s.
	 * 
	 * @param start
	 *            {@link ModelCountry}
	 * @param target
	 *            {@link ModelCountry}
	 * @return
	 */
	public Stack<ModelCountry> getPathBetween(ModelCountry start, ModelCountry target, final ModelUnit unit) {

		// Holds the path from the friendly country to the target country.
		Stack<ModelCountry> path = new Stack<ModelCountry>();

		// Holds all the traversed countries
		Set<ModelCountry> traversed = new HashSet<>();

		// If the target belongs to the current player.
		if (target != null && target.getRuler() == start.getRuler()) {

			// If there is a path between the highlighted and the target add the points to
			// the drawn path.
			isPath(path, traversed, start, target, unit);
		}

		return path;

	}

	/**
	 * A {@link ModelCountry} is valid to be primary selected if:
	 * <ul>
	 * <li>It is <strong>NOT</strong> null.</li>
	 * <li>Has the same ruler as the current {@link Player}.</li>
	 * <li>There is not a current primary {@link ModelCountry} <strong>OR</strong>
	 * the specified {@link ModelCountry} is <strong>NOT</strong> a
	 * {@link MovementState#isValidLink(ModelCountry)}</li>
	 * </ul>
	 */
	protected boolean selectPrimary(ModelCountry country, GameController api) {

		if (country == null) {

			return false;
		}

		// Holds the current player
		ModelPlayer player = api.getCurrentModelPlayer();

		// Holds the ruler of the country
		ModelPlayer ruler = country.getRuler();

		return getPrimary() == null && player.equals(ruler) && country.getArmy().getStrength() > 1;

	}

	/**
	 * A {@link ModelCountry} is valid to be secondary selected if:
	 * <ul>
	 * <li>It is <strong>NOT</strong> null.</li>
	 * <li>Has the same ruler as the current {@link Player}.</li>
	 * <li>The specified {@link ModelCountry} is a
	 * {@link MovementState#isValidLink(ModelCountry)}</li>
	 * </ul>
	 */
	protected boolean selectSecondary(ModelCountry country, GameController api) {

		if (country == null || getPrimary() == null) {

			return false;
		}

		// Holds the current player
		final ModelPlayer player = api.getCurrentModelPlayer();

		// Holds the ruler of the country
		final ModelPlayer ruler = country.getRuler();

		// The country is different to the primary and has the same ruler as the player.
		final boolean friendlyModelCountry = player.equals(ruler) && getPrimary() != country;

		if (friendlyModelCountry) {

			// Iterate over all the units in the army
			for (ModelUnit current : getPrimary().getArmy()) {
				
				// The path between the current primary and the specified country.
				final Stack<ModelCountry> path = getPathBetween(getPrimary(), country, current);

				// If there is a path
				if (!path.isEmpty()) {
					return true;
				}
			}
		}

		return false;

	}

	/**
	 * This method will using a set of traversed nodes recursively perform a depth
	 * first search from one node to another. The children of each node are added to
	 * the
	 * 
	 * @param travsersed
	 *            {@link Set} of {@link ModelCountry} that have been traversed.
	 * @param country
	 *            {@link ModelCountry} currently being checked.
	 * @param target
	 *            {@link ModelCountry} the is to be reached.
	 * @return Whether the current {@link ModelCountry} is on the path to the target
	 *         {@link ModelCountry}.
	 */
	private boolean isPath(Stack<ModelCountry> path, Set<ModelCountry> travsersed, ModelCountry current,
			ModelCountry traget, final ModelUnit unit) {

		// Add the current country to the path
		path.push(current);

		// Holds the children of the current country that have the same ruler.
		Set<ModelCountry> validChildren = new HashSet<>();

		// Iterate through all the neighbours that the current country has
		for (ModelCountry country : current.getNeighbours()) {

			/*
			 * If the target country is a neighbour of the current country add it to the
			 * path then return true. This will result in all the parents of the current
			 * node to return true also.
			 */
			if (country.equals(traget)) {
				path.push(country);
				return true;
			}

			// If the country has not already been traversed and has the same ruler.
			if (!travsersed.contains(country) && current.getRuler() == country.getRuler()
					&& current.getLinkTo(country).canTransfer(unit, current, country)) {
				validChildren.add(country);
				travsersed.add(country);
			}

		}

		/*
		 * If there are no valid children then this path is a dead end. Due to this pop
		 * the current country from the path and return false.
		 */
		if (validChildren.isEmpty()) {
			path.pop();
			return false;
		}

		/*
		 * Iterate through each valid child and if the child is a part of the path
		 * return true.
		 */
		for (ModelCountry child : validChildren) {
			if (isPath(path, travsersed, child, traget, unit)) {
				return true;
			}
		}

		/*
		 * This will only be performed if the non of the children have valid reached the
		 * target. There for the current country is not on the path to the target.
		 */
		path.pop();
		return false;
	}

	/**
	 * Moves one unit from the primary {@link SlickCountry} to the secondary
	 * {@link SlickCountry}.
	 */
	public void fortify(ModelUnit unit) {

		ModelCountry primary = getSelected(0);
		ModelCountry target = getSelected(1);

		// If there is two countries highlighted
		if (primary != null && target != null) {

			// If the army of the primary highlighted country is larger that 1 unit in size
			if (primary.getArmy().getStrength() > unit.strength) {

				// Holds the army of the primary country
				ModelArmy primaryArmy = primary.getArmy();

				// Holds the army of the target country
				ModelArmy targetArmy = target.getArmy();

				// Move the unit.
				targetArmy.setStrength(targetArmy.getStrength() + unit.strength);
				primaryArmy.setStrength(primaryArmy.getStrength() - unit.strength);

				if (primaryArmy.getStrength() == 1) {
					deselectAll();
				}
			} else {
				// DO NOTHING
			}

		} else {
			// DO NOTHING
		}

	}

}
