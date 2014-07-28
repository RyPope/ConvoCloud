package ryan.pope.textcloud.cloud.font;

import android.graphics.Typeface;

public enum FontWeight 
{
    PLAIN(Typeface.NORMAL),
    BOLD(Typeface.BOLD),
    ITALIC(Typeface.ITALIC);

    private final int weight;

    FontWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

}
