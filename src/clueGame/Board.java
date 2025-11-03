package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

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

	/**
	 * Private constructor to enforce singleton pattern
	 */
	private Board() {
		super();
	}

	/**
	 * Get the singleton instance of the Board
	 * @return the single Board instance
	 */
	public static Board getInstance() {
		return theInstance;
	}

	/**
	 * Initialize the board by loading setup and layout configuration files
	 * and calculating adjacencies for all cells
	 */
	public void initialize() {
		try {
			// Reset state for fresh initialization
			roomMap = null;
			grid = null;
			targets = null;
			visited = null;
			
			loadSetupConfig();
			loadLayoutConfig();
			calcAdjacencies();
		} catch (BadConfigFormatException | FileNotFoundException e) {
			System.out.println("Error loading config files: " + e.getMessage());
		}
	}

	/**
	 * Set the configuration file paths for the board
	 * @param layoutFile the name of the layout configuration file
	 * @param setupFile the name of the setup configuration file
	 */
	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = DATA_DIRECTORY + layoutFile;
		setupConfigFile = DATA_DIRECTORY + setupFile;
	}

	/**
	 * Load the setup configuration file to initialize rooms and spaces
	 * @throws BadConfigFormatException if the setup file format is invalid
	 * @throws FileNotFoundException if the setup file cannot be found
	 */
	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap<>();
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
				if (parts.length != 3) {
					throw new BadConfigFormatException("Invalid setup file format: " + line);
				}
				
				String type = parts[0].trim();
				String name = parts[1].trim();
				String initialStr = parts[2].trim();
				
				// type is either "Room" or "Space"
				if (!type.equals("Room") && !type.equals("Space")) {
					throw new BadConfigFormatException("Invalid room type (must be 'Room' or 'Space'): " + line);
				}
				
				// initial must be single character
				if (initialStr.length() != 1) {
					throw new BadConfigFormatException("Room initial must be single character: " + line);
				}
				
				char initial = initialStr.charAt(0);
				
				// Create room and add to map
				Room room = new Room(name);
				roomMap.put(initial, room);
			}
		} finally {
			scanner.close();
		}
	}

	/**
	 * Load the layout configuration file to initialize the board grid
	 * Parses cell types, doors, room centers, labels, and secret passages
	 * @throws BadConfigFormatException if the layout file format is invalid
	 * @throws FileNotFoundException if the layout file cannot be found
	 */
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
				if (!roomMap.containsKey(roomInitial)) {
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

	/**
	 * Get the room associated with the given initial character
	 * @param c the initial character of the room
	 * @return the Room object, or an empty Room if not found
	 */
	public Room getRoom(char c) {
		if (roomMap.containsKey(c)) {
			return roomMap.get(c);
		}
		return new Room();
	}

	/**
	 * Get the room associated with the given cell
	 * @param cell the BoardCell to get the room for
	 * @return the Room object, or an empty Room if not found
	 */
	public Room getRoom(BoardCell cell) {
		if (cell != null) {
			return getRoom(cell.getInitial());
		}
		return new Room();
	}

	/**
	 * Get the number of rows in the board
	 * @return the number of rows
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * Get the number of columns in the board
	 * @return the number of columns
	 */
	public int getNumColumns() {
		return numColumns;
	}

	/**
	 * Get the cell at the specified row and column
	 * @param row the row of the cell
	 * @param col the column of the cell
	 * @return the BoardCell at the specified location, or an empty cell if invalid
	 */
	public BoardCell getCell(int row, int col) {
		if (isValidCell(row, col)) {
			return grid[row][col];
		}
		return new BoardCell();
	}
	
	/**
	 * Check if the given row and column are within valid bounds
	 * @param row the row to check
	 * @param col the column to check
	 * @return true if the cell is within bounds, false otherwise
	 */
	private boolean isValidCell(int row, int col) {
		return row >= 0 && row < numRows && col >= 0 && col < numColumns;
	}
	
	/**
	 * Calculate adjacencies for all cells on the board
	 * Sets up the adjacency list for walkway cells and room centers
	 */
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
	
	/**
	 * Calculate adjacencies for room center cells
	 * Includes doorways that lead into the room and secret passages
	 * @param cell the room center cell to calculate adjacencies for
	 */
	private void calcRoomCenterAdj(BoardCell cell) {
		char roomInitial = cell.getInitial();
		
		// Add all doorways that lead into this room
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell potentialDoor = grid[row][col];
				if (potentialDoor.isDoorway()) {
					BoardCell doorTarget = getDoorTarget(potentialDoor);
					// Check if this door points to this specific room center
					if (doorTarget == cell) {
						cell.addAdj(potentialDoor);
					}
				}
			}
		}
		
		// Check for secret passages - only add once per room
		// Look for any cell in this room that has a secret passage
		boolean secretPassageAdded = false;
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell roomCell = grid[row][col];
				// If this cell is part of our room and has a secret passage
				if (roomCell.getInitial() == roomInitial && roomCell.getSecretPassage() != NO_SECRET_PASSAGE) {
					if (!secretPassageAdded) {
						char targetRoomInitial = roomCell.getSecretPassage();
						Room targetRoom = roomMap.get(targetRoomInitial);
						if (targetRoom != null && targetRoom.getCenterCell() != null) {
							cell.addAdj(targetRoom.getCenterCell());
							secretPassageAdded = true;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Calculate adjacencies for walkway cells
	 * Handles regular walkways and doorways with direction-based adjacency rules
	 * @param cell the walkway cell to calculate adjacencies for
	 */
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
	
	/**
	 * Add a walkway cell to the adjacency list if it's valid and is a walkway
	 * @param cell the cell to add the adjacency to
	 * @param row the row of the potential adjacent cell
	 * @param col the column of the potential adjacent cell
	 */
	private void addWalkwayAdj(BoardCell cell, int row, int col) {
		if (isValidCell(row, col)) {
			BoardCell adj = grid[row][col];
			if (adj.getInitial() == WALKWAY_INITIAL) {
				cell.addAdj(adj);
			}
		}
	}
	
	/**
	 * Get the room center that a door points to based on its direction
	 * @param door the door cell to find the target for
	 * @return the room center cell that the door leads to, or null if not found
	 */
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
	
	/**
	 * Calculate adjacency list for a cell at the given row and column
	 * @param row the row of the cell
	 * @param col the column of the cell
	 * @return the set of adjacent cells
	 */
	public Set<BoardCell> getAdjList(int row, int col) {
		BoardCell cell = getCell(row, col);
		if (cell != null && cell.getAdjList() != null) {
			return cell.getAdjList();
		}
		return new HashSet<BoardCell>();
	}
	
	/**
	 * Calculate all possible target cells from starting cell
	 * @param startCell the starting cell
	 * @param pathLength the number of steps to take
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	
	/**
	 * Recursively find all target cells reachable from the current cell
	 * Uses backtracking to explore all possible paths
	 * @param cell the current cell being explored
	 * @param numSteps the number of steps remaining
	 */
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
	
	/**
	 * Get the set of target cells from the last calcTargets call
	 * @return the set of target cells
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}
}

