package main.java.CallCenter;

public enum CallToughness {
    Low(0),
    Medium(1),
    High(2);

    private int value;

    CallToughness(int v) { value = v; }
    public int getValue() { return value; }

    public static CallToughness getToughnessFromValue(int value) {
        switch (value) {
            case 0: return Low;
            case 1: return Medium;
            case 2: return High;
            default: return null;
        }
    }
}
