package com.aico.remote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Remote implements Runnable {
	final List<RemoteListener> listeners = new ArrayList<RemoteListener>();
	Controller joystick;
	Component roll = null;
	Component trottle = null;
	Component yaw = null;
	Component pitch = null;
	Component start = null;

	public Remote() {
		Controller[] ca = ControllerEnvironment.getDefaultEnvironment()
				.getControllers();

		for (int i = 0; i < ca.length; i++) {

			/* Get the name of the controller */
			System.out.println(ca[i].getName());
			System.out.println("Type: " + ca[i].getType().toString());

			if (ca[i].getType() == Controller.Type.STICK) {
				joystick = ca[i];
				/* Get this controllers components (buttons and axis) */
				Component[] components = ca[i].getComponents();
				System.out.println("Component Count: " + components.length);
				for (int j = 0; j < components.length; j++) {

					/* Get the components name */
					System.out.println("Component " + j + ": "
							+ components[j].getName());
					System.out.println("    Identifier: "
							+ components[j].getIdentifier().getName());
					System.out.print("    ComponentType: ");
					if (components[j].isRelative()) {
						System.out.print("Relative");
					} else {
						System.out.print("Absolute");
					}
					if (components[j].isAnalog()) {
						System.out.print(" Analog");
						if (components[j].getIdentifier() == Identifier.Axis.X) {
							roll = components[j];
						} else if (components[j].getIdentifier() == Identifier.Axis.Y) {
							pitch = components[j];
						} else if (components[j].getIdentifier() == Identifier.Axis.SLIDER) {
							trottle = components[j];
						} else if (components[j].getIdentifier() == Identifier.Axis.RZ) {
							yaw = components[j];
						}
					} else {
						System.out.print(" Digital");
						if (components[j].getIdentifier() == Identifier.Button.BASE) {
							start = components[j];
						}

					}
					System.out.println();
				}
				break;
			}
		}
	}

	public void addRemoteListener(RemoteListener remoteListener) {
		listeners.add(remoteListener);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		float t = -1;
		float r = -1;
		float p = -1;
		float y = -1;
		float s = -1;
		if (joystick != null) {
			while (true) {
				joystick.poll();
				if ((t != trottle.getPollData()) || (r != roll.getPollData())
						|| (p != pitch.getPollData())
						|| (y != yaw.getPollData())
						|| (s != start.getPollData())) {
					t = trottle.getPollData();
					r = roll.getPollData();
					p = pitch.getPollData();
					y = yaw.getPollData();
					s = start.getPollData();
					final float deadZone = 0.05F;
					if (Math.abs(t) < deadZone) {
						t = 0;
					}
					if (t < 0) {
						t += deadZone;
					}
					if (t > 0) {
						t -= deadZone;
					}
					if (Math.abs(r) < deadZone) {
						r = 0;
					}
					if (r < 0) {
						r += deadZone;
					}
					if (r > 0) {
						r -= deadZone;
					}
					if (Math.abs(p) < deadZone) {
						p = 0;
					}
					if (p < 0) {
						p += deadZone;
					}
					if (p > 0) {
						p -= deadZone;
					}
					if (Math.abs(y) < deadZone) {
						y = 0;
					}
					if (y < 0) {
						y += deadZone;
					}
					if (y > 0) {
						y -= deadZone;
					}
					for (RemoteListener listener : listeners) {
						try {
							listener.changed(t, r / 2, p / 2, y / 2,
									start.getPollData());

						} catch (Exception e) {

						}
					}
				}

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Remote remote = new Remote();
		final JFrame frame = new JFrame();
		final RemotePanel panel = new RemotePanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.setLayout(new BorderLayout());
		frame.add(panel);
		remote.addRemoteListener(panel);
		frame.setBounds(100, 100, 300, 400);
		final Thread thread = new Thread(remote);
		thread.start();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
