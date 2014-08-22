package ryan.pope.convocloud.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ryan.pope.convocloud.application.Globals;
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

	public void addWords(String sms) 
	{
		/* Parse each word and count them */
		String parsedSMS = sms.replaceAll("[^A-Za-z0-9 ]+", "");
		String[] splitSMS = parsedSMS.toUpperCase(Locale.getDefault()).split("\\s+");
		for(String s : splitSMS)
		{
			if(s.length() >= Globals.MIN_MESSAGE_SIZE && !Globals.STOP_WORDS.contains(s))
			{
				if(_allWords.get(s) == null)
					_allWords.put(s, 1);
				else
					_allWords.put(s, _allWords.get(s) + 1);
			}
		}
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
