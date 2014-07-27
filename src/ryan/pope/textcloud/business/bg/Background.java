package ryan.pope.textcloud.business.bg;

import ryan.pope.textcloud.business.collide.Collidable;

/**
 * Created by kenny on 6/30/14.
 */
public interface Background {
    boolean isInBounds(Collidable collidable);
}
