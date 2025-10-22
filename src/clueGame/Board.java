package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board();

	private Board() {
		super();
	}

	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
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
}

