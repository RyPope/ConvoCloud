package ryan.pope.convocloud.cloud.objects;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import ryan.pope.convocloud.application.Globals;
import ryan.pope.convocloud.cloud.background.Background;
import ryan.pope.convocloud.cloud.background.RectangleBackground;
import ryan.pope.convocloud.cloud.collide.RectanglePixelCollidable;
import ryan.pope.convocloud.cloud.collide.checkers.CollisionChecker;
import ryan.pope.convocloud.cloud.collide.checkers.RectangleCollisionChecker;
import ryan.pope.convocloud.cloud.collide.checkers.RectanglePixelCollisionChecker;
import ryan.pope.convocloud.cloud.collide.image.AngleGenerator;
import ryan.pope.convocloud.cloud.collide.image.CollisionRaster;
import ryan.pope.convocloud.cloud.collide.image.ImageRotation;
import ryan.pope.convocloud.cloud.font.scale.FontScalar;
import ryan.pope.convocloud.cloud.font.scale.LinearFontScalar;
import ryan.pope.convocloud.cloud.padding.Padder;
import ryan.pope.convocloud.cloud.padding.RectanglePadder;
import ryan.pope.convocloud.cloud.padding.WordPixelPadder;
import ryan.pope.convocloud.cloud.palette.ColorPalette;
import ryan.pope.convocloud.cloud.font.CloudFont;
import ryan.pope.convocloud.cloud.font.FontWeight;
import ryan.pope.convocloud.presentation.ProgressDialogHelper;

public class WordCloud 
{
	protected static final Random RANDOM = new Random();
	protected int _width;
	protected int _height;
	protected CollisionMode _collisionMode;
	protected CollisionChecker _collisionChecker;
	protected Padder _padder;
	protected int _padding = 0;
	protected Background _background;
	protected RectanglePixelCollidable _backgroundCollidable;
	protected int _backgroundColor = Color.WHITE;
	protected FontScalar _fontScalar = new LinearFontScalar(10, 40);
	protected CloudFont _cloudFont = new CloudFont("Comic Sans MS", FontWeight.BOLD);
	protected AngleGenerator _angleGenerator = new AngleGenerator();
	protected CollisionRaster _collisionRaster;
	protected Bitmap _imageBitmap;
	protected Set<Word> _placedWords = new HashSet<Word>();
	protected Typeface _mainTypeface;
	protected ColorPalette _colorPalette = new ColorPalette(Color.RED, Color.BLACK, Color.YELLOW, Color.GRAY, Color.GREEN);
	private ProgressDialogHelper _progressHelper;
	private boolean _running = true;
	private Canvas _bitmapCanvas;

	public WordCloud(ProgressDialogHelper progressHelper, int width, int height, CollisionMode collisionMode) 
	{
		_width = width;
		_height = height;
		_collisionMode = collisionMode;
		_progressHelper = progressHelper;
		switch(collisionMode) 
		{
		case PIXEL_PERFECT:
			_padder = new WordPixelPadder();
			_collisionChecker = new RectanglePixelCollisionChecker();
			break;

		case RECTANGLE:
		default:
			_padder = new RectanglePadder();
			_collisionChecker = new RectangleCollisionChecker();
			break;
		}
		_collisionRaster = new CollisionRaster(width, height);
		_imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		_bitmapCanvas = new Canvas(_imageBitmap);
		_backgroundCollidable = new RectanglePixelCollidable(_collisionRaster, 0, 0);
		_background = new RectangleBackground(width, height);
	}

