package peril.ui.states.gameStates.multiSelectState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.ai.Player;
import peril.board.Country;
import peril.ui.Button;
import peril.ui.components.menus.WarMenu;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of the 'Combat' state of the game. In this state
 * the {@link Game#getCurrent()} chooses which of their {@link Country}s they
 * will attack other {@link Country}s with.
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
	public CombatState(Game game, int id) {
		super(game, STATE_NAME, id);
		this.attackButton = "attack";
		this.warMenu = game.menus.warMenu;
	}

	/**
	 * Performs the exit state operations specific to this {@link CombatState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		getButton(attackButton).hide();
		getGame().menus.pauseMenu.hideSaveOption();
		warMenu.clear();
	}

	/**
	 * Displays super classes render method then the current {@link Player}s name
	 * and then the enemy {@link Country}.
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
	}

	/**
	 * Updates this {@link CombatState}.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		// Hide the war menu if the pause menu is over it.
		if (getGame().menus.pauseMenu.isVisible()) {
			warMenu.hide();
		}
	}

	/**
	 * Enters this {@link CombatState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		getGame().menus.pauseMenu.showSaveOption();
	}

	/**
	 * Retrieves the {@link Country} the {@link CoreGameState#getSelected()} will
	 * sent troops too when attacking.
	 * 
	 * @return {@link Country}
	 */
	public Country getEnemyCountry() {
		return super.getSecondary();
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
	 * Clears the highlight from the current primary and secondary {@link Country}s.
	 */
	public void clear() {
		removeHighlight(getPrimary());
		removeHighlight(getSecondary());
		hideAttackButton();
		removeSelected();
	}

	/**
	 * The specified {@link Country} is a valid primary {@link Country} if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is ruled by the current player.</li>
	 * </ul>
	 */
	@Override
	protected boolean selectPrimary(Country country) {
		return country != null && getGame().players.getCurrent().equals(country.getRuler());
	}

	/**
	 * The specified {@link Country} is a valid primary {@link Country} if:
	 * <ul>
	 * <li>Is <strong>NOT</strong> null</li>
	 * <li>It is a valid target of the primary {@link Country}.</li>
	 * </ul>
	 */
	@Override
	protected boolean selectSecondary(Country country) {

		if (country == null) {
			getButton(attackButton).hide();
			return false;
		}

		final boolean selectableSecondary = getPrimary() != null && isValidTarget(getPrimary(), country);

		if (selectableSecondary) {
			moveAttackButton(getPrimary(), country);
			getButton(attackButton).show();
		} else {
			getButton(attackButton).hide();
		}

		return selectableSecondary;
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
	 * Retrieves whether or not the secondary {@link Country} is a valid targets for
	 * the primary {@link Country}.
	 * 
	 * @param primaryCountry
	 *            is the primary {@link Country}
	 * @param secondaryTarget
	 *            is the second {@link Country}
	 * @return <code>boolean</code> if it is a valid target.
	 */
	private boolean isValidTarget(Country primaryCountry, Country secondaryTarget) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (!primaryCountry.getRuler().equals(secondaryTarget.getRuler())) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (primaryCountry.isNeighbour(secondaryTarget)) {

				// If the army size of the primary country is greater than 1.
				if (primaryCountry.getArmy().getSize() > 1) {
					return true;
				}

			}

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

		Country highlighted = super.getSelected();

		// If there is a country highlighted.
		if (highlighted != null) {

			// Assign the line colour.
			g.setColor(Color.white);

			for (Country country : highlighted.getNeighbours()) {

				// if it is a valid target highlight the country and draw a line from the
				// highlighted country to the neighbour country.
				if (isValidTarget(highlighted, country)) {

					Point enemy = super.getCenterArmyPosition(country);
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
	 *            {@link Country}
	 */
	private void moveAttackButton(Country primary, Country target) {

		Point p1 = getCenterArmyPosition(primary);
		Point p2 = getCenterArmyPosition(target);
		int x = ((p2.x - p1.x) / 2) + p1.x;
		int y = ((p2.y - p1.y) / 2) + p1.y;

		getButton(attackButton).setPosition(new Point(x, y));

	}

}
