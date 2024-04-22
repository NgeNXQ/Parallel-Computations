package common;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

final public class DocumentAnalyzer
{
    public static void printWordsStatsDefault(Document document)
    {
        if (document == null)
        {
            throw new IllegalArgumentException("document cannot be null.");
        }

        long timestepStart = System.currentTimeMillis();

        int totalLength = 0;

        for (String word : document.getWords())
        {
            totalLength += word.length();
        }

        final double AVERAGE_WORD_LENGTH = (double) totalLength / document.getWordsCount();

        double totalSquares = 0;

        for (String word : document.getWords())
        {
            totalSquares += Math.pow(word.length() - AVERAGE_WORD_LENGTH, 2);
        }

        final double VARIANCE = totalSquares / document.getWordsCount();
        final double STANDARD_DEVIATION = Math.sqrt(VARIANCE);
        final double DISPERSION = STANDARD_DEVIATION / AVERAGE_WORD_LENGTH;

        WordsStats stats = new WordsStats(document.getWordsCount(), VARIANCE, DISPERSION, STANDARD_DEVIATION, AVERAGE_WORD_LENGTH);

        long timestepEnd = System.currentTimeMillis();
        long executionTime = timestepEnd - timestepStart;

        stats.printResults();

        System.out.println(String.format("Execution time: %d ms", executionTime));
    }

    public static void printWordsStatsEnhanced(Document document)
    {
        if (document == null)
        {
            throw new IllegalArgumentException("document cannot be null.");
        }

        long timestepStart = System.currentTimeMillis();

        WordsStats stats = ForkJoinPool.commonPool().invoke(new WordsStatsTask(document, 0, document.getWordsCount()));

        long timestepEnd = System.currentTimeMillis();
        long executionTime = timestepEnd - timestepStart;

        stats.printResults();

        System.out.println(String.format("Execution time: %d ms", executionTime));
    }

    public static HashMap<String, Integer> getCommonWords(Document... documents)
    {
        HashMap<String, Integer> commonWords = new HashMap<>();

        for (Document document : documents)
        {
            HashMap<String, Integer> documentWords = ForkJoinPool.commonPool().invoke(new CommonWordsTask(document, 0, document.getWordsCount()));

            if (commonWords.isEmpty())
            {
                commonWords.putAll(documentWords);
            }
            else
            {
                commonWords.keySet().retainAll(documentWords.keySet());

                for (String word : commonWords.keySet())
                {
                    commonWords.put(word, commonWords.get(word) + documentWords.get(word));
                }
            }
        }

        return commonWords;
    }

    public static List<Document> getDocumentsWithKeyWords(List<String> keywords, Document... documents)
    {
        List<Document> result =  new ArrayList<>();

        for (Document document : documents)
        {
            if (ForkJoinPool.commonPool().invoke(new KeywordsSearchTask(document, keywords, 0, document.getWordsCount())))
            {
                result.add(document);
            }
        }

        return result;
    }
}
