const express = require('express');
const path = require('path');
const app = express();
const http = require('http');
const server = http.createServer(app).listen(80);
const mysql = require('mysql2');

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

app.get('/insert', function(req,res){
    let insertQuery = `INSERT INTO news(title, content) VALUES('${req.query.title}', '${req.query.content}')`;
    connection.query(insertQuery, function(err, result) {
        if (err) {
            console.log(err);
            return res.send({ error: err });
        }

        const selectQuery = 'SELECT * FROM news WHERE id = ?';
        connection.query(selectQuery, [result.insertId], function(err, rows) {
            if (err) {
                return res.send({ error: err });
            }

            console.log(rows[0]);
            res.send(rows[0]);
        });
    });
});