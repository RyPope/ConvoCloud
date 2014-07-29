package ryan.pope.convocloud.cloud.collide;

import ryan.pope.convocloud.cloud.collide.image.CollisionRaster;

public interface Collidable 
{
    boolean collide(Collidable collidable);
    Vector2d getPosition();
    int getWidth();
    int getHeight();
    CollisionRaster getCollisionRaster();
}
