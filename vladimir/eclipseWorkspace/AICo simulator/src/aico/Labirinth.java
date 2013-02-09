package aico;

import java.awt.Color;

import javax.vecmath.Color3f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import simbad.sim.Arch;
import simbad.sim.EnvironmentDescription;
import simbad.sim.StartOrFinishTarget;
import simbad.sim.Wall;
import simbad.sim.WallCylinder;

public class Labirinth extends EnvironmentDescription {
	private static final float WALL_HEIGHT = 3;
	private static final float WALL_THIKNESS = (float) 0.3;
	private static final float LAB_LENGTH = 50;
	private static final float LAB_WIDTH = 25;
	private static final float MIDDLE_WALL_LENGTH = 45;
	// private static final float MIDDLE_WALL_OFFSET_FROM_START_CORRIDOR =
	// LAB_WIDTH / 2;
	private static final float RANDOM_WALL_OFFSET_FROM_FURTHEST_WALL = 15;
	private static final float GATE_WIDTH = 2;
	private static final float GATE_OFFSET_FROM_WALL = 5;

	// Вопрос 1. рандомная преграда будет до или после поворота на 180 градусов?

	public Labirinth() {
		setWorldSize(50);

		// Main wall 1 (length)
		Wall w = new Wall(new Vector3d(0, 0, LAB_WIDTH / 2), LAB_LENGTH,
				WALL_THIKNESS, WALL_HEIGHT, this);
		add(w);

		// main wall 2 (length)
		w = new Wall(new Vector3d(0, 0, (LAB_WIDTH / 2) * -1), LAB_LENGTH,
				WALL_THIKNESS, WALL_HEIGHT, this);
		add(w);

		// main wall 3 (width)
		w = new Wall(new Vector3d(LAB_LENGTH / 2, 0, 0), LAB_WIDTH,
				WALL_THIKNESS, WALL_HEIGHT, this);
		w.rotate90(1);
		add(w);

		// main wall 4 (width)
		w = new Wall(new Vector3d(-1 * (LAB_LENGTH / 2), 0, 0), LAB_WIDTH,
				WALL_THIKNESS, WALL_HEIGHT, this);
		w.rotate90(1);
		add(w);

		// main wall 5 (middle wall)
		w = new Wall(new Vector3d(-0.5 * (LAB_LENGTH - MIDDLE_WALL_LENGTH), 0,
				0), MIDDLE_WALL_LENGTH, WALL_THIKNESS, WALL_HEIGHT, this);
		add(w);

		// main wall 6 (random wall)
		w = new Wall(new Vector3d((LAB_LENGTH / 2)
				- RANDOM_WALL_OFFSET_FROM_FURTHEST_WALL, 0, //
				LAB_WIDTH / 2 - GATE_OFFSET_FROM_WALL / 2),//
				GATE_OFFSET_FROM_WALL, WALL_THIKNESS, //
				WALL_HEIGHT, this);
		w.rotate90(1);
		add(w);

		// main wall 7 (random wall)
		w = new Wall(
				new Vector3d((LAB_LENGTH / 2)
						- RANDOM_WALL_OFFSET_FROM_FURTHEST_WALL,
						0, //
						0.5 * (LAB_WIDTH / 2 - GATE_OFFSET_FROM_WALL - GATE_WIDTH)),//
				LAB_WIDTH / 2 - GATE_OFFSET_FROM_WALL - GATE_WIDTH,
				WALL_THIKNESS, //
				WALL_HEIGHT, this);
		w.rotate90(1);
		add(w);

		StartOrFinishTarget start = new StartOrFinishTarget(new Vector2d(-1
				* (LAB_LENGTH / 3), -1 * (LAB_WIDTH / 4)), (float) 1.5,
				(float) 0.1, new Color3f(Color.black),
				new Color3f(Color.white), this);
		add(start);

		StartOrFinishTarget finish = new StartOrFinishTarget(new Vector2d(
				-1 * (LAB_LENGTH / 3), 1 * (LAB_WIDTH / 4)), (float) 1.5,
				(float) 0.1, new Color3f(Color.white),
				new Color3f(Color.black), this);
		add(finish);

		add(new WallGoerAico(new Vector3d(-1 * (LAB_LENGTH / 3), 0.1, -1
				* (LAB_WIDTH / 4)), "Aico Robot"));
	}
}
