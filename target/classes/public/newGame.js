
var newGameBikeList = [];




function request(obj) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.open(obj.method || "GET", obj.url);

        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.response);
            } else {
                reject(xhr.statusText);
            }
        };
        xhr.onerror = () => reject(xhr.statusText);

        xhr.send(obj.body);
    });
};

function xmlRequest(verb, url) {
    var xhr = new XMLHttpRequest();
    xhr.open(verb || "GET", url, true);
    xhr.onload = () => {
        console.log(xhr.response);
    };
    xhr.onerror = () => {
        console.log("error: " + statusText);
    };
    xhr.send();
}


function listBikes() {
    var tester = "";
    request({ url: "/getGames?tableName=bikeclasses", method: "GET" })
        .then(data => {
            if (data.length != null) {
                let res = JSON.parse(data);
                console.log(res);

                for (var i = 0; i < res.length; i++) {
                    for (var j = 0; j < res[0].length; j++) {
                    
                   if (res[i][j] != null){
                   tester = tester + res[i][j] + " ";
                   }
                    }
                    
                    
                }
                document.getElementById("bikeListOutput").innerHTML=tester; 
console.log(tester);
            }
        })
        .catch(error => {
            result.innerHTML += "Could not find any games."
        });

    document.getElementById("bikeListOutput").value += "";
}

function createGame() {
    var newGameName = document.getElementById("userGameName");
    var bikes = "";
    for (var i = 0, l = newGameBikeList.length; i < l; ++i) {
        bikes = bikes + "|" + newGameBikeList[i];
    }
    
    request({ url: "/createGame?newGameId=" + newGameName + "bikeList=" + bikes, method: "POST" })
    .then(data => {
        console.log("New game " + data + "has been created. Cool" )
    })
    .catch(error => {
        console.log(error);

    });
}



function initialize() {
    console.log("intializing game...");
    request({ url: "/getGrid", method: "GET" })
        .then(data => {
            //parse that yes
        })
        .catch(error => {
            console.log(error);

        });
}


function selectBike(bikeID) {

    newGameBikeList.push(bikeID);
}

function selectGame() {
    var gameid = document.getElementById("userGameId");
    request({ url: "/selectGame?gameid=" + gameid, method: "POST" })
}


