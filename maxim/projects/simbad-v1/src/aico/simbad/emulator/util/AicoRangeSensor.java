package aico.simbad.emulator.util;

import simbad.sim.RangeSensorBelt;

public class AicoRangeSensor extends RangeSensorBelt {

	public AicoRangeSensor(float radius, float minRange, float maxRange,
			int nbsensors, int type, int flags) {
		super(radius, minRange, maxRange, nbsensors, type, flags);
	}
	
	public void setName(String name) {
		super.setName(name);
	}

}
