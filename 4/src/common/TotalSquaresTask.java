package common;

import java.util.List;

import java.util.concurrent.RecursiveTask;

public class TotalSquaresTask extends RecursiveTask<Double>
{
    private static int Threshold = 100;

    private final int INDEX_END;
    private final int INDEX_START;
    private final Document DOCUMENT;

    private final double AVERAGE_WORD_LENGTH;

    public TotalSquaresTask(double averageWordLength, Document document, int indexStart, int indexEnd)
    {
        this.DOCUMENT = document;
        this.INDEX_END = indexEnd;
        this.INDEX_START = indexStart;

        this.AVERAGE_WORD_LENGTH = averageWordLength;
    }

    @Override
    protected Double compute()
    {
        if (this.INDEX_END - this.INDEX_START <= TotalSquaresTask.Threshold)
        {
            double totalSquares = 0;
            List<String> wordsSlice = this.DOCUMENT.getWordsSlice(this.INDEX_START, this.INDEX_END);

            for (String word : wordsSlice)
            {
                totalSquares += Math.pow(word.length() - this.AVERAGE_WORD_LENGTH, 2);
            }

            return totalSquares;
        }
        else
        {
            final int INDEX_MID = (INDEX_END + INDEX_START) / 2;

            TotalSquaresTask firstTask = new TotalSquaresTask(this.AVERAGE_WORD_LENGTH, this.DOCUMENT, this.INDEX_START, INDEX_MID);
            TotalSquaresTask secondTask = new TotalSquaresTask(this.AVERAGE_WORD_LENGTH, this.DOCUMENT, INDEX_MID, this.INDEX_END);

            firstTask.fork();
            secondTask.fork();

            return firstTask.join() + secondTask.join();
        }
    }

    public static int getThreshold()
    {
        return TotalSquaresTask.Threshold;
    }

    public static void setThreshold(int value)
    {
        TotalSquaresTask.Threshold = value;
    }
}
