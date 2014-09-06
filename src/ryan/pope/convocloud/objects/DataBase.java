package ryan.pope.convocloud.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ryan.pope.convocloud.cloud.objects.WordInfo;

public class DataBase 
{
	private HashMap<String, Integer> _allWords;
	private String _name;
	private int _count;
	
	public DataBase()
	{
		_allWords = new HashMap<String, Integer>();
		_name = "None";
		_count = 0;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public void addWord(String word)
	{
		if(_allWords.get(word) == null)
			_allWords.put(word, 1);
		else
			_allWords.put(word, _allWords.get(word) + 1);
	}
	
	public int getWordCount()
	{
		return _count == 0 ? _allWords.size() : _count;
	}

	public ArrayList<String> getWords() 
	{
		ArrayList<WordInfo> wordFreqList = new ArrayList<WordInfo>();
		ArrayList<String> wordList = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : _allWords.entrySet())
		{
			wordFreqList.add(new WordInfo(entry.getKey(), entry.getValue()));
		}
		
		/* Sort word list and return arraylist */
		Collections.sort(wordFreqList);
		
		for(WordInfo info : wordFreqList)
		{
			wordList.add(info.getWord());
		}
		return wordList;
	}

	public void setFauxWordCount(int count)
	{
		_count = count;
	}
	
	public int getFauxWordCount()
	{
		return _count;
	}

	public boolean isLoaded()
	{
		return _count == 0;
	}
}
