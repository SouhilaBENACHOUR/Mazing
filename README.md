# Mazing 🧩

**Mazing** is a Java-based maze game developed as a group project. The player explores different mazes, collects keys, unlocks doors, avoids enemies and tries to reach the exit.

The game features three levels with increasing difficulty, different maze sizes and a score system.

## 🎮 Features

- **Three playable levels** with increasing difficulty.
- **Maze exploration:** navigate through walls, corridors, doors and exits.
- **Key collection:** collect keys to unlock doors.
- **Enemy AI:** enemies follow the player using a Breadth-First Search (BFS) pathfinding algorithm.
- **Lives system:** the player starts with three lives.
- **Scoring system:**
  - 1,000 points for completing a level.
  - 100 points for each enemy eliminated.
- **JSON-based levels:** maze layouts and configurations are stored in JSON files.

## 🗺️ Levels

| Level | Name | Size | Difficulty |
|---|---|---|---|
| 1 | Initiation | 10 × 10 | Easy |
| 2 | Le Dédale | 15 × 15 | Medium |
| 3 | La Forteresse | 20 × 20 | Hard |

## 🛠️ Technologies

- **Java 23**
- **Gradle** – Build automation and dependency management.
- **Gson** – JSON parsing and configuration.
- **JUnit 5** – Testing framework.
- **Mazing Engine** – Local game library included in the `lib` directory.

## 🏗️ Architecture

The project follows the **Model-View-Controller (MVC)** architectural pattern.

The source code is organized into three main components:

- **Model:** manages the game state, maze, entities and game logic.
- **View:** handles the visual representation of the game.
- **Controller:** manages player input, enemy behavior and the game loop.

The project also uses design patterns such as:

- **Observer:** allows game components to be notified of state changes.
- **Factory:** centralizes the creation of game entities.

### Pathfinding

Enemy movement relies on **Breadth-First Search (BFS)** to find a path through the maze towards the player.

A separate `PathFinder` class also implements **Dijkstra's algorithm** for pathfinding.

## 📁 Project Structure

```text
Mazing/
├── examples/
├── gradle/
├── lib/
│   ├── mazing-1.0.1.jar
│   ├── mazing-1.0.1-sources.jar
│   └── mazing-1.0.1-javadoc.jar
├── src/
│   └── main/
│       ├── java/
│       │   └── fr/ubordeaux/ao/project/
│       │       ├── Main.java
│       │       ├── controller/
│       │       ├── model/
│       │       │   ├── entities/
│       │       │   ├── graph/
│       │       │   └── patterns/
│       │       └── view/
│       └── resources/
│           └── levels/
│               ├── level1.json
│               ├── level2.json
│               └── level3.json
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle.kts
```

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 23 or later.
- Git.

### 1. Clone the repository

```bash
git clone https://github.com/SouhilaBENACHOUR/Mazing.git
cd Mazing
```

### 2. Run the application

On Linux or macOS:

```bash
./gradlew run
```

On Windows:

```bash
gradlew.bat run
```

### 3. Run the tests

```bash
./gradlew test
```

## 👥 Contributors

This project was developed collaboratively by:

- [Djena3107](https://github.com/Djena3107)
- [SouhilaBENACHOUR](https://github.com/SouhilaBENACHOUR)
- [aymoji](https://github.com/aymoji)

## 📄 Project Report

The project report is available in the repository: [`Rapport_AO-1.pdf`](Rapport_AO-1.pdf).

---

**University of Bordeaux** — Object-Oriented Programming Project.
