package ryan.pope.textcloud.cloud.collide.checkers;

import ryan.pope.textcloud.cloud.collide.Collidable;

public interface CollisionChecker 
{
    boolean collide(Collidable collidable, Collidable collidable2);
}
