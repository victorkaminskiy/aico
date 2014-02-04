package vision;

import helpers.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 9:38 PM
 * Comment: Check if there is enough white pixels in the image.
 */
public class CheckWhite {

    public void parseImage(BufferedImage image) {
        isThereWhite(image);
    }

    private boolean isThereWhite(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();

        Graphics gr = image.getGraphics();

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
            gr.fillRect(0, h - 30, w, 30);
            return true;
        } else {
            gr.setColor(new Color(186, 149, 193));
            gr.fillRect(0, h - 30, w, 30);
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
