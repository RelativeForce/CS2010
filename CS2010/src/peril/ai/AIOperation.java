package peril.ai;

import java.util.LinkedList;
import java.util.List;

import peril.controllers.api.Country;

public class AIOperation {

	public boolean processAgain;
	
	public final List<Country> select;
	
	public AIOperation() {
		this.select = new LinkedList<>();
		this.processAgain = false;
	}
	
}