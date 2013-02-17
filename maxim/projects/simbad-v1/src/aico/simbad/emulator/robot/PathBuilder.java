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
		path.add(PathElements.FINISH);
		return path;
	}
}
