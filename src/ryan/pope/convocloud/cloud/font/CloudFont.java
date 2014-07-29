package ryan.pope.convocloud.cloud.font;

import android.graphics.Typeface;

public class CloudFont 
{

    private static final int DEFAULT_WEIGHT = 10;

    private final Typeface _font;

    public CloudFont(String type, FontWeight weight) 
    {
        _font = Typeface.create(type, DEFAULT_WEIGHT);
    }

    public CloudFont(Typeface font) 
    {
        _font = font;
    }

    public Typeface getFont() 
    {
        return _font;
    }

}
