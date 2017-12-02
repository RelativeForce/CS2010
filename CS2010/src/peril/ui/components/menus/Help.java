package peril.ui.components.menus;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.io.fileReaders.ImageReader;
import peril.multiThread.Action;
import peril.ui.Button;
import peril.ui.Region;
import peril.ui.Viewable;
import peril.ui.components.TextField;

/**
 * Encapsulates the behaviour of a help window that displays information to the
 * user. This wraps the {@link TextField} as this class cannot support adding
 * text pre-initialisation.
 * 
 * @author Joshua_Eddy
 *
 */
public class Help extends Menu {

	/**
	 * The {@link TextField} that will display the text to the user.
	 */
	private final TextField text;

	/**
	 * Holds the lines of text that have been give to this {@link Help} before this
	 * {@link Help} has be initialised using {@link Help#init()}. When this
	 * {@link Help} is initialised all the elements of this {@link List} will be
	 * added to the {@link TextField}.
	 */
	private final List<String> temp;

	/**
	 * The padding in the horizontal direction between the edge of the {@link Help}
	 * and the edge of the {@link TextField}.
	 */
	private final int paddingX;

	/**
	 * The padding in the vertical direction between the edge of the {@link Help}
	 * and the edge of the {@link TextField}.
	 */
	private final int paddingY;

	/**
	 * Whether or not this {@link Help} has been initialised or not.
	 */
	private boolean isInitialised;

	/**
	 * Constructs a new {@link Help}.
	 * 
	 * @param game
	 *            {@link Game}
	 * @param position
	 *            {@link Point} position of the {@link Help}
	 * @param width
	 *            of the {@link Help}
	 * @param height
	 *            of {@link Help}}
	 */
	public Help(Game game, Point position, int width, int height) {
		super("Help", game, new Region(width, height, position));

		this.isInitialised = false;
		this.paddingY = height / 10;
		this.paddingX = width / 12;
		this.temp = new LinkedList<>();
		this.text = new TextField(width - (paddingX * 2), height - (paddingY * 2),
				new Point(position.x + paddingX, position.y + paddingY));
	}

	/**
	 * Adds a line of text to this {@link Help}. This will wrap to the next line if
	 * it is too long.
	 * 
	 * @param text
	 */
	public void addText(String text) {
		if (isInitialised) {
			this.text.addText(text);
		} else {
			this.temp.add(text);
		}
	}

	/**
	 * Initialises this {@link Help}.
	 */
	@Override
	public void init() {

		super.addImage(new Viewable(
				ImageReader.getImage(getGame().assets.ui + "toolTipBox.png").getScaledCopy(getWidth(), getHeight()),
				new Point(0, 0)));

		super.addButton(new Button(new Point(this.getWidth() - 50, 0),
				ImageReader.getImage(getGame().assets.ui + "xButton.png").getScaledCopy(50, 50),
				new Action<Help>(this, help -> help.hide()), "help"));

		isInitialised = true;
		text.init();
		temp.forEach(line -> text.addText(line));
		temp.clear();
	}

	/**
	 * Draws thus {@link Help} on screen. If this {@link Help} is hidden, this with
	 * do nothing.
	 */
	@Override
	public void draw(Graphics g) {
		if (!isVisible())
			return;

		super.draw(g);
		text.draw(g);

	}

	/**
	 * Processes a click at a {@link Point} position on this {@link Button}.
	 * 
	 * @param click
	 *            {@link Point}
	 */
	public void parseClick(Point click) {
		super.clickedButton(click);
	}

	/**
	 * Moves the {@link Button} and {@link Viewable}s of the {@link Help}.
	 */
	@Override
	public void moveComponents(Point vector) {

		Point textCurrent = text.getPosition();

		text.setPosition(new Point(textCurrent.x + vector.x, textCurrent.y + vector.y));

	}

}
