var isSetup = true;//determines whether all ships have been placed (false when they have)
var placedShips = 0;//tracks the number of placed player ship. max of 3
var game;
var shipType;//when a ship is detected as 'hit', holds the type of ship
var vertical = false;
var battleHistory = "";//holds string to display to battle log


var Playmodal = document.getElementById("playModal");
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];
var Endmodal = document.getElementById("SurrenderModal");
var gameStart = false;
var reloadbtn = document.getElementById("Reload");

 btn.onclick = function(){
    Playmodal.style.display = "block";
}
 span.onclick = function(){
    Playmodal.style.display = "none";
}
 document.onclick = function(e){
    if(e.target == Playmodal){
        Playmodal.style.display = "none";
    }
}

/*
    Creates the playable grid
*/
function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {//create row
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {//create column
            let peg = document.createElement('div');
            peg.classList.add("peg");
            peg.classList.add("hidden");
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            column.appendChild(peg);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

// tracker vars for handling newly detected ships via sonar
var sonarDetectedTotal = 0;
var sonarNewlyDetected = false;

/*
    Interprets attacks on the clicked square.
*/
function markHits(board, elementId, surrenderText) {
    let counter = 0;//tracks current iteration
    let sonarOcc = false;
    let sonarDetected = 0;

    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK")
            className = "sink";
        else if (attack.result === "SURRENDER" && gameStart == true) {
           Endmodal.style.display = "block";
           var text = document.createTextNode(surrenderText);
           document.getElementById("Surrendertext").appendChild(text);
           gameStart = false;
           Reload.onclick = function(){
               window.location.reload();
           }
        } else if(attack.result == "SONAR_EMPTY") {
            className = "sonar-empty";

        } else if(attack.result == "SONAR_OCCUPIED") {
            className = "sonar-occupied";
            sonarDetected++;

        } else if(attack.result == "UNDERWATER"){
                className = "underwater";

        }
          else {
            alert("Unrecognized attack result of '"+attack.result+"'. Set up handling for this!");

        }

        var i1 = attack.location.row-1;
        if(i1 < 0) {
            alert("Invalid attack row of "+i1);
            return;
        }
        var i2 = attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0);
        if(i2 < 0) {
            alert("Invalid attack column of "+i2);
            return;
        }

        //adds the colored pegs on the square
        document.getElementById(elementId).rows[i1].cells[i2].childNodes[0].classList.add(className);
        document.getElementById(elementId).rows[i1].cells[i2].childNodes[0].classList.remove("hidden");

        //on the last iteration, write the attacked square
        if(counter == (board.attacks.length - 1))
        {
            if(sonarDetectedTotal < sonarDetected) {
                sonarNewlyDetected = true;
                sonarDetectedTotal = sonarDetected;
            }


            writeBRAttack(elementId, attack.location.row, attack.location.column, attack.result);

            sonarNewlyDetected = false;
        }

        counter++;
    });
}

/*
    writes the result of an attack
*/
function writeBRAttack(attacker, locY, locX, attRes)
{
    let newRow = numCharInvert(true, locY);//turn row from number to letter
    let newCol = numCharInvert(false, locX);//turn  col from letter to number
    let oppElem = "";


    if(attacker == "opponent"){
        oppElem = "PLAYER";
    }
    else{
        oppElem = "OPPONENT"
    }

    let attText = oppElem+" attacked "+newRow+""+newCol+" and ";

    if(attRes == "MISS"){
        attText = attText+" <span class='attackMiss'>MISSED</span>";
    }
    else if(attRes == "HIT"){
        attText = attText+" <span class='attackHit'>HIT</span>"
    }
    else if (attRes == "SUNK"){
        attText = attText+" <span class='attackSunk'>SUNK SHIP</span>";
    }
    else if(sonarNewlyDetected){ // overrides sonar empty
        attText = "PLAYER <span class='sonarDetectedS'> SONAR SWEEP DETECTED A SHIP</span>";
    }
    else if (attRes == "SONAR_EMPTY"){
        attText = "PLAYER <span class='sonarDetectedN'> SONAR SWEEP EMPTY</span>";
    }
    attText = attText+"!<br/>";

    handleBattleReport(attText);
}


function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
        if(square.captain) {
            document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied-captain");
        }
    }));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

var usingSonar = false;

function toggleSonarMode(e) {
    // toggle using sonar mode
    usingSonar = usingSonar ? false : true;
}

