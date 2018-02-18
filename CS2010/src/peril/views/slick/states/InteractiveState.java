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

import peril.controllers.GameController;
import peril.views.slick.Container;
import peril.views.slick.Frame;
import peril.views.slick.SlickGame;
import peril.views.slick.components.Component;
import peril.views.slick.components.menus.HelpMenu;
import peril.views.slick.util.Button;
import peril.views.slick.util.Point;
import peril.views.slick.util.Viewable;

/**
 * 
 * A {@link BasicGameState} that allows the user to click on {@link Buttons} and
 * displays {@link Viewable} objects. It is also a {@link Container}.
 * 
 * @author Joshua_Eddy
 * 
 * @since 2018-02-17
 * @version 1.01.01
 * 
 * @see Container
 * @see BasicGameState
 *
 */
public abstract class InteractiveState extends BasicGameState implements Container {

	/**
	 * Holds the {@link GameController} that allows the {@link InteractiveState} to
	 * interact with the game.
	 */
	protected final GameController game;

	/**
	 * The {@link SlickGame} that allows the {@link InteractiveState} to interact
	 * with the visual representation of the game.
	 */
	protected final SlickGame slick;

	/**
	 * Holds the name of a specific {@link InteractiveState}.
	 */
	private final String stateName;

	/**
	 * A {@link Map} of {@link Button} names to their {@link Button}s elements that
	 * this {@link InteractiveState} has.
	 */
	private final Map<String, Button> buttons;

	/**
	 * A {@link Set} of {@link Viewable} elements that this {@link InteractiveState}
	 * has.
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
	 * The {@link Frame} that displays the {@link InteractiveState} to the user.
	 */
	private final Frame frame;

	/**
	 * Constructs a new {@link InteractiveState}.
	 * 
	 * @param game
	 *            The {@link GameController} this state is a part of.
	 * @param stateName
	 *            Holds the name of a specific {@link InteractiveState}.
	 * @param id
	 *            The id of this {@link InteractiveState}.
	 * @param helpId
	 *            The id of this {@link InteractiveState}'s help page.
	 */
	public InteractiveState(GameController game, String stateName, int id, int helpId) {

		if (game == null) {
			throw new NullPointerException("Game cannot be null.");
		} else if (stateName.isEmpty()) {
			throw new NullPointerException("StateName cannot be empty.");
		} else if (!(game.getView() instanceof SlickGame)) {
			throw new IllegalArgumentException(
					"The current game view is not compatible with this state. Use the Slick2D view.");
		}

		this.helpId = helpId;
		this.id = id;
		this.game = game;
		this.slick = (SlickGame) game.getView();
		this.stateName = stateName;
		this.buttons = new HashMap<>();
		this.images = new HashSet<>();
		this.components = new LinkedList<>();
		this.frame = new Frame();
	}

	/**
	 * Draws the current {@link Frame}.
	 * 
	 * @param gc
	 *            The {@link GameContainer} that contains the game.
	 * @param frame
	 *            The {@link Frame} that displays the {@link InteractiveState} to
	 *            the user.
	 */
	public abstract void render(GameContainer gc, Frame frame);

	/**
	 * Updates the state of the frame objects before they are drawn again.
	 * 
	 * @param gc
	 *            The {@link GameContainer} that contains the game.
	 * @param delta
	 *            The number of milliseconds between the current frame and the
	 *            previous.
	 * @param frame
	 *            The {@link Frame} that displays the {@link InteractiveState} to
	 *            the user.
	 */
	public abstract void update(GameContainer gc, int delta, Frame frame);

	/**
	 * The {@link Music} that plays in the background of this
	 * {@link InteractiveState}.
	 *
	 * @return The {@link Music} of this {@link InteractiveState}.
	 */
	public abstract Music getMusic();

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
	 * Assigns the pan direction of the {@link InteractiveState}.
	 * 
	 * @param mousePosition
	 *            The {@link Point} position of the mouse.
	 */
	public void parseMouse(Point mousePosition) {
		// TODO Add support for mouse hovering.
	}

