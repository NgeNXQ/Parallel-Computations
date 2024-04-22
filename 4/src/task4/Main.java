package task4;

import java.util.List;
import java.util.Arrays;

import common.*;

final class Main
{
    public static void main(String[] args)
    {
        final Document DOCUMENT_1 = Document.readFile("4/resources/1k.txt");
        final Document DOCUMENT_2 = Document.readFile("4/resources/10k.txt");
        final Document DOCUMENT_3 = Document.readFile("4/resources/100k.txt");
        final Document DOCUMENT_4 = Document.readFile("4/resources/hightech.txt");

        final List<String> KEYWORDS = Arrays.asList("tech", "technology", "innovation", "sector", "industr", "web", "device", "startup", "service", "global");

        List<Document> documents = DocumentAnalyzer.getDocumentsWithKeyWords(KEYWORDS, DOCUMENT_1, DOCUMENT_2, DOCUMENT_3, DOCUMENT_4);

        for (Document document : documents)
        {
            System.out.print(String.format("%s\n", document.getFilename()));
        }
    }
}
