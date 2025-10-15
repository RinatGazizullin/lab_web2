package builder;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import exceptions.DataException;
import exceptions.ValidatorException;
import protocol.Request;
import validator.RequestValidator;
import validator.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс для парсинга данных клиента.
 *
 * @author rinat
 */
public class RequestBuilder implements Builder<List<Request>> {
    private static final Validator<Map<String, Object>> validator = new RequestValidator();
    private static final Gson gson = new Gson();

    /**
     * Метод для сборки запроса.
     *
     * @param data Данные для сборки
     * @return Собранные объекты
     * @throws DataException Ошибка сборки
     */
    @Override
    public List<Request> build(Map<String, Object> data) throws DataException {
        final List<Request> requests = new ArrayList<>();
        try {
            validator.validate(data);
        } catch (ValidatorException e) {
            throw new DataException(e.getMessage());
        }
        try {
            final List<Float> xs = gson.fromJson(data.get("x").toString(),
                    new TypeToken<List<Float>>() {}.getType());
            final BigDecimal y = new BigDecimal((String) data.get("y"));
            final float r = Float.parseFloat((String) data.get("r"));
            xs.forEach(x -> requests.add(new Request(x, y, r)));
        } catch (NumberFormatException e) {
            throw new DataException("Данные должны быть числами");
        } catch (JsonParseException e) {
            throw new DataException("Неверный формат чисел по оси X " + data.get("x").toString());
        }
        return requests;
    }
}
