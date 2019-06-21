package main.java.DeckOfCards;

import java.util.ArrayList;
import java.util.List;

/*
* Hand (一手牌)
* - 对于一手牌来说，玩家可以：
*   1. 抓牌（添加到这手牌中）
*   2. 计算这手牌的总点数
* */

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
