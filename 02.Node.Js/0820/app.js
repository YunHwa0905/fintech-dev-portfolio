const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app).listen(80);
const mysql = require('mysql2');

const connection = mysql.createConnection({
    host : 'localhost',
    port : '3307',
    user : 'root',
    password : '1234',
    database : 'web'
});

connection.connect((err) => {
    if(err){
        console.error('db연결에 실패했습니다.', err);
        return;
    }
    console.log('db연결 성공');
})

app.get('/test', (req,res) => {
    res.sendFile(__dirname + '/test.html')
})
app.get('/test2', (req,res) => {
    res.sendFile(__dirname + '/test2.html')
})
app.get('/query', (req,res) => {
    query = req.query.query
    console.log(query)
    
    connection.query(query,(err,rows,fields) =>{
        if(err) throw err;
        res.send(rows)
    })
})
