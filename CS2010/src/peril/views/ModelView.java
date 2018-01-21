package peril.views;

import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelHazard;
import peril.model.states.ModelState;

public interface ModelView {

	Object getVisualCountry(ModelCountry country);
	
	Object getVisualContinent(ModelContinent continent);
	
	Object getVisualBoard(ModelBoard board);
	
	Object getVisualHazard(ModelHazard hazard);
	
	Object getVisualArmy(ModelArmy army);
	
	Object getVisualState(ModelState state);
	
	Object getVisualPlayer(ModelPlayer player);
	
	void clear();
	
	void addCountry(Object country);
	
	void addContinent(Object continent);
	
	void addPlayer(Object player);
	
	void addArmy(Object army);
	
	void addBoard(Object board);
	
	void addState(Object state);
	
	void addHazard(Object hazard);

	void init(GameController gc);
	
}
