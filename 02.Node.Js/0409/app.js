var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);
const path = require('path')

app.get('/', function (req, res){
    res.send("hello world");
});

app.get('/reply', function (req, res){
    res.sendFile(path.join(__dirname, 'reply.html'));
});

app.get('/reply2', function (req, res){
    res.sendFile(path.join(__dirname, 'reply2.html'));
});

app.get('/array', function (req, res){
    res.sendFile(path.join(__dirname, 'array.html'));
});