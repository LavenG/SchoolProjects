// evenWarmer.js
// create Request and Response constructors...
//parses in the httpRequest passed in as a string
function Request(httpRequest){

  httpRequest = "" + httpRequest;
  this.httpRequest = httpRequest;
  //Create an Array out of the string
  const splitRequest = httpRequest.split('\r\n');
  //Represents the line with the actual request
  let requestline = splitRequest[0];
  //Represent the line with the host
  let host = splitRequest[1];
  //Represents the line with the referer
  let referer = splitRequest[2];
  //Represents the line with the body
  const body = splitRequest[3];

  //Parse the request line and set the method and path using the resulting array
  requestline = requestline.split(' ');
  this.method = requestline[0];
  this.path = requestline[1];
  //console.log(this.path);
  //Parse the host and referers and set the headers of the request accordingly
  host = host.split(' ');
  referer = referer.split(' ');

  if(referer[1]){
    this.headers = {
      Host : host[1],
      Referer: referer[1]
    };
  }else{
    this.headers = {
      Host : host[1]
    };
  }
  //Set the body equal to the body passed into the request
  this.body = body;



}
//Return the string passed in when toString is called
Request.prototype.toString = function(){
  return this.httpRequest;
};


function Response(socket){
  //Set the headers to an empty object to be initialized when a response object is constructed
  this.headers = {};
  this.socket = socket;
}

//setHeader methid
Response.prototype.setHeader = function(name, value) {
  this.headers[name] = value;
};
//write method writes using the socket passed in
Response.prototype.write = function(data){
  this.socket.write(data);
};
//end method ends using the socket passed in
Response.prototype.end = function(data){
  this.socket.end(data);
};

//send method defines the status code, the body and ends using the socket passed in with a correctly formatted string
Response.prototype.send = function(statusCode, body){
  this.statusCode = statusCode;
  this.body = body;
  let string = 'HTTP/1.1';
  string +=" " + statusCode + " OK\r\n";
  for(let i in this.headers){
    string += i + ": " + this.headers[i] +"\r\n\r\n";
  }
  string += this.body;
  this.socket.end(string);
};

//Sets the status code and writes
Response.prototype.writeHead = function(statusCode){
  this.statusCode = statusCode;
  let string = 'HTTP/1.1';
  string +=" " + statusCode + " OK\r\n";
  for(let i in this.headers){
    string += i + ": " + this.headers[i] +"\r\n\r\n";
  }
  this.write(string);
};

//Redicrects to an other url by adding it to the headers and ending
Response.prototype.redirect = function(statusCode, url){
  if(isNaN(statusCode)){
    this.statusCode = 301;
    this.headers["Location"] = statusCode;
  }else{
    this.statusCode = statusCode;
    this.headers["Location"] = url;
  }
  this.send(this.statusCode, this.body);
};

//converts to a String
Response.prototype.toString = function(){
  const statusCodes = {
    "200" : "OK",
    "404" : "Not Found",
    "500" : "Internal Server Error",
    "400" : "Bad Request",
    "301" : "Moved Permanently",
    "302" : "Found",
    "303" : "See Other"
  };
  //Handle empty or undefined bodies
  let toRet = "HTTP/1.1";
  if(this.headers !== undefined && (this.headers).length !== 0){
    if(this.body === "\r\n" || this.body === undefined){
      this.body = "";
    }
    let headersStr = "";
    for(let i in this.headers){
      headersStr += i + ": " + this.headers[i] + "\r\n";
    }
    toRet += " " + this.statusCode + " " + statusCodes[this.statusCode] + "\r\n" + headersStr + "\r\n" + this.body;
  }else{
    if(this.body === undefined){
      this.body = "\r\n";
    }
    toRet+= " " + this.statusCode + " " + statusCodes[this.statusCode] + "\r\n" + this.body;
  }
  return toRet;
};

