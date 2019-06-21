package main.java.DeckOfCards;

import java.util.ArrayList;
import java.util.List;

public class Hand <T extends Card> {
    protected List<T> cards = new ArrayList<>();  // TODO: why protected

    public int score() {
        return cards.stream().map(Card::value).reduce(0, Integer::sum);
    }

    public void addCard(T card) {
        cards.add(card);
    }
}
