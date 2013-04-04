package aico.simbad.emulator.robot;

import java.util.ArrayList;
import java.util.List;


public class PathBuilder {
	public static List<PathElements> buildPath(List<PathElements> path) {
		path = new ArrayList<PathElements>();
		path.add(PathElements.START);
		path.add(PathElements.FORWARD);
		path.add(PathElements.LEFTWARD);
		path.add(PathElements.BACKWARD);
		path.add(PathElements.STRAFE);
		path.add(PathElements.BACKWARD);
        path.add(PathElements.LAND);
        path.add(PathElements.TAKE_OFF);
        path.add(PathElements.FORWARD);
        // path.add(PathElements.STRAFE); TODO Not now as we do not search for white label
        path.add(PathElements.RIGHTWARD);
        path.add(PathElements.BACKWARD);
        path.add(PathElements.LAND);
		path.add(PathElements.FINISH);
		return path;
	}
}
