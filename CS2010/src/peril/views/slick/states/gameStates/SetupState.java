package peril.views.slick.states.gameStates;

import java.util.Observable;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import net.java.games.input.Component;
import peril.Game;
import peril.controllers.GameController;
import peril.model.board.ModelCountry;
import peril.model.states.Setup;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.util.Point;

/**
 * The state where the user selects which player gets what
 * {@link SlickCountry}s.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 *         {@link CoreGameState}
 *
 */
public final class SetupState extends CoreGameState {

	/**
	 * Constructs a new {@link SetupState}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link SetupState} is a part of.
	 * @param id
	 *            The ID of this {@link SetupState}
	 */
	public SetupState(GameController game, int id, Setup model) {
		super(game, model.getName(), id, model);

		model.addObserver(this);
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.hideSaveOption();
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
	public void parseButton(Frame frame, int key, Point mousePosition) {

		ModelCountry highlighted = model.getSelected(0);

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

		super.parseButton(frame, key, mousePosition);

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
