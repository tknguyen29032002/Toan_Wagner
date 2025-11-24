package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import clueGame.*;

// Test class for computer AI logic
public class ComputerAITest {
    private static Board board;
    
    // Static cards for testing
    private static Card mustardCard;
    private static Card peacockCard;
    private static Card whiteCard;
    private static Card greenCard;
    private static Card plumCard;
    private static Card scarletCard;
    
    private static Card knifeCard;
    private static Card ropeCard;
    private static Card pipeCard;
    private static Card wrenchCard;
    private static Card candlestickCard;
    private static Card revolverCard;
    
    private static Card ballroomCard;
    private static Card conservatoryCard;
    private static Card libraryCard;
    private static Card studyCard;
    private static Card hallCard;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        
        // Create static test cards
        mustardCard = new Card("Colonel Mustard", CardType.PERSON);
        peacockCard = new Card("Mrs. Peacock", CardType.PERSON);
        whiteCard = new Card("Mrs. White", CardType.PERSON);
        greenCard = new Card("Mr. Green", CardType.PERSON);
        plumCard = new Card("Professor Plum", CardType.PERSON);
        scarletCard = new Card("Miss Scarlet", CardType.PERSON);
        
        knifeCard = new Card("Knife", CardType.WEAPON);
        ropeCard = new Card("Rope", CardType.WEAPON);
        pipeCard = new Card("Pipe", CardType.WEAPON);
        wrenchCard = new Card("Wrench", CardType.WEAPON);
        candlestickCard = new Card("Candlestick", CardType.WEAPON);
        revolverCard = new Card("Revolver", CardType.WEAPON);
        
