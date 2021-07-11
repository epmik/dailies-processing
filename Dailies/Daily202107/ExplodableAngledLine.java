package Dailies.Daily202107;

import processing.core.*;

  public class ExplodableAngledLine extends ExplodableLine
  {
    private float[] _randomvector = null;

    @Override
    public void Length(float length)
    {
      _length = length;
      ComputeVectors();
    }

    @Override
    public void Longitude(float longitude)
    {
      _longitude = longitude;
      ComputeVectors();
    }

    @Override
    public void Latitude(float latitude)
    {
      _latitude = latitude;
      ComputeVectors();
    }

    private void ComputeVectors()
    {
      super.ComputeDirectionVector();
      _randomvector = new float[] { _direction[0] * _length, _direction[1] * _length, _direction[2] * _length };
    }

    public void Draw(PGraphics canvas)
    {
      canvas.pushMatrix();

      canvas.rotateY(_longitude);
      canvas.rotateZ(_latitude);
      canvas.translate(_offset, 0, 0);

      canvas.beginShape(PConstants.LINES);
  
      canvas.strokeWeight(_weight);
      canvas.noFill();

      canvas.stroke(_colorOrigin);
      canvas.vertex(_x, _y, _z);

      canvas.stroke(_colorExtent);
      canvas.vertex(_x + _length + _randomvector[0], _y + _randomvector[1], _z + _randomvector[2]);

      canvas.endShape();   
  
      canvas.popMatrix();
    }
  }
