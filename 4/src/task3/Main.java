package task3;

import java.util.Map;
import java.util.HashMap;

import common.*;

final class Main
{
    public static void main(String[] args)
    {
        final Document DOCUMENT_1 = Document.readFile("4/res/1k.txt");
        final Document DOCUMENT_2 = Document.readFile("4/res/10k.txt");
        final Document DOCUMENT_3 = Document.readFile("4/res/100k.txt");

        HashMap<String, Integer> commonWords = DocumentAnalyzer.getCommonWords(DOCUMENT_1, DOCUMENT_2, DOCUMENT_3);

        for (Map.Entry<String, Integer> entry : commonWords.entrySet())
        {
            System.out.println(String.format("Word: %12s | Quantity: %3d", entry.getKey(), entry.getValue()));
        }
    }
}
