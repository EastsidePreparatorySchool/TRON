//client side of TRON

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