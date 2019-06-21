package main.java.LRUCache;

import java.time.Instant;

// An Entry represents a node in the doubly linked list in LRUCache
public class Entry {
    public String query, value;  // in addition to the value, the query is also stored
    public Entry prev, next;
    public long createdAt;       // time reference for whether to expire the entry

    public Entry(String query, String value) {
        this.query = query;
        this.value = value;
        createdAt = Instant.now().getEpochSecond();
    }

    @Override
    public String toString() { return value; }
}

