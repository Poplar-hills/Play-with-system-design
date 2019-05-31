package main.java.LRUCache;

import java.time.Instant;
import java.util.*;

import static main.java.Utils.Helpers.log;

/*
* Least Recently Used Cache.
*
* - Requirement:
*   1. Fixed Size: Cache needs to have bounds to limit memory usages.
*   2. Fast Access: The insert and lookup operations should be fast, preferably O(1) time.
*   3. Replacement of Entry: Cache evicts the least recently used entry when the specified memory is full.
*   4. Automatic time-out mechanism: No query, regardless of how popular it is, can sit in the cache forever.
*      All cache entries should have a max-age so that overage entries should be cleared. This ensures that
*      the cached contents can be refreshed periodically.
*
* - Implementation:
*   - To get O(1) lookup/access, HashMap is an obvious answer, but it doesn't has mechanism of tracking which
*     entry has been queried recently and which not.
*   - To track recent access, we require another data structure -- Doubly linked list. Reason for that is it
*     provides O(1) insertion, deletion and update on both ends.
*   - To periodically clear the cache, we need a timer running on a different thread to check the cache every
*     xxx minutes and remove it if it's overage.
*   - So our implementation will be a HashMap holding the keys and address of the nodes of a doubly linked list,
*     and the doubly linked list holds the values to the keys (SEE _illustration.png).
* */

public class LRUCache {
    private static int MAX_CACHE_SIZE, MAX_AGE;
    private Map<String, Entry> map;
    private Entry head, tail;
    private Timer timer;

    public LRUCache(int size, int maxAge) {
        MAX_CACHE_SIZE = size;
        MAX_AGE = maxAge;
        map = new HashMap<>();
        Thread thread = new Thread(this::periodicallyClearCache);
        thread.start();
    }

    public String getEntry(String query) {
        if (!map.containsKey(query)) return null;
        Entry entry = map.get(query);
        entry.timestamp = Instant.now().getEpochSecond();  // update its freshness
        removeEntryFromList(entry);
        addToTopOfList(entry);
        return entry.result;
    }

    public void putEntry(String query, String result) {
        if (map.containsKey(query)) {            // update existing entry
            Entry entry = map.get(query);
            entry.result = result;
            removeEntryFromList(entry);
            addToTopOfList(entry);
            return;
        }

        Entry entry = new Entry(query, result);  // create a new entry
        map.put(query, entry);
        addToTopOfList(entry);

        if (map.size() > MAX_CACHE_SIZE) {       // remove the least recently used entry
            map.remove(tail.query);
            removeEntryFromList(tail);
        }
    }

    private void addToTopOfList(Entry entry) {
        entry.prev = null;
        entry.next = head;
        if (head != null) head.prev = entry;  // remember to maintain the head, tail pointers
        head = entry;
        if (tail == null) tail = head;        // same here
    }

    private void removeEntryFromList(Entry entry) {
        Entry prevEntry = entry.prev, nextEntry = entry.next;

        if (prevEntry != null)
            prevEntry.next = nextEntry;
        else
            head = nextEntry;  // remember to maintain the head, tail pointers

        if (nextEntry != null)
            nextEntry.prev = prevEntry;
        else
            tail = prevEntry;  // same here
    }

    private void periodicallyClearCache() {  // schedule a timer to periodically clear expired cache entries
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long currTimestamp = Instant.now().getEpochSecond();
                Iterator<Map.Entry<String, Entry>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Entry cacheEntry = it.next().getValue();
                    if (currTimestamp - cacheEntry.timestamp >= MAX_AGE) {  // check if the cache entry has expired
                        removeEntryFromList(cacheEntry);
                        it.remove();  // Node: we cannot remove entries while looping over it using "for" or "forEach", iterator is the only possible way to do it
                    }
                }
            }
        }, MAX_AGE * 1000);
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

    public void destroy() {
        timer.cancel();
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(3, 2);

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

        try {
            log("Sleeping for 1 seconds...");
            Thread.sleep(1000);
        }
        catch (InterruptedException e) { log(e); }

        cache.putEntry("e", "E");
        log(cache.toString());   // expects E <-> C <-> D ("A" got evicted)

        try {
            log("Sleeping for 1 seconds...");
            Thread.sleep(1000);
        }
        catch (InterruptedException e) { log(e); }

        log(cache.toString());   // expects E ("C", "D" got evicted)

        cache.destroy();
    }
}
