package experiment;

import java.util.HashSet;
import java.util.Set;

/**
 TestBoardCell represents one cell in the grid for testing movement algorithms
 */
public class TestBoardCell {
	private int row;
	private int col;
	private boolean isRoom;
	private boolean isOccupied;
	private Set<TestBoardCell> adjList;
	
	/**
	 Constructor for TestBoardCell
	 @param row - row position of the cell
	 @param col - column position of the cell
	 */
	public TestBoardCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.isRoom = false;
		this.isOccupied = false;
		this.adjList = new HashSet<>();
	}
	
	/**
	 Add a cell to this cell's adjacency list
	 @param cell - the adjacent cell to add
	 */
	public void addAdjacency(TestBoardCell cell) {
		adjList.add(cell);
	}
	
	/**
	 Get the adjacency list for this cell
	 @return Set of adjacent cells
	 */
	public Set<TestBoardCell> getAdjList() {
		return adjList;
	}
	
	/**
	 Set whether this cell is a room
	 @param isRoom - true if cell is a room, false otherwise
	 */
	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	/**
	 Check if this cell is a room
	 @return true if cell is a room
	 */
	public boolean isRoom() {
		return isRoom;
	}
	
	/**
	 Set whether this cell is occupied by another player
	 @param isOccupied - true if cell is occupied
	 */
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	/**
	 Check if this cell is occupied
	 @return true if cell is occupied
	 */
	public boolean getOccupied() {
		return isOccupied;
	}
}

