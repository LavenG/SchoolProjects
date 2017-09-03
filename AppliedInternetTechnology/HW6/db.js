const mongoose = require('mongoose');

//Define the schema 
const User = new mongoose.Schema({
  username: {type: String, unique:true},
  password: String
});

//define models
mongoose.model('User', User);

//connect
mongoose.connect('mongodb://localhost/hw06');
