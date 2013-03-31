package aico.simbad.emulator.robot;

import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import aico.simbad.emulator.util.AicoAgent;
import aico.simbad.emulator.util.Constants;
import aico.simbad.emulator.util.DataBlock;
import aico.simbad.emulator.util.AicoDeviceBuilder;

import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import simbad.sim.SensorDevice;

public class Robot extends AicoAgent {
	
	final NavigationAlgorithm navalgo;

	final RangeSensorBelt sonars;
	final RangeSensorBelt bumpers;
	final CameraSensor camera;
	final BufferedImage cameraImage;

	public Robot(Vector3d pos, String name) {
		super(pos, name);
		sonars = AicoDeviceBuilder.addAicoDistanceBeltSensor(this, 4);
		bumpers = RobotFactory.addBumperBeltSensor(this, 8);
		camera = RobotFactory.addBottomCameraSensor(this);
		cameraImage = camera.createCompatibleImage();
		navalgo = new NavigationAlgorithm();
	}

	public void initBehavior() {
		// Point in new direction
//		rotateY(-Math.PI / 2);
	}

	public void performBehavior() {
		// Build block of current data
		if (getCounter() % Constants.REFRESH_INTERVAL == 0) {
			DataBlock data = new DataBlock(sonars.getNumSensors());

			// Get data from distance sensors
			for (int i = 0; i < sonars.getNumSensors(); i++) {
				data.getDistanceMeasurement()[i] = sonars.getMeasurement(i);
				data.getDistanceSensorAngle()[i] = sonars.getSensorAngle(i);
				data.getDistanceHasHit()[i] = sonars.hasHit(i);

				// Logging
				System.out.println("Sonar at angle "
						+ data.getDistanceSensorAngle()[i] + " measured range ="
						+ data.getDistanceMeasurement()[i]
						+ " has hit something:" + data.getDistanceHasHit()[i]);
			}

			EngineActions nextAction = navalgo.calcNextAction(data);
			performEngineAction(nextAction);

		}

	}

	
	/**
	 * Perform engine action, depending on received action from
	 * navigation algorithm. Navigation algorithm does not know
	 * how to do any type of actions, it just knows the list
	 * of available actions (EngineActions). This method knows
	 * how to make this actions in real life.
	 * @param action abstraction of action which will be executed
	 */
	private void performEngineAction(EngineActions action) {
		switch (action) {
		case MOVE_FORWARD:
			setAicoTranslationalVelocity(Constants.TRANSLATION_VELOCITY);
			break;
		case MOVE_BACKWARD:
			setAicoTranslationalVelocity(-Constants.TRANSLATION_VELOCITY);
			break;
		case MOVE_LEFTWARD:
			setAicoTranslationalVelocity(0);
			setAicoShiftVelocity(-Constants.TRANSLATION_VELOCITY);
			break;
		case MOVE_RIGHTWARD:
			setAicoTranslationalVelocity(0);
			setAicoShiftVelocity(Constants.TRANSLATION_VELOCITY);
			break;
		case MOVE_STOP:
			setAicoTranslationalVelocity(0);
			setAicoShiftVelocity(0);
			break;
		case ROTATE_LEFT:
			setAicoRotationalVelocity(Constants.ROTATION_VELOCITY);
			break;
		case ROTATE_RIGHT:
			setAicoRotationalVelocity(-Constants.ROTATION_VELOCITY);
			break;
		case ROTATE_STOP:
			setAicoRotationalVelocity(0);
			break;
		case ALL_STOP:
			setAicoTranslationalVelocity(0);
			setAicoShiftVelocity(0);
			setAicoRotationalVelocity(0);
			break;
		default:
			System.err.println("Not all EngineActions are implemented yet!");
			break;
		}
	}

	/** 
	 * This function used is AicoDeviceBuilder for attaching belt of sensors.
	 */
	public int addSensorDevice(SensorDevice sd, Vector3d position, double angle) {
		return super.addSensorDevice(sd, position, angle);
	}

}
