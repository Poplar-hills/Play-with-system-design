package main.java.DeckOfCards;

import java.util.ArrayList;
import java.util.List;

public class Hand <T extends Card> {
    private List<T> cards = new ArrayList<>();

    public int score() {
        return cards.stream().map(Card::value).reduce(0, Integer::sum);
    }

    public void addCard(T card) {
        cards.add(card);
    }

    public List<T> get() { return cards; }
}
