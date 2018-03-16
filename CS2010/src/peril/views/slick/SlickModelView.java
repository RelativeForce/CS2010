package peril.views.slick;

import java.util.IdentityHashMap;
import java.util.Map;

import peril.GameController;
import peril.model.ModelPlayer;
import peril.model.board.ModelArmy;
import peril.model.board.ModelBoard;
import peril.model.board.ModelContinent;
import peril.model.board.ModelCountry;
import peril.model.board.ModelHazard;
import peril.model.board.ModelUnit;
import peril.model.board.links.ModelLinkState;
import peril.model.states.ModelState;
import peril.views.ModelView;
import peril.views.View;
import peril.views.slick.board.SlickArmy;
import peril.views.slick.board.SlickBoard;
import peril.views.slick.board.SlickContinent;
import peril.views.slick.board.SlickCountry;
import peril.views.slick.board.SlickHazard;
import peril.views.slick.board.SlickLinkState;
import peril.views.slick.board.SlickPlayer;
import peril.views.slick.board.SlickUnit;
import peril.views.slick.states.gameStates.CoreGameState;

/**
 * This maps model objects to their slick visual representations.
 * 
 * @author Joshua_Eddy, Joseph_Rolli
 * 
 * @since 2018-02-28
 * @version 1.01.01
 * 
 * @see ModelView
 * @see View
 *
 */
public final class SlickModelView implements ModelView {

	/**
	 * The {@link Map} of {@link ModelCountry}s to their {@link SlickCountry}s.
	 */
	private final Map<ModelCountry, SlickCountry> countries;

	/**
	 * The {@link Map} of the {@link ModelContinent}s to their
	 * {@link SlickContinent}s.
	 */
	private final Map<ModelContinent, SlickContinent> continents;

	/**
	 * The {@link Map} of {@link ModelBoard}s to their {@link SlickBoard}s.
	 */
	private final Map<ModelBoard, SlickBoard> boards;

	/**
	 * The {@link Map} of {@link ModelHazard}s to their {@link SlickHazard}s.
	 */
	private final Map<ModelHazard, SlickHazard> hazards;

	/**
	 * The {@link Map} of {@link ModelState}s of their {@link CoreGameState}s.
	 */
	private final Map<ModelState, CoreGameState> states;

	/**
	 * The {@link Map} of {@link ModelArmy}s and their {@link SlickArmy}s.
	 */
	private final Map<ModelArmy, SlickArmy> armies;

	/**
	 * The {@link Map} of {@link ModelPlayer}s to their {@link SlickPlayer}s.
	 */
	private final Map<ModelPlayer, SlickPlayer> players;

	/**
	 * THe {@link Map} of {@link ModelUnit}s to their {@link SlickUnit}s.
	 */
	private final Map<ModelUnit, SlickUnit> units;

	/**
	 * The {@link Map} of {@link ModelLinkState}s to their {@link SlickLinkState}s.
	 */
	private final Map<ModelLinkState, SlickLinkState> links;

	/**
	 * Whether or not {@link SlickModelView} has been initialised.
	 */
	private boolean isInitialised;

	/**
	 * Constructs a new {@link SlickModelView}.
	 */
	public SlickModelView() {
		countries = new IdentityHashMap<>();
		continents = new IdentityHashMap<>();
		hazards = new IdentityHashMap<>();
		states = new IdentityHashMap<>();
		armies = new IdentityHashMap<>();
		boards = new IdentityHashMap<>();
		players = new IdentityHashMap<>();
		units = new IdentityHashMap<>();
		links = new IdentityHashMap<>();
		isInitialised = false;
	}

	/**
	 * Clears all the model to visual mappings from this {@link SlickModelView}.
	 */
	@Override
	public void clear() {
		countries.clear();
		continents.clear();
		states.clear();
		armies.clear();
		players.clear();
		units.clear();
	}

	@Override
	public SlickCountry getVisual(ModelCountry country) {
		return countries.get(country);
	}

	@Override
	public SlickContinent getVisual(ModelContinent continent) {
		return continents.get(continent);
	}

	@Override
	public SlickBoard getVisual(ModelBoard board) {
		return boards.get(board);
	}

	@Override
	public SlickHazard getVisual(ModelHazard hazard) {
		return hazards.get(hazard);
	}

	@Override
	public SlickArmy getVisual(ModelArmy army) {
		return armies.get(army);
	}

	@Override
	public CoreGameState getVisual(ModelState state) {
		return states.get(state);
	}

	@Override
	public SlickPlayer getVisual(ModelPlayer player) {
		return players.get(player);
	}

	public SlickUnit getVisual(ModelUnit unit) {
		return units.get(unit);
	}

	@Override
	public void init(GameController gc) {

		if (!isInitialised) {
			isInitialised = true;

			// When this is initialised add the board.
			addBoard(new SlickBoard(gc.getModelBoard(), this));

			for (SlickHazard hazard : SlickHazard.values()) {
				addHazard(hazard);
			}

			for (SlickLinkState link : SlickLinkState.values()) {
				addLinkState(link);
			}

		}
	}

	@Override
	public void addUnit(Object unit) {
		checkInitialised();

		if (!(unit instanceof SlickUnit)) {
			throw new IllegalArgumentException("The specifed unit is not a slick2d unit.");
		}

		SlickUnit slickUnit = (SlickUnit) unit;

		units.put(slickUnit.model, slickUnit);
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

	@Override
	public SlickLinkState getVisual(ModelLinkState link) {
		return links.get(link);
	}

	@Override
	public void addLinkState(Object linkState) {

		checkInitialised();

		if (!(linkState instanceof SlickLinkState)) {
			throw new IllegalArgumentException("The specifed hazard is not a slick2d hazard.");
		}

		SlickLinkState slickLinkState = (SlickLinkState) linkState;

		links.put(slickLinkState.model, slickLinkState);

	}

	private void checkInitialised() {
		if (!isInitialised) {
			throw new IllegalStateException("The Slick model view is not initialised.");
		}
	}
}
