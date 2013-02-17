package aico.steps;

import aico.WallGoerAico;

public class StepGoSouthUntilOfTheEndOfLine extends Step {

	public StepGoSouthUntilOfTheEndOfLine(WallGoerAico copter) {
		super(copter);
	}

	@Override
	public void perform() {
		setDirection(SOUTH);
	}

	@Override
	public boolean isFinished() {
		return getRangeToWall(SOUTH) < 1;
	}

}
