package peril.views.slick.states.gameStates.multiSelectState;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.GameController;
import peril.model.states.ModelState;
import peril.views.slick.Point;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviours of a {@link CoreGameState} that can select two
 * {@link SlickCountry}s.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class MultiSelectState extends CoreGameState {

	/**
	 * Constructs a new {@link MultiSelectState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link MultiSelectState}.
	 * @param id
	 *            The ID of this {@link MultiSelectState}.
	 * @param stateName
	 *            The name of this {@link MultiSelectState}.
	 * 
	 */
	public MultiSelectState(GameController game, String stateName, int id, ModelState model) {
		super(game, stateName, id, model);
	}

	/**
	 * Processes a mouse click a {@link Point} on this {@link MultiSelectState}.
	 */
	@Override
	public void parseClick(int button, Point click) {

		if (button == Input.MOUSE_RIGHT_BUTTON) {
			model.deselectAll();
		}

		super.parseClick(button, click);
	}

	/**
	 * Performs the exit state operations of this {@link MultiSelectState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		// Remove the highlight effect on the current highlighted country.
		model.deselectAll();
	}

}
