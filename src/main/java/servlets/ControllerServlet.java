package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import validator.RequestValidator;
import validator.Validator;
import java.io.IOException;
import java.util.Map;

/**
 * Основной сервлет для распределения запросов.
 *
 * @author rinat
 */
@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    private static final Validator<Map<String, Object>> validator = new RequestValidator();

    /**
     * Метод для обработки GET-запросов.
     *
     * @param request Запрос клиента
     * @param response Ответ сервера
     * @throws IOException Ошибка отправки данных
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.getRequestDispatcher("./index.jsp").forward(request, response);
        } catch (IOException | ServletException e) {
            sendError(response, "Не удалось проверить данные");
        }
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
            final String action = request.getParameter("action");
            if (action == null) {
                sendError(response, "Не указано действие");
            } else if (action.equals("clear")) {
                request.getRequestDispatcher("./clear").forward(request, response);
            } else if (action.equals("check")) {
                request.getRequestDispatcher("./check").forward(request, response);
            }
            sendError(response, "Неизвестное действие");
        } catch (IOException | ServletException e) {
            sendError(response, "Не удалось проверить данные");
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }
}
