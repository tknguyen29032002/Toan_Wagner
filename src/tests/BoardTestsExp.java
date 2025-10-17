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
    private TestBoard board;

    @BeforeEach
    public void setUp() {
        board = new TestBoard();
    }

    /* -------------------------
     *  ADJACENCY TESTS
     * ------------------------- */

    @Test
    public void testAdjacency0_0() {
        TestBoardCell cell = board.getCell(0, 0);
        Set<TestBoardCell> list = cell.getAdjList();
        assertTrue(list.contains(board.getCell(1, 0)));
        assertTrue(list.contains(board.getCell(0, 1)));
        assertEquals(2, list.size());
    }

    @Test
    public void testAdjacency3_3() {
        TestBoardCell cell = board.getCell(3, 3);
        Set<TestBoardCell> list = cell.getAdjList();
        assertTrue(list.contains(board.getCell(3, 2)));
        assertTrue(list.contains(board.getCell(2, 3)));
        assertEquals(2, list.size());
    }

    @Test
    public void testAdjacency1_3() {
        TestBoardCell cell = board.getCell(1, 3);
        Set<TestBoardCell> list = cell.getAdjList();
        assertTrue(list.contains(board.getCell(0, 3)));
        assertTrue(list.contains(board.getCell(1, 2)));
        assertTrue(list.contains(board.getCell(2, 3)));
        assertEquals(3, list.size());
    }

    @Test
    public void testAdjacency3_0() {
        TestBoardCell cell = board.getCell(3, 0);
        Set<TestBoardCell> list = cell.getAdjList();
        assertTrue(list.contains(board.getCell(2, 0)));
        assertTrue(list.contains(board.getCell(3, 1)));
        assertEquals(2, list.size());
    }

    @Test
    public void testAdjacency2_2() {
        TestBoardCell cell = board.getCell(2, 2);
        Set<TestBoardCell> list = cell.getAdjList();
        assertTrue(list.contains(board.getCell(1, 2)));
        assertTrue(list.contains(board.getCell(3, 2)));
        assertTrue(list.contains(board.getCell(2, 1)));
        assertTrue(list.contains(board.getCell(2, 3)));
        assertEquals(4, list.size());
    }



    @Test
    public void testTargetsNormal() {
        // (0,0) 3 steps
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

        // (1,1) 2 steps
        cell = board.getCell(1, 1);
        board.calcTargets(cell, 2);
        targets = board.getTargets();
        
        assertTrue(targets.contains(board.getCell(0, 0)));
        assertTrue(targets.contains(board.getCell(1, 3)));
        assertTrue(targets.contains(board.getCell(2, 2)));
        assertTrue(targets.contains(board.getCell(3, 1)));
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertTrue(targets.contains(board.getCell(2, 0)));

        // (1,1) 1 step
        cell = board.getCell(1, 1);
        board.calcTargets(cell, 1);
        targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(0, 1)));
        assertTrue(targets.contains(board.getCell(2, 1)));
        assertTrue(targets.contains(board.getCell(1, 0)));
        assertTrue(targets.contains(board.getCell(1, 2)));

        // (0,0) 4 steps
        cell = board.getCell(0, 0);
        board.calcTargets(cell, 4);
        targets = board.getTargets();
        assertTrue(targets.contains(board.getCell(0, 0)));
        assertTrue(targets.contains(board.getCell(1, 1)));
        assertTrue(targets.contains(board.getCell(2, 2)));
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertTrue(targets.contains(board.getCell(2, 0)));

        // (0,0) 6 steps
        cell = board.getCell(0, 0);
        board.calcTargets(cell, 6);
        targets = board.getTargets();
        assertTrue(targets.contains(board.getCell(0, 0)));
        assertTrue(targets.contains(board.getCell(1, 1)));
        assertTrue(targets.contains(board.getCell(2, 2)));
        assertTrue(targets.contains(board.getCell(3, 3)));
        assertTrue(targets.contains(board.getCell(0, 2)));
        assertTrue(targets.contains(board.getCell(2, 0)));
        assertTrue(targets.contains(board.getCell(1, 3)));
    }

    @Test
    public void testTargetsRoom() {
        // Scenario 1: Room - player stops immediately upon entering
        board.getCell(1, 2).setRoom(true);
        
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(3, 0)));
        assertTrue(targets.contains(board.getCell(2, 1)));
        assertTrue(targets.contains(board.getCell(0, 1)));
        assertTrue(targets.contains(board.getCell(1, 2)));  // Room - stop early
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(1, 0)));

        // Reset
        board.getCell(1, 2).setRoom(false);

        // Scenario 2: Room at different location
        board.getCell(2, 1).setRoom(true);
        
        cell = board.getCell(3, 3);//start cell
        board.calcTargets(cell, 3); // target
        targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(2, 1)));  // Room
        assertTrue(targets.contains(board.getCell(3, 0)));
        assertTrue(targets.contains(board.getCell(2, 3)));
        assertTrue(targets.contains(board.getCell(3, 2)));
        assertTrue(targets.contains(board.getCell(1, 2)));
        
        // Reset
        board.getCell(2, 1).setRoom(false);
    }

    @Test
    public void testTargetsOccupied() {
        // Scenario 1: Occupied cell blocking paths
        board.getCell(1, 2).setOccupied(true);

        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(5, targets.size());
        assertTrue(targets.contains(board.getCell(3, 0)));
        assertTrue(targets.contains(board.getCell(2, 1)));
        assertTrue(targets.contains(board.getCell(0, 1)));
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(1, 0)));

        // Reset
        board.getCell(1, 2).setOccupied(false);

        // Scenario 2: Occupied at different location
        board.getCell(0, 2).setOccupied(true);

        cell = board.getCell(0, 3);
        board.calcTargets(cell, 3);
        targets = board.getTargets();
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(1, 3)));
        assertTrue(targets.contains(board.getCell(2, 2)));
        assertTrue(targets.contains(board.getCell(3, 3)));
        
        // Reset
        board.getCell(0, 2).setOccupied(false);
    }

    @Test
    public void testTargetsMixed() {
        // Scenario 1
        board.getCell(0, 2).setOccupied(true);
        board.getCell(1, 2).setRoom(true);

        TestBoardCell cell = board.getCell(0, 3);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(1, 2)));
        assertTrue(targets.contains(board.getCell(2, 2)));
        assertTrue(targets.contains(board.getCell(3, 3)));

        // Reset
        board.getCell(1, 2).setRoom(false);
        board.getCell(1, 2).setOccupied(false);
        board.getCell(0, 2).setRoom(false);

        // Scenario 2
        board.getCell(0, 2).setOccupied(true);
        board.getCell(2, 1).setRoom(true);

        cell = board.getCell(3, 3);
        board.calcTargets(cell, 3);
        targets = board.getTargets();
        
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(2, 1)));
        assertTrue(targets.contains(board.getCell(3, 0)));
    }
}
