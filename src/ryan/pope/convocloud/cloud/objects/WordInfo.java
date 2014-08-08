package ryan.pope.convocloud.cloud.objects;

public class WordInfo implements Comparable<WordInfo> 
{

    private String _word;

    private int _frequency;

    public WordInfo(String word, int frequency) 
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
    public int compareTo(WordInfo wordFrequency) 
    {
        return wordFrequency._frequency - _frequency;
    }

}
