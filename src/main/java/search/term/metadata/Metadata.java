package search.term.metadata;

import java.util.Collection;

public class Metadata {

    Metadata(final Collection<String> key, final int rank, final int sumOfTimesThisKeyAppearsInDifferentRanks) {
        this.keys = key;
        this.rank = rank;
        this.sumOfTimesThisKeyAppearsInDifferentRanks = sumOfTimesThisKeyAppearsInDifferentRanks;
    }

    public final Collection<String> keys; //X To Y characters (2-8 typical) or more for Exact Search;
    public final int rank; //Rank of supplier that this key came from
    public int sumOfTimesThisKeyAppearsInDifferentRanks; //Might want to have this be an independently existing thing
}
