package helpers;

/**
 * User: Maxim
 * Date: 8/4/13
 * Time: 7:39 PM
 * Comment: Constants for all cases.
 */
public class Constants {
    public static final int IMAGE_WIDTH = 640;
    public static final int IMAGE_HEIGHT = 480;
    public static final String IMAGE_URL = "http://192.168.1.101:8080/shot.jpg";

    // Порог серого, чтобы считать его белым
    public static final int WHITE_COLOR_THRESHOLD = 180;
    // Количество белых пикселей, необходимых для посадки
    public static final int WHITE_QUANTITY_THRESHOLD = 100000;


}
