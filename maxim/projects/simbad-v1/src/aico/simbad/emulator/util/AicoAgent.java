package aico.simbad.emulator.util;

import javax.vecmath.Vector3d;

import simbad.sim.Agent;

public class AicoAgent extends Agent {
	private AicoKinematicModel kinematicModel;
	
	public AicoAgent(Vector3d pos, String name) {
		super(pos, name);
		kinematicModel = new AicoKinematicModel();
		setKinematicModel(kinematicModel);
	}
	
	/**
     * Sets rotational velocity in radians per second (specially for Aico).
     */
    public final void setAicoRotationalVelocity(double rv) {
        if (kinematicModel instanceof AicoKinematicModel)
            ((AicoKinematicModel)kinematicModel).setRotationalVelocity(rv);
    }

    /**
     * Sets translational velocity in meter per second (specially for Aico).
     */
    public final void setAicoTranslationalVelocity(double tv) {
        if (kinematicModel instanceof AicoKinematicModel)
            ((AicoKinematicModel)kinematicModel).setTranslationalVelocity(tv);
    }

    /**
     * Sets shift velocity in meter per second (specially for Aico).
     */
    public final void setAicoShiftVelocity(double tv) {
        if (kinematicModel instanceof AicoKinematicModel)
            ((AicoKinematicModel)kinematicModel).setShiftVelocity(tv);
    }
    
    /**
     * Gets rotational velocity in radians per second (specially for Aico).
     */
     public final double getAicoRotationalVelocity() {
         if (kinematicModel instanceof AicoKinematicModel)
             return ((AicoKinematicModel)kinematicModel).getRotationalVelocity();
         else return 0.0;
    }
     
     /**
      * Gets translational velocity in meter per second (specially for Aico).
      */
    public final double getAicoTranslationalVelocity() {
        if (kinematicModel instanceof AicoKinematicModel)
            return ((AicoKinematicModel)kinematicModel).getTranslationalVelocity();
        else return 0.0;     
    }
    
    /**
     * Gets shift velocity in meter per second (specially for Aico).
     */
   public final double getAicoShiftVelocity() {
       if (kinematicModel instanceof AicoKinematicModel)
           return ((AicoKinematicModel)kinematicModel).getShiftVelocity();
       else return 0.0;     
   }

}
