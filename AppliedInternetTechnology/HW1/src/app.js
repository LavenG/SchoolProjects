const rev = require('./reversi.js');
const readlineSync = require('readline-sync');
const fs = require('fs');

function score(board){
  const scores = rev.getLetterCounts(board);
  const toReturn = "Score\n=====\nX: "+ scores.X +"\n" + "O: " + scores.O;
  return toReturn;
}

let directory = "";
if(process.argv[2] === undefined){
  console.log("No config file was specified, User Controlled Game Settings will be used");
  console.log("Welcome to REVERSI");
  let boardWidth = parseInt(readlineSync.question("How wide would you like the board to be? (even numbers between 4 and 26, inclusive) "));
  while(!(parseInt(boardWidth)>= 4) || !(parseInt(boardWidth)<= 26)){
    boardWidth = parseInt(readlineSync.question("How wide would you like the board to be? (even numbers between 4 and 26, inclusive)› "));
  }
  let userLetter = readlineSync.question("Pick you letter: X (black) or O (white) ");
  while(userLetter !== "X" && userLetter !=="O"){
    userLetter = readlineSync.question("Pick you letter: X (black) or O (white) ");
  }
  console.log("Player is " + userLetter);
  let board = rev.generateBoard(boardWidth, boardWidth, " ");
  if(boardWidth%2 === 0){
    board = rev.setBoardCell(board, "X", boardWidth/2-1, boardWidth/2);
    board = rev.setBoardCell(board, "X", (boardWidth)/2, boardWidth/2-1);
    board = rev.setBoardCell(board, "O", (boardWidth)/2-1, boardWidth/2-1);
    board = rev.setBoardCell(board, "O", (boardWidth)/2, boardWidth/2);
  }else{
    board = rev.setBoardCell(board, "X", (boardWidth-1)/2-1, (boardWidth-1)/2);
    board = rev.setBoardCell(board, "X", (boardWidth-1)/2, (boardWidth-1)/2-1);
    board = rev.setBoardCell(board, "O", (boardWidth-1)/2-1, (boardWidth-1)/2-1);
    board = rev.setBoardCell(board, "O", (boardWidth-1)/2, (boardWidth-1)/2);
  }
  console.log(rev.boardToString(board));
  console.log(score(board));
  let counter = 0;
  while(!rev.isBoardFull(board) || counter !== 2){
    //The player is X so he goes first
    if(userLetter === "X"){
      let move = readlineSync.question("What's your move?\n");
      if(rev.isValidMoveAlgebraicNotation(board, userLetter, move) && rev.getValidMoves(board, userLetter).length!==0){
        board = rev.placeLetter(board, userLetter, move);
        const moveAsObj = rev.algebraicToRowCol(move);
        board = rev.flipCells(board, rev.getCellsToFlip(board, moveAsObj.row, moveAsObj.col));
        console.log(rev.boardToString(board));
        console.log(score(board));
      }else if(rev.getValidMoves(board, userLetter).length ===0){
        readlineSync.question("No valid moves available for you\n Press <ENTER> to pass. ");
        counter++;
      }else{
        console.log("INVALID MOVE. Your move should:\n* be in a  format \n* specify an existing empty cell\n* flip at elast one of your oponent's pieces");
      }
      const enter = readlineSync.question("Press <ENTER> to show computer's move...");
      if(rev.getValidMoves(board, "O").length !== 0){
        const computerMove = rev.getValidMoves(board, "O")[0];
        console.log(computerMove);
        board = rev.setBoardCell(board, "O", computerMove[0], computerMove[1]);
        board = rev.flipCells(board, rev.getCellsToFlip(board, computerMove[0], computerMove[1]));
        console.log(rev.boardToString(board));
        console.log(score(board));
        //board = rev.placeLetter(board, "O", move);
      }else{
        console.log("No valid moves for computer, passing");
        counter++;
      }
    //The computer is X therefore it goes first
    }else{
      const enter = readlineSync.question("Press <ENTER> to show computer's move...");
      if(rev.getValidMoves(board, "O").length !== 0){
        const computerMove = rev.getValidMoves(board, "O")[0];
        console.log(computerMove);
        board = rev.setBoardCell(board, "O", computerMove[0], computerMove[1]);
        board = rev.flipCells(board, rev.getCellsToFlip(board, computerMove[0], computerMove[1]));
        console.log(rev.boardToString(board));
        console.log(score(board));
      }else{
        console.log("No valid moves for computer, passing");
        counter++;
      }
      let move = readlineSync.question("What's your move?\n");
      if(rev.isValidMoveAlgebraicNotation(board, userLetter, move) && rev.getValidMoves(board, userLetter).length!==0){
        board = rev.placeLetter(board, userLetter, move);
        const moveAsObj = rev.algebraicToRowCol(move);
        board = rev.flipCells(board, rev.getCellsToFlip(board, moveAsObj.row, moveAsObj.col));
        console.log(rev.boardToString(board));
        console.log(score(board));
      }else if(rev.getValidMoves(board, userLetter).length ===0){
        readlineSync.question("No valid moves available for you\n Press <ENTER> to pass. ");
        counter++;
      }else{
        console.log("INVALID MOVE. Your move should:\n* be in a  format \n* specify an existing empty cell\n* flip at elast one of your oponent's pieces");
      }
    }
  }
}else{
  console.log("A config file was specified, setting up the game");
  console.log("Welcome to REVERSI");
  directory = process.argv[2];
  fs.readFile(directory, 'utf8', function(err, data) {
   if (err) {
     console.log('uh oh', err);
   }else{
     const dataJS = JSON.parse(data);
     let board = dataJS.boardPreset.board;
     const playerLetter = dataJS.boardPreset.playerLetter;
     let computerLetter = "";
     if(playerLetter === "X"){
      computerLetter = "O";
    }else{
      computerLetter = "X";
    }
    //This does not account for an uneven amount of scripted moves... sorry
    for(let i = 0; i<dataJS.scriptedMoves.player.length;i++){
      //Checking for whether the moves were valid would lead to an error because getCellsToFlip breaks it
        let enter = readlineSync.question("Press <ENTER> to see your next move");
        console.log("Player, played: " + dataJS.scriptedMoves.player[i]);
        board = rev.placeLetters(board, playerLetter,  dataJS.scriptedMoves.player[i]);
        let moveAsObj = rev.algebraicToRowCol(dataJS.scriptedMoves.player[i]);
        board = rev.flipCells(board, rev.getCellsToFlip(board, moveAsObj.row, moveAsObj.col));
        console.log(rev.boardToString(board));
        console.log(score(board));
        enter = readlineSync.question("Press <ENTER> to see the computer's next move");
        console.log("Computer, played: " + dataJS.scriptedMoves.computer[i]);
        board = rev.placeLetters(board, computerLetter, dataJS.scriptedMoves.computer[i]);
        moveAsObj = rev.algebraicToRowCol(dataJS.scriptedMoves.computer[i]);
        board = rev.flipCells(board, rev.getCellsToFlip(board, moveAsObj.row, moveAsObj.col));
        console.log(rev.boardToString(board));
        console.log(score(board));
    }

    let counter = 0;
    while(!rev.isBoardFull(board) || counter !== 2){
      //The player is X so he goes first
      if(playerLetter === "X"){
        let move = readlineSync.question("What's your move?\n");
        if(rev.isValidMoveAlgebraicNotation(board, playerLetter, move) && rev.getValidMoves(board, playerLetter).length!==0){
          board = rev.placeLetter(board, playerLetter, move);
          const moveAsObj = rev.algebraicToRowCol(move);
          board = rev.flipCells(board, rev.getCellsToFlip(board, moveAsObj.row, moveAsObj.col));
          console.log(rev.boardToString(board));
          console.log(score(board));
        }else if(rev.getValidMoves(board, playerLetter).length ===0){
          readlineSync.question("No valid moves available for you\n Press <ENTER> to pass. ");
          counter++;
        }else{
          console.log("INVALID MOVE. Your move should:\n* be in a  format \n* specify an existing empty cell\n* flip at elast one of your oponent's pieces");
        }
        const enter = readlineSync.question("Press <ENTER> to show computer's move...");
        if(rev.getValidMoves(board, "O").length !== 0){
          const computerMove = rev.getValidMoves(board, "O")[0];
          console.log(computerMove);
          board = rev.setBoardCell(board, "O", computerMove[0], computerMove[1]);
          board = rev.flipCells(board, rev.getCellsToFlip(board, computerMove[0], computerMove[1]));
          console.log(rev.boardToString(board));
          console.log(score(board));
        }else{
          console.log("No valid moves for computer, passing");
          counter++;
        }
      //The computer is X therefore it goes first
      }else{
        const enter = readlineSync.question("Press <ENTER> to show computer's move...");
        if(rev.getValidMoves(board, "O").length !== 0){
          const computerMove = rev.getValidMoves(board, "O")[0];
          console.log(computerMove);
          board = rev.setBoardCell(board, "O", computerMove[0], computerMove[1]);
          board = rev.flipCells(board, rev.getCellsToFlip(board, computerMove[0], computerMove[1]));
          console.log(rev.boardToString(board));
          console.log(score(board));
        }else{
          console.log("No valid moves for computer, passing");
          counter++;
        }
        let move = readlineSync.question("What's your move?\n");
        if(rev.isValidMoveAlgebraicNotation(board, playerLetter, move) && rev.getValidMoves(board, playerLetter).length!==0){
          board = rev.placeLetter(board, playerLetter, move);
          const moveAsObj = rev.algebraicToRowCol(move);
          board = rev.flipCells(board, rev.getCellsToFlip(board, moveAsObj.row, moveAsObj.col));
          console.log(rev.boardToString(board));
          console.log(score(board));
        }else if(rev.getValidMoves(board, playerLetter).length ===0){
          readlineSync.question("No valid moves available for you\n Press <ENTER> to pass. ");
          counter++;
        }else{
          console.log("INVALID MOVE. Your move should:\n* be in a  format \n* specify an existing empty cell\n* flip at elast one of your oponent's pieces");
        }
      }
    }

  }
});
}
