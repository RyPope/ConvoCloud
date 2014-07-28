package ryan.pope.textcloud.cloud.font.scale;

public class LinearFontScalar implements FontScalar 
{

    private int _minFont;
    private int _maxFont;

    public LinearFontScalar(int minFont, int maxFont) 
    {
        _minFont = minFont;
        _maxFont = maxFont;
    }

    @Override
    public float scale(int value, int minValue, int maxValue) 
    {
        float leftSpan = maxValue - minValue;
        float rightSpan = _maxFont - _minFont;

        float valueScaled = (value - minValue) / leftSpan;

        return (_minFont + (valueScaled * rightSpan));
    }
}
