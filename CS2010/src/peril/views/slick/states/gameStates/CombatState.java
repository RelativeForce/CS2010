package peril.views.slick.states.gameStates;

import java.util.Observable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.GameController;
import peril.model.board.ModelCountry;
import peril.model.board.links.ModelLinkState;
import peril.model.states.combat.Attack;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.util.Button;
import peril.views.slick.util.Point;

/**
 * Encapsulates the behaviour of the 'Combat' state of the game. In this state
 * the {@link Game#getCurrent()} chooses which of their {@link SlickCountry}s
 * they will attack other {@link SlickCountry}s with.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public final class CombatState extends CoreGameState {

	/**
	 * Holds the instance of a attack {@link Button}.
	 */
	private final String attackButton;
	private String upgradeButton;

	/**
	 * Constructs a new {@link CombatState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link CombatState}.
	 * @param id
	 *            The ID of this {@link CombatState}
	 */
	public CombatState(GameController game, int id, Attack model) {
		super(game, model.getName(), id, model);
		this.attackButton = "attack";
		this.upgradeButton = "upgrades";
		model.addObserver(this);

	}

	/**
	 * Performs the exit state operations specific to this {@link CombatState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		getButton(attackButton).hide();
		menus.hideSaveOption();
		menus.clearMenus();
	}

	/**
	 * Displays super classes render method then the current {@link SlickPlayer}s
	 * name and then the enemy {@link SlickCountry}.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {

		super.render(gc, frame);

		super.drawAllLinks(frame);

		this.drawValidTargets(frame);

		super.drawArmies(frame);
		super.drawPlayerName(frame);
		super.drawImages();
		super.drawButtons();
		super.drawPlayerName(frame);
		super.drawMiniMap(frame);

		menus.draw(frame);

	}

	/**
	 * Updates this {@link CombatState}.
	 */
	@Override
	public final void update(GameContainer gc, int delta, Frame frame) {
		super.update(gc, delta, frame);
		game.processAI(delta);

	}

	@Override
	public void parseButton(int key, Point mousePosition) {
		super.parseButton(key, mousePosition);

		if (key == Input.KEY_B) {

			final ModelCountry primary = model.getSelected(0);
			final ModelCountry secondary = model.getSelected(1);

			if (primary != null && secondary != null) {

				secondary.getLinkTo(primary).setState(ModelLinkState.BLOCKADE, 3);

			}
		}
	}

	/**
	 * Enters this {@link CombatState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.showSaveOption();
		getButton(attackButton).hide();
		getButton(upgradeButton).hide();
		slick.showToolTip("Click the '?' for help");
	}

	/**
	 * Hide the attack button.
	 */
	public void hideAttackButton() {
		getButton(attackButton).hide();
	}

	/**
	 * Pans the visual elements of the {@link CombatState} along a specified
	 * {@link Point} vector.
	 */
	@Override
	protected void panElements(Point panVector) {
		panButton(getButton(attackButton), panVector);
		panButton(getButton(upgradeButton), panVector);
	}

	/**
	 * Draws a line between the {@link CoreGameState#getSelected()} and or all its
	 * valid targets.
	 * 
	 * @param frame
	 *            {@link Graphics}
	 */
	private void drawValidTargets(Frame frame) {

		// If there is a country highlighted.
		if (!selected.isEmpty()) {

			SlickCountry highlighted = selected.get(0);

			// Assign the line colour.
			frame.setColor(Color.white);

			for (ModelCountry modelNeighbour : highlighted.model.getNeighbours()) {

				final SlickCountry neighbour = slick.modelView.getVisual(modelNeighbour);

				// if it is a valid target highlight the country and draw a line from the
				// highlighted country to the neighbour country.
				if (((Attack) model).isValidTarget(highlighted.model, modelNeighbour)) {

					final Point enemy = neighbour.getArmyPosition();
					final Point selected = highlighted.getArmyPosition();

					frame.drawLine(enemy, selected);
				}
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
	private void moveAttackButton(SlickCountry primary, SlickCountry target) {

		final Point p1 = primary.getArmyPosition();
		final Point p2 = target.getArmyPosition();
		final int x = ((p2.x - p1.x) / 2) + p1.x;
		final int y = ((p2.y - p1.y) / 2) + p1.y;

		getButton(attackButton).setPosition(new Point(x, y));

	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		// If there is no attack Button the game is not loaded or there is an error. In
		// either case there should be no full state update.
		if (getButton(attackButton) == null) {
			return;
		}

		// If this is the primary country
		if (selected.size() > 0) {
			showUpgradeButton(getButton(upgradeButton));
		} else {
			getButton(upgradeButton).hide();
		}

		if (selected.size() == 2 && selected.get(0).model.getArmy().getStrength() > 1) {
			getButton(attackButton).show();
			moveAttackButton(selected.get(0), selected.get(1));
		} else {
			getButton(attackButton).hide();
		}

	}
}
