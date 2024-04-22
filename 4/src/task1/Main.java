package task1;

import common.*;

final class Main
{
    public static void main(String[] args)
    {
        final Document DOCUMENT_1 = Document.readFile("4/resources/1k.txt");

        DocumentAnalyzer.printWordsStatsDefault(DOCUMENT_1);

        System.out.print("\n");

        DocumentAnalyzer.printWordsStatsEnhanced(DOCUMENT_1);

        System.out.print("\n");

        final Document DOCUMENT_2 = Document.readFile("4/resources/10k.txt");

        DocumentAnalyzer.printWordsStatsDefault(DOCUMENT_2);

        System.out.print("\n");

        DocumentAnalyzer.printWordsStatsEnhanced(DOCUMENT_2);

        System.out.print("\n");

        final Document DOCUMENT_3 = Document.readFile("4/resources/100k.txt");

        DocumentAnalyzer.printWordsStatsDefault(DOCUMENT_3);

        System.out.print("\n");

        DocumentAnalyzer.printWordsStatsEnhanced(DOCUMENT_3);

        System.out.print("\n");
    }
}