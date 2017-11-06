package peril.ui.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;
import peril.Player;
import peril.Point;
import peril.board.Board;
import peril.board.Country;
import peril.ui.Button;
import peril.ui.visual.Clickable;
import peril.ui.visual.Region;

/**
 * Encapsulates the behaviour of the Reinforcement {@link CoreGameState} where
 * the {@link Player} places their units from
 * {@link Player#getDistributableArmySize()} on their {@link Country}s.
 * 
 * @author Joseph_Rolli, Joshua_Eddy
 *
 */
public class ReinforcementState extends CoreGameState {

	/**
	 * The ID of this {@link ReinforcementState}
	 */
	private static final int ID = 1;

	public ReinforcementState(Game game) {
		super(game);
		stateName = "Reinforcement";
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		// Set the text color to magenta
		g.setColor(Color.magenta);

		super.render(gc, sbg, g);

		// Set the text color to magenta
		g.setColor(Color.magenta);

		// Draw player name
		g.drawString("Units to place: " + game.getCurrentPlayer().getDistributableArmySize(), 5, 35);
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void parseClick(int button, Point click) {

		// If the states buttons were not clicked click the board.
		if (!clickButton(click)) {
			clickBoard(click);
		}

	}

	@Override
	public void parseButton(int key, char c) {
		
		Country highlighted = getHighlightedCountry();

		switch (key) {
		case Input.KEY_1:
			if (highlighted != null)
				highlighted.setRuler(Player.PLAYERONE);
			break;
		case Input.KEY_2:
			if (highlighted != null)
				highlighted.setRuler(Player.PLAYERTWO);
			break;
		case Input.KEY_3:
			if (highlighted != null)
				highlighted.setRuler(Player.PLAYERTHREE);
			break;
		case Input.KEY_4:
			if (highlighted != null)
				highlighted.setRuler(Player.PLAYERFOUR);
			break;
		case Input.KEY_SPACE:
			if (highlighted != null)
				highlighted.setRuler(null);
			break;
		case Input.KEY_N:
			game.nextPlayer();
			break;

		}		
	}
	
	/**
	 * Simulate a click a specified {@link Point} and check if any of the
	 * {@link Clickable} {@link Button}s are intersected by the {@link Point}.
	 * 
	 * @param click
	 *            {@link Point}
	 * @return whether any {@link Button} was intersected by the {@link Point}.
	 */
	private boolean clickButton(Point click) {

		// Iterate through all the buttons in the current state.
		for (Clickable element : ((CoreGameState) game.getCurrentState()).getClickableElements()) {

			// If the click is in the current element
			if (element.isClicked(click)) {

				// If the element is a button
				if (element instanceof Button) {

					// Click the button
					((Button) element).click();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Simulates a click at a {@link Point} on the {@link Board} and highlights the
	 * {@link Country} that clicked.
	 * 
	 * @param click
	 *            {@link Point}
	 */
	private void clickBoard(Point click) {

		// Holds the game board
		Board board = game.getBoard();

		// If there is a game board
		if (board != null) {

			// Get the country that is clicked.
			Country clickedCountry = board.getCountry(click);

			// Get the currently highlighted country
			Country highlighted = getHighlightedCountry();

			// If a country was clicked
			if (clickedCountry != null) {

				// Highlight the country that was clicked
				clickedCountry.setImage(clickedCountry.getRegion().getPosition(),
						clickedCountry.getRegion().convert(Color.yellow));

				// If the currently highlighted country and the clicked country are different
				// unhighlight the current country.
				if (highlighted != null && !clickedCountry.equals(highlighted)) {
					unhighlightCountry(highlighted);
				}

				// Highlight the clicked country
				highlightCountry(clickedCountry);
				System.out.println(clickedCountry.getName());
			} else {
				unhighlightCountry(highlighted);
				highlightCountry(null);
			}
		}
	}

	/**
	 * Removes the highlight colouring effect on a {@link Country}.
	 * 
	 * @param toUnhighlight
	 *            {@link Country}
	 */
	private void unhighlightCountry(Country toUnhighlight) {

		// If there is a country to unhighlight
		if (toUnhighlight != null) {

			// Holds the position of the country
			Point position = toUnhighlight.getRegion().getPosition();

			// Holds the region of the country
			Region region = toUnhighlight.getRegion();

			// Holds the ruler of the country
			Player ruler = toUnhighlight.getRuler();

			// If there is a ruler then return the colour of the country to that of the
			// ruler. Otherwise remove the highlight effect.
			if (ruler != null) {
				toUnhighlight.setImage(position, region.convert(ruler.getColor()));
			} else {
				toUnhighlight.setImage(null, null);
			}

		}

	}

	
}
