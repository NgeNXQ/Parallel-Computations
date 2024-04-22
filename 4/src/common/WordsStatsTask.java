package common;

import java.util.concurrent.RecursiveTask;

final class WordsStatsTask extends RecursiveTask<WordsStats>
{
    private static int Threshold = 100;

    private final int INDEX_END;
    private final int INDEX_START;
    private final Document DOCUMENT;

    public WordsStatsTask(Document document, int indexStart, int indexEnd)
    {
        this.DOCUMENT = document;
        this.INDEX_END = indexEnd;
        this.INDEX_START = indexStart;
    }

    @Override
    protected WordsStats compute()
    {
        if (this.INDEX_END - this.INDEX_START <= WordsStatsTask.Threshold)
        {
            return calculateStats();
        }
        else
        {
            final int INDEX_MID = (INDEX_END + INDEX_START) / 2;

            WordsStatsTask firstTask = new WordsStatsTask(DOCUMENT, INDEX_START, INDEX_MID);
            WordsStatsTask secondTask = new WordsStatsTask(DOCUMENT, INDEX_MID, INDEX_END);
    
            firstTask.fork();

            WordsStats secondResult = secondTask.compute();
            WordsStats firstResult = firstTask.join();
    
            return WordsStats.combineStats(firstResult, secondResult);
        }
    }

    private WordsStats calculateStats()
    {
        final int WORDS_COUNT = this.DOCUMENT.getWordsSlice(this.INDEX_START, this.INDEX_END).size();

        int totalLength = 0;

        for (String word : this.DOCUMENT.getWordsSlice(this.INDEX_START, this.INDEX_END))
        {
            totalLength += word.length();
        }

        final double AVERAGE_WORD_LENGTH = (double) totalLength / WORDS_COUNT;

        double totalSquares = 0;

        for (String word : this.DOCUMENT.getWordsSlice(this.INDEX_START, this.INDEX_END))
        {
            totalSquares += Math.pow(word.length() - AVERAGE_WORD_LENGTH, 2);
        }

        final double VARIANCE = totalSquares / WORDS_COUNT;
        final double STANDARD_DEVIATION = Math.sqrt(VARIANCE);
        final double DISPERSION = STANDARD_DEVIATION / AVERAGE_WORD_LENGTH;

        return new WordsStats(WORDS_COUNT, VARIANCE, DISPERSION, STANDARD_DEVIATION, AVERAGE_WORD_LENGTH);
    }
}
