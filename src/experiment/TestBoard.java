package experiment;

import java.util.HashSet;
import java.util.Set;


public class TestBoard {
	private TestBoardCell[][] grid;
	private Set<TestBoardCell> targets;
	final static int COLS = 4;
	final static int ROWS = 4;
	
	
	public TestBoard() {
		// Initialize the grid
		grid = new TestBoardCell[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				grid[i][j] = new TestBoardCell(i, j);
			}
		}
		
		// Initialize empty targets set
		targets = new HashSet<>();
		
		// Calculate adjacencies for each cell
		calcAdjacencies();
	}
	
	
	private void calcAdjacencies() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				TestBoardCell cell = grid[i][j];
				
				// Add cell above (if exists)
				if (i > 0) {
					cell.addAdjacency(grid[i-1][j]);
				}
				// Add cell below (if exists)
				if (i < ROWS - 1) {
					cell.addAdjacency(grid[i+1][j]);
				}
				// Add cell to the left (if exists)
				if (j > 0) {
					cell.addAdjacency(grid[i][j-1]);
				}
				// Add cell to the right (if exists)
				if (j < COLS - 1) {
					cell.addAdjacency(grid[i][j+1]);
				}
			}
		}
	}
	
	/**

	@param startCell 
	@param pathlength 
	 */
	public void calcTargets(TestBoardCell startCell, int pathlength) {
		
		targets.clear();
		// TODO: Implement algorithm in next assignment
	}
	
	/**
	Get cell at specified row and column
	@param row row index
	@param col column index
	@return the cell at the specified position
	*/
	public TestBoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	/**
	 Get the targets last created by calcTargets()
	 @return Set of target cells
	 */
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
}

