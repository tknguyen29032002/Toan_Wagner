package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell;
	private BoardCell labelCell;
	
	public Room() {
		this.name = "";
	}
	
	public Room(String name) {
		this.name = name;
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
}

