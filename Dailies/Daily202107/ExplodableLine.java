package Dailies.Daily202107;

import processing.core.*;
import Utility.Color;
import Utility.ColorPicker;

  public class ExplodableLine extends AbstractExplodable
  {
    protected float _length = 1.0F;
    protected float _weight = 1.0F;
    protected int _colorOrigin =  Color.RgbToInt(0, 0, 0);
    protected int _colorExtent = Color.RgbToInt(255, 255, 255);
    protected boolean _enableVertexColors;

    public float Length() {
      return _length;
    }

    public void Length(float length) {
      _length = length;
    }

    public boolean EnableVertexColors() {
      return _enableVertexColors;
    }

    public void EnableVertexColors(boolean enableVertexColors) {
      _enableVertexColors = enableVertexColors;
    }

    public float Weight() {
      return _weight;
    }

    public void Weight(float weight) {
      _weight = weight;
    }

    public int ColorOrigin() {
      return _colorOrigin;
    }

    public void ColorOrigin(int colorOrigin) {
      _colorOrigin = colorOrigin;
    }

    public int ColorExtent() {
      return _colorExtent;
    }

    public void ColorExtent(int colorExtent) {
      _colorExtent = colorExtent;
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
      canvas.vertex(_x + _length, _y, _z);

      canvas.endShape();   
  
      canvas.popMatrix();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      if (_enableVertexColors)
      {
        var c = colorPicker.RandomColor();
        ColorOrigin(c);
        ColorExtent(c);
      }
      else {
        ColorOrigin(colorPicker.RandomColor());
        ColorExtent(colorPicker.RandomColor());
      }
    }
  }
