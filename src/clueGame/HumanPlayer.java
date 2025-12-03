package clueGame;

import java.util.HashMap;
import java.util.Map;

public class HumanPlayer extends Player {
    // Track which cards have been seen and which player showed them
    private Map<Card, Player> seenCardOwners;
    // Flag to track if player was moved to room by suggestion (can stay next turn)
    private boolean wasMovedBySuggestion;
    
    public HumanPlayer(String name, String colorStr, int row, int col) {
        super(name, colorStr, row, col);
        this.seenCardOwners = new HashMap<>();
        this.wasMovedBySuggestion = false;
    }
    
    /**
     * Add a card to the seen list with the player who showed it
     * @param card The card that was seen
     * @param owner The player who showed the card
     */
    public void updateSeen(Card card, Player owner) {
        seenCardOwners.put(card, owner);
    }
    
    /**
     * Get all seen cards with their owners
     * @return Map of seen cards to the players who showed them
     */
    public Map<Card, Player> getSeenCards() {
        return new HashMap<>(seenCardOwners);
    }
    
    /**
     * Track if player was moved to current room by suggestion
     * @param wasMoved true if moved by another player's suggestion
     */
    public void setWasMovedBySuggestion(boolean wasMoved) {
        this.wasMovedBySuggestion = wasMoved;
    }
    
    /**
     * Check if player was moved by suggestion (can stay in room)
     * @return true if player was moved by suggestion
     */
    public boolean wasMovedBySuggestion() {
        return wasMovedBySuggestion;
    }
}