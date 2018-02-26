package peril.views.slick.states.gameStates;

import java.util.Observable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import net.java.games.input.Component;
import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelCountry;
import peril.model.states.Setup;
import peril.views.slick.Frame;
import peril.views.slick.util.Point;

/**
 * The state where the user selects which {@link ModelPlayer} gets what
 * {@link ModelCountry}s before the game properly begins.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 * @since 2018-02-26
 * @version 1.01.01
 * 
 * @see CoreGameState
 *
 */
public final class SetupState extends CoreGameState {

	/**
	 * Constructs a new {@link SetupState}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link SetupState} to
	 *            query the state of the game.
	 * @param id
	 *            The ID of this {@link SetupState}
	 */
	public SetupState(GameController game, int id, Setup model) {
		super(game, model.getName(), id, model);

		model.addObserver(this);
	}

	/**
	 * Enters the {@link SetupState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.hideSaveOption();
		slick.showToolTip("Press 'Auto' to evenly distrubute units, then press 'Play'");
	}

	/**
	 * Renders this {@link SetupState}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		super.render(gc, frame);

		super.drawAllLinks(frame);
		super.drawArmies(frame);
		super.drawImages();
		super.drawButtons();
		super.drawMiniMap(frame);

		menus.draw(frame);

	}

	/**
	 * Parses a button press on this {@link SetupState}.
	 */
	@Override
	public void parseButton(int key, Point mousePosition) {
		super.parseButton(key, mousePosition);

		final ModelCountry highlighted = model.getSelected(0);

		/*
		 * If the player has highlighted a county then parse the button presses that
		 * change the owner ship of a country.
		 */
		if (highlighted != null) {

			switch (key) {
			case Input.KEY_1:
				if (game.isPlaying(1)) {
					model.swapRuler(highlighted, game.getModelPlayer(1));
				}
				break;
			case Input.KEY_2:
				if (game.isPlaying(2)) {
					model.swapRuler(highlighted, game.getModelPlayer(2));
				}
				break;
			case Input.KEY_3:
				if (game.isPlaying(3)) {
					model.swapRuler(highlighted, game.getModelPlayer(3));
				}
				break;
			case Input.KEY_4:
				if (game.isPlaying(4)) {
					model.swapRuler(highlighted, game.getModelPlayer(4));
				}
				break;
			case Input.KEY_SPACE:
				model.swapRuler(highlighted, null);
				break;
			}
		}
	}

	/**
	 * Moves all the {@link Component}s of this {@link SetupState}.
	 */
	@Override
	protected void panElements(Point panVector) {
		// No elements to pan.
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		// Do any updates when a country is selected.

	}
}
