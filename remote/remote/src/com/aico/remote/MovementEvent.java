package com.aico.remote;

public class MovementEvent {
	public long time;
	public float ax;
	public float ay;
	public float az;
	public float gx;
	public float gy;
	public float gz;
	public float mx;
	public float my;
	public float mz;

	public MovementEvent(long time, float ax, float ay, float az, float gx,
			float gy, float gz, float mx, float my, float mz) {
		this.time = time;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
		this.gx = gx;
		this.gy = gy;
		this.gz = gz;
		this.mx = mx;
		this.my = my;
		this.mz = mz;
	}

	public long getTime() {
		return time;
	}

	public float getAx() {
		return ax;
	}

	public float getAy() {
		return ay;
	}

	public float getAz() {
		return az;
	}

	public float getGx() {
		return gx;
	}

	public float getGy() {
		return gy;
	}

	public float getGz() {
		return gz;
	}

	public float getMx() {
		return mx;
	}

	public float getMy() {
		return my;
	}

	public float getMz() {
		return mz;
	}
	
	
}