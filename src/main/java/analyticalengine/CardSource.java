package analyticalengine;
//Card Source Descriptors (non-period: for debugging)


class CardSource {
    String sourceName; // Source of card (usually file name)
    int startIndex; // First index from this source

    public CardSource(String sn, int si) {
        sourceName = sn;
        startIndex = si;
    }
}