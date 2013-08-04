package core;

import gui.GuiWindow;
import helpers.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * User: Maxim
 * Date: 8/4/13
 * Time: 5:11 PM
 * Comment: Get the image from web and show in GUI.
 */
public class WebImageReader {
    public static void main(String[] args) {
        GuiWindow gui = new GuiWindow();
        String urlFromForm = JOptionPane.showInputDialog(
                gui,
                "Enter custom JPEG image URL. Press Cancel to use default value.",
                "Enter image URL",
                JOptionPane.QUESTION_MESSAGE);
        if (urlFromForm == null || urlFromForm.isEmpty()) urlFromForm = Constants.IMAGE_URL;

        gui.setUrlLabel(urlFromForm);

        long totalNumberOfFrames = 0;

        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                long frameGetTime = System.currentTimeMillis();
                URL url = new URL(Constants.IMAGE_URL);
                BufferedImage image = ImageIO.read(url);
                gui.setImage(image);
                gui.repaint();
                totalNumberOfFrames++;
                long frameOverallTime = System.currentTimeMillis() - frameGetTime;
                double framesPerSecond = 1000.0 / frameOverallTime;
                gui.setStatLabel("Total number of frames: " + totalNumberOfFrames + ", FPS: " + framesPerSecond);
            } catch (IOException e) {
                System.out.println("Error during image retrieving");
            }
        }
    }
}
