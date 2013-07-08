package aico.flight;

import com.shigeodayo.ardrone.ARDrone;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:22 PM
 * Comment: FlightControl realization for Ar.Drone.
 */
public class FlightControlArDrone implements FlightControl {
    private ARDrone ardrone;

    public FlightControlArDrone(ARDrone ardrone) {
        this.ardrone = ardrone;
    }

    @Override
    public void flyForward() {
        ardrone.backward();
    }

    @Override
    public void flyBackward() {
        ardrone.forward();
    }

    @Override
    public void flyLeft() {
        ardrone.goLeft();
    }

    @Override
    public void flyRight() {
        ardrone.goRight();
    }

    @Override
    public void rotateLeft() {
        ardrone.spinLeft();
    }

    @Override
    public void rotateRight() {
        ardrone.spinRight();
    }

    @Override
    public void takeOff() {
        ardrone.takeOff();
    }

    @Override
    public void land() {
        ardrone.landing();
    }

    @Override
    public void altitudeUp() {
        ardrone.up();
    }

    @Override
    public void altitudeDown() {
        ardrone.down();
    }

    @Override
    public void panic() {
        ardrone.reset();
    }

    @Override
    public void stopCurrentAction() {
        ardrone.stop();
    }
}
