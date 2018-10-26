var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical = false;
var Playmodal = document.getElementById("playModal");
var btn = document.getElementById("myBtn");
var span = document.getElementsByClassName("close")[0];
var Endmodal = document.getElementById("SurrenderModal");
var gameStart = false;

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

function makeGrid(table, isPlayer) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK")
            className = "sink";
        else if (attack.result === "SURRENDER" && gameStart == true){
            Endmodal.style.display = "block";
            var text = document.createTextNode(surrenderText);
            document.getElementById("Surrendertext").appendChild(text);
            gameStart = false;
           // alert(surrenderText);
           }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });
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

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    console.log(col);
    console.log(row);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;

            redrawGrid();
            placedShips++;
            if (placedShips == 3) {
                isSetup = false;
                registerCellListener((e) => {});
            }
            // clear placing mode, so hitting 'V' again
            // doesn't reshow our ship on the screen
            placingMode = 0;
        });
    } else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            alert("Cannot complete the action");
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

        }
    });
};