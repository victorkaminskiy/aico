package com.aico.remote;

public class MovementEvent {
	public long time;
	public short ax;
	public short ay;
	public short az;
	public short gx;
	public short gy;
	public short gz;
	public short mx;
	public short my;
	public short mz;

	public short r1;
	public short r2;
	public short r3;
	public short r4;

	public short trot;
	public short roll;
	public short pitch;
	public short yaw;

	public MovementEvent(long time, short ax, short ay, short az, short gx,
			short gy, short gz, short mx, short my, short mz, short r1,
			short r2, short r3, short r4, short trot, short roll, short pitch,
			short yaw) {
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
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
		this.r4 = r4;
		this.trot = trot;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public long getTime() {
		return time;
	}

	public short getAx() {
		return ax;
	}

	public short getAy() {
		return ay;
	}

	public short getAz() {
		return az;
	}

	public short getGx() {
		return gx;
	}

	public short getGy() {
		return gy;
	}

	public short getGz() {
		return gz;
	}

	public short getMx() {
		return mx;
	}

	public short getMy() {
		return my;
	}

	public short getMz() {
		return mz;
	}

	public short getR1() {
		return r1;
	}

	public short getR2() {
		return r2;
	}

	public short getR3() {
		return r3;
	}

	public short getR4() {
		return r4;
	}

	public short getTrot() {
		return trot;
	}

	public short getRoll() {
		return roll;
	}

	public short getPitch() {
		return pitch;
	}

	public short getYaw() {
		return yaw;
	}

}