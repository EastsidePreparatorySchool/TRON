let result = document.getElementById("result");
let name = document.getElementById("name");

name.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) { //enter key: 13
        event.preventDefault();
        document.getElementById("dump").click();
    }
});

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

//dump table function
function dump() {
    name = document.getElementById("name");

    result.innerHTML = name.value + ": <hr>";

    request({ url: "/dumpTable?name=" + name.value, method: "GET" })
        .then(data => {
            if (data.length != null) {
                let res = JSON.parse(data);
                console.log(res);

                for (var i = 0; i < res.length; i++) {
                    for (var j = 0; j < res[0].length; j++) {
                        result.innerHTML += res[i][j] + " ";
                    }
                    result.innerHTML += "<br>";
                }
            }
        })
        .catch(error => {
            result.innerHTML += "no such table. try again"
        });

    document.getElementById("name").value = "";
}