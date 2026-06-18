var express = require('express');
var http = require('http');
var app = express();
var server = http.createServer(app).listen(80);
const path = require('path')


app.get('/mid1', function (req, res){
    res.sendFile(path.join(__dirname, 'mid1.html'));
});

app.get('/mid2', function (req, res){
    res.sendFile(path.join(__dirname, 'mid2.html'));
});

app.get('/mid3', function (req, res){
    res.sendFile(path.join(__dirname, 'mid3.html'));
});

app.get('/getBMI', function (req, res){
    const cm = parseFloat(req.query.cm);
    const kg = parseFloat(req.query.kg);

     console.log(cm, kg);

    const m = cm / 100;
    const bmi = Math.round((kg / (m * m)) * 10) / 10;

    let result;
    if (bmi < 20){
        result = '저체중';
    }else if (bmi < 25){
        result = '정상';
    }else if (bmi < 30){
        result = '과체중';
    }else {
        result = '비만';
    }

    res.send(`BMI: ${bmi} (${result})`);

});

app.get('/mid4', function (req, res){
    res.sendFile(path.join(__dirname, 'mid4.html'));
});

const score = [];

app.get('/getScore', function (req, res){
    const ko = Number(req.query.ko);
    const en = Number(req.query.en);
    const ma = Number(req.query.ma);

    console.log(ko, en, ma);
    
    const total = ko + (en * 2) + (ma * 3);

    const scores = [
        { name: '국어', score: ko },
        { name: '영어', score: en },
        { name: '수학', score: ma }
    ];

    
    score.push(total);
    
    
    score.sort().reverse();
    console.log(score);

    const rank = score.indexOf(total)+1;
    res.send(`석차: ${rank}등`);
});