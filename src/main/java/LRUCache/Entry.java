package main.java.LRUCache;

import java.time.Instant;

public class Entry {
    public String query, result;
    public Entry prev, next;
    public long timestamp;

    public Entry(String query, String result) {
        this.query = query;
        this.result = result;
        timestamp = Instant.now().getEpochSecond();
    }

    @Override
    public String toString() { return result; }
}

