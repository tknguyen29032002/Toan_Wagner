package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gui.GameControlPanel;
import gui.KnownCardsPanel;
import gui.SuggestionDialog;

/**
 * Main entry point for the Clue Game GUI
 * This class creates the game window and integrates all panels
 * Handles game flow, turn management, and player interactions
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
	
	// Game state
	private int currentPlayerIndex;
	private int currentRoll;
	private Set<BoardCell> currentTargets;
	private boolean humanTurnInProgress;
	private boolean hasMovedThisTurn;
	private Random random;
	
	/**
	 * Constructor - sets up the game window and all panels
	 */
	public ClueGame() {
		// Initialize game state
		random = new Random();
		currentPlayerIndex = 0;
		currentRoll = 0;
		humanTurnInProgress = false;
		hasMovedThisTurn = false;
		
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
		
		// Set up button listeners
		setupButtonListeners();
		
		// Set up board click listener for human player moves
		setupBoardClickListener();
		
		// Show welcome message
		JOptionPane.showMessageDialog(this, 
			"You are " + (humanPlayer != null ? humanPlayer.getName() : "Miss Scarlet") + ".\nCan you find the solution\nbefore the Computer players?",
			"Welcome to Clue", 
			JOptionPane.INFORMATION_MESSAGE);
		
		// Start the first turn
		startTurn();
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
	 * Set up button listeners for Next and Accusation buttons
	 */
	private void setupButtonListeners() {
		// Get buttons from control panel (need to add getters)
		controlPanel.getNextButton().addActionListener(e -> handleNextButton());
		controlPanel.getAccusationButton().addActionListener(e -> handleAccusationButton());
	}
	
	/**
	 * Set up board click listener for human player moves
	 */
	private void setupBoardClickListener() {
		board.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleBoardClick(e);
			}
		});
	}
	
	/**
	 * Start a new turn for the current player
	 * Flowchart: Update current player -> Roll dice -> Calc Targets -> Update Game Control Panel
	 * Then: Is new player human? Yes -> Display Targets, Flag unfinished
	 *                            No -> Do accusation?, Do Move, Make Suggestion?
	 */
	private void startTurn() {
		// Update current player (already set by currentPlayerIndex)
		Player currentPlayer = board.getPlayers().get(currentPlayerIndex);
		
		// Roll the dice (1-6)
		currentRoll = random.nextInt(6) + 1;
		
		// Calc Targets
		board.calcTargets(board.getCell(currentPlayer.getRow(), currentPlayer.getCol()), currentRoll);
		currentTargets = board.getTargets();
		
		// Update Game Control Panel
		controlPanel.setTurn(currentPlayer, currentRoll);
		controlPanel.setGuess("");
		controlPanel.setGuessResult("");
		
		// Repaint board to show/hide targets
		board.repaint();
		
		// Is new player human?
		if (currentPlayer instanceof HumanPlayer) {
			// YES: Display Targets and Flag unfinished
			humanTurnInProgress = true;
			hasMovedThisTurn = false;
			
			// Check if there are any valid targets (handle no moves case)
			if (currentTargets == null || currentTargets.isEmpty()) {
				// No valid moves - allow player to pass
				hasMovedThisTurn = true; // Flag as finished since no moves possible
			}
			// Targets are displayed via board repaint (highlighted cells)
			// Player must click on board to move -> End (wait for event)
		} else {
			// NO: Computer player turn
			humanTurnInProgress = false;
			hasMovedThisTurn = true;
			
			// Do accusation? (dummied out for this assignment)
			// TODO: Add computer accusation logic in next assignment
			
			// Do Move
			processComputerTurn();
			
			// Make Suggestion? handled inside processComputerTurn if in room
			// -> End (computer turn complete)
		}
	}
	
	/**
	 * Handle the Next button click
	 * Flowchart: Next Player Pressed -> current human player finished? 
	 *            No -> Error Message
	 *            Yes -> Update current player -> Roll dice -> Calc Targets
	 */
	private void handleNextButton() {
		// Check: current human player finished?
		Player currentPlayer = board.getPlayers().get(currentPlayerIndex);
		if (currentPlayer instanceof HumanPlayer && !hasMovedThisTurn) {
			// NO: Error Message
			JOptionPane.showMessageDialog(this, 
				"You must complete your turn before pressing Next!",
				"Error", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// YES: Clear targets from previous turn
		board.clearTargets();
		board.repaint();
		
		// Update current player (advance to next)
		currentPlayerIndex = (currentPlayerIndex + 1) % board.getPlayers().size();
		
		// Roll dice, Calc Targets, etc. handled in startTurn()
		startTurn();
	}
	
	/**
	 * Handle the Accusation button click
	 * NOTE: Full accusation logic will be added in the next assignment
	 * For now, just show a message that this feature is coming soon
	 */
	private void handleAccusationButton() {
		Player currentPlayer = board.getPlayers().get(currentPlayerIndex);
		
		// Only human player can make accusations via button
		if (!(currentPlayer instanceof HumanPlayer) || !humanTurnInProgress) {
			JOptionPane.showMessageDialog(this, 
				"You can only make an accusation on your turn!",
				"Invalid Action", 
				JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// Dummied out for this assignment - full implementation in next assignment
		JOptionPane.showMessageDialog(this, 
			"Accusation feature will be available in the next assignment.",
			"Coming Soon", 
			JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Handle board clicks for human player movement
	 * Flowchart: Board Clicked On -> Is it Human Player Turn?
	 *            No -> End
	 *            Yes -> Clicked on Target?
	 *                   No -> Error Message -> End
	 *                   Yes -> Move Player -> In Room?
	 *                          No -> End
	 *                          Yes -> Handle Suggestion -> Update Result -> End
	 */
	private void handleBoardClick(MouseEvent e) {
		// Is it Human Player Turn?
		if (!humanTurnInProgress || hasMovedThisTurn) {
			// NO: End (ignore click)
			return;
		}
		
		// Calculate which cell was clicked
		int cellWidth = board.getWidth() / board.getNumColumns();
		int cellHeight = board.getHeight() / board.getNumRows();
		int col = e.getX() / cellWidth;
		int row = e.getY() / cellHeight;
		
		// Validate bounds
		if (row < 0 || row >= board.getNumRows() || col < 0 || col >= board.getNumColumns()) {
			return;
		}
		
		BoardCell clickedCell = board.getCell(row, col);
		
		// Clicked on Target?
		if (currentTargets == null || !currentTargets.contains(clickedCell)) {
			// NO: Error Message
			JOptionPane.showMessageDialog(this, 
				"That is not a valid target. Please select a highlighted cell.",
				"Invalid Target", 
				JOptionPane.ERROR_MESSAGE);
			return; // End
		}
		
		// YES: Move Player
		humanPlayer.setPosition(row, col);
		hasMovedThisTurn = true;
		
		// Clear targets after move and repaint
		board.clearTargets();
		board.repaint();
		
		// In Room?
		if (clickedCell.isRoomCenter()) {
			// YES: Handle Suggestion
			Room room = board.getRoom(clickedCell);
			handleHumanSuggestion(room);
			// Update Result (done inside handleHumanSuggestion)
		}
		// NO or after suggestion: End (turn complete, waiting for Next button)
	}
	
	/**
	 * Process a computer player's turn
	 * Flowchart: Do Move -> Make Suggestion? (if in room)
	 */
	private void processComputerTurn() {
		ComputerPlayer computer = (ComputerPlayer) board.getPlayers().get(currentPlayerIndex);
		
		// Do Move - Select a target and move
		BoardCell target = computer.selectTargets(currentTargets);
		if (target != null) {
			computer.setPosition(target.getRow(), target.getCol());
			
			// Clear targets after move and repaint
			board.clearTargets();
			board.repaint();
			
			// Make Suggestion? - If in a room, make a suggestion
			if (target.isRoomCenter()) {
				Room room = board.getRoom(target);
				Card roomCard = new Card(room.getName(), CardType.ROOM);
				Solution suggestion = computer.createSuggestion(roomCard);
				
				// Display the suggestion in control panel
				String suggestionText = suggestion.getPerson().getName() + ", " + 
					suggestion.getWeapon().getName() + ", " + 
					suggestion.getRoom().getName();
				controlPanel.setGuess(suggestionText);
				
				// Handle the suggestion and update result
				Board.SuggestionResult result = board.handleSuggestionWithOwner(computer, suggestion);
				if (result != null) {
					controlPanel.setGuessResult("Disproven by " + result.getPlayer().getName());
					computer.updateSeen(result.getCard());
					
					// If human player showed the card, update their seen list
					if (result.getPlayer() instanceof HumanPlayer) {
						updateHumanSeenCard(result.getCard(), computer);
					}
				} else {
					controlPanel.setGuessResult("No one could disprove!");
				}
			}
		} else {
			// No valid targets - just clear and repaint
			board.clearTargets();
			board.repaint();
		}
		// End - computer turn complete
	}
	
	/**
	 * Handle human player making a suggestion
	 */
	private void handleHumanSuggestion(Room room) {
		// Create room card
		Card roomCard = new Card(room.getName(), CardType.ROOM);
		
		// Create and show suggestion dialog
		SuggestionDialog dialog = new SuggestionDialog(
			this, 
			roomCard, 
			board.getPersonNames(), 
			board.getWeaponNames()
		);
		dialog.setVisible(true);
		
		// Get the suggestion from the dialog
		Solution suggestion = dialog.getSuggestion();
		
		if (suggestion != null) {
			// Display the suggestion
			String suggestionText = suggestion.getPerson().getName() + ", " + 
				suggestion.getWeapon().getName() + ", " + 
				suggestion.getRoom().getName();
			controlPanel.setGuess(suggestionText);
			
			// Handle the suggestion
			Board.SuggestionResult result = board.handleSuggestionWithOwner(humanPlayer, suggestion);
			if (result != null) {
				controlPanel.setGuessResult("Disproven by " + result.getPlayer().getName());
				
				// Update human player's seen cards
				updateHumanSeenCard(result.getCard(), result.getPlayer());
				
				// Show which card was revealed
				JOptionPane.showMessageDialog(this, 
					result.getPlayer().getName() + " shows you: " + result.getCard().getName(),
					"Suggestion Disproven", 
					JOptionPane.INFORMATION_MESSAGE);
			} else {
				controlPanel.setGuessResult("No one could disprove!");
				JOptionPane.showMessageDialog(this, 
					"No one could disprove your suggestion!",
					"Suggestion Result", 
					JOptionPane.INFORMATION_MESSAGE);
			}
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

