package exceptions;

/**
 * Ошибка проверки данных клиента.
 *
 * @author rinat
 */
public class ValidatorException extends Exception {
    /**
     * Конструктор.
     *
     * @param message Сообщение ошибки
     */
    public ValidatorException(String message) {
        super(message);
    }
}
