package aico.kt4;

import aico.core.DroneControl;
import aico.flight.FlightControl;
import aico.flight.FlightControlArDrone;
import aico.gui.GuiWindow;
import aico.helpers.Constants;
import aico.vision.CheckWhite;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:16 PM
 * Comment: Main class for KT4.
 */
@SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
public class AicoKT4 {

    static Map<Integer, Date> lastDateMap = new HashMap<Integer, Date>();
    static boolean landNow = false;

    public static void main(String args[]) {
        GuiWindow gui = new GuiWindow();
        final DroneControl drone = new DroneControl();
        final FlightControl control = new FlightControlArDrone(drone.getArdrone());
        CheckWhite vision = new CheckWhite(control, drone);
        drone.initializeVideoFrame(gui, vision);
        drone.useBottomCamera();
        drone.useBottomCamera();

        setEmergencyButtons(gui, control);

        realSleep(Constants.WAIT_INITIALIZATION);

        if (!Constants.ONLY_VISION_DEMO) {

            System.out.println("TAKE OFF (1/2): START");
            control.takeOff();

            if (Constants.DOUBLE_CAMERA_INITIALIZATION) drone.useBottomCamera();

            moveCounterTillNow(0);
            while (true) {
                if (mayProceed(0, Constants.WAIT_TAKE_OFF)) {
                    System.out.println("TAKE OFF (2/2): FINISH");
                    break;
                }
            }

            moveCounterTillNow(0);
            moveCounterTillNow(1);
            while (true) {
                if (mayProceed(0, Constants.WAIT_UNTIL_FLY_FORWARD)) {
                    flyForward(control);
                    moveCounterTillNow(0);
                    System.out.print("-");
                }
                if (mayProceed(1, Constants.WAIT_UNTIL_DETECT_MODE_ON)) {
                    System.out.println("\nFLYING WITH DETECT MODE ON (1/1): STARTED");
                    break;
                }
            }

            System.out.println("SET TARGET MARKER AS NOT FOUND");
            vision.setTargetFound(false);

            moveCounterTillNow(0);
            while (!vision.isTargetFound()) {
                if (mayProceed(0, Constants.WAIT_UNTIL_FLY_FORWARD) && !landNow) {
                    flyForward(control);
                    moveCounterTillNow(0);
                    System.out.print("-");
                }
            }

            System.out.println("START LANDING, MARKER WAS FOUND");
            control.land();

        }
    }

    private static void flyForward(FlightControl control) {
        if (Constants.INVERT_FLY_FORWARD_DIRECTION) control.flyBackward();
        else control.flyForward();
    }

    /**
     * The fucking sleep method. It uses method of counting difference in time and infinite loop.
     * Perfect for one thread program with gui.
     *
     * @param timeToSleep time to sleep
     */
    private static void realSleep(long timeToSleep) {
        Date lastDate = new Date(System.currentTimeMillis());
        while (true) {
            if (lastDate.getTime() + timeToSleep < System.currentTimeMillis()) {
                break;
            }
        }
    }

    /**
     * This is fucking stupid method to sleep but not to stop the thread. It checks each time the difference
     * between now and last successful passing. This function does not block the process.
     *
     * @param timeCounter number of the parallel universe
     * @param timeToWait  check if this time has passed from the last method call
     * @return true if time has passed
     */
    private static boolean mayProceed(int timeCounter, long timeToWait) {
        return lastDateMap.get(timeCounter).getTime() + timeToWait < System.currentTimeMillis();
    }

    /**
     * Sets the time counter to now.
     *
     * @param timeCounter number of the parallel universe
     */
    private static void moveCounterTillNow(int timeCounter) {
        lastDateMap.put(timeCounter, new Date(System.currentTimeMillis()));
    }

    /**
     * This method adds ENTER as an emergency button, press it and all engines will stop.
     * Also press SPACE and copter will try to land correctly.
     *
     * @param gui     graphical user interface for buttons listening
     * @param control flight control system to process the key pressing
     */
    private static void setEmergencyButtons(GuiWindow gui, final FlightControl control) {
        gui.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_ENTER:
                        System.out.println("PANIC BUTTON WAS PRESSED");
                        control.panic();
                        break;
                    case KeyEvent.VK_SPACE:
                        landNow = true;
                        System.out.println("STARTING EMERGENCY SOFT LANDING");
                        realSleep(1000);
                        control.land();
                }
            }
        });
    }

}
