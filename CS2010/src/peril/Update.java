package peril;

public class Update<T> {

	public final String property;
	
	public final T oldValue;
	
	public final T newValue;
	
	public Update(String property, T oldValue, T newValue) {
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
}
