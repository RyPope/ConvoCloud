package ryan.pope.textcloud.cloud.font;

import android.graphics.Typeface;

public enum FontWeight 
{
    PLAIN(Typeface.NORMAL),
    BOLD(Typeface.BOLD),
    ITALIC(Typeface.ITALIC);

    private int _weight;

    FontWeight(int weight) 
    {
        _weight = weight;
    }

    public int getWeight() 
    {
        return _weight;
    }

}
