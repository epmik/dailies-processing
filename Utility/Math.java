package Utility;

import Procedural.*;
import Procedural.Interfaces.*;

public class Math
{
  public static final double Pi = 3.1415926535897932384626433832795;
  public static final double Pi2 = Pi * 2.0;

  private static final double ToRadiansFactor = Pi / 180;
  private static final double ToDegreesFactor = 180 / Pi;
  private static IRandomGenerator RandomGenerator = new RandomGenerator();

  public static float Pow(float value, float power) 
  {
    return (float)java.lang.Math.pow(value, power);
  }  

  public static double Pow(double value, double power) 
  {
    return java.lang.Math.pow(value, power);
  }

  public static float Abs(float value) 
  {
    return (float)java.lang.Math.abs(value);
  }  

  public static double Abs(double value) 
  {
    return java.lang.Math.abs(value);
  }

  public static float Floor(float value) 
  {
    return (float)java.lang.Math.floor(value);
  }  

  public static double Floor(double value) 
  {
    return java.lang.Math.floor(value);
  }  

  public static float Ceil(float value) 
  {
    return (float)java.lang.Math.ceil(value);
  }  

  public static double Ceil(double value) 
  {
    return java.lang.Math.ceil(value);
  }
      
  public static float Sqrt(float value) 
  {
    return (float)java.lang.Math.sqrt(value);
  }  

  public static double Sqrt(double value) 
  {
    return java.lang.Math.sqrt(value);
  }  

  public static float Cos(float value) 
  {
    return (float)java.lang.Math.cos(value);
  }  

  public static double Cos(double value) 
  {
    return java.lang.Math.cos(value);
  }  

  public static float aCos(float value) 
  {
    return (float)java.lang.Math.acos(value);
  }  

  public static double aCos(double value) 
  {
    return java.lang.Math.acos(value);
  }  

  public static float Sin(float value) 
  {
    return (float)java.lang.Math.sin(value);
  }  

  public static double Sin(double value) 
  {
    return java.lang.Math.sin(value);
  }  

  public static float aSin(float value) 
  {
    return (float)java.lang.Math.asin(value);
  }  

  public static double aSin(double value) 
  {
    return java.lang.Math.asin(value);
  }  

  public static float Tan(float value) 
  {
    return (float)java.lang.Math.tan(value);
  }  

  public static double Tan(double value) 
  {
    return java.lang.Math.tan(value);
  }  

  public static float aTan(float value) 
  {
    return (float)java.lang.Math.atan(value);
  }  

  public static double aTan(double value) 
  {
    return java.lang.Math.atan(value);
  }  

  public static float Max(float m, float n) 
  {
    return (float)java.lang.Math.max(m, n);
  }  

  public static double Max(double m, double n) 
  {
    return java.lang.Math.max(m, n);
  }  

  public static float Min(float m, float n) 
  {
    return (float)java.lang.Math.min(m, n);
  }  

  public static double Min(double m, double n) 
  {
    return java.lang.Math.min(m, n);
  }  

  public static double Map(double value, double newMax) 
  {
    return Map(value, 0D, 1D, 0D, newMax);
  }  

  public static double Map(double value, double newMin, double newMax) 
  {
    return Map(value, 0D, 1D, newMin, newMax);
  }  

  public static double Map(double value, double min, double max, double mapMin, double mapMax) 
  {
    double normalized = (value - min) / (max - min);
    return normalized * (mapMax - mapMin) + mapMin;
  }  

	public static double Clamp(double value)
  {
      return Clamp(value, 0.0, 1.0);
  }
  
	public static double Clamp(double value, double min, double max)
	{
    if (value > max) 
    {
			return max;
		}
    if (value < min) 
    {
			return min;
		}
		return value;
	}
  
// Converts 3D cartesian coordinates to polar coordinates
// 
// theVector : vector to convert
// returns   : vector containing 'length', 'angleY' and 'angleZ', 
//             so that rotating a point (length, 0, 0) first  
//             around the y-axis with angleY and then around the  
//             z-axis with angleZ results again in point (x, y, z)
public static double[] CartesianToSphericalCoordinates(double x, double y, double z) 
{

  // see
  // https://www.processing.org/discourse/beta/num_1236393966.html

  var length = Length(x, y, z);

  var longitude = 0.0;
  var latitude = 0.0;

  if (length > 0) 
  {
    longitude = -java.lang.Math.atan2(z, x);
    latitude = java.lang.Math.asin(y / length);
  }
  
  return new double[] {
    longitude,
    latitude,
    length };
}  

  public static double[] RandomPointOnSphere() 
  {
    return RandomPointOnSphere(1.0);
  }

  public static double[] RandomPointOnSphere(double radius) 
  {
    var r = RandomSphericalCoordinates();
    return VectorFromSphericalCoordinates(r[0], r[1], radius);
  }

  public static double[] RandomPointOnSphere(IRandomGenerator randomGenerator) 
  {
    return RandomPointOnSphere(1.0, randomGenerator);
  }

