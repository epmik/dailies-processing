package Procedural.Interfaces;

public interface INoiseGenerator
{
    int Octaves();
    void Octaves(int octaves);
    boolean ClampValues();
    void ClampValues(boolean state);
    boolean AllowNegativeValues();
    void AllowNegativeValues(boolean state);
    double InputMultiplierX();
    double InputMultiplierY();
    double InputMultiplierZ();
    double InputMultiplierW();
    // void InputMultipliers(double multiplier);
    void InputMultiplier(double multiplierX);
    // void InputMultiplier(double multiplierX, double multiplierY);
    void InputMultiplier(double multiplierX, double multiplierY, double multiplierZ);
    void InputMultiplier(double multiplierX, double multiplierY, double multiplierZ, double multiplierW);
    long Seed();
    long ReSeed();
    long ReSeed(long seed);
    double Value(double x);
    double Value(double x, double y);
    double Value(double x, double y, double z);
    double Value(double x, double y, double z, double w);
}
