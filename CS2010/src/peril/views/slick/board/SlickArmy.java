package peril.views.slick.board;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import peril.model.board.ModelArmy;
import peril.views.slick.Point;

/**
 * The slick view for {@link ModelArmy}. This observes the {@link ModelArmy} and
 * will update upon model changes.
 * 
 * @author Joshua_Eddy
 *
 */
public class SlickArmy implements Observer {

	/**
	 * The {@link ModelArmy} this {@link SlickArmy} will observe and display.
	 */
	public final ModelArmy model;

	/**
	 * Constructs a new {@link SlickArmy} using an {@link ModelArmy}.
	 * 
	 * @param model
	 */
	public SlickArmy(ModelArmy model) {
		this.model = model;

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

		// Draw a background oval with the rulers colour. If no ruler found default to
		// light grey.
		if (ruler != null) {
			g.setColor(ruler.color);
		} else {
			g.setColor(Color.lightGray);
		}

		// Holds the size of the current countries army
		int troopNumber = model.getSize();

		drawArmyOval(position, troopNumber, g);

		g.setColor(Color.white);

		// Draw a string representing the number of troops
		// within that army at (x,y).

		g.drawString(Integer.toString(troopNumber), position.x, position.y);

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

}
