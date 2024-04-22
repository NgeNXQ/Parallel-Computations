package task3;

import java.util.Map;
import java.util.HashMap;

import common.*;

final class Main
{
    public static void main(String[] args)
    {
        final Document DOCUMNET_1K = Document.readFile("4/resources/1k.txt");
        final Document DOCUMNET_10K = Document.readFile("4/resources/10k.txt");
        final Document DOCUMNET_100K = Document.readFile("4/resources/100k.txt");

        HashMap<String, Integer> commonWords = DocumentAnalyzer.getCommonWords(DOCUMNET_1K, DOCUMNET_10K, DOCUMNET_100K);

        for (Map.Entry<String, Integer> entry : commonWords.entrySet())
        {
            System.out.println(String.format("Word: %12s | Quantity: %3d", entry.getKey(), entry.getValue()));
        }
    }
}
