# Project Structure

## Directory Organization

```
├── src/
│   ├── clueGame/       # Core game logic and models
│   ├── gui/            # Swing GUI components
│   ├── tests/          # JUnit test classes
│   └── experiment/     # Experimental/prototype code
├── data/               # Configuration files (CSV, TXT)
├── bin/                # Compiled class files (generated)
└── .settings/          # Eclipse project settings
```

## Package Architecture

### `clueGame` Package (Core Game Logic)
- **ClueGame.java**: Main entry point, game window, turn management, event handling
- **Board.java**: Singleton managing board state, adjacencies, targets, player/card management
- **BoardCell.java**: Individual cell with position, type, adjacencies, and rendering
- **Player.java**: Abstract base class for players with hand management
- **HumanPlayer.java**: Human player with seen card tracking
- **ComputerPlayer.java**: AI player with suggestion/accusation logic
- **Card.java**: Represents a game card (person, weapon, or room)
- **Solution.java**: Represents a suggestion or accusation (person + weapon + room)
- **Room.java**: Room metadata with label and center cells
- **CardType.java**: Enum for card types (PERSON, WEAPON, ROOM)
- **DoorDirection.java**: Enum for door orientations (UP, DOWN, LEFT, RIGHT, NONE)
- **BadConfigFormatException.java**: Custom exception for configuration errors

### `gui` Package (User Interface)
- **GameControlPanel.java**: Bottom panel showing turn info, guess, and buttons
- **KnownCardsPanel.java**: Side panel displaying human player's hand and seen cards
- **SuggestionDialog.java**: Modal dialog for making suggestions in rooms
- **AccusationDialog.java**: Modal dialog for making accusations
- **SuggestionDialogTest.java**: Test harness for suggestion dialog

### `tests` Package (Unit Tests)
- **BoardTestsExp.java**: Experimental board tests
- **FileInitTests.java**: Tests for loading configuration files
- **ExceptionTests.java**: Tests for error handling
- **BoardAdjTargetTest.java**: Tests for adjacency and target calculations
- **GameSetupTests.java**: Tests for player/card/deck initialization
- **GameSolutionTest.java**: Tests for suggestion and accusation handling
- **ComputerAITest.java**: Tests for computer player AI logic

### `experiment` Package
- **TestBoard.java**: Prototype board implementation
- **TestBoardCell.java**: Prototype cell implementation

## Design Patterns

- **Singleton**: Board class uses singleton pattern for global access
- **Inheritance**: Player hierarchy (Player → HumanPlayer/ComputerPlayer)
- **MVC-like**: Separation between game logic (clueGame), view (gui), and data (config files)
- **Observer**: Swing event listeners for button clicks and mouse events

## Data Flow

1. Configuration files loaded from `data/` directory
2. Board singleton initializes grid, rooms, players, and cards
3. ClueGame creates GUI and starts turn loop
4. User interactions trigger event handlers
5. Board state updates and GUI components repaint
