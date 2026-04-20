var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(81);
const path = require('path')

app.get('/', function (req, res){
    res.send("hello world");
});

app.get('/search', function (req, res){
    res.sendFile(path.join(__dirname, 'main.html'));
});

app.get('/test', function (req, res){
    res.sendFile(path.join(__dirname, 'test.html'));
});

app.get('/practice', function (req, res){
    res.sendFile(path.join(__dirname, 'practice.html'));
});

app.get('/join', function (req, res){
    res.sendFile(path.join(__dirname, 'join.html'));
});

app.get('/write', function (req, res){
    res.sendFile(path.join(__dirname, 'write.html'));
});

app.get('/table', function (req, res){
    res.sendFile(path.join(__dirname, 'table.html'));
});

app.get('/resume', function (req, res){
    res.sendFile(path.join(__dirname, 'resume.html'));
});