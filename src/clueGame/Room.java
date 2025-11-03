package clueGame;

/**
 * Represents a room on the Clue game board
 * Each room has a name, a center cell for player movement, and a label cell for display
 */
public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	/**
	 * Default constructor creates an empty room with no name
	 */
	public Room() {
		this.name = "";
	}
	
	/**
	 * Constructor to create a room with a specific name
	 * @param name the name of the room
	 */
	public Room(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the room
	 * @return the room name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the room
	 * @param name the room name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the center cell of the room where players land when entering
	 * @return the center BoardCell
	 */
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	/**
	 * Set the center cell of the room
	 * @param cell the BoardCell to set as the center
	 */
	public void setCenterCell(BoardCell cell) {
		this.centerCell = cell;
	}

	/**
	 * Get the label cell where the room name is displayed
	 * @return the label BoardCell
	 */
	public BoardCell getLabelCell() {
		return labelCell;
	}
	
	/**
	 * Set the label cell for the room
	 * @param cell the BoardCell to set as the label cell
	 */
	public void setLabelCell(BoardCell cell) {
		this.labelCell = cell;
	}
}

