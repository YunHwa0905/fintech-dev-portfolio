const express = require('express');
const path = require('path');
const app = express();
const http = require('http');
const server = http.createServer(app).listen(80);
const mysql = require('mysql2');
const bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));
const axios = require('axios');
const cheerio = require('cheerio');
const request = require('request');


const connection = mysql.createConnection({
    host:'localhost',
    port: 3306,
    user: 'root',
    password: '0000',
    database: 'web'
});

app.get('/chart', function (req, res){
    res.sendFile(path.join(__dirname, 'chart.html'));
});

app.get('/chart1', function (req, res){
    res.sendFile(path.join(__dirname, 'chart1.html'));
});

app.get('/stock', async (req, res) => {
    const { code } = req.query;
    
    const response = await axios.get(
        `https://polling.finance.naver.com/api/realtime?query=SERVICE_ITEM:${code}`,
        { 
            headers: { 'User-Agent': 'Mozilla/5.0' },
            responseType: 'text'  // ← 이거 추가!
        }
    );
    
    const raw = response.data;
    const json = JSON.parse(raw.replace(/^[^(]+\(/, '').replace(/\);?$/, ''));
    
    const price = json.result.areas[0].datas[0].nv;
    
    res.json({ price });
});

app.get('/stockprice', function (req, res){
    res.sendFile(path.join(__dirname, '/stockprice.html'));
});

app.get('/stockFromNaver', function(req, res){
    request('https://polling.finance.naver.com/api/realtime?query=SERVICE_ITEM:005930|SERVICE_RECENT_ITEM:005930,000270&_callback=window.__lindo2_callback._', 
        function(error, response, body) {
            res.send(body);
        });
});

app.get('/dbstockpage', function (req, res){
    res.sendFile(path.join(__dirname, '/dbstockpage.html'));
});

app.post('/dbstock', function(req,res){
    let selectQuery = `INSERT INTO stock(price, created_at) VALUES('${req.body.price}', NOW())`;

    connection.query(selectQuery,
        function(err, rows, fileds){

            res.send(rows);
        }
    )
});

// 서버 - 최근 10개 시간순으로
app.get('/getstock', function(req, res){
    connection.query(
        `SELECT * FROM (SELECT price, created_at FROM stock ORDER BY created_at DESC LIMIT 10) sub ORDER BY created_at ASC`,
        function(err, rows) {
            res.send(rows);
        }
    );
});