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
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private static Board theInstance = new Board();

	private Board() {
		super();
	}

	public static Board getInstance() {
		return theInstance;
	}

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

	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = "data/" + layoutFile;
		setupConfigFile = "data/" + setupFile;
	}

	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {
		roomMap = new HashMap<>();
		FileReader reader = new FileReader(setupConfigFile);
		Scanner scanner = new Scanner(reader);
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			
			// Skip empty lines and comments
			if (line.isEmpty() || line.startsWith("//")) {
				continue;
			}
			
			// Split by comma and trim each part
			String[] parts = line.split(",");
			if (parts.length != 3) {
				scanner.close();
				throw new BadConfigFormatException("Invalid setup file format: " + line);
			}
//* couting room and walkway is the naother solution &*
			String type = parts[0].trim();
			String name = parts[1].trim();
			String initialStr = parts[2].trim();
			
			// type is either "Room" or "Space"
			if (!type.equals("Room") && !type.equals("Space")) {
				scanner.close();
				throw new BadConfigFormatException("Invalid room type (must be 'Room' or 'Space'): " + line);
			}
			
			// initial must be single character
			if (initialStr.length() != 1) {
				scanner.close();
				throw new BadConfigFormatException("Room initial must be single character: " + line);
			}
			
			char initial = initialStr.charAt(0);
			
			// Create room and add to map
			Room room = new Room(name);
			roomMap.put(initial, room);
		}
		
		scanner.close();
	}

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
						case '#':  // Label cell
							cell.setLabel(true);
							roomMap.get(roomInitial).setLabelCell(cell);
							break;
						case '*':  // Center cell
							cell.setRoomCenter(true);
							roomMap.get(roomInitial).setCenterCell(cell);
							break;
						case '^':  // Door facing UP
							cell.setDoorDirection(DoorDirection.UP);
							break;
						case 'v':  // Door facing DOWN
							cell.setDoorDirection(DoorDirection.DOWN);
							break;
						case '<':  // Door facing LEFT
							cell.setDoorDirection(DoorDirection.LEFT);
							break;
						case '>':  // Door facing RIGHT
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

	public Room getRoom(char c) {
		if (roomMap.containsKey(c)) {
			return roomMap.get(c);
		}
		return new Room();
	}

	public Room getRoom(BoardCell cell) {
		if (cell != null) {
			return getRoom(cell.getInitial());
		}
		return new Room();
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCell(int row, int col) {
		if (row >= 0 && row < numRows && col >= 0 && col < numColumns) {
			return grid[row][col];
		}
		return new BoardCell();
	}
	
	// Calculate adjacencies for all cells on the board
	private void calcAdjacencies() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				BoardCell cell = grid[row][col];
				
				// Room cells that are not centers have no adjacencies
				if (cell.getInitial() != 'W' && !cell.isRoomCenter()) {
					continue;
				}
				
				// Room center cells
				if (cell.isRoomCenter()) {
					calcRoomCenterAdj(cell);
				}
				// Walkway cells
				else if (cell.getInitial() == 'W') {
					calcWalkwayAdj(cell);
				}
			}
		}
	}
	
	// Calculate adjacencies for room center cells
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
				if (roomCell.getInitial() == roomInitial && roomCell.getSecretPassage() != ' ') {
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
	
	// Calculate adjacencies for walkway cells
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
	
	// Add adjacent walkway if valid
	private void addWalkwayAdj(BoardCell cell, int row, int col) {
		if (row >= 0 && row < numRows && col >= 0 && col < numColumns) {
			BoardCell adj = grid[row][col];
			if (adj.getInitial() == 'W') {
				cell.addAdj(adj);
			}
		}
	}
	
	// Get the room center that a door points to
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
		if (targetCell != null && targetCell.getInitial() != 'W') {
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
	
	// Recursive method to find all targets
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

