package utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import exceptions.DataException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Класс для парсинга и проверки клиентского GSON.
 *
 * @author rinat
 */
public class DataParser {
    private static final Gson GSON = new Gson();

    /**
     * Метод для чтения тела запроса клиента.
     *
     * @param request Запрос клиента
     * @return Тело запроса
     * @throws DataException Неверный формат данных клиента
     */
    public static String getString(HttpServletRequest request) throws DataException {
        final String contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase().contains("application/json")) {
            throw new DataException("Неверный тип данных");
        }

        final StringBuilder jsonData = new StringBuilder();
        try {
            final BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            throw new DataException("Ошибка чтения тела запроса");
        }

        final String body = jsonData.toString();
        if (body.isEmpty()) {
            throw new DataException("Пустое тело запроса");
        }
        return body;
    }

    /**
     * Метод для получения MAP из JSON тела запроса.
     *
     * @param json Данные
     * @return Словарь
     * @throws DataException Неверный формат данных
     */
    public static Map<String, Object> getMap(String json) throws DataException {
        try {
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            return GSON.fromJson(json, type);
        } catch (JsonSyntaxException | IllegalStateException e) {
            throw new DataException("Неверный формат ответ");
        }
    }
}
