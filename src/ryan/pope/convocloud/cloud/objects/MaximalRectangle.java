package ryan.pope.convocloud.cloud.objects;

import java.util.Stack;

/*Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle containing all ones and return its area.*/

public class MaximalRectangle 
{
    public int maximalRectangle(char[][] matrix) 
    {
        if (matrix==null ||matrix.length==0){
            return 0;
        }
        
        int[][] heights=new int[matrix.length][matrix[0].length];
        
        // build heights table, once we have heights table, at each row, we can use 
        // method which used in question of Largest Rectangle in Histogram to calculate 
        // max area at until this row,.
        
        for (int row=0; row<matrix.length; row++)
        {
            for (int col=0; col<matrix[0].length; col++)
            {
                if (row==0){
                    heights[row][col]=matrix[row][col]=='0'?0:1;
                }
                else{
                    heights[row][col]=matrix[row][col]=='0'?0:heights[row-1][col]+1;
                }
            }
        }
        
        int max=0;
        
        for (int row=0; row<heights.length; row++)
        {
          // update max with maxArea of each row
            max=Math.max(max, maxArea(heights[row]));
        }
        
        return max;
    }
    
    // calculate maxArea for each row
    private int maxArea(int[] heights){
        if (heights==null||heights.length==0)
        {
            return 0;
        }
        
        Stack<Integer> stack=new Stack<Integer>();
        int max=0;
        
        int i=0;
        while(i<heights.length){
            if (stack.isEmpty()||heights[stack.peek()]<=heights[i]){
                stack.push(i);
                i++;
            }
            else{
                int height=heights[stack.pop()];
                
                int width=stack.isEmpty()?i:i-stack.peek()-1;
                
                max=Math.max(max, height*width);
            }
            
        }
        while(!stack.isEmpty())
        {
            int height=heights[stack.pop()];
            
            int width=stack.isEmpty()?i:i-stack.peek()-1;
            max=Math.max(max, height*width);
        }
        
        return max;
        
    }
}