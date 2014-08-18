package ryan.pope.convocloud.cloud.colour;

import java.util.Random;

import ryan.pope.convocloud.objects.Scheme;
import android.graphics.Color;

public class ColourPalette 
{

    private static final Random RANDOM = new Random();

    private Scheme _scheme;

    public ColourPalette(Scheme scheme) 
    {
        _scheme = scheme;
    }
    
    public int random()
    {
    	switch(_scheme)
    	{
    		case Default:
    		{
		    	float hue = RANDOM.nextInt(359) + 1;
		    	float saturation = .9f;
		    	float brightness = .7f;
		    	
		    	float[] hsv = { hue, saturation, brightness };
		    	return Color.HSVToColor(hsv);
    		}
    		case Black:
    		{
		    	float hue = RANDOM.nextInt(359) + 1;
		    	float saturation = .9f;
		    	float brightness = 0f;
		    	
		    	float[] hsv = { hue, saturation, brightness };
		    	return Color.HSVToColor(hsv);
    		}
    		case Dark:
    		{
		    	float hue = RANDOM.nextInt(359) + 1;
		    	float saturation = .9f;
		    	float brightness = .25f;
		    	
		    	float[] hsv = { hue, saturation, brightness };
		    	return Color.HSVToColor(hsv);
    		}
    		case Bright:
    		{
		    	float hue = RANDOM.nextInt(359) + 1;
		    	float saturation = .9f;
		    	float brightness = .75f;
		    	
		    	float[] hsv = { hue, saturation, brightness };
		    	return Color.HSVToColor(hsv);
    		}
    		case GrayScale:
    		{
		    	float hue = 0;
		    	float saturation = 0f;
		    	float brightness = RANDOM.nextFloat();
		    	
		    	float[] hsv = { hue, saturation, brightness };
		    	return Color.HSVToColor(hsv);
    		}
    		case White:
    		{
		    	float hue = 360;
		    	float saturation = 0f;
		    	float brightness = 1f;
		    	
		    	float[] hsv = { hue, saturation, brightness };
		    	return Color.HSVToColor(hsv);
    		}
    		default:
    		{
		    	float hue = RANDOM.nextInt(359) + 1;
		    	float saturation = .9f;
		    	float brightness = .7f;
		    	
		    	float[] hsv = { hue, saturation, brightness };
		    	return Color.HSVToColor(hsv);
    		}
    	}
    }

}
