package main.java.LRUCache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    public static int MAX_SIZE = 5;
    public Map<String, Node> map;
    public Node head, tail;
    public int listLen = 0;

    public LRUCache() {
        map = new HashMap<>();
    }

    public void updateFreshness(Node node) {
        node.next = head;
        head = node;
    }

    public void removeFromLinkedList(Node node) {

    }

    public String getResults(String query) {
        if (!map.containsKey(query)) return null;
        Node node = map.get(query);
        updateFreshness(node);
        return node.result;
    }

    public void putResults(String query, String result) {}

    public static void main(String[] args) {
        LRUCache cache = new LRUCache();
    }
}
