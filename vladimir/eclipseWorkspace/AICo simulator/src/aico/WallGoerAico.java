package aico;

import java.awt.image.BufferedImage;

import simbad.gui.Simbad;
import simbad.sim.*;
import javax.vecmath.Vector3d;

/**
 * It can move only in 4 directions at one moment: NORTH, SOUTH, EAST, WEST. It
 * has one US sensor on each direction. One camera is built on the bottom of
 * this robot to detect targets for landing.
 * 
 * @author vvpreo
 * 
 */
public class WallGoerAico extends Agent {
	private static final float SENSOR_RANGE_DISTANCE = 10;
	private static final double NORTH_DIRECTION = 0;
	private static final double EAST_DIRECTION = Math.PI / 2;
	private static final double SOUTH_DIRECTION = Math.PI;
	private static final double WEST_DIRECTION = Math.PI * 3 / 4;

	enum State {
		Thinks, //
		MovesNorth, //
		MovesEast, //
		MovesSouth, //
		MovesWest, //
		GetsSmallestDistanceToAWall, //
	}

	private double speed = 1;
	private State state = null;
	private final RangeSensorBelt sonars;
	private final CameraSensor camera;
	private final BufferedImage cameraImage;

	public WallGoerAico(Vector3d position, String name) {
		super(position, name);
		sonars = RobotFactory.addSonarBeltSensor(this, 4, 0,
				SENSOR_RANGE_DISTANCE);
		// add a camera on top of the robot
		camera = RobotFactory.addCameraSensor(this);
		// reserve space for image capture
		cameraImage = camera.createCompatibleImage();
	}

	public void initBehavior() {
		state = State.Thinks;
	}

	public void performBehavior() {
		camera.copyVisionImage(cameraImage);
		if (isDead()) {
			System.err.println("AICO broken...");
			return;
		}

		switch (state) {
		case Thinks:
			state = State.MovesWest;
		case MovesNorth:
			move(NORTH_DIRECTION);
			break;
		case MovesEast:
			move(EAST_DIRECTION);
			break;
		case MovesSouth:
			move(SOUTH_DIRECTION);
			break;
		case MovesWest:
			move(WEST_DIRECTION);
			break;
		case GetsSmallestDistanceToAWall:
			break;

		}

		// Get last info from sensors about the world around
		if (getCounter() % 20 == 0) {
			// print each sonars measurement
			for (int i = 0; i < sonars.getNumSensors(); i++) {
				double range = sonars.getMeasurement(i);
				double angle = sonars.getSensorAngle(i);
				boolean hit = sonars.hasHit(i);
				System.out.println("sensor " + i + ": angle - " + angle
						+ ", range - " + range);
			}
		}

		// Choose direction
		// if ((getCounter() % 100) == 0)
		// setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));

		// Make next movement.
		setTranslationalVelocity(1);

		// // every 20 frames - bumper
		// if (getCounter() % 20 == 0) {
		// // print each bumper state
		// for (int i = 0; i < bumpers.getNumSensors(); i++) {
		// double angle = bumpers.getSensorAngle(i);
		// boolean hit = bumpers.hasHit(i);
		// System.out.println("Bumpers at angle " + angle
		// + " has hit something:" + hit);
		// }
		// }

		// get camera image
		// process image
		// ... use BufferedImage api
	}

	private void move(double direction) {
		setRotationalVelocity(direction);
		setTranslationalVelocity(speed);
		setRotationalVelocity(NORTH_DIRECTION);
	}

	private boolean isDead() {
		if (collisionDetected()) {
			setTranslationalVelocity(0.0);
			setRotationalVelocity(0);
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		final Simbad frame = new Simbad(new Labirinth(), false);
	}
}