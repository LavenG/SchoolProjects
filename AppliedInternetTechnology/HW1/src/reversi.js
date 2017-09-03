// reversi.js

function repeat(value, n){
  const array = [];
  for (let i = 0; i<n; i++){
    array[i] = value;
  }
  return array;
}

function generateBoard(rows, columns, initialCellValue = " "){
  return repeat(initialCellValue, rows*columns);
}

function rowColToIndex(board, rowNumber, columnNumber){
  const boardWidth = Math.sqrt(board.length);
  return (boardWidth*rowNumber + columnNumber);
}

function indexToRowCol(board, i){
  const boardWidth = Math.sqrt(board.length);
  const object = {
    row: Math.floor(i/boardWidth),
    col: i%boardWidth
  };
  return object;
}

function setBoardCell(board, letter, row, col){
  const index = rowColToIndex(board, row, col);
  //making a shallow copy to fit the pre-written tests for the assingment
  const array = board.slice(0, board.length);
  array[index] = letter;
  return array;
}

function algebraicToRowCol(algebraicNotation){
  //handle incorrect usage
  if(algebraicNotation.charCodeAt(0)-65 < 0 || algebraicNotation.charCodeAt(0)-65>26 || algebraicNotation.length === 1 || algebraicNotation.includes(" ")){
    return undefined;
  }else if(algebraicNotation.length<3 && isNaN(algebraicNotation.charAt(1))){
    return undefined;
  }else if(algebraicNotation.length<4 && isNaN(algebraicNotation.charAt(2))){
    return undefined;
  }
  if(isNaN(algebraicNotation.charAt(1))){
    return undefined;
  }
  //If the usage is correct convert the algebraic notation to an object
  const columnToReturn = algebraicNotation.charCodeAt(0)-65;
  let rowToReturn = 0;
  if(algebraicNotation.length<3){
    rowToReturn += parseInt(algebraicNotation.charAt(1))-1;
  }else{
    rowToReturn += parseInt(algebraicNotation.charAt(1))*10 + parseInt(algebraicNotation.charAt(2));
  }
  const object = {
    row: rowToReturn,
    col: columnToReturn
  };
  return object;
}

function placeLetter(board, letter, algebraicNotation){
  const placement = algebraicToRowCol(algebraicNotation);
  return setBoardCell(board, letter, placement.row, placement.col);
}

function placeLetters(board, letter, ...arguments){
  let array = board.slice(0, board.length);
  for(let i = 0; i<arguments.length; i++){

    array = placeLetter(array, letter, arguments[i]);
  }
  return array;
}

//Prints the board, if the board is 10*10 or bigger removes a space from the row so that it's all aligned
function boardToString(board){
  let toPrint = "";
  const boardWidth = Math.sqrt(board.length);
  let firstRow = "     ";
  let rowDelim = "   ";
  for(let i = 0; i<boardWidth; i++){
    if(i!== (boardWidth-1)){
      firstRow += String.fromCodePoint(i + 65) + "   ";
      rowDelim += "+---";
    }else{
      firstRow += String.fromCodePoint(i + 65) + "  ";
      rowDelim += "+---";
    }
  }
  rowDelim += "+";
  let currentRow = "";
  toPrint += firstRow +"\n" + rowDelim +"\n";
  for(let i= 0; i<boardWidth;i++){
    if((i+1)/10 < 1){
      currentRow += " " + (i+1) +" | ";
    }else{
      currentRow += " " + (i+1) +"| ";
    }

    for(let j = 0; j<boardWidth; j++){
      if(j!== (boardWidth-1)){
        currentRow +=board[i*boardWidth+j] + " | ";
      }else{
        currentRow +=board[i*boardWidth+j] + " |";
      }
    }
    toPrint += currentRow + "\n" + rowDelim +"\n";
    currentRow = "";
  }
  return toPrint;
}

//Checks whether the board is full by checking every index
function isBoardFull(board){
  for(let i =0; i<board.length;i++){
    if(board[i] === " "){
      return false;
    }
  }
  return true;
}

