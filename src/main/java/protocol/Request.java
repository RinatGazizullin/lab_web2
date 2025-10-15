package protocol;

import java.math.BigDecimal;

/**
 * Запрос от клиента.
 *
 * @param x Координата x
 * @param y Координата y
 * @param r Коэффициент
 *
 * @author rinat
 */
public record Request(Float x, BigDecimal y, Float r) {
}
