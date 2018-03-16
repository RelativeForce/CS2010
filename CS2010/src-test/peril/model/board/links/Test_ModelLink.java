package peril.model.board.links;

import static org.junit.Assert.*;

import org.junit.Test;

import peril.model.ModelColor;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;

/**
 * 
 * Tests {@link ModelLink}.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 * 
 * @version 1.01.03
 * @since 2018-03-16
 * 
 * @see ModelLink
 *
 */
public final class Test_ModelLink {

	/**
	 * Tests that {@link ModelLink#setState(ModelLinkState, int)} functions normally
	 * when given normal parameters.
	 */
	@Test
	public void test_setState_normal() {

		final ModelLink link = new ModelLink(ModelLinkState.OPEN);
		final int duration = 3;

		link.setState(ModelLinkState.BLOCKADE, duration);

		// Assert that the state has been changed.
		assertTrue(link.getState() == ModelLinkState.BLOCKADE);

		// Count down the duration of the link's state confirming that the remaining
		// duration also steps down.
		for (int elapsingDuration = duration; elapsingDuration >= 0; elapsingDuration--) {

			assertTrue(link.getDuration() == elapsingDuration);
			link.elapse();

		}

	}

	/**
	 * Confirm that that {@link ModelLink#setState(ModelLinkState, int)} throws a
	 * {@link NullPointerException} when null is given in place of a valid
	 * {@link ModelLinkState}.
	 */
	@Test(expected = NullPointerException.class)
	public void test_setState_null() {

		final ModelLink link = new ModelLink(ModelLinkState.OPEN);
		link.setState(null, 3);
	}

	/**
	 * Confirm that that {@link ModelLink#setState(ModelLinkState, int)} throws a
	 * {@link IllegalArgumentException} when a negative duration is given.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void test_setState_invalidDuration() {

		final ModelLink link = new ModelLink(ModelLinkState.OPEN);
		link.setState(ModelLinkState.BLOCKADE, -1);
	}

	/**
	 * Confirm that that {@link ModelLink#setState(ModelLinkState, int)} does not
	 * change the state when the state is returned to default.
	 */
	@Test
	public void test_setState_defaultState() {

		final ModelLinkState defaultState = ModelLinkState.OPEN;
		final ModelLink link = new ModelLink(defaultState);

		link.setState(defaultState, 3);

		assertTrue(link.getState() == defaultState);
		assertTrue(link.getDuration() == 0);

	}

	/**
	 * Confirm that that
	 * {@link ModelLink#canTransfer(ModelUnit)} returns
	 * the same value as the
	 * {@link ModelLinkState#canTransfer(ModelUnit)}
	 * that is given by the links current state.
	 */
	@Test
	public void test_canTransfer() {

		final ModelLinkState defaultState = ModelLinkState.OPEN;
		final ModelCountry origin = new ModelCountry("origin", new ModelColor(0, 0, 0));
		final ModelCountry destination = new ModelCountry("destination", new ModelColor(0, 0, 0));
		final ModelLink link = new ModelLink(defaultState);
		final ModelUnit unit = new ModelUnit("test", 1, "na");

		origin.addNeighbour(destination, link);

		assertTrue(defaultState.canTransfer(unit) == link.canTransfer(unit));

	}

}
