package ryan.pope.convocloud.cloud.background;

import ryan.pope.convocloud.cloud.collide.Collidable;

public interface Background 
{
    boolean inBounds(Collidable collidable);
}
