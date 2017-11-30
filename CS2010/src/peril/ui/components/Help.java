package peril.ui.components;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.io.fileReaders.ImageReader;
import peril.ui.Clickable;
import peril.ui.Region;

/**
 * Encapsulates the behaviour of a help window that displays information to the
 * user. This wraps the {@link TextField} as this class cannot support adding
 * text pre-initialisation.
 * 
 * @author Joshua_Eddy
 *
 */
public class Help extends Clickable implements Component {

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
	 * Holds the {@link Game} this {@link Help} is a part of.
	 */
	private final Game game;

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
	 * Whether or not this {@link Help is visible.}
	 */
	private boolean visible;

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
		super(new Region(width, height, position));

		this.game = game;
		this.isInitialised = false;
		this.paddingY = height / 10;
		this.paddingX = width / 12;
		this.temp = new LinkedList<>();
		this.visible = false;
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
	 * Hides this {@link Help}.
	 */
	public void hide() {
		visible = false;
	}

	/**
	 * Toggles the visibility of this {@link Help}.
	 */
	public void toggleVisibility() {
		visible = !visible;
	}

	/**
	 * Shows this {@link Help}.
	 */
	public void show() {
		visible = true;
	}

	/**
	 * Initialises this {@link Help}.
	 */
	@Override
	public void init() {

		super.setImage(getPosition(), ImageReader.getImage(game.assets.ui + File.separatorChar + "toolTipBox.png")
				.getScaledCopy(getWidth(), getHeight()));

		isInitialised = true;
		text.init();
		temp.forEach(line -> text.addText(line));
		temp.clear();
	}

	/**
	 * Assigns a new {@link Point} position of this {@link Help}.
	 */
	@Override
	public void setPosition(Point position) {
		super.setPosition(position);
		text.setPosition(new Point(position.x + paddingX, position.y + paddingY));
	}

	/**
	 * Draws thus {@link Help} on screen. If this {@link Help} is hidden, this with
	 * do nothing.
	 */
	@Override
	public void draw(Graphics g) {
		if (!visible)
			return;

		g.drawImage(getImage(), getPosition().x, getPosition().y);
		text.draw(g);
	}

}
