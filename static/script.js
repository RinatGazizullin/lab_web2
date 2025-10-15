"use strict";

const state = {
    x: [],
    y: "0",
    r: "2",
};

const svg = document.getElementById('plot');
const errorElement = document.getElementById("global-error");
const goodResult = document.getElementById("global-result");
const clearBtn = document.getElementById("clear-results-btn");

const goodX = new Set(["-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3"]);
const goodR = new Set([1.0, 1.5, 2.0, 2.5, 3.0])

const canvas = document.getElementById("plot");
const ctx = canvas.getContext("2d");
canvas.width = canvas.offsetWidth * 4;
canvas.height = canvas.offsetHeight * 4;

const centerX = canvas.width / 2;
const centerY = canvas.height / 2;

const validate = () => {
    console.log(state.r);
    if (!goodR.has(parseFloat(state.r))) {
        showError(`R должно быть в [${[...goodR].join(", ")}]`);
        return false;
    }

    if (isNaN(state.y) || !isFloatString(state.y) || parseFloat(state.y) < -5 || parseFloat(state.y) > 3) {
        showError("Y должен быть от -5 до 3");
        return false;
    }

    if (state.x.length === 0) {
        showError("Нужно выбрать хотя бы одно X");
        return false;
    }
    let result = true;
    state.x.forEach(x => {
            if (isNaN(x) || !goodX.has(x)) {
                showError(`X должно быть в [${[...goodX].join(", ")}]`);
                result = false;
            }
        }
    )
    if (!result) {
        return result;
    }
    hideError();
    return true;
};

svg.addEventListener('click', async function (e) {
    e.preventDefault();

    const rect = svg.getBoundingClientRect();
    const x = (e.clientX - rect.left) * 4;
    const y = (e.clientY - rect.top) * 4;

    console.log(x);
    console.log(y);

    const scale = Math.min(canvas.width, canvas.height) / 4;
    const r = parseFloat(state.r)

    const logicalX = (x - centerX) / scale * r;
    const logicalY = (-y + centerY) / scale * r;

    const data = {
        x: [`${logicalX}`],
        y: `${logicalY}`,
        r: `${r}`
    }

    console.log(logicalX);
    console.log(logicalY);

    const response = await fetch('./controller?action=check', {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })

    saveToLocalStorage({x: logicalX, y: logicalY, r: r});

    if (!response.ok) {
        let dataText = response.text();
        showError("Ошибка сервера");
        console.log(dataText);
    } else {
        window.location.href = "result.jsp";
    }
});

document.querySelectorAll('.x-buttons button').forEach(button => {
    button.addEventListener('click', function() {
        const value = (this.getAttribute('value'));
        if (this.classList.contains('selected')) {
            state.x = state.x.filter(r => r !== value);
            this.classList.remove('selected');
        } else {
            this.classList.add('selected');
            state.x.push(value);
        }
    });
});

document.querySelectorAll('input[name="r"]').forEach(radio => {
    radio.addEventListener('change', function() {
        if (this.checked) {
            state.r = this.value;
        }
        console.log("r changed");
        drawPoints();
    });
});

function drawPoints() {
    clearPlot();
    const saved = JSON.parse(localStorage.getItem("results") || "[]");
    saved.forEach(entry => {
        if (entry.r == state.r) {
            drawPoint(entry.x, entry.y, entry.r);
        }
    });
}

function isFloatString(str) {
    if (typeof str !== 'string' && !(str instanceof String)) return false;
    const trimmed = str.trim();
    if (trimmed === '') return false;
    const num = Number(trimmed);
    return !isNaN(num) && isFinite(num);
}

function hideResult() {
    goodResult.hidden = true;
}

function showError(msg) {
    hideResult();
    errorElement.hidden = false;
    errorElement.innerText = msg;
}

function hideError() {
    errorElement.hidden = true;
}

document.getElementById("y-number").addEventListener("input", (e) => {
    state.y = e.target.value.trim().replace(/,/g, '.');
});

clearBtn.addEventListener("click", async () => {
    try {
        const response = await fetch('./controller?action=clear', {
            method: "POST"
        });

        if (!response.ok) {
            showError("Ошибка сервера");
        } else {
            localStorage.clear();
            location.reload();
        }
    } catch (err) {
        showError("Не удалось очистить");
    }
});

