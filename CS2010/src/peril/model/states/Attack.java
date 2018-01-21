package peril.model.states;

import peril.controllers.GameController;
import peril.model.board.ModelCountry;

public class Attack extends ModelState {

	@Override
	public boolean select(ModelCountry country, GameController api) {

		final boolean selectPrimary = selectPrimary(country, api);
		final boolean selectSecondary = selectSecondary(country);

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
	 * The specified {@link ModelCountry} is a valid primary {@link ModelCountry}
	 * if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is ruled by the current player.</li>
	 * </ul>
	 */
	private boolean selectPrimary(ModelCountry country, GameController api) {
		return country != null && api.getCurrentModelPlayer().equals(country.getRuler());
	}

	/**
	 * The specified {@link ModelCountry} is a valid primary {@link ModelCountry}
	 * if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is a valid target of the primary {@link ModelCountry}.</li>
	 * </ul>
	 */
	private boolean selectSecondary(ModelCountry country) {

		if (country == null) {
			return false;
		}

		return getPrimary() != null && isValidTarget(getPrimary(), country);
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
	 * Retrieves the secondary selected {@link ModelCountry}.
	 * 
	 * @return Primary selected {@link ModelCountry}
	 */
	public ModelCountry getSecondary() {
		return getSelected(1);
	}

	/**
	 * Retrieves whether or not the secondary {@link ModelCountry} is a valid
	 * targets for the primary {@link ModelCountry}.
	 * 
	 * @param primaryModelCountry
	 *            is the primary {@link ModelCountry}
	 * @param secondaryTarget
	 *            is the second {@link ModelCountry}
	 * @return <code>boolean</code> if it is a valid target.
	 */
	public boolean isValidTarget(ModelCountry primaryModelCountry, ModelCountry secondaryTarget) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (!primaryModelCountry.getRuler().equals(secondaryTarget.getRuler())) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (primaryModelCountry.isNeighbour(secondaryTarget)) {

				// If the army size of the primary country is greater than 1.
				if (primaryModelCountry.getArmy().getSize() > 1) {
					return true;
				}

			}

		}

		return false;
	}

}
