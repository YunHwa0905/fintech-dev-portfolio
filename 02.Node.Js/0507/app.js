var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);
const path = require('path')

const items = [
    { name: 'item1', price: 1000 },
    { name: 'item2', price: 5000 },
    { name: 'item3', price: 10000 },
    { name: 'item4', price: 30000 },
    { name: 'item5', price: 50000 },
    { name: 'item6', price: 100000 },
    { name: 'item7', price: 500000 },
    { name: 'item8', price: 800000 },
    { name: 'item9', price: 1000000 },
    { name: 'item10', price: 3000000 },
    { name: 'item11', price: 5000000 }
];

app.get('/item', function (req, res) {
    if (req.query.num1 === undefined) {
        res.sendFile(path.join(__dirname, 'item.html'));
        return;
    }

    const money = Number(req.query.num1);
    console.log('입력 가격:', money);                                      

    let result = '구매 가능한 물품이 없습니다.';

    for (let i = items.length - 1; i >= 0; i--) {
        if (money >= items[i].price) {
            result = items[i].name;
            break;
        }
    }

    res.send(result);
});

app.get('/carPrice', function(req,res){
    res.sendFile(__dirname + '/carPrice.html');
});


app.get('/getPrice', function (req, res) {
    console.log(req.query.car, req.query.color);
    const carPrice = [2100, 1300, 1500, 3500, 3200];
    const colorPrice = [100, 120, 200, 130, 80];
    res.send(carPrice[req.query.car] + colorPrice[req.query.color]);
});

app.get('/function', function(req,res){
    res.sendFile(__dirname + '/function.html');
});

app.get('/bigNum', function(req,res){
    res.sendFile(__dirname + '/bigNum.html');
});