package clueGame;

// Represents a room with name, center cell, and label cell
public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	private String type;
	
	public Room() {
		this.name = "";
		this.type = "";
	}
	
	public Room(String name, String Type) {
		this.name = name;
		this.type = Type;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public void setCenterCell(BoardCell cell) {
		this.centerCell = cell;
	}

	public BoardCell getLabelCell() {
		return labelCell;
	}
	
	public void setLabelCell(BoardCell cell) {
		this.labelCell = cell;
	}
	
	public String getType() { 
		return type; 
	}
}

