package tests;

import clueGame.Board;
import clueGame.BoardCell;

public class CheckRoomBounds {
	public static void main(String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
		
		System.out.println("=== Checking cells between Atrium center (3,3) and doors ===");
		System.out.println("Path from (3,3) to door (3,6):");
		for (int col = 3; col <= 6; col++) {
			BoardCell cell = board.getCell(3, col);
			System.out.println("  (3," + col + "): " + cell.getInitial() + 
			                   " isCenter=" + cell.isRoomCenter() + 
			                   " isDoor=" + cell.isDoorway());
		}
		
		System.out.println("\nPath from (3,3) to door (6,3):");
		for (int row = 3; row <= 6; row++) {
			BoardCell cell = board.getCell(row, 3);
			System.out.println("  (" + row + ",3): " + cell.getInitial() + 
			                   " isCenter=" + cell.isRoomCenter() + 
			                   " isDoor=" + cell.isDoorway());
		}
		
		System.out.println("\n=== Checking cells between Forge center (12,13) and doors ===");
		System.out.println("Path from (12,13) to door (12,10):");
		for (int col = 10; col <= 13; col++) {
			BoardCell cell = board.getCell(12, col);
			System.out.println("  (12," + col + "): " + cell.getInitial() + 
			                   " isCenter=" + cell.isRoomCenter() + 
			                   " isDoor=" + cell.isDoorway());
		}
		
		System.out.println("\nPath from (12,13) to door (9,13):");
		for (int row = 9; row <= 12; row++) {
			BoardCell cell = board.getCell(row, 13);
			System.out.println("  (" + row + ",13): " + cell.getInitial() + 
			                   " isCenter=" + cell.isRoomCenter() + 
			                   " isDoor=" + cell.isDoorway());
		}
	}
}

