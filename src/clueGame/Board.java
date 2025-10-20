package clueGame;

import java.io.FileNotFoundException;
import java.util.Map;

public class Board {
	private BoardCell[][] grid;
	private int numRows;
	private int numColumns;
	private String layoutConfigFile;
	private String setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board();

	private Board() {
		super();
	}

	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {

	}

	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = layoutFile;
		setupConfigFile = setupFile;
	}

	public void loadSetupConfig() throws BadConfigFormatException, FileNotFoundException {

	}

	public void loadLayoutConfig() throws BadConfigFormatException, FileNotFoundException {

	}

	public Room getRoom(char c) {
		return null;
	}

	public Room getRoom(BoardCell cell) {
		return null;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCell(int row, int col) {
		return null;
	}
}

