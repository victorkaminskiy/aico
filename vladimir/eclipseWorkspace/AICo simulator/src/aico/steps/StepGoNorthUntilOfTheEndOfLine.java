package aico.steps;

import aico.WallGoerAico;

public class StepGoNorthUntilOfTheEndOfLine extends Step {

	public StepGoNorthUntilOfTheEndOfLine(WallGoerAico copter) {
		super(copter);
	}

	@Override
	public boolean isFinished() {
		return getRangeToWall(NORTH) <= 1;
	}

	@Override
	public void perform() {
		setDirection(NORTH);
	}

}
