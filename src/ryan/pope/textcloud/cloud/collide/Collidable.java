package ryan.pope.textcloud.cloud.collide;

import ryan.pope.textcloud.cloud.collide.image.CollisionRaster;

public interface Collidable {
    boolean collide(Collidable collidable);
    Vector2d getPosition();
    int getWidth();
    int getHeight();
    CollisionRaster getCollisionRaster();
}
