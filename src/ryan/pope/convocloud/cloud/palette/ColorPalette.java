package ryan.pope.convocloud.cloud.palette;

import java.util.Random;

import android.graphics.Color;

public class ColorPalette 
{

    private static final Random RANDOM = new Random();


    private int[] _colors;

    private int next = 0;

    public ColorPalette(int... colors) 
    {
        _colors = colors;
    }

    public int next() 
    {
        return _colors[next++ % _colors.length];
    }
    
    public int random()
    {
    	float hue = RANDOM.nextInt(359) + 1;
    	float saturation = .9f;
    	float brightness = .7f;
    	
    	float[] hsv = { hue, saturation, brightness };
    	return Color.HSVToColor(hsv);
    }

    public int randomNext() 
    {
        return _colors[RANDOM.nextInt(_colors.length)];
    }

}
