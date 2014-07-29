package ryan.pope.convocloud.cloud.background;

import ryan.pope.convocloud.cloud.collide.Collidable;
import ryan.pope.convocloud.cloud.collide.Vector2d;

public class CircleBackground implements Background 
{

    private final int _radius;

    public CircleBackground(int radius)
    {
        _radius = radius;
    }

    @Override
    public boolean inBounds(Collidable collidable) 
    {
        Vector2d position = collidable.getPosition();
        return inCircle(position.getX(), position.getY()) &&
                inCircle(position.getX() + collidable.getWidth(), position.getY()) &&
                inCircle(position.getX(), position.getY() + collidable.getHeight()) &&
                inCircle(position.getX() + collidable.getWidth(), position.getY() + collidable.getHeight());
    }

    private boolean inCircle(int x, int y) 
    {
        x -= _radius ;
        y -= _radius;
        return  x * x + y * y <= _radius * _radius;
    }

}
