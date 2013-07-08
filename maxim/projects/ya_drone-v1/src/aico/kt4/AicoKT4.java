package aico.kt4;

import aico.core.DroneControl;
import aico.flight.FlightControl;
import aico.flight.FlightControlArDrone;
import aico.gui.GuiWindow;
import aico.vision.CheckWhite;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:16 PM
 * Comment: Main class for KT4.
 */
public class AicoKT4 {

    public static void main(String args[]) {
        GuiWindow gui = new GuiWindow();
        DroneControl drone = new DroneControl();
        FlightControl control = new FlightControlArDrone(drone.getArdrone());
        CheckWhite vision = new CheckWhite(control);
        drone.initializeVideoFrame(gui, vision);
        drone.useBottomCamera();
    }

}
