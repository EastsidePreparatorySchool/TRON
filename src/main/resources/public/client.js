
//----------------------------------------CAMERA, LIGHT, CONTROL, GRID------------------------------------------

//viewing and rendering variables
const WIDTH = window.innerWidth; //change these back!!
const HEIGHT = window.innerHeight;
console.log('width: ' + WIDTH + ' height: ' + HEIGHT);
const VIEW_ANGLE = 45;
const ASPECT = WIDTH / HEIGHT;
const NEAR = 0.1;
const FAR = 500;
var out = 100; //how far out camera is, and also how big grid is
//const container = document.querySelector('#container');
const renderer = new THREE.WebGLRenderer();
var camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
const scene = new THREE.Scene();

//grid and size variables
var gridSize = out; //sidelength of full grid
var gridDivisions = 100; //how many squares along a side
var unit = gridDivisions / gridSize; //sidelength of one square on the grid
var gridCenterColor = 0x00e6e6; //0x444444;
var gridColor = 0x00e6e6; //0x888888;
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
document.body.appendChild(renderer.domElement);

var grid = new THREE.GridHelper(gridSize, gridDivisions, gridCenterColor, gridColor);
scene.add(grid);

const pointLight = new THREE.PointLight(0xFFFFFF);
pointLight.position.x = 10;
pointLight.position.y = 130;
pointLight.position.z = 50;
scene.add(pointLight);

//--------------------------------------------LIGHT TRAIL--------------------------------------------------------

var allpaths = []; //2D array of all coordinates, put the path meshes into the right boxes in the array so I can find and delete them later
var fakeIDs = [1, 21, 15, 3]; //FAKE FOR TESTING COLOR STUFF
var IDs = fakeIDs; //change to empty

var bikeColors = [0x9900ff, 0x99ff33, 0xff0066, 0xff00ff]; //four pre set colors

var pathgeo = new THREE.CubeGeometry(unit, paththickness, unit); //one unit of the path geometry
var pathmat = new THREE.MeshLambertMaterial({ color: 0xff0066 }); //, ambient: 0x121212
var pathmesh = new THREE.Mesh(pathgeo, pathmat);

function drawpath(patharray, color) { //change color to ID???
    //var color = getcolor(ID); //use this if want to use ID instead
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

function getindex (ID) {
    var index = IDs.findIndex((currentID) => currentID == ID );
    return index;
}

function getcolor (ID) {
    var index = IDs.findIndex((currentID) => currentID == ID ); //NEED TO HAND COMPARATOR THE ID YOURE LOOKING FOR
    return bikeColors[index];
}

class Path {
    constructor(patharray, ID) {
        this.patharray = patharray;
        this.ID = ID;
        this.color = getcolor(ID); 
    }
    draw() {
        var pathgeo = new THREE.CubeGeometry(unit, paththickness, unit); //one unit size of the path geometry
        var pathmat = new THREE.MeshLambertMaterial({ color: this.color }); 
        //console.log("draw: " + this.patharray);
        var pathlength = this.patharray.length;
        var i;
        for (i = 0; i < pathlength; i++) {
            var mesh = new THREE.Mesh(pathgeo, pathmat);
            mesh.position.x = this.patharray[i][0] - unit / 2; 
            mesh.position.z = this.patharray[i][1] - unit / 2;
            scene.add(mesh); 
            var x = this.patharray[i][0] + gridDivisions/2;
            var y = this.patharray[i][1] + gridDivisions/2;
            //console.log("index x: " + x + " and y: " + y);
            if( allpaths[x] == null ) allpaths[x] = [];
            allpaths[x][y] = mesh; //add the boi to its coordinate in the path array
        }
    }
    kill() {
        var pathlength = this.patharray.length;
        var i;
        for (i = 0; i < pathlength; i++) {
            var x = this.patharray[i][0] + gridDivisions/2;
            var y = this.patharray[i][1] + gridDivisions/2;
            var pathunit = allpaths[x][y];
            scene.remove(pathunit); //remove it from the scene
            allpaths[x][y] = null; //removes it from the storage array
        }
    }
}

var testpath = [[-10, 9], [-10, 8], [-9, 8], [-9, 7], [-9, 6], [-8, 6], [-7, 6], [-6, 6], [-5, 6], [-4, 6], [-3, 6], [-3, 5], [-2, 5]]; //test path with kinda random points

var path1 = new Path (testpath, 3);
path1.draw();
//path1.kill();

//-----------------------------------------------BIKE------------------------------------------------------------

var bikes = []; //the actual bike meshes go here with the right indices 
var numbers = [1, 2, 3, 4, 5]; //array with just numbers

class Bike {
    constructor(ID) {
        this.ID = ID;
        this.color = getcolor(ID);
        this.x;
        this.y;
    }
    draw(x, y) {
        const RADIUS = unit / 2;
        const SEGMENTS = 16;
        const RINGS = 16;
        const sphereMaterial = new THREE.MeshLambertMaterial({ color: this.color });
        const simplebike = new THREE.Mesh(new THREE.SphereGeometry(RADIUS, SEGMENTS, RINGS), sphereMaterial);
        simplebike.position.x = x - unit / 2;
        simplebike.position.z = y - unit / 2;
        scene.add(simplebike);
        bikes[getindex(this.ID)] = simplebike;
    }
    update(x, y) {
        bikes[getindex(this.ID)].position.x = x - unit / 2;
        bikes[getindex(this.ID)].position.z = y - unit / 2;
    }
    kill() {
        scene.remove(bikes[getindex(this.ID)]);
        bikes[getindex(this.ID)] = null;
    }
}
var bike1 = new Bike (3);
bike1.draw(10, 20);
bike1.update(20, -30);
bike1.kill();


//-------------------------------------------------ROUTES------------------------------------------------------------

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

var testbikes = [["test1", true, [50, 30], [50, -20]], ["test2", false, [-40, 30], [30, 30]]]; //faye's test array -- pretend the first point is where the bike actually is and the rest are the new trails

console.log("what I should get from faye: " + testbikes);

function initializeBikeTest() { //this is a method to just test parsing a bike array from Faye 
    request({ url: "/updateBikeTest", method: "GET" })
        .then(data => {
            bikes = JSON.parse(data); //given a test array with two bike data object
            console.log("what I actually get from faye: " + bikes);
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
                //var bikecolor
                for (j = 2; j < numPositions; j++) {
                    bikePositions[i][j - 2] = bikes[i][j]; //load positions into positions array
                }
                //send off bike positions to be plotted
                drawpath(bikePositions[i], bikeColors[i]); //purple just to test
            }

        })
        .catch(error => {
            console.log("Bike update error: " + error);
        });
}

