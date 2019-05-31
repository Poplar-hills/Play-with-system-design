package main.java.LRUCache;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    private static int MAX_CACHE_SIZE = 3;
    private Map<String, Entry> map;
    private Entry head, tail;

    public LRUCache() {
        map = new HashMap<>();
    }

    public String getEntry(String query) {
        if (!map.containsKey(query)) return null;
        Entry entry = map.get(query);
        addToTop(entry);
        return entry.result;
    }

    public void putEntry(String query, String result) {
        if (map.containsKey(query)) {
            Entry entry = map.get(query);
            entry.result = result;
            removeEntry(entry);
            addToTop(entry);
            return;
        }

        Entry entry = new Entry(query, result);
        map.put(query, entry);
        addToTop(entry);

        if (map.size() > MAX_CACHE_SIZE)
            removeEntry(tail);
    }

    private void addToTop(Entry entry) {
        entry.prev = null;
        entry.next = head;
        if (head != null) head.prev = entry;
        head = entry;
        if (tail == null) tail = head;
    }

    private void removeEntry(Entry entry) {
        Entry prevEntry = entry.prev, nextEntry = entry.next;

        if (prevEntry != null)
            prevEntry.next = nextEntry;
        else
            head = nextEntry;  // 注意双向链表的头尾指针都需要维护

        if (nextEntry != null)
            nextEntry.prev = prevEntry;
        else
            tail = prevEntry;
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache();
        cache.putEntry("a", "AAA");
        cache.putEntry("b", "BBB");
        cache.putEntry("c", "CCC");
    }
}
