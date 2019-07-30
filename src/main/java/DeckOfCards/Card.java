package main.java.DeckOfCards;

/*
* Card
* - 对于一张牌来说，具有以下属性：
*   1. 点数（face result）- 1 for Ace, 11 for Jack, 12 for Queen, 13 for King
*   2. 种类（suit）
*   3. 是否可以发给玩家（isAvailable）
* */

public abstract class Card {  // this is abstract coz the "result" method doesn't make much sense without a specific game attached to them
    private boolean isAvailable;
    protected int faceValue;  // Card 的子类 BlackJackCard 需要访问 faceValue 因此声明为 protected
    private Suit suit;

    public Card (int v, Suit s) {
        faceValue = v;
        suit = s;
        isAvailable = true;
    }

    public abstract int value();
    public Suit getSuit() { return suit; }
    public boolean isAvailable() { return isAvailable; }
    public void markUnavailable() { isAvailable = false; }
    public void markAvailable() { isAvailable = true; }
}
