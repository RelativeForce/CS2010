package peril.views.slick.states.gameStates;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.helpers.UnitHelper;
import peril.model.board.ModelCountry;
import peril.model.states.Fortify;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.util.Button;
import peril.views.slick.util.Point;

/**
 * Encapsulates the behaviour of the 'Movement' state of the game. In this state
 * the {@link Game#getCurrent()} chooses which of their {@link SlickCountry}s
 * they will move units to another {@link SlickCountry}.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 *
 */
public final class MovementState extends CoreGameState {

	/**
	 * Holds the instance of the fortify {@link Button}.
	 */
	private final String fortifyButton;

	/**
	 * Hold the path between two {@link SlickCountry}s in the {@link MovementState}.
	 */
	private List<SlickCountry> path;

	/**
	 * Constructs a new {@link MovementState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link MovementState}.
	 * @param id
	 *            The ID of this {@link MovementState}.
	 * 
	 */
	public MovementState(GameController game, int id, Fortify model) {
		super(game, model.getName(), id, model);
		this.fortifyButton = "fortify";
		path = new LinkedList<>();

		model.addObserver(this);
	}

	/**
	 * Enters this {@link MovementState}
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.showSaveOption();
		getButton(fortifyButton).hide();
	}

	/**
	 * Render the {@link MovementState}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		super.render(gc, frame);

		frame.setLineWidth(3f);

		super.drawPlayerName(frame);
		super.drawAllLinks(frame);

		this.drawPath(frame);

		super.drawArmies(frame);
		super.drawImages();
		super.drawButtons();
		super.drawPlayerName(frame);

		super.drawMiniMap(frame);
		menus.draw(frame);

	}

	@Override
	public void update(GameContainer gc, int delta, Frame frame) {
		super.update(gc, delta, frame);
		game.processAI(delta);
	}

	/**
	 * Performs the exit state operations specific to this {@link MovementState}
	 */
	@Override
	public void leave(GameContainer gc, StateBasedGame game) throws SlickException {
		super.leave(gc, game);
		getButton(fortifyButton).hide();
		menus.hideSaveOption();
		path.clear();
	}

	/**
	 * Hide the fortify {@link Button}.
	 */
	public void hideFortifyButton() {
		getButton(fortifyButton).hide();
	}

	/**
	 * Draws the path between the primary and secondary {@link SlickCountry}s.
	 * 
	 * @param frame
	 *            {@link Graphics}
	 */
	public void drawPath(Frame frame) {

		Point previous = null;

		frame.setLineWidth(3f);
		// Assign the line colour.
		frame.setColor(Color.white);

		for (SlickCountry country : path) {

			Point current = super.getCenterArmyPosition(country);

			if (previous != null) {
				frame.drawLine(previous, current);
			}

			previous = current;
		}

	}

	/**
	 * Processes a mouse movement over this {@link MovementState}.
	 */
	@Override
	public void parseMouse(Point mousePosition) {
		super.parseMouse(mousePosition);
		this.checkPath(mousePosition);
	}

	/**
	 * Checks if there is a path between the {@link SlickCountry} the mouse is
	 * hovering over and the currently highlighted {@link SlickCountry}.
	 * 
	 * @param mousePosition
	 *            {@link Point} position of the mouse.
	 */
	public void checkPath(Point mousePosition) {

		final ModelCountry primary = model.getSelected(0);

		// If there is a highlighted country.
		if (primary != null && primary.getArmy().getNumberOfUnits() > 1) {

			// Holds the country the user is hovering over.
			SlickCountry target = slick.modelView.getVisual(game.getModelBoard()).getCountry(mousePosition);

			if (target != null) {

				path.clear();

				List<ModelCountry> path = ((Fortify) model).getPathBetween(primary, target.model,
						primary.getArmy().getWeakestUnit());

				path.forEach(country -> this.path.add(slick.modelView.getVisual(country)));
			}
		}
	}

	/**
	 * Moves the reinforce {@link Button} to be positioned at the top left of the
	 * current country.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 */
	private void moveFortifyButton(SlickCountry country) {

		Point armyPosition = getCenterArmyPosition(country);

		int x = armyPosition.x;
		int y = armyPosition.y + 25;

		getButton(fortifyButton).setPosition(new Point(x, y));
	}

	/**
	 * Moves the fortify button along the pan {@link Point} vector.
	 */
	@Override
	protected void panElements(Point panVector) {
		Point current = getButton(fortifyButton).getPosition();
		getButton(fortifyButton).setPosition(new Point(current.x + panVector.x, current.y + panVector.y));
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		// If there is no fortify button the game is not loaded or there is an error. In
		// either case there should be no full state update.
		if (getButton(fortifyButton) == null) {
			return;
		}

		if (selected.size() == 2 && selected.get(0).model.getArmy().getStrength() > 1) {
			getButton(fortifyButton).show();

			path.clear();
			
			moveFortifyButton(selected.get(1));
		} else {
			getButton(fortifyButton).hide();
			path.clear();
		}

	}
}
