package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Сервлет для очистки памяти.
 *
 * @author rinat
 */
@WebServlet("/clear")
public class ClearServlet extends HttpServlet {
    /**
     * Метод для обработки POST-запросов.
     *
     * @param request Запрос клиента
     * @param response Ответ сервера
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        getServletContext().removeAttribute("calculator_results");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