//Sends a file
Response.prototype.sendFile = function(fileName){
  const fs = require('fs');
  const path = require('path');
  //Find the correct directory
  const _dirname = path.resolve(".");
  const publicRoot = _dirname + "/public/";
  const filePath = publicRoot + fileName;

  //Define the supported extensions for the file to be sent
  const extensions = {
    "jpeg" : "image/jpeg",
    "jpg": "image/jpeg",
    "png": "image/png",
    "gif": "image/gif",
    "html": "text/html",
    "css": "text/css",
    "txt": "text/plain"
  };
  //determine the extension of the current file
  let currentExtension = fileName.split('.');
  currentExtension = currentExtension[currentExtension.length-1];
  this.contentType = extensions[currentExtension];
  //The callback function that is responsible for handling the acutal writing of the data and the closing of the socket
  this.callback = function(contentType, err, data){
    if(err){
      this.writeHead(500);
      this.end();
    }else{
      this.setHeader('Content-Type', contentType);
      this.writeHead(200);
      this.write(data);
      this.end();
    }
  };
  //Call the callback function using the appropriate structure
  if(currentExtension === 'jpeg' || currentExtension === 'jpg' || currentExtension === 'png' || currentExtension === 'gif'){
    fs.readFile(filePath, {}, this.callback.bind(this, this.contentType));
  }else{
    fs.readFile(filePath, {encoding : 'utf8'}, this.callback.bind(this, this.contentType));
  }

};

//The following code was used for testing the methods implemented above.

/*
const net = require('net');
const HOST = '127.0.0.1';
const PORT = 8080;
const server = net.createServer((sock) => {
  sock.on('data', (binaryData)=>{
    const req = new Request(binaryData);
    const res = new Response(sock);
    if(req.path !== undefined){
      if(req.path === "/foo.css"){
        res.setHeader("Content-Type", "text/css");
        res.send(200, "h2 {color: red;}");
      }else if(req.path === "/"){
        res.setHeader("Content-Type", "text/html");
        res.send(200, "<h2> <link rel='stylesheet' type = 'text/css' href='/Users/alperenkaraoglu/Desktop/AIT/ak4410-homework03/public/foo.css'>this is a red header!</h2> \r\n<em>Hello</em> <strong>World</strong>");
      }else if(req.path === "/test"){
        res.sendFile("html/test.html");
      }else if(req.path==="/bmo1.gif"){
        res.sendFile("img/bmo1.gif");
      }else{
        res.setHeader("Content-Type", "text/html");
        res.send(404, "uh oh... 404 page not found!");
      }
    }else{
      res.setHeader("Content-Type", "text/html");
      res.send(404, "uh oh... 404 page not found!");
    }
  });
});
server.listen(PORT, HOST);
*/


//Class for App object with constructore
class App {

      constructor(){
        const net = require('net');
        //Define the default values for when the App object is constructed
        this.routes = {};
        this.server = net.createServer(this.handleConnection.bind(this));
      }
}

//adds path as a property name in routes
App.prototype.get = function(path, cb){
    this.routes[path] = cb;
};

//binds the server to the given port and host
App.prototype.listen = function(port, host){
    this.server.listen(port, host);
};
//the function called when a client connects to the server
App.prototype.handleConnection = function(sock){
    sock.on('data', this.handleRequestData.bind(this, sock));
};

//the function called when the socket receives data from the client
App.prototype.handleRequestData = function(sock, binaryData){
    const str = ""+binaryData;
    this.data = str;
    this.req = new Request(str);
    this.res = new Response(sock);

    if(str[0] !== 'HTTP/1.1'){
      this.res.statusCode = 400;
    }

    if(this.routes[this.req.path] !== undefined){
      this.routes[this.req.path](this.req, this.res);
    }else{
      this.res.statusCode = 404;
      this.res.setHeader("Content-Type", "text/html");
      this.res.send(404, "uh oh... 404 page not found!");
    }
    sock.on('close', this.logResponse.bind(this, this.req, this.res));

  };
//logs out the http request method and path… as well as the response status code and short message
App.prototype.logResponse= function(req, res){
    const statusCodes = {
      "200" : "OK",
      "404" : "Not Found",
      "500" : "Internal Server Error",
      "400" : "Bad Request",
      "301" : "Moved Permanently",
      "302" : "Found",
      "303" : "See Other"
    };
    console.log(req.method + ' ' + req.path + ' - ' + res.statusCode +' ' + statusCodes[res.statusCode]);
};


module.exports = {
  App: App,
  Response: Response,
  Request: Request
};