//Flips the letter of the provided cell
function flip(board, row, col){
  const index = rowColToIndex(board, row, col);
  if(board[index] === "X"){
    board[index] = "O";
  }else if(board[index] === "O"){
    board[index] = "X";
  }
  return board;
}
//Flips multiple cells on the board
function flipCells(board, cellsToFlip){
  for(let i= 0; i< cellsToFlip.length;i++){
    for(let j=0; j< cellsToFlip[i].length; j++){
      board = flip(board, cellsToFlip[i][j][0], cellsToFlip[i][j][1]);
    }
  }
  return board;
}

//This function is broken and leads to most other errors on the project
function getCellsToFlip(board, lastRow, lastCol){

  const index = rowColToIndex(board, lastRow, lastCol);
  const boardWidth = Math.sqrt(board.length);
  const playedLetter = board[index];
  let letterToFlip = "";
  if(playedLetter === "X"){
    letterToFlip = "O";
  }else{
    letterToFlip = "X";
  }

  let cellsToFlip = [];
  let potentialFlips = [];
  let startRecording = false;
  let counter = 0;
  //Check the last row from left until the current placed item
  startRecording = false;
  potentialFlips = [];
  counter = (lastRow-1)*boardWidth;
  let playCount = 0;
  while(true && lastCol !== boardWidth-1){
    if(board[counter] === playedLetter){
      playCount++;
    }
    if(counter >= index || playCount===2){
      if(potentialFlips.length !== 0){
        cellsToFlip.push(potentialFlips);
      }
      break;
    }
    else if(board[counter] === playedLetter){
      startRecording = true;
    }else if(board[counter] === " "){
      break;
    }else if(board[counter] === letterToFlip && startRecording){
      const topush = new Array(Math.floor(counter/boardWidth), counter%boardWidth);
      potentialFlips.push(topush);
    }
    counter+= 1;
  }
    startRecording = false;
    potentialFlips = [];
    // Check the last row from current placed item to the right
    for(let i = lastCol+1; i<boardWidth;i++){
      //we've hit the boarder
      if(boardWidth*lastRow+i === boardWidth-1){
        if(potentialFlips.length !== 0){
          cellsToFlip.push(potentialFlips);
        }
        break;
      }
      else if(board[boardWidth*lastRow+i] === playedLetter){
        startRecording = true;
      }else if(board[boardWidth*lastRow+i] === " "){
        break;
      }else if(board[boardWidth*lastRow+i] === letterToFlip && startRecording){
        const topush = new Array(lastRow, i);
        potentialFlips.push(topush);
      }
    }
    //Check from the top to the current placed item
    startRecording = false;
    potentialFlips = [];
    counter = lastCol;
    while(true){
      if(counter >= index){
        if(potentialFlips.length !== 0){
          cellsToFlip.push(potentialFlips);
        }
        break;
      }
      else if(board[counter] === playedLetter){
        startRecording = true;
      }else if(board[counter] === " "){
        break;
      }else if(board[counter] === letterToFlip && startRecording){
        const topush = new Array(Math.floor(counter/boardWidth), counter%boardWidth);
        potentialFlips.push(topush);
      }
      counter+= boardWidth;
    }
    //Check from current placed item to bottom
    startRecording = false;
    potentialFlips = [];
    counter = index;
    playCount = 0;
    while(true && lastRow !== boardWidth-1){
      if(board[counter] === playedLetter){
        playCount++;
      }
      if(counter >= board.length || playCount===2){
        if(potentialFlips.length !== 0){
          cellsToFlip.push(potentialFlips);
        }
        break;
      }
      else if(board[counter] === playedLetter){
        startRecording = true;
      }else if(board[counter] === " "){
        break;
      }else if(board[counter] === letterToFlip && startRecording){
        const topush = new Array(Math.floor(counter/boardWidth), counter%boardWidth);
        potentialFlips.push(topush);
      }
      counter+= boardWidth;
    }
    //Check from left upper diagonal to last placed item
    startRecording = false;
    potentialFlips = [];

    counter = index-lastRow*boardWidth-(lastRow*1);
    while(true && lastCol!==0){
      if(counter > index){
        if(potentialFlips.length !== 0){
          cellsToFlip.push(potentialFlips);
        }
        break;
      }
      else if(board[counter] === playedLetter){
        startRecording = true;
      }else if(board[counter] === " "){
        break;
      }else if(board[counter] === letterToFlip && startRecording){
        const topush = new Array(Math.floor(counter/boardWidth), counter%boardWidth);
        potentialFlips.push(topush);
      }
      counter+= boardWidth+1;
    }
    //Check from last placed item to lower right diagonal
    startRecording = false;
    potentialFlips = [];
    counter = index+boardWidth+1;
    while(true && lastCol !== (boardWidth-1)){
      if(counter >= board.length){
        if(potentialFlips.length !==0){
          cellsToFlip.push(potentialFlips);
        }
        break;
      }
      else if(board[counter] === playedLetter){
        startRecording = true;
      }else if(board[counter] === " "){
        break;
      }else if(board[counter] === letterToFlip && startRecording){
        const topush = new Array(Math.floor(counter/boardWidth), counter%boardWidth);
        potentialFlips.push(topush);
      }
      counter+= boardWidth+1;
    }
    //Check from right upper diagonal to last placed item
    startRecording = false;
    potentialFlips = [];
    counter = index-lastRow*boardWidth+(lastRow*1);
    while(true && lastCol !== (boardWidth-1)){
      if(counter >= index){
        if(potentialFlips.length !== 0){
          cellsToFlip.push(potentialFlips);
        }
        break;
      }
      else if(board[counter] === playedLetter){
        startRecording = true;
      }else if(board[counter] === " "){
        break;
      }else if(board[counter] === letterToFlip && startRecording){
        const topush = new Array(Math.floor(counter/boardWidth), counter%boardWidth);
        potentialFlips.push(topush);
      }
      counter+= boardWidth-1;
    }
    //Check from last place item to bottom left
    startRecording = false;
    potentialFlips = [];

    counter = index+ boardWidth-1;
    while(true && lastCol!==0){
      if(counter >= board.length){
        if(potentialFlips.length !== 0){
          cellsToFlip.push(potentialFlips);
        }
        break;
      }
      else if(board[counter] === playedLetter){
        startRecording = true;
      }else if(board[counter] === " "){
        break;
      }else if(board[counter] === letterToFlip && startRecording){
        const topush = new Array(Math.floor(counter/boardWidth), counter%boardWidth);
        potentialFlips.push(topush);
      }
      counter+= boardWidth-1;
    }
    return cellsToFlip;
}
//This function works correctly, it generated an error because getCellsToFLip is broken
function isValidMove(board, letter, row, col){

  const boardCopy = board.slice(0, board.length);
  const index = rowColToIndex(board, row, col);
  boardCopy[index] = letter;
  const array = getCellsToFlip(boardCopy, row, col);
  if(board[index] !== " "){
    return false;
  }else if(index > board.length){
    return false;
  }else if(array.length === 0){
    return false;
  }
  return true;

}

