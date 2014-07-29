package ryan.pope.convocloud.cloud.padding;

import java.util.HashSet;
import java.util.Set;

import ryan.pope.convocloud.cloud.collide.Vector2d;
import ryan.pope.convocloud.cloud.collide.image.CollisionRaster;
import ryan.pope.convocloud.cloud.objects.Word;
import android.graphics.Color;

public class WordPixelPadder implements Padder 
{
    private static final int PAD_COLOR = Color.BLACK;

    private RectanglePadder _rectanglePadder = new RectanglePadder();

    public void pad(Word word, int padding) 
    {
        if(padding <= 0) 
        { 
        	return; 
        }
        _rectanglePadder.pad(word, padding);

        CollisionRaster collisionRaster = word.getCollisionRaster();

        Set<Vector2d> toPad = new HashSet<Vector2d>();
        int width = collisionRaster.getWidth();
        int height = collisionRaster.getHeight();

        for(int y = 0; y < height; y++) 
        {
            for(int x = 0; x < width; x++)
            {
                if(shouldPad(collisionRaster, x, y, padding)) 
                {
                    toPad.add(new Vector2d(x, y));
                }
            }
        }
        for(Vector2d padPoint : toPad) 
        {
            collisionRaster.setRGB(padPoint.getX(), padPoint.getY(), PAD_COLOR);
        }
    }

    private boolean shouldPad(CollisionRaster collisionRaster, int cx, int cy, int padding) 
    {
        if(!collisionRaster.isTransparent(cx, cy)) 
        { 
        	return false; 
        }

        for(int y = cy - padding; y <= cy + padding; y++) 
        {
            for(int x = cx - padding; x <= cx + padding; x++) 
            {
                if(x == cx && y == cy) 
                { 
                	continue; 
                }
                if(inBounds(collisionRaster, x, y))
                {
                    if(!collisionRaster.isTransparent(x, y)) 
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean inBounds(CollisionRaster collisionRaster, int x, int y)
    {
        return x >= 0
                && y >= 0
                && x < collisionRaster.getWidth()
                && y < collisionRaster.getHeight();
    }

}
