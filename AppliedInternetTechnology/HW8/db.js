var mongoose = require('mongoose');

var Movie = mongoose.Schema({
  title: String,
  director: String,
  year: Number
});

mongoose.model('Movie', Movie);
mongoose.connect('mongodb://localhost/hw08');
