package peril.ui.components.menus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Graphics;

import peril.Game;
import peril.Point;
import peril.io.fileReaders.ImageReader;
import peril.multiThread.Action;
import peril.ui.Button;
import peril.ui.Region;
import peril.ui.Viewable;
import peril.ui.components.Component;
import peril.ui.components.TextField;

/**
 * Encapsulates the behaviour of a help window that displays information to the
 * user. This wraps the {@link TextField} as this class cannot support adding
 * text pre-initialisation.
 * 
 * @author Joshua_Eddy
 *
 */
public class HelpMenu extends Menu {

	public static final int NULL_PAGE = -1; 
	
	private static final int WIDTH = 400;

	private static final int HEIGHT = 400;

	private static final String NAME = "Help";

	/**
	 * The padding in the horizontal direction between the edge of the
	 * {@link HelpMenu} and the edge of the {@link TextField}.
	 */
	private static final int PADDING_X = WIDTH / 12;

	/**
	 * The padding in the vertical direction between the edge of the
	 * {@link HelpMenu} and the edge of the {@link TextField}.
	 */
	private static final int PADDING_Y = HEIGHT / 10;

	private int currentPage;

	private final Map<Integer, HelpPage> pages;

	/**
	 * Constructs a new {@link HelpMenu}.
	 * 
	 * @param game
	 *            {@link Game}
	 * @param position
	 *            {@link Point} position of the {@link HelpMenu}
	 * @param width
	 *            of the {@link HelpMenu}
	 * @param height
	 *            of {@link HelpMenu}}
	 */
	public HelpMenu(Point position, Game game) {
		super(NAME, game, new Region(WIDTH, HEIGHT, position));
		this.pages = new HashMap<>();
		this.currentPage = 0;
	}

	/**
	 * Initialises this {@link HelpMenu}.
	 */
	@Override
	public void init() {

		super.addImage(new Viewable(
				ImageReader.getImage(getGame().assets.ui + "toolTipBox.png").getScaledCopy(getWidth(), getHeight()),
				new Point(0, 0)));

		super.addButton(new Button(new Point(this.getWidth() - 50, 0),
				ImageReader.getImage(getGame().assets.ui + "xButton.png").getScaledCopy(50, 50),
				new Action<HelpMenu>(this, help -> help.hide()), "help"));

		// Initialise all the pages
		pages.forEach((id, page) -> page.init());

	}

	public void addPage(int id, int nextPage, int previousPage, String title) {
		
		// Check if the page id already exists. There can be no duplicate pages.
		if (pages.containsKey(id)) {
			throw new IllegalStateException("Page: " + id + " already exists.");
		}

		// Construct the text feild that will show this page.
		TextField text = new TextField(WIDTH - (PADDING_X *2), HEIGHT - (PADDING_Y * 2), new Point(PADDING_X, PADDING_Y));
		
		// Construct the help page
		HelpPage newPage = new HelpPage(text, nextPage, previousPage);
		
		// Add the help page to the as its new page.
		pages.put(id, newPage);

	}

	public void changePage(int pageId) {
		if (!pages.containsKey(pageId)) {
			throw new IllegalArgumentException("Page: " + pageId + " does not exist.");
		}

		this.currentPage = pageId;
	}

	public void nextPage() {
		int next = pages.get(currentPage).next;
		
		if(next != NULL_PAGE) {
			currentPage = next;
		}
		
	}
	
	public void previousPage() {
		int previous = pages.get(currentPage).previous;
		
		if(previous != NULL_PAGE) {
			currentPage = previous;
		}
	}
	
	public void addText(int pageId, String text) {
		if (!pages.containsKey(pageId)) {
			throw new IllegalArgumentException("Page: " + pageId + " does not exist.");
		}

		pages.get(pageId).addText(text);
	}

	/**
	 * Draws thus {@link HelpMenu} on screen. If this {@link HelpMenu} is hidden,
	 * this with do nothing.
	 */
	@Override
	public void draw(Graphics g) {
		if (!isVisible())
			return;

		super.draw(g);

		// Draw the current page
		if (pages.containsKey(currentPage)) {
			pages.get(currentPage).draw(g);
		}

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
	 * Moves the {@link Button} and {@link Viewable}s of the {@link HelpMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

		pages.forEach((id, page) -> page
				.setPosition(new Point(page.getPosition().x + vector.x, page.getPosition().y + vector.y)));

	}

	private class HelpPage implements Component {

		public final int next;

		public final int previous;

		/**
		 * The {@link TextField} that will display the text to the user.
		 */
		private final TextField text;

		private String title;

		/**
		 * Whether or not this {@link HelpMenu} has been initialised or not.
		 */
		private boolean isInitialised;

		/**
		 * Holds the lines of text that have been give to this {@link HelpMenu} before
		 * this {@link HelpMenu} has be initialised using {@link HelpMenu#init()}. When
		 * this {@link HelpMenu} is initialised all the elements of this {@link List}
		 * will be added to the {@link TextField}.
		 */
		private final List<String> temp;

		public HelpPage(TextField text, int next, int previous) {

			this.next = next;
			this.previous = previous;
			this.isInitialised = false;
			this.temp = new LinkedList<>();
			this.text = text;
			this.title = "Help";
		}

		/**
		 * Adds a line of text to this {@link HelpMenu}. This will wrap to the next line
		 * if it is too long.
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

		public void setTitle(String title) {
			this.title = title;
		}

		public void clear() {
			text.clear();
			temp.clear();
		}

		@Override
		public void init() {
			isInitialised = true;
			text.init();
			temp.forEach(line -> text.addText(line));
			temp.clear();
		}

		@Override
		public void draw(Graphics g) {

			g.drawString(title, text.getPosition().x, text.getPosition().y - 10);
			text.draw(g);
		}

		@Override
		public void setPosition(Point position) {
			text.setPosition(position);
		}

		@Override
		public Point getPosition() {
			return text.getPosition();
		}
	}
}
