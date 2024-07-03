package search.term.metadata;

import java.util.LinkedHashSet;

public interface Provider {
    LinkedHashSet<Metadata> getMetadata();
}