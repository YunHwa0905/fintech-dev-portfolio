const express = require('express');
const path = require('path');
const app = express();
const http = require('http');
const server = http.createServer(app).listen(80);
const mysql = require('mysql2');
const bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));


const connection = mysql.createConnection({
    host:'localhost',
    port: 3306,
    user: 'root',
    password: '0000',
    database: 'web'
});

app.get('/test', function(req, res){
    let selectQuery = `select * from test`;
    connection.query(selectQuery,
        function(err, rows, fields){

            console.log(rows);

            res.send(rows);
        }
    )
});

app.get('/getbynumber', function(req,res){
    let selectQuery = `select * from test where id = ${req.query.id}`;

    connection.query(selectQuery,
        function(err, rows, fileds){
            console.log(rows);

            res.send(rows);
        }
    )
});

app.get('/newsWritePage', function (req, res){
    res.sendFile(path.join(__dirname, 'newsWritePage.html'));
});

app.post('/news', function(req,res){
    let insertQuery = `INSERT INTO news(title, content) VALUES('${req.body.title}', '${req.body.content}')`;
    connection.query(insertQuery, 
        function(err, rows, fields) {
            if(rows.affectedRows==1){
                res.end("입력 성공");
            }else{
                res.send("입력 실패");
            }
    })
});

app.get('/newsListPage', function (req, res){
    res.sendFile(path.join(__dirname, 'newsListPage.html'));
});

app.get('/newsList', function(req,res){
    let selectQuery = `select id, title from news WHERE deleted_at IS NULL`;

    connection.query(selectQuery,
        function(err, rows, fileds){

            res.send(rows);
        }
    )
});

app.get('/newsDetailPage', function (req, res){
    res.sendFile(path.join(__dirname, 'newsDetailPage.html'));
});

app.get('/newsDetail', function(req,res){
    let selectQuery = `select * from news where id = ${req.query.id}`;

    connection.query(selectQuery, 
        function(err, rows, fields){
            res.send(rows);
    });
});

app.get('/newsUpdatePage', function (req, res){
    res.sendFile(path.join(__dirname, 'newsUpdatePage.html'));
});

app.put('/newsUpdate', function(req,res){
    let updateQuery = `UPDATE news SET title='${req.body.title}', content='${req.body.content}' WHERE id=${req.body.id}`;

    connection.query(updateQuery,
        function(err, rows, fields){
            res.send(rows);
        }
    )
})

app.post('/newsDelete', function(req,res){
    let deleteQuery = `UPDATE news SET deleted_at = NOW() WHERE id = ${req.body.id}`;

    connection.query(deleteQuery,
        function(err, rows, fields){
            res.send(rows);
        }
    )
})

app.get('/timer', function (req, res){
    res.sendFile(path.join(__dirname, 'timer.html'));
});

app.get('/timer2', function (req, res){
    res.sendFile(path.join(__dirname, 'timer2.html'));
});

app.get('/chart', function (req, res){
    res.sendFile(path.join(__dirname, 'chart.html'));
});