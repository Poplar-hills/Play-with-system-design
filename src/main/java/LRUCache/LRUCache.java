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
*      All cache entries should have a max-age so that overage entries could be cleared. This ensures that
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
*
* - Enhancement (SEE: p394 of Cracking the Coding Interview 6th Edition):
*   1. Expand to many machines
*   2. Clear the cache based on the topic or the URLs.
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
        Thread thread = new Thread(this::periodicallyClearCache);  // spawn a new thread to periodically clear overage cache
        thread.start();
    }

    public String get(String query) {
        if (!map.containsKey(query)) return null;
        Entry entry = map.get(query);
        entry.createdAt = Instant.now().getEpochSecond();  // update its freshness
        removeEntryFromList(entry);  // place the entry at top of the list after visiting it
        addToTopOfList(entry);
        return entry.value;
    }

    public void put(String query, String result) {
        if (map.containsKey(query)) {  // update existing entry
            Entry entry = map.get(query);
            entry.value = result;
            removeEntryFromList(entry);
            addToTopOfList(entry);
            return;
        }

        Entry entry = new Entry(query, result);  // create a new entry
        map.put(query, entry);
        addToTopOfList(entry);

        if (map.size() > MAX_CACHE_SIZE) {  // remove the least recently used entry
            map.remove(tail.query);
            removeEntryFromList(tail);
        }
    }

    private void addToTopOfList(Entry entry) {
        entry.prev = null;
        entry.next = head;
        if (head != null) head.prev = entry;  // don't forget to maintain the head, tail pointers
        head = entry;
        if (tail == null) tail = head;  // same here
    }

    private void removeEntryFromList(Entry entry) {  // namely removing a node from the linked list
        Entry prevEntry = entry.prev, nextEntry = entry.next;

        if (prevEntry != null) prevEntry.next = nextEntry;
        else head = nextEntry;  // corner case: removing the first node - need to maintain the head pointer

        if (nextEntry != null) nextEntry.prev = prevEntry;
        else tail = prevEntry;  // corner case: removing the last node - need to maintain the tail pointer
    }

    private void periodicallyClearCache() {  // schedule a timer to periodically clear expired cache entries
        timer = new Timer();
        timer.schedule(new TimerTask() {  // TimerTask is not a SAM (single abstract method) type, meaning we cannot pass a lambda here
            @Override
            public void run() {
            long currTimestamp = Instant.now().getEpochSecond();
            Iterator<Map.Entry<String, Entry>> it = map.entrySet().iterator();  // iterate all the entries of the map
            while (it.hasNext()) {
                Entry cacheEntry = it.next().getValue();
                if (currTimestamp - cacheEntry.createdAt >= MAX_AGE) {  // check if the cache entry has expired
                    removeEntryFromList(cacheEntry);
                    it.remove();  // Node: it's impossible to remove entries while looping over the map using "for" or "forEach",
                }                 // iterator is the only way to do it.
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
        timer.cancel();  // once the timer is cancelled, the thread will finish
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(3, 2);

        cache.put("a", "A");
        log(cache.toString());   // expects A

        cache.put("b", "B");
        log(cache.toString());   // expects B <-> A

        String valueA = cache.get("a");
        log(cache.toString());   // expects A <-> B

        cache.put("c", "C");
        log(cache.toString());   // expects C <-> A <-> B

        cache.put("d", "D");
        log(cache.toString());   // expects D <-> C <-> A ("B" got evicted)

        String valueC = cache.get("c");
        log(cache.toString());   // expects C <-> D <-> A

        try {
            log("Sleeping for 1 seconds...");
            Thread.sleep(1000);
        }
        catch (InterruptedException e) { log(e); }

        cache.put("e", "E");
        log(cache.toString());   // expects E <-> C <-> D ("A" got evicted)

        try {
            log("Sleeping for 1 seconds...");
            Thread.sleep(1000);
        }
        catch (InterruptedException e) { log(e); }

        log(cache.toString());   // expects E ("C", "D" got evicted)

        cache.destroy();  // don't forget to cancel the timer so that the release can finish
    }
}
