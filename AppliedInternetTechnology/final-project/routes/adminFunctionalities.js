const express = require('express');
const router = express.Router();
//Set up for session management
const session = require('express-session');
const mongoose = require('mongoose');
const Flight = mongoose.model('Flight');
const allFlights = mongoose.model('allFlights');

require('../db');

router.get('/flights', function(req, res){
  res.render('flights', {'flights': allFlights});
});

router.post('/flights', function(req, res){
  const newFlight = new Flight({
    flightNumber: req.body.flightNumber,
    airline: req.body.airline,
    availableSeats: req.body.availableSeats,
    takeOffLocation: req.body.availableSeats,
    takeOffDate: req.body.takeOffDate,
    takeOffTime: req.body.takeOffTime,
    landingLocation: req.body.landingLocation,
    landingDate: req.body.landingDate,
    landingTime: req.body.landingTime
  });
  newFlight.save(function(err){
    if(err){
      res.send(err);
    }else{
      allFlights.flights.push(newFlight);
      console.log(allFlights);
      res.render('flights', {'flights': allFlights.flights});
    }
  });
 });


module.exports = router;
