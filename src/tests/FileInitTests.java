package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTests {
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 30;
	public static final int NUM_COLUMNS = 30;

	private static Board board;

	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		assertEquals("Atrium", board.getRoom('A').getName());
		assertEquals("Greenhouse", board.getRoom('G').getName());
		assertEquals("Observatory", board.getRoom('O').getName());
		assertEquals("Laboratory", board.getRoom('L').getName());
		assertEquals("Vault", board.getRoom('V').getName());
		assertEquals("Walkway", board.getRoom('W').getName());
	}

	@Test
	public void testBoardDimensions() {
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(6, 3);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		
		cell = board.getCell(9, 4);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		
		cell = board.getCell(3, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		
		cell = board.getCell(3, 10);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		
		cell = board.getCell(8, 8);
		assertFalse(cell.isDoorway());
	}

	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		assertEquals(17, numDoors);
	}

	@Test
	public void testRooms() {
		BoardCell cell = board.getCell(26, 26);
		Room room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Vault");
		assertFalse(cell.isLabel());
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isDoorway());

		cell = board.getCell(1, 2);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Atrium");
		assertTrue(cell.isLabel());
		assertTrue(room.getLabelCell() == cell);

		cell = board.getCell(3, 3);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Atrium");
		assertTrue(cell.isRoomCenter());
		assertTrue(room.getCenterCell() == cell);

		cell = board.getCell(1, 1);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Atrium");
		assertTrue(cell.getSecretPassage() == 'V');

		cell = board.getCell(8, 8);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Walkway");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());

		cell = board.getCell(0, 0);
		room = board.getRoom(cell);
		assertTrue(room != null);
		assertEquals(room.getName(), "Unused");
		assertFalse(cell.isRoomCenter());
		assertFalse(cell.isLabel());
	}
}

