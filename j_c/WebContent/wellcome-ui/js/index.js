const mint = '#aff1da';
const chenin = {
	regular: '#e2d77f',
	dark10: '#d9ca55',
	dark15: '#d4c441',
	dark25: '#b8a82a'
}
const chiffon = {
	regular: '#fffcc9',
	light5: '#fffffc',
	dark5: '#fcf8b2',
	dark15: '#faf481',
	dark25: '#f9ef50',
	dark30: '#ede442'
};
const pastel = '#5cde83';
const pastel2 = '#76DE5C';
const emerald = '#52c775';
const shamrock = '#32D563';
const eigengrau = {
	regular: '#16161d',
	light30: '#585874'
}
const supernova = '#ecbb14';
const cerise = '#c73e8d';
const soil = '#a4764a';
const grassColors = [
	pastel,
	pastel2,
	emerald,
	shamrock
];

const TAU = Zdog.TAU;
const groundRadius = 200;

const randomInt = (min, max) => Math.floor(Math.random() * (max - min + 1) + min);
const checkCircularBoundary = (x,y,d) => Math.pow((0 - x),2) + Math.pow((0 - y),2) <= Math.pow(d,2) ? true : false;
const pointInCircle = (d) => {
	let angle = Math.random() * 2 * Math.PI;
	let rsq = Math.random() * Math.pow((d/2),2);
	let x = Math.floor(Math.sqrt(rsq) * Math.cos(angle));
	let y = Math.floor(Math.sqrt(rsq) * Math.sin(angle));
	
	return [x,y]
}

function makeNatureScene(coords,i) {
	
	this.makeFlower(coords);
	this.makeGrass(coords);
}

function makeFlower(coords) {
	
	do {
		coords = pointInCircle((groundRadius * 2 - 10));
	} while (checkCircularBoundary(coords[0],coords[1],75))
		
	let flower = new Zdog.Anchor({
		addTo: sceneGroup,
		rotate: {
			x: TAU/4,
			z: randomInt(0,360)
		},
		translate: {
			x: coords[0],
			y: 38,
			z: coords[1]
		}
	});
	
	let flowerGroup = new Zdog.Group({
		addTo: flower,
	});
	
	let petal = new Zdog.Ellipse({
		addTo: flowerGroup,
		backface: false,
		color: cerise,
		diameter: 5,
		fill: true
	});
	
	let petalInner = new Zdog.Ellipse({
		addTo: petal,
		backface: false,
		color: supernova,
		diameter: 2,
		fill: true
	});
	
	petal.copyGraph({
		translate: {
			x: 5
		}
	});
	
	petal.copyGraph({
		translate: {
			x: 2.5,
			y: 4
		}
	});
}

function makeGrass(coords) {
	let path = [];
	let pathY = -5;

	for (i = 0; i < 9; i ++) {
		path.push(
			{x: i, y: i%2 === 0 ? pathY : 0, z: 0}
		);
		(i > 3) ? pathY++ : pathY--;
	}
	
	let grass = new Zdog.Anchor({
		addTo: sceneGroup,
		rotate: {
			y: randomInt(0,360)
		},
		translate: {
			x: coords[0],
			y: 36,
			z: coords[1]
		}
	});
	
	let grassPath = new Zdog.Shape({
		addTo: grass,
		color: grassColors[randomInt(0,(grassColors.length-1))],
		closed: false,
		path: path
	});
}

let scene = new Zdog.Illustration({
	element: '#scene',
	zoom: 2,
	rotate: { 
		y: -TAU/8,
		x: -TAU/16
	},
	// dragRotate: true,
  resize: 'fullscreen',
	onResize: function( width, height ) {
		let minSize = Math.min( width, height );
		this.zoom = minSize / 500;
  },
});

let sceneGroup = new Zdog.Group({
	addTo: scene,
	updateSort: true
});

let grass = new Zdog.Cylinder({
	addTo: scene,
	color: soil,
	fill: true,
	diameter: groundRadius*2,
	length: 38,
	rotate: {
		x: TAU/4
	},
	translate: {
		y: 60
	},
	frontFace: mint,
	backface: eigengrau.regular
});

