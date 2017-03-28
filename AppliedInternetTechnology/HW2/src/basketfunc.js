// basketfunc.js
//const rep = require('./report.js');

function processGameData(data){
  //console.log(data);
  const gameID = "Game ID: " + data.g.gid +", " + data.g.gdte +"\n=====" ;
  const rtc = data.g.vls.tc;
  const rtn = data.g.vls.tn;
  const ctc = data.g.hls.tc;
  const ctn = data.g.hls.tn;
  const rockets = data.g.vls.tc + " " + data.g.vls.tn;
  const celtics = data.g.hls.tc + " " + data.g.hls.tn;
  const rocketsPlayers = data.g.vls.pstsg;
  const celticsPlayers = data.g.hls.pstsg;

  //calculate the Rockets' score
  const rThreePts = rocketsPlayers.reduce(function(acc, value){
    return acc + value.tpm;

  }, 0);
      const rTwoPts = rocketsPlayers.reduce(function(acc, value){
        return acc + (value.fgm-value.tpm);
      }, 0);

      const rOnePts = rocketsPlayers.reduce(function(acc, value){
        return acc +value.ftm;
      }, 0);

      const rPts = 3*rThreePts + 2*rTwoPts + rOnePts;

      //Calculate the Celtics' score
      const cThreePts = celticsPlayers.reduce(function(acc, value){
        return acc + value.tpm;

      }, 0);
      const cTwoPts = celticsPlayers.reduce(function(acc, value){
        return acc + (value.fgm-value.tpm);
      }, 0);

      const cOnePts = celticsPlayers.reduce(function(acc, value){
        return acc +value.ftm;
      }, 0);

      const cPts = 3*cThreePts + 2*cTwoPts + cOnePts;

      const score = celtics + " - " + cPts +"\n" + rockets +" - " + rPts;

      //Calculate the celtics player with the most rebounds
      let cMostRebound = 0;
      let cMostReboundPlayer = "";

      celticsPlayers.forEach(function(element){
        if(element.oreb + element.dreb > cMostRebound){
          cMostRebound = element.oreb + element.dreb;
          cMostReboundPlayer = element.fn + " " + element.ln;
        }
      });
      //Calculate the rockets player with the most rebounds
      let rMostRebound = 0;
      let rMostReboundPlayer = "";

      rocketsPlayers.forEach(function(element){
        if(element.oreb + element.dreb > cMostRebound){
          rMostRebound = element.oreb + element.dreb;
          rMostReboundPlayer = element.fn + " " + element.ln;
        }
      });

      //Find the player with most rebounds overall
      let mostRb = 0;
      let mostRbPlayer = "";
      if(rMostRebound > cMostRebound){
        mostRb = rMostRebound;
        mostRbPlayer = rMostReboundPlayer;
      }else{
        mostRb = cMostRebound;
        mostRbPlayer = cMostReboundPlayer;
      }

      const finalMostRb = "* Most rebounds:" + mostRbPlayer + " with " + mostRb;

      //Find players that attempted at least 5 three points in each team
      const rAtLeastFiveThreeAttempted = rocketsPlayers.filter(function(element){
        return element.tpa > 5;
      });

      const cAtLeastFiveThreeAttempted = celticsPlayers.filter(function(element){
        return element.tpa > 5;
      });

      //Find player with highest 3 points percentage in both teams then overall
      let rHighestPctg = 0;
      let rHighestPctgPLayer = "";
      let rHighestPctgtpm = 0;
      let rHighestPctgtpa = 0;
      rAtLeastFiveThreeAttempted.forEach(function(element){
        if(element.tpm/element.tpa > rHighestPctg){
          rHighestPctg = element.tpm/element.tpa;
          rHighestPctgPLayer = element.fn + " " + element.ln;
          rHighestPctgtpm = element.tpm;
          rHighestPctgtpa = element.tpa;
        }
      });

      let cHighestPctg = 0;
      let cHighestPctgPLayer = "";
      let cHighestPctgtpm = 0;
      let cHighestPctgtpa = 0;
      cAtLeastFiveThreeAttempted.forEach(function(element){
        if(element.tpm/element.tpa > cHighestPctg){
          cHighestPctg = element.tpm/element.tpa;
          cHighestPctgPLayer = element.fn + " " + element.ln;
          cHighestPctgtpm = element.tpm;
          cHighestPctgtpa = element.tpa;
        }
      });

      let oHighestPctg = 0;
      let oHighestPctgPLayer = "";
      let oHighestPctgtpm = 0;
      let oHighestPctgtpa = 0;
      if(rHighestPctg > cHighestPctg){
        oHighestPctg = rHighestPctg;
        oHighestPctgPLayer = rHighestPctgPLayer;
        oHighestPctgtpm = rHighestPctgtpm;
        oHighestPctgtpa = rHighestPctgtpa;
      }else{
        oHighestPctg = cHighestPctg;
        oHighestPctgPLayer = cHighestPctgPLayer;
        oHighestPctgtpm = cHighestPctgtpm;
        oHighestPctgtpa = cHighestPctgtpa;
      }

      const highetPctgStr = "* Player with highest 3 point percentage that took at least 5 shots: " + oHighestPctgPLayer + " at " + oHighestPctg*100 + "%"+ " (" + oHighestPctgtpm + "/" + oHighestPctgtpa + ")";

      //Find the number of players with at least one block
      const cAtLeastOneBlock = celticsPlayers.filter(function(element){
        if(element.blk > 0){
          return element;
        }
      });

      const rAtLeastOneBlock = rocketsPlayers.filter(function(element){
        if(element.blk > 0){
          return element;
        }
      });

      const totalAtLeastOneBlock = "* There were " + (cAtLeastOneBlock.length+rAtLeastOneBlock.length) + " players that had at least one block";

      //Find all the players with more turnovers than assists in both teams
      let moreTurnoversThanAssists = "* Players with more turnovers than assists:\n\t" + ctc +" - " + ctn +"\n";

      celticsPlayers.forEach(function(element){
        if(element.tov > element.ast){
          moreTurnoversThanAssists += "\t* " + element.fn +" " + element.ln + " has an assist to turnover ratio of " + element.ast + ":" + element.tov +"\n";
        }
      });

      moreTurnoversThanAssists += "\n\t" + rtc +" - " + rtn +"\n";

      rocketsPlayers.forEach(function(element){
        if(element.tov > element.ast){
          moreTurnoversThanAssists += "\t* " + element.fn +" " + element.ln + " has an assist to turnover ratio of " + element.ast + ":" + element.tov +"\n";
        }
      });
      const toReturn = gameID +"\n" + score +"\n"+finalMostRb+"\n"+highetPctgStr+"\n" + totalAtLeastOneBlock + "\n" + moreTurnoversThanAssists;
      return toReturn;
}
//Export to be used in report.js
module.exports = {
  processGameData:processGameData
};
