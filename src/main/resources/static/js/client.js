//client side of TRON

const WIDTH = 1500; //innerWidth; //change these back!!
const HEIGHT = 700; //innerHeight;

const VIEW_ANGLE = 45;
const ASPECT = WIDTH / HEIGHT;
const NEAR = 0.1;
const FAR = 10000;

const container = document.querySelector('#container');

const renderer = new THREE.WebGLRenderer();
var camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
var controls = new THREE.OrbitControls(camera);
const scene = new THREE.Scene();

camera.position.set(0, 10, 10);
var cameraFocus = new THREE.Vector3(0, 0, 0);
camera.lookAt(cameraFocus);
controls.update();

scene.add(camera);
renderer.setSize(WIDTH, HEIGHT);
container.appendChild(renderer.domElement);


//now the game starts
bikes = []; //store IDs of each bike in the game

class Bike {
    constructor(material, startx, starty, id, lightpath) { //lightpath is a double array
        this.mat = material;
        this.mesh = new THREE.Mesh(cubeGeo, this.mat);
        scene.add(this.mesh);
        this.mesh.position.x = startx; //an initial x, y
        this.mesh.position.y = starty;
        this.mesh.position.z = 0; //no 3D?
        this.id = id; //maybe like 3 bikes
        this.lightpath = lightpath; //this will be an array of [x, y] pairs
    }
    kill() {
        scene.remove(this.mesh);
        delete bikes[this.id];
    }
}


var gridSize = 10;
var gridDivisions = 100;
var gridCenterColor = 0x66ffd9; //0x444444;
var gridColor = 0x668cff; //0x888888;

//grid helper constructor: (size, divisions, colorCenterLine, colorGrid)
var grid = new THREE.GridHelper (gridSize, gridDivisions, gridCenterColor, gridColor);
scene.add (grid);






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