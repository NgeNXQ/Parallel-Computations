package common;

import java.util.List;

import java.util.concurrent.RecursiveTask;

public class KeywordsSearchTask extends RecursiveTask<Boolean>
{
    private static int Threshold = 100;
    private static int MinRequiredKeywordsCount = 5;

    private final Document DOCUMENT;
    private final List<String> KEYWORDS;

    private final int INDEX_START;
    private final int INDEX_FINISH;

    public KeywordsSearchTask(Document document, List<String> keywords, int indexStart, int indexFinish)
    {
        this.DOCUMENT = document;
        this.KEYWORDS = keywords;

        this.INDEX_START = indexStart;
        this.INDEX_FINISH = indexFinish;
    }

    @Override
    protected Boolean compute()
    {
        if (this.INDEX_FINISH - this.INDEX_START < Threshold)
        {
            int count = 0;
            List<String> words = this.DOCUMENT.getWordsSlice(this.INDEX_START, this.INDEX_FINISH);

            for (String word : words)
            {
                for (String keyword : KEYWORDS)
                {
                    if (word.toLowerCase().contains(keyword.toLowerCase()))
                    {
                        ++count;

                        if (count >= KeywordsSearchTask.MinRequiredKeywordsCount)
                        {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
        else
        {
            final int INDEX_MID = this.INDEX_START + (this.INDEX_FINISH - this.INDEX_START) / 2;

            KeywordsSearchTask firstTask = new KeywordsSearchTask(this.DOCUMENT, this.KEYWORDS, this.INDEX_START, INDEX_MID);
            KeywordsSearchTask secondTask = new KeywordsSearchTask(this.DOCUMENT, this.KEYWORDS, INDEX_MID, this.INDEX_FINISH);

            firstTask.fork();
            secondTask.fork();

            return firstTask.join() || secondTask.join();
        }
    }

    public static int getThreshold()
    {
        return KeywordsSearchTask.Threshold;
    }

    public static void setThreshold(int value)
    {
        KeywordsSearchTask.Threshold = value;
    }

    public static int getMinRequiredKeywordsCount()
    {
        return KeywordsSearchTask.MinRequiredKeywordsCount;
    }

    public static void setMinRequiredKeywordsCount(int value)
    {
        KeywordsSearchTask.MinRequiredKeywordsCount = value;
    }
}
