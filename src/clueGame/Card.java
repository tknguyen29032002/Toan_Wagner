package clueGame;

import java.util.Objects;

public class Card {
    private String cardName;
    private CardType type;

    public Card(String cardName, CardType type) {
        this.cardName = cardName;
        this.type = type;
    }

    public String getName() {
        return cardName;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardName.equals(card.cardName) && type == card.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardName, type);
    }
}