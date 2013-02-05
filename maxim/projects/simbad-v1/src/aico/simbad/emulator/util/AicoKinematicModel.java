package aico.simbad.emulator.util;

import java.text.DecimalFormat;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;

import simbad.sim.KinematicModel;

/**
 * This is the standard kinematic with 3 control parameters : translation, shift
 * and rotation.
 */
public class AicoKinematicModel extends KinematicModel {

	/** Translational velocity in meters per second. */
	private double translationalVelocity;
	/** Shift velocity in meters per second. */
	private double shiftVelocity;
	/** Rotational velocity in radians per second */
	private double rotationalVelocity;

	public AicoKinematicModel() {
		reset();
	}

	/** Resets all control parameters to their initial values. */
	protected void reset() {
		rotationalVelocity = 0;
		translationalVelocity = 0;
		shiftVelocity = 0;
	}

	/**
	 * Compute instant translation and rotation vectors .
	 * 
	 * @param elapsedSecond
	 *            time elapsed
	 * @param rotationcurrent
	 *            rotation
	 * @param instantTranslation
	 *            to store translation
	 * @param instantRotation
	 *            to store rotation
	 */

	protected void update(double elapsedSecond, Transform3D rotation,
			Vector3d instantTranslation, Vector3d instantRotation) {

		instantTranslation.set(translationalVelocity * elapsedSecond, 0.0, shiftVelocity * elapsedSecond);
		// apply current rotation (on y axis)
		rotation.transform(instantTranslation);

		// perform rotation - on y axis
		instantRotation.set(0, elapsedSecond * rotationalVelocity, 0);

	}

	/**
	 * Sets rotational velocity in radians per second.
	 */
	public final void setRotationalVelocity(double rv) {
		rotationalVelocity = rv;
	}

	/**
	 * Sets translational velocity in meter per second.
	 */
	public final void setTranslationalVelocity(double tv) {
		translationalVelocity = tv;
	}

	/**
	 * Sets shift velocity in meter per second.
	 */
	public final void setShiftVelocity(double tv) {
		shiftVelocity = tv;
	}

	/**
	 * Gets rotational velocity in radians per second
	 */
	public final double getRotationalVelocity() {
		return rotationalVelocity;
	}

	/**
	 * Gets translational velocity in meter per second.
	 */
	public final double getTranslationalVelocity() {
		return translationalVelocity;
	}

	/**
	 * Gets shift velocity in meter per second.
	 */
	public final double getShiftVelocity() {
		return shiftVelocity;
	}

	/** Resets all control parameters to their initial values. */
	protected String toString(DecimalFormat format) {
		return "kinematic \t= DefaultKinematic\n" + "rotVelocity   \t= "
				+ format.format(rotationalVelocity) + " rad/s\n"
				+ "transVelocity \t= " + format.format(translationalVelocity)
				+ " m/s\n" + "shiftVelocity \t= "
				+ format.format(shiftVelocity) + " m/s\n";
	}
}
