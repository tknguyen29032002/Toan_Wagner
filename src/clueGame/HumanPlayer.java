package clueGame;

import java.util.HashMap;
import java.util.Map;

public class HumanPlayer extends Player {
    // Track which cards have been seen and which player showed them
    private Map<Card, Player> seenCardOwners;
    
    public HumanPlayer(String name, String colorStr, int row, int col) {
        super(name, colorStr, row, col);
        this.seenCardOwners = new HashMap<>();
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
}