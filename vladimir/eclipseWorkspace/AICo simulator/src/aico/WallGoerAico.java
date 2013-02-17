package aico;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import aico.steps.Step;
import aico.steps.StepGoNorthUntilOfTheEndOfLine;
import aico.steps.StepGoSouthUntilOfTheEndOfLine;
import aico.steps.StepGoToEastWall;
import aico.steps.StepLiftUp;
import aico.steps.StepSearchForTheHole;
import aico.steps.StepTwoMetersToEast;

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

	private final RangeSensorBelt sonars;
	private final CameraSensor camera;
	private final BufferedImage cameraImage;

	private List<Step> steps = new ArrayList<Step>();

	public WallGoerAico(Vector3d position, String name) {
		super(position, name);
		setKinematicModel(km);
		sonars = RobotFactory.addSonarBeltSensor(this, 4, 0,
				SENSOR_RANGE_DISTANCE);
		// add a camera on top of the robot
		camera = RobotFactory.addBottomCameraSensor(this);
		// reserve space for image capture
		cameraImage = camera.createCompatibleImage();

		steps.add(new StepLiftUp(this));
		steps.add(new StepGoToEastWall(this));
		steps.add(new StepGoNorthUntilOfTheEndOfLine(this));
		steps.add(new StepTwoMetersToEast(this));
		steps.add(new StepGoSouthUntilOfTheEndOfLine(this));
		steps.add(new StepSearchForTheHole(this));
		steps.add(new StepGoSouthUntilOfTheEndOfLine(this));

		// steps.add(new StepGoToFinish(this));
	}

	public void performBehavior() {
		// if (getCounter() % 50 == 0) {
		// // print each sonars measurement
		// for (int i = 0; i < sonars.getNumSensors(); i++) {
		// double range = sonars.getMeasurement(i);
		// double angle = sonars.getSensorAngle(i);
		// System.out.println("sensor " + i + ": angle - " + angle
		// + ", range - " + range);
		// }
		// }

		camera.copyVisionImage(cameraImage);
		if (isDead()) {
			System.err.println("AICO broken...");
			return;
		}

		boolean inStep = false;
		Step step = null;
		if (inStep = !steps.isEmpty()) {
			step = steps.get(0);
			inStep = !step.isFinished();
			if (inStep) {
				step.perform();
			} else {
				System.out.println(inStep + " " + getCounter());
				steps.remove(0);
			}
		} else {
			System.out.print("Mission completed");
		}
		if (!inStep) {
			km.setTranslationalVelocity(Step.STOP);
			km.setRotationalVelocity(Step.STOP);
			km.setStrafeVelocity(Step.STOP);
			km.setFloatUpVelocity(Step.STOP);
		}
	}

	private boolean isDead() {
		if (collisionDetected()) {
			km.reset();
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		new Simbad(new Labirinth(), false);
	}

	public CopterKinematicModel getKm() {
		return km;
	}

	public RangeSensorBelt getSonars() {
		return sonars;
	}
}