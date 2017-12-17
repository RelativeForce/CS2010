package peril.ai.api;

import java.util.List;

public interface Continent {

	Player getOwner();
	
	List<? extends Country> getCountries();
	
}
