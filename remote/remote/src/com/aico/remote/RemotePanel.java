package com.aico.remote;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class RemotePanel extends JPanel implements RemoteListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -977197592284133514L;
	private int SIZE = 100;
	private float trottle;
	private float roll;
	private float pitch;
	private float yaw;

	public RemotePanel() {
		setMinimumSize(new Dimension(SIZE + 10, 2 * SIZE + 20));
		setPreferredSize(new Dimension(SIZE + 10, 2 * SIZE + 20));
	}

	@Override
	public void changed(float trottle, float roll, float pitch, float yaw,
			float start) {
		this.trottle = trottle;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.printComponent(g);
		g.setColor(Color.GREEN);
		g.drawLine(5, SIZE / 2 + 5, 2*SIZE + 20, SIZE / 2 + 5);
		g.drawLine(5 + SIZE / 2, 5, 5 + SIZE / 2, SIZE + 10);
		g.drawLine(SIZE * 3 / 2 + 15, 5, SIZE * 3 / 2 + 15, SIZE + 10);
		g.setColor(Color.blue);
		g.fillOval((int) (5 + SIZE / 2 + roll * SIZE / 2),
				(int) (5 + SIZE / 2 + trottle * SIZE / 2), 2, 2);
		g.fillOval((int) (15 + SIZE * 3 / 2 + SIZE / 2 * yaw),
				(int) (15 + SIZE / 2 + SIZE / 2 * pitch), 2, 2);
	}
}
