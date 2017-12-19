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
import peril.ai.AI;
import peril.board.Army;
import peril.board.Country;
import peril.ui.Button;
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
	private final String fortifyButton;

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
		this.fortifyButton = "fortify";
		path = new LinkedList<>();
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
	 * Render the {@link MovementState}.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);
		
		g.setLineWidth(3f);

		super.drawPlayerName(g);
		super.drawAllLinks(g);
		
		this.drawPath(g);
		
		super.drawArmies(g);
		super.drawImages(g);
		super.drawButtons(g);
		super.drawPlayerName(g);
		super.drawPopups(g);
		super.drawHelp(g);
		super.drawPauseMenu(g);
		super.drawChallengeMenu(g);
		
		g.destroy();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);

		if (getGame().players.getCurrent().ai != AI.USER && !getGame().players.getCurrent().ai.fortify(delta)) {
			getGame().enterState(getGame().states.reinforcement);
			getGame().players.nextPlayer();
		}
	}

	/**
	 * Performs the exit state operations specific to this {@link MovementState}
	 */
	@Override
	public void leave(GameContainer gc, StateBasedGame game) throws SlickException {
		super.leave(gc, game);
		getButton(fortifyButton).hide();
		getGame().menus.pauseMenu.hideSaveOption();
		path.clear();
	}

	/**
	 * Retrieves the {@link Country} the {@link CoreGameState#getSelected()} will
	 * sent troops too when fortify is clicked.
	 * 
	 * @return {@link Counrty}
	 */
	public Country getTargetCountry() {
		return super.getSecondary();
	}

	/**
	 * Hide the fortify {@link Button}.
	 */
	public void hideFortifyButton() {
		getButton(fortifyButton).hide();
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

		Country primary = getSelected();
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
					super.removeHighlight(target);
					super.setSecondary(null);
				}
			} else {
				// DO NOTHING
			}

		} else {
			// DO NOTHING
		}

	}

	/**
	 * Retrieves whether or not there is a path between two {@link Country}s.
	 * 
	 * @param start
	 *            {@link Country}
	 * @param target
	 *            {@link Country}
	 * @return
	 */
	public boolean isPathBetween(Country start, Country target) {
		return !getPathBetween(start, target).isEmpty();
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
		if (getPrimary() != null && getPrimary().getArmy().getSize() > 1) {

			// Holds the country the user is hovering over.
			Country target = getGame().board.getCountry(mousePosition);

			getPathBetween(getPrimary(), target).forEach(country -> path.add(country));
		}
	}

	/**
	 * Retrieves the path between two path between two {@link Country}s.
	 * 
	 * @param start
	 *            {@link Country}
	 * @param target
	 *            {@link Country}
	 * @return
	 */
	private Stack<Country> getPathBetween(Country start, Country target) {

		// Holds the path from the friendly country to the target country.
		Stack<Country> path = new Stack<Country>();

		// Holds all the traversed countries
		Set<Country> traversed = new HashSet<>();

		// Clear the current path.
		this.path.clear();

		// If the target belongs to the current player.
		if (target != null && target.getRuler() == start.getRuler()) {

			// If there is a path between the highlighted and the target add the points to
			// the drawn path.
			isPath(path, traversed, start, target);
		}

		return path;

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
	 * Moves the reinforce {@link Button} to be positioned at the top left of the
	 * current country.
	 * 
	 * @param country
	 *            {@link Country}
	 */
	private void moveFortifyButton(Country country) {

		Point armyPosition = getCenterArmyPosition(country);

		int x = armyPosition.x;
		int y = armyPosition.y + 25;

		getButton(fortifyButton).setPosition(new Point(x, y));
	}

	/**
	 * A {@link Country} is valid to be primary selected if:
	 * <ul>
	 * <li>It is <strong>NOT</strong> null.</li>
	 * <li>Has the same ruler as the current {@link Player}.</li>
	 * <li>There is not a current primary {@link Country} <strong>OR</strong> the
	 * specified {@link Country} is <strong>NOT</strong> a
	 * {@link MovementState#isValidLink(Country)}</li>
	 * </ul>
	 */
	@Override
	protected boolean selectPrimary(Country country) {

		if (country == null) {
			path.clear();
			return false;
		}

		// Holds the current player
		Player player = getGame().players.getCurrent();

		// Holds the ruler of the country
		Player ruler = country.getRuler();

		return getPrimary() == null && player.equals(ruler) && country.getArmy().getSize() > 1;

	}

	/**
	 * A {@link Country} is valid to be secondary selected if:
	 * <ul>
	 * <li>It is <strong>NOT</strong> null.</li>
	 * <li>Has the same ruler as the current {@link Player}.</li>
	 * <li>The specified {@link Country} is a
	 * {@link MovementState#isValidLink(Country)}</li>
	 * </ul>
	 */
	@Override
	protected boolean selectSecondary(Country country) {

		this.path.clear();
		
		if (country == null || getPrimary() == null) {
			getButton(fortifyButton).hide();
			return false;
		}

		// Holds the current player
		Player player = getGame().players.getCurrent();

		// Holds the ruler of the country
		Player ruler = country.getRuler();

		// The country is different to the primary and has the same ruler as the player.
		final boolean friendlyCountry = player.equals(ruler) && getPrimary() != country;

		if (friendlyCountry) {

			// The path between the current primary and the specified country.
			final Stack<Country> path = getPathBetween(getPrimary(), country);

			// If there is a path
			if (!path.isEmpty()) {

				// Display the new path.
				path.forEach(c -> this.path.add(c));

				// Show the fortify button
				moveFortifyButton(country);
				getButton(fortifyButton).show();

				return true;
			}

		}
		getButton(fortifyButton).hide();

		return false;

	}

	/**
	 * Moves the fortify button along the pan {@link Point} vector.
	 */
	@Override
	protected void panElements(Point panVector) {
		Point current = getButton(fortifyButton).getPosition();
		getButton(fortifyButton).setPosition(new Point(current.x + panVector.x, current.y + panVector.y));
	}

}
