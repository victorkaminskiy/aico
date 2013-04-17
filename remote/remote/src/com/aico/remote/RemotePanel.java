package com.aico.remote;

import gnu.io.CommPortIdentifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class RemotePanel extends JPanel implements RemoteListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -977197592284133514L;
	private int SIZE = 200;
	private float trottle = -1F;
	private float roll = 0;
	private float pitch = 0;
	private float yaw = 0;
	private JComboBox comboBox;
	private JProgressBar trottleProgr;
	private JProgressBar yawProgr;
	private JPanel axisPanel;
	private JButton connect;
	private Communicator com;
	private JSlider slider;
	private Copter copter;
	private boolean hold = false;

	public RemotePanel() {
		copter = new Copter();
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
			/**
			 * 
			 */
			private static final long serialVersionUID = 5271629384214049797L;

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
		com = new Communicator(copter);
		connect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = null;
				if (comboBox.getSelectedItem() != null) {
					selected = comboBox.getSelectedItem().toString();
				}
				com.connect(selected);
			}
		});
		box.add(connect);
		add(box, BorderLayout.NORTH);
		final ParamsPanel params = new ParamsPanel();
		com.addChangeListener(params);
		add(params, BorderLayout.EAST);
		Box vert = Box.createVerticalBox();
		trottleProgr = new JProgressBar(0, 100);
		vert.add(trottleProgr);
		yawProgr = new JProgressBar(0, 100);
		vert.add(yawProgr);
		Box b = Box.createVerticalBox();
		slider = new JSlider(0, 100, 0);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// changed((slider.getValue()-50)/ 50F, roll, pitch, yaw, 0);
			}
		});
		b.add(slider);

		JButton button = new JButton("Start");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changed(trottle, 0, 0, 0, 1, false);
			}
		});
		b.add(button);
		final JToggleButton button1 = new JToggleButton("Hold");
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (button1.isSelected()) {
					hold=true;
				} else {
					hold=false;
				}
				changed(trottle, roll, pitch, yaw, 0, hold);
			}
		});
		b.add(button1);
		vert.add(b);
		add(vert, BorderLayout.SOUTH);
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());
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

	public float checkRanges(float value, float min, float max) {
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		return value;
	}

	@Override
	public void changed(float trottle, float roll, float pitch, float yaw,
			float start, boolean hold) {
		trottle = checkRanges(trottle, -1F, 1F);
		roll = checkRanges(roll, -1F, 1F);
		pitch = checkRanges(pitch, -1F, 1F);
		yaw = checkRanges(yaw, -1F, 1F);
		com.changed(trottle, roll, pitch, yaw, start, hold);
		this.trottle = trottle;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
		axisPanel.repaint();
		// slider.setValue((int)((trottle+1)*100F));
		trottleProgr.setValue((int) ((trottle + 1) * 100));
		yawProgr.setValue((int) ((yaw + 1) * 50));
	}

	private class MyDispatcher implements KeyEventDispatcher {

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			System.out.println("key code= "+e.getKeyCode()+" "+e.getKeyChar());
			switch (e.getKeyCode()) {
			case KeyEvent.VK_D: {
				changed(trottle, 0, 0, 0, 0, hold);
				break;
			}
			case KeyEvent.VK_X: {
				changed(trottle - 0.05F, 0, 0, 0, 0, hold);
				break;
			}
			case KeyEvent.VK_A: {
				changed(trottle + 0.01F, roll, pitch, yaw, 0, hold);
				break;
			}
			case KeyEvent.VK_Z: {
				changed(trottle - 0.01F, roll, pitch, yaw, 0, hold);
				break;
			}
			case KeyEvent.VK_Q: {
				changed(trottle, roll, pitch, yaw - 0.01F, 0, hold);
				break;
			}
			case KeyEvent.VK_W: {
				changed(trottle, roll, pitch, yaw + 0.01F, 0, hold);
				break;
			}
			case KeyEvent.VK_UP: {
				changed(trottle, roll, pitch + 0.01F, yaw, 0, hold);
				break;
			}
			case KeyEvent.VK_DOWN: {
				changed(trottle, roll, pitch - 0.01F, yaw, 0, hold);
				break;
			}
			case KeyEvent.VK_LEFT: {
				changed(trottle, roll - 0.01F, pitch, yaw, 0, hold);
				break;
			}
			case KeyEvent.VK_RIGHT: {
				changed(trottle, roll + 0.01F, pitch, yaw, 0, hold);
				break;
			}
			case KeyEvent.VK_E: {
				changed(trottle, 0, 0, 0, 1, hold);
				break;
			}
			default:
				return false;
			}
			return true;
		}

	}
}
