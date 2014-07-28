package ryan.pope.textcloud.cloud.font;

import android.graphics.Typeface;

public class CloudFont 
{

    private static final int DEFAULT_WEIGHT = 10;

    private final Typeface font;

    public CloudFont(String type, FontWeight weight) 
    {
        this.font = Typeface.create(type, DEFAULT_WEIGHT);
    }

    public CloudFont(Typeface font) {
        this.font = font;
    }

    public Typeface getFont() {
        return this.font;
    }

}
