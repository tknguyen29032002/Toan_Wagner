package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;

/**
 * Known Cards Panel for Clue Game
 * Displays the player's hand and cards they have seen
 * Organized by card type: People, Rooms, and Weapons
 * Uses color coding to show which player is holding each seen card
 * 
 * @author Toan Nguyen and Wagner
 */
public class KnownCardsPanel extends JPanel {
	
	// Color for cards in the human player's hand
	private static final Color HAND_COLOR = new Color(255, 182, 193); // Light pink
	
	// Store the card-to-player mapping for seen cards
	private Map<Card, Player> seenCardOwners;
	
	// Panels for each section
	private JPanel peoplePanel;
	private JPanel roomsPanel;
	private JPanel weaponsPanel;
	
	/**
	 * Constructor for the Known Cards Panel
	 * Sets up the layout with three main sections: People, Rooms, Weapons
	 */
	public KnownCardsPanel() {
		// Initialize the seen card owners map
		seenCardOwners = new HashMap<>();
		
		// Create main layout - 3 rows (People, Rooms, Weapons)
		setLayout(new GridLayout(3, 1));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		
		// Initialize the three section panels
		peoplePanel = new JPanel();
		roomsPanel = new JPanel();
		weaponsPanel = new JPanel();
		
		// Add three main sections
		add(peoplePanel);
		add(roomsPanel);
		add(weaponsPanel);
	}
	
	/**
	 * Updates the panel with the player's hand and seen cards
	 * @param hand List of cards in the player's hand
	 * @param seenCards Map of seen cards to the players who showed them
	 */
	public void updatePanel(List<Card> hand, Map<Card, Player> seenCards) {
		// Update the seen card owners map
		this.seenCardOwners = new HashMap<>(seenCards);
		
		// Rebuild all three sections
		updatePeoplePanel(hand);
		updateRoomsPanel(hand);
		updateWeaponsPanel(hand);
		
		// Refresh the display
		revalidate();
		repaint();
	}
	
	/**
	 * Updates the People panel with cards from hand and seen
	 * @param hand List of cards in the player's hand
	 */
	private void updatePeoplePanel(List<Card> hand) {
		// Clear and rebuild the people panel
		peoplePanel.removeAll();
		peoplePanel.setLayout(new GridLayout(0, 1));
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		
		// Add "In Hand:" section
		peoplePanel.add(new JLabel("In Hand:"));
		boolean hasHandCards = false;
		for (Card card : hand) {
			if (card.getType() == CardType.PERSON) {
				JTextField field = createCardField(card.getName(), HAND_COLOR);
				peoplePanel.add(field);
				hasHandCards = true;
			}
		}
		if (!hasHandCards) {
			JTextField field = createCardField("None", Color.WHITE);
			peoplePanel.add(field);
		}
		
		// Add "Seen:" section
		peoplePanel.add(new JLabel("Seen:"));
		boolean hasSeenCards = false;
		for (Map.Entry<Card, Player> entry : seenCardOwners.entrySet()) {
			Card card = entry.getKey();
			if (card.getType() == CardType.PERSON) {
				Player owner = entry.getValue();
				JTextField field = createCardField(card.getName(), owner.getColor());
				peoplePanel.add(field);
				hasSeenCards = true;
			}
		}
		if (!hasSeenCards) {
			JTextField field = createCardField("None", Color.WHITE);
			peoplePanel.add(field);
		}
	}
	
	/**
	 * Updates the Rooms panel with cards from hand and seen
	 * @param hand List of cards in the player's hand
	 */
	private void updateRoomsPanel(List<Card> hand) {
		// Clear and rebuild the rooms panel
		roomsPanel.removeAll();
		roomsPanel.setLayout(new GridLayout(0, 1));
		roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		
		// Add "In Hand:" section
		roomsPanel.add(new JLabel("In Hand:"));
		boolean hasHandCards = false;
		for (Card card : hand) {
			if (card.getType() == CardType.ROOM) {
				JTextField field = createCardField(card.getName(), HAND_COLOR);
				roomsPanel.add(field);
				hasHandCards = true;
			}
		}
		if (!hasHandCards) {
			JTextField field = createCardField("None", Color.WHITE);
			roomsPanel.add(field);
		}
		
		// Add "Seen:" section
		roomsPanel.add(new JLabel("Seen:"));
		boolean hasSeenCards = false;
		for (Map.Entry<Card, Player> entry : seenCardOwners.entrySet()) {
			Card card = entry.getKey();
			if (card.getType() == CardType.ROOM) {
				Player owner = entry.getValue();
				JTextField field = createCardField(card.getName(), owner.getColor());
				roomsPanel.add(field);
				hasSeenCards = true;
			}
		}
		if (!hasSeenCards) {
			JTextField field = createCardField("None", Color.WHITE);
			roomsPanel.add(field);
		}
	}
	
	/**
	 * Updates the Weapons panel with cards from hand and seen
	 * @param hand List of cards in the player's hand
	 */
	private void updateWeaponsPanel(List<Card> hand) {
		// Clear and rebuild the weapons panel
		weaponsPanel.removeAll();
		weaponsPanel.setLayout(new GridLayout(0, 1));
		weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		
		// Add "In Hand:" section
		weaponsPanel.add(new JLabel("In Hand:"));
		boolean hasHandCards = false;
		for (Card card : hand) {
			if (card.getType() == CardType.WEAPON) {
				JTextField field = createCardField(card.getName(), HAND_COLOR);
				weaponsPanel.add(field);
				hasHandCards = true;
			}
		}
		if (!hasHandCards) {
			JTextField field = createCardField("None", Color.WHITE);
			weaponsPanel.add(field);
		}
		
		// Add "Seen:" section
		weaponsPanel.add(new JLabel("Seen:"));
		boolean hasSeenCards = false;
		for (Map.Entry<Card, Player> entry : seenCardOwners.entrySet()) {
			Card card = entry.getKey();
			if (card.getType() == CardType.WEAPON) {
				Player owner = entry.getValue();
				JTextField field = createCardField(card.getName(), owner.getColor());
				weaponsPanel.add(field);
				hasSeenCards = true;
			}
		}
		if (!hasSeenCards) {
			JTextField field = createCardField("None", Color.WHITE);
			weaponsPanel.add(field);
		}
	}
	
	/**
	 * Creates a text field for displaying a card with a specific background color
	 * @param cardName The name of the card
	 * @param color The background color for the field
	 * @return JTextField configured for the card
	 */
	private JTextField createCardField(String cardName, Color color) {
		JTextField field = new JTextField(cardName);
		field.setEditable(false);
		field.setBackground(color);
		return field;
	}
	
	// Note: Test main() method removed to avoid confusion with actual game
	// The panel is now only used within ClueGame.java
}

