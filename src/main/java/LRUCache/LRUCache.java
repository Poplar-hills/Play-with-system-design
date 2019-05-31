package main.java.LRUCache;

import java.util.HashMap;
import java.util.Map;

import static main.java.Utils.Helpers.log;

/*
* Least Recently Used Cache.
*
* - Requirement:
*   1. Fixed Size: Cache needs to have bounds to limit memory usages.
*   2. Fast Access: The insert and lookup operations should be fast, preferably O(1) time.
*   3. Replacement of Entry: Cache evicts the least recently used entry when the specified memory is full.
* */

public class LRUCache {
    private static final int MAX_CACHE_SIZE = 3;
    private Map<String, Entry> map;
    private Entry head, tail;

    public LRUCache() {
        map = new HashMap<>();
    }

    public String getEntry(String query) {
        if (!map.containsKey(query)) return null;
        Entry entry = map.get(query);
        removeEntry(entry);
        addToTop(entry);
        return entry.result;
    }

    public void putEntry(String query, String result) {
        if (map.containsKey(query)) {            // update existing entry
            Entry entry = map.get(query);
            entry.result = result;
            removeEntry(entry);
            addToTop(entry);
            return;
        }

        Entry entry = new Entry(query, result);  // create a new entry
        map.put(query, entry);
        addToTop(entry);

        if (map.size() > MAX_CACHE_SIZE) {       // remove the least recently used entry
            map.remove(tail.query);
            removeEntry(tail);
        }
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
            head = nextEntry;  // Remember to maintain the head, tail pointers

        if (nextEntry != null)
            nextEntry.prev = prevEntry;
        else
            tail = prevEntry;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("{ ");
        map.forEach((k, v) -> s.append(String.format("%s: %s; ", k, v)));  // print the map
        s.append("}\n");

        for (Entry curr = head; curr != null; curr = curr.next) {  // print the linked list
            s.append(curr.toString());
            if (curr != tail) s.append(" <-> ");
        }

        return s.toString();
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache();

        cache.putEntry("a", "A");
        log(cache.toString());   // expects A

        cache.putEntry("b", "B");
        log(cache.toString());   // expects B <-> A

        cache.getEntry("a");
        log(cache.toString());   // expects A <-> B

        cache.putEntry("c", "C");
        log(cache.toString());   // expects C <-> A <-> B

        cache.putEntry("d", "D");
        log(cache.toString());   // expects D <-> C <-> A ("B" got evicted)

        cache.getEntry("c");
        log(cache.toString());   // expects C <-> D <-> A

        cache.putEntry("e", "E");
        log(cache.toString());   // expects E <-> C <-> D ("A" got evicted)
    }
}
