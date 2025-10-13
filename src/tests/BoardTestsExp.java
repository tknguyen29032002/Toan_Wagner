package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoard;
import experiment.TestBoardCell;

/**
 * JUnit test class for TestBoard - tests adjacency lists and target calculation
 */
public class BoardTestsExp {
	TestBoard board;
	
	/**
	 * Set up the board before each test
	 */
	@BeforeEach
	public void setUp() {
		// board should create adjacency list
		board = new TestBoard();
	}
	
	/*
	 * Test adjacencies for several different locations
	 * Test centers and edges
	 */
	
	/**
	 * Test adjacency for top left corner (0,0)
	 * Should have 2 adjacencies: right and down
	 */
	@Test
	public void testAdjacency0_0() {
		TestBoardCell cell = board.getCell(0, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	
	/**
	 * Test adjacency for bottom right corner (3,3)
	 * Should have 2 adjacencies: left and up
	 */
	@Test
	public void testAdjacency3_3() {
		TestBoardCell cell = board.getCell(3, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(2, testList.size());
	}
	
	/**
	 * Test adjacency for right edge (1,3)
	 * Should have 3 adjacencies: left, up, and down
	 */
	@Test
	public void testAdjacency1_3() {
		TestBoardCell cell = board.getCell(1, 3);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(3, testList.size());
	}
	
	/**
	 * Test adjacency for left edge (3,0)
	 * Should have 2 adjacencies: right and up
	 */
	@Test
	public void testAdjacency3_0() {
		TestBoardCell cell = board.getCell(3, 0);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(3, 1)));
		assertEquals(2, testList.size());
	}
	
	/**
	 * Test adjacency for middle of grid (2,2)
	 * Should have 4 adjacencies: left, right, up, and down
	 */
	@Test
	public void testAdjacency2_2() {
		TestBoardCell cell = board.getCell(2, 2);
		Set<TestBoardCell> testList = cell.getAdjList();
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(4, testList.size());
	}
	
	/*
	 * Test target calculations on empty board
	 * Test for several different starting locations and path lengths
	 */
	
	/**
	 * Test targets with several rolls and starting points
	 * Tests on empty board (no rooms or occupied cells)
	 */
	@Test
	public void testTargetsNormal() {
		TestBoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		
		// Test from middle location with 2 steps
		cell = board.getCell(1, 1);
		board.calcTargets(cell, 2);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		
		// Test with 1 step
		cell = board.getCell(1, 1);
		board.calcTargets(cell, 1);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		
		// Test with 4 steps from (0,0)
		cell = board.getCell(0, 0);
		board.calcTargets(cell, 4);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		
		// Test with 6 steps (maximum die roll) from (0,0)
		cell = board.getCell(0, 0);
		board.calcTargets(cell, 6);
		targets = board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		
		// Test from different starting location (2,2) with 3 steps
		cell = board.getCell(2, 2);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		
		// Test from (1,1) with 3 steps
		cell = board.getCell(1, 1);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
	}
	
	/**
	 * Test targets with a room cell
	 * Player stops immediately upon entering a room
	 */
	@Test
	public void testTargetsRoom() {
		// set up occupied cells
		board.getCell(0, 2).setRoom(true);
		board.getCell(1, 2).setOccupied(true);
		
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		// Test with room at (1,2)
		board.getCell(0, 2).setRoom(false);
		board.getCell(1, 2).setOccupied(false);
		board.getCell(1, 2).setRoom(true);
		
		cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
	}
	
	/**
	 * Test targets with occupied cells
	 * Player cannot move into or through occupied cells
	 */
	@Test
	public void testTargetsOccupied() {
		// Set cell (0,2) as occupied
		board.getCell(0, 2).setOccupied(true);
		board.getCell(1, 2).setRoom(true);
		
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		// Test with occupied cell blocking a path
		board.getCell(0, 2).setOccupied(false);
		board.getCell(1, 2).setRoom(false);
		board.getCell(1, 2).setOccupied(true);
		
		cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	/**
	 * Test targets with both rooms and occupied cells
	 * Complex scenario mixing both constraints
	 */
	@Test
	public void testTargetsMixed() {
		// set up occupied cells
		board.getCell(0, 2).setRoom(true);
		board.getCell(1, 2).setOccupied(true);
		
		TestBoardCell cell = board.getCell(0, 3);
		board.calcTargets(cell, 3);
		Set<TestBoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		// Another mixed scenario
		board.getCell(0, 2).setRoom(false);
		board.getCell(1, 2).setOccupied(false);
		board.getCell(0, 2).setOccupied(true);
		board.getCell(2, 1).setRoom(true);
		
		cell = board.getCell(3, 3);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(1, 3)));
	}
}

