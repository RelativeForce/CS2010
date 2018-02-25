package peril.views.slick.components.menus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Color;
import peril.controllers.GameController;
import peril.views.slick.Frame;
import peril.views.slick.components.Component;
import peril.views.slick.components.TextField;
import peril.views.slick.util.Button;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;
import peril.views.slick.util.Region;
import peril.views.slick.util.Viewable;

/**
 * Encapsulates the behaviour of a help window that displays information to the
 * user. This window uses pages that resemble a doubly linked list nodes that
 * point to the next page and previous page using the IDs of those pages.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-18
 * @version 1.01.01
 * 
 * @see Menu
 *
 */
public final class HelpMenu extends Menu {

	/**
	 * The id of a null page. If this assigned to the next or previous page of a
	 * page the current page will not link to anything.
	 */
	public static final int NULL_PAGE = -1;

	/**
	 * The uniquely identifying string name of the {@link HelpMenu}.
	 */
	public static final String NAME = "Help";

	/**
	 * The width of the {@link HelpMenu}
	 */
	private static final int WIDTH = 600;

	/**
	 * The height of the {@link HelpMenu}.
	 */
	private static final int HEIGHT = 600;

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

	/**
	 * The {@link Font} that the title of the {@link HelpPage} is drawn in.
	 */
	private final Font titleFont;

	/**
	 * Whether or not this {@link HelpMenu} has been initialised or not.
	 */
	private boolean isInitialised;

	/**
	 * The id of the current {@link HelpPage} being displayed on the
	 * {@link HelpMenu}.
	 */
	private int currentPage;

	/**
	 * Denotes the current number of the current page in a successive chain.
	 */
	private int pageNumber;

	/**
	 * Denotes the number of pages in one successive chain.
	 */
	private int numberOfPages;

	/**
	 * The {@link Map} of IDs and their associated {@link HelpPage}s.
	 */
	private final Map<Integer, HelpPage> pages;

	/**
	 * The id of the {@link Button} that moves this {@link HelpMenu} to the next
	 * {@link HelpPage}.
	 */
	private final String next;

	/**
	 * The id of the {@link Button} that moves this {@link HelpMenu} to the previous
	 * {@link HelpPage}.
	 */
	private final String previous;

	/**
	 * Constructs a new {@link HelpMenu}.
	 * 
	 * @param game
	 *            The {@link GameController} that allows the {@link HelpMenu} to
	 *            query the state of the game.
	 * @param position
	 *            The {@link Point} position of the {@link HelpMenu}
	 * @param width
	 *            The width of the {@link HelpMenu}
	 * @param height
	 *            The height of {@link HelpMenu}}
	 */
	public HelpMenu(Point position, GameController game) {
		super(NAME, game, new Region(WIDTH, HEIGHT, position));

		this.pages = new HashMap<>();
		this.currentPage = 0;
		this.isInitialised = false;
		this.pageNumber = 0;
		this.numberOfPages = 0;
		this.previous = "previous";
		this.next = "next";
		this.titleFont = new Font("Arial", Color.white, 20);
	}

	/**
	 * Initialises this {@link HelpMenu}.
	 */
	@Override
	public void init() {

		isInitialised = true;

		// Initialise all the pages
		pages.forEach((id, page) -> page.init());
	}

