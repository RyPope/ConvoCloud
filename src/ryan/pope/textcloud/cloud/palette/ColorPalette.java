package ryan.pope.textcloud.cloud.palette;

import java.util.Random;

/**
 * Created by kenny on 6/30/14.
 */
public class ColorPalette {

    private static final Random RANDOM = new Random();


    private final int[] colors;

    private int next = 0;

    public ColorPalette(int... colors) {
        this.colors = colors;
    }

    public int next() {
        return colors[next++ % colors.length];
    }

    public int randomNext() {
        return colors[RANDOM.nextInt(colors.length)];
    }

}