/*
    This function determines what happens when a cell is clicked.
    It first checks if all player ships have been placed. If they have not, it interprets the action
    as an attempt to place a ship to that square. If all ships have been placed, the action is instead
    interpreted as an attack.
*/
function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);

     let newRow = numCharInvert(true, row);//turn row from number to letter
     let newCol = numCharInvert(false, col);//turn  col from letter to number

    //isSetup determines whether all player battle ships have been placed
    if (isSetup) {//interpret action as an attempt to place a ship
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;

            // lock out this ship type
            setDisabled(shipType);

            redrawGrid();
            placedShips++;

            //Once a ship is successfully placed, a report is sent to battle report
            handleBattleReport("<span class='shipsPlacedBR'>Player placed "+shipType+" at: " +newRow+""+newCol+"</span><br/>");

            if (placedShips == 4) {//all ships have been placed
                isSetup = false;
                registerCellListener((e) => {});

                handleBattleReport("<span class='shipsPlacedBR'>All ships have been placed. Begin attack on the enemy!</span><br/>");

                // visually indicate it's time to start attacking
                document.getElementById("opponent").className+=" animate-this";
                (new Toast("All pieces placed, begin your attack!", "#0f0")).show();
            }
            // clear placing mode, so hitting 'V' again
            // doesn't reshow our ship on the screen
            placingMode = 0;
        });
    } else {
        if(!usingSonar) {
            sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
                if(data.opponentsBoard.sonarEnabled && !game.opponentsBoard.sonarEnabled) {
                    // enable the sonar button with green highlight
                    var ss = document.getElementById("sweepScan");
                    ss.className = "sweep-scan-enabled";
                    ss.addEventListener("click", toggleSonarMode);
                    ss.title = "Click here and select a space to perform a sonar pulse.";
                    ss.childNodes[0].src = "/assets/images/sweep2.png";
                    ss.childNodes[0].className = "animate-this";
                    (new Toast("Sonar Pulse and Laser Weapon Enabled!", "#0f0")).show();

                    handleBattleReport("<span class='weaponUpgrade'>--Sonar pulse: ENABLED--</span><br/>");
                    handleBattleReport("<span class='weaponUpgrade'>--Space laser: ENABLED--</span><br/>");

                    var LS = document.getElementById("LaserWeapon");
                    LS.title = "ION Cannon Available";
                    LS.childNodes[0].src = "/assets/images/Ret2.png";
                    LS.childNodes[0].className = "animate-this";
                    //(new Toast("Laser Weapon Enabled", "#0f0")).show();

                }
                game = data;
                redrawGrid();
            });

        } else {
            // make a call for the sonar button
            usingSonar = false;
            sendXhr("POST", "/sonar", {game: game, x: row, y: col}, function(data) {
                game = data;
                redrawGrid();

                // swap the img source to the next one down
                if(game.opponentsBoard.sonarCount == 1) {
                    // 1 left
                    document.getElementById("sweepScanImg").src = "/assets/images/sweep1.png";

                } else {
                    // 0 left, disable & remove event listener again
                    document.getElementById("sweepScanImg").src = "/assets/images/sweep0.png";
                    document.getElementById("sweepScanImg").title = "";
                    document.getElementById("sweepScan").className = "";
                    document.getElementById("sweepScan").removeEventListener("click", toggleSonarMode);

                    handleBattleReport("<br/><span id='sonarOffline'>--Sonar: OFFLINE--</span><br/>");
                }

            });
        }
    }
}

function setDisabled(shipType) {
    if (shipType === "MINESWEEPER"){shipId = "place_minesweeper";}
    else if (shipType === "DESTROYER"){shipId = "place_destroyer";}
    else if (shipType === "BATTLESHIP"){shipId = "place_battleship";}
    else if (shipType === "SUBMARINE"){shipId = "place_submarine";}
    else {console.warn("!! Unknown ship found in lockout code, '"+shipType+"' !!"); return;} // warn of an unknown ship type

    document.getElementById(shipId).disabled = true;
    document.getElementById(shipId).classList.add("ship-placed");
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            // show a toast
            (new Toast("Cannot complete the action!", "#e00")).show();
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}


function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        let table = document.getElementById("player");

        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
    }
}

/*
    converts a number to a letter from A - J or a letter to a number from 1 - 10
    'toLet' is a boolean which determined if 'inp' will be converted to a number or letter.
*/
function numCharInvert(toLett, inp)
{
    var a = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
    if(toLett == false){
        return a.indexOf(inp)+1;
    }
    else{
        return a[inp-1];
       }
}

 /*
    Writes to the 'battleReport' element. Its single parameter 'newText' is a string
    which is appended to the end of the already existing string 'battleHistory'
*/
function handleBattleReport(newText)
{
    battleHistory = newText + battleHistory;
    var br = document.getElementById("battleReport");
    br.innerHTML = battleHistory;;

    //br.scrollTop = br.scrollHeight;
}

// Tracks and restores placing when 'V' is pressed
// Is cleared when a piece is placed successfully
var placingMode = 0;

function initGame() {
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    gameStart = true;
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
       placingMode = 1;
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
       placingMode = 2;
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
        shipType = "BATTLESHIP";
       registerCellListener(place(4));
       placingMode = 3;
    });
    document.getElementById("place_submarine").addEventListener("click", function(e) {
       shipType = "SUBMARINE";
       registerCellListener(place(5));
           placingMode = 4;
        });
    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });

    // Adds keydown listener to flip pieces with 'v'
    document.addEventListener("keydown", function(event) {
        var key = event.keyCode;
        if (key === 86 && vertical == false){
            vertical = true;
        }
        else if (key === 86 && vertical == true){
            vertical = false;
        }

        // Find and clear any pieces that are currently 'placed'
        // Restore current placing mode as well
        redrawGrid();
        if(placingMode == 1) {
            shipType = "MINESWEEPER";
            registerCellListener(place(2));

        } else if(placingMode == 2) {
            shipType = "DESTROYER";
            registerCellListener(place(3));

        } else if(placingMode == 3) {
            shipType = "BATTLESHIP";
           registerCellListener(place(4));

        } else if(placingMode == 4) {
                     shipType = "SUBMARINE";
                    registerCellListener(place(5));

        }
    });

    (new Toast("Place your ships to start the game!", "#0f0")).show();
};
