package aico.core;

import aico.gui.GuiWindow;
import aico.helpers.Constants;
import aico.vision.CheckWhite;
import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.navdata.*;
import com.shigeodayo.ardrone.video.ImageListener;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 9:18 PM
 * Comment: Used for copter initialization and beginning the network communication.
 */
public class DroneControl {
    private ARDrone ardrone;
    private int batteryValue;

    public DroneControl() {
        ardrone = new ARDrone("192.168.1.1");
        System.out.println("Connect drone controller");
        ardrone.connect();
        System.out.println("Connect drone navdata");
        ardrone.connectNav();
        System.out.println("Connect drone video");
        ardrone.connectVideo();
        System.out.println("Start drone");
        ardrone.start();

        startMonitoringBattery();

        if (Constants.PRINT_NAV_LOG) {
            printLoggingData();
        }
    }

    public ARDrone getArdrone() {
        return ardrone;
    }

    public void initializeVideoFrame(final GuiWindow videoPanel, final CheckWhite vision) {
        ardrone.addImageUpdateListener(new ImageListener() {
            @Override
            public void imageUpdated(BufferedImage image) {
                if (videoPanel != null) {
                    vision.parseImage(image);
                    videoPanel.setImage(image);
                    videoPanel.repaint();
                }
            }
        });
    }

    private void startMonitoringBattery() {
        ardrone.addBatteryUpdateListener(new BatteryListener() {
            @Override
            public void batteryLevelChanged(int percentage) {
                batteryValue = percentage;
            }
        });
    }

    public int getBatteryValue() {
        return batteryValue;
    }

    public void useForwardCamera() {
        ardrone.setHorizontalCamera();
    }

    public void useBottomCamera() {
        ardrone.setVerticalCamera();
    }


    private void printLoggingData() {
        ardrone.addAttitudeUpdateListener(new AttitudeListener() {
            @Override
            public void attitudeUpdated(float pitch, float roll, float yaw, int altitude) {
                System.out.println(new Date(System.currentTimeMillis()).toString() +
                        ": pitch: " + pitch + ", roll: " + roll + ", yaw: " + yaw + ", altitude: " + altitude);
            }
        });

        ardrone.addBatteryUpdateListener(new BatteryListener() {
            @Override
            public void batteryLevelChanged(int percentage) {
                System.out.println(new Date(System.currentTimeMillis()).toString() +
                        ": battery: " + percentage + " %");
            }
        });

        ardrone.addStateUpdateListener(new StateListener() {
            @Override
            public void stateChanged(DroneState state) {
                System.out.println(new Date(System.currentTimeMillis()).toString() +
                        ": state: " + state);
            }
        });

        ardrone.addVelocityUpdateListener(new VelocityListener() {
            @Override
            public void velocityChanged(float vx, float vy, float vz) {
                System.out.println(new Date(System.currentTimeMillis()).toString() +
                        ": vx: " + vx + ", vy: " + vy + ", vz: " + vz);
            }
        });
    }
}
