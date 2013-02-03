package aico.simbad.emulator.robot;

import java.util.ArrayList;
import java.util.List;

import aico.simbad.emulator.util.DataBlock;
import aico.simbad.emulator.util.PathBuilder;

public class NavAlgo {
	
	List<Elements> path;
	int currentPartOfPath;
	EngineActions prevEngineAction;
	
	public NavAlgo() {
		path = new ArrayList<Elements>();
		path = PathBuilder.buildPath(path);
		currentPartOfPath = 0;
		prevEngineAction = EngineActions.MOVE_STOP;
	}
	
	// TODO This realization works only with 4 sensors (in front, in back, on left and on right)
	public EngineActions calcNextAction(DataBlock data) {
		
		return EngineActions.MOVE_FORWARD; // TODO Change this to real return value 
	}

}
