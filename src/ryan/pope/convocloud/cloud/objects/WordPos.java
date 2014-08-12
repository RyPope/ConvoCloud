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

    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (!(o instanceof WordPos)) return false;

        WordPos pos = (WordPos) o;

        if (_x != pos._x) return false;
        if (_y != pos._y) return false;

        return true;
    }

    public int getX() 
    {
        return _x;
    }

    public int getY() 
    {
        return _y;
    }

    public void setX(int x) 
    {
        _x = x;
    }

    public void setY(int y) 
    {
        this._y = y;
    }
}
