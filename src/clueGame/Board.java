package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

public class Board {
	// Constants for cell types and special characters
	private static final char WALKWAY_INITIAL = 'W';
	private static final char LABEL_INDICATOR = '#';
	private static final char CENTER_INDICATOR = '*';
	private static final char DOOR_UP = '^';
	private static final char DOOR_DOWN = 'v';
	private static final char DOOR_LEFT = '<';
	private static final char DOOR_RIGHT = '>';
	private static final char NO_SECRET_PASSAGE = ' ';
	private static final String DATA_DIRECTORY = "data/";
	
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private static Board theInstance = new Board();
	
	private List<Player> players;
    private List<String> personNames;
    private List<String> weaponNames;
    private List<Card> deck;
    private Solution theAnswer;

	// Private constructor for singleton pattern
	private Board() {
		super();
	}

	// Return the singleton Board instance
	public static Board getInstance() {
		return theInstance;
	}

	// Initialize board: load configs and calculate adjacencies
	public void initialize() {
		try {
			// Reset state for fresh initialization
			roomMap = null;
			grid = null;
			targets = null;
			visited = null;
			
			players = new ArrayList<>();
            personNames = new ArrayList<>();
            weaponNames = new ArrayList<>();
            deck = null;
            theAnswer = null;
			
			loadSetupConfig();
			loadLayoutConfig();
			calcAdjacencies();
		} catch (BadConfigFormatException | FileNotFoundException e) {
			System.out.println("Error loading config files: " + e.getMessage());
		}
	}

