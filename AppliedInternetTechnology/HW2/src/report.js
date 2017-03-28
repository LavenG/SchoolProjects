//report.js
const request = require('request');
const basket = require("./basketfunc.js");
// const fs = require("fs");
// fs.readFile('tests/0021600681_gamedetail.json', 'utf8', function(err, data){
//   if(err){
//     console.log(err);
//   }else{
//     const parsedObject = JSON.parse(data);
//     const string = basket.processGameData(parsedObject);
//     console.log(string);
//   }
// });
request('http://foureyes.github.io/csci-ua.0480-spring2017-008/homework/02/0021600680_gamedetail.json', function callback(error, response, data){
  if(error){
    console.log(error);
  }else{
    const parsedObject = JSON.parse(data);
    const string = basket.processGameData(parsedObject);
    console.log(string +"\n\n");
    if(parsedObject.g.nextgid !== undefined){
      const url = "http://foureyes.github.io/csci-ua.0480-spring2017-008/homework/02/" + parsedObject.g.nextgid + "_gamedetail.json";
      request(url, callback);
    }
  }
});
