package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.Color;

import peril.Update;
import peril.model.board.ModelArmy;
import peril.model.board.ModelUnit;
import peril.views.slick.EventListener;
import peril.views.slick.Frame;
import peril.views.slick.SlickModelView;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * The visual representation of a {@link ModelArmy}. This observes the
 * {@link ModelArmy} and will update upon model changes.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-03-15
 * @version 1.01.03
 * 
 * @see Observer
 * @see ModelArmy
 *
 */
public final class SlickArmy implements Observer {

	/**
	 * The padding between {@link SlickUnit}s in the expanded view.
	 */
	private static final int PADDING = SlickUnit.WIDTH + 10;

	/**
	 * The {@link ModelArmy} this {@link SlickArmy} will observe and display.
	 */
	public final ModelArmy model;

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
	 * Whether of not this {@link SlickArmy} will be displayed in its expanded view
	 * or not.
	 */
	private boolean expanded;

	/**
	 * Constructs a new {@link SlickArmy} using an {@link ModelArmy}.
	 * 
	 * @param model
	 *            The {@link ModelArmy} this {@link SlickArmy} will observe and
	 *            display.
	 */
	public SlickArmy(ModelArmy model) {

		this.model = model;
		this.expanded = false;
		this.collapsedFont = new Font("Arial", Color.white, 50);
		this.expandedFont = new Font("Arial", Color.red, 50);
		this.selected = null;

		model.addObserver(this);
	}

	/**
	 * Updates this {@link SlickArmy} from the {@link ModelArmy}.
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
	 * Draws this {@link SlickArmy} in it expanded view which shows all the
	 * different types of {@link SlickUnit} in this {@link SlickArmy} and how many
	 * of them there are.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display the {@link SlickArmy} to the
	 *            user.
	 * @param position
	 *            The {@link Point} of the centre of the {@link SlickArmy}.
	 * @param ruler
	 *            The {@link SlickPlayer} that rules this {@link SlickArmy}.
	 * @param view
	 *            The {@link SlickModelView} that allows the {@link ModelUnit}s in
	 *            the {@link ModelArmy} to be mapped to their {@link SlickArmy}
	 *            counterparts.
	 */
	public void drawExpanded(Frame frame, Point position, SlickPlayer ruler, SlickModelView view) {

		final SlickUnit slickSelected = view.getVisual(selected);

		// Reposition the units on screen.
		rePositionUnits(position, view);

		int x = position.x - ((model.getVarietyOfUnits() - 1) * PADDING) - (SlickUnit.WIDTH / 2);
		int y = position.y - (SlickUnit.HEIGHT / 2);

		// For each unit in the army
		for (ModelUnit current : model) {

			// Holds the visual version of the unit.
			final SlickUnit unit = view.getVisual(current);

			// If the current unit is the selected unit then draw a highlight rectangle
			// behind it.
			if (view.getVisual(current).equals(slickSelected)) {

				final int xPadding = SlickUnit.WIDTH / 20;
				final int yPadding = SlickUnit.HEIGHT / 20;

				final int width = SlickUnit.WIDTH + (2 * xPadding);
				final int height = SlickUnit.HEIGHT + (2 * yPadding);

				frame.setColor(Color.cyan);
				frame.fillOval(x - xPadding, y - yPadding, width, height);
			}

			// Draw the unit.
			drawUnit(frame, unit);

			x += PADDING;

		}
	}

	/**
	 * Draws this {@link SlickArmy} at a position on screen.
	 * 
	 * @param frame
	 *            The {@link Frame} that will display the {@link SlickArmy} to the
	 *            user.
	 * @param position
	 *            The {@link Point} of the centre of the {@link SlickArmy}.
	 * @param ruler
	 *            The {@link SlickPlayer} that rules this {@link SlickArmy}.
	 * @param view
	 *            The {@link SlickModelView} that allows the {@link ModelUnit}s in
	 *            the {@link ModelArmy} to be mapped to their {@link SlickArmy}
	 *            counterparts.
	 */
	public void draw(Frame frame, Point position, SlickPlayer ruler, SlickModelView view) {

		collapsedFont.init();
		expandedFont.init();

		if (expanded) {
			drawExpanded(frame, position, ruler, view);
		} else {
			drawCollapsed(frame, position, ruler);
		}

	}

