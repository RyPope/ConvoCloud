package ryan.pope.convocloud.cloud.collide.image;

import android.graphics.Bitmap;

public class CollisionRaster 
{

    private int[][] _data;

    private int _width;

    private int _height;

    public CollisionRaster(final Bitmap bufferedImage) 
    {
        _width = bufferedImage.getWidth();
        _height = bufferedImage.getHeight();
        _data = new int[_width][_height];
        for(int y = 0; y < _height; y++) 
        {
            for(int x = 0; x < _width; x++) 
            {
                _data[x][y] = bufferedImage.getPixel(x, y);
            }
        }
    }

    public CollisionRaster(int width, int height) 
    {
        _width = width;
        _height = height;
        _data = new int[width][height];
    }

    public int getRGB(int x, int y) 
    {
        return _data[x][y];
    }

    public void setRGB(int x, int y, int rgb) 
    {
        _data[x][y] = rgb;
    }

    public void mask(final CollisionRaster collisionRaster, int x, int y) 
    {
        int maxHeight = Math.min(y + collisionRaster.getHeight(), _height);
        int maxWidth = Math.min(x + collisionRaster.getWidth(), _width);
        for(int offY = y, offY2 = 0; offY < maxHeight; offY++, offY2++) 
        {
            for(int offX = x, offX2 = 0; offX < maxWidth; offX++, offX2++) 
            {
                if(!collisionRaster.isTransparent(offX2, offY2)) 
                {
                    _data[offX][offY] = collisionRaster.getRGB(offX2, offY2);
                }
            }
        }
    }

    public boolean isTransparent(int x, int y) 
    {
        return (_data[x][y] & 0xFF000000) == 0x00000000;
    }

    public int getWidth() 
    {
        return _width;
    }

    public int getHeight() 
    {
        return _height;
    }

}
