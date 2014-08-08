package ryan.pope.convocloud.cloud.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import ryan.pope.convocloud.cloud.collide.Vector2d;
import ryan.pope.convocloud.cloud.collide.image.ImageRotation;

public class Word
{
    private String _word;
    private Vector2d _textPosition = new Vector2d(0, 0);
    private Bitmap.Config _conf = Bitmap.Config.ARGB_8888;
    private Bitmap _imageBitmap;
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

    public Word(String word, int color, Rect rect, Typeface typeface, double theta) 
    {
        _word = word;
        _textPosition.setX(rect.left);
        _textPosition.setY(rect.top);

        textPaint.setColor(color);
        
        if(typeface != null)
        	textPaint.setTypeface(typeface);

        textPaint.setTextAlign(Align.LEFT);
        
        adjustTextSize(word, textPaint, rect, theta);

        Rect bounds = new Rect();
        textPaint.getTextBounds(word, 0, word.length(), bounds);
        int width = bounds.width();
        int height = bounds.height();
        
        _imageBitmap = Bitmap.createBitmap(width, height, _conf);
       
        Canvas canvas = new Canvas(_imageBitmap);

        canvas.drawText(word, 0, height, textPaint);

        if(theta == 0)
        {
	        _textPosition.setX(rect.left + (rect.width() - width));
	        _textPosition.setY(rect.top + (rect.height() - height));
        }
        else
        {
        	_imageBitmap = ImageRotation.rotate(_imageBitmap, theta);
        }

    }
	
	private void adjustTextSize(String word, Paint textPaint, Rect rect, double theta) 
	{
		textPaint.setTextSize(100);
		textPaint.setTextScaleX(1.0f);
	    Rect bounds = new Rect();
	    textPaint.getTextBounds(word, 0, word.length(), bounds);

	    if(theta == 0)
	    {
		    int h = bounds.height();
	
		    float targetHeight = (float)rect.height()*1f;
		 
		    float heightSize  = ((targetHeight/h)*100f);
		    
		    int w = bounds.width();
	
		    float targetWidth = (float)rect.width()*1f;
		 
		    float widthSize  = ((targetWidth/w)*100f);
	
		    textPaint.setTextSize(Math.min(heightSize, widthSize));
	    }
	    else
	    {
		    int h = bounds.height();
			
		    float targetHeight = (float)rect.width()*1f;
		 
		    float heightSize  = ((targetHeight/h)*100f);
		    
		    int w = bounds.width();
	
		    float targetWidth = (float)rect.height()*1f;
		 
		    float widthSize  = ((targetWidth/w)*100f);
	
		    textPaint.setTextSize(Math.min(heightSize, widthSize));
	    }
	}

    public Bitmap getImageBitmap() 
    {
        return _imageBitmap;
    }

    public void setBufferedImage(Bitmap imageBitmap) 
    {
        _imageBitmap = imageBitmap;
    }

    public String getWord() 
    {
        return _word;
    }

    public Vector2d getPosition() 
    {
        return _textPosition;
    }

    public int getX() 
    {
        return _textPosition.getX();
    }

    public void setX(int x) 
    {
        _textPosition.setX(x);
    }

    public int getY() 
    {
        return _textPosition.getY();
    }

    public void setY(int y) 
    {
        _textPosition.setY(y);
    }

    public int getWidth() 
    {
        return _imageBitmap.getWidth();
    }

    public int getHeight() 
    {
        return _imageBitmap.getHeight();
    }
}
