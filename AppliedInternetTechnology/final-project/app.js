//app.js

//Set up express, app and router
const express = require('express');
const app = express();

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

//Set up middleware
const cookieParser = require('cookie-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());

//Setting up the routers
const routes = require('./routes/index');
const users = require('./routes/users');
const userFunctionalities = require('./routes/userFunctionalities');
const adminFunctionalities = require('./routes/adminFunctionalities');

//activating session management
app.use(session(sessionOptions));

//Set up passport
const passport = require('passport');
const passportLocal = require('passport-local').Strategy;
app.use(passport.initialize());
app.use(passport.session());

//Set up flash for messages
const flash = require('connect-flash');

//Set up mongodb and mongoose
//const db = require('./db');
require('./db');
const mongoose = require('mongoose');
const Flight = mongoose.model('Flight');
const User = mongoose.model('User');
const Contact = mongoose.model('Contact');

//Set up express validator
const expressValidator = require('express-validator');
app.use(expressValidator({
  errorFormatter: function(param, msg, value) {
      const namespace = param.split('.')
      , root = namespace.shift()
      , formParam = root;

    while(namespace.length) {
      formParam += '[' + namespace.shift() + ']';
    }
    return {
      param : formParam,
      msg   : msg,
      value : value
    };
  }
}));

// Connect Flash
app.use(flash());

// Global Variables for flash
app.use(function (req, res, next) {
  res.locals.success_msg = req.flash('success_msg');
  res.locals.error_msg = req.flash('error_msg');
  res.locals.error = req.flash('error');
  res.locals.user = req.user || null;
  next();
});
//const Comment = mongoose.model('Comment');
//hbs setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'hbs');


app.get('/', function(req, res){
	res.render('home');
});

app.get('/contact', function(req, res){
	Contact.find(function(err, contacts, count){
		console.log(err, contacts, count);
		res.render('contact');
	});
});

app.post('/contact', function(req, res){
	const newContact = new Contact({
		firstName: req.body.firstName,
		lastName: req.body.lastName,
		emailAddress: req.body.emailAddress,
		phoneNbr: req.body.phoneNbr,
		message: req.body.message
	});
	newContact.save(function(err){
		if(err){
			console.log(err);
		}else{
			res.render('contact', {'message': newContact.message, 'firstName':newContact.firstName, 'lastName':newContact.lastName, 'emailAddress':newContact.emailAddress,'phoneNbr':newContact.phoneNbr});
			//res.redirect('/contact');
		}
	});

});

app.use('/', routes);
app.use('/', users);
app.use('/', userFunctionalities);
app.use('/', adminFunctionalities);


//app.post()
//listen on port 3000
app.listen(process.env.PORT || 3000);
