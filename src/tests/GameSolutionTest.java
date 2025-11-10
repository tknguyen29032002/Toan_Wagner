package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import clueGame.*;

// Test class for game card processing
public class GameSolutionTest {
    private static Board board;
    
    // Static cards for testing
    private static Card mustardCard;
    private static Card peacockCard;
    private static Card whiteCard;
    private static Card knifeCard;
    private static Card ropeCard;
    private static Card pipeCard;
    private static Card ballroomCard;
    private static Card conservatoryCard;
    private static Card libraryCard;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        
        // Create static test cards
        mustardCard = new Card("Colonel Mustard", CardType.PERSON);
        peacockCard = new Card("Mrs. Peacock", CardType.PERSON);
        whiteCard = new Card("Mrs. White", CardType.PERSON);
        
        knifeCard = new Card("Knife", CardType.WEAPON);
        ropeCard = new Card("Rope", CardType.WEAPON);
        pipeCard = new Card("Pipe", CardType.WEAPON);
        
        ballroomCard = new Card("Ballroom", CardType.ROOM);
        conservatoryCard = new Card("Conservatory", CardType.ROOM);
        libraryCard = new Card("Library", CardType.ROOM);
    }

    // TESTS FOR checkAccusation
    
    @Test
    public void testAccusationCorrect() {
        // Test: Accusation that matches the answer returns true
        Solution testAnswer = new Solution(mustardCard, knifeCard, ballroomCard);
        board.setTheAnswer(testAnswer);
        
        Solution accusation = new Solution(mustardCard, knifeCard, ballroomCard);
        assertTrue(board.checkAccusation(accusation));
    }
    
    @Test
    public void testAccusationWrongPerson() {
        // Test: Accusation with wrong person returns false
        Solution testAnswer = new Solution(mustardCard, knifeCard, ballroomCard);
        board.setTheAnswer(testAnswer);
        
        Solution accusation = new Solution(peacockCard, knifeCard, ballroomCard);
        assertFalse(board.checkAccusation(accusation));
    }
    
    @Test
    public void testAccusationWrongWeapon() {
        // Test: Accusation with wrong weapon returns false
        Solution testAnswer = new Solution(mustardCard, knifeCard, ballroomCard);
        board.setTheAnswer(testAnswer);
        
        Solution accusation = new Solution(mustardCard, ropeCard, ballroomCard);
        assertFalse(board.checkAccusation(accusation));
    }
    
    @Test
    public void testAccusationWrongRoom() {
        // Test: Accusation with wrong room returns false
        Solution testAnswer = new Solution(mustardCard, knifeCard, ballroomCard);
        board.setTheAnswer(testAnswer);
        
        Solution accusation = new Solution(mustardCard, knifeCard, libraryCard);
        assertFalse(board.checkAccusation(accusation));
    }

    // TESTS FOR disproveSuggestion
    
    @Test
    public void testDisproveSuggestionOneMatch() {
        // Test: Player has one matching card, it should be returned
        Player player = new ComputerPlayer("Test Player", "red", 0, 0);
        player.updateHand(mustardCard);
        player.updateHand(knifeCard);
        player.updateHand(ballroomCard);
        
        Solution suggestion = new Solution(peacockCard, ropeCard, ballroomCard);
        Card result = player.disproveSuggestion(suggestion);
        
        assertNotNull(result);
        assertEquals(ballroomCard, result);
    }
    
    @Test
    public void testDisproveSuggestionNoMatch() {
        // Test: Player has no matching cards, null should be returned
        Player player = new ComputerPlayer("Test Player", "red", 0, 0);
        player.updateHand(mustardCard);
        player.updateHand(knifeCard);
        player.updateHand(ballroomCard);
        
        Solution suggestion = new Solution(peacockCard, ropeCard, libraryCard);
        Card result = player.disproveSuggestion(suggestion);
        
        assertNull(result);
    }
    
    @Test
    public void testDisproveSuggestionMultipleMatches() {
        // Test: Player has multiple matching cards, one should be returned randomly
        Player player = new ComputerPlayer("Test Player", "red", 0, 0);
        player.updateHand(mustardCard);
        player.updateHand(knifeCard);
        player.updateHand(ballroomCard);
        
        // Suggestion matches all three cards
        Solution suggestion = new Solution(mustardCard, knifeCard, ballroomCard);
        
        // Run multiple times to ensure randomness
        Set<Card> returnedCards = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            Card result = player.disproveSuggestion(suggestion);
            assertNotNull(result);
            assertTrue(result.equals(mustardCard) || result.equals(knifeCard) || result.equals(ballroomCard));
            returnedCards.add(result);
        }
        
        // All three cards should have been returned at least once
        assertTrue(returnedCards.size() > 1, "Multiple matching cards should be chosen randomly");
    }

    // TESTS FOR handleSuggestion
    
    @Test
    public void testHandleSuggestionNoDisprove() {
        // Test: Suggestion that no one can disprove returns null
        board.clearPlayers();
        
        Player human = new HumanPlayer("Human", "red", 0, 0);
        human.updateHand(mustardCard);
        human.updateHand(knifeCard);
        
        Player comp1 = new ComputerPlayer("Comp1", "blue", 1, 1);
        comp1.updateHand(peacockCard);
        comp1.updateHand(ropeCard);
        
        board.addPlayer(human);
        board.addPlayer(comp1);
        
        // Suggestion that no one can disprove
        Solution suggestion = new Solution(whiteCard, pipeCard, libraryCard);
        Card result = board.handleSuggestion(human, suggestion);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleSuggestionAccuserCanDisprove() {
        // Test: Suggestion that only the accuser can disprove returns null
        board.clearPlayers();
        
        Player human = new HumanPlayer("Human", "red", 0, 0);
        human.updateHand(mustardCard);
        human.updateHand(knifeCard);
        
        Player comp1 = new ComputerPlayer("Comp1", "blue", 1, 1);
        comp1.updateHand(peacockCard);
        comp1.updateHand(ropeCard);
        
        board.addPlayer(human);
        board.addPlayer(comp1);
        
        // Suggestion that only human can disprove
        Solution suggestion = new Solution(mustardCard, pipeCard, libraryCard);
        Card result = board.handleSuggestion(human, suggestion);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleSuggestionHumanDisproves() {
        // Test: Suggestion that only human can disprove (when not the accuser) returns card
        board.clearPlayers();
        
        Player comp1 = new ComputerPlayer("Comp1", "blue", 1, 1);
        comp1.updateHand(peacockCard);
        comp1.updateHand(ropeCard);
        
        Player human = new HumanPlayer("Human", "red", 0, 0);
        human.updateHand(mustardCard);
        human.updateHand(knifeCard);
        
        board.addPlayer(comp1);
        board.addPlayer(human);
        
        // Comp1 makes suggestion that human can disprove
        Solution suggestion = new Solution(mustardCard, pipeCard, libraryCard);
        Card result = board.handleSuggestion(comp1, suggestion);
        
        assertNotNull(result);
        assertEquals(mustardCard, result);
    }
    
    @Test
    public void testHandleSuggestionCorrectPlayerOrder() {
        // Test: Players are queried in order, first player to disprove returns answer
        board.clearPlayers();
        
        Player accuser = new ComputerPlayer("Accuser", "red", 0, 0);
        accuser.updateHand(whiteCard);
        
        Player player1 = new ComputerPlayer("Player1", "blue", 1, 1);
        player1.updateHand(mustardCard);
        player1.updateHand(ballroomCard);
        
        Player player2 = new ComputerPlayer("Player2", "green", 2, 2);
        player2.updateHand(peacockCard);
        player2.updateHand(knifeCard);
        
        board.addPlayer(accuser);
        board.addPlayer(player1);
        board.addPlayer(player2);
        
        // Both player1 and player2 can disprove this suggestion
        // But player1 should be asked first
        Solution suggestion = new Solution(mustardCard, knifeCard, libraryCard);
        Card result = board.handleSuggestion(accuser, suggestion);
        
        assertNotNull(result);
        // Result should be from player1 (either mustardCard or ballroomCard, but mustardCard matches)
        assertEquals(mustardCard, result);
    }
    
    @Test
    public void testHandleSuggestionWrapsAround() {
        // Test: Query order wraps around to start of list (skipping accuser)
        board.clearPlayers();
        
        Player player0 = new ComputerPlayer("Player0", "red", 0, 0);
        player0.updateHand(whiteCard);
        
        Player player1 = new ComputerPlayer("Player1", "blue", 1, 1);
        player1.updateHand(peacockCard);
        
        Player player2 = new ComputerPlayer("Player2", "green", 2, 2);
        player2.updateHand(mustardCard);
        
        board.addPlayer(player0);
        board.addPlayer(player1);
        board.addPlayer(player2);
        
        // Player2 makes suggestion, should query player0 and player1
        Solution suggestion = new Solution(whiteCard, knifeCard, libraryCard);
        Card result = board.handleSuggestion(player2, suggestion);
        
        assertNotNull(result);
        assertEquals(whiteCard, result);
    }
}

