package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import clueGame.Card;
import clueGame.Solution;

/**
 * Modal dialog for human player to make a suggestion
 * Displays current room (read-only) and combo boxes for person and weapon selection
 * 
 * @author Toan Nguyen and James Wagner
 */
public class SuggestionDialog extends JDialog {
    
    // The suggestion created by the user (null if cancelled)
    private Solution suggestion;
    
    // UI Components
    private JTextField roomField;
    private JComboBox<String> personCombo;
    private JComboBox<String> weaponCombo;
    private JButton submitButton;
    private JButton cancelButton;
    
    // Data
    private Card currentRoomCard;
    private boolean submitted = false;
    
    /**
     * Constructor - creates the suggestion dialog
     * 
     * @param parent The parent frame (for modal behavior)
     * @param currentRoom The room card the player is currently in (read-only)
     * @param personNames List of all person names for the combo box
     * @param weaponNames List of all weapon names for the combo box
     */
    public SuggestionDialog(JFrame parent, Card currentRoom, List<String> personNames, List<String> weaponNames) {
        // Call JDialog constructor with parent and modal=true
        super(parent, "Make a Suggestion", true);
        
        this.currentRoomCard = currentRoom;
        this.suggestion = null;
        
        // Set up the dialog
        setSize(300, 200);
        setLocationRelativeTo(parent); // Center on parent
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        
        // Add padding
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Current Room (read-only)
        add(new JLabel("Current room"));
        roomField = new JTextField(currentRoom.getName());
        roomField.setEditable(false);
        roomField.setBackground(Color.WHITE);
        add(roomField);
        
        // Person (combo box)
        add(new JLabel("Person"));
        personCombo = new JComboBox<>(personNames.toArray(new String[0]));
        add(personCombo);
        
        // Weapon (combo box)
        add(new JLabel("Weapon"));
        weaponCombo = new JComboBox<>(weaponNames.toArray(new String[0]));
        add(weaponCombo);
        
        // Buttons
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        
        // Add button listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancel();
            }
        });
        
        add(submitButton);
        add(cancelButton);
    }
    
    /**
     * Handle submit button click
     * Creates a Solution object from the selected values
     */
    private void handleSubmit() {
        String selectedPerson = (String) personCombo.getSelectedItem();
        String selectedWeapon = (String) weaponCombo.getSelectedItem();
        
        // Create cards for the suggestion
        Card personCard = new Card(selectedPerson, clueGame.CardType.PERSON);
        Card weaponCard = new Card(selectedWeapon, clueGame.CardType.WEAPON);
        
        // Create the suggestion (Solution object)
        suggestion = new Solution(personCard, weaponCard, currentRoomCard);
        submitted = true;
        
        // Close the dialog
        dispose();
    }
    
    /**
     * Handle cancel button click
     */
    private void handleCancel() {
        suggestion = null;
        submitted = false;
        dispose();
    }
    
    /**
     * Get the suggestion created by the user
     * @return The suggestion (Solution object), or null if cancelled
     */
    public Solution getSuggestion() {
        return suggestion;
    }
    
    /**
     * Check if the user submitted (vs cancelled)
     * @return true if submitted, false if cancelled
     */
    public boolean wasSubmitted() {
        return submitted;
    }
}

