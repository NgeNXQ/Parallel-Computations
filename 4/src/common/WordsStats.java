package common;

public final class WordsStats
{
    private final int WORDS_COUNT;
    private final double DISPERSION;
    private final double STANDARD_DEVIATION;
    private final double AVERAGE_WORD_LENGTH;

    public WordsStats(int wordsCount, double dispersion, double standardDeviation, double averageWordLength)
    {
        this.WORDS_COUNT = wordsCount;
        this.DISPERSION = dispersion;
        this.STANDARD_DEVIATION = standardDeviation;
        this.AVERAGE_WORD_LENGTH = averageWordLength;
    }

    public int getWordsCount()
    {
        return this.WORDS_COUNT;
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
    }
}
