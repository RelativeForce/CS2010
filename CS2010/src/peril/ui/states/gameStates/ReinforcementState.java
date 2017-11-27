package peril.ui.states.gameStates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Country;
import peril.ui.Button;
import peril.ui.components.menus.PauseMenu;

/**
 * Encapsulates the behaviour of the Reinforcement {@link CoreGameState} where
 * the {@link Player} places their units from
 * {@link Player#getDistributableArmySize()} on their {@link Country}s.
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
	 * Holds the instance of the reinforce {@link Button}.
	 */
	private Button reinforceButton;

	/**
	 * Constructs a new {@link ReinforcementState}.
	 * 
	 * @param game
	 *            The {@link Game} this {@link ReinforcementState} is a part of.
	 * @param id
	 *            The ID of this {@link ReinforcementState}
	 * @param pauseMenu
	 *            The {@link PauseMenu} for this {@link ReinforcementState}.
	 */
	public ReinforcementState(Game game, int id, PauseMenu pauseMenu) {
		super(game, STATE_NAME, id, pauseMenu);
	}

	/**
	 * Enters this {@link ReinforcementState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		getGame().pauseMenu.showSaveOption();
	}
	
	/**
	 * Adds a {@link Button} to this {@link ReinforcementState}. The last
	 * {@link Button} with the specified id will be used as the
	 * {@link ReinforcementState#reinforceButton}.
	 */
	@Override
	public void addButton(Button button) {
		super.addButton(button);

		if (button.getId().equals("reinforce")) {
			reinforceButton = button;
			reinforceButton.hide();
		}
	}

	/**
	 * Performs the exit state operations specific to this
	 * {@link ReinforcementState}.
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		reinforceButton.hide();
		getGame().pauseMenu.hideSaveOption();
	}

	/**
	 * Hides the {@link ReinforcementState#reinforceButton}.
	 */
	public void hideReinforceButton() {
		reinforceButton.hide();
	}

	/**
	 * Highlights a {@link Country} in this {@link ReinforcementState}.
	 */
	@Override
	public void highlightCountry(Country country) {

		super.removeHighlightFrom(super.getHighlightedCountry());

		// If the country is null then set the primary highlighted as null and
		// unhighlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().players.getCurrent();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			if (player.equals(ruler)) {

				if (player.getDistributableArmySize() > 0) {
					moveReinforceButton(country);
					reinforceButton.show();
				} else {
					reinforceButton.hide();
				}
				super.highlightCountry(country);

			}

		} else {
			super.highlightCountry(country);
			reinforceButton.hide();
		}

	}

	/**
	 * Renders this {@link ReinforcementState} on screen.
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		super.render(gc, sbg, g);

		super.drawPlayerName(g);

		drawImages(g);
		drawButtons(g);

		// Set the text color to magenta
		g.setColor(Color.black);

		// Draw player name
		g.drawString("Units: " + getGame().players.getCurrent().getDistributableArmySize(), 5, 35);

		drawPauseMenu(g);
	}

	/**
	 * Moves the reinforce {@link Button} to be positioned at the top left of the
	 * current country.
	 * 
	 * @param country
	 *            {@link Country}
	 */
	public void moveReinforceButton(Country country) {

		Point armyPosition = getArmyPosition(country);

		int x = armyPosition.x;
		int y = armyPosition.y + 25;

		reinforceButton.setPosition(new Point(x, y));

	}

	/**
	 * Pans this {@link ReinforcementState}.
	 */
	@Override
	protected void pan(Point panVector) {

		Point old = reinforceButton.getPosition();

		Point vector = getGame().board.move(panVector);

		if (vector.x != 0 || vector.y != 0) {
			reinforceButton.setPosition(new Point(old.x + vector.x, old.y + vector.y));
		}

	}
}
