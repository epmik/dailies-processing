package Procedural.Interfaces;

public interface IRandomGenerator
{
    long Seed();
    long ReSeed();
    long ReSeed(long seed);
    double Value();
    double Value(double max);
    double Value(double min, double max);
    int Value(int max);
    int Value(int min, int max);
    boolean Boolean();
}
