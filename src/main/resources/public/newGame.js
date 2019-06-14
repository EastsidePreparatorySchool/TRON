
var newGameBikeList = [];
var bikeID = document.getElementById("userBikes").value;

function createGameTest() {
    var typeOfTest = document.getElementById("typeTest").value;
    var numTest = document.getElementById("numTest").value;
    var outputHandle = document.getElementById("resultTest");
    console.log(typeOfTest);
    var gameInfo = new FormData();
    gameInfo.append("type", typeOfTest);
    gameInfo.append("num", numTest);
    console.log(gameInfo);
    request({ url: "/runGameTest", method: "POST", body: gameInfo }) //body:type and more to come...
        .then(data => {
            console.log("New game of type " + gameInfo.type + " has been created.");
            console.log("Game running...");
            var results = JSON.parse(data);
            console.log("Results: \n");
            console.log(results);
            var cleanResults = JSON.stringify(results);
            cleanResults = cleanResults.replace(",", " ");
            cleanResults = cleanResults.replace("\\n\"", "<br> ");
            cleanResults = cleanResults.replace("[", "");
            cleanResults = cleanResults.replace("]", "");
            outputHandle.innerHTML = cleanResults;
        })
        .catch(error => {
            console.log(error);
        });
}

function request(obj) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();

        xhr.open(obj.method || "GET", obj.url);
       // xhr.setRequestHeader("Content-type", "application/json")

        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(xhr.response);
            } else {
                reject(xhr.statusText);
            }
        };
        xhr.onerror = () => reject(xhr.statusText)

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

                        if (res[i][j] != null) {
                            tester = tester + res[i][j] + "<br>";
                        }
                    }


                }
                document.getElementById("bikeListOutput").innerHTML = tester;
                console.log(tester);
            }
        })
        .catch(error => {
            result.innerHTML += "Could not find any games."
        });

    document.getElementById("bikeListOutput").value += "";
}

function createGame() {
    var newGameName = document.getElementById("userGameName").value;
    var bikes="";
    
    for (i = 0; i < newGameBikeList.length; i++) {
       
        console.log(newGameBikeList[i]);
        
    }
   
    for (i = 0; i < newGameBikeList.length; i++) {
       
        bikes = bikes + ":"+newGameBikeList[i];
        
    }
    while(bikes.charAt(0) === ':')
{
 bikes = bikes.substr(1);
}
    
   
    console.log(bikes+"bikes");

    
    request({ url: "/createGame?gameNameAndBikes=" + newGameName + "/"+bikes, method: "POST"}) 
        .then(data => {
            console.log("New game " + data + "has been created. Cool")
        })
        .catch(error => {
            console.log(error);
        });
}

function createGame8(){
    var newGameName = document.getElementById("userGameName").value;
    
}

function initialize() {
    console.log("intializing game...");
    request({ url: "/getGrid", method: "GET" })
        .then(data => {
            var cleanData = data.JSON.parse();
            console.log(cleanData);
        })
        .catch(error => {
            console.log(error);
        });
}

function selectBike() {
    var bikeID = document.getElementById("userBikes").value;
    console.log(bikeID);
    newGameBikeList.push(bikeID);
    document.getElementById("currentBikes").innerHTML+="<br>"+bikeID;

}

function selectGame() {
    var gameid = document.getElementById("userGameId");
    request({ url: "/selectGame?gameid=" + gameid, method: "POST" })

}