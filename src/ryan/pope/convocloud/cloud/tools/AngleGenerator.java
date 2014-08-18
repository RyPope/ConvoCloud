package ryan.pope.convocloud.cloud.tools;

import java.util.Random;

import ryan.pope.convocloud.objects.RotationType;

public class AngleGenerator 
{
    private static final Random RANDOM = new Random();
    private int _steps;
    private double[] _thetas;
    private int next = 0;

    public AngleGenerator(RotationType _rotation) 
    {
    	switch(_rotation)
    	{
    		case Random:
    		{
    	        _steps = 2;
    	        _thetas = calculateThetas(0, 90);
    			break;
    		}
    		case Horizontal:
    		{
    	        _steps = 1;
    	        _thetas = new double[]{ 0 };
    			break;
    		}
    		case Vertical:
    		{
    	        _steps = 1;
    	        _thetas = new double[]{ degreesToRadians(90) };
    			break;
    		}
    		default:
    		{
    	        _steps = 2;
    	        _thetas = calculateThetas(0, 90);
    			break;
    		}
    	}
    }


    private double[] calculateThetas(final double to, final double from) 
    {
        final double stepSize = (to - from) / (_steps - 1);
        final double[] thetas = new double[_steps];
        for(int i = 0; i < _steps; i++) 
        {
            thetas[i] = degreesToRadians(from + (i * stepSize));
        }
        return thetas;
    }

    private double degreesToRadians(final double degrees)
    {
        return Math.PI * degrees / 180.0;
    }

    public double next() 
    {
        return _thetas[next++ % _steps];
    }

    public double randomNext() 
    {
        return _thetas[RANDOM.nextInt(_steps)];
    }

}
