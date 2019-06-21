package main.java.DeckOfCards;

/*
* Card
* - 对于一张牌来说，具有以下属性：
*   1. 点数（face value）
*   2. 种类（suit）
*   3. 是否已发到玩家手中（isAvailable）
* */

public abstract class Card {  // this is abstract coz the "value" method doesn't make much sense without a specific game attached to them
    private boolean isAvailable = true;
    private int faceValue;  // number of face on the card - 1 for Ace, 11 for Jack, 12 for Queen, 13 for King
    private Suit suit;

    public Card (int v, Suit s) {
        faceValue = v;
        suit = s;
    }

    public abstract int value();
    public Suit getSuit() { return suit; }
    public boolean isAvailable() { return isAvailable; }
    public void markUnavailable() { isAvailable = false; }
    public void markAvailable() { isAvailable = true; }
}
