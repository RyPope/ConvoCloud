package ryan.pope.textcloud.cloud.collide.image;

import java.util.Random;

public class AngleGenerator 
{

    private static final Random RANDOM = new Random();

    private int _steps;

    private double[] _thetas;

    private int next = 0;

    public AngleGenerator() 
    {
        _steps = 3;
        _thetas = calculateThetas(-90, 90);
    }

    public AngleGenerator(int degrees) 
    {
        _steps = 1;
        _thetas = new double[] { degreesToRadians(degrees) };
    }

    public AngleGenerator(double fromDegrees, double toDegrees, int steps) 
    {
        _steps = steps;
        _thetas = calculateThetas(fromDegrees, toDegrees);
    }

    public double next() 
    {
        return _thetas[next++ % _steps];
    }

    public double randomNext() 
    {
        return _thetas[RANDOM.nextInt(_steps)];
    }

    private double[] calculateThetas(final double to, final double from) 
    {
        final double stepSize = (to - from) / (_steps - 1);
        final double[] thetas = new double[_steps];
        for(int i = 0; i < _steps; i++) {
            thetas[i] = degreesToRadians(from + (i * stepSize));
        }
        return thetas;
    }

    private double degreesToRadians(final double degrees)
    {
        return Math.PI * degrees / 180.0;
    }

}