for (let i = 0; i < 10; i++) {
	makeFlower(pointInCircle((groundRadius * 2 - 20)));
};

let tentAnchor = new Zdog.Anchor({
	addTo: sceneGroup
});

let tentFrontWallGroup = new Zdog.Group({
	addTo: tentAnchor
});

let tentRightWallGroup = new Zdog.Group({
	addTo: tentAnchor
});

let tentLeftWallGroup = new Zdog.Group({
	addTo: tentAnchor
});

let tentFrontWall = new Zdog.Shape({
	addTo: tentFrontWallGroup,
	color: chiffon.dark5,
	fill: true,
	path: [
		{ x: -30, y: 20, z: 25 },
		{ x: -30, y: 20, z: -25 },
		{ x: -30, y: -20, z: -25 },
		{ x: -30, y: -40, z: 0 },
		{ x: -30, y: -20, z: 25 },
	]
});

let openingFlap = new Zdog.Shape({
	addTo: tentFrontWallGroup,
	color: chiffon.dark25,
	fill: true,
	path: [
		{ x: -30, y: -17,z: 18 },
		{ x: -30, y: 20, z: 18 },
		{ x: -30, y: 20, z: -18 },
		{ x: -30, y: -17, z: -18 },
		{ x: -30, y: -31, z: 0 }
	],
});

let opening = new Zdog.Shape({
	addTo: tentFrontWallGroup,
	color: eigengrau.light30,
	fill: true,
	path: [
		{ x: -30, y: -17,z: 13 },
		{ x: -30, y: 20, z: 13 },
		{ x: -30, y: 20, z: -13 },
		{ x: -30, y: -17, z: -13 },
		{ x: -30, y: -27, z: 0 }
	],
});

tentFrontWall.copy({
	addTo: tentAnchor,
	color: chiffon.dark25,
	rotate: {
		y: TAU/2
	}
}); 

let tentRightWall = new Zdog.Rect({
	addTo: tentRightWallGroup,
	color: chiffon.dark25,
	fill: true,
	width: 60,
	height: 40,
	translate: {
		z: 25
	}
});

let tentRightWindow = new Zdog.Rect({
	addTo: tentRightWall,
	color: eigengrau.light30,
	fill: true,
	width: 15,
	height: 15
});

tentRightWall.copyGraph({
	addTo: tentLeftWallGroup,
	color: chiffon.dark5,
	translate: {
		z: -25
	}
});

let roof = new Zdog.Rect({
	addTo: tentAnchor,
	width: 60,
	height: 32,
	color: chiffon.dark15,
	fill: true,
	rotate: {
		x: TAU/7.012076353720296
	},
	translate: {
		y: -30,
		z: 13
	}
});

roof.copy({
	color: chiffon.regular,
	rotate: {
		x: -TAU/7.012076353720296,
	},
	translate: {
		y: -30,
		z: -13
	}
});

let platform = new Zdog.Box({
	addTo: tentAnchor,
	color: chenin.dark10,
	width: 100,
	height: 8,
	depth: 60,
	rightFace: chenin.dark15,
	frontFace: chenin.dark15,
	topFace: chenin.regular,
	bottomFace: false,
	translate: {
		x: -15,
		y: 25
	}
});

let post = new Zdog.Box({
	addTo: tentAnchor,
	color: chenin.dark25,
	width: 5,
	height: 8,
	depth: 5,
	translate: {
		x: 30,
		y: 34,
		z: 25
	},
	topFace: false,
	bottomFace: false,
	leftFace: chenin.dark15,
	rearFace: chenin.dark15
});

post.copy({
	translate: {
		x: 30,
		y: 34,
		z: -25
	}
});

post.copy({
	translate: {
		x: -60,
		y: 34,
		z: 25
	}
});

post.copy({
	translate: {
		x: -60,
		y: 34,
		z: -25
	}
});

let tether = new Zdog.Shape({
	addTo: tentAnchor,
	color: eigengrau.light30,
	path: [
		{ x: 30, y: -20, z: 25 },
		{ x: 30, y: 38, z: 60 }
	]
});

tether.copy({
	addTo: tether,
	translate: {
		x: -60
	}
});

