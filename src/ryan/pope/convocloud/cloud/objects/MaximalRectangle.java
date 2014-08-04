package ryan.pope.convocloud.cloud.objects;

import java.util.Stack;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

final class Cell 
{

	final int col;
	final int row;

	Cell(final int col, final int row) 
	{
		this.col = col;
		this.row = row;
	}

}

final class Cache 
{
	private int[] aggregateHeights;

	Cache(final int size) 
	{
		aggregateHeights = new int[size + 1];
	}

	public int get(final int col) 
	{
		return aggregateHeights[col];
	}

	public void aggregate(final int[] row) 
	{
		for(int col = 0; col < row.length; col++) 
		{
			final int element = row[col];

			if(element != Color.WHITE) 
			{
				aggregateHeights[col] = 0;
			} 
			else 
			{
				aggregateHeights[col] = aggregateHeights[col] + 1;
			}
		}
	}
}

 public class MaximalRectangle
 {

	public Rect maximalRect(Bitmap _imageBitmap)
	{
		int bestArea = 0;
		Cell bestLowerLeftCorner = new Cell(0, 0);
		Cell bestUpperRightCorner = new Cell(-1, -1);

		final int numColumns = _imageBitmap.getWidth();
		final int numRows = _imageBitmap.getHeight();

		final Stack<Cell> stack = new Stack<Cell>();
		final Cache rectangleHeightCache = new Cache(numColumns);

		for(int row = 0; row < numRows; row++) 
		{
			int[] width = new int[_imageBitmap.getWidth()];
			for(int i = 0; i < _imageBitmap.getWidth(); i++)
			{
				width[i] = _imageBitmap.getPixel(i, row);
			}

			rectangleHeightCache.aggregate(width);
			for(int col = 0, currentRectHeight = 0; col <= numColumns; col++) 
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
						final int rectWidth = col - rectStartCell.col;
						final int area = currentRectHeight * rectWidth;
						if(area > bestArea) 
						{
							bestArea = area;
							bestLowerLeftCorner = new Cell(rectStartCell.col, row);
							bestUpperRightCorner = new Cell(col - 1, row - currentRectHeight + 1);
						}
						currentRectHeight = rectStartCell.row;
					} while(aggregateRectHeight < currentRectHeight);

					currentRectHeight = aggregateRectHeight;
					if(currentRectHeight != 0) 
					{
						stack.push(rectStartCell);
					}
				}
			}
		}
		
		return new Rect(bestLowerLeftCorner.col, bestUpperRightCorner.row, bestUpperRightCorner.col, bestLowerLeftCorner.row);
	}
 }