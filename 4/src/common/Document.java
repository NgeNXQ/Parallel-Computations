package common;

import java.nio.file.*;
import java.io.IOException;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.util.regex.Pattern;

public final class Document 
{
    private final String FILENAME;
    private final List<String> LINES;
    private final List<String> WORDS;

    private Document(String filename, List<String> lines)
    {
        this.FILENAME = filename;

        this.LINES = lines;
        this.WORDS = new ArrayList<>();

        final Pattern PATTERN = Pattern.compile("[\\s\\p{Punct}]+");

        for (String line : lines)
        {
            this.WORDS.addAll(Arrays.asList(PATTERN.split(line)));
        }
    }

    public String getFilename()
    {
        return this.FILENAME;
    }

    public int getLinesCount()
    {
        return this.LINES.size();
    }

    public int getWordsCount()
    {
        return this.WORDS.size();
    }

    public List<String> getLines()
    {
        return this.LINES;
    }

    public List<String> getWords()
    {
        return this.WORDS;
    }

    public String getLine(int index)
    {
        if (index < 0 || index > this.LINES.size())
        {
            throw new IllegalArgumentException("Index is out of bounds");
        }

        return this.LINES.get(index);
    }

    public String getWord(int index)
    {
        if (index < 0 || index > this.LINES.size())
        {
            throw new IllegalArgumentException("Index is out of bounds");
        }

        return this.WORDS.get(index);
    }

    public List<String> getLinesSlice(int startIndex, int endIndex)
    {
        if (startIndex < 0 || startIndex > this.LINES.size())
        {
            throw new IllegalArgumentException("startIndex is out of bounds");
        }

        if (endIndex < 0 || endIndex > this.LINES.size())
        {
            throw new IllegalArgumentException("endIndex is out of bounds");
        }

        if (startIndex > endIndex)
        {
            throw new IllegalArgumentException("startIndex must be smaller than endIndex");
        }

        return this.LINES.subList(startIndex, endIndex);
    }

    public List<String> getWordsSlice(int startIndex, int endIndex)
    {
        if (startIndex < 0 || startIndex > this.WORDS.size())
        {
            throw new IllegalArgumentException("startIndex is out of bounds");
        }

        if (endIndex < 0 || endIndex > this.WORDS.size())
        {
            throw new IllegalArgumentException("endIndex is out of bounds");
        }

        if (startIndex > endIndex)
        {
            throw new IllegalArgumentException("startIndex must be smaller than endIndex");
        }

        return this.WORDS.subList(startIndex, endIndex);
    }

    public static Document readFile(String path)
    {
        try
        {
            final Path FILE_PATH = Paths.get(path);
            final String FILENAME = FILE_PATH.getFileName().toString();
            return new Document(FILENAME, Files.readAllLines(FILE_PATH));
        }
        catch (IOException exception)
        {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
