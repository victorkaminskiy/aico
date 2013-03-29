package com.aico.remote;

public class Copter {
	private long lastRefreshTime;
	private short ax;
	private short ay;
	private short az;
	private short gx;
	private short gy;
	private short gz;
	private short mx;
	private short my;
	private short mz;

	private short r1;
	private short r2;
	private short r3;
	private short r4;

	private short trot;
	private short roll;
	private short pitch;
	private short yaw;

	public Copter() {
	}

	public void setLastRefreshTime(long time) {
		this.lastRefreshTime = time;
	}

	public void setAx(short ax) {
		this.ax = ax;
	}

	public void setAy(short ay) {
		this.ay = ay;
	}

	public void setAz(short az) {
		this.az = az;
	}

	public void setGx(short gx) {
		this.gx = gx;
	}

	public void setGy(short gy) {
		this.gy = gy;
	}

	public void setGz(short gz) {
		this.gz = gz;
	}

	public void setMx(short mx) {
		this.mx = mx;
	}

	public void setMy(short my) {
		this.my = my;
	}

	public void setMz(short mz) {
		this.mz = mz;
	}

	public void setR1(short r1) {
		this.r1 = r1;
	}

	public void setR2(short r2) {
		this.r2 = r2;
	}

	public void setR3(short r3) {
		this.r3 = r3;
	}

	public void setR4(short r4) {
		this.r4 = r4;
	}

	public void setTrottle(short trot) {
		this.trot = trot;
	}

	public void setRoll(short roll) {
		this.roll = roll;
	}

	public void setPitch(short pitch) {
		this.pitch = pitch;
	}

	public void setYaw(short yaw) {
		this.yaw = yaw;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
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