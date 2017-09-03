// foodz.js

const express = require('express');
const app = express();
const path = require('path');
const publicPath = path.resolve(__dirname, "public");
const bodyParser = require('body-parser');

//set default available meals
let availableMeals =[];
availableMeals.push({name: "chocoramen", description: "ramen noodles in a chocolate almond milk broth", filterCategory: "breakfast"});
availableMeals.push({name: "lycheezy", description: "cheese pizza with lychee on top", filterCategory: "anytime"});
availableMeals.push({name: "crazy cookie", description: "a 1 foot diameter cookie", filterCategory: "dinner"});

app.use(express.static(publicPath));
//set layout for handlebars
app.set('view engine', 'hbs');

app.use(bodyParser.urlencoded({ extended: false}));
app.use(function(req, res, next) {
  console.log(req.method, req.path);
  next();
});

//handles the default path
app.get('/', function(req, res){

  let filterCategory = "";
  let filteredMeals = [];
  //handle the query which consists of filtering the available meals, change the
  //layout accordingly
  if(req.query!== {}){
    filterCategory = req.query.filterCategory;
    for(let i = 0; i < availableMeals.length; i++){
      if(availableMeals[i].filterCategory === filterCategory){
        filteredMeals.push(availableMeals[i]);
      }
    }
  }
  if(filterCategory === undefined){
    res.render('home.hbs', {'availableMeals':availableMeals});
  }else{
    const str = "(" + filterCategory + " only)";
    res.render('home.hbs', {'availableMeals':filteredMeals, "str":str});
  }

});
//Handle addition of new meals
app.post('/', function(req, res){
  availableMeals.push(req.body);
  res.redirect('/');
});

app.listen(8080);
