#Path Planning

##Wtf is path planning and why do I give a fuck?
* Pretty neat.
* Google Maps
* Motherfucking robots
* Mah Roomba

##Dijkstra's Algorithm
* Conceived in 1956 
* Edsger Wybe Dijkstra
* Netherlands

##A*
* 1968
* Peter Hart, Nils Nilsson and Bertram Raphael, Stanford Research Institute
* Looks at fewer vertices than any other algorithm with the same heuristic, while still giving shortest path.
* Admissability 
* 

##Real-time Path Planning
* More real-world applications
* Robots, traffic.
* Changing/Unknown environments

##D* Lite
* 2002
* Koenig and Likhachev
* Remembers the edge costs

##Demonstration
* Show legit maps
* Draw paths and updated paths
* Show equivalence in output from D* Lite and A*
* Then explain the differences under the hood.
* Holds onto cost map vs. recalculating all the stuff.


##Comparison
* Bar graphs
* Number of pushes and pops
* Size of the heap (average or weighted average).
* Size of heap over time.

##Challenges
* Getting D* Lite to be as fast as A*
* Trying to get fair comparison.
* 

##Pretty picture.
* ggwp


The shortest path problem is a quintessential problem in Computer Science. Simply stated, it means finding the shortest, or least cost, path between two points.

More specifically, it is defined by Wikipedia as "...the problem of finding a path between two vertices in a graph such that the sum of the weights of its constituent edges is minimized." (Wikipedia)

Nowadays, this problem and its applications are ubiquitous. From Roombas to Google Maps, there is great interest in finding efficient routes.

In 1956, up in the Netherlands, a man named Dijkstra conceived of an algorithm for finding these paths rather quickly. 
It more or less works by always looking at the point nearest to the start, so when you get to the goal, you know that whatever path you took to get there was the shortest.

DRAW THREE DUDES WITH NAMES Peter Hart, Nils Nilsson and Bertram Raphael 
About 30 years later, 3 neat guys at the Stanford Research Institute conceived of a modified algorithm called A-Star.
This algorithm was very similar to Dijkstra's except that it incorporated a guesstimate of how close a given location is to the goal.
The intuition is that we can avoid looking at certain places if we have some information about our world.

COMPARISON TO GOOGLE MAPS, HAVE CUT OUT CARS WITH PEOPLES' HANDS MOVING THEM. HAVE ONE GET INTO CRASH.

Sadly, however, it is often the case that we do not have perfect information, or that our world is different since we last looked.
This poses a problem for the aforementioned algorithms, as each time we see something new, our paths are no longer valid, and must be recalculated.
What we want, is an algorithm that can plan paths in real-time.

There have been many algorithms implemented to solve this problem. These include Lifelong Planning A-Star and D-Star.
In 2002, however, 2 dudes implemented an algorithm called D-Star Lite, which took the best parts of LPA* and D*, and put them together.

DRAW TWO GUYS WITH THEIR NAMES SVEN KOENIG AND MAXIM LIKHACHEV

Generally with A-Star, once we find our paths, we throw away all of the information we calculated, except the path, because, like, why not?
D-Star Lite, however, keeps this information, as well as calculating some extra so that when we see some, new, osbtacle we can *update* our path instead of entirely recalculating it.

FUN DOODLE OF D* LITE, GO BACK TO GOOGLE MAPS, CRASHED CAR UPDATES GOOGLE MAPS' ROUTES. YAY. 

Generally, one of the bottlenecks of A* and D* Lite (besides the number of vertices and edges) is finding the cheapest vertex that we want to explore. One popular option is a binary heap, with nice, friendly, speedy, insertion and remove-min. 

DRAW LITTLE BINARY TREE, MAYBE WRITE OUT HEAP PROPERTY

The current lower bound for running time utilizes a *fibonacci-heap*, with REALLY FAST insertion, and the same remove-min. We didn't implement this because:

 "They are complicated when it comes to coding them."
  "...they are not as efficient in practice when compared with the theoretically less efficient forms of heaps..."
 ("The Pairing Heap: A new form of Self Adjusting Heap")

QUICK DOODLE OF F-HEAP. NO EXPLANATION. SCRIBBLE OVER TO SIGNIFY UNIMPORTANCE.

NOW FOR SOMETHING COMPLETELY DIFFERENT
WARNING: TECHNICAL JARGON AHEAD

Due to the issues with priority queues, Our major metric for performance was the number of pushes, remove-min's, and removals from the heap. 
Each of these operations require O(log(n)) calculations, and provide a fair comparison between A* and D* Lite. 

Removing the minimum from the heap represents visiting a vertex in our graph. So counting the total number of remove-min's is equivalent to the total number of vertices we visit. 

A note on real-world running time: because actual running time is implementation based, it isn't a terribly valuable metric. In our implementation, for example, after profiling D* Lite, and found that adding and removing from HashMaps was more than 50% of our running time! This was our implementation decision, and potentially there are much better options, meaning it wouldn't provide any important information to compare real-world time.



##Data

* Number of log(n) operations
** Push, pop, remove

Pops represent vertices expanded.
For each pop, we potentially push 8 neighbors.




