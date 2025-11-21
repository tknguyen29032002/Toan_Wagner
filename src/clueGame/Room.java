package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
	
	// Draw the room name at the label cell location
	public void draw(Graphics g, int cellWidth, int cellHeight) {
		// Only draw name if we have a label cell and this is a room (not a space)
		if (labelCell != null && "Room".equals(type)) {
			int x = labelCell.getCol() * cellWidth;
			int y = labelCell.getRow() * cellHeight;
			
			// Set font and color for room name
			g.setColor(Color.BLUE);
			g.setFont(new Font("Arial", Font.BOLD, 14));
			
			// Draw the room name
			g.drawString(name, x + 5, y + cellHeight / 2);
		}
	}
}