document.getElementById("input-form").addEventListener("submit", async function (e) {
    e.preventDefault();

    if (!validate()) return;

    const data = {
        x: state.x,
        y: state.y,
        r: state.r
    }

    const response = await fetch('./controller?action=check', {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })

    saveToLocalStorage(data);

    if (!response.ok) {
        let dataText = await response.text();
        showError("Ошибка сервера");
        console.log(dataText);
    } else {
        window.location.href = "result.jsp";
    }
});

function drawArrow(x1, y1, x2, y2, k1, k2, arrowSize = 60) {
    const angle = Math.atan2(y2 - y1, x2 - x1);

    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2 + k1, y2 + k2);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(x2, y2);
    ctx.lineTo(
        x2 - arrowSize * Math.cos(angle - Math.PI / 6),
        y2 - arrowSize * Math.sin(angle - Math.PI / 6)
    );
    ctx.lineTo(
        x2 - arrowSize * Math.cos(angle + Math.PI / 6),
        y2 - arrowSize * Math.sin(angle + Math.PI / 6)
    );
    ctx.closePath();
    ctx.fillStyle = 'rgb(190, 195, 199)';
    ctx.fill();
}

function drawPlot() {
    const scale = Math.min(canvas.width, canvas.height) / 4;
    ctx.beginPath();
    ctx.moveTo(61, centerY);
    ctx.lineTo(canvas.width - 61, centerY);
    ctx.strokeStyle = 'rgb(190, 195, 199)';
    ctx.lineWidth = 8;
    ctx.stroke();
    drawArrow(
        canvas.width - 61, centerY,
        canvas.width - 30, centerY,
        -11, 0
    );

    ctx.beginPath();
    ctx.moveTo(centerX, 61);
    ctx.lineTo(centerX, canvas.height - 61);
    ctx.strokeStyle = 'rgb(190, 195, 199)';
    ctx.lineWidth = 8;
    ctx.stroke();
    drawArrow(
        centerX, 61,
        centerX, 31,
        0, 11
    );

    ctx.beginPath();
    ctx.arc(
        centerX,
        centerY,
        scale * .5,
        Math.PI,
        Math.PI * 3 / 2,
    );
    ctx.lineTo(centerX, centerY);

    ctx.lineTo(centerX + scale, centerY);
    ctx.lineTo(centerX + scale, centerY + scale * .5);
    ctx.lineTo(centerX, centerY + scale * .5);
    ctx.lineTo(centerX, centerY + scale);
    ctx.lineTo(centerX - scale * .5, centerY);
    ctx.closePath();

    ctx.fillStyle = 'rgba(52, 152, 219, 0.6)';
    ctx.fill()

    ctx.strokeStyle = '#3498db';
    ctx.lineWidth = 4;
    ctx.stroke();

    ctx.font = '4rem Arial bold';
    ctx.fillStyle = '#2c3e50';

    ctx.fillText('x', canvas.width - 121, centerY - 30);
    ctx.fillText('y', centerX + 31, 121);

    ctx.fillText('-R', centerX - scale - 10, centerY + 61);
    ctx.fillText('-R/2', centerX - scale * 0.5 - 15, centerY + 60);
    ctx.fillText('R/2', centerX + scale * 0.5 - 5, centerY + 60);
    ctx.fillText('R', centerX + scale - 5, centerY + 60);

    ctx.fillText('-R', centerX + 10, centerY + scale + 10);
    ctx.fillText('-R/2', centerX + 10, centerY + scale * 0.5 + 10);
    ctx.fillText('R/2', centerX + 10, centerY - scale * 0.5 - 5);
    ctx.fillText('R', centerX + 10, centerY - scale - 5);
}

function drawPoint(x, y, r) {
    const scale = Math.min(canvas.width, canvas.height) / 4;
    const px = centerX + (x * scale) / r;
    const py = centerY - (y * scale) / r;
    ctx.beginPath();
    ctx.arc(px, py, 12, 0, 2 * Math.PI);
    ctx.fillStyle = 'green';
    ctx.fill();
    ctx.strokeStyle = 'darkgreen';
    ctx.lineWidth = 4;
    ctx.stroke();
}

function saveToLocalStorage(data) {
    const saved = JSON.parse(localStorage.getItem("results") || "[]");
    saved.push({
        x: data.x,
        y: data.y,
        r: data.r
    });
    localStorage.setItem("results", JSON.stringify(saved));
}

function clearPlot() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawPlot();
}

window.addEventListener("load", () => {
    clearPlot();
    drawPoints();
});
