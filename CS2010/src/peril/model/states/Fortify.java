package peril.model.states;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import peril.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLink;

/**
 * The logic for the {@link ModelState} where current {@link ModelPlayer} can
 * select one of their {@link ModelCountry}s then another provided that there is
 * a path of friendly {@link ModelCountry}s between the two
 * {@link ModelCountry}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-16
 * @version 1.01.08
 *
 * @see ModelState
 */
public final class Fortify extends ModelState {

	/**
	 * The name of the {@link Fortify} state.
	 */
	private static final String STATE_NAME = "Fortify";

	/**
	 * Constructs an new {@link Fortify}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link ModelState} to
	 *            query the state of the game.
	 */
	public Fortify(GameController game) {
		super(STATE_NAME, game);
	}

	/**
	 * During the {@link Fortify} state the current {@link ModelPlayer} can select
	 * one of their {@link ModelCountry}s then another provided that there is a path
	 * of friendly {@link ModelCountry}s between the two {@link ModelCountry}.
	 */
	@Override
	public boolean select(ModelCountry country) {

		// Whether the country is a valid primary country.
		final boolean validPrimary = selectPrimary(country);

		// Whether the country is a valid secondary country.
		final boolean validSecondary = selectSecondary(country);

		// If it a valid primary or secondary country
		if (validPrimary || validSecondary) {

			// If the country is a valid primary then the old primary is de-selected
			if (validPrimary && !validSecondary) {
				deselectAt(0);
				addSelected(country, 0);
			}
			// Otherwise the the secondary is de-selected and then the country is
			// selected.
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
	 * Retrieves the primary selected {@link ModelCountry}.
	 * 
	 * @return Primary selected {@link ModelCountry}
	 */
	public ModelCountry getPrimary() {
		return getSelected(0);
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
	 * Retrieves the path between two path between two {@link ModelCountry}s ruled
	 * by the same {@link ModelPlayer} that the specified {@link ModelUnit} can
	 * traverse.
	 * 
	 * @param start
	 *            The {@link ModelCountry} where the path will begin.
	 * @param target
	 *            The {@link ModelCountry} where the path will end.
	 * @param unit
	 *            THe {@link ModelUnit} that will be transfered across the path.
	 * @return The path of {@link ModelCountry}s between the two specified
	 *         {@link ModelCountry}s.
	 */
	public List<ModelCountry> getPathBetween(ModelCountry start, ModelCountry target, ModelUnit unit) {

		// Holds the path from the friendly country to the target country.
		final LinkedList<ModelCountry> path = new LinkedList<ModelCountry>();

		// Holds all the traversed countries
		final Set<ModelCountry> traversed = new HashSet<>();

		// If the target belongs to the current player.
		if (target != null && target.getRuler() == start.getRuler()) {

			// Populate the path between the start and the target.
			isPath(path, traversed, start, target, unit);

		}

		// Return the path as a list so that it is clear which end is the start.
		return path;

	}

	/**
	 * Moves one unit from the primary {@link ModelCountry} to the secondary
	 * {@link ModelCountry} along the path between them determined by
	 * {@link Fortify#getPathBetween(ModelCountry, ModelCountry, ModelUnit)}.
	 */
	public void fortify() {

		final ModelCountry primary = getSelected(0);
		final ModelCountry target = getSelected(1);

		// If there is two countries selected.
		if (primary != null && target != null) {

			// Holds the army of the primary country
			final ModelArmy primaryArmy = primary.getArmy();

			// Whether of not there is a unit selected in the current country's army.
			final boolean unitSelected = primaryArmy.getSelected() != null;

			// If there is a selected unit fortify the country with that otherwise use the
			// weakest unit in the army.
			final ModelUnit unit = unitSelected ? primaryArmy.getSelected() : primaryArmy.getStrongestUnit();

			// If the primary army is larger that 1 unit in size and has at least one of the
			// specified unit.
			if (primaryArmy.getNumberOfUnits() > 1 && primaryArmy.getNumberOf(unit) > 0) {

				// The path between the two countries.
				final List<ModelCountry> path = getPathBetween(primary, target, unit);

				// Transfer the unit along the path
				transferAlongPath(path, unit);

				// If there is one unit left in the primary army then de-select the countries.
				if (primaryArmy.getNumberOfUnits() == 1) {
					deselectAll();
				}
			} else {
				// DO NOTHING
			}

		} else {
			// DO NOTHING
		}

	}

	/**
	 * Transfers the specified {@link ModelUnit} across all the {@link ModelLink}
	 * between all the {@link ModelCountry}s on the path.
	 * 
	 * @param path
	 *            The {@link List} of {@link ModelCountry}s that connect two
	 *            {@link ModelCountry}s.
	 * @param unit
	 *            The {@link ModelUnit} that will be transfered across the path.
	 */
	private void transferAlongPath(List<ModelCountry> path, ModelUnit unit) {

		// Holds the country that was before the current country in the path.
		ModelCountry previous = null;

		// Iterate over every country in the path an move the unit from the previous
		// country to the next.
		for (ModelCountry current : path) {

			// If this is not the first country in the path.
			if (previous != null) {

				// The link between the previous and current country.
				final ModelLink link = previous.getLinkTo(current);

				// Transfer the unit.
				link.transferBetween(unit, previous, current);

			}

			// Set the current as the previous.
			previous = current;

		}
	}

	/**
	 * Retrieves whether or not the specified {@link ModelCountry} could be selected
	 * as the {@link Fortify}'s primary {@link ModelCountry}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that could be selected.
	 * @return Whether or not the specified {@link ModelCountry} could be selected.
	 */
	private boolean selectPrimary(ModelCountry country) {

		// If the country is null then it is not a valid primary.
		if (country == null) {
			return false;
		}

		// Whether the country is ruled by the current player.
		final boolean ruledByCurrentPlayer = game.getCurrentModelPlayer().equals(country.getRuler());

		// Whether the country's army has enough units to fortify another country once.
		final boolean hasEnoughUnits = country.getArmy().getNumberOfUnits() > 1;

		// Whether there is no primary currently selected.
		final boolean noPrimary = getPrimary() == null;

		// Return whether all the criteria were met or not.
		return noPrimary && ruledByCurrentPlayer && hasEnoughUnits;

	}

	/**
	 * Retrieves whether or not the specified {@link ModelCountry} could be selected
	 * as the {@link Fortify}'s secondary {@link ModelCountry}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that could be selected.
	 * @return Whether or not the specified {@link ModelCountry} could be selected.
	 */
	private boolean selectSecondary(ModelCountry country) {

		if (country == null || getPrimary() == null) {
			return false;
		}

		// Whether the country is ruled by the current player.
		final boolean ruledByCurrentPlayer = game.getCurrentModelPlayer().equals(country.getRuler());

		// Whether the country is different to the primary and has the same ruler as the
		// player.
		final boolean friendlyCountry = ruledByCurrentPlayer && getPrimary() != country;

		// If the country is a friendly country check return whether any units could be
		// transfered between them.
		return friendlyCountry ? canTransfer(country) : false;

	}

	/**
	 * Retrieves whether or not {@link ModelUnit}s can be transfered between the
	 * currently selected primary {@link ModelCountry} and the specified
	 * {@link ModelCountry}.
	 * 
	 * @param country
	 *            The {@link ModelCountry} that could be selected.
	 * @return Whether or not {@link ModelUnit}s can be transfered between the
	 *         currently selected primary {@link ModelCountry} and the specified
	 *         {@link ModelCountry}.
	 */
	private boolean canTransfer(ModelCountry country) {

		// Holds the current primary country
		final ModelCountry primary = getPrimary();

		// Holds the army of the primary country
		final ModelArmy primaryArmy = primary.getArmy();

		/*
		 * If the user has selected a model unit check check if that unit can be
		 * Transfered. Otherwise check if any of the units in the army can be
		 * transfered.
		 */
		if (primaryArmy.getSelected() != null) {

			// Holds the currently selected model unit from the army.
			final ModelUnit unit = primaryArmy.getSelected();

			// If there is a path then the unit can be transfered.
			return !getPathBetween(primary, country, unit).isEmpty();

		} else {

			// Iterate over all the units in the army an see if at least one can be
			// transfered.
			for (ModelUnit unit : primaryArmy) {

				// The path between the current primary and the specified country.
				final List<ModelCountry> path = getPathBetween(primary, country, unit);

				// If there is a path
				if (!path.isEmpty()) {
					return true;
				}
			}

			// If non of the units could be transfered return false;
			return false;
		}

	}

	/**
	 * This method will using a {@link Set} of traversed nodes (aka
	 * {@link ModelCountry}) recursively perform a depth first search from one node
	 * to another. If a link between two {@link ModelCountry}s does not allow the
	 * specified {@link ModelUnit} to be transfered
	 * ({@link ModelLink#canTransfer(ModelUnit)}) then
	 * the link will to be used in the path.
	 * 
	 * @param path
	 *            The current path of the search.
	 * @param travsersed
	 *            The {@link Set} of {@link ModelCountry} that have already been
	 *            traversed.
	 * @param current
	 *            The {@link ModelCountry} currently being checked.
	 * @param target
	 *            The {@link ModelCountry} that will result in the path being
	 *            complete.
	 * @param unit
	 *            The unit that will be transfered across the path.
	 * @return Whether the current {@link ModelCountry} is on the path to the target
	 *         {@link ModelCountry}.
	 * 
	 * @see ModelLink
	 * @see Deque
	 */
	private boolean isPath(Deque<ModelCountry> path, Set<ModelCountry> travsersed, ModelCountry current,
			ModelCountry target, ModelUnit unit) {

		// Add the current country to the path
		path.addLast(current);

		// Holds the children of the current country that can be traversed.
		final Set<ModelCountry> validChildren = new HashSet<>();

		// Iterate through all the neighbours that the current country has
		for (ModelCountry country : current.getNeighbours()) {

			/*
			 * If the target country is a neighbour of the current country and has an open
			 * link, add it to the path then return true. This will result in all the
			 * parents of the current node to return true also.
			 */
			if (country.equals(target) && current.getLinkTo(country).canTransfer(unit)) {
				path.addLast(country);
				return true;
			}

			// If the country has not already been traversed and has the same ruler.
			if (!travsersed.contains(country) && current.getRuler() == country.getRuler()) {

				// Holds the links between the two countries.
				final ModelLink link = current.getLinkTo(country);

				// If the unit can be transfered over the link then set the country as a valid
				// child and set it as traversed.
				if (link.canTransfer(unit)) {
					validChildren.add(country);
					travsersed.add(country);
				}
			}

		}

		/*
		 * If there are no valid children then this path is at dead end. Due to this pop
		 * the current country from the path and return false signifying that the
		 * current county is not on the path.
		 */
		if (validChildren.isEmpty()) {
			path.removeLast();
			return false;
		}

		/*
		 * Iterate through each valid child and if the child is a part of the path
		 * return true signifying that the current county is on the path.
		 */
		for (ModelCountry child : validChildren) {
			if (isPath(path, travsersed, child, target, unit)) {
				return true;
			}
		}

		/*
		 * This will only be performed if non of the children have reached the target.
		 * Therefore the current country is not on the path to the target.
		 */
		path.removeLast();
		return false;
	}

}
