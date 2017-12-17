package peril.ai.api;

import java.util.Map;
import java.util.Set;

public interface Board {

	Map<String, ? extends Continent> getContinents();
	
	int getNumberOfCountries();
	
	Set<? extends Country> getCountries();
}
