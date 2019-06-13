//client side of TRON

//CONSTANTS AND Three.js:
//viewing and rendering variables
const WIDTH = 700; //innerWidth; //change these back!!
const HEIGHT = 700; //innerHeight;
const VIEW_ANGLE = 45;
const ASPECT = WIDTH / HEIGHT;
const NEAR = 0.1;
const FAR = 10000;
var out = 100; //how far out camera is, and also how big grid is
const container = document.querySelector('#container');
const renderer = new THREE.WebGLRenderer();
var camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
const scene = new THREE.Scene();

//CONSTANTS
var gridSize = out; //sidelength of full grid
var gridDivisions = 100; //how many squares along a side
var unit = gridDivisions / gridSize; //sidelength of one square on the grid
var gridCenterColor = 0x66ffd9; //0x444444;
var gridColor = 0x668cff; //0x888888;
var wallThickness = 5; //thickness of light trail
var bikeRadius = 2;//bikes are just spheres

var wallgeo = new THREE.CubeGeometry(unit, wallThickness, unit); //one unit of the path geometry
var wallmat = new THREE.MeshLambertMaterial({ color: 0x121212 }); //ambient: 0x121212

var bikemat = new THREE.MeshLambertMaterial({ color: 0xFF2800 });//ferrari red!!!!!!!
var bikegeo = new THREE.SphereGeometry(bikeRadius, 8, 8);
var bikemesh = new THREE.Mesh(bikegeo, bikemat);

var grid = new THREE.GridHelper(gridSize, gridDivisions, gridCenterColor, gridColor);
scene.add(grid);

//lighting
const pointLight = new THREE.PointLight(0xFFFFFF);
pointLight.position.x = 10;
pointLight.position.y = 130;
pointLight.position.z = 50;
scene.add(pointLight);


//Three.js:
// orbit control stuff
var controls = new THREE.OrbitControls(camera);
controls.enableZoom = true;
controls.maxDistance = out * 3;
controls.minDistance = out / 30;
// controls.keys = { //move around center of grid with keys
//     LEFT: 37, //left arrow
//     UP: 38, // up arrow
//     RIGHT: 39, // right arrow
//     BOTTOM: 40 // down arrow

// }

//setting and adding camera
camera.position.set(0, out * 1.2, out / 1.5);
var cameraFocus = new THREE.Vector3(100, 0, 0);
camera.lookAt(cameraFocus);
controls.update(); //have to update controls after camera is changed
scene.add(camera);
renderer.setSize(WIDTH, HEIGHT);
container.appendChild(renderer.domElement);

//RENDERING AND UPDATING
renderer.render(scene, camera);

function animate() {
    renderer.render(scene, camera);
    controls.update();
    requestAnimationFrame(animate); //Schedule the next frame.
}

requestAnimationFrame(animate); // Schedule the first frame.


//now the game starts
var bikes = []; //store IDs of each bike in the game
var testpath = [[-10, 9], [-10, 8], [-9, 8], [-9, 7], [-9, 6], [-8, 6], [-7, 6], [-6, 6], [-5, 6], [-4, 6], [-3, 6], [-3, 5], [-2, 5]]; //test path with kinda random points
var numbers = [1, 2, 3, 4, 5]; //array with just numbers because why not

class Bike {
    constructor(material, startx, starty, id, species, lightpath) { //lightpath is a double array
        this.material = material;
        this.mesh = new THREE.Mesh(cubeGeo, this.material);
        scene.add(this.mesh);
        this.mesh.position.x = startx; //an initial x, y
        this.mesh.position.z = starty; //this is the y on the grid
        this.mesh.position.y = 0; //up from grid
        this.id = id; //every bike will have a unique id
        this.species = species; //may be multiple bikes of the same species
        this.lightpath = lightpath; //this will be an array of [x, y] pairs
    }
    kill() {
        scene.remove(this.mesh);
        delete bikes[this.id];
    }
}

function drawGrid(grid) {
    var height = grid.length;
    var width = grid[0].length;
    console.log("trying to draw a grid with dimensions " + height + ", " + width);

    for (var i = 0; i < height; i++) {
        for (var j = 0; j < width; j++) {
            console.log("i is " + i + ", j is " + j + ".");
            //console.log(grid[i][j]);
            if (grid[i][j] == 0) {
                break;
            } else if (grid[i][j] == 1) {//bikes
                console.log("found a bike!!")
                var mesh = new THREE.Mesh(bikegeo, bikemat);
            } else if (grid[i][j] == 2) {//walls or trails
                var mesh = new THREE.Mesh(wallgeo, wallmat);
            }
            mesh.position.x = grid[i][j] - unit / 2; //HAS PROBLEMS HERE ~ Audrey
            mesh.position.z = grid[i][j] - unit / 2; //yay thanks I love debugging JS ~ Trajan
            //i wanna join the party ~ Faye
            scene.add(mesh);
        }
    }
}

function simpleBike(x, y, color) {
    const RADIUS = unit / 2;
    const SEGMENTS = 16;
    const RINGS = 16;
    const sphereMaterial = new THREE.MeshLambertMaterial({ color: color });
    const simplebike = new THREE.Mesh(new THREE.SphereGeometry(RADIUS, SEGMENTS, RINGS), sphereMaterial);
    simplebike.position.x = x - unit / 2;
    simplebike.position.z = y - unit / 2;
    scene.add(simplebike);
}

//functions and routes
function request(obj) {
    return new Promise((resolve, reject) => {
        let xhr = new XMLHttpRequest();
        xhr.open(obj.method || "GET", obj.url); //starting to send data to the server

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

//TODO: finish updates serverside then here
function updateGrid() {
    request({ url: "/getBoard", method: "GET" })
        .then(data => {
            var board = JSON.parse(data);
            drawGrid(board); //just to test
        })
        .catch(error => {
            console.log("Bike update error: " + error);
        });
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
                            tester = tester + res[i][j] + " ";
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

function runGame() {
    var newGameName = document.getElementById("userGameName");
    console.log(newGameName);
    var bikes = {
        nameBikeList: JSON.stringify(newGameBikeList)
    };
    //xmlhttp.setRequestHeader("Content-type", "application/json");
    request({ url: "/runGame", method: "POST", body: bikes }) //body:bikes
        .then(data => {
            console.log("New game " + data + "has been created. Cool")
        })
        .catch(error => {
            console.log(error);
        });
}

//do this once please...
function initialize() {
    console.log("intializing game...");
    request({ url: "/getBoard", method: "GET" })
        .then(data => {
            var cleanData = data.JSON.parse();
            console.log(cleanData);
        })
        .catch(error => {
            console.log(error);
        });
}

function frameStep() {
    console.log("one frame!");
    request({ url: "/getBoard", method: "GET" })//need to either make this post or use URL params
        .then(data => {
            var cleanData = JSON.parse(data);
            drawGrid(cleanData);
            console.log(cleanData);
        })
        .catch(error => {
            console.log(error);
        });
}

function frameContinuous() {
    console.log("get me ALL THE UPDATES");
    //lambdas inside lambdas are fUN
    setInterval(function () {
        request({ url: "/getBoard", method: "GET" })
            .then(data => {
                var cleanData = data.JSON.parse();
                console.log(cleanData);
            })
            .catch(error => {
                console.log(error);
            });
    }, 20);
}

function startGame() {
    console.log("starting game...")
    request({ url: "/startGame", method: "GET" })//makes a presetgame
        .then(data => {
            console.log("Game started successfully!");
        })
        .catch(error => {
            console.log(error);
        });
}