package ryan.pope.convocloud.cloud.objects;

import java.util.Stack;

import ryan.pope.convocloud.application.Globals;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

public class MaximalRectangle 
{
	private int totalWidth;
	private int totalHeight;
    public Rect maximalRectangle(Bitmap _imageBitmap) 
    {
    	int _imageBitmapWidth = _imageBitmap.getWidth();
    	int _imageBitmapHeight = _imageBitmap.getHeight();
        if (_imageBitmap == null || _imageBitmapWidth == 0)
        {
            return new Rect();
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
        
        int maxRow = 0;
        Rect maxRect = new Rect();
        
        for (int row = 0; row < heights.length; row++)
        {
        	Rect checkRect = maxArea(heights[row]);
        	if(maxRect.width() * maxRect.height() < checkRect.width() * checkRect.height())
        	{
        		maxRow = row;
        		maxRect.set(heights[maxRow][0] - checkRect.right, checkRect.top, (heights[maxRow][0] - checkRect.right) + checkRect.right, checkRect.bottom);
        	}
        }
        
        if(Globals.DEBUG) Log.i(Globals.DEBUG_TAG, "Max row: " + heights[maxRow][0]);
        return maxRect;
    }

    private Rect maxArea(int[] heights)
    {
        if (heights == null || heights.length == 0)
        {
            return new Rect();
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
                
                if(max < height * width)
                {
                	max = height * width;
                	maxRect.set(0, 0, height, width);
                }
            }
            
        }
        while(!stack.isEmpty())
        {
            int height = heights[stack.pop()];
            
            int width = stack.isEmpty() ? i : i - stack.peek() - 1;
            if(max < height * width)
            {
            	max = height * width;
            	maxRect.set(0, 0, height, width);
            }
        }
        
        return maxRect;
        
    }
}