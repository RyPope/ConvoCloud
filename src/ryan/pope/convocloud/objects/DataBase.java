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
	
	public DataBase()
	{
		_allWords = new HashMap<String, Integer>();
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
		return _allWords.size();
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
}
