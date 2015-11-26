#To not sound stupid

##Basics
* These algorithms find the least cost path through a graph between two points.
* A* is the most common, and is proven to be the fastest algorithm for this type of thing (under certain circumstances)
* D* Lite *modifies* A* so that we can find paths *even faster* **IF AND ONLY IF** the graph has changed, and we are simply *updating* the path.

##Implementation
* We are on a 2D grid.
* We have a little "robot" who wants to move from start to goal.
* Our "robot" can see **ONLY** the cells next to him.
* If he sees an obstacle in his way, he replans his path.
* D* Lite **UPDATES** the path. The path is still optimal.
* A* replans the entire path. The path is optimal.
* When A* replans the path, it forgets **EVERYTHING** from before.

##Results
* D* Lite **FUCKING DOMINATES** A*
* So much faster it is absolutely stupid
* This is **GOOD** we **LIKE** this. This is how things **SHOULD** be.
