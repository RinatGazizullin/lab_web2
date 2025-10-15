<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Лабораторная работа №2</title>
    <link rel="stylesheet" href="style.css">
    <script defer src="script.js"></script>
</head>
<body>

<header id="main-header">
    <div class="header-container">
        <div class="header-content">
            <h1>Лабораторная работа по Веб-программированию</h1>
            <div class="student-info">
                <div class="info-item">
                    <span class="label">ФИО:</span>
                    <span class="value">Газизуллин Ринат Ришатович</span>
                </div>
                <div class="info-item">
                    <span class="label">Группа:</span>
                    <span class="value">P3216</span>
                </div>
                <div class="info-item">
                    <span class="label">Вариант:</span>
                    <span class="value">321664</span>
                </div>
            </div>
        </div>
    </div>
</header>

<main>
    <div class="container">
        <section class="results-section">
            <h2>История результатов</h2>
            <div id="results-container">
                <table id="results-table">
                    <thead>
                    <tr>
                        <th>X</th>
                        <th>Y</th>
                        <th>R</th>
                        <th>Результат</th>
                        <th>Время запроса</th>
                        <th>Время работы</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="result" items="${applicationScope.calculator_results}">
                        <tr>
                            <td>${result.x}</td>
                            <td>${result.y}</td>
                            <td>${result.r}</td>
                            <td class="${result.hit ? 'hit' : 'miss'}">${result.hit ? "Попадание" : "Промах"}</td>
                            <td>${result.timestamp}</td>
                            <td>${result.time} мс</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </div>
</main>

<footer>
    <div class="footer-content-back">
        <div class="border">
            <a href="index.jsp"><button type="submit" class="submit-btn">Вернуться на главную страницу</button></a>
        </div>
    </div>
</footer>
</body>
</html>
