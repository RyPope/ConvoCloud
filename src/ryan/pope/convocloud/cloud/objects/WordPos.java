package ryan.pope.convocloud.cloud.objects;

public class WordPos 
{

    private int _x;

    private int _y;

    public WordPos(int x, int y) 
    {
        _x = x;
        _y = y;
    }

    public int getX() 
    {
        return _x;
    }

    public void setX(int x) 
    {
        _x = x;
    }

    public int getY() 
    {
        return _y;
    }

    public void setY(int y) 
    {
        this._y = y;
    }

    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (!(o instanceof WordPos)) return false;

        WordPos vector2d = (WordPos) o;

        if (_x != vector2d._x) return false;
        if (_y != vector2d._y) return false;

        return true;
    }

    @Override
    public int hashCode() 
    {
        int result = _x;
        result = 31 * result + _y;
        return result;
    }
}
