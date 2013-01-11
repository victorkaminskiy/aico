package aico.simbad.test_v1;

import java.awt.image.BufferedImage;

import  simbad.sim.*;
import javax.vecmath.Vector3d;

public class MyRobot extends Agent {
	
	final RangeSensorBelt sonars;
	final RangeSensorBelt bumpers;
	final CameraSensor camera;
    final BufferedImage cameraImage;
	
    public MyRobot (Vector3d position, String name) {     
        super(position,name);
        sonars = RobotFactory.addSonarBeltSensor(this,8);
        bumpers = RobotFactory.addBumperBeltSensor(this,8);
        
        // add a camera on top of the robot
        camera = RobotFactory.addCameraSensor(this);
        // reserve space for image capture
        cameraImage = camera.createCompatibleImage();
    }
    public void initBehavior() {}
    
    public void performBehavior() {
        if (collisionDetected()) {
            // stop the robot
            setTranslationalVelocity(0.0);
            setRotationalVelocity(0);
        } else {
            // progress at 0.5 m/s
            setTranslationalVelocity(0.5);
            // frequently change orientation 
            if ((getCounter() % 100)==0) 
               setRotationalVelocity(Math.PI/2 * (0.5 - Math.random()));
        }
        
        //every 20 frames - sonar
        if (getCounter()%20==0){
            // print each sonars measurement
            for (int i=0;i< sonars.getNumSensors();i++) {
                double range = sonars.getMeasurement(i); 
                double angle = sonars.getSensorAngle(i);
                boolean hit = sonars.hasHit(i);
                System.out.println("Sonar at angle "+ angle +
                "measured range ="+range+ " has hit something:"+hit); 
            }
        }
        
        //every 20 frames - bumper
        if (getCounter()%20==0){
            // print each bumper state 
            for (int i=0;i< bumpers.getNumSensors();i++) {
                double angle = bumpers.getSensorAngle(i);
                boolean hit = bumpers.hasHit(i);
                System.out.println("Bumpers at angle "+ angle 
                + " has hit something:"+hit); 
            }
        }
        
        // get camera image 
        camera.copyVisionImage(cameraImage);
        // process image 
        // ... use BufferedImage api
    }
}