package com.aico.remote;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class ParamsPanel extends Box implements ChangeListener {
	private JLabel ax = new JLabel("0000");
	private JLabel ay = new JLabel("0000");
	private JLabel az = new JLabel("0000");

	private JLabel gx = new JLabel("0000");
	private JLabel gy = new JLabel("0000");
	private JLabel gz = new JLabel("0000");

	private JLabel mx = new JLabel("0000");
	private JLabel my = new JLabel("0000");
	private JLabel mz = new JLabel("0000");

	private JLabel rotor1 = new JLabel("0000");
	private JLabel rotor2 = new JLabel("0000");
	private JLabel rotor3 = new JLabel("0000");
	private JLabel rotor4 = new JLabel("0000");

	private JLabel trot = new JLabel("0000");
	private JLabel roll = new JLabel("0000");
	private JLabel pitch = new JLabel("0000");
	private JLabel yaw = new JLabel("0000");
	
	private JLabel anglex = new JLabel("0000");
	private JLabel angley = new JLabel("0000");
	private JLabel head = new JLabel("0000");

	/**
	 * 
	 */
	private static final long serialVersionUID = 7337761205637294208L;

	public ParamsPanel() {
		super(BoxLayout.Y_AXIS);
		Box box = Box.createHorizontalBox();
		box.add(new JLabel("ax"));
		box.add(Box.createHorizontalStrut(5));
		box.add(ax);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("ay"));
		box.add(Box.createHorizontalStrut(5));
		box.add(ay);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("az"));
		box.add(Box.createHorizontalStrut(5));
		box.add(az);
		add(box);

		box = Box.createHorizontalBox();
		box.add(new JLabel("gx"));
		box.add(Box.createHorizontalStrut(5));
		box.add(gx);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("gy"));
		box.add(Box.createHorizontalStrut(5));
		box.add(gy);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("gz"));
		box.add(Box.createHorizontalStrut(5));
		box.add(gz);
		add(box);

		box = Box.createHorizontalBox();
		box.add(new JLabel("mx"));
		box.add(Box.createHorizontalStrut(5));
		box.add(mx);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("my"));
		box.add(Box.createHorizontalStrut(5));
		box.add(my);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("mz"));
		box.add(Box.createHorizontalStrut(5));
		box.add(mz);
		add(box);

		box = Box.createHorizontalBox();
		box.add(new JLabel("rotor 1"));
		box.add(Box.createHorizontalStrut(5));
		box.add(rotor1);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("rotor 2"));
		box.add(Box.createHorizontalStrut(5));
		box.add(rotor2);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("rotor 3"));
		box.add(Box.createHorizontalStrut(5));
		box.add(rotor3);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("rotor 4"));
		box.add(Box.createHorizontalStrut(5));
		box.add(rotor4);
		add(box);

		box = Box.createHorizontalBox();
		box.add(new JLabel("trot"));
		box.add(Box.createHorizontalStrut(5));
		box.add(trot);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("rot"));
		box.add(Box.createHorizontalStrut(5));
		box.add(roll);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("pitch"));
		box.add(Box.createHorizontalStrut(5));
		box.add(pitch);
		add(box);
		box = Box.createHorizontalBox();
		box.add(new JLabel("yaw"));
		box.add(Box.createHorizontalStrut(5));
		box.add(yaw);
		add(box);
		
		box = Box.createHorizontalBox();
		box.add(new JLabel("anglex"));
		box.add(Box.createHorizontalStrut(5));
		box.add(anglex);
		add(box);
		
		box = Box.createHorizontalBox();
		box.add(new JLabel("angley"));
		box.add(Box.createHorizontalStrut(5));
		box.add(angley);
		add(box);
		
		box = Box.createHorizontalBox();
		box.add(new JLabel("head"));
		box.add(Box.createHorizontalStrut(5));
		box.add(head);
		add(box);

		ax.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		ay.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		az.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		gx.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		gy.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		gz.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		mx.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		my.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		mz.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

		rotor1.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		rotor2.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		rotor3.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		rotor4.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

		trot.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		roll.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		pitch.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		yaw.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		
		anglex.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		angley.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		head.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

	}

	@Override
	public void changed(Copter event) {
		ax.setText(String.valueOf(event.getAx()));
		ay.setText(String.valueOf(event.getAy()));
		az.setText(String.valueOf(event.getAz()));

		gx.setText(String.valueOf(event.getGx()));
		gy.setText(String.valueOf(event.getGy()));
		gz.setText(String.valueOf(event.getGz()));

		mx.setText(String.valueOf(event.getMx()));
		my.setText(String.valueOf(event.getMy()));
		mz.setText(String.valueOf(event.getMz()));

		rotor1.setText(String.valueOf(event.getR1()));
		rotor2.setText(String.valueOf(event.getR2()));
		rotor3.setText(String.valueOf(event.getR3()));
		rotor4.setText(String.valueOf(event.getR4()));

		trot.setText(String.valueOf(event.getTrottle()));
		roll.setText(String.valueOf(event.getRoll()));
		pitch.setText(String.valueOf(event.getPitch()));
		yaw.setText(String.valueOf(event.getYaw()));
		

		final int angx=event.getAngx() / 10;
		final int angy=event.getAngy() / 10;
		final float a = radians(angx);
		float b;
		if (angy < -90) {
			b = radians(-180 - angy);
		} else if (angy > 90){
			b = radians(+180 - angy);
		}else{
			b = radians(angy);
		}
		final float h = radians(event.getHead());
		
		anglex.setText(String.valueOf(event.getAngx()/10));
		angley.setText(String.valueOf(event.getAngy()/10));
		head.setText(String.valueOf(event.getHead()));
	}
	public float radians(int ang){
		return (float)(ang*Math.PI/180);
	}

}
