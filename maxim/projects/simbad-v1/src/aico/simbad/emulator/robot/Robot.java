package aico.simbad.emulator.robot;

import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import aico.simbad.emulator.util.Constants;
import aico.simbad.emulator.util.DataBlock;
import aico.simbad.emulator.util.DeviceBuilder;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import simbad.sim.SensorDevice;

public class Robot extends Agent {
	
	final NavAlgo navalgo;

	final RangeSensorBelt sonars;
	final RangeSensorBelt bumpers;
	final CameraSensor camera;
	final BufferedImage cameraImage;

	public Robot(Vector3d pos, String name) {
		super(pos, name);
		sonars = DeviceBuilder.addDistanceBeltSensor(this, 4);
		bumpers = RobotFactory.addBumperBeltSensor(this, 8);
		camera = RobotFactory.addCameraSensor(this);
		cameraImage = camera.createCompatibleImage();
		navalgo = new NavAlgo();
	}

	public void initBehavior() {
		// Point in new direction
		rotateY(-Math.PI / 2);
		// Set body color
		setColor(new Color3f(0, 1, 0));
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
						+ data.getDistanceSensorAngle()[i] + "measured range ="
						+ data.getDistanceMeasurement()[i]
						+ " has hit something:" + data.getDistanceHasHit()[i]);
			}

			EngineActions nextAction = navalgo.calcNextAction(data);
			performMovement(nextAction);

		}

	}

	private void performMovement(EngineActions action) {
		switch (action) {
		case MOVE_FORWARD:
			setTranslationalVelocity(Constants.TRANSLATION_VELOCITY);
			break;
		case MOVE_BACKWARD:
			setTranslationalVelocity(-Constants.TRANSLATION_VELOCITY);
			break;
		case MOVE_LEFT:
			// TODO Move left not realized
			break;
		case MOVE_RIGHT:
			// TODO Move right not realized
			break;
		case MOVE_STOP:
			setTranslationalVelocity(0);
			break;
		case ROTATE_LEFT:
			setRotationalVelocity(Constants.ROTATION_VELOCITY);
			break;
		case ROTATE_RIGHT:
			setRotationalVelocity(-Constants.ROTATION_VELOCITY);
			break;
		case ROTATE_STOP:
			setRotationalVelocity(0);
			break;
		default:
			break;
		}
	}

	public int addSensorDevice(SensorDevice sd, Vector3d position, double angle) {
		return super.addSensorDevice(sd, position, angle);
	}

}
