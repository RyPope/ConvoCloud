package ryan.pope.convocloud.cloud.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import ryan.pope.convocloud.cloud.collide.Collidable;
import ryan.pope.convocloud.cloud.collide.Vector2d;
import ryan.pope.convocloud.cloud.collide.checkers.CollisionChecker;
import ryan.pope.convocloud.cloud.collide.image.CollisionRaster;

public class Word implements Collidable
{

    private CollisionChecker _collisionChecker;
    private String _word;
    private Vector2d _textPosition = new Vector2d(0, 0);
    private Bitmap.Config _conf = Bitmap.Config.ARGB_8888;
    private Bitmap _imageBitmap;
    private CollisionRaster _collisionRaster;

    public Word(String word, int color, int fontHeight, Typeface typeface, CollisionChecker collisionChecker) 
    {
        _word = word;
        _collisionChecker = collisionChecker;
        
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

        textPaint.setColor(color);
        
        if(typeface != null)
        	textPaint.setTypeface(typeface);

        setTextSizeForHeight(textPaint, fontHeight, word);
        textPaint.setTextAlign(Align.LEFT);
        
        Rect bounds = new Rect();
        textPaint.getTextBounds(word, 0, word.length(), bounds);
        int width = bounds.width() + 2;
        
        _imageBitmap = Bitmap.createBitmap(width, fontHeight, _conf);
       
        Canvas canvas = new Canvas(_imageBitmap);

        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawText(word, 0, fontHeight, textPaint);

        _collisionRaster = new CollisionRaster(_imageBitmap);
    }
    
    private static void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {

        final float testTextSize = 48f;

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        paint.setTextSize(desiredTextSize);
    }
    
    private static void setTextSizeForHeight(Paint paint, float desiredHeight, String text) {

        final float testTextSize = 48f;

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        float desiredTextSize = testTextSize * desiredHeight / bounds.height();

        paint.setTextSize(desiredTextSize);
    }

    public Bitmap getBufferedImage() 
    {
        return _imageBitmap;
    }

    public void setBufferedImage(Bitmap imageBitmap) 
    {
        _imageBitmap = imageBitmap;
        _collisionRaster = new CollisionRaster(imageBitmap);
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

    @Override
    public CollisionRaster getCollisionRaster() 
    {
        return _collisionRaster;
    }

    @Override
    public boolean collide(Collidable collidable) 
    {
        return _collisionChecker.collide(this, collidable);
    }

    public void draw(CollisionRaster collisionRaster) 
    {
        collisionRaster.mask(collisionRaster, _textPosition.getX(), _textPosition.getY());
    }
}
