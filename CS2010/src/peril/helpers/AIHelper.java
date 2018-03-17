package peril.helpers;

import java.lang.reflect.InvocationTargetException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import peril.GameController;
import peril.ai.*;

/**
 * This class is responsible for retrieving and storing all the {@link AI} which
 * will be used in the game.
 * 
 * @author James_Rowntree, Joshua_Eddy
 * 
 * @since 2018-03-17
 * 
 * @version 1.01.06
 * 
 * @see Iterable
 * @see AI
 */
public final class AIHelper implements Iterable<AI> {

	/**
	 * The {@link Map} that contains all the {@link AI} in the game. {@link AI}s
	 * that are available for the user to select have a true value.
	 */
	private final Map<AI, Boolean> ai;

	/**
	 * The {@link GameController} that allows the {@link AIHelper} to interact with
	 * the game.
	 */
	private final GameController game;

	/**
	 * Constructs a new {@link AIHelper} object.
	 * 
	 * @param game
	 *            The {@link GameController} that allows the {@link AIHelper} to
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
		final NegativeFiveIQ nfiq = new NegativeFiveIQ(api);
		final Ocelot ocelot = new Ocelot(api);
		final Knight knight = new Knight(api);
		final Ernie ernie = new Ernie(api);
		final Noob noob = new Noob(api);

		// Add AIs to ai map
		ai.put(user, true);
		ai.put(monkey, false);
		ai.put(finalBoss, true);
		ai.put(goat, false);
		ai.put(nfiq, false);
		ai.put(ocelot, false);
		ai.put(knight, true);
		ai.put(ernie, true);
		ai.put(noob, false);

	}

	/**
	 * Retrieves the {@link AI} that is associated with a specified String. The
	 * {@link AI} will be retrieved if it is available in game or not.
	 * 
	 * @param name
	 *            The name of the {@link AI}
	 * @return The {@link AI} with that name.
	 */
	public AI getAI(String name) {

		for (AI tempAI : ai.keySet()) {
			if (tempAI.name.equals(name)) {
				return tempAI;
			}
		}

		// Try to find the class for that AI and if it is found, add it to the map of
		// all the AIs and return it.
		try {

			final Class<?> newAIclass = Class.forName("peril.ai." + name);

			final AI newAI = (AI) newAIclass.getDeclaredConstructor(AIController.class)
					.newInstance(game.getAIController());

			ai.put(newAI, false);

			return newAI;

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new AINotFound("The '" + name + "' (AI) could not be fonud.");
		}

	}

	/**
	 * Retrieves the {@link Iterator} that iterates through all the AI's available
	 * in this {@link AIHelper}.
	 */
	@Override
	public Iterator<AI> iterator() {
		// Stream the key set of the AI map.
		return ai.entrySet().stream()

				// Filter out the AIs that are not normally available.
				.filter(entry -> entry.getValue())

				// Map the available AIs to a list of AIs
				.map(entry -> entry.getKey()).collect(Collectors.toList())

				// Return the iterator
				.iterator();
	}
}
