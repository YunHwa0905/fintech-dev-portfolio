var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);
const path = require('path')

app.get('/', function (req, res){
    res.send("hello world");
});

app.get('/req', function (req, res){
    res.send("res");
});

app.get('/main', function (req, res){
    res.sendFile(path.join(__dirname, 'main.html'));
});

app.get('/input', function (req, res){
    res.sendFile(path.join(__dirname, 'input.html'));
});

app.get('/login', function (req, res){
    res.sendFile(path.join(__dirname, 'login.html'));
});

app.get('/type', function (req, res){
    res.sendFile(path.join(__dirname, 'type.html'));
});

console.log("back");