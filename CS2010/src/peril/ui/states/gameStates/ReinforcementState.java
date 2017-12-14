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
import peril.ui.Font;

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
	public ReinforcementState(Game game, int id) {
		super(game, STATE_NAME, id);
		this.reinforceButton = "reinforce";
		this.unitFont = new Font("Arial", Color.white, 50);
		this.textFont = new Font("Arial", Color.white, 20);
	}

	/**
	 * Enters this {@link ReinforcementState}.
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		super.enter(gc, sbg);
		getGame().menus.pauseMenu.showSaveOption();
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
		getGame().menus.pauseMenu.hideSaveOption();
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

		String units = Integer.toString(getGame().players.getCurrent().distributableArmy.getSize());

		unitFont.draw(g, units, 150 - (unitFont.getWidth(units) / 2), 45);

		textFont.draw(g, "UNITS", 150 - (textFont.getWidth("UNITS") / 2), 95);

		super.drawPopups(g);

		super.drawHelp(g);

		super.drawPauseMenu(g);

		super.drawChallengeMenu(g);
	}

	/**
	 * Moves the reinforce {@link Button} to be positioned at the top left of the
	 * current country.
	 * 
	 * @param country
	 *            {@link Country}
	 */
	public void moveReinforceButton(Country country) {

		Point armyPosition = getCenterArmyPosition(country);

		int x = armyPosition.x;
		int y = armyPosition.y + 25;

		getButton(reinforceButton).setPosition(new Point(x, y));

	}

	/**
	 * Reinforces the selected {@link Country}.
	 */
	public void reinfoce() {

		// Holds the currently highlighted country
		Country highlightedCountry = getSelected();

		// Holds the current player.
		Player player = getGame().players.getCurrent();

		// If there is a country highlighted.
		if (highlightedCountry != null) {

			// If the player has any units to place
			if (player.distributableArmy.getSize() > 0) {

				Player ruler = highlightedCountry.getRuler();

				// If the highlighted country has a ruler and it is that player
				if (ruler != null && ruler.equals(player)) {

					// Get that country's army and increase its size by one.
					highlightedCountry.getArmy().add(1);

					// Remove the unit from the list of units to place.
					player.distributableArmy.remove(1);
					player.totalArmy.add(1);
					getGame().players.checkChallenges(getGame().states.reinforcement);

					if (player.distributableArmy.getSize() == 0) {
						getButton(reinforceButton).hide();
					}

				} else {
					System.out.println(player.toString() + " does not rule this country");
				}

			} else {
				System.out.println("No units to distribute.");
			}

		} else {
			System.out.println("No country selected.");
		}

	}

	/**
	 * The {@link Country} that is to be selected must be owned by the current
	 * {@link Player} in order to be selected.
	 */
	@Override
	protected boolean select(Country country) {

		// If the country is null then set the primary highlighted as null and
		// de-highlight the current enemy country.
		if (country != null) {

			// Holds the current player
			Player player = getGame().players.getCurrent();

			// Holds the ruler of the country
			Player ruler = country.getRuler();

			if (player.equals(ruler)) {

				removeHighlight(getSelected());

				if (player.distributableArmy.getSize() > 0) {
					moveReinforceButton(country);
					getButton(reinforceButton).show();
				} else {
					getButton(reinforceButton).hide();
				}

				addHighlight(country);
				setSelected(country);
				return true;

			}

		} else {
			getButton(reinforceButton).hide();
			removeHighlight(getSelected());
			return true;
		}

		removeSelected();
		return false;
	}

	/**
	 * Pans this {@link ReinforcementState}.
	 */
	@Override
	protected void panElements(Point panVector) {
		Point current = getButton(reinforceButton).getPosition();
		getButton(reinforceButton).setPosition(new Point(current.x + panVector.x, current.y + panVector.y));
	}

}
