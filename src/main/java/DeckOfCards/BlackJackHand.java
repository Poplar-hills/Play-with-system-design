package main.java.DeckOfCards;

import java.util.ArrayList;
import java.util.List;

/*
* BlackJackHand（BlackJack 中的一手牌）
* - BlackJackHand 的总点数计算（score 方法）不像在 Hand 中那么简单，因为 ace 既可是1点（若总点数超过21），也可是11点（若总点数不超过21）。
* */

public class BlackJackHand extends Hand<BlackJackCard> {
    @Override
    public int score() {
        List<Integer> possibleScores = getPossibleScores();  // 先找到一手牌总点数的所有可能性
        int maxUnder = Integer.MIN_VALUE;  // 小于21的最大值
        int minOver = Integer.MAX_VALUE;   // 大于21的最小值

        for (int score : possibleScores) {
            if (score > 21 && score < minOver)
                minOver = score;
            if (score <= 21 && score > maxUnder)
                maxUnder = score;
        }
        return maxUnder == Integer.MIN_VALUE ? minOver : maxUnder;  // 若所有可能性都大于21，则爆牌（返回 minOver），否则返回 maxUnder
    }

    private List<Integer> getPossibleScores() {  // 实现1
        List<Integer> scores = new ArrayList<>();
        if (cards.isEmpty()) return scores;

        scores.add(0);
        for (BlackJackCard card : cards) {
            int len = scores.size();
            for (int i = 0; i < len; i++) {  // 把每一张牌的点数加到 scores 中的每一个 score 上
                int score = scores.get(i);
                scores.set(i, score + card.minValue());
                if (card.maxValue() != card.minValue())   // 如果是 ace，则再往 scores 中推入一个新值
                    scores.add(score + card.maxValue());
            }
        }

        return scores;
    }

    private List<Integer> getPossibleScores2() {  // 实现2
        List<Integer> scores = new ArrayList<>();
        if (cards.isEmpty()) return scores;

        int aceCount = 0;
        int score = 0;
        int aceMaxValue = 0;

        for (BlackJackCard card : cards) {  // 所有的 ace 按1点计算，获得所有牌的点数
            if (card.isAce()) {
                aceCount++;
                aceMaxValue = card.maxValue();  // 小缺陷：这里会重复赋值
            }
            score += card.minValue();
        }

        for (int i = 0; i <= aceCount; i++)  // 有几张 ace 就往 scores 里添加几个不同的点数
            scores.add(score + aceMaxValue * i);

        return scores;
    }

    public boolean busted() {
        return score() > 21;
    }

    public boolean is21() {
        return score() == 21;
    }

    public boolean isBlackJack() {  // 若玩家只有两张牌，且点数为21点则为 black jack
        if (cards.size() != 2) return false;
        BlackJackCard first = cards.get(0);
        BlackJackCard second = cards.get(1);
        return (first.isAce() && second.isFaceCard()) || (second.isAce() && first.isFaceCard());
    }
}
