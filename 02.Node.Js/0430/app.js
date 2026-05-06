var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);
const path = require('path')

app.get('/', function (req, res){
    res.send("hello world");
});

app.get('/main', function (req, res){
    res.sendFile(path.join(__dirname, 'main.html'));
});

app.get('/test2', function (req, res){
    console.log(req.query);
    console.log(req.query.num1, req.query.num2);
    res.send(Number(req.query.num1)+Number(req.query.num2));
});


app.get('/mul3', function (req, res) {
    console.log(req.query);
    console.log(req.query.num1, req.query.num2, req.query.num3);

    let result = Number(req.query.num1) * Number(req.query.num2) * Number(req.query.num3);
    res.send(result.toString());
});

app.get('/item', function (req, res) {
    console.log(req.query);
    console.log(req.query.num1);

    let result = Number(req.query.num1) 
    res.send(result.toString());
});