package aico.flight;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:21 PM
 * Comment: Interface of flight control systems.
 */
public interface FlightControl {
    /**
     * Fly forward.
     */
    public void flyForward();

    /**
     * Fly backward.
     */
    public void flyBackward();

    /**
     * Fly left.
     */
    public void flyLeft();

    /**
     * Fly right.
     */
    public void flyRight();

    /**
     * Rotate left.
     */
    public void rotateLeft();

    /**
     * Rotate right.
     */
    public void rotateRight();

    /**
     * Take off.
     */
    public void takeOff();

    /**
     * Land.
     */
    public void land();

    /**
     * Go up.
     */
    public void altitudeUp();

    /**
     * Go down.
     */
    public void altitudeDown();

    /**
     * Stops the copter in case of emergency.
     */
    public void panic();

    /**
     * Stops the current action of the copter and prepare for the next.
     */
    public void stopCurrentAction();
}
