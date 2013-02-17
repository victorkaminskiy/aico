package aico.steps;

import aico.WallGoerAico;

public class StepGoToEastWall extends Step {

	public StepGoToEastWall(WallGoerAico wallGoerAico) {
		super(wallGoerAico);
	}

	@Override
	public boolean isFinished() {
		return getRangeToWall(EAST) <= 1.0;
	}

	@Override
	public void perform() {
		setDirection(EAST);
	}

}
