//client side of TRON

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

//grid and size variables
var gridSize = out; //sidelength of full grid
var gridDivisions = 100; //how many squares along a side
var unit = gridDivisions / gridSize; //sidelength of one square on the grid
var gridCenterColor = 0x66ffd9; //0x444444;
var gridColor = 0x668cff; //0x888888;
var paththickness = 5; //thickness of light trail


// orbit control stuff
var controls = new THREE.OrbitControls(camera);
controls.enableZoom = true;
controls.maxDistance = out * 3;
controls.minDistance = out / 30;
controls.keys = { //move around center of grid with keys
    LEFT: 37, //left arrow
    UP: 38, // up arrow
    RIGHT: 39, // right arrow
    BOTTOM: 40 // down arrow
}

//setting and adding camera
camera.position.set(0, out * 1.2, out / 1.5);
var cameraFocus = new THREE.Vector3(100, 0, 0);
camera.lookAt(cameraFocus);
controls.update(); //have to update controls after camera is changed
scene.add(camera);
renderer.setSize(WIDTH, HEIGHT);
container.appendChild(renderer.domElement);


//now the game starts
var bikes = []; //store IDs of each bike in the game
var testpath = [[-10, 9], [-10, 8], [-9, 8], [-9, 7], [-9, 6], [-8, 6], [-7, 6], [-6, 6], [-5, 6], [-4, 6], [-3, 6], [-3, 5], [-2, 5]]; //test path with kinda random points
var numbers = [1, 2, 3, 4, 5]; //array with just numbers

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


var pathgeo = new THREE.CubeGeometry(unit, paththickness, unit); //one unit of the path geometry
var pathmat = new THREE.MeshLambertMaterial({ color: 0xff0066 }); //, ambient: 0x121212

var pathmesh = new THREE.Mesh(pathgeo, pathmat);
/*
pathmesh.position.x = unit/2;
pathmesh.position.z = unit/2;
scene.add(pathmesh); */

function drawpath(patharray, color) {
    var pathgeo = new THREE.CubeGeometry(unit, paththickness, unit); //one unit of the path geometry
    var pathmat = new THREE.MeshLambertMaterial({ color: color }); //, ambient: 0x121212
    var pathlength = patharray.length;
    var i;
    for (i = 0; i < pathlength; i++) {
        var mesh = new THREE.Mesh(pathgeo, pathmat);
        mesh.position.x = patharray[i][0] - unit / 2; //HAS PROBLEMS HERE
        mesh.position.z = patharray[i][1] - unit / 2;
        scene.add(mesh);
    }
}

drawpath(testpath, 0xff0066);




//grid helper constructor: (size, divisions, colorCenterLine, colorGrid)
var grid = new THREE.GridHelper(gridSize, gridDivisions, gridCenterColor, gridColor);
scene.add(grid);

// fake bike stuff
/*const RADIUS = unit / 2;
const SEGMENTS = 16;
const RINGS = 16;
const sphereMaterial = new THREE.MeshLambertMaterial({ color: 0xCC0000 });
const fakebike = new THREE.Mesh(new THREE.SphereGeometry(RADIUS, SEGMENTS, RINGS), sphereMaterial);
fakebike.position.x = unit / 2;
fakebike.position.z = unit / 2; */

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

//scene.add(fakebike);

//

const pointLight = new THREE.PointLight(0xFFFFFF);
pointLight.position.x = 10;
pointLight.position.y = 130;
pointLight.position.z = 50;
scene.add(pointLight);



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

function updateBikeTest() { //initialize bikes
    request({ url: "updateBikeTest", method: "GET" })
        .then(data => {
            bikes = JSON.parse(data); //given a test array with two bike data object
            var numBikes = bikes.length;
            var bikeNames = []; //bike names will be test1 and test2
            var bikePositions = Array(numBikes); //with sub arrays of points
            var bikeColors = [0x9900ff, 0x99ff33, 0xff0066]; //make this better later

            var i;
            var j;
            for (i = 0; i < numBikes; i++) {
                var numPositions = bikes[i].length; //the two at the beginning are name and aliveness
                //console.log(numPositions);
                bikeNames[i] = bikes[i][j];
                bikePositions[i] = [];
                var bikecolor
                for (j = 2; j < numPositions; j++) {
                    bikePositions[i][j - 2] = bikes[i][j]; //load positions into positions array
                }
                //send off bike positions to be plotted
                drawpath(bikePositions[i], bikeColors[i]); //purple just to test
            }

        })
        .catch(error => {
            print("Bike update error: " + error);
        });
}

var testbikes = [["test1", true, [50, 30], [50, -20]], ["test2", false, [-40, 30], [30, 30]]]; //faye's test array -- pretend the first point is where the bike actually is and the rest are the new trails
console.log(testbikes.length);



function updateBikeTest2() { //this one without a url request just so I can test it here
    bikes = testbikes; //testbikes;
    var numBikes = bikes.length;
    var bikeNames = []; //bike names will be test1 and test2
    var bikePositions = Array(numBikes); //with sub arrays of points
    var bikeColors = [0x9900ff, 0x99ff33, 0xff0066]; //make this better later

    /*var c; //make some pretty random colors later: 
    for (c = 0; c < numBikes; c++) {
        var rand = Math.floor(Math.random() * 100000000);
        console.log(rand);
        // (0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6)
        bikeColors[c] = '0x'+rand.toString(16).substr(1,6);
    } */

    var i;
    var j;
    for (i = 0; i < numBikes; i++) {
        var numPositions = bikes[i].length; //the two at the beginning are name and aliveness
        //console.log(numPositions);
        bikeNames[i] = bikes[i][j];
        bikePositions[i] = [];
        simpleBike(bikes[i][2][0], bikes[i][2][1], bikeColors[i]); //draw a simple sphere bike

        for (j = 3; j < numPositions; j++) {
            bikePositions[i][j - 3] = bikes[i][j]; //load positions into positions array
        }
        //send off bike positions to be plotted
        drawpath(bikePositions[i], bikeColors[i]); //purple just to test
    }
}

updateBikeTest2();

//already have a bike[] initialized at the top



function initializeBikes() { //initialize bikes
    request({ url: "initializeBikes", method: "GET" })
        .then(data => {
            bikes = JSON.parse(data); //an array of initial bike objects with position, ID
            //for some number of bikes
            //construct a new bike with some ID and assign it a color
            //figure out bike headings for STL, rotate bikes to drive straight
            //draw the bikes
            var numBikes = bikes.length;
            var bikeIDs;
            var bikeColors; //do this later
            var i;
            for (i = 0; i < numBikes; i++) {
                //make and draw bikes
                bikeIDs[i] = bikes[i][0];

            }
        })
        .catch(error => {
            print("Bike update error: " + error);
        });
}



var bikeUpdates;

function updateBikes() {
    request({ url: "updateBikes", method: "GET" })
        .then(data => {
            bikeUpdates = JSON.parse(data);
        })
        .catch(error => {
            print("Bike update error: " + error);
        });
}





//going to recieve:
// bike values
// deaths
// trails

//[[bike0, true/false, bikeposition, [points]], [bike1, true/false, bike position, [points]]];
// true/false applies to if it is alive


//RENDERING AND UPDATING
renderer.render(scene, camera);

function animate() {
    renderer.render(scene, camera);
    controls.update();
    requestAnimationFrame(animate); //Schedule the next frame.
}

requestAnimationFrame(animate); // Schedule the first frame.