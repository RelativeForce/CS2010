package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.helpers.UnitHelper;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;
import peril.views.slick.Font;
import peril.views.slick.Point;

/**
 * The slick view for {@link ModelArmy}. This observes the {@link ModelArmy} and
 * will update upon model changes.
 * 
 * @author Joshua_Eddy
 *
 */
public final class SlickArmy implements Observer {

	/**
	 * The {@link ModelArmy} this {@link SlickArmy} will observe and display.
	 */
	public final ModelArmy model;

	/**
	 * Whether of not this {@link SlickArmy} will be displayed in its expanded view
	 * or not.
	 */
	private boolean expanded;

	/**
	 * The {@link Font} that will be used to display the collapsed strength of the
	 * {@link SlickArmy}.
	 */
	private final Font collapsedFont;

	/**
	 * The {@link Font} that will be used to display the number of each unit in this
	 * {@link SlickArmy}.
	 */
	private final Font expandedFont;

	/**
	 * Constructs a new {@link SlickArmy} using an {@link ModelArmy}.
	 * 
	 * @param model
	 */
	public SlickArmy(ModelArmy model) {

		this.model = model;
		this.expanded = false;
		this.collapsedFont = new Font("Arial", Color.white, 17);
		this.expandedFont = new Font("Arial", Color.black, 10);

		model.addObserver(this);
	}

	/**
	 * Updates this {@link SlickArmy} from the {@link Model}.
	 */
	@Override
	public void update(Observable o, Object arg) {
		// Army is just a number
	}

	/**
	 * Causes this {@link SlickArmy} to be displayed in its expanded view.
	 */
	public void expand() {
		expanded = true;
	}

	/**
	 * Causes this {@link SlickArmy} to be displayed in its collapsed view.
	 */
	public void collapse() {
		expanded = false;
	}

	/**
	 * Draws this {@link SlickArmy} at a position on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param position
	 *            {@link Point}
	 * @param ruler
	 *            {@link SlickPlayer}
	 */
	public void draw(Graphics g, Point position, SlickPlayer ruler) {

		collapsedFont.init();
		expandedFont.init();

		if (expanded) {
			drawExpanded(g, position, ruler);
		} else {
			drawCollapsed(g, position, ruler);
		}

	}

	/**
	 * Retrieves the width of the oval that will be displayed behind the army of a
	 * country.
	 * 
	 * @param armySize
	 *            <code>int</code> size of the army
	 * @return <code>int</code> width of oval in pixels.
	 */
	public int getOvalWidth(int armySize) {
		return (((int) Math.log10(armySize)) + 1) * 15;
	}

	/**
	 * Draws this {@link SlickArmy} in it expanded view which shows all the
	 * different types of unit in this {@link SlickArmy} and how many of them there
	 * are.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param position
	 *            {@link Point}
	 * @param ruler
	 *            {@link SlickPlayer}
	 */
	private void drawExpanded(Graphics g, Point position, SlickPlayer ruler) {

		final UnitHelper units = UnitHelper.getInstance();

		int x = position.x;
		int y = position.y;

		ModelUnit current = units.getStrongest();

		// Iterate until there are not more types of unit.
		while (current != null) {

			// If the unit exists in the model army draw it
			if (model.hasUnit(current)) {

				drawUnits(g, new Point(x, y), current);

				x += 15;
			}

			current = units.getUnitBelow(current);
		}

	}

	/**
	 * Draws this {@link SlickArmy} in it collapsed view which just shows the
	 * strength of the army.
	 * 
	 * @param g
	 *            {@link Graphics}
	 * @param position
	 *            {@link Point}
	 * @param ruler
	 *            {@link SlickPlayer}
	 */
	private void drawCollapsed(Graphics g, Point position, SlickPlayer ruler) {

		// Draw a background oval with the rulers colour. If no ruler found default to
		// light grey.
		if (ruler != null) {
			g.setColor(ruler.color);
		} else {
			g.setColor(Color.lightGray);
		}

		// Holds the size of the current countries army
		int troopNumber = model.getStrength();

		drawArmyOval(position, troopNumber, g);

		// Draw a string representing the number of troops
		// within that army at (x,y).
		collapsedFont.draw(g, Integer.toString(troopNumber), position.x, position.y);

	}

	/**
	 * Draws the oval that is displayed behind the {@link ModelArmy} on a
	 * {@link SlickCountry}.
	 * 
	 * @param position
	 *            {@link Point}
	 * @param size
	 *            size of the {@link ModelArmy}
	 * @param g
	 *            {@link Graphics}
	 */
	private void drawArmyOval(Point position, int size, Graphics g) {

		int width = getOvalWidth(size);

		int offset = width / 5;

		g.fillOval(position.x - offset, position.y - 3, width, 25);

	}

	/**
	 * Draws a {@link ModelUnit} on screen at a specified {@link Point} position.
	 * 
	 * @param position
	 *            {@link Point}
	 * @param unit
	 *            {@link ModelUnit}
	 * @param g
	 *            {@link Graphics}
	 * 
	 */
	private void drawUnits(Graphics g, Point position, ModelUnit unit) {

		final int numberOfCurrent = model.getUnit(unit);

		g.setColor(Color.lightGray);
		g.fillRect(position.x, position.y, 15, 15);

		expandedFont.draw(g, Integer.toString(numberOfCurrent), position.x, position.y);

	}

}
