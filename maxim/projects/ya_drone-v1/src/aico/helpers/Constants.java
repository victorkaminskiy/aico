package aico.helpers;

import java.util.Date;

/**
 * User: Maxim
 * Date: 7/6/13
 * Time: 8:30 PM
 * Comment: Different set up constants.
 */
public class Constants {
    // Порог серого, чтобы считать его белым
    public static final int WHITE_COLOR_THRESHOLD = 180;
    // Количество белых пикселей, необходимых для посадки
    public static final int WHITE_QUANTITY_THRESHOLD = 100000;

    // Ждать до команды взлета (в милисекундах), чтобы камера проинициализировалась и картинка появилась
    public static final long WAIT_INITIALIZATION = 12000;
    // Ждать после команды взлета (в милисекундах) перед тем как ничинать движение вперед
    public static final long WAIT_TAKE_OFF = 3000;
    // Ждать после каждой команды "лететь вперед" (в милисекундах) перед тем как выполнять следующую
    public static final long WAIT_UNTIL_FLY_FORWARD = 250;
    // Лететь данное время (в милисекундах) вперед без включенного режима отслеживания метки
    public static final long WAIT_UNTIL_DETECT_MODE_ON = 2000;

    // При true коптер не будет включать двигатели, используется для калибровки нахождения метки
    public static final boolean ONLY_VISION_DEMO = false;
    // При true печатает в лог навигационные данные с коптера
    public static final boolean PRINT_NAV_LOG = false;
    // Повторная инициализация камеры, смотрящей вниз, на случай бага в прошивке
    public static final boolean DOUBLE_CAMERA_INITIALIZATION = true;
    // Баг с полетом вперед-назад, при true будет подана команда лететь назад
    public static final boolean INVERT_FLY_FORWARD_DIRECTION = true;
}
