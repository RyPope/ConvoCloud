package ryan.pope.textcloud.cloud.objects;

public class WordFrequency implements Comparable<WordFrequency> 
{

    private String _word;

    private int _frequency;

    public WordFrequency(String word, int frequency) 
    {
        _word = word;
        _frequency = frequency;
    }

    public String getWord() 
    {
        return _word;
    }

    public int getFrequency() 
    {
        return _frequency;
    }

    @Override
    public int compareTo(WordFrequency wordFrequency) 
    {
        return wordFrequency._frequency - _frequency;
    }

}
