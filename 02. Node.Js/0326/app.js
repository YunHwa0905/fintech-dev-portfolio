var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);
const path = require('path')

app.get('/', function (req, res){
    res.send("hello world");
});

app.get('/span', function (req, res){
    res.sendFile(path.join(__dirname, 'span.html'));
});

app.get('/for', function (req, res){
    res.sendFile(path.join(__dirname, 'for.html'));
});

app.get('/multi', function (req, res){
    res.sendFile(path.join(__dirname, 'multi.html'));
});

app.get('/button', function (req, res){
    res.sendFile(path.join(__dirname, 'button.html'));
});