package com.aico.remote;

/**
 * User: Maxim
 * Date: 8/11/13
 * Time: 7:53 PM
 * Comment: List of global constants for system settings.
 */
public class Constants {
    /**
     * Describes if we use new PID controller or old way for taking off.
     */
    public static boolean USE_PID = true;

    /**
     * PID factor for proportional part.
     */
    public static double PID_FACTOR_K_P = 1;

    /**
     * PID factor for integral part.
     */
    public static double PID_FACTOR_K_I = 1;

    /**
     * PID factor for derivative part.
     */
    public static double PID_FACTOR_K_D = 1;

    /**
     * Sets up the maximum allowed value for throttles.
     */
    public static double PID_MAX_OUTPUT = 1;

    /**
     * Sets up the minimum allowed value for throttles.
     */
    public static double PID_MIN_OUTPUT = 0;

    /**
     * Length of PID history.
     */
    public static int PID_HISTORY_OF_OUTPUT_LENGTH = 100;

}
