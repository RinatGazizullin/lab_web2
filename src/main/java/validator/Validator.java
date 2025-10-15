package validator;

import exceptions.ValidatorException;

/**
 * Проверка данных клиента.
 *
 * @param <T> Тип данных для проверки
 */
public interface Validator<T> {
    /**
     * Метод проверки.
     *
     * @param t Объект для проверки
     * @throws ValidatorException Ошибка проверка
     */
    void validate(T t) throws ValidatorException;
}
