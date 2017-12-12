package peril.ui.states.gameStates.multiSelectState;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
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
	 * Whether or not {@link CombatState} is currently after or before a country has
	 * been conquered.
	 */
	private boolean isPostCombat;

	/**
	 * The {@link WarMenu} that displays the combat of the game.
	 */
	public final WarMenu warMenu;

	/**
	 * Holds the instance of a attack {@link Button}.
	 */
	private Button attackButton;

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

		this.isPostCombat = false;
		this.warMenu = game.menus.warMenu;
	}
	
	/**
	 * Sets the state if this {@link CombatState} to after a {@link Country} has
	 * been conquered.
	 */
	public void setPostCombat() {
		isPostCombat = true;
	}

	/**
	 * Sets the state if this {@link CombatState} to before a {@link Country} has
	 * been conquered.
	 */
	public void setPreCombat() {
		isPostCombat = false;
	}

	/**
	 * Highlights a specified {@link Country}. If there is a friendly
	 * {@link Country} already highlighted then this will check if the
	 * {@link Country} is a valid target and allow highlighting accordingly.
	 */
	@Override
	public void highlightCountry(Country country) {

		if (isPostCombat) {
			highlightCountryPostCombat(country);
		} else {
			highlightCounrtyPreCombat(country);
		}

	}

	/**
	 * Performs the exit state operations specific to this {@link CombatState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		attackButton.hide();
		getGame().menus.pauseMenu.hideSaveOption();
		warMenu.clear();
	}

	/**
	 * Adds a {@link Button} to this {@link CombatState}.
	 */
	@Override
	public void addButton(Button button) {
		super.addButton(button);

		if (button.getId().equals("attack")) {
			attackButton = button;
			attackButton.hide();
		}
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
	 * Retrieves the {@link Country} the
	 * {@link CoreGameState#getHighlightedCountry()} will sent troops too when
	 * attacking.
	 * 
	 * @return {@link Country}
	 */
	public Country getEnemyCountry() {
		return super.getSecondaryHightlightedCounrty();
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
		attackButton.hide();
	}
	
	/**
	 * Pans this {@link CombatState}.
	 */
	@Override
	protected void pan(Point panVector) {

		Point old = attackButton.getPosition();

		Point vector = getGame().board.move(panVector);

		if (vector.x != 0 || vector.y != 0) {
			attackButton.setPosition(new Point(old.x + vector.x, old.y + vector.y));
		}

	}
	
	/**
	 * Processes whether a {@link Country} is a valid target for the
	 * {@link CoreGameState#getHighlightedCountry()} to attack. This is based on the
	 * {@link Player} ruler and the {@link Player} ({@link Game#getCurrent()})
	 * 
	 * @param country
	 *            {@link Country}
	 * @param player
	 *            {@link Player}
	 * @param ruler
	 *            {@link Player}
	 */
	protected void processCountry(Country country, Player player, Player ruler) {

		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (getHighlightedCountry() != null) {

			// if the country is a valid target of the primary country..
			if (isValidTarget(super.getHighlightedCountry(), country)) {

				super.removeHighlightFrom(super.getSecondaryHightlightedCounrty());
				super.setSecondaryHighlightedCountry(country);
				moveAttackButton(getArmyPosition(getHighlightedCountry()), getArmyPosition(country));
				attackButton.show();

			}
			// If the player owns the other country
			else if (player.equals(ruler)) {

				// Remove the highlight from the currently highlighted countries and highlight
				// the clicked country.
				super.removeHighlightFrom(super.getSecondaryHightlightedCounrty());
				super.removeHighlightFrom(super.getHighlightedCountry());
				super.highlightCountry(country);
				attackButton.hide();
			}

		}
		// If the player does not ruler the country
		else if (!player.equals(ruler)) {
			// DO NOTHING
		}
		// If the country clicked is to be the new primary country and is owned by the
		// player.
		else {

			// Remove the highlight from the currently highlighted countries and highlight
			// the clicked country.
			super.removeHighlightFrom(super.getSecondaryHightlightedCounrty());
			super.removeHighlightFrom(super.getHighlightedCountry());
			super.highlightCountry(country);
			attackButton.hide();
		}
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
	 * If a {@link Player} has not conquered a new {@link Country} since they last
	 * clicked a {@link Country}.
	 * 
	 * @param country
	 *            {@link Country} that was clicked.
	 */
	private void highlightCounrtyPreCombat(Country country) {

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().players.getCurrent();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			processCountry(country, player, ruler);

		} else {
			attackButton.hide();
			super.removeHighlightFrom(super.getSecondaryHightlightedCounrty());
			super.setSecondaryHighlightedCountry(null);
			super.removeHighlightFrom(super.getHighlightedCountry());
			super.highlightCountry(country);
		}
	}

	/**
	 * If a {@link Player} has not conquered a new {@link Country} since they last
	 * clicked a {@link Country}. Remove the highlight effect on both the primary
	 * and secondary country.
	 * 
	 * @param country
	 */
	private void highlightCountryPostCombat(Country country) {

		super.removeHighlightFrom(country);
		super.setSecondaryHighlightedCountry(null);
		super.removeHighlightFrom(getHighlightedCountry());
		super.highlightCountry(null);
		attackButton.hide();

		setPreCombat();
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
	 * Draws a line between the {@link CoreGameState#getHighlightedCountry()} and or
	 * all its valid targets.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawValidTargets(Graphics g) {

		Country highlighted = super.getHighlightedCountry();

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
	private void moveAttackButton(Point p1, Point p2) {

		int x = ((p2.x - p1.x) / 2) + p1.x;
		int y = ((p2.y - p1.y) / 2) + p1.y;

		attackButton.setPosition(new Point(x, y));

	}
}
