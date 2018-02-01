package peril.views;

import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelHazard;
import peril.model.board.ModelUnit;
import peril.model.states.ModelState;

public interface ModelView {

	Object getVisual(ModelCountry country);
	
	Object getVisual(ModelContinent continent);
	
	Object getVisual(ModelBoard board);
	
	Object getVisual(ModelHazard hazard);
	
	Object getVisual(ModelArmy army);
	
	Object getVisual(ModelState state);
	
	Object getVisual(ModelPlayer player);
	
	Object getVisual(ModelUnit unit);
	
	void clear();
	
	void addUnit(Object unit);
	
	void addCountry(Object country);
	
	void addContinent(Object continent);
	
	void addPlayer(Object player);
	
	void addArmy(Object army);
	
	void addBoard(Object board);
	
	void addState(Object state);
	
	void addHazard(Object hazard);

	void init(GameController gc);
	
}
