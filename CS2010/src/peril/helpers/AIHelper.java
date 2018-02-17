package peril.helpers;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import peril.ai.*;
import peril.controllers.AIController;
import peril.controllers.GameController;

/**
 * This class is responsible for retrieving and storing all the {@linkAI} which
 * will be used in the game.
 * 
 * @author James_Rowntree
 * 
 * @since 2018-02-13
 * 
 * @version 1.01.02
 *
 */
public final class AIHelper implements Iterable<AI> {

	/**
	 * The {@link Map} that contains all the {@link AI} that are available for the
	 * user to select.
	 */
	private final Map<String, AI> ai;

	/**
	 * The {@link GameController} that allows the {@link AIHelper} to interact with
	 * the game.
	 */
	private final GameController game;

	/**
	 * Constructs a new {@link AIHelper} object.
	 * 
	 * @param The
	 *            {@link GameController} that allows the {@link AIHelper} to
	 *            interact with the game.
	 * 
	 */
	public AIHelper(GameController game) {
		this.ai = new IdentityHashMap<>();
		this.game = game;
		addAll();
	}

	/**
	 * Defines all the {@link AI} that are available for the user to select.
	 */
	private void addAll() {

		// AI controller api
		final AIController api = game.getAIController();
		
		// Define all AIs
		final AI user = AI.USER;
		final Monkey monkey = new Monkey(api);
		final FinalBoss finalBoss = new FinalBoss(api);
		final Goat goat = new Goat(api);
		final Duckling duckling = new Duckling(api);
		final Ocelot ocelot = new Ocelot(api);

		// Add AIs to ai map
		ai.put(user.name, user);
		ai.put(monkey.name, monkey);
		ai.put(finalBoss.name, finalBoss);
		ai.put(goat.name, goat);
		ai.put(duckling.name, duckling);
		ai.put(ocelot.name, ocelot);

	}

	/**
	 * Retrieves the {@link AI} that is associated with a specified String.
	 * 
	 * @param name
	 *            of the {@link AI}
	 * @return
	 */
	public AI getAI(String name) {
		return ai.get(name);
	}

	/**
	 * Retrieves the {@link Iterator} that iterates through all the AI's in this
	 * {@link AIHelper}.
	 */
	@Override
	public Iterator<AI> iterator() {
		return ai.values().iterator();
	}

}