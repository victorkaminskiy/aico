package aico.simbad.emulator.robot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aico.simbad.emulator.util.Constants;
import aico.simbad.emulator.util.DataBlock;

public class NavigationAlgorithm {

    static Map<String, Double> conditionMap = new HashMap<String, Double>();

    List<PathElements> path;
    int currentPartOfPath;
    PathElements previousPathElement;

	public NavigationAlgorithm() {
		path = new ArrayList<PathElements>();
		path = PathBuilder.buildPath(path);
		currentPartOfPath = 0;
		previousPathElement = PathElements.START;
        conditionMap.put("going_in_back_direction", 0d);
	}

	// TODO This realization works only with 4 sensors (in front, in back, on
	// left and on right)
	public EngineActions calcNextAction(DataBlock data) {
		PathElements currentPathElement = path.get(currentPartOfPath);
		EngineActions nextEngineAction = EngineActions.ALL_STOP;

        Double goingInBackDirectionFloat = conditionMap.get("going_in_back_direction");
        boolean goingInBackDirection = false;
        if (goingInBackDirectionFloat != null && goingInBackDirectionFloat == 1) goingInBackDirection = true;

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
		case RIGHTWARD:
			// Same check as for FORWARD but for movement in left
			if (data.getDistanceHasHit()[3] == false
					|| data.getDistanceMeasurement()[3] > Constants.NORMAL_DISTANCE) {
				nextEngineAction = EngineActions.MOVE_RIGHTWARD;
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

            BufferedImage image = data.getCameraImage();
            double midR = 0;
            double midG = 0;
            double midB = 0;

            for (int i = 0; i < image.getWidth(); i++ ) {
                for (int j = 0; j < image.getHeight(); j++ ) {
                    Color pixel = new Color(image.getRGB(i, j));
                    midR += pixel.getRed();
                    midG += pixel.getGreen();
                    midB += pixel.getBlue();
                }
            }

            long numberOfPixels = image.getWidth() * image.getHeight();
            midR /= numberOfPixels;
            midG /= numberOfPixels;
            midB /= numberOfPixels;

            if (goingInBackDirection) {
                if (Math.abs((midR / Constants.COLOR_BLACK.getRed()) - 1) < Constants.PERCENT_OF_CIRCLE_COLOR_IN_CAMERA
                        && Math.abs((midG / Constants.COLOR_BLACK.getGreen()) - 1) < Constants.PERCENT_OF_CIRCLE_COLOR_IN_CAMERA
                        && Math.abs((midB / Constants.COLOR_BLACK.getBlue()) - 1) < Constants.PERCENT_OF_CIRCLE_COLOR_IN_CAMERA) {
                    nextEngineAction = EngineActions.ALL_STOP;
                    currentPartOfPath++;
                    conditionMap.put("going_in_back_direction", 1d);
                }
            } else {
                if (Math.abs((midR / Constants.COLOR_WHITE.getRed()) - 1) < Constants.PERCENT_OF_CIRCLE_COLOR_IN_CAMERA
                        && Math.abs((midG / Constants.COLOR_WHITE.getGreen()) - 1) < Constants.PERCENT_OF_CIRCLE_COLOR_IN_CAMERA
                        && Math.abs((midB / Constants.COLOR_WHITE.getBlue()) - 1) < Constants.PERCENT_OF_CIRCLE_COLOR_IN_CAMERA) {
                    nextEngineAction = EngineActions.ALL_STOP;
                    currentPartOfPath = 6; // TODO Hardcoded part of element (because we can miss STRAFE if gate is very wide)
                    conditionMap.put("going_in_back_direction", 1d);
                }
            }

			break;
		case STRAFE:
            Double previousMainElementIsBackwardFloat = conditionMap.get("previous_main_element_is_backward");
            boolean previousMainElementIsBackward = false;
            if (previousMainElementIsBackwardFloat != null && previousMainElementIsBackwardFloat == 1) previousMainElementIsBackward = true;

            if (previousPathElement == PathElements.BACKWARD || previousMainElementIsBackward) {
                conditionMap.put("previous_main_element_is_backward", 1d);

                Double gateStartDetectedFloat = conditionMap.get("gate_start_detected");
                boolean gateStartDetected = false;
                if (gateStartDetectedFloat != null && gateStartDetectedFloat == 1) gateStartDetected = true;

                Double gateEndDetectedFloat = conditionMap.get("gate_end_detected");
                boolean gateEndDetected = false;
                if (gateEndDetectedFloat != null && gateEndDetectedFloat == 1) gateEndDetected = true;

                if (data.getDistanceHasHit()[2] && (data.getDistanceMeasurement()[2] < Constants.NORMAL_DISTANCE)
                        || data.getDistanceMeasurement()[0] < Constants.NORMAL_DISTANCE) {
                    if (gateStartDetected) {
                        if (gateEndDetected) {
                            nextEngineAction = EngineActions.MOVE_LEFTWARD;
                        } else {
                            conditionMap.put("gate_end_detected", 1d);
                            conditionMap.put("gate_end_detected_time", (double) System.currentTimeMillis());
                            nextEngineAction = EngineActions.MOVE_LEFTWARD;
                        }
                    } else {
                        nextEngineAction = EngineActions.MOVE_RIGHTWARD;
                    }
                } else {
                    if (gateStartDetected) {
                        if (gateEndDetected) {
                            Double endGateTime = conditionMap.get("gate_end_detected_time");
                            Double startGateTime = conditionMap.get("gate_start_detected_time");
                            Double spendTime = endGateTime - startGateTime;
                            Double nowTime = (double) System.currentTimeMillis();
                            if (endGateTime + spendTime / 2 < nowTime) {
                                nextEngineAction = EngineActions.ALL_STOP;
                                currentPartOfPath++;
                                conditionMap.put("gate_start_detected", 0d);
                                conditionMap.put("gate_end_detected", 0d);
                                conditionMap.put("previous_main_element_is_backward", 0d);
                            } else {
                                nextEngineAction = EngineActions.MOVE_LEFTWARD;
                            }
                        } else {
                            nextEngineAction = EngineActions.MOVE_RIGHTWARD;
                        }
                    } else {
                        conditionMap.put("gate_start_detected", 1d);
                        conditionMap.put("gate_start_detected_time", (double) System.currentTimeMillis());
                        nextEngineAction = EngineActions.MOVE_RIGHTWARD;
                    }

                }
            } else if (previousPathElement == PathElements.FORWARD) {
                // TODO Not yet implemented forward strafing
            }
			break;
        case LAND:
            nextEngineAction = EngineActions.ALL_STOP;
            currentPartOfPath++;
            break;
        case TAKE_OFF:
            nextEngineAction = EngineActions.ALL_STOP;
            currentPartOfPath++;
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
