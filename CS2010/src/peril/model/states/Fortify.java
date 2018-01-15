package peril.model.states;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import peril.ai.api.Player;
import peril.ai.api.Controller;
import peril.board.Country;
import peril.ui.states.gameStates.multiSelectState.MovementState;

public class Fortify extends ModelState {

	@Override
	public boolean select(Country country, Controller api) {

		final boolean selectPrimary = selectPrimary(country, api);
		final boolean selectSecondary = selectSecondary(country, api);

		// If it a valid primary or secondary country
		if (selectPrimary || selectSecondary) {

			// The secondary is dependent on the primary so as the country is valid primary
			// or secondary the current secondary must be de-highlighted.
			removeHighlight(getSecondary());

			// If the country is a valid primary then the old primary is de-highlighted
			if (selectPrimary && !selectSecondary) {
				removeHighlight(getPrimary());
				deselectAt(0);

				addSelected(country, 0);

			}
			// Otherwise the the secondary is de-highlighted and then the country is
			// highlighted.
			else {
				deselectAt(1);
				addSelected(country, 1);
			}

			addHighlight(country);

			return true;
		}

		// Otherwise remove the highlight from both primary and secondary.
		removeHighlight(getPrimary());
		removeHighlight(getSecondary());
		deselectAll();
		return false;
	}

	/**
	 * Retrieves the secondary selected {@link Country}.
	 * 
	 * @return Secondary selected {@link Country}
	 */
	public Country getSecondary() {
		return getSelected(1);
	}

	/**
	 * Retrieves the secondary selected {@link Country}.
	 * 
	 * @return Primary selected {@link Country}
	 */
	public Country getPrimary() {
		return getSelected(0);
	}

	/**
	 * Retrieves the path between two path between two {@link Country}s.
	 * 
	 * @param start
	 *            {@link Country}
	 * @param target
	 *            {@link Country}
	 * @return
	 */
	private Stack<Country> getPathBetween(Country start, Country target) {

		// Holds the path from the friendly country to the target country.
		Stack<Country> path = new Stack<Country>();

		// Holds all the traversed countries
		Set<Country> traversed = new HashSet<>();

		// If the target belongs to the current player.
		if (target != null && target.getRuler() == start.getRuler()) {

			// If there is a path between the highlighted and the target add the points to
			// the drawn path.
			isPath(path, traversed, start, target);
		}

		return path;

	}


	/**
	 * A {@link Country} is valid to be primary selected if:
	 * <ul>
	 * <li>It is <strong>NOT</strong> null.</li>
	 * <li>Has the same ruler as the current {@link Player}.</li>
	 * <li>There is not a current primary {@link Country} <strong>OR</strong> the
	 * specified {@link Country} is <strong>NOT</strong> a
	 * {@link MovementState#isValidLink(Country)}</li>
	 * </ul>
	 */
	protected boolean selectPrimary(Country country, Controller api) {

		if (country == null) {
			
			return false;
		}

		// Holds the current player
		Player player = api.getCurrentPlayer();

		// Holds the ruler of the country
		Player ruler = country.getRuler();

		return getPrimary() == null && player.equals(ruler) && country.getArmy().getSize() > 1;

	}

	/**
	 * A {@link Country} is valid to be secondary selected if:
	 * <ul>
	 * <li>It is <strong>NOT</strong> null.</li>
	 * <li>Has the same ruler as the current {@link Player}.</li>
	 * <li>The specified {@link Country} is a
	 * {@link MovementState#isValidLink(Country)}</li>
	 * </ul>
	 */
	protected boolean selectSecondary(Country country, Controller api) {
		
		if (country == null || getPrimary() == null) {
			
			return false;
		}

		// Holds the current player
		Player player = api.getCurrentPlayer();

		// Holds the ruler of the country
		Player ruler = country.getRuler();

		// The country is different to the primary and has the same ruler as the player.
		final boolean friendlyCountry = player.equals(ruler) && getPrimary() != country;

		if (friendlyCountry) {

			// The path between the current primary and the specified country.
			final Stack<Country> path = getPathBetween(getPrimary(), country);

			// If there is a path
			if (!path.isEmpty()) {
				return true;
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
	 *            {@link Set} of {@link Country} that have been traversed.
	 * @param country
	 *            {@link Country} currently being checked.
	 * @param target
	 *            {@link Country} the is to be reached.
	 * @return Whether the current {@link Country} is on the path to the target
	 *         {@link Country}.
	 */
	private boolean isPath(Stack<Country> path, Set<Country> travsersed, Country current, Country traget) {

		// Add the current country to the path
		path.push(current);

		// Holds the children of the current country that have the same ruler.
		Set<Country> validChildren = new HashSet<>();

		// Iterate through all the neighbours that the current country has
		for (Country country : current.getNeighbours()) {

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
			if (!travsersed.contains(country) && current.getRuler() == country.getRuler()) {
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
		for (Country child : validChildren) {
			if (isPath(path, travsersed, child, traget)) {
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

}
