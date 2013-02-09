package simbad.sim;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingPolytope;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import com.sun.j3d.utils.geometry.Primitive;

public class WallCylinder extends BlockWorldObject {
	/** Object dimension.s */
	float radius;
	float height;

	public WallCylinder(Vector3d pos, float radius, float height,
			EnvironmentDescription wd, Color3f color) {
		this.radius = radius;
		this.height = height;
		pos.y += height / 2;
		create3D(wd, color);
		translateTo(pos);
	}

	private void create3D(EnvironmentDescription wd, Color3f color) {
		super.create3D();
		appearance.setMaterial(new Material());

		int flags = Primitive.GEOMETRY_NOT_SHARED
				| Primitive.ENABLE_GEOMETRY_PICKING
				| Primitive.GENERATE_NORMALS;
		flags |= Primitive.ENABLE_APPEARANCE_MODIFY;

		com.sun.j3d.utils.geometry.Cylinder c = new com.sun.j3d.utils.geometry.Cylinder(
				radius, height, flags, appearance);

		c.setPickable(true);

		// XXX todo the correct boundings
		// BoundingPolytope bp = new BoundingPolytope();
		// bp.setPlanes(new Vector4d[] //
		// { //
		// new Vector4d(1, height, 1, -1 * radius / 2), //
		// new Vector4d(1, height, -1, radius / 2), //
		// new Vector4d(1, height, 1, -1 * radius / 2), //
		// new Vector4d(1, height, -1, radius / 2), //
		// });

		
		// unreal boundings
		BoundingBox b = new BoundingBox(new Point3d(0.0001, 0.0001, 0.0001),
				new Point3d(-0.0001, -0.0001, -0.0001));

		setBounds(b);

		setColor(color);
		addChild(c);
	}

}
