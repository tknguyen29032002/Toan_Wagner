package gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Player;

/**
 * Game Control Panel for Clue Game
 * Displays game controls and information for the current turn
 * 
 * @author Toan Nguyen and Wagner
 */
public class GameControlPanel extends JPanel {
	
	// Text fields for displaying game information
	private JTextField currentPlayerField;
	private JTextField rollField;
	private JTextField guessField;
	private JTextField guessResultField;
	
	// Buttons for game controls
	private JButton nextButton;
	private JButton accusationButton;
	
	/**
	 * Constructor for the panel, sets up the layout and components
	 */
	public GameControlPanel() {
		setLayout(new GridLayout(2, 0));
		
		// Create top panel (row 1) with 4 sections
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));
		
		// Add whose turn panel
		topPanel.add(createWhoseTurnPanel());
		
		// Add roll panel
		topPanel.add(createRollPanel());
		
		// Add accusation button
		accusationButton = new JButton("Make Accusation");
		topPanel.add(accusationButton);
		
		// Add next button
		nextButton = new JButton("NEXT!");
		topPanel.add(nextButton);
		
		// Create bottom panel (row 2) with 2 sections
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(0, 2));
		
		// Add padding to the main panel
		setBorder(new EmptyBorder(10, 0, 0, 0)); // Top padding only, as side padding is handled by layout
		
		// Add guess panel
		bottomPanel.add(createGuessPanel());
		
		// Add guess result panel
		bottomPanel.add(createGuessResultPanel());
		
		// Add both panels to main panel
		add(topPanel);
		add(bottomPanel);
	}
	
	/**
	 * Creates the panel displaying whose turn it is
	 * @return JPanel with label and text field for current player
	 */
	private JPanel createWhoseTurnPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		
		JLabel label = new JLabel("Whose turn?");
		currentPlayerField = new JTextField(20);
		currentPlayerField.setEditable(false);
		
		panel.add(label);
		panel.add(currentPlayerField);
		
		return panel;
	}
	
	/**
	 * Creates the panel displaying the die roll
	 * @return JPanel with label and text field for roll
	 */
	private JPanel createRollPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		
		JLabel label = new JLabel("Roll:");
		rollField = new JTextField(5);
		rollField.setEditable(false);
		
		panel.add(label);
		panel.add(rollField);
		
		return panel;
	}
	
	/**
	 * Creates the panel displaying the current guess
	 * @return JPanel with border and text field for guess
	 */
	private JPanel createGuessPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		
		guessField = new JTextField(30);
		guessField.setEditable(false);
		
		panel.add(guessField);
		
		return panel;
	}
	
	/**
	 * Creates the panel displaying the guess result
	 * @return JPanel with border and text field for guess result
	 */
	private JPanel createGuessResultPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 0));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		
		guessResultField = new JTextField(30);
		guessResultField.setEditable(false);
		
		panel.add(guessResultField);
		
		return panel;
	}
	
	/**
	 * Sets the current turn information
	 * @param player The player whose turn it is
	 * @param roll The die roll value
	 */
	public void setTurn(Player player, int roll) {
		currentPlayerField.setText(player.getName());
		currentPlayerField.setBackground(player.getColor());
		
		// Adjust text color for better contrast
		Color bgColor = player.getColor();
		if (bgColor.equals(Color.BLUE) || 
			bgColor.equals(new Color(128, 0, 128)) || // Purple
			bgColor.equals(Color.RED)) {
			currentPlayerField.setForeground(Color.WHITE);
		} else {
			currentPlayerField.setForeground(Color.BLACK);
		}
		
		rollField.setText(String.valueOf(roll));
	}
	
	/**
	 * Sets the guess text
	 * @param guess The guess to display
	 */
	public void setGuess(String guess) {
		guessField.setText(guess);
	}
	
	/**
	 * Sets the guess result text
	 * @param result The result to display
	 */
	public void setGuessResult(String result) {
		guessResultField.setText(result);
	}
	
	/**
	 * Get the Next button for adding listeners
	 * @return The next button
	 */
	public JButton getNextButton() {
		return nextButton;
	}
	
	/**
	 * Get the Accusation button for adding listeners
	 * @return The accusation button
	 */
	public JButton getAccusationButton() {
		return accusationButton;
	}
	
	// Note: Test main() method removed to avoid confusion with actual game
	// The panel is now only used within ClueGame.java
}