        ballroomCard = new Card("Ballroom", CardType.ROOM);
        conservatoryCard = new Card("Conservatory", CardType.ROOM);
        libraryCard = new Card("Library", CardType.ROOM);
        studyCard = new Card("Study", CardType.ROOM);
        hallCard = new Card("Hall", CardType.ROOM);
    }

    // TESTS FOR createSuggestion
    
    @Test
    public void testCreateSuggestionRoomMatches() {
        // Test: Room in suggestion matches current location
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 0, 0);
        
        // Add some cards to hand and seen list
        player.updateHand(mustardCard);
        player.updateSeen(knifeCard);
        
        // Create suggestion for Ballroom
        Solution suggestion = player.createSuggestion(ballroomCard);
        
        assertNotNull(suggestion);
        assertEquals(ballroomCard, suggestion.getRoom());
    }
    
    @Test
    public void testCreateSuggestionOnlyOneWeaponNotSeen() {
        // Test: If only one weapon not seen, it's selected
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 0, 0);
        
        // Player has seen all weapons except knifeCard
        player.updateSeen(ropeCard);
        player.updateSeen(pipeCard);
        player.updateSeen(wrenchCard);
        player.updateSeen(candlestickCard);
        player.updateSeen(revolverCard);
        
        // Add some person cards as seen too
        player.updateSeen(mustardCard);
        
        Solution suggestion = player.createSuggestion(ballroomCard);
        
        assertNotNull(suggestion);
        assertEquals(knifeCard, suggestion.getWeapon(), "Only unseen weapon should be selected");
    }
    
    @Test
    public void testCreateSuggestionOnlyOnePersonNotSeen() {
        // Test: If only one person not seen, it's selected
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 0, 0);
        
        // Player has seen all persons except peacockCard
        player.updateSeen(mustardCard);
        player.updateSeen(whiteCard);
        player.updateSeen(greenCard);
        player.updateSeen(plumCard);
        player.updateSeen(scarletCard);
        
        // Add some weapon cards as seen too
        player.updateSeen(knifeCard);
        
        Solution suggestion = player.createSuggestion(ballroomCard);
        
        assertNotNull(suggestion);
        assertEquals(peacockCard, suggestion.getPerson());
    }
    
    @Test
    public void testCreateSuggestionMultipleWeaponsNotSeen() {
        // Test: If multiple weapons not seen, one is randomly selected
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 0, 0);
        
        // Player has seen some weapons but not all
        player.updateSeen(ropeCard);
        player.updateSeen(pipeCard);
        player.updateSeen(wrenchCard);
        // knifeCard, candlestickCard, revolverCard not seen
        
        // Add a person to seen
        player.updateSeen(mustardCard);
        
        Set<Card> returnedWeapons = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            Solution suggestion = player.createSuggestion(ballroomCard);
            assertNotNull(suggestion);
            Card weapon = suggestion.getWeapon();
            
            // Should be one of the unseen weapons
            returnedWeapons.add(weapon);
        }
        
        // Should have randomly selected different weapons
        assertTrue(returnedWeapons.size() > 1, "Multiple unseen weapons should be selected randomly");
    }
    
    @Test
    public void testCreateSuggestionMultiplePersonsNotSeen() {
        // Test: If multiple persons not seen, one is randomly selected
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 0, 0);
        
        // Player has seen some persons but not all
        player.updateSeen(mustardCard);
        player.updateSeen(whiteCard);
        player.updateSeen(greenCard);
        // peacockCard, plumCard, scarletCard not seen
        
        // Add a weapon to seen
        player.updateSeen(knifeCard);
        
        Set<Card> returnedPersons = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            Solution suggestion = player.createSuggestion(ballroomCard);
            assertNotNull(suggestion);
            Card person = suggestion.getPerson();
            
            // Should be one of the unseen persons
            assertTrue(person.equals(peacockCard) || person.equals(plumCard) || person.equals(scarletCard));
            returnedPersons.add(person);
        }
        
        // Should have randomly selected different persons
        assertTrue(returnedPersons.size() > 1, "Multiple unseen persons should be selected randomly");
    }
    
    @Test
    public void testCreateSuggestionExcludesCardsInHand() {
        // Test: Cards in player's hand are treated as "seen" and not suggested
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 0, 0);
        
        // Add cards to hand (should be treated as seen)
        player.updateHand(mustardCard);
        player.updateHand(knifeCard);
        
        // Mark other cards as seen
        player.updateSeen(peacockCard);
        player.updateSeen(whiteCard);
        player.updateSeen(greenCard);
        player.updateSeen(plumCard);
        // Only scarletCard unseen for persons
        
        player.updateSeen(ropeCard);
        player.updateSeen(pipeCard);
        player.updateSeen(wrenchCard);
        player.updateSeen(candlestickCard);
        // Only revolverCard and knifeCard unseen for weapons, but knifeCard is in hand
        // So only revolverCard should be selected
        
        Solution suggestion = player.createSuggestion(ballroomCard);
        
        assertNotNull(suggestion);
        assertEquals(scarletCard, suggestion.getPerson(), "Only unseen person (not in hand) should be selected");
        assertEquals(revolverCard, suggestion.getWeapon(), "Only unseen weapon (not in hand) should be selected");
    }

    // TESTS FOR selectTargets
    
    @Test
    public void testSelectTargetNoRooms() {
        // Test: If no rooms in target list, select randomly
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 8, 0);
        
        // Calculate targets from a walkway location (no rooms nearby)
        board.calcTargets(board.getCell(8, 0), 2);
        Set<BoardCell> targets = board.getTargets();
        
        // Ensure there are targets and none are room centers
        assertFalse(targets.isEmpty(), "Should have at least one target to test");
        boolean hasRooms = false;
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                hasRooms = true;
                break;
            }
        }
        
        if (!hasRooms && targets.size() > 1) {
            // Run multiple times to check randomness
            Set<BoardCell> selectedTargets = new HashSet<>();
            for (int i = 0; i < 20; i++) {
                BoardCell selected = player.selectTargets(targets);
                assertTrue(targets.contains(selected), "Selected target must be from the targets set");
                selectedTargets.add(selected);
            }
            
            // Should have selected different targets randomly
            assertTrue(selectedTargets.size() > 1, "Targets without rooms should be selected randomly");
        }
    }
    
    @Test
    public void testSelectTargetRoomNotSeen() {
        // Test: If room in list that has not been seen, select it
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 19, 6);
        
        // Calculate targets that include a room
        board.calcTargets(board.getCell(19, 6), 1);
        Set<BoardCell> targets = board.getTargets();
        
        // Find room center in targets
        BoardCell roomTarget = null;
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                roomTarget = cell;
                break;
            }
        }
        
        if (roomTarget != null) {
            // Room has not been seen, so it should be selected
            BoardCell selected = player.selectTargets(targets);
            assertEquals(roomTarget, selected);
        }
    }
    
    @Test
    public void testSelectTargetRoomAlreadySeen() {
        // Test: If room in list has been seen, each target selected randomly
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 19, 6);
        
        // Calculate targets that include a room
        board.calcTargets(board.getCell(19, 6), 1);
        Set<BoardCell> targets = board.getTargets();
        
        // Find room center in targets and mark as seen
        BoardCell roomTarget = null;
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                roomTarget = cell;
                // Get the room and mark as seen
                Room room = board.getRoom(cell);
                Card roomCard = new Card(room.getName(), CardType.ROOM);
                player.updateSeen(roomCard);
                break;
            }
        }
        
        if (roomTarget != null && targets.size() > 1) {
            // Run multiple times to check randomness
            Set<BoardCell> selectedTargets = new HashSet<>();
            for (int i = 0; i < 30; i++) {
                BoardCell selected = player.selectTargets(targets);
                assertNotNull(selected);
                assertTrue(targets.contains(selected));
                selectedTargets.add(selected);
            }
            
            // Should have selected different targets randomly (including the seen room)
            assertTrue(selectedTargets.size() > 1, "When room is seen, all targets should be chosen randomly");
        }
    }
    
    @Test
    public void testSelectTargetMultipleUnseenRooms() {
        // Test: If multiple unseen rooms in list, one is randomly selected
        ComputerPlayer player = new ComputerPlayer("Test Player", "red", 6, 0);
        
        // Calculate targets from a location with multiple room options
        board.calcTargets(board.getCell(6, 0), 3);
        Set<BoardCell> targets = board.getTargets();
        
        // Find all room centers in targets
        Set<BoardCell> roomTargets = new HashSet<>();
        for (BoardCell cell : targets) {
            if (cell.isRoomCenter()) {
                roomTargets.add(cell);
            }
        }
        
        if (roomTargets.size() > 1) {
            // Run multiple times to check randomness among rooms
            Set<BoardCell> selectedRooms = new HashSet<>();
            for (int i = 0; i < 30; i++) {
                BoardCell selected = player.selectTargets(targets);
                assertNotNull(selected);
                // Should always select a room when rooms are unseen
                assertTrue(selected.isRoomCenter());
                selectedRooms.add(selected);
            }
            
            // Should have selected different rooms randomly
            assertTrue(selectedRooms.size() > 1, "Multiple unseen rooms should be selected randomly");
        }
    }
}

