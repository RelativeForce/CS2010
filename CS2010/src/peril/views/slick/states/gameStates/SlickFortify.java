package peril.views.slick.states.gameStates;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.GameController;
import peril.model.board.ModelCountry;
import peril.model.board.ModelUnit;
import peril.model.states.Fortify;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.util.Point;

/**
 * Displays the {@link Fortify} state to the user.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 * @since 2018-02-27
 * @version 1.01.02
 * 
 * @see Fortify
 * @see CoreGameState
 *
 */
public final class SlickFortify extends CoreGameState {

	/**
	 * Holds the name of the fortify button.
	 */
	private final String fortifyButton;

	/**
	 * Holds the name of the upgrade button.
	 */
	private final String upgradeButton;

	/**
	 * Hold the path between two {@link SlickCountry}s in the {@link SlickFortify}.
	 */
	private final List<SlickCountry> path;

	/**
	 * Constructs a new {@link SlickFortify}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link SlickFortify}.
	 * @param id
	 *            The ID of this {@link SlickFortify}.
	 * @param model
	 *            The {@link Fortify} state this {@link SlickFortify} displays.
	 * 
	 */
	public SlickFortify(GameController game, int id, Fortify model) {
		super(game, model.getName(), id, model);

		this.fortifyButton = "fortify";
		this.upgradeButton = "upgrades";
		this.path = new LinkedList<>();

		model.addObserver(this);
	}

	/**
	 * Enters this {@link SlickFortify}
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.showSaveOption();
		getButton(fortifyButton).hide();
		getButton(upgradeButton).hide();
		
		if(game.getRoundNumber() == 0) {
			slick.showToolTip("Click '?' for help");
		}
		
	}

	/**
	 * Render the {@link SlickFortify}.
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

	/**
	 * Updates this {@link SlickFortify}.
	 */
	@Override
	public void update(GameContainer gc, int delta, Frame frame) {
		super.update(gc, delta, frame);
		game.processAI(delta);
	}

	/**
	 * Performs the exit state operations specific to this {@link SlickFortify}
	 */
	@Override
	public void leave(GameContainer gc, StateBasedGame game) throws SlickException {
		super.leave(gc, game);
		getButton(fortifyButton).hide();
		menus.hideSaveOption();
		path.clear();
	}

	/**
	 * Hide the fortify button.
	 */
	public void hideFortifyButton() {
		getButton(fortifyButton).hide();
	}

	/**
	 * Draws the path between the primary and secondary {@link SlickCountry}s.
	 * 
	 * @param frame
	 *            The {@link Frame} which displays {@link SlickFortify} to the user.
	 */
	public void drawPath(Frame frame) {

		Point previous = null;

		frame.setLineWidth(3f);
		// Assign the line colour.
		frame.setColor(Color.white);

		for (SlickCountry country : path) {

			Point current = country.getArmyPosition();

			if (previous != null) {
				frame.drawLine(previous, current);
			}

			previous = current;
		}

	}

	/**
	 * Processes a mouse movement over this {@link SlickFortify}.
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
	 * Updates this {@link SlickFortify} when its {@link Observable}s have changed.
	 */
	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		// If there is no fortify button the game is not loaded or there is an error. In
		// either case there should be no full state update.
		if (getButton(fortifyButton) == null) {
			return;
		}

		// If this is the primary country
		if (selected.size() > 0) {
			showUpgradeButton(getButton(upgradeButton));
		} else {
			getButton(upgradeButton).hide();
		}

		// If there are two countries selected there must be a path between them.
		if (selected.size() == 2 && selected.get(0).model.getArmy().getNumberOfUnits() > 1) {
			getButton(fortifyButton).show();

			if (!path.isEmpty()) {
				path.clear();
			}

			final Fortify fortify = (Fortify) model;
			final ModelCountry primary = fortify.getPrimary();
			final ModelCountry target = fortify.getSecondary();
			final ModelUnit strongest = primary.getArmy().getStrongestUnit();

			// Retrieve the path between the two selected countries then add the countries
			// in the model path to the drawn path.
			fortify.getPathBetween(primary, target, strongest).forEach(country -> {

				final SlickCountry slickCountry = slick.modelView.getVisual(country);
				path.add(slickCountry);

			});

			moveFortifyButton(selected.get(1));
		} else {
			getButton(fortifyButton).hide();
			path.clear();
		}

	}

	/**
	 * Moves the fortify button along the pan {@link Point} vector.
	 */
	@Override
	protected void panElements(Point panVector) {
		panButton(getButton(fortifyButton), panVector);
		panButton(getButton(upgradeButton), panVector);
	}

	/**
	 * Moves the fortify button to be positioned at the top left of the current
	 * country.
	 * 
	 * @param country
	 *            The {@link SlickCountry} the fortify button will be positioned
	 *            over.
	 */
	private void moveFortifyButton(SlickCountry country) {

		Point armyPosition = country.getArmyPosition();

		int x = armyPosition.x;
		int y = armyPosition.y + 25;

		getButton(fortifyButton).setPosition(new Point(x, y));
	}

}
