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
import peril.ui.visual.Clickable;
import peril.ui.visual.Viewable;

/**
 * 
 * A state that allows the user to click on {@link Buttons} and displays
 * {@link Viewable} objects.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class InteractiveState extends BasicGameState {

	/**
	 * Holds the name of a specific {@link InteractiveState}.
	 */
	private String stateName;

	/**
	 * Holds the the current {@link Game} this {@link InteractiveState} is
	 * associated with.
	 */
	private Game game;

	/**
	 * A {@link List} of {@link Button} elements that this {@link InteractiveState}
	 * has.
	 */
	private List<Button> buttons;

	/**
	 * A {@link List} of {@link Viewable} elements that this
	 * {@link InteractiveState} has.
	 */
	private List<Viewable> viewables;

	/**
	 * The id of this {@link InteractiveState}.
	 */
	private final int id;

	/**
	 * Constructs a new {@link InteractiveState}.
	 * 
	 * @param game
	 *            The {@link Game} this state is a part of.
	 * @param stateName
	 *            Holds the name of a specific {@link InteractiveState}.
	 * @param id
	 *            The id of this {@link InteractiveState}.
	 */
	public InteractiveState(Game game, String stateName, int id) {

		if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		} else if (stateName.isEmpty()) {
			throw new NullPointerException("StateName cannot be empty.");
		}

		this.id = id;
		this.game = game;
		this.stateName = stateName;
		this.buttons = new LinkedList<>();
		this.viewables = new LinkedList<>();
	}

	/**
	 * Adds a {@link Viewable} element to the list of {@link Viewable}s in this
	 * state.
	 * 
	 * @param element
	 *            The {@link Viewable} element to be added to the list.
	 * 
	 */
	public void addVisual(Viewable element) {
		viewables.add(element);
	}

	/**
	 * Gets the {@link Game} that uses this {@link InteractiveState}.
	 * 
	 * @return {@link Game}
	 */
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
	 * Processes a click at a {@link Point} on this {@link InteractiveState}.
	 * 
	 * @param button
	 *            <code>int</code> button
	 * @param click
	 *            {@link Point} of the click
	 */
	public abstract void parseClick(int button, Point click);

	/**
	 * Processes a button press on this {@link InteractiveState}.
	 * 
	 * @param key
	 *            <code>int</code> key code
	 * @param c
	 *            <code>char</code> char on the button
	 * 
	 * @see org.newdawn.slick.Input
	 */
	public abstract void parseButton(int key, char c);

	/**
	 * Called when the state is entered, before slick2d's game loop commences.
	 * 
	 * @param gc
	 *            The game window.
	 * 
	 * @param sbg
	 *            The {@link StateBasedGame} this state is a part of.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		System.out.println("Entering gamestate: " + stateName);
	}

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
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.setUpdateOnlyWhenVisible(true);
		game.loadAssets();
	}

	/**
	 * Called as part of slick2d's game loop. Renders this state to the game's
	 * graphics context. Draws all the {@link Button}s then the {@link Viewable}
	 * objects.
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
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		buttons.forEach(button -> g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y));
	}

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
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

	}

	@Override
	public int getID() {
		return id;
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

}
