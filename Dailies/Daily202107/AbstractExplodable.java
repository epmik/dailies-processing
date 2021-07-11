package Dailies.Daily202107;

import processing.core.*;
import Utility.ColorPicker;
import Utility.Math;
  
  public abstract class AbstractExplodable implements IExplodable
  {
    protected float _x;
    protected float _y;
    protected float _z;
    protected float _longitude;
    protected float _latitude;
    protected float _offset;
    protected float[] _direction;

    public float X() {
      return _x;
    }

    public void X(float x) {
      _x = x;
    }

    public float Y() {
      return _y;
    }

    public void Y(float y) {
      _y = y;
    }

    public float Z() {
      return _z;
    }

    public void Z(float z) {
      _z = z;
    }

    public float Offset() {
      return _offset;
    }

    public void Offset(float offset) {
      _offset = offset;
    }

    public float Longitude()
    {
      return _longitude;
    }

    public void Longitude(float longitude)
    {
      _longitude = longitude;
      ComputeDirectionVector();
    }

    public float Latitude()
    {
      return _latitude;
    }

    public void Latitude(float latitude)
    {
      _latitude = latitude;
      ComputeDirectionVector();
    }

    public abstract void Draw(PGraphics canvas);

    public abstract void Colorize(ColorPicker colorPicker);

    protected void ComputeDirectionVector()
    {
      var v = Math.VectorFromSphericalCoordinates(_longitude, _latitude);

      _direction = new float[] { (float) v[0], (float) v[1], (float) v[2] };
    }
  }