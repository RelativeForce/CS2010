package peril.views.slick;

import java.util.IdentityHashMap;
import java.util.Map;

import peril.controllers.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelHazard;
import peril.model.states.ModelState;
import peril.views.ModelView;
import peril.views.slick.board.SlickArmy;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickContinent;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickHazard;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.states.gameStates.CoreGameState;

public final class SlickModelView implements ModelView {

	private final Map<ModelCountry, SlickCountry> countries;
	private final Map<ModelContinent, SlickContinent> continents;
	private final Map<ModelBoard, SlickBoard> boards;
	private final Map<ModelHazard, SlickHazard> hazards;
	private final Map<ModelState, CoreGameState> states;
	private final Map<ModelArmy, SlickArmy> armies;
	private final Map<ModelPlayer, SlickPlayer> players;

	private boolean isInitialised;

	public SlickModelView() {
		countries = new IdentityHashMap<>();
		continents = new IdentityHashMap<>();
		hazards = new IdentityHashMap<>();
		states = new IdentityHashMap<>();
		armies = new IdentityHashMap<>();
		boards = new IdentityHashMap<>();
		players = new IdentityHashMap<>();
		isInitialised = false;
	}
	
	public void clear() {
		countries.clear();
		continents.clear();
		hazards.clear();
		states.clear();
		armies.clear();
		boards.clear();
		players.clear();
	}

	public SlickCountry getVisualCountry(ModelCountry country) {
		return countries.get(country);
	}

	@Override
	public SlickContinent getVisualContinent(ModelContinent continent) {
		return continents.get(continent);
	}

	@Override
	public SlickBoard getVisualBoard(ModelBoard board) {
		return boards.get(board);
	}

	@Override
	public SlickHazard getVisualHazard(ModelHazard hazard) {
		return hazards.get(hazard);
	}

	@Override
	public SlickArmy getVisualArmy(ModelArmy army) {
		return armies.get(army);
	}

	@Override
	public CoreGameState getVisualState(ModelState state) {
		return states.get(state);
	}

	@Override
	public SlickPlayer getVisualPlayer(ModelPlayer player) {
		return players.get(player);
	}

	@Override
	public void init(GameController gc) {

		if (!isInitialised) {
			isInitialised = true;
			
			// When this is initialised add the board.
			addBoard(new SlickBoard(gc.getModelBoard(), this));

		}
	}

	private void checkInitialised() {
		if (!isInitialised) {
			throw new IllegalStateException("The Slick model view is not initialised.");
		}
	}

	@Override
	public void addCountry(Object country) {
		checkInitialised();

		if (!(country instanceof SlickCountry)) {
			throw new IllegalArgumentException("The specifed country is not a slick2d country.");
		}

		SlickCountry slickCountry = (SlickCountry) country;

		countries.put(slickCountry.model, slickCountry);

	}

	@Override
	public void addContinent(Object continent) {
		checkInitialised();

		if (!(continent instanceof SlickContinent)) {
			throw new IllegalArgumentException("The specifed continent is not a slick2d continent.");
		}

		SlickContinent slickContinent = (SlickContinent) continent;

		continents.put(slickContinent.model, slickContinent);

	}

	@Override
	public void addPlayer(Object player) {
		
		checkInitialised();

		if (!(player instanceof SlickPlayer)) {
			throw new IllegalArgumentException("The specifed player is not a slick2d player.");
		}

		SlickPlayer slickPlayer = (SlickPlayer) player;

		players.put(slickPlayer.model, slickPlayer);


	}

	@Override
	public void addArmy(Object army) {
		
		checkInitialised();

		if (!(army instanceof SlickArmy)) {
			throw new IllegalArgumentException("The specifed army is not a slick2d army.");
		}

		SlickArmy slickArmy = (SlickArmy) army;

		armies.put(slickArmy.model, slickArmy);

	}

	@Override
	public void addBoard(Object board) {
		
		checkInitialised();

		if (!(board instanceof SlickBoard)) {
			throw new IllegalArgumentException("The specifed board is not a slick2d board.");
		}

		SlickBoard slickBoard = (SlickBoard) board;

		boards.put(slickBoard.model, slickBoard);
		
	}

	@Override
	public void addState(Object state) {
		
		checkInitialised();

		if (!(state instanceof CoreGameState)) {
			throw new IllegalArgumentException("The specifed state is not a slick2d core game state.");
		}

		CoreGameState slickState = (CoreGameState) state;

		states.put(slickState.model, slickState);

	}

	@Override
	public void addHazard(Object hazard) {
		
		checkInitialised();

		if (!(hazard instanceof SlickHazard)) {
			throw new IllegalArgumentException("The specifed hazard is not a slick2d hazard.");
		}

		SlickHazard slickHazard = (SlickHazard) hazard;

		hazards.put(slickHazard.model, slickHazard);

	}

}
