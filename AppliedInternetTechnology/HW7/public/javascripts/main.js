//Genreates a deck of cards
function generateDeck(){
  const deck = [];
  for(let i=0; i<4; i++){
    for(let j=1; j<=13; j++){
      let number = "";
      let suit = "";
      if(i === 0){
        suit = "\u2666";
      }else if(i === 1){
        suit = "\u2663";
      }else if(i === 2){
        suit = "\u2665";
      }else{
        suit = "\u2660";
      }
      if(j === 1){
        number = "A";
      }else if(j === 11){
        number = "J";
      }else if(j === 12){
        number = "Q";
      }else if(j === 13){
        number = "K";
      }else{
        number = j + "";
      }
      deck.push({"number":number, "suit":suit});
    }
  }
  return deck;
}

//shuffles the deck in a random manner based off of Durstendfeld shuffle algorithm,
//for reference http://stackoverflow.com/questions/2450954/how-to-randomize-shuffle-a-javascript-array
function shuffle(array){
  for(let i = array.length-1; i>0; i--){
    const j = Math.floor(Math.random()*(i+1));
    const curr = array[i];
    array[i] = array[j];
    array[j] = curr;
  }
}

//parses the user entered values into an array
function startValuesParser(string){
  if(string.length === 0){
    return;
  }
  return string.split(",");
  //Replace the numbers to match the format
}

//Puts the user selected values on top of the deck (which is the end of the array)
//the suit of the card is not considered so the first card matching the user entered number
//found in the deck is placed at the top no matter it's suit
function putOnTopOfDeck(deck, userValues){
  if(userValues === undefined){
    return;
  }
  for(let i = 0; i<userValues.length; i++){
    for(let j = 0; j<deck.length;j++){
      if(deck[j].number === userValues[i]){
        const curr = deck[j];
        deck[j] = deck[deck.length-1-i];
        deck[deck.length-1-i] = curr;
      }
    }
  }
}

//Creates an element that represents a player's deck of cards
function deckElementCreator(className){
  const ele = document.createElement("div");
  ele.className = className;
  for(let i = 1; i < arguments.length; i++){
    const child = document.createElement("p");
    ele.appendChild(child);
    let currentCard = arguments[i].number + "" + arguments[i].suit;
    if(typeof currentCard ==='string'){
      currentCard = document.createTextNode(currentCard);
      child.appendChild(currentCard);
    }
    if (className==='compDeckEle' && i!==1) {
      child.classList.toggle('cardDisplayComp');
    }else{
      child.classList.toggle('cardDisplay');
    }

  }
  return ele;
}

//Given a deck of cards calculates the total value of the cards,
//optimizing for aces
function calculateTotal(deck){
  let total = 0;
  const aces = [];
  for(let i=0; i< deck.length;i++){
    if(deck[i].number === 'A'){
      aces.push(deck[i]);
    }else if(deck[i].number === 'J' || deck[i].number === 'Q' || deck[i].number === 'K'){
      total+=10;
    }else{
      total += parseInt(deck[i].number);
    }
  }
  for(let i=0;i<aces.length;i++){
    if(total+11<=21){
      total+=11;
    }else{
      total+=1;
    }
  }
  return total;
}

//Creates an element to represent the total of the computer and the user
function totalElementCreator(string, deck){
  if(string === "computer"){
    const ele = document.createElement("div");
    ele.classList.toggle("total");
    ele.appendChild(document.createTextNode("Computer Hand - Total: ?"));
    return ele;
  }else{
    const ele = document.createElement("div");
    ele.classList.toggle("total");
    const total = calculateTotal(deck);
    ele.appendChild(document.createTextNode("User Hand - Total: "+total));
    return ele;
  }
}

//updates a given element, which is created by the function above
function updateTotal(element, deck, string){
  if(string === 'user'){
    const total = calculateTotal(deck);
    element.childNodes[0].nodeValue = "User Hand - Total: "+total;
    if(total>21){
      const ele = document.createElement("div");
      ele.classList.toggle("total");
      ele.appendChild(document.createTextNode("Player Lost: Bust!"));
      const game = document.getElementsByClassName("game");
      game[0].appendChild(ele);
    }
  }
  else{
    const total = calculateTotal(deck);
    element.childNodes[0].nodeValue = "Computer Hand - Total: "+total;
    if(total>21){
      const ele = document.createElement("div");
      ele.classList.toggle("total");
      ele.appendChild(document.createTextNode("Computer Lost: Bust!"));
      const game = document.getElementsByClassName("game");
      game[0].appendChild(ele);
    }
  }
}
//Creates an element that represents a card
function cardElementCreator(card){
  const ele = document.createElement("p");
  const currentCard = document.createTextNode(card.number+""+card.suit);
  ele.appendChild(currentCard);
  ele.classList.toggle('cardDisplay');
  return ele;
}
//Creates a button element with a given name
function buttonElementCreator(string){
  const ele = document.createElement("BUTTON");
  const text = document.createTextNode(string);
  ele.className = string;
  ele.appendChild(text);
  ele.classList.toggle("button");
  return ele;
}

