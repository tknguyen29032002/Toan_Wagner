package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Player {
    private String name;
    private Color color;
    private int row;
    private int col;
    protected List<Card> hand;

    public Player(String name, String colorStr, int row, int col) {
        this.name = name;
        this.color = parseColor(colorStr);
        this.row = row;
        this.col = col;
        this.hand = new ArrayList<>();
    }

    private Color parseColor(String colorStr) {
        switch (colorStr.toLowerCase()) {
            case "red": return Color.red;
            case "green": return Color.green;
            case "blue": return Color.blue;
            case "yellow": return Color.yellow;
            case "purple": return new Color(128, 0, 128); // Purple
            case "white": return Color.white;
            default: throw new IllegalArgumentException("Invalid color: " + colorStr);
        }
    }

    public void updateHand(Card card) {
        hand.add(card);
    }
    
    // Try to disprove suggestion with cards in hand, return matching card or null (random if multiple)
    public Card disproveSuggestion(Solution suggestion) {
        List<Card> matchingCards = new ArrayList<>();
        
        // Check each card in hand for a match
        for (Card card : hand) {
            if (card.equals(suggestion.getPerson()) ||
                card.equals(suggestion.getWeapon()) ||
                card.equals(suggestion.getRoom())) {
                matchingCards.add(card);
            }
        }
        
        // If no matching cards, return null
        if (matchingCards.isEmpty()) {
            return null;
        }
        
        // If one matching card, return it
        if (matchingCards.size() == 1) {
            return matchingCards.get(0);
        }
        
        // If multiple matching cards, choose randomly
        Random rand = new Random();
        int randomIndex = rand.nextInt(matchingCards.size());
        return matchingCards.get(randomIndex);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    // Setter for position (used when moving player)
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // For testing
    public List<Card> getHand() {
        return hand;
    }
    
    // Draw the player as a colored circle on the board
    public void draw(Graphics g, int cellWidth, int cellHeight, int offsetIndex) {
        int x = col * cellWidth;
        int y = row * cellHeight;
        
        // Calculate circle dimensions (slightly smaller than cell)
        int diameter = Math.min(cellWidth, cellHeight) - 8;
        int baseOffsetX = (cellWidth - diameter) / 2;
        int baseOffsetY = (cellHeight - diameter) / 2;
        
        // Apply offset for multiple players in same cell
        // Offset pattern: 0=center, 1=right, 2=down, 3=left, 4=up, 5=down-right, etc.
        int additionalOffsetX = 0;
        int additionalOffsetY = 0;
        int offsetAmount = diameter / 3;
        
        switch (offsetIndex % 6) {
            case 0: // Center
                break;
            case 1: // Right
                additionalOffsetX = offsetAmount;
                break;
            case 2: // Down
                additionalOffsetY = offsetAmount;
                break;
            case 3: // Left
                additionalOffsetX = -offsetAmount;
                break;
            case 4: // Up
                additionalOffsetY = -offsetAmount;
                break;
            case 5: // Down-right
                additionalOffsetX = offsetAmount / 2;
                additionalOffsetY = offsetAmount / 2;
                break;
        }
        
        // Draw player as a filled circle with their color
        g.setColor(color);
        g.fillOval(x + baseOffsetX + additionalOffsetX, y + baseOffsetY + additionalOffsetY, diameter, diameter);
        
        // Draw black outline
        g.setColor(Color.BLACK);
        g.drawOval(x + baseOffsetX + additionalOffsetX, y + baseOffsetY + additionalOffsetY, diameter, diameter);
    }
}