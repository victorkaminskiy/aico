package aico.simbad.emulator.util;

import java.awt.image.BufferedImage;

public class DataBlock {
	double translationalValocity;
	double rotationalVelocity;
	double[] distanceMeasurement;
	boolean[] distanceHasHit;
	double[] distanceSensorAngle;
    BufferedImage cameraImage;
	
	public DataBlock(int numberOfDistanceSensors) {
		distanceMeasurement = new double[numberOfDistanceSensors];
		distanceSensorAngle = new double[numberOfDistanceSensors];
		distanceHasHit = new boolean[numberOfDistanceSensors];
	}

	public double getTranslationalValocity() {
		return translationalValocity;
	}

	public void setTranslationalValocity(double translationalValocity) {
		this.translationalValocity = translationalValocity;
	}

	public double getRotationalVelocity() {
		return rotationalVelocity;
	}

	public void setRotationalVelocity(double rotationalVelocity) {
		this.rotationalVelocity = rotationalVelocity;
	}

	public double[] getDistanceMeasurement() {
		return distanceMeasurement;
	}

	public void setDistanceMeasurement(double[] distanceMeasurement) {
		this.distanceMeasurement = distanceMeasurement;
	}

	public double[] getDistanceSensorAngle() {
		return distanceSensorAngle;
	}

	public void setDistanceSensorAngle(double[] distanceSensorAngle) {
		this.distanceSensorAngle = distanceSensorAngle;
	}

	public boolean[] getDistanceHasHit() {
		return distanceHasHit;
	}

	public void setDistanceHasHit(boolean[] distanceHasHit) {
		this.distanceHasHit = distanceHasHit;
	}

    public BufferedImage getCameraImage() {
        return cameraImage;
    }

    public void setCameraImage(BufferedImage cameraImage) {
        this.cameraImage = cameraImage;
    }
}
