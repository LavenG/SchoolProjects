var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Movie = mongoose.model('Movie');

//Main directory
router.get('/', function(req, res) {
  res.redirect('/movies');
});

router.get('/movies', function(req, res) {
  var movieFilter = {},
    searchExists = false;

  if(req.query.director) {
    movieFilter.director = req.query.director;
    searchExists = true;
  }

  Movie.find(movieFilter, function(err, movies, count) {
    res.render('movies', {'movies': movies, searchExists: searchExists, director: req.query.director });
  });
});

//writes JSON to the response 
router.get('/api/movies', function(req,res){
    //if there is no query string return a list of all movies in the database
    if(req.query.director === undefined || req.query.director === ''){
      //Return all the movies
      Movie.find({}, function(err, movies, count){
        res.json(movies.map(function(element){
          return {
            'title': element.title,
            'director': element.director,
            'year': element.year
          }
        }));
      });
    //if there is a query string parameter for director, it should filter the
    //list based on director
    }else{
      //Return movies with a matching director
      Movie.find({'director':req.query.director}, function(err, movies, count){
        res.json(movies.map(function(element){
          return {
            'title': element.title,
            'director': element.director,
            'year': element.year
          }
        }));
      });
    }
});

//Creates a movie sends it in as a JSON
router.post('/api/movies/create', function(req, res){
  const mov1 = new Movie({
    title: req.body.title,
    director: req.body.director,
    year: parseInt(req.body.year)
  });
  mov1.save((err, movie)=>{
    if(err){
      console.log(err);
      res.json(err);
    } else{
      res.json(movie);
    }
  });
});



module.exports = router;