function isValidMoveAlgebraicNotation(board, letter, algebraicNotation){
  const object = algebraicToRowCol(algebraicNotation);
  const row = object.row;
  const col = object.col;
  return isValidMove(board, letter, row, col);
}

function getLetterCounts(board){
  let Xcount = 0;
  let Ocount = 0;

  for(let i =0; i<board.length;i++){
    if(board[i] === "X"){
      Xcount++;
    }else if(board[i] === "O"){
      Ocount++;
    }
  }

  const object = {
    X: Xcount,
    O: Ocount
  };
  return object;
}

function getValidMoves(board, letter){
  const boardWidth = Math.sqrt(board.length);
  let toReturn = [];
  for(let i = 0; i<boardWidth;i++){
    for(let j = 0; j<boardWidth; j++){
      if(isValidMove(board, letter, i, j)){
        toReturn.push([i, j]);
      }
    }
  }
  return toReturn;
}


module.exports = {
  repeat: repeat,
  generateBoard: generateBoard,
  rowColToIndex: rowColToIndex,
  indexToRowCol: indexToRowCol,
  setBoardCell: setBoardCell,
  algebraicToRowCol: algebraicToRowCol,
  placeLetter: placeLetter,
  placeLetters: placeLetters,
  boardToString: boardToString,
  isBoardFull: isBoardFull,
  flip: flip,
  flipCells: flipCells,
  getCellsToFlip: getCellsToFlip,
  isValidMove: isValidMove,
  isValidMoveAlgebraicNotation: isValidMoveAlgebraicNotation,
  getLetterCounts: getLetterCounts,
  getValidMoves: getValidMoves
};
