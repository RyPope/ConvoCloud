package ryan.pope.textcloud.cloud.padding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ryan.pope.textcloud.cloud.objects.Word;

public class RectanglePadder implements Padder 
{

    @Override
    public void pad(Word word, int padding) 
    {
        if(padding <= 0) 
        { 
        	return; 
        }

        Bitmap _imageBitmap = word.getBufferedImage();
        int width = _imageBitmap.getWidth() + padding * 2;
        int height = _imageBitmap.getHeight() + padding * 2;

        Bitmap newBufferedImage = Bitmap.createBitmap(width, height, _imageBitmap.getConfig());
        Canvas graphics = new Canvas(newBufferedImage);
        
        graphics.drawBitmap(_imageBitmap, padding, padding, null);

        word.setBufferedImage(newBufferedImage);
    }

}
