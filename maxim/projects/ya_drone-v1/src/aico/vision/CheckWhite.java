package aico.vision;

import aico.core.DroneControl;
import aico.flight.FlightControl;
import aico.helpers.Constants;

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
    private final DroneControl drone;
    private boolean targetFound = false;

    public CheckWhite(FlightControl control, DroneControl drone) {
        this.control = control;
        this.drone = drone;
    }

    public void parseImage(BufferedImage image) {
        if (isThereWhite(image)) {
            //System.out.println("TARGET DETECTED!");
            targetFound = true;
        }
    }

    public boolean isTargetFound() {
        return targetFound;
    }

    public void setTargetFound(boolean targetFound) {
        this.targetFound = targetFound;
    }

    private boolean isThereWhite(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        Graphics gr = image.getGraphics();

        // Draw battery
        gr.setColor(new Color(138, 242, 255));
        gr.fillRect(0, 0, (int) (drone.getBatteryValue() / 100.0 * w), 3);

        int totalNumberOfWhitePixels = 0;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int pixel = image.getRGB(i, j);
                Color c = new Color(pixel);

                if (isPixelWhite(c)) {
                    totalNumberOfWhitePixels++;
                    gr.setColor(new Color(255, 0, 0));
                    gr.fillRect(i, j, 1, 1);
                }
            }
        }

        if (totalNumberOfWhitePixels > Constants.WHITE_QUANTITY_THRESHOLD) {
            gr.setColor(new Color(0, 255, 0));
            gr.fillRect(0, h - 5, w, 5);
            return true;
        } else {
            gr.setColor(new Color(0, 0, 0));
            gr.fillRect(0, h - 5, w, 5);
            return false;
        }
    }

    private boolean isPixelWhite(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        int bw = (r + g + b) / 3;
        return bw > Constants.WHITE_COLOR_THRESHOLD;
    }
}
