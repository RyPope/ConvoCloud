package ryan.pope.convocloud.cloud.objects;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
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
    protected Set<Word> _skippedWords = new HashSet<Word>();
    protected Typeface _mainTypeface;
    protected ColorPalette _colorPalette = new ColorPalette(Color.RED, Color.BLACK, Color.YELLOW, Color.GRAY, Color.GREEN);
    
    public WordCloud(int width, int height, CollisionMode collisionMode) 
    {
        _width = width;
        _height = height;
        _collisionMode = collisionMode;
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
        _backgroundCollidable = new RectanglePixelCollidable(_collisionRaster, 0, 0);
        _background = new RectangleBackground(width, height);
    }

    public void build(ArrayList<WordFrequency> wordFrequencies) 
    {
        Collections.sort(wordFrequencies);
        drawForegroundToBackground();
        insertWatermark();
        int i = 1;
        for(Word word : buildwords(wordFrequencies, _colorPalette)) 
        {
        	if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Placing " + i + " of " + wordFrequencies.size());
            int startX = RANDOM.nextInt(Math.max(_width - word.getWidth(), _width));
            int startY = RANDOM.nextInt(Math.max(_height - word.getHeight(), _height));
            place(word, startX, startY);
            i++;
        }
    }

	private void insertWatermark() 
    {
		Word watermark = new Word("#TextCloud", Color.BLACK,_imageBitmap.getHeight()/20, _mainTypeface, _collisionChecker);
		place(watermark, _imageBitmap.getWidth() - 120, _imageBitmap.getHeight() - 50);
		
	}

	public void writeToFile(final String outputFileName) 
    {
        
        FileOutputStream out = null;
        try 
        {
               out = new FileOutputStream(outputFileName);
               _imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
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

    protected void drawForegroundToBackground() 
    {
        Bitmap backgroundBufferedImage = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888);
        Canvas graphics = new Canvas(backgroundBufferedImage);

        Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(_backgroundColor);
        graphics.drawRect(0, 0, _width, _height, fillPaint);

        Canvas graphics2 = new Canvas(_imageBitmap);
        graphics2.drawBitmap(backgroundBufferedImage, 0, 0, null);
    }

    protected void place(final Word word, final int startX, final int startY) 
    {
        Canvas graphics = new Canvas(_imageBitmap);

        int maxRadius = _width;

        for(int r = 0; r < maxRadius; r += 2) 
        {
            for(int x = -r; x <= r; x++) 
            {
                if(startX + x < 0) 
                { 
                	continue; 
                }
                if(startX + x >= _width) 
                { 
                	continue; 
                }

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
                    graphics.drawBitmap(word.getBufferedImage(), word.getX(), word.getY(), null);
                	return;
                }

            }
        }

        _skippedWords.add(word);
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
                for(Word placeWord : this._placedWords) 
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

    protected ArrayList<Word> buildwords(ArrayList<WordFrequency> wordFrequencies, ColorPalette colorPalette) 
    {
        int maxFrequency = maxFrequency(wordFrequencies);

        ArrayList<Word> words = new ArrayList<Word>();
        for(WordFrequency wordFrequency : wordFrequencies) 
        {
            words.add(buildWord(wordFrequency, maxFrequency, colorPalette));
        }
        return words;
    }

    private Word buildWord(WordFrequency wordFrequency, int maxFrequency, ColorPalette colorPalette) 
    {

        int frequency = wordFrequency.getFrequency();
        float fontHeight = _fontScalar.scale(frequency, 0, maxFrequency);
        Word word = new Word(wordFrequency.getWord(), colorPalette.next(), (int) fontHeight, _mainTypeface, _collisionChecker);

        double theta = _angleGenerator.randomNext();
        if(theta != 0) 
        {
            word.setBufferedImage(ImageRotation.rotate(word.getBufferedImage(), theta));
        }
        if(_padding > 0) 
        {
            _padder.pad(word, _padding);
        }
        return word;
    }

    private int maxFrequency(Collection<WordFrequency> wordFrequencies) 
    {
        if(wordFrequencies.isEmpty()) 
        { 
        	return 1; 
        }
        
        int max = 1;
        for(WordFrequency f : wordFrequencies)
        {
        	if (f.getFrequency() > max)
        	{
        		max = f.getFrequency();
        	}
        }
        
        return max;
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

    public Set<Word> getSkipped() 
    {
        return _skippedWords;
    }
}
