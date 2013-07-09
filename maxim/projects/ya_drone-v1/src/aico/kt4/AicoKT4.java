package aico.kt4;

import aico.core.DroneControl;
import aico.flight.FlightControl;
import aico.flight.FlightControlArDrone;
import aico.gui.GuiWindow;
import aico.helpers.Constants;
import aico.vision.CheckWhite;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:16 PM
 * Comment: Main class for KT4.
 */
public class AicoKT4 {

    public static void main(String args[]) {
        GuiWindow gui = new GuiWindow();
        final DroneControl drone = new DroneControl();
        final FlightControl control = new FlightControlArDrone(drone.getArdrone());
        CheckWhite vision = new CheckWhite(control, drone);
        drone.initializeVideoFrame(gui, vision);
        drone.useBottomCamera();

        gui.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_SPACE:
                        control.panic();
                        break;
                }
            }
        });

        Date lastDate = new Date(System.currentTimeMillis());
        while (true) {
            if (lastDate.getTime() + Constants.WAIT_INITIALIZATION < System.currentTimeMillis()) {
                break;
            }
        }

        //noinspection PointlessBooleanExpression,ConstantConditions
        if (!Constants.ONLY_VISION_DEMO) {

            System.out.println("TAKE OFF START");
            control.takeOff();
            //drone.useBottomCamera();

            lastDate = new Date(System.currentTimeMillis());
            while (true) {
                if (lastDate.getTime() + Constants.WAIT_TAKE_OFF < System.currentTimeMillis()) {
                    System.out.println("TAKE OFF FINISH");
                    break;
                }
            }

            System.out.println("SET TARGET NOT FOUND");
            vision.setTargetFound(false);

            lastDate = new Date(System.currentTimeMillis());
            while (!vision.isTargetFound()) {
                if (lastDate.getTime() + Constants.WAIT_UNTIL_FLY_FORWARD < System.currentTimeMillis()) {
                    lastDate = new Date(System.currentTimeMillis());
                    control.flyBackward();
                    System.out.println("FLY FORWARD");
                }
            }

            System.out.println("LAND");
            control.land();

        }
    }

}
