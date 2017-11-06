package peril.ui.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import peril.Game;

public class ReinforcementState extends CoreGameState {

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
		// TODO Auto-generated method stub
		return 1;
	}
}
