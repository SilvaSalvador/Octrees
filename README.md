Octree Cylinder Visualization
=====================

Introduction
------------

This project is a Scala-based graphical application that leverages Octrees to efficiently manage and render 3D objects loaded from a text file. The primary focus of the project is implementing View Frustum Culling using Octrees to optimize the rendering process. The project was developed to deepen the understanding of recursion, data structures, and functional programming in Scala.

What Are Octrees?
-----------------

An **octree** is a tree data structure where each node has up to eight children. It is used in 3D graphics and spatial indexing. In this project, octrees are used to represent 3D volumes and are visualized as boxes.

Octrees are particularly useful for tasks that involve partitioning 3D space, such as:

*   Efficiently rendering 3D scenes by dividing them into smaller sections
    
*   Collisions detection in 3D games
    
*   Spatial indexing in various 3D applications
    

In our project, each octree represents a 3D bounding box, and these boxes can be subdivided recursively into smaller boxes (or leaves), representing objects inside them.

Project Goals
-------------

The main goals of this project were:

*   Learning how to implement octrees in Scala
    
*   Applying recursion to build and manipulate the octree structure
    
*   Implementing basic functionalities such as scaling, applying color effects, and saving octree states
    
*   Utilizing Scala's functional programming concepts, particularly recursion and immutability
    

Features
--------

*   **3D Model Visualization**: The application reads a text file containing geometric objects (such as boxes and cylinders) and visualizes them in a 3D space.  
*   **Octree Implementation**: Objects are spatially subdivided using an Octree, which helps efficiently manage the rendering of objects in 3D.
*   **View Frustum Culling**: The application employs a cylindrical camera to determine which parts of the 3D scene are visible, improving performance by avoiding the rendering of objects outside the camera's view.   
*   **Interactive GUI**: Users can interact with the 3D scene, applying transformations (such as scaling) and visual effects (such as sepia and color removal).
    

### How It Works

-   The application loads objects from a text file. Objects include cylinders and boxes, which are organized in a hierarchical structure using **Octrees**.
-   A **cylindrical camera view** is implemented, which determines the visibility of objects based on the camera's position and orientation.
-   The **View Frustum Culling** algorithm uses Octrees to determine which objects are inside or outside the view frustum, improving performance.
-   Users can apply various transformations, such as scaling objects up or down and changing colors.

Running the Application
-----------------------
1.  Clone the repository and navigate to the project folder.
    
2.  Make sure JavaFX is properly set up in your IDE.
    
3.  Compile and run the main class UI to launch the application.
    

Once the application is running, you can interact with the octree through the GUI by scaling, applying color effects, or saving the state of the octree.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
   
