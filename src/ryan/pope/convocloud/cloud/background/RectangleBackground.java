package ryan.pope.convocloud.cloud.background;

import ryan.pope.convocloud.cloud.collide.Collidable;
import ryan.pope.convocloud.cloud.collide.Vector2d;

public class RectangleBackground implements Background 
{

    private int _width;

    private int _height;

    public RectangleBackground(int width, int height) 
    {
        _width = width;
        _height = height;
    }

    @Override
    public boolean inBounds(Collidable collidable) 
    {
        Vector2d position = collidable.getPosition();
        return position.getX() >= 0 &&
                position.getX() + collidable.getWidth() < _width &&
                position.getY() >= 0 &&
                position.getY() + collidable.getHeight() < _height;
    }

}
