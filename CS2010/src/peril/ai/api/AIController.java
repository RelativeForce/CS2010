package peril.ai.api;

import java.util.function.Consumer;

public interface AIController {

	Board getBoard();

	boolean select(Country country);

	void forEachCountry(Consumer<Country> task);

	void attack();

	void fortify();

	boolean isPathBetween(Country safe, Country border);

	void reinforce();

	Player getCurrentPlayer();
}
