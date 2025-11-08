package tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import clueGame.*;

public class GameSetupTests {
    private static Board board;

    @BeforeAll
    public static void setUp() {
        board = Board.getInstance();
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        board.createDeck(); // Create deck after init
        board.dealCards(); // Deal after deck
    }

    @Test
    public void testLoadPeople() {
        // Tests loading and instantiation of 6 players: 1 Human, 5 Computer
        List<Player> players = board.getPlayers();
        assertEquals(6, players.size());

        // Assume specific order from file: e.g., Miss Scarlet Human, others Computer
        Player p1 = players.get(0);
        assertTrue(p1 instanceof HumanPlayer);
        assertEquals("Miss Scarlet", p1.getName());
        assertEquals(java.awt.Color.red, p1.getColor());
        assertEquals(16, p1.getRow()); // Example starting loc
        assertEquals(0, p1.getCol());

        Player p2 = players.get(1);
        assertTrue(p2 instanceof ComputerPlayer);
        assertEquals("Colonel Mustard", p2.getName());
        assertEquals(java.awt.Color.yellow, p2.getColor());
        // ... similarly test others for name, color, loc, type
    }

    @Test
    public void testLoadWeapons() {
        // Tests loading 6 weapons
        List<String> weapons = board.getWeaponNames();
        assertEquals(6, weapons.size());
        assertTrue(weapons.contains("Knife"));
        assertTrue(weapons.contains("Candlestick"));
        // ... check others
    }

    @Test
    public void testCreateDeck() {
        // Tests full deck: 21 cards, 9 ROOM, 6 PERSON, 6 WEAPON, no dupes
        List<Card> deck = board.getDeck();
        assertEquals(21, deck.size());

        long roomCount = deck.stream().filter(c -> c.getType() == CardType.ROOM).count();
        assertEquals(9, roomCount);

        long personCount = deck.stream().filter(c -> c.getType() == CardType.PERSON).count();
        assertEquals(6, personCount);

        long weaponCount = deck.stream().filter(c -> c.getType() == CardType.WEAPON).count();
        assertEquals(6, weaponCount);

        Set<Card> unique = new HashSet<>(deck);
        assertEquals(21, unique.size()); // No duplicates
    }

    @Test
    public void testDealCards() {
        // Tests: Solution has 1 each type, all cards dealt, roughly equal hands (3 each), no dupes
        Solution solution = board.getTheAnswer();
        assertNotNull(solution);
        assertEquals(CardType.PERSON, solution.getPerson().getType());
        assertEquals(CardType.WEAPON, solution.getWeapon().getType());
        assertEquals(CardType.ROOM, solution.getRoom().getType());

        List<Player> players = board.getPlayers();
        for (Player p : players) {
            assertEquals(3, p.getHand().size()); // 18/6 = 3
        }

        Set<Card> allDealt = new HashSet<>();
        allDealt.add(solution.getPerson());
        allDealt.add(solution.getWeapon());
        allDealt.add(solution.getRoom());

        for (Player p : players) {
            for (Card c : p.getHand()) {
                assertFalse(allDealt.contains(c)); // No dupes
                allDealt.add(c);
            }
        }

        assertEquals(21, allDealt.size()); // All cards accounted for
    }
}