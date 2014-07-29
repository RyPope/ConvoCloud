package ryan.pope.convocloud.cloud.palette;

import java.util.Random;

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

    public int randomNext() 
    {
        return _colors[RANDOM.nextInt(_colors.length)];
    }

}
