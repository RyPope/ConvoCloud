package ryan.pope.convocloud.cloud.objects;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.cloud.colour.ColourPalette;
import ryan.pope.convocloud.cloud.tools.AngleGenerator;
import ryan.pope.convocloud.objects.RotationType;
import ryan.pope.convocloud.objects.Scheme;
import ryan.pope.convocloud.presentation.ProgressDialogHelper;

public class WordCloud 
{
	protected static final Random RANDOM = new Random();
	protected int _width;
	protected int _height;
	protected int _backgroundColor = Color.WHITE;
	protected AngleGenerator _angleGenerator;
	protected Bitmap _imageBitmap;
	protected Typeface _mainTypeface;
	protected ColourPalette _colorPalette;
	private ProgressDialogHelper _progressHelper;
	private boolean _running = true;
	private Canvas _bitmapCanvas;
	private Scheme _scheme;
	private RotationType _rotation;
	private ArrayList<String> _excludedWords;
	private ArrayList<String> _wordsRemaining;
	private boolean _fresh;

	public WordCloud(ProgressDialogHelper progressHelper, int width, int height) 
	{
		_width = width;
		_height = height;
		
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Width: " + width + " Height: " + height);
		_progressHelper = progressHelper;
		_imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		_bitmapCanvas = new Canvas(_imageBitmap);
		_excludedWords = new ArrayList<String>();
		_wordsRemaining = new ArrayList<String>();
		_scheme = Scheme.Default;
		_rotation = RotationType.Random;
		_fresh = true;
		
	}

	public void build(ArrayList<String> wordList) 
	{
		_wordsRemaining = new ArrayList<String>(wordList);
		_colorPalette = new ColourPalette(_scheme);
		_angleGenerator = new AngleGenerator(_rotation);
		
		if(_fresh)
		{
			drawBackgroundColor();
			insertWatermark();
		}
		
		int minimumFontPixelSize = _width / 35;
		int _numPlaced = 1;
		
		MaximalRectangle maxRect = new MaximalRectangle();
		for (String wordToPlace : wordList)
		{
			if(!_running)
				break;
			
			if(!_excludedWords.contains(wordToPlace))
			{
				Rect rect = maxRect.maximalRect(_imageBitmap, _backgroundColor);
				/* If the biggest rectangle available is smaller than the minimum size, finish */
				if(rect.width() < minimumFontPixelSize && rect.height() < minimumFontPixelSize)
					break;
	
				Word word = new Word(wordToPlace, _colorPalette.random(), rect, _mainTypeface, _angleGenerator.randomNext(), false);
				
				draw(word);
				
				if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "left: " + rect.left + " top: " + rect.top + " right: " + rect.right + " bottom: " + rect.bottom + " area: " + rect.width() * rect.height()); 
			}
		
			if(_running)
			{
				_progressHelper.changeCloudDialogMessage((wordList.size() - _numPlaced) + " placeable words remaining." + Globals.CLOUD_NOTE);
			}
				
			if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Placing " + _numPlaced + " of " + wordList.size() + " Word: " + wordToPlace);
			_numPlaced++;
			_wordsRemaining.remove(wordToPlace);
			
		}
	}

	private void draw(Word word) 
	{
		try
		{
			_bitmapCanvas.drawBitmap(word.getImageBitmap(), word.getX(), word.getY(), null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void drawBackgroundColor() 
	{
		Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fillPaint.setColor(_backgroundColor);
		_bitmapCanvas.drawRect(0, 0, _width, _height, fillPaint);
	}

	public Bitmap getBufferedImage() 
	{
		return _imageBitmap;
	}
	
	public void setScheme(Scheme scheme)
	{
		_scheme = scheme;
	}

	private void insertWatermark() 
	{
		Rect rect = new Rect(_width - (_width / 2), _height - (_height / 15), _width, _height);
		int wordColor = Color.rgb(255-Color.red(_backgroundColor), 255-Color.green(_backgroundColor), 255 - Color.blue(_backgroundColor));
		Word watermark = new Word("#ConvoCloud", wordColor, rect, _mainTypeface, 0, true);
		draw(watermark);
	}

	public void kill() 
	{
		_running  = false;
	}

	public void setAngleGenerator(AngleGenerator angleGenerator)
	{
		_angleGenerator = angleGenerator;
	}
	
	public void setBackgroundColor(int backgroundColor) 
	{
		_backgroundColor = backgroundColor;
	}

	public void setTypeface(Typeface typeface)
	{
		_mainTypeface = typeface;
	}

	public void writeToFile(final String outputFileName) 
	{

		FileOutputStream out = null;
		try 
		{
			out = new FileOutputStream(outputFileName);
			_imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try
			{
				out.close();
			} 
			catch(Exception e) 
			{

			}
		}
	}

	public void setExcludedWords(ArrayList<String> excludedWords)
	{
		_excludedWords = excludedWords;
		
	}

	public void setRotation(RotationType rotation)
	{
		_rotation = rotation;
	}

	public void setBitmap(String path)
	{ 
		_fresh = false;
        FileInputStream in;
        BufferedInputStream buf;
        try 
        {
       	    in = new FileInputStream(path);
            buf = new BufferedInputStream(in);
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            
    		if(bMap != null)
    		{
    			_bitmapCanvas.drawBitmap(bMap, 0, 0, null);
    			if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Loaded partial bitmap ");
    		}
    		
            if (in != null) 
            {
            	
            	in.close();
            }
            if (buf != null) 
            {
            	buf.close();
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
		
	}

	public ArrayList<String> getRemaining()
	{
		return _wordsRemaining;
	}
}
