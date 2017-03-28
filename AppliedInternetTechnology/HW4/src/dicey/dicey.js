// dicey.js
const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const fs = require('fs');
const path = require('path');
const publicPath = path.resolve(__dirname, "public");

app.use(express.static(publicPath));
app.set('view engine', 'hbs');

app.use(bodyParser.urlencoded({ extended: false}));
//Set the default path
app.use(function(req, res, next) {
  console.log(req.method, req.path);
  next();
});
//redirect for the default path
app.get('/', function(req, res){
  res.redirect('dice');
});
//redirect for the default path
app.get('/about', function(req, res){
  res.render('aboutTemplate');
});


let numOfWords ='';
let separator = '';

//keep track of the seperators
const separators = {
  "space": " ",
  "star": "*",
  "dash": "-",
  "comma": ",",
  "none": ""
};

//read in the worllist synchronuously
const wordlist = fs.readFileSync("diceware.wordlist.txt").toString();
const wordListSplit = wordlist.split("\n");
//make an object that contains all the words
let words = {};
for(let i = 0; i<wordListSplit.length; i++){
  const currentwordsplit= wordListSplit[i].split("\t");
  words[parseInt(currentwordsplit[0])] = currentwordsplit[1];
}

//redirect for the default path
app.get('/dice', function(req, res){

  //handle the form submissions
  if(req.query !== undefined){
    numOfWords = req.query.numWords;
    if(separators.hasOwnProperty(req.query.glue)){
      separator = separators[req.query.glue];
    }else{
      separator = "space";
    }
  }

  let password = '';
  let records = [];

  //generate the password
  for(let i =0; i< parseInt(numOfWords); i++){
    let currentNum = '';
    for(let j=0; j< 5; j++){
      const rand = Math.floor(Math.random()*6)+1;
      currentNum+= rand;
    }
    records.push({number: currentNum, word: words[currentNum]});
    if(i !== parseInt(numOfWords)-1){
      password += words[currentNum]+separator;
    }else{
      password += words[currentNum];
    }

  }

  //send in the result
  res.render('diceTemplate', {'numOfWords' :numOfWords, 'seperator': separator, 'records': records, 'password': password});
});

//listen on the default port 8080
app.listen(8080);
console.log("started server on port 8080");
