package gui;

import javax.swing.JFrame;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Solution;

/**
 * Test class to demonstrate how to use the SuggestionDialog
 * This shows how the main ClueGame class will integrate the dialog
 * 
 * @author Toan Nguyen and James Wagner
 */
public class SuggestionDialogTest {
    
    public static void main(String[] args) {
        // Initialize the board to get person and weapon names
        Board board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        
        // Create a dummy frame (in real game, this would be your ClueGame frame)
        JFrame frame = new JFrame("Test Frame");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Example: Human player enters the Lounge
        Card currentRoomCard = new Card("Lounge", CardType.ROOM);
        
        // Create and show the suggestion dialog
        SuggestionDialog dialog = new SuggestionDialog(
            frame,
            currentRoomCard,
            board.getPersonNames(),
            board.getWeaponNames()
        );
        
        // Show the dialog (this blocks until user submits or cancels)
        dialog.setVisible(true);
        
        // After dialog closes, check what the user selected
        if (dialog.wasSubmitted()) {
            Solution suggestion = dialog.getSuggestion();
            System.out.println("User made a suggestion:");
            System.out.println("  Person: " + suggestion.getPerson().getName());
            System.out.println("  Weapon: " + suggestion.getWeapon().getName());
            System.out.println("  Room: " + suggestion.getRoom().getName());
            
            // In your real game, you would now call:
            // board.handleSuggestion(humanPlayer, suggestion);
        } else {
            System.out.println("User cancelled the suggestion");
        }
        
        // Exit for testing
        System.exit(0);
    }
}

