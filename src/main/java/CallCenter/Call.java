package main.java.CallCenter;

import java.time.Instant;

public class Call {
    private String id;
    private int number;
    private long startTime;
    private long endTime;
    private CallToughness toughness;

    public Call(String id, int number) {
        this.id = id;
        this.number = number;
        startTime = Instant.now().getEpochSecond();
    }

    public void setEndTime(long endTime) { this.endTime = endTime; }

    public void incrementToughness() {
        CallToughness newUrgency = CallToughness.getToughnessFromValue(toughness.getValue() + 1);
        if (newUrgency != null)
            toughness = newUrgency;
    }

    public CallToughness getToughness() { return toughness; }
}
