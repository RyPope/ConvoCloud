package ryan.pope.convocloud.cloud.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import ryan.pope.convocloud.cloud.tools.ImageRotation;

public class Word
{
    private WordPos _pos = new WordPos(0, 0);
    private Bitmap.Config _conf = Bitmap.Config.ARGB_8888;
    private Bitmap _imageBitmap;
    private Paint _textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

    public Word(String word, int color, Rect rect, Typeface typeface, double theta) 
    {
        _pos.setX(rect.left);
        _pos.setY(rect.top);

        _textPaint.setColor(color);
        
        if(typeface != null)
        	_textPaint.setTypeface(typeface);

        _textPaint.setTextAlign(Align.LEFT);
        
        adjustTextSize(word, _textPaint, rect, theta);

        Rect bounds = new Rect();
        _textPaint.getTextBounds(word, 0, word.length(), bounds);
        int width = bounds.width();
        int height = bounds.height();
        
        _imageBitmap = Bitmap.createBitmap(width, height, _conf);
       
        Canvas canvas = new Canvas(_imageBitmap);

        canvas.drawText(word, 0, height, _textPaint);

        if(theta == 0)
        {
	        _pos.setX(rect.left + (rect.width() - width));
	        _pos.setY(rect.top + (rect.height() - height));
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
	    	/* If the word is vertical, get the text size that would fit the
	    	 * bounds for the width and the height and use the one that is smaller
	    	 * to ensure the word does not go outside of it's bounds.
	    	 */
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
	    	/* If the word is vertical, get the bounds and set the 
	    	 * size planned for the rotation.
	    	 */
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

    public int getX() 
    {
        return _pos.getX();
    }

    public void setX(int x) 
    {
        _pos.setX(x);
    }

    public int getY() 
    {
        return _pos.getY();
    }

    public void setY(int y) 
    {
        _pos.setY(y);
    }
}
