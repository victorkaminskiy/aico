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

public class RemotePanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -977197592284133514L;
	private int SIZE = 200;
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
				g.fillOval((int) (5 + SIZE / 2 + com.getRoll() * SIZE / 2) - 2,
						(int) (5 + SIZE / 2 + com.getPitch() * SIZE / 2) - 2, 4, 4);
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

		final JButton button = new JButton("Arm");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				com.arm();
			}
		});
		b.add(button);
		final JButton disarm = new JButton("Disarm");
		disarm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				com.disarm();
			}
		});
		b.add(disarm);
		final JToggleButton button1 = new JToggleButton("Hold");
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println(button1.isSelected());
				if(button1.isSelected()){
					com.setRc5(1);
				}else{
					com.setRc5(0);
				}
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
	
	private void setTrottle(float trottle){
		com.setTrottle(trottle);
		trottleProgr.setValue((int) ((trottle + 1) * 100));
	}
	
	private void setYaw(float yaw){
		com.setYaw(yaw);
		yawProgr.setValue((int) ((com.getYaw() + 1) * 50));
	}
	
	private void setRoll(float roll){
		com.setRoll(roll);
		axisPanel.repaint();
	}
	
	private void setPitch(float pitch){
		com.setPitch(pitch);
		axisPanel.repaint();
	}
	
	private void center(){
		setPitch(0);
		setYaw(0);
		setRoll(0);
	}

	private class MyDispatcher implements KeyEventDispatcher {

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			//System.out.println("key code= "+e.getKeyCode()+" "+e.getKeyChar());
			switch (e.getKeyCode()) {
			case KeyEvent.VK_U: {
				com.setTrottle(1F);
				com.setPitch(-1F);
				com.setYaw(-1F);
				com.setRoll(0F);
				break;
			}
			case KeyEvent.VK_J: {
				com.setTrottle(-1F);
				com.setPitch(-1F);
				com.setYaw(-1F);
				com.setRoll(0F);
				break;
			}
			case KeyEvent.VK_M: {
				com.setTrottle(1F);
				com.setPitch(-1F);
				com.setYaw(1F);
				com.setRoll(0F);
				break;
			}
			case KeyEvent.VK_S: {
				center();
				break;
			}
			case KeyEvent.VK_O: {
				com.setHoldAlt(com.getHoldAlt()+5);
				break;
			}
			case KeyEvent.VK_L: {
				com.setHoldAlt(com.getHoldAlt()-5);
				break;
			}
			case KeyEvent.VK_D: {
				com.flight(18);
				break;
			}
			case KeyEvent.VK_C: {
				com.landing();
				break;
			}
			case KeyEvent.VK_X: {
				setTrottle(com.getTrottle() - 0.05F);
				break;
			}
			case KeyEvent.VK_A: {
				setTrottle(com.getTrottle() + 0.01F);
				break;
			}
			case KeyEvent.VK_Z: {
				setTrottle(com.getTrottle() - 0.01F);
				break;
			}
			case KeyEvent.VK_Q: {
				setYaw(com.getYaw()-0.001F);
				break;
			}
			case KeyEvent.VK_W: {
				setYaw(com.getYaw()+0.001F);
				break;
			}
			case KeyEvent.VK_UP: {
				setPitch(com.getPitch()+0.001F);
				break;
			}
			case KeyEvent.VK_DOWN: {
				setPitch(com.getPitch()-0.001F);
				break;
			}
			case KeyEvent.VK_LEFT: {
				setRoll(com.getRoll()-0.001F);
				break;
			}
			case KeyEvent.VK_RIGHT: {
				setRoll(com.getRoll()+0.001F);
				break;
			}
			case KeyEvent.VK_R: {
				com.arm();
				break;
			}
			case KeyEvent.VK_E: {
				com.disarm();
				break;
			}
			case KeyEvent.VK_T: {
				com.setRc5(1000);
				break;
			}
			case KeyEvent.VK_G: {
				com.setRc5(com.getRc5()+10);
				break;
			}
			case KeyEvent.VK_B: {
				com.setRc5(com.getRc5()-10);
				break;
			}
			default:
				return false;
			}
			return true;
		}

	}
}
