# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer:

Our implementation tried to draw hexagons one "ring" at a time, whereas the provided implementation drew one column at a time after drawing 5 hexagons at the
top. In hindsight, this approach is far easier given that our canvas is represented by two orthogonal axes. The provided implementation exploited this to make
drawing an entire column from a reference hexagon very simple.

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer:

A hexagon would correspond to a room, and the tesselation would correspond to the layout of rooms. Obviously, this would not be a uniform tesselation.

**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer:

The lab used a "starting" hexagon as a reference point for drawing all the other hexagons. We think that it would be similarly useful to first
generate a "starting" room, and then build off that.

**What distinguishes a hallway from a room? How are they similar?**

Answer:

We believe that a hallway is a room with width or height 1. We don't think that there is any other functional difference between the two.