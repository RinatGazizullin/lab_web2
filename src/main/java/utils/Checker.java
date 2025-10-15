package utils;

import protocol.Request;
import java.math.BigDecimal;

/**
 * Класс для проверки попадания точки в график.
 *
 * @author rinat
 */
public class Checker {
    /**
     * Метод для проверки попадания.
     *
     * @param data Данные клиента
     * @return Попал ли
     */
    public boolean isHit(Request data) {
        return quarter2(data) || quarter3(data) || quarter4(data);
    }

    private boolean quarter2(Request data) {
        if (data.x() <= 0 && data.y().compareTo(BigDecimal.ZERO) >= 0) {
            return BigDecimal.valueOf(Math.pow(data.x(), 2) - Math.pow(data.r() * .5, 2))
                    .add(data.y().multiply(data.y())).compareTo(BigDecimal.ZERO) <= 0;
        }
        return false;
    }

    private boolean quarter3(Request data) {
        if (data.x() <= 0 && data.y().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.valueOf(data.x() + data.r() * .5).compareTo(data.y()
                    .multiply(BigDecimal.valueOf(-.5))) >= 0;
        }
        return false;
    }

    private boolean quarter4(Request data) {
        if (data.x() >= 0 && data.y().compareTo(BigDecimal.ZERO) <= 0) {
            return data.x() <= data.r() && data.y().compareTo(BigDecimal.valueOf(-.5 * data.r())) >= 0;
        }
        return false;
    }
}
