package ryan.pope.textcloud.cloud.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import ryan.pope.textcloud.cloud.collide.Collidable;
import ryan.pope.textcloud.cloud.collide.Vector2d;
import ryan.pope.textcloud.cloud.collide.checkers.CollisionChecker;
import ryan.pope.textcloud.cloud.collide.image.CollisionRaster;

public class Word implements Collidable
{

    private final CollisionChecker collisionChecker;

    private final String word;

    private final int color;

    private Vector2d position = new Vector2d(0, 0);
    
    private Bitmap.Config conf = Bitmap.Config.ARGB_8888;

    private Bitmap bufferedImage;

    private CollisionRaster collisionRaster;

    public Word(String word, int color, int fontHeight, CollisionChecker collisionChecker) {
        this.word = word;
        this.color = color;
        this.collisionChecker = collisionChecker;
        
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

        textPaint.setColor(color);

        setTextSizeForHeight(textPaint, fontHeight, word);
        textPaint.setTextAlign(Align.LEFT);
        
        Rect bounds = new Rect();
        textPaint.getTextBounds(word, 0, word.length(), bounds);
        int width = bounds.width();
        
        this.bufferedImage = Bitmap.createBitmap(width, fontHeight, conf);
       
        Canvas canvas = new Canvas(this.bufferedImage);

        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawText(word, 0, fontHeight, textPaint);

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
    
    private static void setTextSizeForHeight(Paint paint, float desiredHeight, String text) {

        final float testTextSize = 48f;

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        float desiredTextSize = testTextSize * desiredHeight / bounds.height();

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
