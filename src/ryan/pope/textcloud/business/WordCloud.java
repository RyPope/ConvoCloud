package ryan.pope.textcloud.business;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import ryan.pope.textcloud.business.bg.Background;
import ryan.pope.textcloud.business.bg.RectangleBackground;
import ryan.pope.textcloud.business.collide.RectanglePixelCollidable;
import ryan.pope.textcloud.business.collide.checkers.CollisionChecker;
import ryan.pope.textcloud.business.collide.checkers.RectangleCollisionChecker;
import ryan.pope.textcloud.business.collide.checkers.RectanglePixelCollisionChecker;
import ryan.pope.textcloud.business.collide.image.AngleGenerator;
import ryan.pope.textcloud.business.collide.image.CollisionRaster;
import ryan.pope.textcloud.business.collide.image.ImageRotation;
import ryan.pope.textcloud.business.font.CloudFont;
import ryan.pope.textcloud.business.font.FontWeight;
import ryan.pope.textcloud.business.font.scale.FontScalar;
import ryan.pope.textcloud.business.font.scale.LinearFontScalar;
import ryan.pope.textcloud.business.padding.Padder;
import ryan.pope.textcloud.business.padding.RectanglePadder;
import ryan.pope.textcloud.business.padding.WordPixelPadder;
import ryan.pope.textcloud.business.palette.ColorPalette;

public class WordCloud {
    protected static final Random RANDOM = new Random();

    protected final int width;

    protected final int height;

    protected final CollisionMode collisionMode;

    protected final CollisionChecker collisionChecker;

    protected final Padder padder;

    protected int padding = 0;

    protected Background background;

    protected final RectanglePixelCollidable backgroundCollidable;

    protected int backgroundColor = Color.WHITE;

    protected FontScalar fontScalar = new LinearFontScalar(10, 40);

    protected CloudFont cloudFont = new CloudFont("Comic Sans MS", FontWeight.BOLD);

    protected AngleGenerator angleGenerator = new AngleGenerator();

    protected final CollisionRaster collisionRaster;

    protected final Bitmap bufferedImage;

    protected final Set<Word> placedWords = new HashSet<Word>();

    protected final Set<Word> skipped = new HashSet<Word>();

    protected ColorPalette colorPalette = new ColorPalette(Color.RED, Color.WHITE, Color.YELLOW, Color.GRAY, Color.GREEN);

    public WordCloud(int width, int height, CollisionMode collisionMode) {
        this.width = width;
        this.height = height;
        this.collisionMode = collisionMode;
        switch(collisionMode) {
            case PIXEL_PERFECT:
                this.padder = new WordPixelPadder();
                this.collisionChecker = new RectanglePixelCollisionChecker();
                break;

            case RECTANGLE:
            default:
                this.padder = new RectanglePadder();
                this.collisionChecker = new RectangleCollisionChecker();
                break;
        }
        this.collisionRaster = new CollisionRaster(width, height);
        this.bufferedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.backgroundCollidable = new RectanglePixelCollidable(collisionRaster, 0, 0);
        this.background = new RectangleBackground(width, height);
    }

    public void build(List<WordFrequency> wordFrequencies) {
        Collections.sort(wordFrequencies);
        drawForgroundToBackground();
        for(final Word word : buildwords(wordFrequencies, this.colorPalette)) {
            final int startX = RANDOM.nextInt(Math.max(width - word.getWidth(), width));
            final int startY = RANDOM.nextInt(Math.max(height - word.getHeight(), height));
            place(word, startX, startY);

        }
    }

    public void writeToFile(final String outputFileName) {
        String extension = "";
        int i = outputFileName.lastIndexOf('.');
        if (i > 0) {
            extension = outputFileName.substring(i + 1);
        }
        
        FileOutputStream out = null;
        try {
               out = new FileOutputStream(outputFileName);
               bufferedImage.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) 
        {
            e.printStackTrace();
        } finally 
        {
               try
               {
                   out.close();
               } 
               catch(Throwable ignore) 
               {
            	   
               }
        }
    }

