package peril.ui.states;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.ui.Button;
import peril.ui.states.gameStates.CoreGameState;
import peril.ui.visual.Clickable;
import peril.ui.visual.Viewable;

public abstract class InteractiveState extends BasicGameState {

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
	 * A {@link List} of {@link Button} elements that this {@link CoreGameState}
	 * has.
	 */
	private List<Button> buttons;

	/**
	 * A {@link List} of {@link Viewable} elements that this {@link CoreGameState}
	 * has.
	 */
	protected List<Viewable> viewables;

	public InteractiveState(Game game) {

		if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		}

		this.game = game;
		this.buttons = new LinkedList<>();
		this.viewables = new LinkedList<>();
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

	/**
	 * Gets the {@link Game} that uses this {@link CoreGameState}.
	 * 
	 * @return {@link Game}
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Simulate a click a specified {@link Point} and check if any of the
	 * {@link Clickable} {@link Button}s are intersected by the {@link Point}.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return whether any {@link Button} was intersected by the {@link Point}.
	 */
	protected boolean clickButton(Point click) {

		// Iterate through all the buttons in the current state.
		for (Button button : game.getCurrentState().getButtons()) {

			// If the click is in the current element
			if (button.isClicked(click)) {

				// Click the button
				button.click();
				return true;

			}
		}
		return false;
	}

	/**
	 * Returns the name of the current state, as a String.
	 * 
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * Returns a list of {@link Clickable} elements present in this state.
	 * 
	 */
	public List<Button> getButtons() {
		return buttons;
	}

	/*
	 * Adds a {@link Button} to the list of buttons in this state.
	 * 
	 * @param element The {@link Button} element to be added to the list.
	 * 
	 */
	public void addButton(Button element) {
		// Check whether the region is valid.
		if (element.hasRegion()) {
			buttons.add(element);
		} else {
			throw new IllegalArgumentException("Clickable elemnent must have a valid region.");
		}
	}

	/**
	 * Processes a click at a {@link Point} on this {@link CoreGameState}.
	 * 
	 * @param button
	 *            <code>int</code> button
	 * @param click
	 *            {@link Point} of the click
	 */
	public abstract void parseClick(int button, Point click);

	/**
	 * Processes a button press on this {@link CoreGameState}.
	 * 
	 * @param key
	 *            <code>int</code> key code
	 * @param c
	 *            <code>char</code> char on the button
	 * 
	 * @see org.newdawn.slick.Input
	 */
	public abstract void parseButton(int key, char c);

	@Override
	public abstract void init(GameContainer gc, StateBasedGame sbg) throws SlickException;

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		buttons.forEach(button -> g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y));
	}

	@Override
	public abstract void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException;

	@Override
	public abstract int getID();

}
