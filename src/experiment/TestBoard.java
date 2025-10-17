package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	private TestBoardCell startCell; // thanks: allow "end on start" on final step

	final static int COLS = 4;
	final static int ROWS = 4;

	public TestBoard() {
		grid = new TestBoardCell[ROWS][COLS];
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				grid[r][c] = new TestBoardCell(r, c);
			}
		}
		targets = new HashSet<>();
		visited = new HashSet<>();
		calcAdjacencies();
	}
	//aloow the user to move to the adjacent cells
	private void calcAdjacencies() {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				TestBoardCell cell = grid[r][c];
				if (r > 0) cell.addAdjacency(grid[r - 1][c]);
				if (r < ROWS - 1) cell.addAdjacency(grid[r + 1][c]);
				if (c > 0) cell.addAdjacency(grid[r][c - 1]);
				if (c < COLS - 1) cell.addAdjacency(grid[r][c + 1]);
			}
		}
	}

	//calculate the targets depends on the start cell and the pathlength
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		targets.clear();
		visited.clear();

		this.startCell = startCell; // thanks
		visited.add(startCell);     // thanks
		findAllTargets(startCell, pathlength);
	}

	private void findAllTargets(TestBoardCell current, int stepsRemaining) {
	    for (TestBoardCell adj : current.getAdjList()) {

	        // Rule 1: Can't go into or through occupied cells
	        if (adj.getOccupied()) continue;

	        // Rule 2: If you step into a room → add it and stop exploring that path
	        if (adj.isRoom()) {
	            targets.add(adj);
	            continue;
	        }

	        // Rule 3: Handle start cell — can end here only on last step
	        if (adj == startCell && stepsRemaining == 1) {
	            targets.add(adj);
	            continue;
	        }

	        // Rule 4: Skip already visited to avoid infinite loops
	        if (visited.contains(adj)) continue;

	        // 🚶 Rule 5: Normal exploration
	        visited.add(adj);
	        if (stepsRemaining == 1) {
	            targets.add(adj);
	        } else {
	            findAllTargets(adj, stepsRemaining - 1);
	        }
	        visited.remove(adj); // backtrack
	    }
	}



	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}

	public Set<TestBoardCell> getTargets() {
		return targets;
	}
}