    protected void drawForgroundToBackground() 
    {
        final Bitmap backgroundBufferedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas graphics = new Canvas(backgroundBufferedImage);

        // draw current color
        Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(backgroundColor);
        graphics.drawRect(0, 0, width, height, fillPaint);

        // draw back to original
        final Canvas graphics2 = new Canvas(bufferedImage);
        graphics2.drawBitmap(backgroundBufferedImage, 0, 0, null);
    }

    /**
     * try to place in center, build out in a spiral trying to place words for N steps
     * @param word
     */
    protected void place(final Word word, final int startX, final int startY) {
        final Canvas graphics = new Canvas(this.bufferedImage);

        final int maxRadius = width;

        for(int r = 0; r < maxRadius; r += 2) {
            for(int x = -r; x <= r; x++) {
                if(startX + x < 0) { continue; }
                if(startX + x >= width) { continue; }

                boolean placed = false;
                word.setX(startX + x);

                // try positive root
                int y1 = (int) Math.sqrt(r * r - x * x);
                if(startY + y1 >= 0 && startY + y1 < height) {
                    word.setY(startY + y1);
                    placed = tryToPlace(word);
                }
                // try negative root
                int y2 = -y1;
                if(!placed && startY + y2 >= 0 && startY + y2 < height) {
                    word.setY(startY + y2);
                    placed = tryToPlace(word);
                }
                if(placed) 
                {
                    collisionRaster.mask(word.getCollisionRaster(), word.getX(), word.getY());
                    graphics.drawBitmap(word.getBufferedImage(), word.getX(), word.getY(), null);
//                    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//                    textPaint.setTextSize(20);
//                    textPaint.setColor(Color.BLACK);
//                    textPaint.setTextAlign(Align.LEFT);
//                	graphics.drawText(word.getWord(), word.getX(), word.getY(), textPaint);
                	return;
                }

            }
        }

        skipped.add(word);
    }

    private boolean tryToPlace(final Word word) {
        if(!background.isInBounds(word)) { return false; }

        switch(this.collisionMode) {
            case RECTANGLE:
                for(Word placeWord : this.placedWords) {
                    if(placeWord.collide(word)) {
                        return false;
                    }
                }
                placedWords.add(word);
                return true;

            case PIXEL_PERFECT:
                if(backgroundCollidable.collide(word)) { return false; }
                placedWords.add(word);
                return true;

        }
        return false;
    }

    protected List<Word> buildwords(final List<WordFrequency> wordFrequencies, final ColorPalette colorPalette) {
        final int maxFrequency = maxFrequency(wordFrequencies);

        final List<Word> words = new ArrayList<Word>();
        for(final WordFrequency wordFrequency : wordFrequencies) {
            words.add(buildWord(wordFrequency, maxFrequency, colorPalette));
        }
        return words;
    }

    private Word buildWord(final WordFrequency wordFrequency, int maxFrequency, final ColorPalette colorPalette) {
        final Canvas graphics = new Canvas(this.bufferedImage);

        final int frequency = wordFrequency.getFrequency();
        final float fontHeight = this.fontScalar.scale(frequency, 0, maxFrequency);
        final Word word = new Word(wordFrequency.getWord(), colorPalette.next(), (int) fontHeight, this.collisionChecker);

        final double theta = angleGenerator.randomNext();
        if(theta != 0) {
            word.setBufferedImage(ImageRotation.rotate(word.getBufferedImage(), theta));
        }
        if(padding > 0) {
            padder.pad(word, padding);
        }
        return word;
    }

    private int maxFrequency(final Collection<WordFrequency> wordFrequencies) {
        if(wordFrequencies.isEmpty()) { return 1; }
        
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

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setColorPalette(ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public void setFontScalar(FontScalar fontScalar) {
        this.fontScalar = fontScalar;
    }

    public void setCloudFont(CloudFont cloudFont) {
        this.cloudFont = cloudFont;
    }

    public void setAngleGenerator(AngleGenerator angleGenerator) {
        this.angleGenerator = angleGenerator;
    }

    public Bitmap getBufferedImage() {
        return bufferedImage;
    }

    public Set<Word> getSkipped() {
        return skipped;
    }
}
