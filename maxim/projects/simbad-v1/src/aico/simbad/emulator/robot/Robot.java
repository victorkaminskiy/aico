package aico.simbad.emulator.robot;

import java.awt.image.BufferedImage;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import aico.simbad.emulator.util.DeviceBuilder;

import simbad.sim.Agent;
import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import simbad.sim.SensorDevice;

public class Robot extends Agent {

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
	}

	public void initBehavior() {
		// Point in new direction
		rotateY(-Math.PI / 2);
	}

	public void performBehavior() {
		if (collisionDetected()) {
			// Stop the robot and make it red
			setTranslationalVelocity(0.0);
			setRotationalVelocity(0);
			setColor(new Color3f(1, 0, 0));
		} else {
			setTranslationalVelocity(0.5);
		}

		// every 20 frames - sonar
		if (getCounter() % 20 == 0) {
			// print each sonars measurement
			for (int i = 0; i < sonars.getNumSensors(); i++) {
				double range = sonars.getMeasurement(i);
				double angle = sonars.getSensorAngle(i);
				boolean hit = sonars.hasHit(i);
				System.out.println("Sonar at angle " + angle
						+ "measured range =" + range + " has hit something:"
						+ hit);
			}
		}

		// every 20 frames - bumper
		if (getCounter() % 20 == 0) {
			// print each bumper state
			for (int i = 0; i < bumpers.getNumSensors(); i++) {
				double angle = bumpers.getSensorAngle(i);
				boolean hit = bumpers.hasHit(i);
				System.out.println("Bumpers at angle " + angle
						+ " has hit something:" + hit);
			}
		}

		// get camera image
		camera.copyVisionImage(cameraImage);
		// process image
		// ... use BufferedImage api
	}

	public int addSensorDevice(SensorDevice sd, Vector3d position, double angle) {
		return super.addSensorDevice(sd, position, angle);
	}

}
