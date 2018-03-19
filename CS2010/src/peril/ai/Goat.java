package peril.ai;

/**
 * 
 * My {@link AI}.
 * 
 * @author James_Rowntree
 * 
 * @since 2018-02-16
 * @version 1.01.01
 *
 */
public final class Goat extends AI {

	private static final String NAME = "Goat";

	public Goat(AIController api) {
		super(NAME, MAX_SPEED, api);
	}

	@Override
	protected AIOperation processReinforce(AIController api) {
		// TODO Auto-generated method stub
		return new AIOperation();
	}

	@Override
	protected AIOperation processAttack(AIController api) {
		// TODO Auto-generated method stub
		return new AIOperation();
	}

	@Override
	protected AIOperation processFortify(AIController api) {
		// TODO Auto-generated method stub
		return new AIOperation();
	}

}
