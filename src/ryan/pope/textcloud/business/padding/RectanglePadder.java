package ryan.pope.textcloud.business.padding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ryan.pope.textcloud.business.Word;

public class RectanglePadder implements Padder {

    @Override
    public void pad(Word word, int padding) {
        if(padding <= 0) { return; }

        final Bitmap bufferedImage = word.getBufferedImage();
        final int width = bufferedImage.getWidth() + padding * 2;
        final int height = bufferedImage.getHeight() + padding * 2;

        final Bitmap newBufferedImage = Bitmap.createBitmap(width, height, bufferedImage.getConfig());
        final Canvas graphics = new Canvas(newBufferedImage);
        
        graphics.drawBitmap(bufferedImage, padding, padding, null);

        word.setBufferedImage(newBufferedImage);
    }

}
