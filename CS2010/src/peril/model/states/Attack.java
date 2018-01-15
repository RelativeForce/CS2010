package peril.model.states;

import peril.board.Country;
import peril.controllers.ModelController;

public class Attack extends ModelState {

	@Override
	public boolean select(Country country, ModelController api) {

		final boolean selectPrimary = selectPrimary(country, api);
		final boolean selectSecondary = selectSecondary(country);

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
	 * The specified {@link Country} is a valid primary {@link Country} if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is ruled by the current player.</li>
	 * </ul>
	 */
	private boolean selectPrimary(Country country, ModelController api) {
		return country != null && api.getCurrentPlayer().equals(country.getRuler());
	}

	/**
	 * The specified {@link Country} is a valid primary {@link Country} if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is a valid target of the primary {@link Country}.</li>
	 * </ul>
	 */
	private boolean selectSecondary(Country country) {

		if (country == null) {
			return false;
		}

		return getPrimary() != null && isValidTarget(getPrimary(), country);
	}

	/**
	 * Retrieves the secondary selected {@link Country}.
	 * 
	 * @return Secondary selected {@link Country}
	 */
	private Country getSecondary() {
		return getSelected(1);
	}

	/**
	 * Retrieves the secondary selected {@link Country}.
	 * 
	 * @return Primary selected {@link Country}
	 */
	private Country getPrimary() {
		return getSelected(0);
	}

	/**
	 * Retrieves whether or not the secondary {@link Country} is a valid targets for
	 * the primary {@link Country}.
	 * 
	 * @param primaryCountry
	 *            is the primary {@link Country}
	 * @param secondaryTarget
	 *            is the second {@link Country}
	 * @return <code>boolean</code> if it is a valid target.
	 */
	private boolean isValidTarget(Country primaryCountry, Country secondaryTarget) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (!primaryCountry.getRuler().equals(secondaryTarget.getRuler())) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (primaryCountry.isNeighbour(secondaryTarget)) {

				// If the army size of the primary country is greater than 1.
				if (primaryCountry.getArmy().getSize() > 1) {
					return true;
				}

			}

		}

		return false;
	}

}
