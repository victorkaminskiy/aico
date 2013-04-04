package simbad.sim;

import javax.vecmath.Color3f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;


public class StartOrFinishTarget extends BlockWorldCompositeObject {
	float radius;
	float circumferenceWidth;
	private Color3f backgroundColor;
	private Color3f otherColors;

	public StartOrFinishTarget(Vector2d pos, float radius,
			float circumferenceWidth, Color3f backgroundColor,
			Color3f otherColors, EnvironmentDescription wd) {
		this.radius = radius;
		this.circumferenceWidth = circumferenceWidth;
		this.backgroundColor = backgroundColor;
		this.otherColors = otherColors;
		create3D(wd);
		translateTo(new Vector3d(pos.x, 0, pos.y));
	}

	void create3D(EnvironmentDescription wd) {
		super.create3D();

		WallCylinder bigCircle = new WallCylinder(new Vector3d(0, 0, 0),
				radius, (float) 0.01, wd, otherColors);

		WallCylinder smallCircle = new WallCylinder(new Vector3d(0, 0, 0),
				radius - circumferenceWidth / 2, (float) 0.015, wd,
				backgroundColor);

		Wall l1 = new Wall(new Vector3d(0.0, 0.0, 0.0), (float) 2.6,
				(float) 0.1, (float) 0.02, wd);
		l1.setColor(otherColors);

		Wall l2 = new Wall(new Vector3d(0.0, 0.0, 0.0), (float) 2.6,
				(float) 0.1, (float) 0.02, wd);
		l2.setColor(otherColors);
		l2.rotate90(1);

		addComponent(bigCircle);
		addComponent(smallCircle);
		addComponent(l1);
		addComponent(l2);
	}
}
