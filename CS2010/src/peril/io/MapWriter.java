package peril.io;

import java.util.HashSet;
import java.util.Set;

import peril.Game;
import peril.board.Country;

public class MapWriter {

	private Set<Country> savedLinks;
	
	private Game game;

	public MapWriter(Game game) {
		this.savedLinks = new HashSet<>();
		this.game = game;
	}
	
	public void write() {
		
		
		
		
	}
	

	private String parseCountry(Country country) {

		StringBuilder line = new StringBuilder();

		line.append("country");
		line.append(',');

		// Country name
		line.append(country.getName());
		line.append(',');

		// Country RGB
		line.append(formatRGB(country.getColor().getRed()));
		line.append(formatRGB(country.getColor().getGreen()));
		line.append(formatRGB(country.getColor().getBlue()));
		line.append(',');

		// Army Size
		line.append(country.getArmy().getSize());
		line.append(',');
		
		// Army offset
		line.append(Integer.toString(country.getArmy().getOffset().x));
		line.append(',');
		line.append(Integer.toString(country.getArmy().getOffset().y));
		line.append(',');
		
		// Player ruler
		line.append(country.getRuler().toString());

		return line.toString();
	}

	private String formatRGB(int value) {

		String str = "" + value;

		int preceedingZeros = 2 - (int) Math.log10((double) value);

		for (int index = 0; index < preceedingZeros; index++) {
			str = "0" + str;
		}

		return str;

	}
}
