const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app).listen(80);
const mysql = require('mysql2');
const axios = require('axios');
const cheerio = require('cheerio');
const ExcelJS = require('exceljs');

// JSON body 파싱 (인기도 업데이트 시 필요)
app.use(express.json());

const connection = mysql.createConnection({
    host: 'localhost',
    port: '3306',
    user: 'root',
    password: '0000',
    database: 'web'
});

connection.connect((err) => {
    if (err) {
        console.error('db연결에 실패했습니다.', err);
        return;
    }
    console.log('db연결 성공');
});

app.get('/frontquery', function (req, res) {
    res.sendFile(__dirname + '/frontquery.html');
});

app.get('/queryfromfront', function (req, res) {
    connection.query(`${req.query.q}`,
        function (err, rows, fields) {
            res.send(rows);
        }
    );
});

// ---------------------------------------------------------------
// 1) 학교 서버(KOPO 강서캠퍼스 식단정보) 크롤링 → DB 저장
//
//    실제 페이지: https://kopo.ac.kr/kangseo/content.do?menu=262
//    테이블 구조: 구분(요일) | 조식 | 중식 | 석식
//    지금은 중식(점심) 메뉴만 콤마(,)로 구분되어 있어서 그것만 파싱함.
//
//    ⚠️ 아래 셀렉터( 'table tr' )는 예시입니다.
//    실제로는 브라우저 개발자도구(F12)로 이 페이지의 <table> class명을
//    확인해서 더 구체적인 셀렉터로 바꿔주는 게 안전합니다.
//    (페이지 안에 다른 table도 있을 수 있어서, 헤더가 '구분/조식/중식/석식'
//     인 table만 골라내는 로직도 필요할 수 있음)
// ---------------------------------------------------------------
app.get('/crawl', async function (req, res) {
    try {
        const response = await axios.get('https://www.kopo.ac.kr/kangseo/content.do?menu=262');
        const $ = cheerio.load(response.data);

        const days = ['월', '화', '수', '목', '금'];
        const rows = $('table tr'); // TODO: 실제 class로 좁히기

        console.log('=== 디버그 시작 ===');
        console.log('전체 tr 개수:', rows.length);

        const tasks = [];
        let matchedRowCount = 0;

        rows.each((i, el) => {
            const tds = $(el).find('td');
            if (tds.length < 3) return; // 헤더행 등은 스킵

            const dayText = $(tds[0]).text().trim();       // 예: '월요일'
            console.log(`[행 ${i}] td 개수: ${tds.length}, 첫번째 td: "${dayText}"`);

            const day = days.find(d => dayText.includes(d));
            if (!day) return;

            matchedRowCount++;

            const lunchText = $(tds[2]).text().trim();     // 중식 열
            console.log(`  → 요일 매칭됨(${day}), 중식 텍스트: "${lunchText}"`);

            const items = lunchText.split(',').map(s => s.trim()).filter(Boolean);
            console.log('  → 파싱된 items:', items);

            items.forEach((menuName, idx) => {
                const sql = `
                    INSERT INTO weekly_menu (day, menu_name, item_order, popularity)
                    VALUES (?, ?, ?, 4)
                    ON DUPLICATE KEY UPDATE menu_name = VALUES(menu_name)
                `;
                tasks.push(new Promise((resolve, reject) => {
                    connection.query(sql, [day, menuName, idx], (err) => {
                        if (err) reject(err); else resolve();
                    });
                }));
            });
        });

        console.log('요일 매칭된 행 수:', matchedRowCount);
        console.log('저장할 항목 수(tasks):', tasks.length);
        console.log('=== 디버그 끝 ===');

        await Promise.all(tasks);
        res.send(`크롤링 및 저장 완료 (매칭행:${matchedRowCount}, 저장항목:${tasks.length})`);
    } catch (err) {
        console.error(err);
        res.status(500).send('크롤링 실패: ' + err.message);
    }
});

// ---------------------------------------------------------------
// 2) 메뉴 페이지
// ---------------------------------------------------------------
app.get('/menu', function (req, res) {
    res.sendFile(__dirname + '/menu.html');
});

// ---------------------------------------------------------------
// 3) DB에 저장된 전체 메뉴 목록 (요일/순서 순으로 정렬해서 JSON 반환)
// ---------------------------------------------------------------
app.get('/menu/list', function (req, res) {
    connection.query(
        'SELECT * FROM weekly_menu ORDER BY FIELD(day, "월","화","수","목","금"), item_order',
        (err, rows) => {
            if (err) { res.status(500).send(err.message); return; }
            res.json(rows);
        }
    );
});

// ---------------------------------------------------------------
// 4) 인기도(select 값) 수정
// ---------------------------------------------------------------
app.post('/menu/count', function (req, res) {
    const { id, popularity } = req.body;
    connection.query(
        'UPDATE weekly_menu SET popularity = ? WHERE id = ?',
        [popularity, id],
        (err) => {
            if (err) { res.status(500).send(err.message); return; }
            res.send('ok');
        }
    );
});

// ---------------------------------------------------------------
// 5) 요일별 평균 인기도 (그래프용)
// ---------------------------------------------------------------
app.get('/menu/popularity', function (req, res) {
    const sql = `
        SELECT day, AVG(popularity) AS avgPopularity
        FROM weekly_menu
        GROUP BY day
        ORDER BY FIELD(day, '월','화','수','목','금')
    `;
    connection.query(sql, (err, rows) => {
        if (err) { res.status(500).send(err.message); return; }
        res.json(rows);
    });
});

// ---------------------------------------------------------------
// 6) 엑셀 다운로드 (요일별 메뉴명 | 카운트 두 칸씩 + 마지막 평균행)
//    스크린샷의 엑셀 레이아웃과 동일한 형태
// ---------------------------------------------------------------
app.get('/menu/download', function (req, res) {
    connection.query(
        'SELECT * FROM weekly_menu ORDER BY FIELD(day, "월","화","수","목","금"), item_order',
        async (err, rows) => {
            if (err) { res.status(500).send(err.message); return; }

            const days = ['월', '화', '수', '목', '금'];
            const grouped = {};
            days.forEach(d => grouped[d] = []);
            rows.forEach(r => grouped[r.day].push(r));

            const workbook = new ExcelJS.Workbook();
            const sheet = workbook.addWorksheet('menu');

            // 헤더: 요일마다 (메뉴명 칸, 카운트 칸) 두 개씩
            const headerRow = [];
            days.forEach(d => headerRow.push(d, ''));
            sheet.addRow(headerRow);

            const maxLen = Math.max(...days.map(d => grouped[d].length));
            for (let i = 0; i < maxLen; i++) {
                const row = [];
                days.forEach(d => {
                    const item = grouped[d][i];
                    row.push(item ? item.menu_name : '', item ? item.popularity : '');
                });
                sheet.addRow(row);
            }

            // 평균 행
            const avgRow = [];
            days.forEach(d => {
                const list = grouped[d];
                const avg = list.length
                    ? (list.reduce((s, x) => s + x.popularity, 0) / list.length).toFixed(2)
                    : '';
                avgRow.push('평균', avg);
            });
            sheet.addRow(avgRow);

            res.setHeader('Content-Disposition', 'attachment; filename=menu.xlsx');
            res.setHeader(
                'Content-Type',
                'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            );
            await workbook.xlsx.write(res);
            res.end();
        }
    );
});