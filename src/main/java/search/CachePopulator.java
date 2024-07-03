package search;

import search.term.metadata.Metadata;
import search.term.metadata.Provider;

import java.util.*;
import java.util.function.Supplier;

public interface CachePopulator<T extends Provider> {

    String exactPrefix = "\u0000";

    default Map<String, Collection<T>>
    loadData(Collection<T> items, int minKeyLength) { //minKeyLength is typically 2, reduces useless large trie size for large tries.
        Map<String, Collection<T>> data = new HashMap<>((int) (items.size() * 1.2)); //Use TreeMap to order the keys for debugging
        for (T item : items) {
            linkFuzzy(data, item, minKeyLength);
            linkExact(data, item);
        }
        return data;
    }

    static <T extends Provider> void
    linkFuzzy(Map<String, Collection<T>> data, T item, int minKeyLength) {
        for (Metadata m : item.getMetadata()) {
            for (String key : m.keys) {
                if (key.startsWith(exactPrefix)) {
                    break;
                }
                for (int pos = minKeyLength; pos <= key.length(); pos++) {
                    String subStr = key.substring(0, pos);
                    data.computeIfAbsent(subStr, k -> new ArrayList<>(24)).add(item);
                }
            }
        }
    }

    static <T extends Provider> void
    linkExact(Map<String, Collection<T>> data, T item) {
        for (Metadata m : item.getMetadata()) {
            for (String key : m.keys) {
                if (key.startsWith(exactPrefix)) {
                    break;
                }
                data.computeIfAbsent(key, k -> new ArrayList<>(24)).add(item);
            }
        }
    }

    default boolean startPeriodicRefresh(long intervalMillis, Supplier<List<T>> supplier, int minKeyLength) {
        try {
            new Timer(true).scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Map<String, Collection<T>> data = loadData(supplier.get(), minKeyLength);
                }
            }, 0, intervalMillis);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    class CacheData<T> {
        private final T data;
        private final Metadata metadata;

        public CacheData(T data, Metadata metadata) {
            this.data = data;
            this.metadata = metadata;
        }

        public T getData() {
            return data;
        }

        public Metadata getMetadata() {
            return metadata;
        }
    }
}