	public void build(ArrayList<WordFrequency> wordFrequencies) 
	{
		Collections.sort(wordFrequencies);
		drawBackgroundColor();
		insertWatermark();

		/* Starter width */
		int fontWidthPixel = _imageBitmap.getWidth() - 10;
		int minimumFontPixelSize = fontWidthPixel / 15;
		int i = 1;
		
		if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Minimum font size: " + minimumFontPixelSize);

		MaximalRectangle maxRect = new MaximalRectangle();
		for (WordFrequency wordFreq : wordFrequencies)
		{

			if(!_running)
				break;

			Word word = new Word(wordFreq.getWord(), _colorPalette.next(), fontWidthPixel, _mainTypeface, _collisionChecker);

			double theta = _angleGenerator.randomNext();
			if(theta != 0) 
			{
				word.setBufferedImage(ImageRotation.rotate(word.getImageBitmap(), theta));
			}

			_progressHelper.changeCloudDialogMessage("Placing " + i + " of " + wordFrequencies.size());
			if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Placing " + i + " of " + wordFrequencies.size());
			while(_running)
			{
				if(fontWidthPixel < minimumFontPixelSize)
				{
					break;
				}
				if(testPlace(word))
				//if(place(word, _width / 2, _height / 2))
				{
					int max = maxRect.maximalRectangle(_imageBitmap);
					if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "Max Rect: " + max); 
					i++;
					break;
				}
				else
				{
					fontWidthPixel *= .5;
					if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Lowering text size to: " + fontWidthPixel);
					word.setTextPixelSize(fontWidthPixel);
				}
			}
			
			if(fontWidthPixel < minimumFontPixelSize)
			{
				break;
			}
		}
	}

	private void insertWatermark() 
	{
		Word watermark = new Word("#ConvoCloud", Color.BLACK,_width / 2, _mainTypeface, _collisionChecker);
		_collisionRaster.mask(watermark.getCollisionRaster(), _width - watermark.getWidth(), _height - watermark.getHeight() - 2);
		_bitmapCanvas.drawBitmap(watermark.getImageBitmap(), _width - watermark.getWidth(), _height - watermark.getHeight() - 2, null);

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

	protected boolean testPlace(final Word word) 
	{
		for(int y = 0; y < _height - word.getHeight() && _running; y += 4) 
		{
			for(int x = 0; x < _width - word.getWidth() && _running; x += 2) 
			{
				word.setX(x);
				word.setY(y);
				//if(Globals.DEBUG)Log.i(Globals.DEBUG_TAG, "x:" + x + ",y:" + y);
				if(tryToPlace(word)) 
				{
					_collisionRaster.mask(word.getCollisionRaster(), word.getX(), word.getY());
					_bitmapCanvas.drawBitmap(word.getImageBitmap(), word.getX(), word.getY(), null);
					return true;
				}

			}
		}

		return false;
	}

	protected boolean place(final Word word, final int startX, final int startY) 
	{

		int maxRadius = _width;

        for(int r = 0; r < maxRadius; r += 2) 
        {
            for(int x = -r; x <= r; x++) {
                if(startX + x < 0) { continue; }
                if(startX + x >= _width) { continue; }

				boolean placed = false;
				word.setX(startX + x);

				int y1 = (int) Math.sqrt(r * r - x * x);
				if(startY + y1 >= 0 && startY + y1 < _height)
				{
					word.setY(startY + y1);
					placed = tryToPlace(word);
				}

				int y2 = -y1;
				if(!placed && startY + y2 >= 0 && startY + y2 < _height) 
				{
					word.setY(startY + y2);
					placed = tryToPlace(word);
				}
				if(placed) 
				{
					_collisionRaster.mask(word.getCollisionRaster(), word.getX(), word.getY());
					_bitmapCanvas.drawBitmap(word.getImageBitmap(), word.getX(), word.getY(), null);
					return true;
				}

			}
		}

		return false;
	}

	private boolean tryToPlace(Word word) 
	{
		if(!_background.inBounds(word)) 
		{ 
			return false; 
		}

		switch(this._collisionMode) 
		{
		case RECTANGLE:
			for(Word placeWord : _placedWords) 
			{
				if(placeWord.collide(word)) 
				{
					return false;
				}
			}
			_placedWords.add(word);
			return true;

		case PIXEL_PERFECT:
			if(_backgroundCollidable.collide(word)) 
			{ 
				return false; 
			}
			_placedWords.add(word);
			return true;

		}
		return false;
	}

	public void setBackgroundColor(int backgroundColor) 
	{
		_backgroundColor = backgroundColor;
	}

	public void setTypeface(Typeface typeface)
	{
		_mainTypeface = typeface;
	}

	public void setPadding(int padding) 
	{
		_padding = padding;
	}

	public void setColorPalette(ColorPalette colorPalette) 
	{
		_colorPalette = colorPalette;
	}

	public void setBackground(Background background) 
	{
		_background = background;
	}

	public void setFontScalar(FontScalar fontScalar) 
	{
		_fontScalar = fontScalar;
	}

	public void setCloudFont(CloudFont cloudFont) 
	{
		_cloudFont = cloudFont;
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
