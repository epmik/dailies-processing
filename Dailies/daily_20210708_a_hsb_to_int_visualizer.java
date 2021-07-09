package Dailies;

// import java.util.ArrayList;
// import java.util.List;
import processing.core.*;
import processing.event.MouseEvent;

import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.util.ElementScanner6;

import org.eclipse.collections.api.list.primitive.DoubleList;

import Geometry.Icosahedron;
import Geometry.Point3D;
//import processing.opengl.*;
import controlP5.*;
import peasy.*;

import Procedural.*;
import Procedural.Interfaces.INoiseGenerator;
import Procedural.Interfaces.IRandomGenerator;
import Utility.Color;
// import Utility.*;
import Utility.Color20210703;
import Utility.ColorPicker;
import Utility.Easing;
import Utility.Lists;
import Utility.Math;
import Utility.Easing.IEasingFunction;

public class daily_20210708_a_hsb_to_int_visualizer extends AbstractDaily {

  public daily_20210708_a_hsb_to_int_visualizer() {
    super();

    Renderer = P2D;
    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 1.0;
  }

  @Override
  public void setup() {

    super.setup();

    SetupGui();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  private void SetupGui() {
    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;
  }

  @Override
  public void Update(double elapsedTime) {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
    NoiseGenerator.ReSeed(NoiseGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) {

    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(255);

    Canvas.strokeWeight(12);
    Canvas.stroke(0);
    Canvas.noFill();

    var weight = 16;
    var maxradius = (int)(Canvas.height * 0.2);
    var x = (int)(Canvas.width / 2);
    var topy = (int)(((Canvas.height / 2) - maxradius) / 2) + (maxradius / 2);
    var bottomy = (int) (Canvas.height / 2) + topy;

    for (var i = 0; i < 90; i += 5) 
    {
      var angle = Math.ToRadians(i);
      var sin = Math.Cos(angle);
      var cos = Math.Sin(angle);
      var h = Color.DegreesToDouble(i);

      for (var s = 0.0; s <= 1.0; s += 0.1)
      {
        var r = (int) (maxradius * s);
        var hsb = Color.HsbToInt(h, s, 1.0);

        Canvas.strokeWeight((float)(weight));
        Canvas.stroke(hsb);
        Canvas.point((float)(x + sin * r), (float)(topy + cos * r));
      }

      for (var b = 0.0; b <= 1.0; b += 0.1)
      {
        var r = (int) (maxradius * b);
        var hsb = Color.HsbToInt(h, 1.0, b);

        Canvas.strokeWeight((float)(weight));
        Canvas.stroke(hsb);
        Canvas.point((float)(x + sin * r), (float)(bottomy + cos * r));
      }
    }

    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }

  public void keyPressed() {

    super.keyPressed();

    if (!PropagateMouseEvents) {
      return;
    }

    if (key == CODED) 
    { 
      switch (keyCode) 
      {
        case UP:
          break;
        case DOWN:
          break;
        default:
          break;
      }
    }
  }

  public void mouseReleased() 
  {
    super.mouseReleased();

    if (!PropagateMouseEvents) {
      return;
    }

    if (mouseButton == RIGHT) {

    }
  }
}