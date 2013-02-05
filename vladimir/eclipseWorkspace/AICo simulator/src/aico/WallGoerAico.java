package aico;

import java.awt.image.BufferedImage;

import javax.vecmath.Vector3d;

import simbad.gui.Simbad;
import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.CopterKinematicModel;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

/**
 * It can move only in 4 directions at one moment: NORTH, SOUTH, EAST, WEST. It
 * has one US sensor on each direction. One camera is built on the bottom of
 * this robot to detect targets for landing.
 * 
 * @author vvpreo
 * 
 */
public class WallGoerAico extends Agent {
	private CopterKinematicModel km = new CopterKinematicModel();
	private static final float SENSOR_RANGE_DISTANCE = 10;
	private final static double SPEED = 1;
	private final static double STOP = 0;
	private final static double EAST = 1 * SPEED;
	private final static double NORTH = 1 * SPEED;
	private final static double UP = 1 * SPEED;
	private final static double WEST = -1 * SPEED;
	private final static double SOUTH = -1 * SPEED;
	private final static double DOWN = -1 * SPEED;

	enum State {
		MovesNorth, //
		MovesEast, //
		MovesSouth, //
		MovesWest, //
		GetsSmallestDistanceToAWall, //
		Stop, //
	}

	private State state = null;
	private final RangeSensorBelt sonars;
	private final CameraSensor camera;
	private final BufferedImage cameraImage;

	public WallGoerAico(Vector3d position, String name) {
		super(position, name);
		setKinematicModel(km);
		sonars = RobotFactory.addSonarBeltSensor(this, 4, 0,
				SENSOR_RANGE_DISTANCE);
		// add a camera on top of the robot
		camera = RobotFactory.addCameraSensor(this);
		// reserve space for image capture
		cameraImage = camera.createCompatibleImage();
	}

	public void initBehavior() {
		state = null;
	}

	public void performBehavior() {
		camera.copyVisionImage(cameraImage);
		if (isDead()) {
			System.err.println("AICO broken...");
			return;
		}

		makeDesicion();

		km.setTranslationalVelocity(STOP);
		km.setStrafeVelocity(WEST);
		km.setFloatUpVelocity(UP);
		km.setRotationalVelocity(Math.PI / 2);

		// switch (state) {
		// case MovesNorth:
		// break;
		// case MovesEast:
		// break;
		// case MovesSouth:
		// break;
		// case MovesWest:
		// break;
		// case GetsSmallestDistanceToAWall:
		// break;
		// case Stop:
		// setTranslationalVelocity(0);
		// break;
		// }

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

	private void makeDesicion() {
		state = State.MovesWest;
	}

	private boolean isDead() {
		if (collisionDetected()) {
			km.reset();
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		final Simbad frame = new Simbad(new Labirinth(), false);
	}
}