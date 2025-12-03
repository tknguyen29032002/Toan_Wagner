package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
    private Set<Card> seenCards;
    
    // Flag indicating the computer should make an accusation (suggestion wasn't disproved)
    private boolean shouldMakeAccusation;
    // Store the suggestion that wasn't disproved (to use as accusation)
    private Solution pendingAccusation;
    // Flag to track if player was moved to room by suggestion (can stay next turn)
    private boolean wasMovedBySuggestion;
    
    public ComputerPlayer(String name, String colorStr, int row, int col) {
        super(name, colorStr, row, col);
        this.seenCards = new HashSet<>();
        this.shouldMakeAccusation = false;
        this.pendingAccusation = null;
        this.wasMovedBySuggestion = false;
    }
    
    // Add card to seen list
    public void updateSeen(Card card) {
        seenCards.add(card);
    }
    
    // Get seen cards for testing
    public Set<Card> getSeenCards() {
        return seenCards;
    }
    
    // Create suggestion from current room and unseen cards
    public Solution createSuggestion(Card currentRoom) {
        Board board = Board.getInstance();
        
        // Get all available cards
        List<String> allPersons = board.getPersonNames();
        List<String> allWeapons = board.getWeaponNames();
        
        // Find unseen persons (not in hand, not in seen list)
        List<Card> unseenPersons = new ArrayList<>();
        for (String personName : allPersons) {
            Card personCard = new Card(personName, CardType.PERSON);
            if (!hand.contains(personCard) && !seenCards.contains(personCard)) {
                unseenPersons.add(personCard);
            }
        }
        
        // Find unseen weapons (not in hand, not in seen list)
        List<Card> unseenWeapons = new ArrayList<>();
        for (String weaponName : allWeapons) {
            Card weaponCard = new Card(weaponName, CardType.WEAPON);
            if (!hand.contains(weaponCard) && !seenCards.contains(weaponCard)) {
                unseenWeapons.add(weaponCard);
            }
        }
        
        // Select random person from unseen
        Card selectedPerson = null;
        if (!unseenPersons.isEmpty()) {
            Random rand = new Random();
            selectedPerson = unseenPersons.get(rand.nextInt(unseenPersons.size()));
        }
        
        // Select random weapon from unseen
        Card selectedWeapon = null;
        if (!unseenWeapons.isEmpty()) {
            Random rand = new Random();
            selectedWeapon = unseenWeapons.get(rand.nextInt(unseenWeapons.size()));
        }
        
        return new Solution(selectedPerson, selectedWeapon, currentRoom);
    }
    
    // Select target: prioritize unseen rooms, otherwise random
    public BoardCell selectTargets(Set<BoardCell> targets) {
        if (targets == null || targets.isEmpty()) {
            return null;
        }
        
        Board board = Board.getInstance();
        List<BoardCell> unseenRooms = new ArrayList<>();
        
        // Find all unseen room centers in targets
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                Room room = board.getRoom(cell);
                Card roomCard = new Card(room.getName(), CardType.ROOM);
                
                // Check if room has not been seen
                if (!seenCards.contains(roomCard)) {
                    unseenRooms.add(cell);
                }
            }
        }
        
        // If there are unseen rooms, select randomly from them
        if (!unseenRooms.isEmpty()) {
            Random rand = new Random();
            return unseenRooms.get(rand.nextInt(unseenRooms.size()));
        }
        
        // Otherwise, select randomly from all targets
        Random rand = new Random();
        List<BoardCell> targetList = new ArrayList<>(targets);
        return targetList.get(rand.nextInt(targetList.size()));
    }
    
    // Set flag to indicate computer should make an accusation
    // Called when a suggestion is not disproved and computer doesn't have the room card
    public void setShouldMakeAccusation(boolean shouldMake, Solution suggestion) {
        this.shouldMakeAccusation = shouldMake;
        this.pendingAccusation = suggestion;
    }
    
    // Check if computer should make accusation at the start of turn
    public boolean shouldMakeAccusation() {
        return shouldMakeAccusation && pendingAccusation != null;
    }
    
    // Get the pending accusation (the suggestion that wasn't disproved)
    public Solution getPendingAccusation() {
        return pendingAccusation;
    }
    
    // Clear accusation state (after making accusation or if disproved)
    public void clearAccusation() {
        this.shouldMakeAccusation = false;
        this.pendingAccusation = null;
    }
    
    // Check if player has a specific card in hand
    public boolean hasCard(Card card) {
        return hand.contains(card);
    }
    
    // Track if player was moved to current room by suggestion
    public void setWasMovedBySuggestion(boolean wasMoved) {
        this.wasMovedBySuggestion = wasMoved;
    }
    
    // Check if player was moved by suggestion (can stay in room)
    public boolean wasMovedBySuggestion() {
        return wasMovedBySuggestion;
    }
}