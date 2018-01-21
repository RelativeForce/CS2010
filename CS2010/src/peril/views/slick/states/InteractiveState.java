package peril.views.slick.states;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Point;
import peril.controllers.GameController;
import peril.views.slick.Button;
import peril.views.slick.Clickable;
import peril.views.slick.Container;
import peril.views.slick.SlickGame;
import peril.views.slick.Viewable;
import peril.views.slick.components.Component;
import peril.views.slick.components.menus.HelpMenu;

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
	private final String stateName;

	/**
	 * Holds the the current {@link Game} this {@link InteractiveState} is
	 * associated with.
	 */
	protected final GameController game;
	
	protected final SlickGame slick;

	/**
	 * A {@link List} of {@link Button} elements that this {@link InteractiveState}
	 * has.
	 */
	private final Map<String, Button> buttons;

	/**
	 * A {@link List} of {@link Viewable} elements that this
	 * {@link InteractiveState} has.
	 */
	private final Set<Viewable> images;

	/**
	 * Holds all the {@link Component}s that will be initialised when this
	 * {@link InteractiveState} is initialised.
	 */
	private final List<Component> components;

	/**
	 * The id of this {@link InteractiveState}.
	 */
	private final int id;

	/**
	 * Holds the id of the {@link HelpMenu} page that explains this
	 * {@link InteractiveState}.
	 */
	private final int helpId;

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
	public InteractiveState(GameController game, String stateName, int id, int helpId) {

		if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		} else if (stateName.isEmpty()) {
			throw new NullPointerException("StateName cannot be empty.");
		}else if(!(game.getView() instanceof SlickGame)) {
			throw new IllegalArgumentException("The current game view is not compatible with this state. Use the Slick2D view.");
		}
		
		this.helpId = helpId;
		this.id = id;
		this.game = game;
		this.slick = (SlickGame) game.getView();
		this.stateName = stateName;
		this.buttons = new HashMap<>();
		this.images = new HashSet<>();
		this.components = new LinkedList<>();
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
	 * Returns the name of the current state, as a String.
	 * 
	 */
	public final String getStateName() {
		return stateName;
	}

	/**
	 * Returns the {@link String} name of this state.
	 */
	public final String getName() {
		return getStateName();
	}

	/*
	 * Adds a {@link Button} to the list of buttons in this state.
	 * 
	 */
	@Override
	public void addButton(Button button) {
		buttons.put(button.id, button);
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
	public void enter(GameContainer gc, StateBasedGame sbg) {
		if (helpId != HelpMenu.NULL_PAGE) {
			game.setHelpMenuPage(helpId);
		}
	}

	/**
	 * Called when this {@link InteractiveState} is being left this method should
	 * perform all exit state operations.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {

	};

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
		components.forEach(c -> c.init());
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

	/**
	 * Retrieves the the <code>int</code> id of this {@link InteractiveState}.
	 */
	@Override
	public final int getID() {
		return id;
	}

	/**
	 * Retrieves a {@link Button} from this {@link InteractiveState} by id.
	 */
	public Button getButton(String buttonId) {
		if (!buttons.containsKey(buttonId)) {
			throw new IllegalArgumentException(
					"'" + buttonId + "' button is not in " + stateName + " interactive state.");
		}
		return buttons.get(buttonId);
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
		for (Button button : buttons.values()) {

			// If the click is in the current element
			if (button.isClicked(click) && button.isVisible()) {

				// Click the button
				button.click();
				return true;

			}
		}
		return false;
	}

	/**
	 * Adds an {@link Viewable} image to this {@link InteractiveState}.
	 */
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
		buttons.forEach((id, button) -> {
			if (button.isVisible()) {
				g.drawImage(button.getImage(), button.getPosition().x, button.getPosition().y);
			}
		});
	}

	/**
	 * Add {@link Component} to this {@link InteractiveState} so that it will be
	 * initialises when the {@link InteractiveState} is. This method will not be
	 * used once the {@link InteractiveState} is initialised.
	 * 
	 * @param component
	 *            The {@link Component} to be added.
	 */
	protected void addComponent(Component component) {
		components.add(component);
	}

	/**
	 * Draws this {@link Game}'s {@link HelpMenu} on screen.
	 * 
	 * @param g
	 *            {@link Graphics}
	 */
	protected void drawHelp(Graphics g) {
		slick.menus.helpMenu.draw(g);
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
	 * Changes the {@link Music} that is currently playing for the {@link Music} of
	 * the {@link Game#getCurrentState()}.
	 * 
	 * @param gc
	 *            {@link GameContainer}
	 */
	protected void changeMusic(GameContainer gc) {

		gc.setMusicOn(false);
		gc.setMusicOn(true);

		if (getMusic() != null) {
			getMusic().play();
		}

	}
}
