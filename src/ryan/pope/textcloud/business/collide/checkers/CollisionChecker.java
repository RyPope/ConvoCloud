package ryan.pope.textcloud.business.collide.checkers;

import ryan.pope.textcloud.business.collide.Collidable;

public interface CollisionChecker 
{
    boolean collide(Collidable collidable, Collidable collidable2);
}
