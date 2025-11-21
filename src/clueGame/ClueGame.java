package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gui.GameControlPanel;
import gui.KnownCardsPanel;

/**
 * Main entry point for the Clue Game GUI
 * This class creates the game window and integrates all panels
 * 
 * @author Toan Nguyen and Wagner
 */
public class ClueGame extends JFrame {
	
	// Singleton board instance
	private Board board;
	
	// GUI panels
	private GameControlPanel controlPanel;
	private KnownCardsPanel cardsPanel;
	
	// Reference to human player for updates
	private HumanPlayer humanPlayer;
	
	/**
	 * Constructor - sets up the game window and all panels
	 */
	public ClueGame() {
		// Initialize the board
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		
		// Deal cards to players
		board.createDeck();
		board.dealCards();
		
		// Set up the frame
		setTitle("Clue Game");
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Set layout to BorderLayout explicitly
		setLayout(new BorderLayout());
		
		// Create and add panels
		controlPanel = new GameControlPanel();
		cardsPanel = new KnownCardsPanel();
		
		// Set preferred sizes for panels to ensure they're visible
		controlPanel.setPreferredSize(new Dimension(800, 180));
		cardsPanel.setPreferredSize(new Dimension(220, 700));
		
		// Add board to center
		add(board, BorderLayout.CENTER);
		
		// Add control panel to south
		add(controlPanel, BorderLayout.SOUTH);
		
		// Add known cards panel to east
		add(cardsPanel, BorderLayout.EAST);
		
		// Initialize the cards panel with human player's hand
		// Find the human player
		for (Player player : board.getPlayers()) {
			if (player instanceof HumanPlayer) {
				humanPlayer = (HumanPlayer) player;
				break;
			}
		}
		
		if (humanPlayer != null) {
			cardsPanel.updatePanel(humanPlayer.getHand(), humanPlayer.getSeenCards());
		}
		
		// Show welcome message
		JOptionPane.showMessageDialog(this, 
			"You are " + (humanPlayer != null ? humanPlayer.getName() : "Miss Scarlet") + ".\nCan you find the solution\nbefore the Computer players?",
			"Welcome to Clue", 
			JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Update the known cards panel when the human player sees a new card
	 * Call this whenever a card is shown to the human player during gameplay
	 * @param card The card that was seen
	 * @param owner The player who showed the card
	 */
	public void updateHumanSeenCard(Card card, Player owner) {
		if (humanPlayer != null) {
			humanPlayer.updateSeen(card, owner);
			cardsPanel.updatePanel(humanPlayer.getHand(), humanPlayer.getSeenCards());
		}
	}
	
	/**
	 * Refresh the known cards panel (useful after any game state change)
	 */
	public void refreshKnownCardsPanel() {
		if (humanPlayer != null) {
			cardsPanel.updatePanel(humanPlayer.getHand(), humanPlayer.getSeenCards());
		}
	}
	
	/**
	 * Main method - entry point for the application
	 * @param args Command line arguments (not used)
	 */
	public static void main(String[] args) {
		// Create and display the game
		ClueGame game = new ClueGame();
		game.setVisible(true);
	}
}