	/**
	 * Adds a {@link HelpPage} to this {@link HelpMenu}.
	 * 
	 * @param id
	 *            The uniquely identifying ID number of the new {@link HelpPage}.
	 * @param nextPage
	 *            The uniquely identifying ID number of the next {@link HelpPage}
	 *            after the new {@link HelpPage}.
	 * @param previousPage
	 *            The uniquely identifying ID number of the previous
	 *            {@link HelpPage} after the new {@link HelpPage}.
	 * @param title
	 *            The title of the new {@link HelpPage}.
	 */
	public void addPage(int id, int nextPage, int previousPage, String title) {

		// Check if the page id already exists. There can be no duplicate pages.
		if (pages.containsKey(id)) {
			throw new IllegalStateException("Page: " + id + " already exists.");
		} else if (id < NULL_PAGE) {
			throw new IllegalStateException(id + " is an invalid page ID.");
		} else if (nextPage < NULL_PAGE) {
			throw new IllegalStateException(nextPage + " is an invalid page ID.");
		} else if (previousPage < NULL_PAGE) {
			throw new IllegalStateException(previousPage + " is an invalid page ID.");
		}

		// Construct the text feild that will show this page.
		final TextField text = new TextField(WIDTH - (PADDING_X * 2),
				new Point(this.getPosition().x + PADDING_X, this.getPosition().y + PADDING_Y));

		// Construct the help page
		final HelpPage newPage = new HelpPage(text, nextPage, previousPage, title);

		// Add the help page to the as its new page.
		pages.put(id, newPage);

		// If the menu is all ready initialised then initialise the page.
		if (isInitialised) {
			newPage.init();
		}

	}

	/**
	 * Changes the currently displayed {@link HelpPage} of the {@link HelpMenu}.
	 * 
	 * @param pageId
	 *            The uniquely identifying ID number of the new {@link HelpPage}.
	 */
	public void changePage(int pageId) {

		numberOfPages = 0;

		if (pages.containsKey(pageId)) {

			// If there is an next page show the button, otherwise hide it.
			if (pages.get(pageId).next != NULL_PAGE) {
				getButton(next).show();
			} else {
				getButton(next).hide();
			}

			// If there is an previous page show the button, otherwise hide it.
			if (pages.get(pageId).previous != NULL_PAGE) {
				getButton(previous).show();
			} else {
				getButton(previous).hide();
			}

			this.currentPage = pageId;
			determinePageNumber();
		} else {

			getButton(next).hide();
			getButton(previous).hide();
			this.currentPage = pageId;
		}

	}

	/**
	 * Changes the {@link HelpPage} being displayed by the {@link HelpMenu} to the
	 * next {@link HelpPage} denoted by the current {@link HelpPage}.
	 */
	public void nextPage() {
		int next = pages.get(currentPage).next;

		if (next != NULL_PAGE) {
			changePage(next);
		}

	}

	/**
	 * Clears the text from the {@link HelpPage} denoted by the specified page id.
	 * 
	 * @param pageId
	 *            The uniquely identifying ID number of the {@link HelpPage} to be
	 *            cleared.
	 */
	public void clearPage(int pageId) {
		if (!pages.containsKey(pageId)) {
			throw new IllegalArgumentException("Page: " + pageId + " does not exist.");
		}

		pages.get(pageId).clear();

	}

	/**
	 * Changes the {@link HelpPage} being displayed by the {@link HelpMenu} to the
	 * previous {@link HelpPage} denoted by the current {@link HelpPage}.
	 */
	public void previousPage() {
		int previous = pages.get(currentPage).previous;

		if (previous != NULL_PAGE) {
			changePage(previous);
		}
	}

	/**
	 * Adds text to the {@link HelpPage} denoted by the specified id.
	 * 
	 * @param pageId
	 *            The uniquely identifying ID number of the {@link HelpPage}.
	 * @param text
	 */
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
	public void draw(Frame frame) {
		if (!isVisible())
			return;

		super.draw(frame);

		// Draw the current page
		if (pages.containsKey(currentPage)) {
			pages.get(currentPage).draw(frame);
		}

		if (numberOfPages != 0) {
			frame.draw(titleFont, pageNumber + "/" + numberOfPages, getPosition().x + PADDING_X,
					getPosition().y + getHeight() - PADDING_Y);
		}

	}

	/**
	 * Moves the {@link Button} and {@link Viewable}s of the {@link HelpMenu}.
	 */
	@Override
	public void moveComponents(Point vector) {

		pages.forEach((id, page) -> page
				.setPosition(new Point(page.getPosition().x + vector.x, page.getPosition().y + vector.y)));

	}