function main(){
  const submitButton = document.querySelector(".playBtn");
  submitButton.addEventListener('click', function(event){
    event.preventDefault();
    const form = document.querySelector(".start");
    form.classList.toggle('hide');
    // form.style.display = 'none';
    let startValues = document.getElementById('startValues').value;
    const deck = generateDeck();
    shuffle(deck);
    startValues = startValuesParser(startValues);
    putOnTopOfDeck(deck, startValues);
    const userDeck = [];
    const computerDeck =[];

    //Deal the first 4 cards cards alternating between the computer and the user
    computerDeck.push(deck.pop());
    userDeck.push(deck.pop());
    computerDeck.push(deck.pop());
    userDeck.push(deck.pop());

    const game = document.getElementsByClassName("game");
    //First create an element that will contain the computer's card and add it after the game div
    const compDeckEle = deckElementCreator("compDeckEle", ...computerDeck);
    game[0].appendChild(compDeckEle);

    //Then create an element that will contain the user's cards and add it after the computer deck
    const userDeckEle = deckElementCreator("userDeckEle", ...userDeck);
    game[0].appendChild(userDeckEle);
    //Create elements to represent computer and user total
    const computerTotal = totalElementCreator("computer", computerDeck);
    const userTotal = totalElementCreator("user", userDeck);
    //insert them in the dom
    game[0].insertBefore(computerTotal, compDeckEle);
    game[0].insertBefore(userTotal, userDeckEle);

    //create and insert the hit and stand buttons
    const hit = buttonElementCreator("hit");
    const stand = buttonElementCreator("stand");
    game[0].appendChild(hit);
    game[0].appendChild(stand);

    //In the case that user hits, deal him a card
    //and see if he passes 21 or not
    hit.addEventListener('click', function(){
      const currentCard = deck.pop();
      userDeck.push(currentCard );
      userDeckEle.appendChild(cardElementCreator(currentCard));
      updateTotal(userTotal, userDeck, 'user');
    });

    //When the user stands, if the computer has less
    //than 21 it automatically draws a card,
    //Then the totals are calculated and the winner is determined
    stand.addEventListener('click', function(){
      //If the computer has less than 21 draw a card
      const compTotal = calculateTotal(computerDeck);
      if(compTotal<21){
        const currentCard = deck.pop();
        computerDeck.push(currentCard);
        compDeckEle.appendChild(cardElementCreator(currentCard));
        updateTotal(computerTotal, computerDeck, 'computer');
      }
      //Display all of the computer's cards
      for(let i = 1; i<compDeckEle.childNodes.length;i++){
        console.log(document.getElementsByClassName("compDeckEle")[0].childNodes[i]);
        document.getElementsByClassName("compDeckEle")[0].childNodes[i].className="cardDisplay";
      }
      //Determine the final totals for user and computer and determine the winner and whether there was a tie
      const finalCompTotal = calculateTotal(computerDeck);
      const finalUserTotal = calculateTotal(userDeck);
      if(finalCompTotal>finalUserTotal && finalCompTotal<=21){
        const ele = document.createElement("div");
        ele.classList.toggle("total");
        ele.appendChild(document.createTextNode("Computer Won!!!"));
        const game = document.getElementsByClassName("game");
        game[0].appendChild(ele);
      }else if(finalUserTotal>finalCompTotal && finalCompTotal<=21){
        const ele = document.createElement("div");
        ele.classList.toggle("total");
        ele.appendChild(document.createTextNode("Player Won!!!"));
        const game = document.getElementsByClassName("game");
        game[0].appendChild(ele);
      }else if(finalCompTotal===finalUserTotal){
        const ele = document.createElement("div");
        ele.classList.toggle("total");
        ele.appendChild(document.createTextNode("Tie!"));
        const game = document.getElementsByClassName("game");
        game[0].appendChild(ele);
      }
    });
  });
}

document.addEventListener('DOMContentLoaded', main);
