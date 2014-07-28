package ryan.pope.textcloud.cloud.collide.checkers;

import ryan.pope.textcloud.cloud.collide.Collidable;
import ryan.pope.textcloud.cloud.collide.Vector2d;
import ryan.pope.textcloud.cloud.collide.image.CollisionRaster;

public class RectanglePixelCollisionChecker implements CollisionChecker 
{

    private static final RectangleCollisionChecker RECTANGLE_COLLISION_CHECKER = new RectangleCollisionChecker();

    @Override
    public boolean collide(Collidable collidable, Collidable collidable2) 
    {
	    // check if bounding boxes intersect
        if(!RECTANGLE_COLLISION_CHECKER.collide(collidable, collidable2)) 
        {
            return false;
        }

        Vector2d position = collidable.getPosition();
        Vector2d position2 = collidable2.getPosition();
        CollisionRaster collisionRaster = collidable.getCollisionRaster();
        CollisionRaster collisionRaster2 = collidable2.getCollisionRaster();

        // get the overlapping box
        int startX = Math.max(position.getX(), position2.getX());
        int endX = Math.min(position.getX() + collidable.getWidth(), position2.getX() + collidable2.getWidth());

        int startY = Math.max(position.getY(), position2.getY());
        int endY = Math.min(position.getY() + collidable.getHeight(), position2.getY() + collidable2.getHeight());

        for(int y = startY ; y < endY ; y++) 
        {
            for(int x = startX ; x < endX ; x++) 
            {
                // compute offsets for surface
                if((!collisionRaster2.isTransparent(x - position2.getX(), y - position2.getY()))
                        && (!collisionRaster.isTransparent(x - position.getX(), y - position.getY()))) 
                {
                    return true;
                }
            }
        }
        return false;
    }

}
