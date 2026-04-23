var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);
const path = require('path')

app.get('/', function (req, res){
    res.send("hello world");
});

app.get('/array', function (req, res){
    res.sendFile(path.join(__dirname, 'array.html'));
});

app.get('/radio', function (req, res){
    res.sendFile(path.join(__dirname, 'radio.html'));
});

app.get('/radio2', function (req, res){
    res.sendFile(path.join(__dirname, 'radio2.html'));
});

app.get('/test', function (req, res){
    console.log(req.query);
    console.log(req.query.value1, req.query.value2, req.query.value3);
    res.send("hello world");
});

app.get('/test2', function (req, res){
    console.log(req.query);
    console.log(req.query.num1, req.query.num2);
    res.send(Number(req.query.num1)+Number(req.query.num2));
});
