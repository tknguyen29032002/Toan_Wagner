package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

/**
 * Modal dialog for human player to make an accusation
 * Displays combo boxes for person, weapon, and room selection
 * 
 * @author Toan Nguyen and Wagner
 */
public class AccusationDialog extends JDialog {
    
    // The accusation created by the user (null if cancelled)
    private Solution accusation;
    
    // UI Components
    private JComboBox<String> personCombo;
    private JComboBox<String> weaponCombo;
    private JComboBox<String> roomCombo;
    private JButton submitButton;
    private JButton cancelButton;
    
    // Data
    private boolean submitted = false;
    
    /**
     * Constructor - creates the accusation dialog
     * 
     * @param parent The parent frame (for modal behavior)
     * @param personNames List of all person names for the combo box
     * @param weaponNames List of all weapon names for the combo box
     * @param roomNames List of all room names for the combo box
     */
    public AccusationDialog(JFrame parent, List<String> personNames, List<String> weaponNames, List<String> roomNames) {
        // Call JDialog constructor with parent and modal=true
        super(parent, "Make an Accusation", true);
        
        this.accusation = null;
        
        // Set up the dialog
        setSize(300, 200);
        setLocationRelativeTo(parent); // Center on parent
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        
        // Add padding
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Person (combo box)
        add(new JLabel("Person"));
        personCombo = new JComboBox<>(personNames.toArray(new String[0]));
        add(personCombo);
        
        // Weapon (combo box)
        add(new JLabel("Weapon"));
        weaponCombo = new JComboBox<>(weaponNames.toArray(new String[0]));
        add(weaponCombo);
        
        // Room (combo box)
        add(new JLabel("Room"));
        roomCombo = new JComboBox<>(roomNames.toArray(new String[0]));
        add(roomCombo);
        
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
        String selectedRoom = (String) roomCombo.getSelectedItem();
        
        // Create cards for the accusation
        Card personCard = new Card(selectedPerson, CardType.PERSON);
        Card weaponCard = new Card(selectedWeapon, CardType.WEAPON);
        Card roomCard = new Card(selectedRoom, CardType.ROOM);
        
        // Create the accusation (Solution object)
        accusation = new Solution(personCard, weaponCard, roomCard);
        submitted = true;
        
        // Close the dialog
        dispose();
    }
    
    /**
     * Handle cancel button click
     */
    private void handleCancel() {
        accusation = null;
        submitted = false;
        dispose();
    }
    
    /**
     * Get the accusation created by the user
     * @return The accusation (Solution object), or null if cancelled
     */
    public Solution getAccusation() {
        return accusation;
    }
    
    /**
     * Check if the user submitted (vs cancelled)
     * @return true if submitted, false if cancelled
     */
    public boolean wasSubmitted() {
        return submitted;
    }
}

