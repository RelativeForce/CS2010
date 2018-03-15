package peril.views.slick.states.gameStates;

import java.util.Observable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.controllers.GameController;
import peril.model.board.ModelCountry;
import peril.model.states.Attack;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.components.menus.WarMenu;
import peril.views.slick.util.Button;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;

/**
 * Displays the {@link Attack} state to the user.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 * 
 * @since 2018-02-27
 * @version 1.01.02
 * 
 * @see CoreGameState
 * @see Attack
 * @see WarMenu
 *
 */
public final class SlickAttack extends CoreGameState {

	/**
	 * The name of the attack button.
	 */
	private final String attackButton;

	/**
	 * The name of the upgrade buton.
	 */
	private final String upgradeButton;

	/**
	 * Constructs a new {@link SlickAttack}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows this {@link SlickAttack} to
	 *            query the state of the game.
	 * @param id
	 *            The ID of this {@link SlickAttack}
	 * @param model
	 *            The {@link Attack} state this {@link SlickAttack} displays to the
	 *            user.
	 */
	public SlickAttack(GameController game, int id, Attack model) {
		super(game, model.getName(), id, model);
		this.attackButton = "attack";
		this.upgradeButton = "upgrades";
		model.addObserver(this);

	}

	/**
	 * Performs the exit state operations specific to this {@link SlickAttack}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		getButton(attackButton).hide();
		menus.hideSaveOption();
		menus.clearMenus();
	}

	/**
	 * Draws this {@link SlickAttack} on screen.
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
	 * Updates this {@link SlickAttack}.
	 */
	@Override
	public final void update(GameContainer gc, int delta, Frame frame) {
		super.update(gc, delta, frame);
		game.processAI(delta);

	}

	/**
	 * Enters this {@link SlickAttack}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);

		hideAttackButton();
		super.getButton(upgradeButton).hide();

		menus.showSaveOption();
		
		if(game.getRoundNumber() == 0) {
			slick.showToolTip("Click the '?' for help");
		}
		
	}

	/**
	 * Hide the attack button.
	 */
	public void hideAttackButton() {
		getButton(attackButton).hide();
	}

	/**
	 * Pans the visual elements of the {@link SlickAttack} along a specified
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
	 *            The {@link Frame} that displays this {@link SlickAttack} to the
	 *            user.
	 */
	private void drawValidTargets(Frame frame) {

		// If there is a country highlighted.
		if (!selected.isEmpty()) {

			final SlickCountry highlighted = selected.get(0);

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
	 * Moves the attack button to be positioned at the in between of the two
	 * specified countries.
	 * 
	 * @param primary
	 *            {@link SlickCountry}
	 * @param target
	 *            {@link SlickCountry}
	 */
	private void moveAttackButton(SlickCountry primary, SlickCountry target) {
		
		final Point p1 = primary.getArmyPosition();
		final Point p2 = target.getArmyPosition();
		
		final Button attack = getButton(attackButton);
		final Button upgrade = getButton(upgradeButton);
		
		final int x = ((p2.x - p1.x) / 2) + p1.x - (attack.getWidth() / 2);
		final int y = ((p2.y - p1.y) / 2) + p1.y - (attack.getHeight() / 2);

		// Set the attack button with its new position
		attack.setPosition(new Point(x, y));
		
		// If the attack button at its new position overlaps 
		if(Region.overlap(attack.getRegion(), upgrade.getRegion() )) {
			
			final int newX = upgrade.getPosition().x + upgrade.getWidth();
			final int newY = upgrade.getPosition().y;

			attack.setPosition(new Point(newX , newY));
		}
		
	

	}

	/**
	 * Updates this {@link SlickAttack} when its {@link Observable}s are changed.
	 */
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

		if (selected.size() == 2 && selected.get(0).model.getArmy().getNumberOfUnits() > 1) {
			getButton(attackButton).show();
			moveAttackButton(selected.get(0), selected.get(1));
		} else {
			getButton(attackButton).hide();
		}

	}
}
