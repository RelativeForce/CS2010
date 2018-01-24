package peril.views.slick.states.gameStates;

import java.util.Observable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.ai.AI;
import peril.controllers.GameController;
import peril.model.states.Reinforce;
import peril.views.slick.Button;
import peril.views.slick.Font;
import peril.views.slick.Point;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickPlayer;

/**
 * Encapsulates the behaviour of the Reinforcement {@link CoreGameState} where
 * the {@link SlickPlayer} places their units from
 * {@link SlickPlayer#getDistributableArmySize()} on their {@link SlickCountry}s.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public final class ReinforcementState extends CoreGameState {

	/**
	 * The name of a specific {@link ReinforcementState}.
	 */
	private static final String STATE_NAME = "Reinforcement";

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

	/**
	 * Constructs a new {@link ReinforcementState}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link ReinforcementState} is a part of.
	 * @param id
	 *            The ID of this {@link ReinforcementState}
	 */
	public ReinforcementState(GameController game, int id, Reinforce model) {
		super(game, STATE_NAME, id, model);
		this.reinforceButton = "reinforce";
		this.unitFont = new Font("Arial", Color.white, 50);
		this.textFont = new Font("Arial", Color.white, 20);
		
		model.addObserver(this);
	}

	/**
	 * Enters this {@link ReinforcementState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		slick.menus.pauseMenu.showSaveOption();
		getButton(reinforceButton).hide();
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
		slick.menus.pauseMenu.hideSaveOption();
	}

	/**
	 * Renders this {@link ReinforcementState} on screen.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		super.drawAllLinks(g);
		super.drawArmies(g);
		super.drawImages(g);
		super.drawButtons(g);
		super.drawPlayerName(g);

		String units = Integer.toString(game.getCurrentModelPlayer().distributableArmy.getSize());

		unitFont.draw(g, units, 150 - (unitFont.getWidth(units) / 2), 45);
		textFont.draw(g, "UNITS", 150 - (textFont.getWidth("UNITS") / 2), 95);

		super.drawPopups(g);
		super.drawHelp(g);
		super.drawPauseMenu(g);
		super.drawChallengeMenu(g);
		
		g.destroy();
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		super.update(gc, sbg, delta);
		
		if(game.getCurrentModelPlayer().ai != AI.USER && !game.getCurrentModelPlayer().ai.reinforce(delta)) {
			slick.enterState(slick.states.combat);
		}
	}

	/**
	 * Moves the reinforce {@link Button} to be positioned at the top left of the
	 * current country.
	 * 
	 * @param country
	 *            {@link SlickCountry}
	 */
	private void moveReinforceButton(SlickCountry country) {

		Point armyPosition = getCenterArmyPosition(country);

		int x = armyPosition.x;
		int y = armyPosition.y + 25;

		getButton(reinforceButton).setPosition(new Point(x, y));

	}

	/**
	 * Pans this {@link ReinforcementState}.
	 */
	@Override
	protected void panElements(Point panVector) {
		Point current = getButton(reinforceButton).getPosition();
		getButton(reinforceButton).setPosition(new Point(current.x + panVector.x, current.y + panVector.y));
	}

	@Override
	public void update(Observable o, Object arg) {
		super.update(o, arg);
	
		if(!selected.isEmpty()) {
			getButton(reinforceButton).show();
			moveReinforceButton(selected.get(0));
		}else {
			getButton(reinforceButton).hide();
		}

	}
}
