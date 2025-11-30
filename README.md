# CSC 301-1_FA_2025 Group Assignment Program 3: Pac-Man

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Java Version](https://img.shields.io/badge/Java-8%2B-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-Academic-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=for-the-badge)

**University of Tampa | CSC 301-1 Fall 2025**  
**Developed by:** Yaroslav and Darwin

[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?style=flat-square&logo=github)](https://github.com/YaroslavTrachIgor/csc-301-p3)

</div>

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Building and Running](#-building-and-running)
- [Project Structure](#-project-structure)
- [Assignment Requirements](#-assignment-requirements)
- [Original Source Code](#-original-source-code)
- [Academic Citation](#-academic-citation)

---

## 🎮 Project Overview

This project is part of the **CSC 301-1 (Fall 2025)** course at the University of Tampa. The assignment involves implementing custom artificial intelligence for the four ghosts in a Pac-Man game, each with distinct behavioral patterns.

The game is built using Java's AWT/Swing graphics library and features:
- Custom ghost AI classes with method stubs for implementation
- Tilemap-based level system
- Collision detection
- Score tracking
- Game state management

![pacman-ss](https://github.com/user-attachments/assets/9f16553b-9092-4894-b740-b8903ed24fa9)

---

## 🔧 Prerequisites

Before installing and running this project, ensure you have the following installed:

- **Java Development Kit (JDK) 8 or higher**
  ```bash
  java -version
  javac -version
  ```
  
- **Git** (for cloning the repository)
  ```bash
  git --version
  ```

- **IDE** (optional but recommended):
  - IntelliJ IDEA
  - Eclipse
  - Visual Studio Code with Java extensions

---

## 📥 Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/YaroslavTrachIgor/csc-301-p3.git
cd csc-301-p3
```

### Step 2: Verify Project Structure

Ensure the following files are present in the project directory:

```bash
ls -la
```

Expected files:
```
App.java
PacMan.java
Block.java
Ghost.java
Blinky.java
Pinky.java
Inky.java
Clyde.java
*.png (image files)
```

### Step 3: Verify Image Resources

The project requires the following image files in the root directory:
- `wall.png`
- `pacmanUp.png`, `pacmanDown.png`, `pacmanLeft.png`, `pacmanRight.png`
- `redGhost.png`, `pinkGhost.png`, `blueGhost.png`, `orangeGhost.png`
- `scaredGhost.png`, `powerFood.png`, `cherry.png`, `cherry2.png`

---

## 🚀 Building and Running

### Method 1: Command Line Compilation

#### Compile the Project

```bash
# Compile all Java files
javac -d out *.java

# Or compile to current directory
javac *.java
```

#### Run the Application

```bash
# If compiled to 'out' directory
java -cp out App

# If compiled to current directory
java App
```

### Method 2: Using an IDE

#### IntelliJ IDEA
1. Open IntelliJ IDEA
2. Select `File` → `Open`
3. Navigate to the project directory and select it
4. Wait for the project to sync
5. Right-click on `App.java` → `Run 'App.main()'`

#### Visual Studio Code
1. Open VS Code in the project directory
2. Install the "Extension Pack for Java" if not already installed
3. Open `App.java`
4. Click the "Run" button above the `main` method
5. Or use the terminal: `java App`

#### Eclipse
1. Open Eclipse
2. Select `File` → `Import` → `Existing Projects into Workspace`
3. Browse to the project directory
4. Select the project and click `Finish`
5. Right-click on `App.java` → `Run As` → `Java Application`

### Expected Output

Upon successful execution, a game window should appear with:
- Pac-Man character controllable via arrow keys
- Ghosts positioned on the map (currently stationary)
- Food pellets to collect
- Score and lives display

---

## 📁 Project Structure

```
csc-301-p3/
│
├── App.java                 # Main entry point
├── PacMan.java              # Main game class (JPanel, game loop, rendering)
├── Block.java               # Base class for game entities
├── Ghost.java               # Abstract base class for ghosts
│
├── Blinky.java              # Red ghost - direct chase behavior
├── Pinky.java               # Pink ghost - anticipatory chase
├── Inky.java                # Blue ghost - hybrid behavior
├── Clyde.java               # Orange ghost - scaredy-cat behavior
│
├── *.png                    # Image resources (sprites, walls, etc.)
├── README.md                # This file
└── .gitignore               # Git ignore rules
```

### Key Classes

#### `App.java`
Main application entry point that creates the JFrame and initializes the game.

```java
public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Pac Man");
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        frame.setVisible(true);
    }
}
```

#### `Ghost.java` (Abstract Base Class)
Defines the interface for all ghost implementations.

```java
public abstract class Ghost extends Block {
    public abstract Point calculateTarget();
    public abstract char chooseDirection();
    public abstract void move();
}
```

#### Ghost Implementations
Each ghost class extends `Ghost` and implements unique AI behavior:
- **Blinky**: Direct pathfinding to Pac-Man's current position
- **Pinky**: Targets position 2 dots ahead of Pac-Man
- **Inky**: Combines Blinky and Pinky strategies
- **Clyde**: Chases when far, flees when within 8 dots

---

## 📝 Assignment Requirements

The assignment prompt specified the following requirements:

> "Clone the open-source Pac-Man game project you see in the current directory. It will be used for the university program in which we need to write a custom code for ghosts:
> 
> - **Blinky**: The direct one. It chases Pac-Man based on current locations.
> - **Pinky**: The smart one. It chases Pac-Man, but it aims for a location 2 dots ahead of Pac-Man, thus trying to anticipate movement.
> - **Inky**: The hybrid. Its movement is a combination of Blinky and Pinky.
> - **Clyde**: The scaredy-cat. It will chase Pac-Man directly, but when it gets within 8 dots it gets scared and runs.
> 
> You should add custom classes for each of those ghosts so that I can program them from scratch. Declare all the basic methods with comments for each ghost class. At the end, you should come up with the same program but the ghosts should not move for now. Pac-Man object should act as it moved before."

### Implementation Status

- ✅ Custom ghost classes created with method stubs
- ✅ Detailed comments and documentation in each class
- ✅ Ghost movement disabled (as per requirements)
- ✅ Pac-Man movement fully functional
- ⏳ Ghost AI implementation (pending)

---

## 📚 Original Source Code

This project is based on an open-source Pac-Man game implementation. The original code and tutorial are available at:

- **Original Tutorial Video**: [Pacman Java Tutorial](https://youtu.be/lB_J-VNMVpE)
- **Tutorial Link**: https://youtu.be/lB_J-VNMVpE
- **Setup Guide**: [How to setup Java with Visual Studio Code](https://youtu.be/BB0gZFpukJU)

The original implementation demonstrates how to create a Pac-Man game using Java's built-in AWT/Swing graphics library, including:
- Game loop implementation
- JFrame and JPanel setup
- Image rendering
- Tilemap-based level loading
- Keyboard input handling
- Random ghost movement algorithm
- Collision detection
- Score tracking and game reset mechanics

---

## 📖 Academic Citation

This project uses code from an open-source Pac-Man tutorial. The original tutorial and implementation serve as the foundation for this assignment, with modifications made to support custom ghost AI development as specified in the assignment requirements.

**Course Information:**
- **Course**: CSC 301-1 (Fall 2025)
- **Institution**: University of Tampa
- **Assignment**: Program 3 - Pac-Man Ghost AI
- **Developers**: Yaroslav and Darwin

---

## 🏷️ Tags

`java` `pacman` `game-development` `artificial-intelligence` `awt` `swing` `university-project` `csc-301` `university-of-tampa` `ghost-ai` `pathfinding` `game-ai`

---

<div align="center">

**University of Tampa | CSC 301-1 Fall 2025**

[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?style=flat-square&logo=github)](https://github.com/YaroslavTrachIgor/csc-301-p3)

</div>
