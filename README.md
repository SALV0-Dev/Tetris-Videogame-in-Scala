# Tetris Videogame in Scala
 
## Overview
This is a Tetris game implemented in Scala. The project follows a modular structure, with separate folders for game logic, rendering, and utilities. The game logic is encapsulated in the `TetrisLogic` class, which handles piece generation, movement, and rotation. The project uses Gradle for dependency management and build automation.

## How It Works
The game initializes a grid where tetrominoes (Tetris pieces) appear and fall. The logic for handling tetromino movement and rotation is implemented in the `TetrisLogic.scala` file. 

### Core Mechanics
- **Piece Generation:** A random tetromino is selected at the start of each round.
- **Rotation Handling:** 
  - The game defines different rotation types for tetrominoes.
  - `type_I_Rotations` and `type_II_Rotations` handle rotation transformations based on predefined rules.
- **Collision Detection:** The game prevents illegal movements by checking for grid boundaries and overlapping pieces.
- **Game Over Detection:** The game ends if a new piece cannot be placed on the board.

## Gradle in the Project
Gradle is used as the build automation tool for this project. It helps with:
- **Managing Dependencies:** Ensures required Scala libraries are included.
- **Compiling and Running the Project:** Automates compilation and execution.
- **Packaging the Game:** Allows for easy distribution.

To build and run the project using Gradle:
```sh
./gradlew run
```
This will compile the Scala source code and execute the game.

## Running the Game
1. Ensure you have Java and Scala installed.
2. Clone the repository and navigate to the project directory.
3. Run `./gradlew run` to start the game.

## Future Improvements
- Implementing a graphical user interface.
- Enhancing piece movement animations.
- Adding score tracking and levels.

Enjoy playing Tetris in Scala! 🎮