	/**
	 * Retrieves whether or not this {@link SlickArmy} is expanded or not.
	 * 
	 * @return boolean Whether or not this {@link SlickArmy} is expanded or not.
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * Processes a click on this {@link SlickArmy}. If a {@link SlickUnit} in this
	 * army is clicked it will be selected.
	 * 
	 * @param click
	 *            The {@link Point} position of the click.
	 * @param armyPosition
	 *            The {@link Point} position of this {@link SlickArmy}.
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
	 * Repositions the {@link SlickUnit}s that make up this {@link SlickArmy} so
	 * that they are correctly drawn on screen.
	 * 
	 * @param position
	 *            The {@link Point} position of this {@link SlickArmy}.
	 * @param view
	 *            The {@link SlickModelView} that allows the {@link ModelUnit}s in
	 *            the {@link ModelArmy} to be mapped to their {@link SlickArmy}
	 *            counterparts.
	 */
	private void rePositionUnits(Point position, SlickModelView view) {

		int x = position.x - ((model.getVarietyOfUnits() - 1) * PADDING) - (SlickUnit.WIDTH / 2);
		int y = position.y - (SlickUnit.HEIGHT / 2);

		// For each unit in the army
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
	 * @param frame
	 *            The {@link Frame} that will display the {@link SlickArmy} to the
	 *            user.
	 * @param position
	 *            The {@link Point} of the centre of the {@link SlickArmy}.
	 * @param ruler
	 *            The {@link SlickPlayer} that rules this {@link SlickArmy}.
	 */
	private void drawCollapsed(Frame frame, Point position, SlickPlayer ruler) {

		// Draw a background oval with the rulers colour. If no ruler found default to
		// light grey.
		if (ruler != null) {
			frame.setColor(ruler.color);
		} else {
			frame.setColor(Color.lightGray);
		}

		// Holds the size of the current countries army
		final int troopNumber = model.getStrength();

		drawArmyOval(position, troopNumber, frame);

		final String troopStr = Integer.toString(troopNumber);

		final int textWidth = collapsedFont.getWidth(troopStr);

		final int textHeight = collapsedFont.getHeight(troopStr);

		// Draw a string representing the number of troops
		// within that army at (x,y).
		frame.draw(collapsedFont, troopStr, position.x - (textWidth / 2), position.y - (textHeight / 2) - 5);

	}

	/**
	 * Draws the oval that is displayed behind the {@link ModelArmy} on a
	 * {@link SlickCountry}.
	 * 
	 * @param position
	 *            The {@link Point} of the centre of the {@link SlickArmy}.
	 * @param strength
	 *            The strength of the {@link ModelArmy}
	 * @param frame
	 *            The {@link Frame} that will display the {@link SlickArmy} to the
	 *            user.
	 */
	private void drawArmyOval(Point position, int strength, Frame frame) {

		final int width = collapsedFont.getWidth(Integer.toString(strength)) + 15;

		final int height = collapsedFont.getHeight(Integer.toString(strength)) + 10;

		frame.fillOval(position.x - (width / 2), position.y - (height / 2) , width, height);

	}

	/**
	 * Draws a {@link ModelUnit} on screen at a specified {@link Point} position.
	 * 
	 * @param unit
	 *            The {@link SlickUnit} to be drawn.
	 * @param frame
	 *            The {@link Frame} that will display the {@link SlickArmy} to the
	 *            user.
	 */
	private void drawUnit(Frame frame, SlickUnit unit) {

		final int numberOfCurrent = model.getNumberOf(unit.model);
		final Point position = unit.getPosition();

		frame.setColor(Color.lightGray);

		frame.draw(unit, new EventListener() {

			@Override
			public void mouseHover(Point mouse) {
				// Do nothing
			}

			@Override
			public void mouseClick(Point mouse, int mouseButton) {

				// Select the unit then flag the fact a unit has been clicked.
				model.select(unit.model);

			}

			@Override
			public void draw(Frame frame) {
				frame.draw(unit.getImage(), position.x, position.y);
			}

			@Override
			public void buttonPress(int key, Point mouse) {
				// Do nothing

			}
		});

		final String number = Integer.toString(numberOfCurrent);

		final int x = position.x + (SlickUnit.WIDTH / 2) - (expandedFont.getWidth(number) / 2);
		final int y = position.y + (SlickUnit.HEIGHT / 2) - (expandedFont.getHeight() / 2);

		frame.draw(expandedFont, number, x, y);

	}

	/**
	 * Updates the selected {@link SlickUnit}.
	 * 
	 * @param value
	 *            The {@link SlickUnit}
	 */
	private void updateSelected(Object value) {
		selected = (ModelUnit) value;
	}
}
