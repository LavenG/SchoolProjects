// hoffy.js
function prod(...arguments){
  if(arguments.length !== 0){
    const prod = arguments.reduce(function(prod, curr) {
      return prod * curr;
    }, 1);

    return prod;
  }else {
    return undefined;
  }
}

function any(arr, fn){
  const passes = arr.filter(fn);
  if(passes.length>0){
    return true;
  }else{
    return false;
  }
}

function maybe(fn){
   return function(fn1){
    const args = [...arguments];
    if(args.indexOf(undefined) > -1 || args.indexOf(null) > -1){
      return undefined;
    }else{
      return fn(...args);
    }
  };
}

function constrainDecorator(fn, min, max){
  return function(){
    if(fn.apply(this, arguments) < min){
      return min;
    }else if(fn.apply(this, arguments)>max){
      return max;
    }else{
      return fn.apply(this, arguments);
    }
  };
}

function limitCallsDecorator(fn, n){
  let counter = 0;
  return function(){
    if(counter < n){
      counter++;
      return fn.apply(this, arguments);
    }else{
      return undefined;
    }
  };
}

function mapWith(fn){
  return function(mappedFn){
    return Array.prototype.map.call(mappedFn, function (element){
      return fn.call(this, element);
    });
  };
}

function simpleINIParse(s){
  s = s.split("\n");
  //let object = {};
  const object = s.reduce(function(object, value){
    if(value.indexOf("=") < 0){
      return object;
    }
    const splitVal = value.split("=");
    if(splitVal[0] === undefined || splitVal[0] === null){
      splitVal[0] = "";
    }else if(splitVal[1] === undefined || splitVal[1] === null){
      splitVal[1] = "";
    }

    object[splitVal[0]] = splitVal[1];
    return object;
    }, {});
  return object;
}

function readFileWith(fn){
   return function(filename, callback){
     const fs = require('fs');
     const directory = filename;
     let dataParsed = {};
     return fs.readFile(directory, 'utf8', function(err, data){
       if(err === null){
         dataParsed = fn(data);
         callback(err, dataParsed);
       }else{
         callback(err, undefined);
       }
     });
   };
}



module.exports = {
  prod: prod,
  any: any,
  maybe:maybe,
  constrainDecorator: constrainDecorator,
  limitCallsDecorator: limitCallsDecorator,
  mapWith: mapWith,
  simpleINIParse: simpleINIParse,
  readFileWith: readFileWith
};
