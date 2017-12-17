package peril.ai.api;

import java.util.function.Consumer;

import peril.Game;

public class AIRequestHandler implements AIController {

	private final Game game;

	public AIRequestHandler(Game game) {
		this.game = game;
	}

	@Override
	public Board getBoard() {
		return game.board;
	}

	@Override
	public boolean select(Country country) {

		// Get the id of the current state.
		int currentStateId = game.getCurrentStateID();

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(country instanceof peril.board.Country)) {
			throw new IllegalArgumentException("The parmameter country is not a valid country.");
		}

		peril.board.Country checkedCountry = (peril.board.Country) country;

		if (currentStateId == game.states.reinforcement.getID()) {
			return game.states.reinforcement.select(checkedCountry);
		} else if (currentStateId == game.states.combat.getID()) {
			return game.states.combat.select(checkedCountry);
		} else if (currentStateId == game.states.movement.getID()) {
			return game.states.movement.select(checkedCountry);
		} else {
			throw new IllegalStateException("The current state is not a valid game state.");

		}
	}

	@Override
	public void forEachCountry(Consumer<Country> task) {
		getBoard().getCountries().forEach(task);
	}

	@Override
	public void attack() {

		// Check correct state
		if (game.getCurrentStateID() != game.states.combat.getID()) {
			throw new IllegalStateException("You can only attack during the combat state.");
		}

		// Check both countries are selected.
		if (game.states.combat.getPrimary() == null || game.states.combat.getSecondary() == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}

		game.menus.warMenu.show();
		game.menus.warMenu.selectMaxDice();
		game.menus.warMenu.attack();

		// Cant attack any more hide the war menu.
		if (game.states.combat.getPrimary() == null || game.states.combat.getSecondary() == null) {
			game.menus.warMenu.hide();
			game.states.movement.removeSelected();
		}

	}

	@Override
	public void fortify() {

		// Check correct state
		if (game.getCurrentStateID() != game.states.movement.getID()) {
			throw new IllegalStateException("You can only attack during the fortify state.");
		}

		// Check both countries are selected.
		if (game.states.movement.getPrimary() == null || game.states.movement.getSecondary() == null) {
			throw new IllegalStateException("There is NOT two countries selected. Select two valid countries.");
		}

		game.states.movement.fortify();
		game.states.movement.removeSelected();

	}

	@Override
	public boolean isPathBetween(Country safe, Country border) {

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(safe instanceof peril.board.Country)) {
			throw new IllegalArgumentException("The parmameter safe country is not a valid country.");
		}

		peril.board.Country checkedSafe = (peril.board.Country) safe;

		// Ensure that the parameter Country is a valid country, This should never be
		// false.
		if (!(safe instanceof peril.board.Country)) {
			throw new IllegalArgumentException("The parmameter border country is not a valid country.");
		}

		peril.board.Country checkedBorder = (peril.board.Country) border;

		return game.states.movement.isPathBetween(checkedSafe, checkedBorder);
	}

	@Override
	public void reinforce() {

		// Check correct state
		if (game.getCurrentStateID() != game.states.reinforcement.getID()) {
			throw new IllegalStateException("You can only attack during the reinforcement state.");
		}

		// Check both countries are selected.
		if (game.states.reinforcement.getSelected() == null ) {
			throw new IllegalStateException("There is valid country selected.");
		}
		
		game.states.reinforcement.reinforce();
		

	}

	
	@Override
	public Player getCurrentPlayer() {
		return game.players.getCurrent();
	}

}
