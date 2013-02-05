package aico.simbad.emulator.launcher;

import aico.simbad.emulator.environment.Environment;
import simbad.gui.Simbad;

public class Launcher {

	public static void main(String[] args) {
		final Simbad frame = new Simbad(new Environment() ,false);
	}
	
	

}
