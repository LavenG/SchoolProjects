const express = require('express');
const router = express.Router();
//Set up for session management
const session = require('express-session');
const mongoose = require('mongoose');
const Flight = mongoose.model('Flight');
const admin = require('./adminFunctionalities');
const allFlights = admin.allFlights;

require('../db');

router.get('/availableFlights', function(req, res){
  res.render('availableFlights', {'flights': allFlights});
});

router.post('/availableFlights', function(req,res){
  //console.log(req);
  if(req.body.flight1){
    const newFlight = new Flight({
      flightNumber: 'TK001',
      airline: 'Turkish Airlines',
      availableSeats: '380',
      takeOffLocation: 'New York',
      takeOffDate: '12/01/2017',
      takeOffTime: '08:00',
      landingLocation: 'Istanbul',
      landingDate: '12/02/2017',
      landingTime: '10:00'
    });
    newFlight.save(function(err){
    if(err){
      res.send(err);
    }
    });
  }
});

router.get('/myCart', function(req, res){
  if(req.user){
    console.log("cart " + req.user.cart);
    res.render('myCart', {flights: req.user.cart});
  }
});

module.exports = router;
