package ryan.pope.convocloud.cloud.collide;

import ryan.pope.convocloud.cloud.collide.checkers.RectanglePixelCollisionChecker;
import ryan.pope.convocloud.cloud.collide.image.CollisionRaster;

public class RectanglePixelCollidable implements Collidable 
{

    private static RectanglePixelCollisionChecker RECTANGLE_PIXEL_COLLISION_CHECKER = new RectanglePixelCollisionChecker();

    private Vector2d _position;

    private CollisionRaster _collisionRaster;

    public RectanglePixelCollidable(CollisionRaster collisionRaster, int x, int y) 
    {
        _collisionRaster = collisionRaster;
        _position = new Vector2d(x, y);
    }

    public int getX() 
    {
        return _position.getX();
    }

    public int getY() 
    {
        return _position.getY();
    }

    @Override
    public boolean collide(Collidable collidable) 
    {
        return RECTANGLE_PIXEL_COLLISION_CHECKER.collide(this, collidable);
    }

    @Override
    public Vector2d getPosition() 
    {
        return _position;
    }

    @Override
    public int getWidth() 
    {
        return _collisionRaster.getWidth();
    }

    @Override
    public int getHeight() 
    {
        return _collisionRaster.getHeight();
    }

    @Override
    public CollisionRaster getCollisionRaster() 
    {
        return _collisionRaster;
    }

}
