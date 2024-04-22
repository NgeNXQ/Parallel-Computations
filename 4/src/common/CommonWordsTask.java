package common;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

final class CommonWordsTask extends RecursiveTask<HashMap<String, Integer>>
{
    private static int Threshold = 100;

    private final Document DOCUMENT;

    private final int INDEX_START;
    private final int INDEX_FINISH;

    public CommonWordsTask(Document document, int indexStart, int indexFinsih)
    {
        this.DOCUMENT = document;
        this.INDEX_START = indexStart;
        this.INDEX_FINISH = indexFinsih;
    }

    @Override
    protected HashMap<String, Integer> compute()
    {
        HashMap<String, Integer> documentWords = new HashMap<>();

        if ((this.INDEX_FINISH - this.INDEX_START) < CommonWordsTask.Threshold)
        {
            List<String> words = this.DOCUMENT.getWordsSlice(this.INDEX_START, this.INDEX_FINISH);

            for (String word : words)
            {
                documentWords.compute(word, (key, value) -> (value == null) ? 1 : value + 1);
            }
        }
        else
        {
            final int INDEX_MID = this.INDEX_START + (this.INDEX_FINISH - this.INDEX_START) / 2;

            CommonWordsTask firstTask = new CommonWordsTask(this.DOCUMENT, this.INDEX_START, INDEX_MID);
            CommonWordsTask secondTask = new CommonWordsTask(this.DOCUMENT, INDEX_MID, this.INDEX_FINISH);

            ForkJoinTask.invokeAll(firstTask, secondTask);

            HashMap<String, Integer> firstResult = firstTask.join();
            HashMap<String, Integer> secondResult = secondTask.join();

            for (Map.Entry<String, Integer> entry : firstResult.entrySet())
            {
                documentWords.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, Integer> entry : secondResult.entrySet())
            {
                documentWords.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        return documentWords;
    }

    public static int getThreshold()
    {
        return CommonWordsTask.Threshold;
    }

    public static void setThreshold(int value)
    {
        CommonWordsTask.Threshold = value;
    }
}
