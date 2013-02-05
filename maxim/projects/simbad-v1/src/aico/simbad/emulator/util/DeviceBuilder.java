package aico.simbad.emulator.util;

import javax.vecmath.Vector3d;

import aico.simbad.emulator.robot.Robot;

import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

public class DeviceBuilder extends RobotFactory{
	static public RangeSensorBelt addDistanceBeltSensor(Robot agent, int nbSonars) {
		double agentHeight = agent.getHeight();
		double agentRadius = agent.getRadius();
		AicoRangeSensor sonarBelt = new AicoRangeSensor((float) agentRadius,
				0f, Constants.DISTANCE_SENSOR_MEASURE_LENGTH, nbSonars,
				RangeSensorBelt.TYPE_LASER, 0);
		sonarBelt.setUpdatePerSecond(3);
		sonarBelt.setName("sonars");
		Vector3d pos = new Vector3d(0, agentHeight / 2, 0.0);
		agent.addSensorDevice(sonarBelt, pos, 0);
		return sonarBelt;
	}
}
