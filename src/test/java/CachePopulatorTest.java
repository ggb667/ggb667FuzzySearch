import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import search.CachePopulator;
import search.term.metadata.Metadata;
import search.term.metadata.Provider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

class CachePopulatorTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void loadData() {
    }

    @Test
    void linkFuzzy() {
    }

    @Test
    void linkExact() {
    }

    @Test
    void startPeriodicRefresh() {
    }

    @Test
    void CacheData() {
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public class Word {
        private final String word;
        private final String partOfSpeech;
        private final int syllables;
        private final String definition;
    }

    @Slf4j
    public class DictionaryCachePopulator implements Provider {

        private final Map<String, Collection<Word>> data = new HashMap<>();

        public DictionaryCachePopulator(String dictionaryFilePath) {
            startPeriodicRefresh(60000, this::loadDictionary); // Start periodic refresh with an interval of 60 seconds
        }

        @Override
        public LinkedHashSet<Metadata> getMetadata() {
            LinkedHashSet<Metadata> metadataSet = new LinkedHashSet<>();
            for (Collection<Word> words : data.values()) {
                for (Word word : words) {
                    metadataSet.add( /* getSearchTermMetadata(word) */ );
                }
            }
            return metadataSet;
        }

        private List<Word> loadDictionary() {
            List<Word> words = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/search/dictionary.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 4) {
                        String word = parts[0];
                        String partOfSpeech = parts[1];
                        int syllables = Integer.parseInt(parts[2]);
                        String definition = parts[3];
                        Word wordObj = new Word(word, partOfSpeech, syllables, definition);
                        words.add(wordObj);

                        data.computeIfAbsent(word, k -> new ArrayList<>()).add(wordObj);

                        // Print the cached data inline with reading
                        System.out.println("Word: " + word);
                        System.out.println("Part of Speech: " + partOfSpeech);
                        System.out.println("Syllables: " + syllables);
                        System.out.println("Definition: " + definition);
                        System.out.println();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return words;
        }

        private boolean startPeriodicRefresh(long intervalMillis, Supplier<List<Word>> supplier) {
            return startPeriodicRefresh(intervalMillis, supplier);
        }
    }
}