package common;

import java.util.List;

import java.util.concurrent.RecursiveTask;

public final class TotalLengthTask extends RecursiveTask<Integer>
{
    private static int Threshold = 100;

    private final int INDEX_END;
    private final int INDEX_START;
    private final Document DOCUMENT;

    public TotalLengthTask(Document document, int indexStart, int indexEnd)
    {
        this.DOCUMENT = document;
        this.INDEX_END = indexEnd;
        this.INDEX_START = indexStart;
    }

    @Override
    protected Integer compute()
    {
        if ((this.INDEX_END - this.INDEX_START) <= TotalLengthTask.Threshold)
        {
            int totalLength = 0;
            List<String> wordsSlice = this.DOCUMENT.getWordsSlice(this.INDEX_START, this.INDEX_END);

            for (String word : wordsSlice)
            {
                totalLength += word.length();
            }

            return totalLength;
        }
        else
        {
            final int INDEX_MID = (INDEX_END + INDEX_START) / 2;

            TotalLengthTask firstTask = new TotalLengthTask(this.DOCUMENT, this.INDEX_START, INDEX_MID);
            TotalLengthTask secondTask = new TotalLengthTask(this.DOCUMENT, INDEX_MID, this.INDEX_END);

            firstTask.fork();
            secondTask.fork();

            return firstTask.join() + secondTask.join();
        }
    }

    public static int getThreshold()
    {
        return TotalLengthTask.Threshold;
    }

    public static void setThreshold(int value)
    {
        TotalLengthTask.Threshold = value;
    }
}
