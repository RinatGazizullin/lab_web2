package exceptions;

/**
 * Ошибка в чтении данных клиента.
 *
 * @author rinat
 */
public class DataException extends Exception {
    /**
     * Конструктор.
     *
     * @param message Сообщение ошибки
     */
    public DataException(String message) {
        super(message);
    }
}
