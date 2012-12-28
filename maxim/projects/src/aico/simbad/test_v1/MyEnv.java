package aico.simbad.test_v1;

import simbad.sim.*;
import javax.vecmath.Vector3d;

public class MyEnv extends EnvironmentDescription {
	public MyEnv(){
        add(new Arch(new Vector3d(3,0,-3),this));
        add(new MyRobot(new Vector3d(0, 0, 0),"my robot"));
    }
}
