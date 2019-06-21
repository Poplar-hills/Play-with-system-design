package main.java.DeckOfCards;

import java.util.List;

public class Deck <T extends Card> {
    private List<T> cards;       // all cards, dealt or not
    private int dealtIndex = 0;  // first undealt card

    public Deck() { }

    public void setDeckOfCards(List<T> deckOfCards) {
        cards = deckOfCards;
    }

    public void shuffle() {
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = (int) Math.random() * (cards.size() - i + 1) + i;
            T card1 = cards.get(i);
            T card2 = cards.get(randomIndex);
            cards.set(i, card2);
            cards.set(randomIndex, card1);
        }
    }

    public int remainingCards() {
        return cards.size() - dealtIndex;
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
}
