package peril.views.slick.states.gameStates.multiSelectState;

import java.util.Observable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.ai.AI;
import peril.controllers.GameController;
import peril.model.board.ModelCountry;
import peril.model.states.Attack;
import peril.views.slick.Button;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.components.menus.WarMenu;
import peril.views.slick.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of the 'Combat' state of the game. In this state
 * the {@link Game#getCurrent()} chooses which of their {@link SlickCountry}s they
 * will attack other {@link SlickCountry}s with.
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
	 * The {@link WarMenu} that displays the combat of the game.
	 */
	private final WarMenu warMenu;

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
		this.warMenu = slick.menus.warMenu;
		
		model.addObserver(this);
		
	}

	/**
	 * Performs the exit state operations specific to this {@link CombatState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		getButton(attackButton).hide();
		slick.menus.pauseMenu.hideSaveOption();
		warMenu.clear();
	}

	/**
	 * Displays super classes render method then the current {@link SlickPlayer}s name
	 * and then the enemy {@link SlickCountry}.
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

		this.warMenu.draw(g);

		super.drawHelp(g);
		super.drawPauseMenu(g);
		super.drawChallengeMenu(g);
		
		g.destroy();
	}

	/**
	 * Updates this {@link CombatState}.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		if(game.getCurrentModelPlayer().ai != AI.USER && !game.getCurrentModelPlayer().ai.attack(delta)) {
			slick.enterState(slick.states.movement);
		}
		
		// Hide the war menu if the pause menu is over it.
		if (slick.menus.pauseMenu.isVisible()) {
			warMenu.hide();
		}
	}

	/**
	 * Enters this {@link CombatState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		slick.menus.pauseMenu.showSaveOption();
	}

	/**
	 * Processes a click at a specified {@link Point} on this {@link CombatState}.
	 */
	@Override
	public void parseClick(int button, Point click) {
		if (!clickedWarMenu(click)) {
			super.parseClick(button, click);
		}
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
	 * Parses a click on the {@link WarMenu} and retrieves whether the
	 * {@link WarMenu} was clicked or not.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return <code>boolean</code>
	 */
	private boolean clickedWarMenu(Point click) {
		if (warMenu.isVisible()) {
			if (warMenu.isClicked(click)) {
				warMenu.parseClick(click);
				return true;
			}
			warMenu.hide();
		}
		return false;
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

				SlickCountry neighbour = slick.modelView.getVisualCountry(modelNeighbour);

				
				// if it is a valid target highlight the country and draw a line from the
				// highlighted country to the neighbour country.
				if (((Attack) model).isValidTarget(highlighted.model, modelNeighbour)) {

					Point enemy = super.getCenterArmyPosition(neighbour);
					Point selected = super.getCenterArmyPosition(highlighted);

					g.drawLine(enemy.x, enemy.y, selected.x, selected.y);
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
	
		if(selected.size() == 2) {
			getButton(attackButton).show();
			moveAttackButton(selected.get(0), selected.get(1));
		}else {
			getButton(attackButton).hide();
		}

	}
}
