package ryan.pope.textcloud.cloud.background;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ryan.pope.textcloud.cloud.collide.Collidable;
import ryan.pope.textcloud.cloud.collide.Vector2d;
import ryan.pope.textcloud.cloud.collide.image.CollisionRaster;

public class PixelBoundryBackground implements Background 
{

    private CollisionRaster _collisionRaster;

    private RectangleBackground _rectangleBackground;

    public PixelBoundryBackground(InputStream imageInputStream) throws IOException 
    {
        Bitmap bufferedImage = BitmapFactory.decodeStream(imageInputStream);
        _collisionRaster = new CollisionRaster(bufferedImage);
        _rectangleBackground = new RectangleBackground(bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @Override
    public boolean inBounds(Collidable collidable) 
    {
        // check if bounding boxes intersect
        if(!_rectangleBackground.inBounds(collidable)) 
        {
            return false;
        }
        
        Vector2d position = collidable.getPosition();

        int startX = Math.max(position.getX(), 0);
        int endX = Math.min(position.getX() + collidable.getWidth(), _collisionRaster.getWidth());

        int startY = Math.max(position.getY(), 0);
        int endY = Math.min(position.getY() + collidable.getHeight(), _collisionRaster.getHeight());

        for(int y = startY ; y < endY ; y++) 
        {
            for(int x = startX ; x < endX ; x++) 
            {
                // compute offsets for surface
                if(_collisionRaster.isTransparent(x - 0, y - 0) &&
                        !collidable.getCollisionRaster().isTransparent(x - position.getX(), y - position.getY())) {
                    return false;
                }
            }
        }
        return true;
    }

}
