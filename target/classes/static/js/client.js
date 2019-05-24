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
var unit = gridDivisions/gridSize; //sidelength of one square on the grid
var gridCenterColor = 0x66ffd9; //0x444444;
var gridColor = 0x668cff; //0x888888;
var paththickness = 0.5; //thickness of light trail


// orbit control stuff
var controls = new THREE.OrbitControls(camera);
controls.enableZoom = true;
controls.maxDistance = out*3; 
controls.minDistance = out/30;
controls.keys = { //move around center of grid with keys
	LEFT: 37, //left arrow
	UP: 38, // up arrow
	RIGHT: 39, // right arrow
	BOTTOM: 40 // down arrow
}

//setting and adding camera
camera.position.set(0, out*1.2, out/1.5);
var cameraFocus = new THREE.Vector3(100, 0, 0);
camera.lookAt(cameraFocus);
controls.update(); //have to update controls after camera is changed
scene.add(camera);
renderer.setSize(WIDTH, HEIGHT);
container.appendChild(renderer.domElement);


//now the game starts
bikes = []; //store IDs of each bike in the game
testpath = [[1, 1], [1, 2], [1, 3]]; //test path with three points

class Bike {
    constructor(material, startx, starty, id, species, lightpath) { //lightpath is a double array
        this.material = material;
        this.mesh = new THREE.Mesh(cubeGeo, this.material);
        scene.add(this.mesh);
        this.mesh.position.x = startx; //an initial x, y
        this.mesh.position.y = starty;
        this.mesh.position.z = 0; //no 3D?
        this.id = id; //every bike will have a unique id
        this.species = species; //may be multiple bikes of the same species
        this.lightpath = lightpath; //this will be an array of [x, y] pairs
    }
    kill() {
        scene.remove(this.mesh);
        delete bikes[this.id];
    }
}

var pathgeo = new THREE.CubeGeometry(unit, unit, )
pathmaterial = MeshLambertMaterial();
pathmate

class lightpath {
    constructor (material, patharray) {
        this.material = MeshLambertMaterial; //maybe don't put material in the constructor, unless it't the only way to change color
        this.path = patharray;
        this.mesh = new THREE.Mesh(cubeGeo, this.material); 
    }
}


//grid helper constructor: (size, divisions, colorCenterLine, colorGrid)
var grid = new THREE.GridHelper (gridSize, gridDivisions, gridCenterColor, gridColor);
scene.add (grid);

// bike stuff
const RADIUS = unit/2;
const SEGMENTS = 16;
const RINGS = 16;
const sphereMaterial = new THREE.MeshLambertMaterial({color: 0xCC0000});
const fakebike = new THREE.Mesh(new THREE.SphereGeometry(RADIUS, SEGMENTS, RINGS), sphereMaterial);
fakebike.position.x = unit/2;
fakebike.position.z = unit/2;
scene.add(fakebike);








//

const pointLight = new THREE.PointLight(0xFFFFFF);
pointLight.position.x = 10;
pointLight.position.y = 50;
pointLight.position.z = 130;
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

//routes I need should go here
function updateBikes() {
    request({url: "updateBikes", method: "GET"})
        .then(data => {
            //something
        })
        .catch(error => {
            print("Bike updata error: " + error);
        });
}

//more routes??


//RENDERING AND UPDATING
renderer.render(scene, camera);

function animate () {
    renderer.render(scene, camera);
    controls.update();
    requestAnimationFrame(animate); //Schedule the next frame.
}

requestAnimationFrame(animate); // Schedule the first frame.