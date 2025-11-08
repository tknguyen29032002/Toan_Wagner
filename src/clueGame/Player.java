package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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

    // For testing
    public List<Card> getHand() {
        return hand;
    }
}