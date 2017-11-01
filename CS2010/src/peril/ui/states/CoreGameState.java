package peril.ui.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import peril.Game;
import peril.board.Continent;
import peril.board.Country;

/**
 * @author Joseph_Rolli, Joshua_Eddy
 */

public abstract class CoreGameState extends BasicGameState {

	/**
	 * Holds the name of a specific {@link CoreGameState}.
	 */
	protected String stateName;
	protected Game game;
	private int x;
	private int y;

	private Country higlightedCountry;

	protected CoreGameState(Game game) {
		this.game = game;
		this.higlightedCountry = null;
	}
	
	

	public void highlight(Country country) {
		higlightedCountry = country;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		System.out.println("Entering gamestate:" + Integer.toString(getID()));
	}

	@Override
	/**
	 * Called when the state is first entered, before slick2d's game loop commences. Initialises the state and loads resources.
	 * 
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gc.setUpdateOnlyWhenVisible(true);

		if (game.getBoard() != null) {
			game.loadAssets();
		}
	}

	@Override
	/**
	 * Called as part of slick2d's game loop. Renders this state to the game's graphics context 
	 * 
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		if (game.getBoard().hasImage())
			g.drawImage(game.getBoard().getImage(), game.getBoard().getPosition().x, game.getBoard().getPosition().y);

		if (higlightedCountry != null) {

			Image c = higlightedCountry.getImage();

			if (c != null)
				g.drawImage(c, higlightedCountry.getPosition().x, higlightedCountry.getPosition().y);
		}

		g.drawString(stateName, 5, 50);
		g.drawString("X" + x + " Y" + y, 700, 700);
		drawArmies(g);
	}

	@Override
	/**
	 * Called as part of slick2d's game loop. Update the state's logic based on the amount of time that has passed.
	 * 
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isMouseButtonDown(0)) {
			x = gc.getInput().getMouseX();
			y = gc.getInput().getMouseY();
		}
	}
	
	/**
	 * Returns the name of the current state, as a String.
	 * 
	 */
	public String getStateName() {
		return stateName;
	}

	
	/**
	 * Draws the {@link army} in its current state over the {@link Country} it is located.
	 * 
	 */
	public void drawArmies(Graphics g) {
		
		//Iterate across every country in every continent on the game board.
		for (Continent continent : game.getBoard().getContinents()) {
			for (Country country : continent.getCountries()) {
				
				//Sets x and y as the central width and height of the current country. 
				int x = country.getPosition().x + country.getWidth()/2;
				int y =	country.getPosition().y + country.getHeight()/2;
				
				//Draw a background oval with the rulers colour. If no ruler found default to light grey.
				if (country.getRuler()!=null) {
				g.setColor(country.getRuler().getColor());
				}else{
				g.setColor(Color.lightGray);
				}
				g.fillOval(x-3, y-3, 15, 25);
				g.setColor(Color.black);
				
				//If the country has an army, draw a string representing the number of troops within that army at (x,y).
				if (country.getArmy() != null) {
					int troopNumber = country.getArmy().getSize();
					g.drawString(Integer.toString(troopNumber), x, y);
				}else {
					g.drawString(Integer.toString(0), x, y);
				}
			}
		}
	}

}
