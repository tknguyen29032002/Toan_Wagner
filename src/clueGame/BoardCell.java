package clueGame;

import java.util.Set;
import java.util.HashSet;

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
	
	public BoardCell() {
		this.doorDirection = DoorDirection.NONE;
		this.initial = WALKWAY_INITIAL;
		this.secretPassage = NO_SECRET_PASSAGE;
		this.isOccupied = false;
	}
	
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
	
	public void addAdj(BoardCell adj) {
		if (adjList == null) {
			adjList = new HashSet<>();
		}
		adjList.add(adj);
	}
	
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
}