	// Set config file paths by prepending data directory
	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = DATA_DIRECTORY + layoutFile;
		setupConfigFile = DATA_DIRECTORY + setupFile;
	}

	// Load setup config file to initialize rooms and spaces
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap<>();
		
		players = new ArrayList<>();
		personNames = new ArrayList<>();
		weaponNames = new ArrayList<>();
		
		FileReader reader = new FileReader(setupConfigFile);
		Scanner scanner = new Scanner(reader);
		
		try {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				
				// Skip empty lines and comments
				if (line.isEmpty() || line.startsWith("//")) {
					continue;
				}
				// Split by comma and trim each part
				String[] parts = line.split(",");
				if (parts.length < 2) {
					throw new BadConfigFormatException("Invalid setup file format: " + line);
				}
				String type = parts[0].trim();
				String name = parts[1].trim();
				switch (type) {
					case "Room":
						//same logic as "Space"
					case "Space":
						if (parts.length != 3) throw new BadConfigFormatException("Invalid Room/Space: " + line);
						String initialStr = parts[2].trim();
						// initial must be single character
						if (initialStr.length() != 1) throw new BadConfigFormatException("Initial must be single char: " + line);
						char initial = initialStr.charAt(0);
						// Create room and add to map
						Room room = new Room(name, type);
						roomMap.put(initial, room);
						break;
						
					case "Person":
						if (parts.length != 6) throw new BadConfigFormatException("Invalid Person: " + line);
						String colorStr = parts[2].trim();
						int row = Integer.parseInt(parts[3].trim());
						int col = Integer.parseInt(parts[4].trim());
						String playerType = parts[5].trim();
						Player player;
						if (playerType.equals("Human")) {
							player = new HumanPlayer(name, colorStr, row, col);
						} else if (playerType.equals("Computer")) {
							player = new ComputerPlayer(name, colorStr, row, col);
						} else {
							throw new BadConfigFormatException("Invalid player type: " + playerType);
						}
						players.add(player);
						personNames.add(name);
						break;
						
					case "Weapon":
						if (parts.length != 2) throw new BadConfigFormatException("Invalid Weapon: " + line);
						weaponNames.add(name);
						break;
						
					default:
						throw new BadConfigFormatException("Invalid type: " + type);
				}
			}
		} finally {
		scanner.close();
		}
	}

	// Load layout config to initialize grid with doors, centers, labels, and passages
	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(layoutConfigFile);
		Scanner scanner = new Scanner(reader);
		
		// First pass: read all lines to determine size
		ArrayList<String> lines = new ArrayList<>();
		while (scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}
		scanner.close();
		
		if (lines.isEmpty()) {
			throw new BadConfigFormatException("Layout file is empty");
		}
		
		// Determine board dimensions
		numRows = lines.size();
		String[] firstRow = lines.get(0).split(",");
		numColumns = firstRow.length;
		
		// Validate all rows have same number of columns
		for (String line : lines) {
			String[] cells = line.split(",");
			if (cells.length != numColumns) {
				throw new BadConfigFormatException("All rows must have the same number of columns");
			}
		}
		
		// Create grid
		grid = new BoardCell[numRows][numColumns];
		
		// Second pass: populate grid
		for (int row = 0; row < numRows; row++) {
			String[] cells = lines.get(row).split(",");
			for (int col = 0; col < numColumns; col++) {
				String cellStr = cells[col].trim();
				
				// Parse cell string
				char roomInitial = cellStr.charAt(0);
				
				// Validate room exists in setup
				if (roomInitial != WALKWAY_INITIAL && !roomMap.containsKey(roomInitial)) {
					throw new BadConfigFormatException("Room '" + roomInitial + "' not found in setup file");
				}
				
				// Create cell
				BoardCell cell = new BoardCell(row, col, roomInitial);
				
				// Parse special characters
				if (cellStr.length() > 1) {
					char secondChar = cellStr.charAt(1);
					
					switch (secondChar) {
						case LABEL_INDICATOR:  // Label cell
							cell.setLabel(true);
							roomMap.get(roomInitial).setLabelCell(cell);
							break;
						case CENTER_INDICATOR:  // Center cell
							cell.setRoomCenter(true);
							roomMap.get(roomInitial).setCenterCell(cell);
							break;
						case DOOR_UP:  // Door facing UP
							cell.setDoorDirection(DoorDirection.UP);
							break;
						case DOOR_DOWN:  // Door facing DOWN
							cell.setDoorDirection(DoorDirection.DOWN);
							break;
						case DOOR_LEFT:  // Door facing LEFT
							cell.setDoorDirection(DoorDirection.LEFT);
							break;
						case DOOR_RIGHT:  // Door facing RIGHT
							cell.setDoorDirection(DoorDirection.RIGHT);
							break;
						default:   // Secret passage
							cell.setSecretPassage(secondChar);
							break;
					}
				}
				
				grid[row][col] = cell;
			}
		}
	}
	
	//creates the deck of 21 cards (or however many is in setup.txt)
	public void createDeck() {
        List<Card> roomCards = new ArrayList<>();
        List<Card> personCards = new ArrayList<>();
        List<Card> weaponCards = new ArrayList<>();

        for (Room room : roomMap.values()) {
            if ("Room".equals(room.getType()) && !room.getName().isEmpty()) {
                roomCards.add(new Card(room.getName(), CardType.ROOM));
            }
        }

        for (String person : personNames) {
            personCards.add(new Card(person, CardType.PERSON));
        }

        for (String weapon : weaponNames) {
            weaponCards.add(new Card(weapon, CardType.WEAPON));
        }

        deck = new ArrayList<>();
        deck.addAll(roomCards);
        deck.addAll(personCards);
        deck.addAll(weaponCards);
    }

	//deal cards to players 6
    public void dealCards() {
        if (deck == null) createDeck();

        List<Card> roomCards = new ArrayList<>();
        List<Card> personCards = new ArrayList<>();
        List<Card> weaponCards = new ArrayList<>();

        for (Card c : deck) {
            switch (c.getType()) {
                case ROOM: roomCards.add(c); break;
                case PERSON: personCards.add(c); break;
                case WEAPON: weaponCards.add(c); break;
            }
        }

        Collections.shuffle(roomCards);
        Collections.shuffle(personCards);
        Collections.shuffle(weaponCards);

        Card room = roomCards.remove(0);
        Card person = personCards.remove(0);
        Card weapon = weaponCards.remove(0);
        theAnswer = new Solution(person, weapon, room);

        List<Card> remainingDeck = new ArrayList<>();
        remainingDeck.addAll(roomCards);
        remainingDeck.addAll(personCards);
        remainingDeck.addAll(weaponCards);
        Collections.shuffle(remainingDeck);

        int playerIdx = 0;
        for (Card c : remainingDeck) {
            players.get(playerIdx).updateHand(c);
            playerIdx = (playerIdx + 1) % players.size();
        }
    }
    
	// Get room by initial character, return empty room if not found
	public Room getRoom(char c) {
		if (roomMap.containsKey(c)) {
			return roomMap.get(c);
		}
		return new Room();
	}

	// Get room for the given cell
	public Room getRoom(BoardCell cell) {
		if (cell != null) {
			return getRoom(cell.getInitial());
		}
		return new Room();
	}

	// Return number of rows on board
	public int getNumRows() {
		return numRows;
	}

	// Return number of columns on board
	public int getNumColumns() {
		return numColumns;
	}

	// Get cell at position, return empty cell if out of bounds
	public BoardCell getCell(int row, int col) {
		if (isValidCell(row, col)) {
			return grid[row][col];
		}
		return new BoardCell();
	}
	
	// Check if row and column are within bounds
	private boolean isValidCell(int row, int col) {
		return row >= 0 && row < numRows && col >= 0 && col < numColumns;
	}
	
	// Calculate adjacencies for walkways and room centers
	private void calcAdjacencies() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				
				// Room cells that are not centers have no adjacencies
				if (cell.getInitial() != WALKWAY_INITIAL && !cell.isRoomCenter()) {
					continue;
				}
				
				// Room center cells
				if (cell.isRoomCenter()) {
					calcRoomCenterAdj(cell);
				}
				// Walkway cells
				else if (cell.getInitial() == WALKWAY_INITIAL) {
					calcWalkwayAdj(cell);
				}
			}
		}
	}
	
	// Calculate adjacencies for room center (doorways + secret passages)
	private void calcRoomCenterAdj(BoardCell cell) {
		addDoorwayAdjacencies(cell);
		addSecretPassageAdjacency(cell);
	}
	
	// Add all doorways that lead into this room center
	private void addDoorwayAdjacencies(BoardCell roomCenter) {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell potentialDoor = grid[row][col];
				if (potentialDoor.isDoorway()) {
					BoardCell doorTarget = getDoorTarget(potentialDoor);
					// Check if this door points to this specific room center
					if (doorTarget == roomCenter) {
						roomCenter.addAdj(potentialDoor);
					}
				}
			}
		}
	}
	
	// Add secret passage for room if one exists
	private void addSecretPassageAdjacency(BoardCell roomCenter) {
		char roomInitial = roomCenter.getInitial();
		
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell roomCell = grid[row][col];
				// If this cell is part of our room and has a secret passage
				if (roomCell.getInitial() == roomInitial && roomCell.getSecretPassage() != NO_SECRET_PASSAGE) {
					char targetRoomInitial = roomCell.getSecretPassage();
					Room targetRoom = roomMap.get(targetRoomInitial);
					if (targetRoom != null && targetRoom.getCenterCell() != null) {
						roomCenter.addAdj(targetRoom.getCenterCell());
						return; // Only add one secret passage per room
					}
				}
			}
		}
	}
	
	// Calculate adjacencies for walkway cells including doorways
	private void calcWalkwayAdj(BoardCell cell) {
		int row = cell.getRow();
		int col = cell.getCol();
		
		// For doorways, don't add adjacency in the direction the door faces
		if (cell.isDoorway()) {
			DoorDirection dir = cell.getDoorDirection();
			BoardCell roomCenter = getDoorTarget(cell);
			
			// Add walkway adjacencies in all directions except the door direction
			if (dir != DoorDirection.UP) {
				addWalkwayAdj(cell, row - 1, col); // Up
			}
			if (dir != DoorDirection.DOWN) {
				addWalkwayAdj(cell, row + 1, col); // Down
			}
			if (dir != DoorDirection.LEFT) {
				addWalkwayAdj(cell, row, col - 1); // Left
			}
			if (dir != DoorDirection.RIGHT) {
				addWalkwayAdj(cell, row, col + 1); // Right
			}
			
			// Add the room center this door points to
			if (roomCenter != null) {
				cell.addAdj(roomCenter);
			}
		} else {
			// Regular walkway - add all four directions
			addWalkwayAdj(cell, row - 1, col); // Up
			addWalkwayAdj(cell, row + 1, col); // Down
			addWalkwayAdj(cell, row, col - 1); // Left
			addWalkwayAdj(cell, row, col + 1); // Right
		}
	}
	
	// Add adjacent walkway if valid position and walkway type
	private void addWalkwayAdj(BoardCell cell, int row, int col) {
		if (isValidCell(row, col)) {
			BoardCell adj = grid[row][col];
			if (adj.getInitial() == WALKWAY_INITIAL) {
				cell.addAdj(adj);
			}
		}
	}
	
	// Get room center that door points to based on direction
	private BoardCell getDoorTarget(BoardCell door) {
		if (!door.isDoorway()) return null;
		
		int row = door.getRow();
		int col = door.getCol();
		BoardCell targetCell = null;
		
		switch (door.getDoorDirection()) {
			case UP:
				targetCell = (row > 0) ? grid[row - 1][col] : null;
				break;
			case DOWN:
				targetCell = (row < numRows - 1) ? grid[row + 1][col] : null;
				break;
			case LEFT:
				targetCell = (col > 0) ? grid[row][col - 1] : null;
				break;
			case RIGHT:
				targetCell = (col < numColumns - 1) ? grid[row][col + 1] : null;
				break;
			default:
				return null;
		}
		
		// Return the room center for this room
		if (targetCell != null && targetCell.getInitial() != WALKWAY_INITIAL) {
			Room room = roomMap.get(targetCell.getInitial());
			if (room != null) {
				return room.getCenterCell();
			}
		}
		return null;
	}
	
	// Return adjacency list for cell at given position
	public Set<BoardCell> getAdjList(int row, int col) {
		BoardCell cell = getCell(row, col);
		if (cell != null && cell.getAdjList() != null) {
			return cell.getAdjList();
		}
		return new HashSet<BoardCell>();
	}
	
	// Calculate all possible targets from starting cell with given path length
	public void calcTargets(BoardCell startCell, int pathLength) {
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	// Recursively find all targets using backtracking
	private void findAllTargets(BoardCell cell, int numSteps) {
		// Get adjacency list for current cell
		Set<BoardCell> adjList = cell.getAdjList();
		if (adjList == null) return;
		
		for (BoardCell adjCell : adjList) {
			// Skip if already visited or occupied (unless it's a room center)
			if (visited.contains(adjCell)) {
				continue;
			}
			if (adjCell.isOccupied() && !adjCell.isRoomCenter()) {
				continue;
			}
			
			visited.add(adjCell);
			
			// If we've reached the target distance or entered a room, add to targets
			if (numSteps == 1 || adjCell.isRoomCenter()) {
				targets.add(adjCell);
			} else {
				// Continue searching from this cell
				findAllTargets(adjCell, numSteps - 1);
			}
			
			visited.remove(adjCell);
		}
	}
	
	// Return target cells from last calcTargets call
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	// Check if accusation matches theAnswer
    public boolean checkAccusation(Solution accusation) {
        if (theAnswer == null || accusation == null) {
            return false;
        }
        
        return theAnswer.getPerson().equals(accusation.getPerson()) &&
               theAnswer.getWeapon().equals(accusation.getWeapon()) &&
               theAnswer.getRoom().equals(accusation.getRoom());
    }
    
    // Handle suggestion by querying players in order, return card that disproves or null
    public Card handleSuggestion(Player accuser, Solution suggestion) {
        // Find the accuser's index in the player list
        int accuserIndex = players.indexOf(accuser);
        if (accuserIndex == -1) {
            return null; // Accuser not in player list
        }
        
        // Query each player in order, starting after the accuser
        for (int i = 1; i < players.size(); i++) {
            int currentIndex = (accuserIndex + i) % players.size();
            Player currentPlayer = players.get(currentIndex);
            
            Card disproofCard = currentPlayer.disproveSuggestion(suggestion);
            if (disproofCard != null) {
                return disproofCard; // First player to disprove
            }
        }
        
        return null; // No one could disprove
    }
    
	// Test getters
    public List<Player> getPlayers() {
        return players;
    }

    public List<String> getWeaponNames() {
        return weaponNames;
    }
    
    public List<String> getPersonNames() {
        return personNames;
    }

    public List<Card> getDeck() {
        return new ArrayList<>(deck); // Copy for test
    }

    public Solution getTheAnswer() {
        return theAnswer;
    }
    
    // Test helpers
    public void setTheAnswer(Solution answer) {
        this.theAnswer = answer;
    }
    
    public void clearPlayers() {
        players.clear();
    }
    
    public void addPlayer(Player player) {
        players.add(player);
    }
}

