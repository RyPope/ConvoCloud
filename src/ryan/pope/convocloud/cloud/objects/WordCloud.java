package ryan.pope.convocloud.cloud.objects;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.cloud.background.Background;
import ryan.pope.convocloud.cloud.background.RectangleBackground;
import ryan.pope.convocloud.cloud.collide.image.AngleGenerator;
import ryan.pope.convocloud.cloud.palette.ColorPalette;
import ryan.pope.convocloud.presentation.ProgressDialogHelper;

public class WordCloud 
{
	protected static final Random RANDOM = new Random();
	protected int _width;
	protected int _height;
	protected Background _background;
	protected int _backgroundColor = Color.WHITE;
	protected AngleGenerator _angleGenerator = new AngleGenerator();
	protected Bitmap _imageBitmap;
	protected Typeface _mainTypeface;
	protected ColorPalette _colorPalette = new ColorPalette(Color.RED, Color.BLACK, Color.YELLOW, Color.GRAY, Color.GREEN);
	private ProgressDialogHelper _progressHelper;
	private boolean _running = true;
	private Canvas _bitmapCanvas;

	public WordCloud(ProgressDialogHelper progressHelper, int width, int height) 
	{
		_width = width;
		_height = height;
		
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Width: " + width + " Height: " + height);
		_progressHelper = progressHelper;
		_imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		_bitmapCanvas = new Canvas(_imageBitmap);
		_background = new RectangleBackground(width, height);
	}

	public void build(ArrayList<WordFrequency> wordFrequencies) 
	{
		Collections.sort(wordFrequencies);
		drawBackgroundColor();
		insertWatermark();

		/* Starter width */
		int minimumFontPixelSize = _width / 50;
		int i = 1;
		
		MaximalRectangle maxRect = new MaximalRectangle();
		for (WordFrequency wordFreq : wordFrequencies)
		{
			if(!_running)
				break;
			
			
			double theta = _angleGenerator.randomNext();
			Rect rect = maxRect.maximalRect(_imageBitmap);
			
			if(rect.width() < minimumFontPixelSize && rect.height() < minimumFontPixelSize)
				break;

			Word word = new Word(wordFreq.getWord(), _colorPalette.next(), rect, _mainTypeface, theta);

			_progressHelper.changeCloudDialogMessage("Placing " + i + " of " + wordFrequencies.size() + "\nNote: Not all words will be placed.");
			if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Placing " + i + " of " + wordFrequencies.size());
			
			draw(word);
			
			if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "left: " + rect.left + " top: " + rect.top + " right: " + rect.right + " bottom: " + rect.bottom + " area: " + rect.width() * rect.height()); 
			i++;
			
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

	private void insertWatermark() 
	{
		Rect rect = new Rect(_width - (_width / 2), _height - (_height / 15), _width, _height);
		Word watermark = new Word("#ConvoCloud", Color.BLACK, rect, _mainTypeface, 0);
		draw(watermark);
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

	protected void drawBackgroundColor() 
	{
		Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		fillPaint.setColor(_backgroundColor);
		_bitmapCanvas.drawRect(0, 0, _width, _height, fillPaint);
	}

	public void setBackgroundColor(int backgroundColor) 
	{
		_backgroundColor = backgroundColor;
	}

	public void setTypeface(Typeface typeface)
	{
		_mainTypeface = typeface;
	}

	public void setColorPalette(ColorPalette colorPalette) 
	{
		_colorPalette = colorPalette;
	}

	public void setBackground(Background background) 
	{
		_background = background;
	}

	public void setAngleGenerator(AngleGenerator angleGenerator)
	{
		_angleGenerator = angleGenerator;
	}

	public Bitmap getBufferedImage() 
	{
		return _imageBitmap;
	}

	public void kill() 
	{
		_running  = false;
	}
}