  public static double[] RandomPointOnSphere(double radius, IRandomGenerator randomGenerator) 
  {
    var r = RandomSphericalCoordinates(randomGenerator);
    return VectorFromSphericalCoordinates(r[0], r[1], radius);
  }

  public static double[] VectorFromSphericalCoordinates(double theta, double phi) 
  {
    return VectorFromSphericalCoordinates(theta, phi, 1.0);
  }

  public static double[] VectorFromSphericalCoordinates(double theta, double phi, double radius) 
  {
    return new double[] {
        (radius * Math.Sin(phi) * Math.Cos(theta)),
        (radius * Math.Sin(phi) * Math.Sin(theta)),
        (radius * Math.Cos(phi)) };
  }

  /// first return value is the Azimuth angle or Longitude () int the range  0 to 2 * PI
  /// second return value is the Zenith angle or Latitude () int the range  0 to PI
  public static double[] RandomSphericalCoordinates() 
  {
    return RandomSphericalCoordinates(RandomGenerator);
  }

  /**
   * Returns the Azimuth angle or Longitude in the range 0 to 2 * PI
   * and
   * the Zenith angle or Latitude in the range 0 to PI
   *
   * @param   randomGenerator   the IRandomGenerator object used to create random coordinates
   * @return  a double[] array containing the Longitude {@code return[0]} and Latitude {@code return[1]}
   */
    public static double[] RandomSphericalCoordinates(IRandomGenerator randomGenerator) 
  {
    var u = randomGenerator.Value();
    var v = randomGenerator.Value();
    var theta = 2 * Math.Pi * u;
    var phi = java.lang.Math.acos(2 * v - 1);
    return new double[] {
      theta,
      phi,
    };    
  }


  public static double CalculateAngle(int originX, int originY, int directionX, int directionY)
  {
    // 
    // returns the angle between 2 points
    // assuming the first point is the origin of the coordinate system
    // and the second point denotes a vector running from the first point to the second point
    // 
    // we're also assuming the positive x-axis runs from left to right
    // and the positive y-axis runs from top to bottom
    // and rotations run clockwise
    // 
    // the return value is in radians. Use Utility.ToDegrees(radians) to convert.
    // 
    // see
    // https://stackoverflow.com/questions/2676719/calculating-the-angle-between-the-line-defined-by-two-points
    // 
    return java.lang.Math.atan2(directionY - originY, directionX - originX);
  }

  public static double ToRadians(double degrees)
  {
    return degrees * ToRadiansFactor;
  }

  public static double ToDegrees(double radians)
  {
      return radians * ToDegreesFactor;
  }
  
  public static double DistanceSquared(int fromX, int fromY, int toX, int toY) 
  {
      return LengthSquared(fromX-toX, fromY-toY);
  }
 
  public static double DistanceSquared(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) 
  {
      return LengthSquared(fromX-toX, fromY-toY, fromZ-toZ);
  }
 
  public static double Distance(int fromX, int fromY, int toX, int toY) 
  {
    return Length(fromX - toX, fromY - toY);
  }
  
  public static double Distance(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) 
  {
      return Length(fromX-toX, fromY-toY, fromZ-toZ);
  }
  
  public static double LengthSquared(int x, int y) 
  {
    return (x * x) + (y * y);
  }
 
  public static double LengthSquared(int x, int y, int z) 
  {
      return (x * x) + (y * y) + (z * z);
  }
  
  public static double Length(int x, int y) 
  {
      return java.lang.Math.sqrt(LengthSquared(x, y));
  }
  
  public static double Length(int x, int y, int z) 
  {
      return java.lang.Math.sqrt(LengthSquared(x, y, z));
  }
  
  public static double DistanceSquared(double fromX, double fromY, double toX, double toY) 
  {
      return LengthSquared(fromX-toX, fromY-toY);
  }
 
  public static double DistanceSquared(double fromX, double fromY, double fromZ, double toX, double toY, double toZ) 
  {
      return LengthSquared(fromX-toX, fromY-toY, fromZ-toZ);
  }
 
  public static double Distance(double fromX, double fromY, double toX, double toY) 
  {
    return Length(fromX - toX, fromY - toY);
  }
  
  public static double Distance(double fromX, double fromY, double fromZ, double toX, double toY, double toZ) 
  {
      return Length(fromX-toX, fromY-toY, fromZ-toZ);
  }
  
  public static double LengthSquared(double x, double y) 
  {
    return (x * x) + (y * y);
  }
 
  public static double LengthSquared(double x, double y, double z) 
  {
      return (x * x) + (y * y) + (z * z);
  }
  
  public static double Length(double x, double y) 
  {
      return java.lang.Math.sqrt(LengthSquared(x, y));
  }
  
  public static double Length(double x, double y, double z) 
  {
      return java.lang.Math.sqrt(LengthSquared(x, y, z));
  }
 
}
