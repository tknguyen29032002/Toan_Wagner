# Technology Stack

## Build System & Environment

- **Build Tool**: Eclipse Java Project
- **Java Version**: Java SE 11
- **Testing Framework**: JUnit 5
- **GUI Framework**: Java Swing

## Project Configuration

- Eclipse project with standard Java nature
- Source folder: `src/`
- Output folder: `bin/`
- JUnit 5 included in classpath

## Common Commands

### Compilation
Eclipse handles compilation automatically. For manual compilation:
```bash
javac -d bin -sourcepath src src/clueGame/ClueGame.java
```

### Running the Game
```bash
java -cp bin clueGame.ClueGame
```

### Running Tests
Tests are run through Eclipse's JUnit runner or via command line:
```bash
java -cp bin:junit-platform-console-standalone.jar org.junit.platform.console.ConsoleLauncher --scan-classpath
```

## Key Libraries

- **javax.swing**: GUI components (JFrame, JPanel, JButton, JDialog, etc.)
- **java.awt**: Graphics rendering and event handling
- **org.junit.jupiter**: Unit testing framework

## Data Files

Game configuration is loaded from CSV and text files in the `data/` directory:
- `ClueLayout.csv`: Board layout with room initials and special markers
- `ClueSetup.txt`: Room definitions, player setup, and weapon list
