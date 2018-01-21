package peril.controllers;

import java.util.function.Consumer;

import peril.controllers.api.Player;
import peril.model.ModelPlayer;
import peril.model.board.ModelBoard;
import peril.model.board.ModelCountry;
import peril.views.View;

public interface GameController {

	/**
	 * Retrieves the current {@link Player}.
	 * 
	 * @return
	 */
	ModelPlayer getCurrentModelPlayer();

	/**
	 * Sets the {@link Game} help menu to the specified page id.
	 * 
	 * @param pageId
	 */
	void setHelpMenuPage(int pageId);
	
	View getView();	
	
	String getUIPath();

	String getMapsPath();
	
	void resetGame();
	
	void setBoardName(String name);

	void addPlayer(ModelPlayer player);
	
	AIController getAIController();
	
	ModelBoard getModelBoard();
	
	void forEachModelCountry(Consumer<ModelCountry> task);

	boolean isPlaying(int playerNumber);
	
	ModelPlayer getModelPlayer(int playerNumber);

	void checkChallenges();

	void nextPlayer();
}
