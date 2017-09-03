// fansite.js
// create your own fansite using your miniWeb framework
const App = require('./miniWeb.js').App;
const app = new App();

//Handle all possible page requests

app.get('/', function(req,res){
  res.sendFile("/html/homePage.html");
});

app.get('/about', function(req,res){
  res.sendFile("/html/about.html");
});

app.get('/rando', function(req,res){
  res.sendFile("/html/random.html");
});

app.get('/css/base.css', function(req, res){
  res.sendFile("/css/base.css");
});

app.get('/img/mortyOnClouds.jpeg', function(req,res){
  res.sendFile('/img/mortyOnClouds.jpeg');
});

app.get('/img/morty.jpg', function(req,res){
  res.sendFile('/img/morty.jpg');
});

app.get('/img/background.jpg', function(req, res){
  res.sendFile('/img/background.jpg');
});

app.get('/home', function(req, res){
  res.redirect(301, '/');
  res.end();
});

app.get('/about', function(req, res){
  res.sendFile('/html/about.html');
});

app.get('/randopic', function(req,res){
  const rando = Math.floor(Math.random()* 3)+1;
  if(rando === 1){
    res.sendFile('/img/mortyghosteyes.gif');
  }else if(rando === 2){
    res.sendFile('/img/mortyspasm.gif');
  }else if(rando === 3){
    res.sendFile('/img/sleepyMorty.gif');
  }
});

app.get('/image1.jpg',function(req, res){
  res.sendFile('/img/squintyEyes.jpg');
});

app.get('/image2.png',function(req, res){
  res.sendFile('/img/surprised.png');
});

app.get('/image3.gif',function(req, res){
  res.sendFile('/img/mortys.gif');
});

app.get('/home', function(req,res){
  res.redirect(301, "/");
  res.end();
});

app.listen(8080, '127.0.1');
