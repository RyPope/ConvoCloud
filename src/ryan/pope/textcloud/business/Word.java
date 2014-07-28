package ryan.pope.textcloud.business;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import ryan.pope.textcloud.business.collide.Collidable;
import ryan.pope.textcloud.business.collide.Vector2d;
import ryan.pope.textcloud.business.collide.checkers.CollisionChecker;
import ryan.pope.textcloud.business.collide.image.CollisionRaster;

public class Word implements Collidable
{

    private final CollisionChecker collisionChecker;

    private final String word;

    private final int color;

    private Vector2d position = new Vector2d(0, 0);
    
    private Bitmap.Config conf = Bitmap.Config.ARGB_8888;

    private Bitmap bufferedImage;

    private CollisionRaster collisionRaster;

    public Word(String word, int color, FontMetrics fontMetrics, CollisionChecker collisionChecker) {
        this.word = word;
        this.color = color;
        this.collisionChecker = collisionChecker;
        final float maxDescent = fontMetrics.descent;
        final float maxAscent = fontMetrics.ascent;
        
        Rect bounds = new Rect();
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.getTextBounds(word,0,word.length(),bounds);
        final int width = bounds.width();
        final int height = bounds.height();

        this.bufferedImage = Bitmap.createBitmap(width, height, conf);
       
        Canvas canvas = new Canvas(this.bufferedImage);
        
        textPaint.setColor(color);
        //textPaint.setTextSize(10);
        setTextSizeForWidth(textPaint, width, word);
        textPaint.setTextAlign(Align.LEFT);

        canvas.drawText(word, 0, 0, textPaint);

        this.collisionRaster = new CollisionRaster(this.bufferedImage);
    }
    
    private static void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {

        final float testTextSize = 48f;

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        paint.setTextSize(desiredTextSize);
    }

    public Bitmap getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(Bitmap bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.collisionRaster = new CollisionRaster(bufferedImage);
    }

    public String getWord() {
        return word;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getX() {
        return position.getX();
    }

    public void setX(int x) {
        position.setX(x);
    }

    public int getY() {
        return position.getY();
    }

    public void setY(int y) {
        position.setY(y);
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public CollisionRaster getCollisionRaster() {
        return collisionRaster;
    }

    @Override
    public boolean collide(Collidable collidable) {
        return collisionChecker.collide(this, collidable);
    }

    public void draw(CollisionRaster collisionRaster) {
        collisionRaster.mask(collisionRaster, position.getX(), position.getY());
    }

    @Override
    public String toString() {
        return "WordRectangle{" +
                "word='" + word + '\'' +
                ", color=" + color +
                ", x=" + getX() +
                ", y=" + getY() +
                ", width=" + bufferedImage.getWidth() +
                ", height=" + bufferedImage.getHeight() +
                '}';
    }

}
