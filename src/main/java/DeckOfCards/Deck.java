package main.java.DeckOfCards;

import java.util.List;

/*
* Deck (一副牌)
* - 对于一副牌来说，玩家可以：
*   1. 洗牌
*   2. 发牌（发一张牌、发一手牌）
* */

public class Deck <T extends Card> {  // Deck 中的 cards 的类型必须继承 Card
    private List<T> cards;            // all cards, dealt or not
    private int dealtIndex = 0;       // the first undealt card in the remaining cards

    public Deck() { }

    public void setDeckOfCards(List<T> deckOfCards) {
        cards = deckOfCards;
    }

    public void shuffle() {
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = (int) Math.random() * (cards.size() - i) + i;  // 每次从 [i, size-1] 区间中选一个
            T card1 = cards.get(i);
            T card2 = cards.get(randomIndex);
            cards.set(i, card2);
            cards.set(randomIndex, card1);
        }
    }

    public T[] dealHand(int number) {
        if (remainingCards() < number) return null;

        T[] hand = (T[]) new Card[number];

        for (int i = 0; i < number; i++) {
            T card = dealCard();
            if (card != null) hand[i] = card;
        }
        return hand;
    }

    public T dealCard() {
        if (remainingCards() == 0) return null;
        T card = cards.get(dealtIndex++);
        card.markUnavailable();
        return card;
    }

    private int remainingCards() {
        return cards.size() - dealtIndex;
    }
}
