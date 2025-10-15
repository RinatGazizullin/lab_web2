package validator;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import exceptions.ValidatorException;
import java.util.List;
import java.util.Map;

/**
 * Проверка данных клиента.
 */
public class RequestValidator implements Validator<Map<String, Object>> {
    private static final Gson gson = new Gson();

    /**
     * Основной метод проверки.
     *
     * @param request Объект для проверки
     * @throws ValidatorException Ошибка проверки
     */
    @Override
    public void validate(Map<String, Object> request) throws ValidatorException {
        validateRequest(request);
        validateX(request.get("x"));
        validateY(request.get("y"));
        validateR(request.get("r"));
    }

    private void validateRequest(Map<String, Object> request) throws ValidatorException {
        if (request == null) {
            throw new ValidatorException("Запрос не может быть пустым");
        }
        if (request.get("x") == null || request.get("y") == null || request.get("r") == null) {
            throw new ValidatorException("Неверный формат запроса");
        }
    }

    private void validateX(Object s) throws ValidatorException {
        if (s == null) {
            throw new ValidatorException("Нужно выбрать X");
        }
        if (!(s instanceof List)) {
            throw new ValidatorException("Неверный формат X");
        }
        try {
            gson.fromJson(s.toString(), new TypeToken<List<String>>(){}.getType());
        } catch (JsonParseException e) {
            throw new ValidatorException("Неверный формат чисел запроса");
        }
    }

    private void validateY(Object s) throws ValidatorException {
        if (s == null) {
            throw new ValidatorException("Нужно выбрать Y");
        }
        if (!(s instanceof String)) {
            throw new ValidatorException("Неверный формат Y");
        }
        final float y;
        try {
            y = Float.parseFloat((String) s);
        } catch (NumberFormatException e) {
            throw new ValidatorException("Y должно быть числом");
        }
        if (y < -5 || y > 3) {
            throw new ValidatorException("Y должен быть в [-5, 3]");
        }
    }

    private void validateR(Object s) throws ValidatorException {
        if (s == null) {
            throw new ValidatorException("Нужно выбрать R");
        }
        if (!(s instanceof String)) {
            throw new ValidatorException("Неверный формат R");
        }
        final float r;
        try {
            r = Float.parseFloat((String) s);
        } catch (NumberFormatException e) {
            throw new ValidatorException("R должно быть числом");
        }
        if (r < 1 || r > 3 || r % .5 != 0) {
            throw new ValidatorException("R должен быть в {1, 1.5, 2, 2.5, 3}");
        }
    }
}
