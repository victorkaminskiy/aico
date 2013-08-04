package gui;

import helpers.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:52 PM
 * Comment: GUI for user controls and image viewing.
 */
public class GuiWindow extends JFrame {
    private ImagePanel imagePanel;
    private JLabel urlLabel;
    private JLabel statLabel;

    public GuiWindow() {
        Container pane = this.getContentPane();

        urlLabel = new JLabel("");
        pane.add(urlLabel, BorderLayout.PAGE_START);
        statLabel = new JLabel("");
        pane.add(statLabel, BorderLayout.PAGE_END);
        imagePanel = new ImagePanel();
        pane.add(imagePanel, BorderLayout.CENTER);

        this.setTitle("Web Video Presenter");
        this.setSize(Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT + 50);
        this.setVisible(true);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setUrlLabel(String url) {
        urlLabel.setText("Image URL: " + url);
    }

    public void setStatLabel(String statistics) {
        statLabel.setText(statistics);
    }

    public void setImage(BufferedImage image) {
        imagePanel.setImage(image);
    }

    public void repaint() {
        imagePanel.repaint();
    }

    private class ImagePanel extends JPanel {
        private BufferedImage image;

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        public void paint(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            if (image != null)
                g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        }
    }
}
