package ryan.pope.textcloud.cloud.collide.image;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by kenny on 6/29/14.
 */
public class ImageRotation {

    private ImageRotation() {}

    public static Bitmap rotate90(final Bitmap bufferedImage) {
        return rotate(bufferedImage, Math.PI / 2);
    }

    public static Bitmap rotateMinus90(final Bitmap bufferedImage) {
        return rotate(bufferedImage, -Math.PI / 2);
    }

    public static Bitmap rotate(Bitmap bufferedImage, double theta) {
        if(theta == 0) { return bufferedImage; }

        final double sin = Math.abs(Math.sin(theta)), cos = Math.abs(Math.cos(theta));
        final int w = bufferedImage.getWidth();
        final int h = bufferedImage.getHeight();
        final int neww = (int) Math.floor(w * cos + h * sin);
        final int newh = (int) Math.floor(h * cos + w * sin);
        //Bitmap result = Bitmap.createBitmap(neww, newh, bufferedImage.getConfig());
        
        
        //Canvas canvas = new Canvas(result);
        Matrix matrix = new Matrix();
        //matrix.setTranslate((neww - w) / 2, (newh - h) / 2);
        matrix.postRotate((float) Math.toDegrees(theta));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bufferedImage,w,h,true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        return rotatedBitmap;
    }

}
