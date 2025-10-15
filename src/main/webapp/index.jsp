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
        <section class="input-section">
            <h2>Введите параметры</h2>
            <form id="input-form">
                <table class="input-table">
                    <tr>
                        <td class="name"><label>Координата X:</label></td>
                        <td class="x-buttons" id="x-buttons">
                            <button type="button" name="x" value="-5">-5</button>
                            <button type="button" name="x" value="-4">-4</button>
                            <button type="button" name="x" value="-3">-3</button>
                            <button type="button" name="x" value="-2">-2</button>
                            <button type="button" name="x" value="-1">-1</button>
                            <button type="button" name="x" value="0">0</button>
                            <button type="button" name="x" value="1">1</button>
                            <button type="button" name="x" value="2">2</button>
                            <button type="button" name="x" value="3">3</button>
                        </td>
                    </tr>
                    <tr>
                        <td class="name"><label for="y-number">Координата Y:</label></td>
                        <td>
                            <input type="text" id="y-number" name="y" placeholder="От -5 до 3" required>
                        </td>
                    </tr>
                    <tr>
                        <td><label>Радиус R:</label></td>
                        <td class="r-buttons" id="r-buttons">
                            <label><input type="radio" name="r" value="1"> 1</label>
                            <label><input type="radio" name="r" value="1.5"> 1.5</label>
                            <label><input type="radio" name="r" value="2" checked> 2</label>
                            <label><input type="radio" name="r" value="2.5"> 2.5</label>
                            <label><input type="radio" name="r" value="3"> 3</label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <button type="submit" class="submit-btn">Проверить попадание</button>
                        </td>
                    </tr>
                </table>
                <div class="error-message" id="global-error" hidden>
                </div>
                <div class="good-message" id="global-result" hidden>
                </div>
            </form>
        </section>

        <section class="plot-section">
            <h2>График области</h2>
            <div id="plot-container">
                <canvas id="plot"></canvas>
            </div>
        </section>

        <section class="results-section">
            <button id="clear-results-btn" class="clear-btn">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                    <line x1="18" y1="6" x2="6" y2="18"></line>
                    <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
            </button>
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
    <div class="footer-content">
        <h3>Спасибо за проверку этой страницы!</h3>
    </div>
</footer>
</body>
</html>
