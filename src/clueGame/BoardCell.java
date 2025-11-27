package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import java.util.HashSet;

// Represents a single cell on the Clue board with position and properties
public class BoardCell {
	// Constants
	private static final char WALKWAY_INITIAL = 'W';
	private static final char NO_SECRET_PASSAGE = ' ';
	
	private int row;
	private int col;
	private char initial;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	private Set<BoardCell> adjList;
	private boolean isOccupied;
	
	// Default constructor creates a walkway cell
	public BoardCell() {
		this.doorDirection = DoorDirection.NONE;
		this.initial = WALKWAY_INITIAL;
		this.secretPassage = NO_SECRET_PASSAGE;
		this.isOccupied = false;
	}
	
	// Create cell at position with room initial
	public BoardCell(int row, int col, char initial) {
		this.row = row;
		this.col = col;
		this.initial = initial;
		this.doorDirection = DoorDirection.NONE;
		this.roomLabel = false;
		this.roomCenter = false;
		this.secretPassage = NO_SECRET_PASSAGE;
		this.isOccupied = false;
	}

	// Check if cell is a doorway
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public void setDoorDirection(DoorDirection direction) {
		this.doorDirection = direction;
	}

	public boolean isLabel() {
		return roomLabel;
	}
	
	public void setLabel(boolean isLabel) {
		this.roomLabel = isLabel;
	}

	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public void setRoomCenter(boolean isCenter) {
		this.roomCenter = isCenter;
	}

	public char getSecretPassage() {
		return secretPassage;
	}
	
	public void setSecretPassage(char passage) {
		this.secretPassage = passage;
	}

	public char getInitial() {
		return initial;
	}
	
	public void setInitial(char initial) {
		this.initial = initial;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	// Add adjacent cell to adjacency list
	public void addAdj(BoardCell adj) {
		if (adjList == null) {
			adjList = new HashSet<>();
		}
		adjList.add(adj);
	}
	
	// Return set of adjacent cells
	public Set<BoardCell> getAdjList() {
		if (adjList == null) {
			adjList = new HashSet<>();
		}
		return adjList;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
	
	// Draw this cell on the board
	public void draw(Graphics g, int cellWidth, int cellHeight, boolean isTarget) {
		int x = col * cellWidth;
		int y = row * cellHeight;
		
		// Determine cell color and drawing based on type
		if (initial == WALKWAY_INITIAL) {
			// Draw walkway as yellow with black border
			g.setColor(Color.YELLOW);
			g.fillRect(x, y, cellWidth, cellHeight);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, cellWidth, cellHeight);
			
			// Draw door indicators ONLY on walkway cells (doors connect walkways to rooms)
			if (isDoorway()) {
				g.setColor(Color.BLUE);
				int doorThickness = 5;
				
				switch (doorDirection) {
					case UP:
						g.fillRect(x, y, cellWidth, doorThickness);
						break;
					case DOWN:
						g.fillRect(x, y + cellHeight - doorThickness, cellWidth, doorThickness);
						break;
					case LEFT:
						g.fillRect(x, y, doorThickness, cellHeight);
						break;
					case RIGHT:
						g.fillRect(x + cellWidth - doorThickness, y, doorThickness, cellHeight);
						break;
					default:
						break;
				}
			}
		} else if (initial == 'X') {
			// Draw unused spaces as black
			g.setColor(Color.BLACK);
			g.fillRect(x, y, cellWidth, cellHeight);
		} else {
			// Draw room cells as gray (no borders, no door indicators)
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x, y, cellWidth, cellHeight);
		}
		
		// Highlight target cells (overwrites cell color)
		if (isTarget) {
			g.setColor(Color.CYAN);
			g.fillRect(x, y, cellWidth, cellHeight);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, cellWidth, cellHeight);
		}
	}
}

