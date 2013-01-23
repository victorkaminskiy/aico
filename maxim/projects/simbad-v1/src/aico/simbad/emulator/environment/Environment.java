package aico.simbad.emulator.environment;

import simbad.sim.*;
import javax.vecmath.Vector3d;

import aico.simbad.emulator.robot.Robot;

public class Environment extends EnvironmentDescription {
	public Environment(){
		// Square walls
		Wall w1 = new Wall(new Vector3d(9, 0, 0), 18, 3, this);
        w1.rotate90(1);
        add(w1);
        Wall w2 = new Wall(new Vector3d(-9, 0, 0), 18, 3, this);
        w2.rotate90(1);
        add(w2);
        Wall w3 = new Wall(new Vector3d(0, 0, 9), 18, 3, this);
        add(w3);
        Wall w4 = new Wall(new Vector3d(0, 0, -9), 18, 3, this);
        add(w4);
        
        // Middle wall
        Wall w5 = new Wall(new Vector3d(0, 0, -2), 14, 3, this);
        w5.rotate90(1);
        add(w5);
        
        // Divider wall
        Wall w6 = new Wall(new Vector3d(2, 0, 0), 4, 3, this);
        w6.rotate90(2);
        add(w6);
        Wall w7 = new Wall(new Vector3d(7.5, 0, 0), 3, 3, this);
        w7.rotate90(-2);
        add(w7);
        
        add(new Robot(new Vector3d(-5, 2, -5),"Aico Robot"));
    }
}
