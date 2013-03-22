package com.aico.remote;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JPanel;

public class PlotterPanel extends JPanel implements MovementListener {
	private float ex = 0;
	private float ey = 0;
	private float ez = 0;

	private float vx = 0;
	private float vy = 0;
	private float vz = 0;
	private float x = 0;
	private float y = 0;
	private float z = 0;
	private long time = -1;
	private Polygon polygon = new Polygon();

	/**
	 * 
	 */
	private static final long serialVersionUID = -384817703112682532L;

	public PlotterPanel() {
		super();
		polygon.addPoint(0, 0);

	}

	@Override
	public void changed(MovementEvent event) {
		if (time != -1) {
			final long dt = event.getTime() - time;
			final float vx = this.vx + event.getAx() * dt;
			final float vy = this.vy + event.getAy() * dt;
			final float vz = this.vz + event.getAz() * dt;
			x += (this.vx + vx) * dt / 2;
			y += (this.vy + vy) * dt / 2;
			z += (this.vz + vz) * dt / 2;
			polygon.addPoint((int) x, (int) y);
			this.vx = vx;
			this.vy = vy;
			this.vz = vz;
		}
		time = event.getTime();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawPolygon(polygon);
	}

}
