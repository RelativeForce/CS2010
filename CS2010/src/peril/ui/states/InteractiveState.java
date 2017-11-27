package peril.ui.states;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.ui.Clickable;
import peril.ui.Container;
import peril.ui.Viewable;
import peril.ui.components.Button;

/**
 * 
 * A state that allows the user to click on {@link Buttons} and displays
 * {@link Viewable} objects.
 * 
 * @author Joshua_Eddy
 *
 */
public abstract class InteractiveState extends BasicGameState implements Container {

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
	private List<Viewable> images;

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
		this.images = new LinkedList<>();
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
		images.add(element);
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
	 * Returns the {@link String} name of this state.
	 */
	public String getName() {
		return getStateName();
	}

	/*
	 * Adds a {@link Button} to the list of buttons in this state.
	 * 
	 */
	@Override
	public void addButton(Button button) {
		// Check whether the region is valid.
		if (button.hasRegion()) {
			buttons.add(button);
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
	 * @param mousePosition
	 *            The {@link Point} position of the mouse relative to the
	 *            {@link GameContainer}.
	 */
	public abstract void parseButton(int key, char c, Point mousePosition);

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
	public abstract void enter(GameContainer gc, StateBasedGame sbg);

	/**
	 * Called when this {@link InteractiveState} is being left this method should
	 * perform all exit state operations.
	 */
	@Override
	public abstract void leave(GameContainer container, StateBasedGame game) throws SlickException;

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
	public abstract void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException;

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

	/**
	 * Retrieves the the <code>int</code> id of this {@link InteractiveState}.
	 */
	@Override
	public int getID() {
		return id;
	}

	/**
	 * 
	 * The {@link Music} that plays in the background of this
	 * {@link InteractiveState}.
	 *
	 * @return {@link Music}
	 */
	public abstract Music getMusic();

	/**
	 * Simulate a click a specified {@link Point} and check if any of the
	 * {@link Clickable} {@link Button}s are intersected by the {@link Point}.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return whether any {@link Button} was intersected by the {@link Point}.
	 */
	@Override
	public boolean clickedButton(Point click) {

		// Iterate through all the buttons in the current state.
		for (Button button : buttons) {

			// If the click is in the current element
			if (button.isClicked(click) && button.isVisible()) {

				// Click the button
				button.click();
				return true;

			}
		}
		return false;
	}

	@Override
	public void addImage(Viewable image) {
		images.add(image);
	}

	/**
	 * Draws all the {@link Button}s in this {@link InteractiveState}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawButtons(Graphics g) {
		buttons.forEach(button -> {
			if (button.isVisible()) {
				g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y);
			}
		});
	}

	/**
	 * Draws all the {@link Viewable}s in this {@link InteractiveState}.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawImages(Graphics g) {
		images.forEach(image -> g.drawImage(image.getImage(), image.getPosition().x, image.getPosition().y));
	}

	/**
	 * Changes the {@link Music} that is currently paying for the {@link Music} of
	 * the {@link Game#getCurrentState()}.
	 * 
	 * @param gc {@link GameContainer}
	 */
	protected void changeMusic(GameContainer gc) {

		gc.setMusicOn(false);
		gc.setMusicOn(true);

		if (getMusic() != null) {
			getMusic().play();
		}

	}
}
