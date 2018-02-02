package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.Update;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;
import peril.views.slick.Font;
import peril.views.slick.Point;
import peril.views.slick.SlickModelView;

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
	 * The padding between {@link SlickUnit}s in the expanded view.
	 */
	private static final int PADDING = 50;

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
	 * Holds the currently selected {@link ModelUnit}.
	 */
	private ModelUnit selected;

	/**
	 * Constructs a new {@link SlickArmy} using an {@link ModelArmy}.
	 * 
	 * @param model
	 */
	public SlickArmy(ModelArmy model) {

		this.model = model;
		this.expanded = false;
		this.collapsedFont = new Font("Arial", Color.white, 17);
		this.expandedFont = new Font("Arial", Color.black, 25);
		this.selected = null;

		model.addObserver(this);
	}

	/**
	 * Updates this {@link SlickArmy} from the {@link Model}.
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (arg == null) {
			return;
		}

		if (arg instanceof Update) {
			Update update = (Update) arg;

			switch (update.property) {
			case "selected":

				updateSelected(update.newValue);

				break;
			}

		}
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
		selected = null;
		expanded = false;
	}

	/**
	 * Retrieves whether or not this {@link SlickArmy} is expanded or not.
	 * 
	 * @return boolean
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * Processes a click on this {@link SlickArmy}. If a {@link SlickUnit} in this
	 * army is clicked it will be selected.
	 * 
	 * @param click
	 *            {@link Point} position of the click.
	 * @param armyPosition
	 *            {@link Point} position of this {@link SlickArmy}.
	 * @param view
	 *            The {@link SlickModelView} that will allow the retrieval of the
	 *            {@link SlickUnit}s
	 * @return Whether or not a {@link SlickUnit} was selected or not.
	 */
	public boolean isClicked(Point click, Point armyPosition, SlickModelView view) {

		// If the army is expanded
		if (isExpanded()) {

			// Reposition units so the click detection works
			rePositionUnits(armyPosition, view);

			// Iterate over each unit in the army
			for (ModelUnit unit : model) {

				// Holds the slick version of the current unit.
				final SlickUnit slickUnit = view.getVisual(unit);

				// If the slick unit was clicked
				if (slickUnit.isClicked(click)) {

					// Select the unit then flag the fact a unit has been clicked.
					model.select(unit);
					return true;
				}
			}
		}

		return false;
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
	 * @param view
	 */
	public void draw(Graphics g, Point position, SlickPlayer ruler, SlickModelView view) {

		collapsedFont.init();
		expandedFont.init();

		if (expanded) {
			drawExpanded(g, position, ruler, view);
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
	 * @param view
	 */
	public void drawExpanded(Graphics g, Point position, SlickPlayer ruler, SlickModelView view) {

		final SlickUnit slickSelected = view.getVisual(selected);

		rePositionUnits(position, view);

		int x = position.x - ((model.getVarietyOfUnits() - 1) * PADDING);
		int y = position.y;

		for (ModelUnit current : model) {

			final SlickUnit unit = view.getVisual(current);

			if (view.getVisual(current).equals(slickSelected)) {

				final int width = unit.getWidth();
				final int height = unit.getHeight();
				final int xPadding = width / 20;
				final int yPadding = height / 20;

				g.setColor(Color.cyan);
				g.fillRect(x - xPadding, y - yPadding, width + (2 * xPadding), height + (2 * yPadding));
			}

			drawUnit(g, unit);

			x += PADDING;

		}
	}

	/**
	 * Repositions the {@link SlickUnit}s that make up this {@link SlickArmy} so
	 * that they are correctly drawn on screen.
	 * 
	 * @param position
	 *            The {@link Point} position of this {@link SlickArmy}.
	 * @param view
	 *            The {@link SlickModelView} that will allow the retrieval of the
	 *            {@link SlickUnit}s
	 */
	private void rePositionUnits(Point position, SlickModelView view) {

		int x = position.x - ((model.getVarietyOfUnits() - 1) * PADDING);
		int y = position.y;

		for (ModelUnit current : model) {

			final SlickUnit unit = view.getVisual(current);

			unit.setPosition(new Point(x, y));

			x += PADDING;

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
		final int troopNumber = model.getStrength();

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
	 * @param view
	 * 
	 */
	private void drawUnit(Graphics g, SlickUnit unit) {

		final int numberOfCurrent = model.getNumberOf(unit.model);
		final Point position = unit.getPosition();

		g.setColor(Color.lightGray);

		g.drawImage(unit.getImage(), position.x, position.y);

		final String number = Integer.toString(numberOfCurrent);
		final int x = position.x + 15 - (expandedFont.getWidth(number) / 2);

		expandedFont.draw(g, number, x, position.y);

	}

	/**
	 * Updates the selected {@link SlickUnit}.
	 * 
	 * @param value
	 *            {@link SlickUnit}
	 */
	private void updateSelected(Object value) {
		if (value == null) {
			selected = null;
		} else {
			selected = (ModelUnit) value;
		}
	}
}
