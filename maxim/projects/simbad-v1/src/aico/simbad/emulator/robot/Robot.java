package aico.simbad.emulator.robot;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import simbad.sim.Agent;

public class Robot extends Agent {

	public Robot(Vector3d pos, String name) {
		super(pos, name);
		
		// Add sensors
		
	}
	
	public void initBehavior() {}
    
    public void performBehavior() {
    	System.err.println("1");
        if (collisionDetected()) {
        	System.out.println("QWE");
            // Stop the robot, emulation failed
            setTranslationalVelocity(0.0);
            setRotationalVelocity(0);
            setColor(new Color3f(1, 0, 0));
        }else {
            // progress at 0.5 m/s
            setTranslationalVelocity(0.5);
            // frequently change orientation 
            if ((getCounter() % 100)==0) 
               setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        }
    }

}
