package com.aico.remote;

import java.util.Stack;

/**
 * User: Maxim
 * Date: 8/11/13
 * Time: 7:41 PM
 * Comment: PID Regulator singleton class. Access using INSTANCE constant.
 * URL: http://en.wikipedia.org/wiki/PID_controller
 */
public class PidRegulator {
    /**
     * Instance access.
     */
    public static final PidRegulator INSTANCE = new PidRegulator();

    /**
     * Locked private class constructor.
     */
    private PidRegulator() {
    }

    private double previousError = 0;
    private double integral = 0;
    private long lastTime = 0;

    private double Kp = Constants.PID_FACTOR_K_P;
    private double Ki = Constants.PID_FACTOR_K_I;
    private double Kd = Constants.PID_FACTOR_K_D;

    private Stack<Double> historyOfOutput = new Stack<Double>();

    /**
     * Clear all values except factors.
     */
    public void clear() {
        previousError = 0;
        integral = 0;
        lastTime = 0;
    }

    /**
     * Set factors for PID.
     * @param Kp for proportional
     * @param Ki for integral
     * @param Kd for derivative
     */
    public void setFactor(double Kp, double Ki, double Kd) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    /**
     * Calculate the next output value.
     * @param measuredHeight current height of copter
     * @param requiredHeight required height of copter
     * @return new throttle value (not yet calibrated)
     */
    public double calculate(double measuredHeight, double requiredHeight) {
        // If time is equal to zero (we have just started to flight, then skip first loop
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
            return 0;
        }

        // Calculate current error in height
        final double error = requiredHeight - measuredHeight;

        // Calculate time from last loop iteration (delta time)
        final long dt = System.currentTimeMillis() - lastTime;
        lastTime += dt;

        // Calculate Integral part
        integral += integral + error * dt;

        // Calculate Derivative part
        final double derivative = (error - previousError) / dt;

        // Calculate the result effort
        double output = Kp * error + Ki * integral + Kd * derivative;
        output = normalizeResult(output);
        addToHistory(output);

        // Save history for debugging
        historyOfOutput.push(output);

        return output;
    }

    /**
     * Check the output values (not to burn out the throttles) and normalize the result.
     * @param rawOutput output from raw PID controller
     * @return normalized result
     */
    private double normalizeResult(double rawOutput) {
        if (rawOutput > Constants.PID_MAX_OUTPUT) return Constants.PID_MAX_OUTPUT;
        else if (rawOutput < Constants.PID_MIN_OUTPUT) return Constants.PID_MIN_OUTPUT;
        else return rawOutput;
    }

    /**
     * Save old error for derivative part.
     * @param output the result new throttle value from PID
     */
    private void addToHistory(double output) {
        historyOfOutput.push(output);
        if (historyOfOutput.size() > Constants.PID_HISTORY_OF_OUTPUT_LENGTH) {
            historyOfOutput.pop();
        }
    }


}
