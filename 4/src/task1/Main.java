package task1;

import common.*;

final class Main
{
    public static void main(String[] args)
    {
        final int THRESHOLD_REDUCER = 8;

        final Document DOCUMENT_1 = Document.readFile("4/res/1k.txt");

        TotalLengthTask.setThreshold(DOCUMENT_1.getWordsCount() / THRESHOLD_REDUCER);
        TotalSquaresTask.setThreshold(DOCUMENT_1.getWordsCount() / THRESHOLD_REDUCER);

        DocumentAnalyzer.getWordsStatsDefault(DOCUMENT_1).printResults();

        System.out.print("\n");

        DocumentAnalyzer.getWordsStatsEnhanced(DOCUMENT_1).printResults();

        System.out.print("\n");

        final Document DOCUMENT_2 = Document.readFile("4/res/10k.txt");

        TotalLengthTask.setThreshold(DOCUMENT_2.getWordsCount() / THRESHOLD_REDUCER);
        TotalSquaresTask.setThreshold(DOCUMENT_2.getWordsCount() / THRESHOLD_REDUCER);

        DocumentAnalyzer.getWordsStatsDefault(DOCUMENT_2).printResults();

        System.out.print("\n");

        DocumentAnalyzer.getWordsStatsEnhanced(DOCUMENT_2).printResults();

        System.out.print("\n");

        final Document DOCUMENT_3 = Document.readFile("4/res/100k.txt");

        TotalLengthTask.setThreshold(DOCUMENT_3.getWordsCount() / THRESHOLD_REDUCER);
        TotalSquaresTask.setThreshold(DOCUMENT_3.getWordsCount() / THRESHOLD_REDUCER);

        DocumentAnalyzer.getWordsStatsDefault(DOCUMENT_3).printResults();

        System.out.print("\n");

        DocumentAnalyzer.getWordsStatsEnhanced(DOCUMENT_3).printResults();

        System.out.print("\n");
    }
}