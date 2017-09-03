// app.js

// Use __dirname to construct absolute paths for:

// 1. express-static
// 2. hbs views

// (the instructions have details on how to do this)

// LISTEN ON PORT 3000

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
const Link = mongoose.model('Link');
//const Comment = mongoose.model('Comment');
//hbs setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');

//Get for the main page, renders the template for the main page
app.get('/', function(req, res){
  Link.find({}, function(err, links){
    if(err){
			console.log(err);
		}
    res.render('mainTemplate', {links:links});
  });

});

//Handles the post form for new link creations
app.post('/magicalLinkCreation', function(req, res){
  const link1 = new Link({
    url: req.body.url,
    title: req.body.title,
  });
  link1.save(function(err){
    if(err){
      res.send(err);
    }else{
      res.redirect('/');
    }
  });
});

//Handles the get for singular comment pages
app.get('/:slug', function(req, res){
  Link.findOne({slug: req.params.slug}, function(err, link){
    if(req.session.lastComment === undefined){
      res.render('commentPage', {link:link, lastComment: ""});
    }else{
      res.render('commentPage', {link:link, lastComment: "(the last comment you made was: " + req.session.lastComment +")"});
    }

  });
});

//Handles the post form in order to generate new comments
app.post('/singularComment', function(req, res){
	//Registers the last comment made by the user
  req.session.lastComment = req.body.comment;
  Link.findOneAndUpdate({slug: req.body.slug}, {$push: {comments: {user:req.body.name + " says:", text:req.body.comment}}}, function(err) {
		if(err){
			console.log(err);
		}
    res.redirect(req.body.slug);
  });
});
//listen on port 3000
app.listen(3000);
