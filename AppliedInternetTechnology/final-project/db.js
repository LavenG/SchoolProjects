const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

const Flight = new mongoose.Schema({
  flightNumber: String,
  airline: String,
  availableSeats: String,
  takeOffLocation: String,
  takeOffDate: String,
  takeOffTime: String,
  landingLocation: String,
  landingDate: String,
  landingTime: String
});

const UserSchema = new mongoose.Schema({
  username: String,
  name: String,
  email: String,
  password: String,
  admin: Boolean,
  bookings: [Flight],
  cart: [Flight]
});

const Contact = new mongoose.Schema({
  firstName: String,
  lastName: String,
  emailAddress: String,
  phoneNbr: String,
  message: String
});

const allFlights= new mongoose.Schema({
  flights: [Flight]
});


mongoose.model('Flight', Flight);
mongoose.model('allFlights', allFlights);
mongoose.model('Contact', Contact);
const User = module.exports = mongoose.model('User', UserSchema);

//const Flights = module.exports = mongoose.model('Flights', allFlights);

//Passport stuff
module.exports.createUser = function(newUser, callback){
  bcrypt.genSalt(10, function(err, salt) {
    bcrypt.hash(newUser.password, salt, function(err, hash) {
      newUser.password = hash;
      newUser.save(callback);
    });
	});
};

module.exports.getUserByUsername = function(username, callback){
	const query = {username: username};
	User.findOne(query, callback);
};

module.exports.getUserById = function(id, callback){
	User.findById(id, callback);
};

module.exports.comparePassword = function(candidatePassword, hash, callback){
	bcrypt.compare(candidatePassword, hash, function(err, isMatch) {
    if(err){
      throw err;
    }
    callback(null, isMatch);
	});
};

// is the environment variable, NODE_ENV, set to PRODUCTION?
if (process.env.NODE_ENV === 'PRODUCTION') {
 // if we're in PRODUCTION mode, then read the configration from a file
 // use blocking file io to do this...
 var fs = require('fs');
 var path = require('path');
 var fn = path.join(__dirname, 'config.json');
 var data = fs.readFileSync(fn);

 // our configuration file will be in json, so parse it and set the
 // conenction string appropriately!
 var conf = JSON.parse(data);
 var dbconf = conf.dbconf;
} else {
 // if we're not in PRODUCTION mode, then use
 dbconf = 'mongodb://localhost/final';
}

mongoose.connect(dbconf);
