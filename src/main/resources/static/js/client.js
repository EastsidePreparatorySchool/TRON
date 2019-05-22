//client side of TRON

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

camera.position.set(0, out*1.2, out/1.5);
var cameraFocus = new THREE.Vector3(100, 0, 0);
camera.lookAt(cameraFocus);
controls.update(); //have to update controls after camera is changed

scene.add(camera);
renderer.setSize(WIDTH, HEIGHT);
container.appendChild(renderer.domElement);


//now the game starts
bikes = []; //store IDs of each bike in the game

class Bike {
    constructor(material, startx, starty, id, species, lightpath) { //lightpath is a double array
        this.mat = material;
        this.mesh = new THREE.Mesh(cubeGeo, this.mat);
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

// grid stuff
var gridSize = out; //sidelength of full grid
var gridDivisions = 100; //how many squares along a side
var unit = gridDivisions/gridSize; //sidelength of one square on the grid
var gridCenterColor = 0x66ffd9; //0x444444;
var gridColor = 0x668cff; //0x888888;

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

renderer.render(scene, camera);

function update () {
    renderer.render(scene, camera);
    controls.update();
    requestAnimationFrame(update); //Schedule the next frame.
}

requestAnimationFrame(update); // Schedule the first frame.