Octree 3D Viewer
=====================

Introduction
------------

This project was mainly focused on learning Scala, recursion, and how to manage and manipulate **octrees**. The graphical user interface (GUI) was not the main focus of the project, but it was implemented to help visualize the 3D structures. This project involves using a cylinder camera view to navigate and visualize **octrees**, which are represented as boxes.

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

*   **Octree Construction**: The program constructs an octree from 3D objects such as boxes and cylinders, recursively dividing space until all objects are placed in the correct nodes.
    
*   **Color Effects**: Users can apply different color effects, such as Sepia or Green Removal, to the octree nodes for visualization.
    
*   **Scaling**: Users can scale the entire octree up or down by a factor of 2 or 0.5, respectively.
    
*   **Saving and Loading**: The program allows users to save the current octree state to a file and reload it later.
    
*   **GUI**: The GUI allows users to interact with the octree by scaling, applying effects, or saving the current state of the 3D view.
    

Code Overview
-------------

### Octree Structure

The core octree structure is implemented in the Otree class, where each node can be an empty node, a leaf node (containing an object), or an internal node (containing eight children). Here are some key methods:

*   **constructTree**: Recursively constructs the octree from a list of 3D objects (boxes or cylinders).
    
*   **mapColorEffect**: Applies a color transformation to all objects in the octree.
    
*   **scaleOctree**: Recursively scales the octree and the objects it contains by a given factor.
    

### Camera View

The camera view is controlled using the CameraTransformer class, which allows users to move and rotate the camera in 3D space. This class provides various transformations like translation, rotation, and scaling, making it easier to navigate through the 3D octree.

### File I/O

The OtreeIO object handles reading and writing the octree structure to and from files. The octree is serialized into a text format, which can later be loaded to restore the octree's state.

### GUI Interaction

The GUI is implemented in the Controller class and uses JavaFX to handle user input and display the 3D scene. The SubScene is used to display the octree and allows users to interact with buttons to scale, apply effects, or save the current state.

Usage
-----

### Requirements

*   Java 11 or higher
    
*   Scala
    
*   JavaFX libraries
    

### Running the Application

1.  Clone the repository and navigate to the project folder.
    
2.  Make sure JavaFX is properly set up in your IDE.
    
3.  Compile and run the main class UI to launch the application.
    

Once the application is running, you can interact with the octree through the GUI by scaling, applying color effects, or saving the state of the octree.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
   
