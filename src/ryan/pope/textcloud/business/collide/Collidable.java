package ryan.pope.textcloud.business.collide;

import ryan.pope.textcloud.business.collide.image.CollisionRaster;

public interface Collidable {
    boolean collide(Collidable collidable);
    Vector2d getPosition();
    int getWidth();
    int getHeight();
    CollisionRaster getCollisionRaster();
}
