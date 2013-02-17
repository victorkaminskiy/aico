package aico.simbad.emulator.robot;

import java.util.ArrayList;
import java.util.List;

import aico.simbad.emulator.util.Constants;
import aico.simbad.emulator.util.DataBlock;

public class NavigationAlgorithm {

	List<PathElements> path;
	int currentPartOfPath;
	PathElements previousPathElement;

	public NavigationAlgorithm() {
		path = new ArrayList<PathElements>();
		path = PathBuilder.buildPath(path);
		currentPartOfPath = 0;
		previousPathElement = PathElements.START;
	}

	// TODO This realization works only with 4 sensors (in front, in back, on
	// left and on right)
	public EngineActions calcNextAction(DataBlock data) {
		PathElements currentPathElement = path.get(currentPartOfPath);
		EngineActions nextEngineAction = EngineActions.ALL_STOP;

		switch (currentPathElement) {
		case START:
			nextEngineAction = EngineActions.ALL_STOP;
			currentPartOfPath++;
			break;
		case FORWARD:
			// Check if there are a lot of free space in the front
			// then move forward, else stop and continue with next
			// part of path
			if (data.getDistanceHasHit()[0] == false
					|| data.getDistanceMeasurement()[0] > Constants.NORMAL_DISTANCE) {
				nextEngineAction = EngineActions.MOVE_FORWARD;
			} else {
				nextEngineAction = EngineActions.ALL_STOP;
				currentPartOfPath++;
			}
			break;
		case LEFTWARD:
			// Same check as for FORWARD but for movement in left
			if (data.getDistanceHasHit()[1] == false
					|| data.getDistanceMeasurement()[1] > Constants.NORMAL_DISTANCE) {
				nextEngineAction = EngineActions.MOVE_LEFTWARD;
			} else {
				nextEngineAction = EngineActions.ALL_STOP;
				currentPartOfPath++;
			}
			break;
		case BACKWARD:
			// Same check as for FORWARD but for movement in back direction
			if (data.getDistanceHasHit()[2] == false
					|| data.getDistanceMeasurement()[2] > Constants.NORMAL_DISTANCE) {
				nextEngineAction = EngineActions.MOVE_BACKWARD;
			} else {
				nextEngineAction = EngineActions.ALL_STOP;
				currentPartOfPath++;
			}
			break;
		case STRAFE:
			// TODO This place works now only for backward movement direction!
			// TODO This place works not nice at all!
			if (data.getDistanceHasHit()[2] && data.getDistanceMeasurement()[2] < Constants.NORMAL_DISTANCE) {
				nextEngineAction = EngineActions.MOVE_RIGHTWARD;
			} else {
				nextEngineAction = EngineActions.ALL_STOP;
				currentPartOfPath++;
			}
			break;
		case FINISH:
			break;
		default:
			System.err.println("Not all PathElements are implemented yet!");
			break;
		}

		previousPathElement = currentPathElement;

		return nextEngineAction;
	}

}
