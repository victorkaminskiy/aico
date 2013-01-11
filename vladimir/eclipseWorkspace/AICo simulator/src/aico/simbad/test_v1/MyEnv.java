package aico.simbad.test_v1;

import simbad.sim.*;
import javax.vecmath.Vector3d;

public class MyEnv extends EnvironmentDescription {
	public MyEnv(){
		// Square walls
		Wall w1 = new Wall(new Vector3d(9, 0, 0), 18, 2, this);
        w1.rotate90(1);
        add(w1);
        Wall w2 = new Wall(new Vector3d(-9, 0, 0), 18, 2, this);
        w2.rotate90(1);
        add(w2);
        Wall w3 = new Wall(new Vector3d(0, 0, 9), 18, 2, this);
        add(w3);
        Wall w4 = new Wall(new Vector3d(0, 0, -9), 18, 2, this);
        add(w4);
        
        // Middle wall
        Wall w5 = new Wall(new Vector3d(0, 0, -2), 14, 2, this);
        w5.rotate90(1);
        add(w5);
        
        add(new MyRobot(new Vector3d(5, 0, -5),"Aico Robot"));
    }
}
