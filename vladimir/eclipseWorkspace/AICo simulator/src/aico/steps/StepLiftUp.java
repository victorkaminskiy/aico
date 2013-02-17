package aico.steps;

import aico.WallGoerAico;

public class StepLiftUp extends Step {

	public StepLiftUp(WallGoerAico copter) {
		super(copter);
	}

	@Override
	public void perform() {
		setDirection(UP);
	}

	@Override
	public boolean isFinished() {
		return copter.getCounter() % 30 == 0 && copter.getCounter() != 0;
	}

}
