var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);

app.get('/', function (req, res){
    res.send("hello world");
});

app.get('/request1', function (req, res){
    res.send("response1");
});

const path = require('path');
app.get('/search', function (req, res){
    res.sendFile(path.join(__dirname, 'main.html'));
});

app.get('/test', function (req, res){
    res.sendFile(path.join(__dirname, 'test.html'));
});