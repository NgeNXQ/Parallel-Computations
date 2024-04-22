package common;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.concurrent.ForkJoinPool;

public final class DocumentAnalyzer
{
    public static WordsStats getWordsStatsDefault(Document document)
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

        final double AVERAGE_WORD_LENGTH = (double)totalLength / document.getWordsCount();

        double totalSquares = 0;

        for (String word : document.getWords())
        {
            totalSquares += Math.pow(word.length() - AVERAGE_WORD_LENGTH, 2);
        }

        final double STANDARD_DEVIATION = Math.sqrt(totalSquares / document.getWordsCount());
        final double DISPERSION = STANDARD_DEVIATION / AVERAGE_WORD_LENGTH;

        WordsStats stats = new WordsStats(document.getWordsCount(), DISPERSION, STANDARD_DEVIATION, AVERAGE_WORD_LENGTH);

        long timestepEnd = System.currentTimeMillis();
        long executionTime = timestepEnd - timestepStart;

        System.out.println(String.format("Execution time: %d ms", executionTime));

        return stats;
    }

    public static WordsStats getWordsStatsEnhanced(Document document)
    {
        if (document == null)
        {
            throw new IllegalArgumentException("document cannot be null.");
        }

        long timestepStart = System.currentTimeMillis();

        final int TOTAL_LENGTH = ForkJoinPool.commonPool().invoke(new TotalLengthTask(document, 0, document.getWordsCount()));

        final double AVERAGE_WORD_LENGTH = (double)TOTAL_LENGTH / document.getWordsCount();

        final double TOTAL_SQUARES = ForkJoinPool.commonPool().invoke(new TotalSquaresTask(AVERAGE_WORD_LENGTH, document, 0, document.getWordsCount()));

        final double STANDARD_DEVIATION = Math.sqrt(TOTAL_SQUARES / document.getWordsCount());
        final double DISPERSION = STANDARD_DEVIATION / AVERAGE_WORD_LENGTH;

        long timestepEnd = System.currentTimeMillis();
        long executionTime = timestepEnd - timestepStart;

        WordsStats stats = new WordsStats(document.getWordsCount(), DISPERSION, STANDARD_DEVIATION, AVERAGE_WORD_LENGTH);

        System.out.println(String.format("Execution time: %d ms", executionTime));

        return stats;
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
                HashMap<String, Integer> newCommonWords = new HashMap<>();

                for (String word : commonWords.keySet())
                {
                    if (documentWords.containsKey(word))
                    {
                        newCommonWords.put(word, commonWords.get(word) + documentWords.get(word));
                    }
                }

                commonWords = newCommonWords;
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