function initializeBikes() { //function takes a "compact log" from Faye (form: "["name", true, [bikex, bikey], [trail points]]) and draws it
    request({ url: "/updateBikeTest", method: "GET" })
        .then(data => {
            bikes = JSON.parse(data); //given a test array with two bike data object
            console.log("what I actually get from faye: " + bikes);
            var numBikes = bikes.length;
            var bikeNames = []; //bike names will be test1 and test2
            var bikePositions = Array(numBikes); //with sub arrays of points

            var i;
            var j;
            for (i = 0; i < numBikes; i++) {
                var numPositions = bikes[i].length; //the two at the beginning are name and aliveness
                //console.log(numPositions);
                bikeNames[i] = bikes[i][j];
                bikePositions[i] = [];
                //var bikecolor
                for (j = 2; j < numPositions; j++) {
                    bikePositions[i][j - 2] = bikes[i][j]; //load positions into positions array
                }
                //send off bike positions to be plotted
                drawpath(bikePositions[i], bikeColors[i]); //purple just to test
            }

        })
        .catch(error => {
            console.log("Bike update error: " + error);
        });
}



var bikeUpdates;

//make a direction function so I can ask what direction someone is going in and draw their bike correctly

var events; //queue of all events -- use later if rest works

function updateBikes() { //takes either a "DEATH" (form: [DEATH, int bikeID]) or "POSUPDATE" (form: [POSUPDATE, int bikeID, [x, y]])
    request({ url: "/updateBikes", method: "GET" })
        .then(data => { 
            var update = JSON.parse(data); //given a test array with two bike data object
            console.log("bike update: " + update);

            // ["POSUPDATE", int id, int[] pos]
            // ["DEATH", int id, null]

            if (update[0] == "POSUPDATE") {
                var ID = update[1];
                var newpoint = update[2];
                var index = getindex(ID);
                var path = new Path (newpoint, ID);
                path.draw();
                bikes[index].update(newpoint[0], newpoint[1]);
            } else if (update[0] == "DEATH"){
                bikes[getindex(update[1])].kill();
                allpaths[getindex(update[1])].kill();
            }
            //updateBikes(); //call update function again after last update finishes
        })
        .catch(error => {
            console.log("Bike update error: " + error);
        });
}

function updateBikeTest() { //takes either a "DEATH" (form: [DEATH, int bikeID]) or "POSUPDATE" (form: [POSUPDATE, int bikeID, [x, y]])
    request({ url: "/updateBikes", method: "GET" })
        .then(data => { 
            var update = JSON.parse(data); //given a test array with two bike data object
            console.log("bike update: " + update);

            // ["POSUPDATE", int id, int[] pos]
            // ["DEATH", int id, null]

            if (update[0] == "POSUPDATE") {
                var ID = update[1];
                var newpoint = update[2];
                var index = getindex(ID);
                var path = new Path (newpoint, ID);
                path.draw();
                bikes[index].update(newpoint[0], newpoint[1]);
            } else if (update[0] == "DEATH"){
                bikes[getindex(update[1])].kill();
                allpaths[getindex(update[1])].kill();
            }
            //updateBikes(); //call update function again after last update finishes
        })
        .catch(error => {
            console.log("Bike update error: " + error);
        });
}



//------------------------------------------RENDERING AND UPDATING----------------------------------------------------

renderer.render(scene, camera);

function animate() {
    renderer.render(scene, camera);
    controls.update();
    requestAnimationFrame(animate); //Schedule the next frame.
}

requestAnimationFrame(animate); // Schedule the first frame.








//-----------------------------------------------------GARBAGE---------------------------------------------------------

//nasty sauce things that I'll probably never use again:

/* 


function updateBikeTest2() { //this one without a url request just so I can test it here
    bikes = testbikes; //testbikes;
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
        simpleBike(bikes[i][2][0], bikes[i][2][1], bikeColors[i]); //draw a simple sphere bike

        for (j = 3; j < numPositions; j++) {
            bikePositions[i][j - 3] = bikes[i][j]; //load positions into positions array
        }
        //send off bike positions to be plotted
        drawpath(bikePositions[i], bikeColors[i]); //purple just to test
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

*/