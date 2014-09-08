package ryan.pope.convocloud.cloud.objects;

import java.util.Stack;

import android.graphics.Bitmap;
import android.graphics.Rect;

final class Cache 
{
	private int[] _height;
	private int _color;

	Cache(final int size, final int color) 
	{
		_height = new int[size + 1];
		_color = color;
	}

	public void aggregate(final int[] row) 
	{
		for(int col = 0; col < row.length; col++) 
		{
			final int element = row[col];

			if(element != _color) 
			{
				_height[col] = 0;
			} 
			else 
			{
				_height[col] = _height[col] + 1;
			}
		}
	}

	public int get(final int col) 
	{
		return _height[col];
	}
}

final class Cell 
{

	final int _col;
	final int _row;

	Cell(final int col, final int row) 
	{
		_col = col;
		_row = row;
	}

}

 public class MaximalRectangle
 {

	public Rect maximalRect(Bitmap _imageBitmap, int color, double minPixels)
	{
		int bestArea = 0;
		Cell bestLowerLeftCorner = new Cell(0, 0);
		Cell bestUpperRightCorner = new Cell(-1, -1);

		final int numColumns = _imageBitmap.getWidth();
		final int numRows = _imageBitmap.getHeight();

		final Stack<Cell> stack = new Stack<Cell>();
		final Cache rectangleHeightCache = new Cache(numColumns, color);

		for(int row = 0; row < numRows; row++) 
		{
			int[] width = new int[_imageBitmap.getWidth()];
			for(int i = 0; i < _imageBitmap.getWidth(); i++)
			{
				width[i] = _imageBitmap.getPixel(i, row);
			}

			rectangleHeightCache.aggregate(width);
			for(int col = 0, currentRectHeight = 0; col <= numColumns; col += 2) 
			{
				final int aggregateRectHeight = rectangleHeightCache.get(col);

				if(aggregateRectHeight > currentRectHeight) 
				{
					stack.push(new Cell(col, currentRectHeight));
					currentRectHeight = aggregateRectHeight;
				} 
				else if(aggregateRectHeight < currentRectHeight) 
				{

					Cell rectStartCell;
					do {
						rectStartCell = stack.pop();
						final int rectWidth = col - rectStartCell._col;
						final int area = currentRectHeight * rectWidth;
						if(area > bestArea && currentRectHeight > minPixels && rectWidth > minPixels) 
						{
							bestArea = area;
							bestLowerLeftCorner = new Cell(rectStartCell._col, row);
							bestUpperRightCorner = new Cell(col - 1, row - currentRectHeight + 1);
						}
						currentRectHeight = rectStartCell._row;
					} while(aggregateRectHeight < currentRectHeight);

					currentRectHeight = aggregateRectHeight;
					if(currentRectHeight != 0) 
					{
						stack.push(rectStartCell);
					}
				}
			}
		}
		
		return new Rect(bestLowerLeftCorner._col, bestUpperRightCorner._row, bestUpperRightCorner._col, bestLowerLeftCorner._row);
	}
 }