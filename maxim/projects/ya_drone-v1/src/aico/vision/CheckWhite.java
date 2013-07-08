package aico.vision;

import aico.core.DroneControl;
import aico.flight.FlightControl;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 9:38 PM
 * Comment: Check if there is enough white pixels in the image.
 */
public class CheckWhite {
    private final FlightControl control;

    public CheckWhite(FlightControl control) {
        this.control = control;
    }

    public void parseImage(BufferedImage image) {
        if (isThereWhite(image)) {
//            control.land();
            System.out.println("DETECTED!!!");
        }
    }

    public boolean isThereWhite(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        w = 1;
        h = 1;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int pixel = image.getRGB(i, j);
                Graphics gr = image.getGraphics();
                System.out.println("HERE: " + String.valueOf(pixel));
                gr.setColor(new Color(pixel));
                gr.fillRect(1, 1, 100, 100);
            }
        }
        return true;
    }
}
