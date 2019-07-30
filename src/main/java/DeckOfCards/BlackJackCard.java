package main.java.DeckOfCards;

/*
* BlackJackCard（BlackJack 中的一张牌）
* - ∵ BlackJack 中的牌跟标准扑克中不完全相同，∴ 继承 Card 类。
* - ∵ BlackJack 规则中，J、Q、K 每张为10点。ace 可记为1点或为11点，∴ result 方法需要 override 父类上的方法。
* - ∵ ace 可能有两种值，∴ 提供 maxValue 和 minValue 两个 public 方法供外界调用，以查看某张牌的是否是 ace。
* - 而至于一张 ace 的值到底是1还是11，需要由玩家当前的一手牌（BlackJackHand）决定，因此不是该类的职责。
* */

public class BlackJackCard extends Card {
    public BlackJackCard(int v, Suit s) {
        super(v, s);
    }

    @Override
    public int value() {
        if (isAce()) return 1;
        if (isFaceCard()) return 10;
        return faceValue;
    }

    protected boolean isAce() {
        return faceValue == 1;
    }

    protected boolean isFaceCard() {
        return faceValue >= 11 && faceValue <= 13;
    }

    public int minValue() {
        return isAce() ? 1 : value();
    }

    public int maxValue() {
        return isAce() ? 11 : value();
    }
}
