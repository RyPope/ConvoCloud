package ryan.pope.textcloud.business.collide;

import ryan.pope.textcloud.business.collide.checkers.RectanglePixelCollisionChecker;
import ryan.pope.textcloud.business.collide.image.CollisionRaster;

public class RectanglePixelCollidable implements Collidable {

    private static final RectanglePixelCollisionChecker RECTANGLE_PIXEL_COLLISION_CHECKER = new RectanglePixelCollisionChecker();

    private final Vector2d position;

    private final CollisionRaster collisionRaster;

    public RectanglePixelCollidable(CollisionRaster collisionRaster, int x, int y) {
        this.collisionRaster = collisionRaster;
        this.position = new Vector2d(x, y);
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    @Override
    public boolean collide(Collidable collidable) {
        return RECTANGLE_PIXEL_COLLISION_CHECKER.collide(this, collidable);
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public int getWidth() {
        return collisionRaster.getWidth();
    }

    @Override
    public int getHeight() {
        return collisionRaster.getHeight();
    }

    @Override
    public CollisionRaster getCollisionRaster() {
        return collisionRaster;
    }

}
