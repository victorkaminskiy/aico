package aico.simbad.emulator.util;

import java.util.ArrayList;
import java.util.List;

import aico.simbad.emulator.robot.Elements;

public class PathBuilder {
	public static List<Elements> buildPath(List<Elements> path) {
		path = new ArrayList<Elements>();
		path.add(Elements.START);
		path.add(Elements.FORWARD);
		path.add(Elements.TURN_LEFT);
		path.add(Elements.FORWARD);
		path.add(Elements.TURN_LEFT);
		path.add(Elements.STRAFE);
		path.add(Elements.FORWARD);
		path.add(Elements.FINISH);
		return path;
	}
}
