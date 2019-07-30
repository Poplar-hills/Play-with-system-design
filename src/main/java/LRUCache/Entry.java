package main.java.LRUCache;

import java.time.Instant;

// An Entry represents a node in the doubly linked list in LRUCache
public class Entry {
    public String query, result;  // in addition to the result, the query is also stored
    public Entry prev, next;
    public long createdAt;       // time reference for whether to expire the entry

    public Entry(String query, String result) {
        this.query = query;
        this.result = result;
        createdAt = Instant.now().getEpochSecond();
    }

    @Override
    public String toString() { return result; }
}

