package servlets;

import builder.Builder;
import builder.RequestBuilder;
import exceptions.DataException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import protocol.Request;
import utils.Checker;
import utils.DataParser;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Сервлет для определения попадания.
 *
 * @author rinat
 */
@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {
    private static final Builder<List<Request>> builder = new RequestBuilder();
    private static final Checker checker = new Checker();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Метод для обработки GET-запросов.
     *
     * @param request Запрос клиента
     * @param response Ответ сервера
     * @throws IOException Ошибка отправки данных
     * @throws ServletException Неверный формат данных
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.getRequestDispatcher("./result.jsp").forward(request, response);
    }

    /**
     * Метод для обработки POST-запросов.
     *
     * @param request Запрос клиента
     * @param response Ответ сервера
     * @throws IOException Ошибка отправки данных
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            check(request, response);
            request.getRequestDispatcher("./result.jsp").forward(request, response);
        } catch (DataException e) {
            sendError(response, e.getMessage());
        } catch (IOException | ServletException e) {
            sendError(response, "Не удалось проверить данные");
        }
    }

    private void check(HttpServletRequest request, HttpServletResponse response)
            throws DataException {
        final String body = DataParser.getString(request);
        final Map<String, Object> data = DataParser.getMap(body);

        final List<Request> requests = builder.build(data);
        final List<ResultEntry> results = new CopyOnWriteArrayList<>();
        for (Request dataRequest : requests) {
            Instant start = Instant.now();
            boolean isHit = checker.isHit(dataRequest);
            Instant end = Instant.now();
            long time = end.toEpochMilli() - start.toEpochMilli();

            results.add(new ResultEntry(dataRequest.x(), dataRequest.y().doubleValue(), dataRequest.r(),
                    isHit, formatter.format(start.atZone(ZoneId.systemDefault())), time));
        }

        List<ResultEntry> allResults = getResultEntries();
        allResults.addAll(results);
        request.setAttribute("current_results", results);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private List<ResultEntry> getResultEntries() {
        ServletContext context = getServletContext();
        final String resToken = "calculator_results";
        synchronized (this) {
            @SuppressWarnings("unchecked")
            List<ResultEntry> allResults = (List<ResultEntry>) context.getAttribute(resToken);
            if (allResults == null) {
                allResults = new CopyOnWriteArrayList<>();
                context.setAttribute(resToken, allResults);
            }
        }
        @SuppressWarnings("unchecked")
        List<ResultEntry> allResults = (List<ResultEntry>) context.getAttribute(resToken);
        return allResults;
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    /**
     * Класс-данные клиентов
     *
     * @param x Координата X
     * @param y Координата Y
     * @param r Коэффициент
     * @param hit Проверка попадания
     * @param timestamp Время начала обработки
     * @param time Время обработки запроса
     */
    public record ResultEntry(double x, double y, double r, boolean hit, String timestamp, long time) {
        public double getX() { return x; }
        public double getY() { return y; }
        public double getR() { return r; }
        public boolean isHit() { return hit; }
        public String getTimestamp() { return timestamp; }
        public long getTime() { return time; }
    }
}
