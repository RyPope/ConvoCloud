package ryan.pope.convocloud.cloud.font.scale;

public class SqrtFontScalar implements FontScalar 
{

    private int _minFont;
    private int _maxFont;

    public SqrtFontScalar(int minFont, int maxFont) 
    {
        _minFont = minFont;
        _maxFont = maxFont;
    }

    @Override
    public float scale(int value, int minValue, int maxValue) 
    {
        double leftSpan = Math.sqrt(maxValue) - Math.sqrt(minValue);
        double rightSpan = _maxFont - _minFont;

        double valueScaled = (Math.sqrt(value) - Math.sqrt(minValue)) / leftSpan;
        
        return (float)(_minFont + (valueScaled * rightSpan));
    }
}
