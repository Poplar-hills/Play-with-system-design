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
*
* - Java 中其实已有现成的实现 - LinkedHashMap，简介 SEE：https://www.jianshu.com/p/cb3573eb1890。
*   - HashMap vs. LinkedHashMap vs. TreeMap?
*     ╔══════════════════╦═════════════════════╦═══════════════════╦═══════════════════════╗
*     ║    Property      ║       HashMap       ║      TreeMap      ║     LinkedHashMap     ║
*     ╠══════════════════╬═════════════════════╬═══════════════════╬═══════════════════════╣
*     ║ Iteration Order  ║  no guarantee order ║   sorted order    ║    insertion order    ║
*     ╠══════════════════╬═════════════════════╬═══════════════════╬═══════════════════════╣
*     ║  Get/put/remove  ║        O(1)         ║      O(logn)      ║          O(1)         ║
*     ╠══════════════════╬═════════════════════╬═══════════════════╬═══════════════════════╣
*     ║ Null values/keys ║       allowed       ║    only values    ║        allowed        ║
*     ╠══════════════════╬═════════════════════╬═══════════════════╬═══════════════════════╣
*     ║  Implementation  ║       buckets       ║  Red-Black Tree   ║ double-linked buckets ║
*     ╚══════════════════╩═════════════════════╩═══════════════════╩═══════════════════════╝
* */

public class LRUCache {
    private static int MAX_CACHE_SIZE, MAX_AGE, CLEAR_INTERVAL;
    private Map<String, Entry> map;
    private Entry head, tail;
    private Timer timer;

    public LRUCache(int size, int maxAge, int clearInterval) {
        MAX_CACHE_SIZE = size;
        MAX_AGE = maxAge;
        CLEAR_INTERVAL = clearInterval;
        map = new HashMap<>();
        new Thread(this::periodicallyClearCache).start();  // spawn a new thread to periodically clear expired cache entries
    }

    public String get(String query) {
        if (!map.containsKey(query)) return null;          // cache miss
        Entry entry = map.get(query);                      // cache hit
        entry.createdAt = Instant.now().getEpochSecond();  // update its freshness
        removeEntryFromList(entry);  // relocate the entry to the top of the list after visiting it
        addToTopOfList(entry);
        return entry.result;
    }

    public void put(String query, String result) {
        if (map.containsKey(query)) {  // update existing entry
            Entry entry = map.get(query);
            entry.result = result;
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
        if (head != null) head.prev = entry;  // don't forget to maintain the head and tail pointers
        head = entry;
        if (tail == null) tail = head;  // same here
    }

    private void removeEntryFromList(Entry entry) {  // namely removing a node from the linked list
        Entry prevEntry = entry.prev;
        Entry nextEntry = entry.next;

        if (prevEntry != null) prevEntry.next = nextEntry;
        else head = nextEntry;  // corner case: removing the first node - need to maintain the head pointer

        if (nextEntry != null) nextEntry.prev = prevEntry;
        else tail = prevEntry;  // corner case: removing the last node - need to maintain the tail pointer
    }

    private void periodicallyClearCache() {  // schedule a timer to periodically clear expired cache entries
        timer = new Timer();
        timer.schedule(new TimerTask() {  // TimerTask is not a SAM (single abstract method) type, meaning we cannot pass a lambda here
            @Override
            public void run() {                // just like the timeout function in JS
            long currTimestamp = Instant.now().getEpochSecond();
            Iterator<Map.Entry<String, Entry>> it = map.entrySet().iterator();  // iterate the map entries
            while (it.hasNext()) {
                Entry cacheEntry = it.next().getValue();
                if (currTimestamp - cacheEntry.createdAt >= MAX_AGE) {  // check if the cache entry has expired
                    removeEntryFromList(cacheEntry);
                    it.remove();  // Node: it's impossible to remove entries while looping over the map using "for" or "forEach",
                }                 // iterator is the only way to do it.
            }
            }
        }, CLEAR_INTERVAL);
    }

    public void destroy() {
        timer.cancel();  // once the timer is cancelled, the thread will end
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
        LRUCache cache = new LRUCache(3, 2, 2000);

        cache.put("a", "A");
        log(cache.toString());   // expects A

        cache.put("b", "B");
        log(cache.toString());   // expects B <-> A

        cache.get("a");          // visit entry a
        log(cache.toString());   // expects A <-> B

        cache.put("c", "C");
        log(cache.toString());   // expects C <-> A <-> B

        cache.put("d", "D");
        log(cache.toString());   // expects D <-> C <-> A ("B" got evicted as the cache capacity is 3)

        cache.get("c");          // visit entry c
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

        log(cache.toString());   // expects E ("C", "D" expired and got cleared)

        cache.destroy();  // don't forget to stop the timer to destroy the timer thread
    }
}
