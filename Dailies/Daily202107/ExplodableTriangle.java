package Dailies.Daily202107;

import processing.core.*;
import Utility.ColorPicker;

  public class ExplodableTriangle extends AbstractExplodable
  {
    private float[][] _points = new float[3][3];
    private int[] _colors = new int[3];
    private boolean _enableVertexColors;

    public float[] Point1()
    {
      return _points[0];
    }

    public void Point1(float[] xyz)
    {
      Point1(xyz[0], xyz[1], xyz[2]);
    }

    public void Point1(float x, float y, float z)
    {
      _points[0][0] = x;
      _points[0][1] = y;
      _points[0][2] = z;
    }

    public float[] Point2()
    {
      return _points[1];
    }

    public void Point2(float[] xyz)
    {
      Point2(xyz[0], xyz[1], xyz[2]);
    }

    public void Point2(float x, float y, float z)
    {
      _points[1][0] = x;
      _points[1][1] = y;
      _points[1][2] = z;
    }

    public float[] Point3()
    {
      return _points[2];
    }

    public void Point3(float[] xyz)
    {
      Point3(xyz[0], xyz[1], xyz[2]);
    }

    public void Point3(float x, float y, float z)
    {
      _points[2][0] = x;
      _points[2][1] = y;
      _points[2][2] = z;
    }

    public int[] Colors()
    {
      return _colors;
    }

    public void Colors(int[] colors)
    {
      Colors(colors[0], colors[1], colors[2]);
    }

    public void Colors(int a, int b, int c)
    {
      _colors[0] = a;
      _colors[1] = b;
      _colors[2] = c;
    }

    public boolean EnableVertexColors() {
      return _enableVertexColors;
    }

    public void EnableVertexColors(boolean enableVertexColors) {
      _enableVertexColors = enableVertexColors;
    }

    public void Draw(PGraphics canvas)
    {
      canvas.pushMatrix();

      canvas.rotateY(_longitude);
      canvas.rotateZ(_latitude);
      canvas.translate(_offset, 0, 0);

      canvas.beginShape(PConstants.TRIANGLES);
  
      canvas.noStroke();

      for (var i = 0; i < 3; i++)
      {
        canvas.fill(_colors[i]);
        canvas.vertex(_points[i][0], _points[i][1], _points[i][2]);
      }

      canvas.endShape();   
  
      canvas.popMatrix();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      if (!_enableVertexColors)
      {
        var c = colorPicker.RandomColor();
        Colors(c, c, c);
      }
      else 
      {
        Colors(colorPicker.RandomColor(), colorPicker.RandomColor(), colorPicker.RandomColor());
      }
    }
  }