	/**
	 * Performs the operations when this {@link InteractiveState} is exited.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);

		frame.clearToolTips();
	}

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
	 * Processes a button press on this {@link InteractiveState}.
	 * 
	 * @param key
	 *            <code>int</code> key code
	 * @param mousePosition
	 *            The {@link Point} position of the mouse relative to the
	 *            {@link GameContainer}.
	 */
	public void parseButton(int key, Point mousePosition) {
		frame.pressButton(key, mousePosition);
	}

	/**
	 * Adds a {@link Viewable} element to the list of {@link Viewable}s in this
	 * state.
	 * 
	 * @param element
	 *            The {@link Viewable} element to be added to the list.
	 * 
	 */
	public final void addVisual(Viewable element) {
		images.add(element);
	}

	/**
	 * Returns the name of the current state, as a String.
	 */
	public final String getStateName() {
		return stateName;
	}

	/**
	 * Returns the {@link String} name of this state.
	 */
	@Override
	public final String getName() {
		return getStateName();
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
	public final void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		frame.updateFrame(delta);
		update(gc, delta, frame);
	}

	/**
	 * Adds a String as a tool tip to this {@link InteractiveState} to be displayed
	 * to the user.
	 * 
	 * @param toolTip
	 *            <code>String</code>
	 */
	public final void showToolTip(String toolTip, Point position) {

		// Display the tool tip for 8 seconds
		frame.addToolTip(toolTip, position, 8000);

	}

	/*
	 * Adds a {@link Button} to the list of buttons in this state.
	 */
	@Override
	public final void addButton(Button button) {
		buttons.put(button.id, button);
	}

	/**
	 * Processes a click on this {@link InteractiveState}.
	 * 
	 * @param mouse
	 *            The {@link Point} position of the mouse click.
	 * @param button
	 *            The mouse button that was pressed.
	 */
	public final void click(Point mouse, int button) {
		frame.click(mouse, button);
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
	public final void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		// Refresh the frame.
		frame.newFrame(g);

		// Render the frame
		render(gc, frame);

		// Draw the tool tips
		frame.drawToolTips();

	}

	/**
	 * Retrieves the the <code>int</code> id of this {@link InteractiveState}.
	 */
	@Override
	public final int getID() {
		return id;
	}

	/**
	 * Retrieves a {@link Button} from this {@link InteractiveState} by id. If the
	 * specified id is not found this shall return <code>null</code>.
	 */
	public final Button getButton(String buttonId) {
		return buttons.get(buttonId);
	}

	/**
	 * Adds an {@link Viewable} image to this {@link InteractiveState}.
	 */
	@Override
	public final void addImage(Viewable image) {
		images.add(image);
	}

	/**
	 * Draws all the {@link Button}s in this {@link InteractiveState}.
	 */
	protected final void drawButtons() {
		buttons.forEach((id, button) -> {
			if (button.isVisible()) {
				frame.draw(button);
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
	protected final void addComponent(Component component) {
		components.add(component);
	}

	/**
	 * Draws all the {@link Viewable}s in this {@link InteractiveState}.
	 */
	protected final void drawImages() {
		images.forEach(image -> frame.draw(image.getImage(), image.getPosition().x, image.getPosition().y));
	}

	/**
	 * Clears all the tool tips that are currently on screen.
	 */
	protected final void clearToolTips() {
		frame.clearToolTips();
	}

	/**
	 * Changes the {@link Music} that is currently playing for the {@link Music} of
	 * the current state.
	 * 
	 * @param gc
	 *            The {@link GameContainer}.
	 */
	protected final void changeMusic(GameContainer gc) {

		gc.setMusicOn(false);
		gc.setMusicOn(true);

		if (getMusic() != null) {
			getMusic().play();
		}

	}
}
