package main.java.CallCenter;

import java.time.Instant;

public class Call {
    private String id;
    private int number;
    private long startTime;
    private long endTime;
    private Rank rank;

    public Call(String id, int number) {
        this.id = id;
        this.number = number;
        startTime = Instant.now().getEpochSecond();
    }

    public Call(String id, int number, Rank rank) {  // Call can be instantiated with a rank
        this(id, number);
        this.rank = rank;
    }

    public String getId() { return id; }

    public void setStartTime(long startTime) { this.startTime = startTime; }

    public void setEndTime(long endTime) { this.endTime = endTime; }

    public void incrementRank() {
        Rank newRank = Rank.getRankFromValue(rank.getValue() + 1);
        if (newRank != null)
            rank = newRank;
    }

    public Rank getRank() { return rank; }
}
