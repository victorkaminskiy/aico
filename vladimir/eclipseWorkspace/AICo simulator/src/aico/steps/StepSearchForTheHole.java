package aico.steps;

import aico.WallGoerAico;

public class StepSearchForTheHole extends Step {
	boolean firstEndFound = false;
	boolean secondEndFound = false;
	boolean centerReached = false;
	int timeToReachOtherEnd = -1;
	int timeSearchStarted = -1;

	public StepSearchForTheHole(WallGoerAico copter) {
		super(copter);
	}

	@Override
	public void perform() {
		if (!firstEndFound) {
			setDirection(EAST);
			if (getRangeToWall(SOUTH) > 2) {
				firstEndFound = true;
			}
		} else if (!secondEndFound) {
			if (timeToReachOtherEnd == -1) {
				timeToReachOtherEnd = copter.getCounter();
			}
			setDirection(EAST);
			if (getRangeToWall(SOUTH) < 2) {
				secondEndFound = true;
				timeToReachOtherEnd = copter.getCounter() - timeToReachOtherEnd;
			}
		} else if (!centerReached) {
			if (timeSearchStarted == -1) {
				timeSearchStarted = copter.getCounter();
			}
			setDirection(WEST);
			if (copter.getCounter() - timeSearchStarted >= timeToReachOtherEnd/2){
				centerReached = true;
			}
		}
	}

	@Override
	public boolean isFinished() {
		return centerReached;
	}

}
