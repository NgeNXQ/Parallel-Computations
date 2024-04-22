package common;

final class WordsStats
{
    private final int WORDS_COUNT;
    private final double VARIANCE;
    private final double DISPERSION;
    private final double STANDARD_DEVIATION;
    private final double AVERAGE_WORD_LENGTH;

    public WordsStats(int wordsCount, double variance, double dispersion, double standardDeviation, double averageWordLength)
    {
        this.WORDS_COUNT = wordsCount;
        this.VARIANCE = variance;
        this.DISPERSION = dispersion;
        this.STANDARD_DEVIATION = standardDeviation;
        this.AVERAGE_WORD_LENGTH = averageWordLength;
    }

    public int getWordsCount()
    {
        return this.WORDS_COUNT;
    }

    public double getVariance()
    {
        return this.VARIANCE;
    }

    public double getDispersion()
    {
        return this.DISPERSION;
    }

    public double getStandardDeviation()
    {
        return this.STANDARD_DEVIATION;
    }

    public double getAverageWordLength()
    {
        return this.AVERAGE_WORD_LENGTH;
    }

    public void printResults()
    {
        System.out.println(String.format("Processed words: %d", this.WORDS_COUNT));
        System.out.println(String.format("Average word length: %.3f", this.AVERAGE_WORD_LENGTH));
        System.out.println(String.format("Standard deviation: %.3f", this.STANDARD_DEVIATION));
        System.out.println(String.format("Dispersion: %.3f", this.DISPERSION));
        System.out.println(String.format("Variance: %.3f", this.VARIANCE));
    }

    public static WordsStats combineStats(WordsStats stats1, WordsStats stats2)
    {
        final int TOTAL_WORDS_COUNT = stats1.WORDS_COUNT + stats2.WORDS_COUNT;

        final double AVERAGE_WORD_LENGTH = (stats1.AVERAGE_WORD_LENGTH * stats1.WORDS_COUNT + stats2.AVERAGE_WORD_LENGTH * stats2.WORDS_COUNT) / TOTAL_WORDS_COUNT;
    
        final double VARIANCE = (stats1.VARIANCE * stats1.WORDS_COUNT + stats2.VARIANCE * stats2.WORDS_COUNT) / TOTAL_WORDS_COUNT;
    
        final double STANDARD_DEVIATION = Math.sqrt(VARIANCE);
    
        final double DISPERSION = STANDARD_DEVIATION / AVERAGE_WORD_LENGTH;
    
        return new WordsStats(TOTAL_WORDS_COUNT, VARIANCE, DISPERSION, STANDARD_DEVIATION, AVERAGE_WORD_LENGTH);
    }
}
