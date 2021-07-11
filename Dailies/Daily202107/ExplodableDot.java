package Dailies.Daily202107;

import processing.core.*;
import Utility.Color;
import Utility.ColorPicker;

  public class ExplodableDot extends AbstractExplodable
  {
    private float _weight = 1.0F;
    private int _color = Color.RgbToInt(0);

    public float Weight() {
      return _weight;
    }

    public void Weight(float weight) {
      _weight = weight;
    }

    public int Color() {
      return _color;
    }

    public void Color(int color) {
      _color = color;
    }

    public void Draw(PGraphics canvas) 
    {
      canvas.pushMatrix();

      canvas.rotateY(_longitude);
      canvas.rotateZ(_latitude);
      canvas.translate(_offset, 0, 0);

      canvas.beginShape(PConstants.POINTS);
  
      canvas.strokeWeight(_weight);
      canvas.stroke(_color);
      canvas.noFill();

      canvas.vertex(_x, _y, _z);

      canvas.endShape();   
  
      canvas.popMatrix();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      Color(colorPicker.RandomColor());
    }
  }
