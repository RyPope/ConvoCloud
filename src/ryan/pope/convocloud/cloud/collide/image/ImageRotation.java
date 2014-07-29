package ryan.pope.convocloud.cloud.collide.image;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageRotation 
{

    public static Bitmap rotate90(final Bitmap bufferedImage) 
    {
        return rotate(bufferedImage, Math.PI / 2);
    }

    public static Bitmap rotateMinus90(final Bitmap bufferedImage) 
    {
        return rotate(bufferedImage, -Math.PI / 2);
    }

    public static Bitmap rotate(Bitmap bufferedImage, double theta) 
    {
        if(theta == 0) 
        { 
        	return bufferedImage; 
        }

        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        
        Matrix matrix = new Matrix();
        matrix.postRotate((float) Math.toDegrees(theta));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bufferedImage,w,h,true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap .getHeight(), matrix, true);
        return rotatedBitmap;
    }

}
