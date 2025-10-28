package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		board.initialize();
	}

	// Test room center adjacencies including secret passages
	@Test
	public void testAdjacenciesRooms()
	{
		// Atrium with secret passage to Vault
		Set<BoardCell> testList = board.getAdjList(3, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(3, 6)));
		assertTrue(testList.contains(board.getCell(23, 24)));
		
		// Forge with one door
		testList = board.getAdjList(12, 13);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(12, 10)));
		
		// Vault with secret passage to Atrium
		testList = board.getAdjList(23, 24);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(23, 21))); // Door at (23,21) points right to Vault
		assertTrue(testList.contains(board.getCell(3, 3)));
	}

	// Test door adjacencies include room center and walkways
	@Test
	public void testAdjacencyDoor()
	{
		// Door entering Atrium (facing right)
		Set<BoardCell> testList = board.getAdjList(3, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(3, 7)));
		assertTrue(testList.contains(board.getCell(2, 6)));

		// Door below Atrium (facing up)
		testList = board.getAdjList(6, 3);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(6, 2)));
		assertTrue(testList.contains(board.getCell(6, 4)));
		assertTrue(testList.contains(board.getCell(7, 3)));
		
		// Door above Laboratory (facing down)
		testList = board.getAdjList(9, 4);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(12, 5)));
		assertTrue(testList.contains(board.getCell(9, 3)));
		assertTrue(testList.contains(board.getCell(9, 5)));
		assertTrue(testList.contains(board.getCell(8, 4)));
	}
	
	// Test various walkway adjacencies
	@Test
	public void testAdjacencyWalkways()
	{
		// Top edge walkways
		Set<BoardCell> testList = board.getAdjList(1, 7);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(1, 6)));
		assertTrue(testList.contains(board.getCell(1, 8)));
		
		// Near door but not adjacent
		testList = board.getAdjList(7, 5);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(7, 4)));
		assertTrue(testList.contains(board.getCell(7, 6)));
		assertTrue(testList.contains(board.getCell(6, 5)));
		assertTrue(testList.contains(board.getCell(8, 5)));

		// Center area walkways
		testList = board.getAdjList(17, 10);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(17, 9)));
		assertTrue(testList.contains(board.getCell(17, 11)));
		assertTrue(testList.contains(board.getCell(16, 10)));
		assertTrue(testList.contains(board.getCell(18, 10)));

		// Next to unused space
		testList = board.getAdjList(1, 21);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(1, 20)));
		assertTrue(testList.contains(board.getCell(2, 21)));
	}
	
	// Test non-center room cells have empty adjacency lists
	@Test
	public void testAdjacencyInsideRoom()
	{
		Set<BoardCell> testList = board.getAdjList(1, 1);
		assertEquals(0, testList.size());
		
		testList = board.getAdjList(11, 1);
		assertEquals(0, testList.size());
		
		testList = board.getAdjList(25, 25);
		assertEquals(0, testList.size());
	}
	
	// Test adjacencies at board edges
	@Test
	public void testAdjacencyEdges()
	{
		// Top edge
		Set<BoardCell> testList = board.getAdjList(1, 10);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(1, 9)));
		assertTrue(testList.contains(board.getCell(2, 10)));
		
		// Bottom edge
		testList = board.getAdjList(28, 15);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(28, 14)));
		assertTrue(testList.contains(board.getCell(28, 16)));
		
		// Left edge (room cell)
		testList = board.getAdjList(10, 1);
		assertEquals(0, testList.size());
		
		// Right edge
		testList = board.getAdjList(8, 28);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(7, 28)));
		assertTrue(testList.contains(board.getCell(9, 28)));
	}
	
	// Test walkways beside rooms without doors
	@Test
	public void testAdjacencyBesideRoom()
	{
		Set<BoardCell> testList = board.getAdjList(5, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(6, 6)));
		assertTrue(testList.contains(board.getCell(5, 7)));
		assertTrue(testList.contains(board.getCell(4, 6)));
		
		testList = board.getAdjList(16, 10);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(16, 9)));
		assertTrue(testList.contains(board.getCell(16, 11)));
		assertTrue(testList.contains(board.getCell(17, 10)));
		
		testList = board.getAdjList(23, 17);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(23, 18)));
		assertTrue(testList.contains(board.getCell(22, 17)));
		assertTrue(testList.contains(board.getCell(24, 17)));
	}
	
	
	// Test targets leaving room with secret passage
	@Test
	public void testTargetsInAtrium() {
		board.calcTargets(board.getCell(3, 3), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 6)));
		assertTrue(targets.contains(board.getCell(23, 24)));
		
		board.calcTargets(board.getCell(3, 3), 3);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(3, 8)));
		assertTrue(targets.contains(board.getCell(2, 7)));
		assertTrue(targets.contains(board.getCell(23, 24)));
		
		board.calcTargets(board.getCell(3, 3), 4);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(3, 9)));
		assertTrue(targets.contains(board.getCell(1, 7)));
		assertTrue(targets.contains(board.getCell(23, 24)));
	}
	
	@Test
	public void testTargetsInVault() {
		board.calcTargets(board.getCell(23, 24), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(23, 21))); // Door position corrected
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		board.calcTargets(board.getCell(23, 24), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(23, 19)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		board.calcTargets(board.getCell(23, 24), 4);
		targets= board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
	}

	// Test targets leaving room without secret passage
	@Test
	public void testTargetsInForge() {
		board.calcTargets(board.getCell(12, 13), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(12, 10)));
		
		board.calcTargets(board.getCell(12, 13), 3);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		
		board.calcTargets(board.getCell(12, 13), 4);
		targets= board.getTargets();
		assertEquals(11, targets.size());
	}
	
	// Test targets from door location
	@Test
	public void testTargetsAtDoor() {
		board.calcTargets(board.getCell(3, 6), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(3, 7)));
		assertTrue(targets.contains(board.getCell(2, 6)));
		
		board.calcTargets(board.getCell(3, 6), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(3, 9)));
		assertTrue(targets.contains(board.getCell(1, 7)));
		assertTrue(targets.contains(board.getCell(5, 7)));
		
		board.calcTargets(board.getCell(3, 6), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(3, 10)));
		assertTrue(targets.contains(board.getCell(1, 6)));
		assertTrue(targets.contains(board.getCell(6, 7)));
	}

	// Test targets from walkways
	@Test
	public void testTargetsInWalkway1() {
		board.calcTargets(board.getCell(7, 7), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(7, 6)));
		assertTrue(targets.contains(board.getCell(7, 8)));
		assertTrue(targets.contains(board.getCell(6, 7)));
		assertTrue(targets.contains(board.getCell(8, 7)));
		
		board.calcTargets(board.getCell(7, 7), 3);
		targets= board.getTargets();
		assertEquals(15, targets.size());
		assertTrue(targets.contains(board.getCell(7, 4)));
		assertTrue(targets.contains(board.getCell(7, 10)));
		assertTrue(targets.contains(board.getCell(4, 7)));
		assertTrue(targets.contains(board.getCell(9, 6)));
		
		board.calcTargets(board.getCell(7, 7), 4);
		targets= board.getTargets();
		assertEquals(21, targets.size());
		assertTrue(targets.contains(board.getCell(7, 3)));
		assertTrue(targets.contains(board.getCell(7, 11)));
		assertTrue(targets.contains(board.getCell(9, 5)));
	}

	@Test
	public void testTargetsInWalkway2() {
		board.calcTargets(board.getCell(17, 10), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(17, 9)));
		assertTrue(targets.contains(board.getCell(17, 11)));
		assertTrue(targets.contains(board.getCell(16, 10)));
		assertTrue(targets.contains(board.getCell(18, 10)));
		
		board.calcTargets(board.getCell(17, 10), 3);
		targets= board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(17, 7)));
		assertTrue(targets.contains(board.getCell(20, 10)));
		assertTrue(targets.contains(board.getCell(19, 9)));
		
		board.calcTargets(board.getCell(17, 10), 4);
		targets= board.getTargets();
		assertEquals(18, targets.size());
	}

	// Test targets with occupied cells blocking paths
	@Test
	public void testTargetsOccupied() {
		// Blocked cell reduces target options
		board.getCell(7, 9).setOccupied(true);
		board.calcTargets(board.getCell(7, 7), 3);
		board.getCell(7, 9).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(7, 4)));
		assertTrue(targets.contains(board.getCell(9, 6)));
		assertFalse(targets.contains(board.getCell(7, 10)));
	
		// Can still enter occupied room center
		board.getCell(3, 3).setOccupied(true);
		board.getCell(3, 7).setOccupied(true);
		board.calcTargets(board.getCell(3, 6), 1);
		board.getCell(3, 3).setOccupied(false);
		board.getCell(3, 7).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 6)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		// Blocked doorway forces use of secret passage
		board.getCell(3, 6).setOccupied(true);
		board.calcTargets(board.getCell(3, 3), 3);
		board.getCell(3, 6).setOccupied(false);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(23, 24)));
	}
}

