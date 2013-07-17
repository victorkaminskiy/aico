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

	private short throttle;
	private short roll;
	private short pitch;
	private short yaw;

	private short angx;
	private short angy;
	private short head;
	private short alt;
	private short rc5;
	private short rc6;
	private short rc7;
	private short rc8;
	private short sonar;
	private short holdAlt;
	private boolean log=true;
	private short baroPid;
	

	public Copter() {
	}
	
	
	
	public short getHoldAlt() {
		return holdAlt;
	}



	public void setHoldAlt(short holdAlt) {
		this.holdAlt = holdAlt;
	}



	public void setSonar(short sonar){
		this.sonar=sonar;
	}
	
	public short getSonar(){
		return sonar;
	}
	
	public void setAlt(short alt){
		this.alt=alt;
	}
	
	public short getAlt(){
		return alt;
	}

	public void setLastRefreshTime(long time) {
		this.lastRefreshTime = time;
	}

	public short getAngx() {
		return angx;
	}

	public void setAngx(short angx) {
		if(log){
			System.out.println("AngX= "+angx);
		}
		this.angx = angx;
	}

	public short getAngy() {
		return angy;
	}

	public void setAngy(short angy) {
		if(log){
			System.out.println("Angy= "+angy);
		}
		this.angy = angy;
	}

	public short getHead() {
		return head;
	}

	public void setHead(short head) {
		if(log){
			System.out.println("Head= "+head);
		}
		this.head = head;
	}

	public void setAx(short ax) {
		if(log){
			System.out.println("ax= "+ax);
		}
		this.ax = ax;
	}

	public void setAy(short ay) {
		if(log){
			System.out.println("ay= "+ay);
		}
		this.ay = ay;
	}

	public void setAz(short az) {
		if(log){
			System.out.println("az= "+az);
		}
		this.az = az;
	}

	public void setGx(short gx) {
		if(log){
			System.out.println("gx= "+gx);
		}
		this.gx = gx;
	}

	public void setGy(short gy) {
		if(log){
			System.out.println("gy= "+gy);
		}
		this.gy = gy;
	}

	public void setGz(short gz) {
		if(log){
			System.out.println("gz= "+gz);
		}
		this.gz = gz;
	}

	public void setMx(short mx) {
		if(log){
			System.out.println("mx= "+mx);
		}
		this.mx = mx;
	}

	public void setMy(short my) {
		if(log){
			System.out.println("my= "+my);
		}
		this.my = my;
	}

	public void setMz(short mz) {
		if(log){
			System.out.println("mz= "+mz);
		}
		this.mz = mz;
	}

	public void setR1(short r1) {
		if(log){
			System.out.println("r1= "+r1);
		}
		this.r1 = r1;
	}

	public void setR2(short r2) {
		if(log){
			System.out.println("r2= "+r2);
		}
		this.r2 = r2;
	}

	public void setR3(short r3) {
		if(log){
			System.out.println("r3= "+r3);
		}
		this.r3 = r3;
	}

	public void setR4(short r4) {
		if(log){
			System.out.println("r4= "+r4);
		}
		this.r4 = r4;
	}

	public void setThrottle(short throttle) {
		if(log){
			System.out.println("throttle= "+throttle);
		}
		this.throttle = throttle;
	}

	public void setRoll(short roll) {
		if(log){
			System.out.println("roll= "+roll);
		}
		this.roll = roll;
	}

	public void setPitch(short pitch) {
		if(log){
			System.out.println("pitch= "+pitch);
		}
		this.pitch = pitch;
	}

	public void setYaw(short yaw) {
		if(log){
			System.out.println("yaw= "+yaw);
		}
		this.yaw = yaw;
	}
	
	public void setRC5(short rc) {
		if(log){
			System.out.println("rc5= "+rc);
		}
		this.rc5 = rc;
	}
	
	public short getRC5() {
		return rc5;
	}
	
	public short getRC8() {
		return rc8;
	}
	
	public void setRC6(short rc) {
		if(log){
			System.out.println("rc6= "+rc);
		}
		this.rc6 = rc;
	}
	
	public void setRC7(short rc) {
		if(log){
			System.out.println("rc7= "+rc);
		}
		this.rc7 = rc;
	}
	
	public void setRC8(short rc) {
		if(log){
			System.out.println("rc8= "+rc);
		}
		this.rc8 = rc;
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

	public short getTrottle() {
		return throttle;
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



	public short getBaroPid() {
		return baroPid;
	}



	public void setBaroPid(short baroPid) {
		this.baroPid=baroPid;
	}

}