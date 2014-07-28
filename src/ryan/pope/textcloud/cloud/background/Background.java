package ryan.pope.textcloud.cloud.background;

import ryan.pope.textcloud.cloud.collide.Collidable;

public interface Background 
{
    boolean inBounds(Collidable collidable);
}
