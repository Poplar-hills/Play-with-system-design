package main.java.LRUCache;

public class Entry {
    public String query, result;
    public Entry prev, next;

    public Entry(String query, String result) {
        this.query = query;
        this.result = result;
    }

    public Entry() {}
}

