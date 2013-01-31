package com.aico.remote;

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class RemotePanel extends JPanel implements RemoteListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -977197592284133514L;
	private int SIZE = 200;
	private float trottle;
	private float roll;
	private float pitch;
	private float yaw;
	private JComboBox comboBox;
	private JProgressBar trottleProgr;
	private JProgressBar yawProgr;
	private JPanel axisPanel;
	private JButton connect;
	private Communicator com;

	public RemotePanel() {
		setMinimumSize(new Dimension(SIZE + 80, SIZE + 50));
		setPreferredSize(new Dimension(SIZE + 80, SIZE + 5));
		setLayout(new BorderLayout());
		comboBox = new JComboBox();
		comboBox.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				refreshPorts();
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}
		});
		axisPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.printComponent(g);
				g.setColor(Color.GREEN);
				g.drawLine(5, SIZE / 2 + 5, SIZE + 10, SIZE / 2 + 5);
				g.drawLine(5 + SIZE / 2, 5, 5 + SIZE / 2, SIZE + 10);
				g.setColor(Color.blue);
				g.fillOval((int) (5 + SIZE / 2 + roll * SIZE / 2) - 2,
						(int) (5 + SIZE / 2 + pitch * SIZE / 2) - 2, 4, 4);
			}
		};
		add(axisPanel);
		final Box box = Box.createHorizontalBox();
		box.add(comboBox);
		connect = new JButton("Connect");
		com = new Communicator();
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				com.connect(comboBox.getSelectedItem().toString());
			}
		});
		box.add(connect);
		add(box, BorderLayout.NORTH);
		Box vert=Box.createVerticalBox();
		trottleProgr=new JProgressBar(0,100);
		vert.add(trottleProgr);
		yawProgr=new JProgressBar(0,100);
		vert.add(yawProgr);
		add(vert, BorderLayout.SOUTH);
	}

	public void refreshPorts() {
		final Enumeration<CommPortIdentifier> ports = CommPortIdentifier
				.getPortIdentifiers();
		while (ports.hasMoreElements()) {
			final CommPortIdentifier port = ports.nextElement();
			if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				comboBox.addItem(port.getName());
			}
		}
	}

	@Override
	public void changed(float trottle, float roll, float pitch, float yaw,
			float start) {
		if(Math.abs(trottle)<0.1){
			trottle=0;
		}
		if(Math.abs(roll)<0.1){
			roll=0;
		}
		if(Math.abs(pitch)<0.1){
			pitch=0;
		}
		if(Math.abs(yaw)<0.1){
			yaw=0;
		}
		com.changed(trottle, roll/2, pitch/2, yaw/2, start);
		this.trottle = trottle;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
		axisPanel.repaint();
		trottleProgr.setValue((int) ((trottle) * 100));
		yawProgr.setValue((int) ((yaw + 1) * 50));
	}
}
