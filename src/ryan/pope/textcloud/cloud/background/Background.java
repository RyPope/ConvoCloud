package ryan.pope.textcloud.cloud.background;

import ryan.pope.textcloud.cloud.collide.Collidable;

/**
 * Created by kenny on 6/30/14.
 */
public interface Background {
    boolean isInBounds(Collidable collidable);
}
