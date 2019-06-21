package main.java.DeckOfCards;

public abstract class Card {
    private boolean available = true;
    protected int faceValue;  // number of face on the card - 1 for Ace, 11 for Jack, 12 for Queen, 13 for King
    protected Suit suit;  // TODO: why protected

    public Card (int v, Suit s) {
        faceValue = v;
        suit = s;
    }

    public abstract int value();
    public Suit getSuit() { return suit; }
    public boolean isAvailable() { return available; }
    public void markUnavailable() { available = false; }
    public void markAvailable() { available = true; }
}
