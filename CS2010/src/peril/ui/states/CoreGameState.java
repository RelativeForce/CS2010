package peril.ui.states;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.board.Continent;
import peril.board.Country;
import peril.ui.visual.Clickable;
import peril.ui.visual.Viewable;

/**
 * @author Joseph_Rolli, Joshua_Eddy
 */

public abstract class CoreGameState extends BasicGameState {

	/**
	 * Holds the name of a specific {@link CoreGameState}.
	 */
	protected String stateName;

	/**
	 * Holds the the current {@link Game} this {@link CoreGameState} is associated
	 * with.
	 */
	protected Game game;

	/**
	 * A {@link List} of {@link Viewable} elements that this {@link CoreGameState}
	 * has.
	 */
	protected List<Viewable> viewables;

	/**
	 * A {@link List} of {@link Clickable} elements that this {@link CoreGameState}
	 * has.
	 */
	protected List<Clickable> clickables;

	/**
	 * The current {@link Country} that the player has highlighted.
	 */
	private Country highlightedCountry;

	/**
	 * Constructs a new {@link CoreGameState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 */
	protected CoreGameState(Game game) {
		this.game = game;

		this.highlightedCountry = null;
		this.clickables = new LinkedList<>();
		this.viewables = new LinkedList<>();
	}

	/**
	 * Set the current {@link Country} that the player has highlighted.
	 */
	public void highlight(Country country) {
		highlightedCountry = country;
	}

	@Override
	/**
	 * Called when the state is entered, before slick2d's game loop commences.
	 * 
	 * @param gc
	 *            The game window.
	 * 
	 * @param sbg
	 *            The {@link StateBasedGame} this state is a part of.
	 */
	public void enter(GameContainer gc, StateBasedGame sbg) {
		System.out.println("Entering gamestate:" + Integer.toString(getID()));

	}

	@Override
	/**
	 * Called when the state is first created, before slick2d's game loop commences.
	 * Initialises the state and loads resources.
	 * 
	 * @param gc
	 *            The game window.
	 * 
	 * @param sbg
	 *            The {@link StateBasedGame} this state is a part of.
	 * 
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.setUpdateOnlyWhenVisible(true);

		game.loadAssets();
	}

	@Override
	/**
	 * Called as part of slick2d's game loop. Renders this state to the game's
	 * graphics context
	 * 
	 * @param gc
	 *            The game window.
	 * 
	 * @param sbg
	 *            The {@link StateBasedGame} this state is a part of.
	 *
	 * @param g
	 *            A graphics context that can be used to render primitives to the
	 *            accelerated canvas provided by LWJGL.
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		// If the board has a visual representation, render it in the graphics context.
		if (game.getBoard().hasImage())
			g.drawImage(game.getBoard().getImage(), game.getBoard().getPosition().x, game.getBoard().getPosition().y);

		game.getBoard().getContinents().forEach(continent -> continent.getCountries().forEach(country -> {

			Image image = country.getImage();
			if (image != null) {
				g.drawImage(image, country.getPosition().x, country.getPosition().y);
			}
		}));

		if (highlightedCountry != null) {

			Image c = highlightedCountry.getImage();

			if (c != null)
				g.drawImage(c, highlightedCountry.getPosition().x, highlightedCountry.getPosition().y);
		}

		clickables.forEach(
				clickable -> g.drawImage(clickable.getImage(), clickable.getPosition().x, clickable.getPosition().y));

		g.drawString(stateName, 5, 50);
		g.drawString(game.getCurrentPlayer().toString(), 5, 35);
		drawArmies(g);
	}

	@Override
	/**
	 * Called as part of slick2d's game loop. Update the state's logic based on the
	 * amount of time that has passed.
	 * 
	 * @param gc
	 *            The game window.
	 * 
	 * @param sbg
	 *            The {@link StateBasedGame} this state is a part of.
	 * 
	 * @param delta
	 *            The amount of time thats passed in millisecond since last update
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	public Game getGame() {
		return game;
	}

	/**
	 * Returns the name of the current state, as a String.
	 * 
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * Draws the {@link army} in its current state over the {@link Country} it is
	 * located.
	 * 
	 * @param g
	 *            A graphics context that can be used to render primitives to the
	 *            accelerated canvas provided by LWJGL.
	 */
	public void drawArmies(Graphics g) {

		// Iterate across every country in every continent on the game board.
		for (Continent continent : game.getBoard().getContinents()) {
			for (Country country : continent.getCountries()) {

				// Sets x and y as the central width and height of the current country.
				int x = country.getPosition().x + country.getWidth() / 2;
				int y = country.getPosition().y + country.getHeight() / 2;

				// Draw a background oval with the rulers colour. If no ruler found default to
				// light grey.
				if (country.getRuler() != null) {
					g.setColor(country.getRuler().getColor());
				} else {
					g.setColor(Color.lightGray);
				}
				g.fillOval(x - 3, y - 3, 15, 25);
				g.setColor(Color.black);

				// If the country has an army, draw a string representing the number of troops
				// within that army at (x,y).
				if (country.getArmy() != null) {
					int troopNumber = country.getArmy().getSize();
					g.drawString(Integer.toString(troopNumber), x, y);
				} else {
					g.drawString(Integer.toString(0), x, y);
				}
			}
		}
	}

	/**
	 * Adds a viewable element to the list of viewables in this state.
	 * 
	 * @param element
	 *            The {@link Viewable} element to be added to the list.
	 * 
	 */
	public void addElement(Viewable element) {
		viewables.add(element);
	}

	/*
	 * Adds a clickable element to the list of clickables in this state.
	 * 
	 * @param element The {@link Clickable} element to be added to the list.
	 * 
	 */
	public void addElement(Clickable element) {
		// Check whether the region is valid.
		if (element.hasRegion()) {
			clickables.add(element);
		} else {
			throw new IllegalArgumentException("Clickable elemnent must have a valid region.");
		}
	}

	/**
	 * Returns a list of {@link Clickable} elements present in this state.
	 * 
	 */
	public List<Clickable> getClickableElements() {
		return clickables;
	}
	
	/**
	 * Returns a the current highlighted {@link Country} in this state.
	 * 
	 */
	public Country getHighlightedCountry() {
		return highlightedCountry;
	}
}
