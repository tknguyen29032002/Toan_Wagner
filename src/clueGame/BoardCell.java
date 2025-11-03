package clueGame;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a single cell on the Clue game board
 * Each cell has a position, room type, and may have special properties
 * such as being a doorway, room center, or containing a secret passage
 */
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
	
	/**
	 * Default constructor creates a walkway cell with no special properties
	 */
	public BoardCell() {
		this.doorDirection = DoorDirection.NONE;
		this.initial = WALKWAY_INITIAL;
		this.secretPassage = NO_SECRET_PASSAGE;
		this.isOccupied = false;
	}
	
	/**
	 * Constructor to create a cell at a specific position with a room type
	 * @param row the row position of the cell
	 * @param col the column position of the cell
	 * @param initial the room initial character
	 */
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

	/**
	 * Check if this cell is a doorway
	 * @return true if the cell is a doorway, false otherwise
	 */
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}

	/**
	 * Get the direction the door faces
	 * @return the DoorDirection enum value
	 */
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	/**
	 * Set the direction the door faces
	 * @param direction the DoorDirection to set
	 */
	public void setDoorDirection(DoorDirection direction) {
		this.doorDirection = direction;
	}

	/**
	 * Check if this cell contains a room label
	 * @return true if this is a label cell, false otherwise
	 */
	public boolean isLabel() {
		return roomLabel;
	}
	
	/**
	 * Set whether this cell contains a room label
	 * @param isLabel true if this is a label cell
	 */
	public void setLabel(boolean isLabel) {
		this.roomLabel = isLabel;
	}

	/**
	 * Check if this cell is a room center
	 * @return true if this is a room center cell, false otherwise
	 */
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	/**
	 * Set whether this cell is a room center
	 * @param isCenter true if this is a room center
	 */
	public void setRoomCenter(boolean isCenter) {
		this.roomCenter = isCenter;
	}

	/**
	 * Get the secret passage character if this cell has one
	 * @return the room initial that this passage leads to, or space if none
	 */
	public char getSecretPassage() {
		return secretPassage;
	}
	
	/**
	 * Set the secret passage for this cell
	 * @param passage the room initial that this passage leads to
	 */
	public void setSecretPassage(char passage) {
		this.secretPassage = passage;
	}

	/**
	 * Get the room initial character for this cell
	 * @return the room initial character
	 */
	public char getInitial() {
		return initial;
	}
	
	/**
	 * Set the room initial character for this cell
	 * @param initial the room initial character
	 */
	public void setInitial(char initial) {
		this.initial = initial;
	}
	
	/**
	 * Get the row position of this cell
	 * @return the row position
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Get the column position of this cell
	 * @return the column position
	 */
	public int getCol() {
		return col;
	}
	
	/**
	 * Add an adjacent cell to this cell's adjacency list
	 * @param adj the adjacent cell to add
	 */
	public void addAdj(BoardCell adj) {
		if (adjList == null) {
			adjList = new HashSet<>();
		}
		adjList.add(adj);
	}
	
	/**
	 * Get the set of adjacent cells for this cell
	 * @return a Set of adjacent BoardCell objects
	 */
	public Set<BoardCell> getAdjList() {
		if (adjList == null) {
			adjList = new HashSet<>();
		}
		return adjList;
	}
	
	/**
	 * Check if this cell is occupied by a player
	 * @return true if occupied, false otherwise
	 */
	public boolean isOccupied() {
		return isOccupied;
	}
	
	/**
	 * Set whether this cell is occupied by a player
	 * @param occupied true if occupied
	 */
	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
}

