package aico.steps;

import aico.WallGoerAico;

public class StepTwoMetersToEast extends Step {
	int count = -1;

	public StepTwoMetersToEast(WallGoerAico copter) {
		super(copter);
	}

	@Override
	public void perform() {
		if (count == -1) {
			count = copter.getCounter();
		}
		setDirection(EAST);
	}

	@Override
	public boolean isFinished() {
		return count != -1 && copter.getCounter() - count > 40;
	}

}
