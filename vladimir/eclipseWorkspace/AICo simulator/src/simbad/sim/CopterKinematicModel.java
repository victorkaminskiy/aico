package simbad.sim;

import java.text.DecimalFormat;

import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;

public class CopterKinematicModel extends DefaultKinematic {
	private double strafeVelocity = 0.0;
	private double floatUpVelocity = 0.0;

	public void setStrafeVelocity(double sv) {
		this.strafeVelocity = sv;
	}

	public double getStrafeVelocity() {
		return strafeVelocity;
	}

	public void setFloatUpVelocity(double fv) {
		this.floatUpVelocity = fv;
	}

	public double getFloatUpVelocity() {
		return floatUpVelocity;
	}

	@Override
	protected void update(double elapsedSecond, Transform3D rotation,
			Vector3d instantTranslation, Vector3d instantRotation) {
		instantTranslation.set(getTranslationalVelocity() * elapsedSecond, //
				elapsedSecond * getFloatUpVelocity(),//
				elapsedSecond * getStrafeVelocity());
		// apply current rotation (on y axis)
		rotation.transform(instantTranslation);

		// perform rotation - on y axis
		instantRotation.set(0, elapsedSecond * getRotationalVelocity(), 0);
	}

	@Override
	public void reset() {
		super.reset();
		strafeVelocity = 0.0;
		floatUpVelocity = 0.0;
	}

	@Override
	public String toString(DecimalFormat format) {
		return super.toString(format);
	}

}
