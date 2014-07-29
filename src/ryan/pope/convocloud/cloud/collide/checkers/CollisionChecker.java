package ryan.pope.convocloud.cloud.collide.checkers;

import ryan.pope.convocloud.cloud.collide.Collidable;

public interface CollisionChecker 
{
    boolean collide(Collidable collidable, Collidable collidable2);
}
