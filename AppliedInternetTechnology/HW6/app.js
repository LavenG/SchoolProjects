// a taste of login! - express app demonstrating registration and login

//Set up express, app and router
const express = require('express');
const app = express();
//const router = express.Router();

//Set up body parser to access data from post
const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended: false}));

//Set up for static file
const path = require('path');
app.use(express.static(path.join(__dirname, "public")));

//Set up for session management
const session = require('express-session');

//Setting up the options for session management
const sessionOptions = {
	secret: 'secret cookie thang',
	resave: true,
	saveUninitialized: true
};

//activating session management
app.use(session(sessionOptions));

require('./db');
//Set up mongoose
const mongoose = require('mongoose');
const User = mongoose.model('User');
//hbs setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');

//require bcrypt
const bcrypt = require('bcrypt');

//renders the home page
app.get('/', function(req, res){
  if(req.session.username){
    res.render("home", {message: "Currently logged in as: " + req.session.username});
  }else{
    res.render("home");
  }

});

app.get("/register", function(req, res){
  res.render("register");
});

app.post('/register', function(req, res){
  const password = req.body.password;
  const user = req.body.username;
  const passwordErrMsg = "The password you use must be at least 8 characters, please try again";
  const userErrMsg = "This user name already exists, please pick another one";

  //Make sure that the password entered is 8 characters or longer
  if(password.length < 8){
    res.render('register', {errorMessage:passwordErrMsg});
  }
  //Check if the username entered already exists
  User.findOne({'username':user}, (err, result)=>{
    //If it does redirect to the registration page with an error message
    if(result){
      res.render('register', {errorMessage:userErrMsg});
    //otherwise register the new user into the data base
    }else{
      //hash the password
      bcrypt.hash(password, 10, function(err, hash){
        if(err){
          console.log(err);
        }
        const newUser = new User({
          username : user,
          password : hash,
        });
        newUser.save(function(err){
          if(err){
            res.send(err);
          }else{
            // assuming that user is the user object just saved to the database
            req.session.regenerate((err) => {
              if (!err) {
                console.log("here");
                req.session.username = newUser.username;
                res.redirect('/');
              } else {
                console.log('error');
                res.send('an error occurred, please see the server logs for more information');
              }
            });
          }
        });
      });
    }
  });
});

//render the login page
app.get('/login', function(req, res){
  res.render('login');
});

//Handles logging in
app.post('/login', function(req, res){
	//checking if there is a match for logging in
  User.findOne({username: req.body.username}, (err, user) => {
      if (err) {
        console.log(err);
      }else if(!user){
        res.render('login', {errorMessage: "The user name you entered does not exists, please try again"});
      }else{
				//making sure the password matches
        bcrypt.compare(req.body.password, user.password, (err, passwordMatch)=>{
          if(err){
            console.log(err);
          }
          if(passwordMatch){
            req.session.regenerate((err) => {
              if (!err) {
                req.session.username = user.username;
                res.redirect('/');
              }else{
                console.log('error');
                res.send('an error occurred, please see the server logs for more information');
              }
            });
          }else{
            res.render('login', {errorMessage: "The password you entered does not match, please try again"});
          }
        });
      }
  });
});

//Allowing to see the restricted page if a user is logged in
app.get("/restricted", function(req, res){
  if(req.session.username){
    res.render('restricted');
  }else{
    res.render('login', {errorMessage: "You were not logged in so you have been redirected to the login page"});
  }
});
//listen on port 3000
app.listen(3000);
