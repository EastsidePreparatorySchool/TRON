//requests xmlhttprequest wrapped in promise
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

//initialize
function initialize() {
    console.log("intializing game...");
    request({ url: "/getGrid", method: "GET" })
        .then(data => {
            //parse that yes
        })
        .catch(error => {
            console.log(error);
        });
    request({ url: "/getBikeSpawn", method: "GET" })
        .then(data => {
            //parse that yes
        })
        .catch(error => {
            console.log(error);
        });
}

//smol updates
function update() {
    console.log("updating...");
    request({ url: "/updateBikes", method: "GET" })
        .then(data => {
            //parse that yes
        })
        .catch(error => {
            console.log(error);
        });
}