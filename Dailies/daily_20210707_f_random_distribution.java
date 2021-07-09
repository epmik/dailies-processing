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

public class daily_20210707_f_random_distribution extends AbstractDaily {

  private List<Easing.IEasingFunction> _easingFunctionArrayList = Arrays.asList(Easing.Functions());
  private int _easingFunctionArrayListIndex = 0;

  public daily_20210707_f_random_distribution() {
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

    var easingFunction = _easingFunctionArrayList.get(_easingFunctionArrayListIndex);

    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(255);

    Canvas.strokeWeight(3);
    Canvas.stroke(0);
    Canvas.noFill();

    for (var i = 0; i < 25000; i++) 
    {
      Canvas.point(
        (float)Math.Map(easingFunction.Ease(RandomGenerator.Value()), 0, Canvas.width),
        (float)Math.Map(easingFunction.Ease(RandomGenerator.Value()), 0, Canvas.height));
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
          _easingFunctionArrayListIndex++;
          if(_easingFunctionArrayListIndex >= _easingFunctionArrayList.size())
          {
            _easingFunctionArrayListIndex = 0;
          }
          break;
        case DOWN:
        _easingFunctionArrayListIndex--;
        if(_easingFunctionArrayListIndex < 0)
        {
          _easingFunctionArrayListIndex = _easingFunctionArrayList.size() - 1;
        }
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