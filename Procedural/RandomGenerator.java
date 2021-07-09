package Procedural;

import Procedural.Interfaces.IRandomGenerator;

public class RandomGenerator implements IRandomGenerator
{
    private long _seed;
    private java.util.Random _random;
 
    public RandomGenerator()
    {
      _random = new java.util.Random();
      ReSeed();
    }
  
    public RandomGenerator(long seed)
    {
      this();
      ReSeed(seed);
    }
    
    public long Seed()
    {
      return _seed;
    }
    
    public long ReSeed()
    {
      return ReSeed(new java.util.Date().getTime());
    }
    
    public long ReSeed(long seed)
    {
      _seed = seed;
      _random.setSeed(_seed);
      return _seed;
    }
    
    public double Value()
    {
        return _random.nextDouble();
    }
    
    public double Value(double max)
    {
        return Value(0, max);
    }
    
    public double Value(double min, double max)
    {
        return min + _random.nextDouble() * (max - min);
    }
    
    public int Value(int max)
    {
        return Value(0, max);
    }
    
    public int Value(int min, int max)
    {
        return (int)(min + _random.nextDouble() * (max - min));
    }
    
    public boolean Boolean()
    {
      return Value(0, 2) == 0;
    }
  
}
