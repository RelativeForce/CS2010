package peril;

public class Update<T> {

	public final String property;
	
	public final T newValue;
	
	public Update(String property, T newValue) {
		this.property = property;
		this.newValue = newValue;
	}
	
}
