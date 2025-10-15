package builder;

import exceptions.DataException;
import java.util.Map;

/**
 * Класс для сборки.
 *
 * @param <T> Тип данных
 *
 *
 * @author rinat
 */
public interface Builder<T> {
    /**
     * Метод для сборки из строки.
     *
     * @param data Данные для сборки
     * @return Собранные объект
     * @throws DataException Неверный формат
     */
    T build(Map<String, Object> data) throws DataException;
}
