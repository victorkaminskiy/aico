package aico.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:52 PM
 * Comment: GUI for user controls and image viewing.
 */
public class GuiWindow extends JFrame {
    private MyPanel myPanel;

    public GuiWindow() {
        myPanel = new MyPanel();
        this.add(myPanel);

        this.setTitle("Aico Video Presenter");
        this.setSize(600, 400);
        this.setVisible(true);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setImage(BufferedImage image) {
        myPanel.setImage(image);
    }

    public void repaint() {
        myPanel.repaint();
    }

    private class MyPanel extends JPanel {
        private static final long serialVersionUID = -7635284252404123776L;

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