	/**
	 * Determines the number of {@link HelpPage} in the current chain of
	 * {@link HelpPage}s.
	 */
	private void determinePageNumber() {

		HelpPage current = pages.get(this.currentPage);

		// Go to the first page in the chain
		while (current.previous != NULL_PAGE) {
			current = pages.get(current.previous);
		}

		numberOfPages = 1;
		pageNumber = 1;

		// Iterate through the chain and mark determine the current pages number and the
		// number of pages.
		while (current.next != NULL_PAGE) {
			current = pages.get(current.next);
			numberOfPages++;

			if (current.equals(pages.get(this.currentPage))) {
				pageNumber = numberOfPages;
			}
		}

	}

	/**
	 * A page on the {@link HelpMenu} that links to a next and previous
	 * {@link HelpPage}. Also wraps {@link TextField} allowing for
	 * pre-initialisation text addition.
	 * 
	 * @author Joshua_Eddy
	 * 
	 * @serial 2018-02-25
	 * @version 1.01.01
	 * 
	 * @see Component
	 *
	 */
	private final class HelpPage implements Component {

		/**
		 * The uniquely identifying ID number of the next {@link HelpPage}.
		 */
		public final int next;

		/**
		 * The uniquely identifying ID number of the previous {@link HelpPage}.
		 */
		public final int previous;

		/**
		 * The {@link TextField} that will display the text to the user.
		 */
		private final TextField text;

		/**
		 * The string title of this {@link HelpPage}.
		 */
		private final String title;

		/**
		 * Holds the lines of text that have been give to this {@link HelpMenu} before
		 * this {@link HelpMenu} has be initialised using {@link HelpMenu#init()}. When
		 * this {@link HelpMenu} is initialised all the elements of this {@link List}
		 * will be added to the {@link TextField}.
		 */
		private final List<String> temp;

		/**
		 * Constructs a new {@link HelpPage}.
		 * 
		 * @param text
		 *            The {@link TextField} that will be displayed by the
		 *            {@link HelpPage}.
		 * @param next
		 *            The uniquely identifying ID number of the next {@link HelpPage}.
		 * @param previous
		 *            The uniquely identifying ID number of the previous
		 *            {@link HelpPage}.
		 * @param title
		 *            The title of the {@link HelpPage}
		 */
		public HelpPage(TextField text, int next, int previous, String title) {

			this.next = next;
			this.previous = previous;
			this.title = title;
			this.temp = new LinkedList<>();
			this.text = text;

		}

		/**
		 * Adds a line of text to this {@link HelpMenu}. This will wrap to the next line
		 * if it is too long.
		 * 
		 * @param text
		 *            The text to be added to the {@link HelpPage}.
		 */
		public void addText(String text) {
			if (isInitialised) {
				this.text.addText(text);
			} else {
				this.temp.add(text);
			}
		}

		/**
		 * Clears the text from the {@link HelpPage}.
		 */
		public void clear() {
			text.clear();
			temp.clear();
		}

		/**
		 * Initialises this {@link HelpPage}.
		 */
		@Override
		public void init() {
			text.init();
			temp.forEach(line -> text.addText(line));
			temp.clear();
			titleFont.init();
		}

		/**
		 * Draws this {@link HelpPage}.
		 */
		@Override
		public void draw(Frame frame) {
			frame.setColor(Color.white);
			frame.draw(titleFont, title, text.getPosition().x, text.getPosition().y - (titleFont.getHeight() * 2));
			text.draw(frame);
		}

		/**
		 * Sets the {@link Point} position of the {@link HelpPage}.
		 */
		@Override
		public void setPosition(Point position) {
			text.setPosition(position);
		}

		/**
		 * Retrieves the {@link Point} position of the {@link HelpPage}.
		 */
		@Override
		public Point getPosition() {
			return text.getPosition();
		}
	}
}
