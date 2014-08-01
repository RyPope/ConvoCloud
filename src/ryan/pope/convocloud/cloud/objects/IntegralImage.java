package ryan.pope.convocloud.cloud.objects;

import android.graphics.Bitmap;

public class IntegralImage 
{
private int[][] integralImage = null;

public IntegralImage(Bitmap image) 
{
    int originalImageHeight = image.getHeight();
    int originalImageWidth = image.getWidth();
    integralImage = new int[originalImageWidth][originalImageHeight];
    int[][] originalPixels = new int[image.getWidth()][image.getHeight()];
    
    for(int i = 0; i < image.getWidth(); i++)
    {
        for(int j = 0; j < image.getHeight(); j++)
        {
            originalPixels[i][j] = image.getPixel(i, j);
        }
    }
    
    int originalPixelValue = 0;
    for (int width = 0; width < originalImageWidth; width++) 
    {
        for (int height = 0; height < originalImageHeight; height++) 
        {
            originalPixelValue = originalPixels[width][height];

            if (width == 0 && height == 0) 
            {
                integralImage[width][height] = originalPixelValue;
            }
            else if (width == 0) 
            {
                integralImage[width][height] = originalPixelValue + integralImage[width][height - 1];
            }
            else if (height == 0) 
            {
                integralImage[width][height] = originalPixelValue + integralImage[width - 1][height];
            }
            else 
            {
                integralImage[width][height] = originalPixelValue + integralImage[width][height - 1] + integralImage[width - 1][height] - integralImage[width - 1][height - 1];
            }
        }
    }
}

public int total(int x1, int y1, int x2, int y2) 
{
    int a = x1 > 0 && y1 > 0 ? integralImage[x1-1][y1-1] : 0;
    int b = x1 > 0 ? integralImage[x1-1][y2] : 0;
    int c = y1 > 0 ? integralImage[x2][y1-1] : 0;
    int d = integralImage[x2][y2];
    return a + d - b - c;
}

}