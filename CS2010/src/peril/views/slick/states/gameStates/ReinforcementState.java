package peril.views.slick.states.gameStates;

import java.util.Observable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.controllers.GameController;
import peril.model.states.Reinforce;
import peril.views.slick.Frame;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.board.SlickUnit;
import peril.views.slick.util.Button;
import peril.views.slick.util.Font;
import peril.views.slick.util.Point;

/**
 * Encapsulates the behaviour of the Reinforcement {@link CoreGameState} where
 * the {@link SlickPlayer} places their units from
 * {@link SlickPlayer#getDistributableArmyStrength()} on their
 * {@link SlickCountry}s.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public final class ReinforcementState extends CoreGameState {

	/**
	 * The {@link Font} for displaying the {@link Player's} available units to
	 * distribute.
	 */
	private final Font unitFont;

	/**
	 * The {@link Font} used to display "UNITS"
	 */
	private final Font textFont;

	/**
	 * Holds the instance of the reinforce {@link Button}.
	 */
	private final String reinforceButton;

	private String upgradeButton;

	/**
	 * Constructs a new {@link ReinforcementState}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link ReinforcementState} is a part of.
	 * @param id
	 *            The ID of this {@link ReinforcementState}
	 */
	public ReinforcementState(GameController game, int id, Reinforce model) {
		super(game, model.getName(), id, model);
		this.reinforceButton = "reinforce";
		this.upgradeButton = "upgrades";
		this.unitFont = new Font("Arial", Color.white, 100);
		this.textFont = new Font("Arial", Color.white, 60);

		model.addObserver(this);
	}

	/**
	 * Enters this {@link ReinforcementState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		menus.showSaveOption();
		getButton(reinforceButton).hide();
		getButton(upgradeButton).hide();
		slick.showToolTip("Click the '?' for help.");
	}

	/**
	 * Initialises this {@link ReinforcementState}.
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		super.init(gc, sbg);
		unitFont.init();
		textFont.init();
	}

	/**
	 * Performs the exit state operations specific to this
	 * {@link ReinforcementState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		getButton(reinforceButton).hide();
		menus.hideSaveOption();
	}

	/**
	 * Renders this {@link ReinforcementState} on screen.
	 */
	@Override
	public void render(GameContainer gc, Frame frame) {
		super.render(gc, frame);

		super.drawAllLinks(frame);
		super.drawArmies(frame);
		super.drawImages();
		super.drawButtons();
		super.drawPlayerName(frame);

		String units = Integer.toString(game.getCurrentModelPlayer().distributableArmy.getStrength());

		frame.draw(unitFont, units, 295 - (unitFont.getWidth(units) / 2), 70);

		frame.draw(textFont, "UNITS", 295 - (textFont.getWidth("UNITS") / 2), 50 + unitFont.getHeight());

		super.drawMiniMap(frame);

		menus.draw(frame);

	}

	@Override
	public final void update(GameContainer gc, int delta, Frame frame) {
		super.update(gc, delta, frame);
		game.processAI(delta);
	}

	/**
	 * Moves the reinforce {@link Button} to be positioned at the top left of the
	 * current country.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 */
	private void moveReinforceButton(SlickCountry country) {

		Point armyPosition = country.getArmyPosition();

		int x = armyPosition.x;
		int y = armyPosition.y + (SlickUnit.HEIGHT / 2) + 5;

		getButton(reinforceButton).setPosition(new Point(x, y));

	}

	/**
	 * Pans this {@link ReinforcementState}.
	 */
	@Override
	protected void panElements(Point panVector) {
		panButton(getButton(upgradeButton), panVector);
		panButton(getButton(reinforceButton), panVector);
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);

		// If there is no reinforce Button the game is not loaded or there is an error.
		// In either case there should be no full state update.
		if (getButton(reinforceButton) == null) {
			return;
		}

		// If this is the primary country
		if (selected.size() > 0) {
			showUpgradeButton(getButton(upgradeButton));
		}else {
			getButton(upgradeButton).hide();
		}

		if (selected.size() == 1 && game.getCurrentModelPlayer().distributableArmy.getStrength() > 0) {
			getButton(reinforceButton).show();
			moveReinforceButton(selected.get(0));
		} else {
			getButton(reinforceButton).hide();
		}

	}
}
