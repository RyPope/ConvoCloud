package ryan.pope.convocloud.cloud.objects;

import java.util.Stack;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

public class MaximalRectangle 
{
    public int maximalRectangle(Bitmap _imageBitmap) 
    {
    	int _imageBitmapWidth = _imageBitmap.getWidth();
    	int _imageBitmapHeight = _imageBitmap.getHeight();
        if (_imageBitmap == null || _imageBitmapWidth == 0)
        {
            return 0;
        }
        
        int[][] heights = new int[_imageBitmapWidth][_imageBitmapHeight];

        for (int row = 0; row < _imageBitmapWidth ; row++)
        {
            for (int col = 0; col < _imageBitmapHeight; col++)
            {
                if (row == 0)
                {
                    heights[row][col] = _imageBitmap.getPixel(row, col) != Color.WHITE ? 0 : 1;
                }
                else
                {
                    heights[row][col] = _imageBitmap.getPixel(row, col) != Color.WHITE ? 0 : heights[row-1][col] + 1;
                }
            }
        }
        
        int max = 0;
        
        for (int row = 0; row < heights.length; row++)
        {
            max = Math.max(max, maxArea(heights[row]));
        }
        
        return max;
    }

    private int maxArea(int[] heights)
    {
        if (heights == null || heights.length == 0)
        {
            return 0;
        }
        
        Stack<Integer> stack = new Stack<Integer>();
        int max = 0;
        Rect maxRect = new Rect();
        
        int i = 0;
        while(i < heights.length)
        {
            if (stack.isEmpty() || heights[stack.peek()] <= heights[i])
            {
                stack.push(i);
                i++;
            }
            else
            {
                int height = heights[stack.pop()];
                
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                
                max = Math.max(max, height * width);
            }
            
        }
        while(!stack.isEmpty())
        {
            int height = heights[stack.pop()];
            
            int width=stack.isEmpty() ? i : i - stack.peek() - 1;
            max = Math.max(max, height*width);
        }
        
        return max;
        
    }
}