tether.copyGraph({
	rotate: {
		y: TAU/2
	}
});

let cloudGroup = new Zdog.Anchor({
	addTo: scene,
});

let cloud = new Zdog.Shape({
	addTo: cloudGroup,
	color: chiffon.light5,
	stroke: 95,
	translate: {
		x: 400,
		y: 50
	},
	rotate: {
		y: TAU/4
	},
});

cloud.copy({
	addTo: cloud,
	stroke: 60,
	translate: {
		x: -50,
		y: 12
	}
});

cloud.copy({
	addTo: cloud,
	stroke: 50,
	translate: {
		x: -70,
		y: 14
	}
});

cloud.copy({
	addTo: cloud,
	stroke: 50,
	translate: {
		x: 45,
		y: 14
	}
});

cloud.copyGraph({
	translate: {
		x: -250,
		y: -100
	},
	rotate: {
		y: -TAU/4
	},
});

cloud.copyGraph({
	translate: {
		z: -350
	},
	rotate: {
		y: -TAU/2
	},
});

cloud.copyGraph({
	translate: {
		z: 300
	},
	rotate: {
		y: -TAU/2
	},
});

let rockAnchor = new Zdog.Anchor({
	addTo: sceneGroup,
	translate: {
		x: 50,
		y: 38,
		z: 60
	},
	rotate: {
		y: TAU/24
	},
	scale: 0.7
});

let rockSide = new Zdog.Shape({
	addTo: rockAnchor,
	color: '#ddd',
	fill: true,
	path: [
		{ x: 0, y: 0, z: 0 },
		{ x: 15, y: -20, z: 5},
		{ x: 15, y: -20, z: 15},
		{ x: 0, y: 0, z: 20 }
	]
});

rockSide.copy({
	path: [
		{ x: 0, y: 0, z: 0 },
		{ x: 15, y: -20, z: 5},
		{ x: 35, y: -15, z: 5},
		{ x: 45, y: 0, z: -2},
	]
});

rockSide.copy({
	color: '#ccc',
	path: [
		{ x: 35, y: -15, z: 5},
		{ x: 45, y: 0, z: -2},
		{ x: 40, y: 0, z: 25},
		{ x: 30, y: -12, z: 18},
	]
});

rockSide.copy({
	color: '#ccc',
	path: [
		{ x: 40, y: 0, z: 25},
		{ x: 30, y: -12, z: 18},
		{ x: 15, y: -20, z: 15},
		{ x: 0, y: 0, z: 20 }
	]
});

rockSide.copy({
	color: '#d5d5d5',
	path: [
		{ x: 35, y: -15, z: 5},
		{ x: 15, y: -20, z: 5},
		{ x: 15, y: -20, z: 15},
		{ x: 30, y: -12, z: 18},
	]
});

rockAnchor.copyGraph({
	translate: {
		x: 70,
		y: 38,
		z: 80
	},
	rotate: {
		y: -TAU/12
	},
	scale: 0.5
});

rockAnchor.copyGraph({
	translate: {
		x: -100,
		y: 38,
		z: -100
	},
	rotate: {
		y: TAU/6
	},
	scale: 0.4
});

let sun = new Zdog.Shape({
	addTo: scene,
	color: chiffon.regular,
	stroke: 100,
	translate: {
		x: -150,
		y: -200,
		z: -300
	}
})

makeGrass([45,60]);
makeGrass([48,62]);
makeGrass([85,90]);
makeGrass([83,87]);
makeGrass([35,95]);
makeGrass([37,97]);
makeGrass([35,100]);
makeGrass([-65,-30]);
makeGrass([-75,-20]);
makeGrass([-70,-10]);
makeGrass([-70,15]);
makeGrass([-75,30]);
makeGrass([-80,30]);
makeGrass([-75,40]);
makeGrass([-65,40]);
makeGrass([-70,40]);
makeGrass([30,40]);
makeGrass([25,40]);
makeGrass([15,40]);

function animate() {
	cloudGroup.rotate.y += 0.0005;
	scene.updateRenderGraph();
	requestAnimationFrame(animate);
}

animate();