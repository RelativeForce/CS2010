package peril.ui.states.gameStates.multiSelectState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Army;
import peril.board.Country;
import peril.ui.Button;
import peril.ui.components.menus.Help;
import peril.ui.states.gameStates.CoreGameState;

/**
 * Encapsulates the behaviour of the 'Movement' state of the game. In this state
 * the {@link Game#getCurrent()} chooses which of their {@link Country}s they
 * will move units to another {@link Country}.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 *
 */
public final class MovementState extends MultiSelectState {

	/**
	 * The name of a specific {@link MovementState}.
	 */
	private static final String STATE_NAME = "Movement";

	/**
	 * Holds the instance of the fortify {@link Button}.
	 */
	private Button fortifyButton;

	/**
	 * Hold the path between two {@link Country}s in the {@link MovementState}.
	 */
	private List<Country> path;

	/**
	 * Constructs a new {@link MovementState}.
	 * 
	 * @param game
	 *            The {@link Game} that houses this {@link MovementState}.
	 * @param id
	 *            The ID of this {@link MovementState}.
	 * 
	 */
	public MovementState(Game game, int id) {
		super(game, STATE_NAME, id);

		path = new LinkedList<>();
	}

	/**
	 * Highlights a specified {@link Country} if it is friendly or a
	 * {@link Country#getNeighbours()} of the
	 * {@link CoreGameState#getHighlightedCountry()}..
	 */
	@Override
	public void highlightCountry(Country country) {

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().players.getCurrent();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			processCountry(country, player, ruler);

		} else {
			fortifyButton.hide();
			super.removeHighlightFrom(super.getSecondaryHightlightedCounrty());
			super.setSecondaryHighlightedCountry(null);
			super.removeHighlightFrom(super.getHighlightedCountry());
			super.highlightCountry(country);
			path.clear();
		}

	}
	
	@Override
	public Help getHelp() {
		// TODO Hake help
		return null;
	}

	/**
	 * Enters this {@link MovementState}
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		getGame().menus.pauseMenu.showSaveOption();
	}

	/**
	 * Adds a {@link Button} to this {@link MovementState}.
	 */
	@Override
	public void addButton(Button button) {
		super.addButton(button);

		if (button.getId().equals("fortify")) {
			fortifyButton = button;
			fortifyButton.hide();
		}
	}

	/**
	 * Render the {@link MovementState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
		g.setLineWidth(3f);

		super.drawPlayerName(g);

		if (path.isEmpty())
			this.drawValidTargets(g);

		this.drawPath(g);

		super.drawArmies(g);

		super.drawImages(g);
		
		super.drawButtons(g);
		
		super.drawPlayerName(g);

		super.drawPauseMenu(g);
	}

	/**
	 * Performs the exit state operations specific to this {@link MovementState}
	 */
	@Override
	public void leave(GameContainer gc, StateBasedGame game) throws SlickException {
		super.leave(gc, game);
		fortifyButton.hide();
		getGame().menus.pauseMenu.hideSaveOption();
	}

	/**
	 * Retrieves the {@link Country} the
	 * {@link CoreGameState#getHighlightedCountry()} will sent troops too when
	 * fortify is clicked.
	 * 
	 * @return {@link Counrty}
	 */
	public Country getTargetCountry() {
		return super.getSecondaryHightlightedCounrty();
	}

	/**
	 * Hide the fortify {@link Button}.
	 */
	public void hideFortifyButton() {
		fortifyButton.hide();
	}

	/**
	 * Draws the path between the primary and secondary {@link Country}s.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	public void drawPath(Graphics g) {

		Point previous = null;

		g.setLineWidth(3f);
		// Assign the line colour.
		g.setColor(Color.white);

		for (Country country : path) {

			Point current = super.getCenterArmyPosition(country);

			if (previous != null) {
				g.drawLine(previous.x, previous.y, current.x, current.y);
			}

			previous = current;
		}

	}

	/**
	 * Processes a mouse movement over this {@link MovementState}.
	 */
	@Override
	public void parseMouse(Point mousePosition) {
		this.checkPath(mousePosition);
		super.parseMouse(mousePosition);
	}

	/**
	 * Moves one unit from the primary {@link Country} to the secondary
	 * {@link Country}.
	 */
	public void fortify() {

		Country primary = getHighlightedCountry();
		Country target = getTargetCountry();

		// If there is two countries highlighted
		if (primary != null && target != null) {

			// If the army of the primary highlighted country is larger that 1 unit in size
			if (primary.getArmy().getSize() > 1) {

				// Holds the army of the primary country
				Army primaryArmy = primary.getArmy();

				// Holds the army of the target country
				Army targetArmy = target.getArmy();

				// Move the unit.
				targetArmy.setSize(targetArmy.getSize() + 1);
				primaryArmy.setSize(primaryArmy.getSize() - 1);

				if (primaryArmy.getSize() == 1) {
					hideFortifyButton();
					path.clear();
					super.removeHighlightFrom(target);
					super.setSecondaryHighlightedCountry(null);
				}
			} else {
				// DO NOTHING
			}

		} else {
			// DO NOTHING
		}

	}

	/**
	 * Checks if there is a path between the {@link Country} the mouse is hovering
	 * over and the currently highlighted {@link Country}.
	 * 
	 * @param mousePosition
	 *            {@link Point} position of the mouse.
	 */
	public void checkPath(Point mousePosition) {

		// If there is a highlighted country.
		if (getHighlightedCountry() != null && getHighlightedCountry().getArmy().getSize() > 1) {

			// Holds the currently highlighted country
			Country highligthed = getHighlightedCountry();

			// Holds the country the user is hovering over.
			Country target = getGame().board.getCountry(mousePosition);

			// If the target belongs to the current player.
			if (target != null && target.getRuler() == highligthed.getRuler()) {

				// Holds the path from the friendly country to the target country.
				Stack<Country> path = new Stack<Country>();

				// Holds all the traversed countries
				Set<Country> traversed = new HashSet<>();

				// Clear the current path.
				this.path.clear();

				// If there is a path between the highlighted and the target add the points to
				// the drawn path.
				if (isPath(path, traversed, highligthed, target)) {
					path.forEach(country -> this.path.add(country));
				}
			}
		}
	}

	/**
	 * Pans this {@link MovementState}.
	 */
	@Override
	protected void pan(Point panVector) {

		Point old = fortifyButton.getPosition();

		Point vector = getGame().board.move(panVector);

		if (vector.x != 0 || vector.y != 0) {
			fortifyButton.setPosition(new Point(old.x + vector.x, old.y + vector.y));
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

		Country highlighted = getHighlightedCountry();
		
		// If there is a primary friendly country and the target is not null and the
		// ruler of the country is not the player.
		if (highlighted != null && player.equals(ruler)) {

			// if the country is a neighbour of the primary highlighted country then it is a
			// valid target.
			if (isValidTarget(highlighted, country) || ((path.contains(country)) && highlighted != country)) {

				super.removeHighlightFrom(super.getSecondaryHightlightedCounrty());
				super.setSecondaryHighlightedCountry(country);
				moveFortifyButton(country);
				fortifyButton.show();

			} else {
				// DO NOTHING
			}

		}
		// If the country clicked is to be the new primary country but is not ruler by
		// the player
		else if (!player.equals(ruler)) {
			// DO NOTHING
		}
		// If the country clicked is to be the new primary country and is owned by the
		// player.
		else {
			fortifyButton.hide();
			super.removeHighlightFrom(super.getSecondaryHightlightedCounrty());
			super.highlightCountry(country);
		}
	}

	/**
	 * This method will using a set of traversed nodes recursively perform a depth
	 * first search from one node to another. The children of each node are added to
	 * the
	 * 
	 * @param travsersed
	 *            {@link Set} of {@link Country} that have been traversed.
	 * @param country
	 *            {@link Country} currently being checked.
	 * @param target
	 *            {@link Country} the is to be reached.
	 * @return Whether the current {@link Country} is on the path to the target
	 *         {@link Country}.
	 */
	private boolean isPath(Stack<Country> path, Set<Country> travsersed, Country current, Country traget) {

		// Add the current country to the path
		path.push(current);

		// Holds the children of the current country that have the same ruler.
		Set<Country> validChildren = new HashSet<>();

		// Iterate through all the neighbours that the current country has
		for (Country country : current.getNeighbours()) {

			/*
			 * If the target country is a neighbour of the current country add it to the
			 * path then return true. This will result in all the parents of the current
			 * node to return true also.
			 */
			if (country.equals(traget)) {
				path.push(country);
				return true;
			}

			// If the country has not already been traversed and has the same ruler.
			if (!travsersed.contains(country) && current.getRuler() == country.getRuler()) {
				validChildren.add(country);
				travsersed.add(country);
			}

		}

		/*
		 * If there are no valid children then this path is a dead end. Due to this pop
		 * the current country from the path and return false.
		 */
		if (validChildren.isEmpty()) {
			path.pop();
			return false;
		}

		/*
		 * Iterate through each valid child and if the child is a part of the path
		 * return true.
		 */
		for (Country child : validChildren) {
			if (isPath(path, travsersed, child, traget)) {
				return true;
			}
		}

		/*
		 * This will only be performed if the non of the children have valid reached the
		 * target. There for the current country is not on the path to the target.
		 */
		path.pop();
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
		// ruler of the country is the player.
		if (primaryCountry.getRuler().equals(secondaryTarget.getRuler())) {

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

		// If there is a country highlighted.
		if (super.getHighlightedCountry() != null) {

			// Assign the line colour.
			g.setColor(Color.white);

			for (Country country : super.getHighlightedCountry().getNeighbours()) {

				// if it is a valid target highlight the country and draw a line from the
				// highlighted country to the neighbour country.
				if (isValidTarget(super.getHighlightedCountry(), country)) {

					Point enemy = super.getArmyPosition(country);
					Point selected = super.getArmyPosition(super.getHighlightedCountry());

					g.drawLine(enemy.x + (getOvalWidth(country.getArmy().getSize()) / 2), enemy.y + 10,
							selected.x + (getOvalWidth(super.getHighlightedCountry().getArmy().getSize()) / 2),
							selected.y + 10);
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
	private void moveFortifyButton(Country country) {

		Point armyPosition = getArmyPosition(country);

		int x = armyPosition.x;
		int y = armyPosition.y + 25;

		fortifyButton.setPosition(new Point(x, y));

	}

	

}
