package aico.steps;

import aico.WallGoerAico;
import simbad.sim.CopterKinematicModel;

public abstract class Step {
	public final static double SPEED = 1;
	public final static double STOP = 0;
	public final static int EAST = 101;
	public final static int NORTH = 102;
	public final static int UP = 103;
	public final static int WEST = 104;
	public final static int SOUTH = 105;
	public final static int DOWN = -106;
	protected WallGoerAico copter;

	public Step(WallGoerAico copter) {
		this.copter = copter;
	}

	public abstract void perform();

	public abstract boolean isFinished();

	protected double getRangeToWall(double direction) {
		int sensorNo = -1;
		if (direction == NORTH) {
			sensorNo = 0;
		} else if (direction == WEST) {
			sensorNo = 1;
		} else if (direction == SOUTH) {
			sensorNo = 2;
		} else if (direction == EAST) {
			sensorNo = 3;
		}
		double range = copter.getSonars().getMeasurement(sensorNo);
//		System.out.println("Direction: " + direction + ", dist:" + range);
		return range;
	}

	protected void setDirection(int direction) {
		CopterKinematicModel m = copter.getKm();
		m.setTranslationalVelocity(STOP);
		m.setFloatUpVelocity(STOP);
		m.setStrafeVelocity(STOP);
		m.setRotationalVelocity(STOP);

		switch (direction) {
		case EAST:
			m.setStrafeVelocity(SPEED);
			break;
		case WEST:
			m.setStrafeVelocity(-SPEED);
			break;
		case NORTH:
			m.setTranslationalVelocity(SPEED);
			break;
		case SOUTH:
			m.setTranslationalVelocity(-SPEED);
			break;
		case UP:
			m.setFloatUpVelocity(SPEED);
			break;
		case DOWN:
			m.setFloatUpVelocity(-SPEED);
			break;
		}
	}
}
