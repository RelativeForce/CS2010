package peril.views.slick.states.gameStates.multiSelectState;

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
import peril.model.states.Attack;
import peril.views.slick.Button;
import peril.views.slick.Point;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of the 'Combat' state of the game. In this state
 * the {@link Game#getCurrent()} chooses which of their {@link SlickCountry}s
 * they will attack other {@link SlickCountry}s with.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public final class CombatState extends MultiSelectState {

	/**
	 * The name of a specific {@link CombatState}.
	 */
	private static final String STATE_NAME = "Combat";

	/**
	 * Holds the instance of a attack {@link Button}.
	 */
	private final String attackButton;

	/**
	 * Constructs a new {@link CombatState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link CombatState}.
	 * @param id
	 *            The ID of this {@link CombatState}
	 */
	public CombatState(GameController game, int id, Attack model) {
		super(game, STATE_NAME, id, model);
		this.attackButton = "attack";

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
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		super.render(gc, sbg, g);

		super.drawAllLinks(g);

		this.drawValidTargets(g);

		super.drawArmies(g);
		super.drawPlayerName(g);
		super.drawImages(g);
		super.drawButtons(g);
		super.drawPlayerName(g);
		super.drawPopups(g);

		menus.draw(g);

		g.destroy();
	}

	/**
	 * Updates this {@link CombatState}.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		game.processAI(delta);
		
	}
	
	@Override
	public void parseButton(int key, char c, Point mousePosition) {
		
		if(key == Input.KEY_B) {
			
			final ModelCountry primary = model.getSelected(0);
			final ModelCountry secondary = model.getSelected(1);
			
			if(primary != null && secondary != null) {
				
				secondary.getLinkTo(primary).setState(ModelLinkState.BLOCKADE, 3);
				
			}
		}
		
		
		super.parseButton(key, c, mousePosition);
	}

	/**
	 * Enters this {@link CombatState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.showSaveOption();
		getButton(attackButton).hide();
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
		Point current = getButton(attackButton).getPosition();
		getButton(attackButton).setPosition(new Point(current.x + panVector.x, current.y + panVector.y));
	}

	/**
	 * Draws a line between the {@link CoreGameState#getSelected()} and or all its
	 * valid targets.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawValidTargets(Graphics g) {

		// If there is a country highlighted.
		if (!selected.isEmpty()) {

			SlickCountry highlighted = selected.get(0);

			// Assign the line colour.
			g.setColor(Color.white);

			for (ModelCountry modelNeighbour : highlighted.model.getNeighbours()) {

				SlickCountry neighbour = slick.modelView.getVisual(modelNeighbour);

				// if it is a valid target highlight the country and draw a line from the
				// highlighted country to the neighbour country.
				if (((Attack) model).isValidTarget(highlighted.model, modelNeighbour)) {

					Point enemy = super.getCenterArmyPosition(neighbour);
					Point selected = super.getCenterArmyPosition(highlighted);

					g.drawLine(enemy.x, enemy.y + 5, selected.x, selected.y + 5);
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

		Point p1 = getCenterArmyPosition(primary);
		Point p2 = getCenterArmyPosition(target);
		int x = ((p2.x - p1.x) / 2) + p1.x;
		int y = ((p2.y - p1.y) / 2) + p1.y;

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

		if (selected.size() == 2 && selected.get(0).model.getArmy().getStrength() > 1) {
			getButton(attackButton).show();
			moveAttackButton(selected.get(0), selected.get(1));
		} else {
			getButton(attackButton).hide();
		}

	}
}
