package main.java.CallCenter;

public enum Rank {
    Respondent(0),
    Manager(1),
    Director(2);

    private int value;

    Rank(int v) { value = v; }
    public int getValue() { return value; }

    public static Rank getRankFromValue(int value) {
        switch (value) {
            case 0: return Respondent;
            case 1: return Manager;
            case 2: return Director;
            default: return null;
        }
    }
}
