package Dailies;

// import java.util.ArrayList;
// import java.util.List;
import processing.core.*;
import processing.event.MouseEvent;

import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.eclipse.collections.api.list.primitive.DoubleList;

//import processing.opengl.*;
import controlP5.*;

import Procedural.*;
// import Utility.*;
import Utility.Color20210703;
import Utility.Easing;
import Utility.Math;

public class daily_20210621_a_crack_tree_red_black extends AbstractDaily 
{
  private boolean _enableWarp;
  private float _warpDistance;
  private float _noiseInputMultiplierX;
  private float _noiseInputMultiplierY;
  private float _ellipseRadiusMultiplier;
  private float _innerEllipseRadius;
  private float _outerEllipseRadius;

  public daily_20210621_a_crack_tree_red_black() 
  {
    super();

    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 2.0;
    _enableWarp = true;
    _warpDistance = (float) (40 * SketchResolutionMultiplier);
    _noiseInputMultiplierX = 0.0020F;
    _noiseInputMultiplierY = 0.0015F;
    _ellipseRadiusMultiplier = 0.15F;
    _innerEllipseRadius = (float) (300 * SketchResolutionMultiplier);
    _outerEllipseRadius = (float) (400 * SketchResolutionMultiplier);
  }

  @Override
  public void setup() 
  {
    super.setup();

    NoiseGenerator = new OpenSimplexNoiseGenerator(0);
    NoiseGenerator.Octaves(4);
    NoiseGenerator.ClampValues(true);
    NoiseGenerator.AllowNegativeValues(false);
    NoiseGenerator.InputMultiplier(_noiseInputMultiplierX, _noiseInputMultiplierY, 1.0);

    RandomGenerator = new RandomGenerator(0L);

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    Gui.addToggle("_enableWarp").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_enableWarp);

    guiY += guiOffsetY;

    Gui.addSlider("_warpDistance").setPosition(guiX, guiY).setLabel("Warp distance").setRange(0, 200)
        .setValue((float) _warpDistance);

    guiY += guiOffsetY;

    Gui.addNumberbox("_noiseInputMultiplierX").setPosition(guiX, guiY).setLabel("Noise input multiplier X")
        .setRange(0.0001F, 0.4000F).setDecimalPrecision(4).setScrollSensitivity(0.0001F)
        .setValue(_noiseInputMultiplierX);

    guiY += guiOffsetY;

    Gui.addNumberbox("_noiseInputMultiplierY").setPosition(guiX, guiY).setLabel("Noise input multiplier Y")
        .setRange(0.0001F, 0.4000F).setDecimalPrecision(4).setScrollSensitivity(0.0001F)
        .setValue(_noiseInputMultiplierY);

    guiY += guiOffsetY;

    Gui.addNumberbox("_ellipseRadiusMultiplier").setPosition(guiX, guiY).setLabel("Ellipse radius multiplier")
        .setRange(-0.975F, 0.975F).setDecimalPrecision(3).setScrollSensitivity(0.025F)
        .setValue(_ellipseRadiusMultiplier);

    guiY += guiOffsetY;

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  @Override
  public void Update(double elapsedTime) 
  {
    NoiseGenerator.InputMultiplier(_noiseInputMultiplierX, _noiseInputMultiplierY, 1.0);
    RandomGenerator.ReSeed(RandomGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    var stepY = 10000;//(int)(Canvas.height / 10.0);
    var itemsPerStepY = 1;
    var scale = 1.00F;
    var scaleFactor = 1.0F;

    // var c = Canvas;
    var c = createGraphics(Canvas.width, Canvas.height, P2D);

    c.beginDraw();

    for (var y = (int) ((double) Canvas.height / 4D); y < Canvas.height; y += stepY) 
    {
      for (var i = 0; i < itemsPerStepY; i++) 
      {
        NoiseGenerator.ReSeed();
        // System.out.println(NoiseGenerator.Seed());
        NoiseGenerator.InputMultiplier(_noiseInputMultiplierX, _noiseInputMultiplierY, 1.0);

        c.clear();

        var x = c.width / 1; //(int) RandomGenerator.Value(0, c.width);

        var minradius = (_innerEllipseRadius - (_innerEllipseRadius * _ellipseRadiusMultiplier));
        var maxradius = (_outerEllipseRadius - (_outerEllipseRadius * _ellipseRadiusMultiplier));
        var yspan = (c.height - (c.height * _ellipseRadiusMultiplier));

        var s = scale * 0.1F;

        DrawEllipses(c, 16, minradius, maxradius, c.width / 2, yspan);

        if (_enableWarp) 
        {
          Warp(c);
        }

        SaveFrame(c);

        Canvas.image(c, 0, 0, Canvas.width, Canvas.height);

        // SaveFrame(Canvas);

        //DrawScaledToCanvas(c, 1.0f, 0, 0);
        //DrawScaledToCanvas(c, (float) RandomGenerator.Value(scale - s, scale + s), x - (int)(Canvas.width / 2.0), y - (int)(Canvas.height / 2.0));
      }

      scale *= scaleFactor;
    }
    
    c.endDraw();

    //image(c, 0, 0, ScreenWidth, ScreenHeight);

    Canvas.endDraw();

    // SaveFrame(Canvas);

    // SaveFrame(Canvas);

    //image(Canvas, 0, 0, Canvas.width, Canvas.height);
    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void DrawScaledToCanvas(PGraphics canvas, float scale, int x, int y) 
  {
    var w = (int) ((float)canvas.width * scale);
    var h = (int) ((float)canvas.height * scale);

    Canvas.image(canvas, x, y, w, h);

    // SaveFrame(Canvas);
  }

  private void DrawEllipses(PGraphics canvas, int count, double minradius, double maxradius, int x, double yspan) 
  {
    var r = 167;
    var g = 6;
    var b = 32;
    var deepRed = color(r, g, b);

    for (var i = 0; i < count; i++) 
    {
      var yoffset = (canvas.height / 2) - (yspan / 2);
      var radius = RandomGenerator.Value(minradius, maxradius);
      var y = (float) RandomGenerator.Value(yoffset, yoffset + yspan);

      var d = Easing.OutCubic(1D - Math.Abs((canvas.height / 2) - y) / (double) (canvas.height / 2));

      radius *= d;

      canvas.stroke(0);
      canvas.strokeWeight((float) (radius * 0.1D));
      canvas.fill(deepRed);
      canvas.ellipse(canvas.width / 2, y, (float) radius, (float) radius);
    }
  }

  private void Warp(PGraphics canvas) 
  {
    canvas.loadPixels();

    var temp = createImage(canvas.width, canvas.height, ARGB);

    temp.loadPixels();

    for (var y = 0; y < canvas.height; y++) 
    {
      for (var x = 0; x < canvas.width; x++) 
      {
        var angle = NoiseGenerator.Value(x, y) * Math.Pi * 2D;

        var xx = x + (int) (Math.Cos(angle) * _warpDistance);
        var yy = y + (int) (Math.Sin(angle) * _warpDistance);

        if (xx < 0) {
          xx = 0;
        } else if (xx >= canvas.width) {
          xx = canvas.width - 1;
        }

        if (yy < 0) {
          yy = 0;
        } else if (yy >= canvas.height) {
          yy = canvas.height - 1;
        }

        var color = canvas.pixels[(int) (yy * canvas.height + xx)];

        temp.pixels[(int) (y * temp.height + x)] = color;
      }
    }

    temp.updatePixels();

    canvas.image(temp, 0, 0, temp.width, temp.height);

    canvas.updatePixels();
  }

  public void mouseReleased() {
    
    super.mouseReleased();

    if (!PropagateMouseEvents) {
      return;
    }
  }

  public void keyPressed() {

    super.keyPressed();

    if (!PropagateKeyboardEvents) {
      return;
    }

    if (key == CODED) {
      switch (keyCode) {
        case CONTROL:
          ((Numberbox) Gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.001F);
          ((Numberbox) Gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.001F);
          break;
        case SHIFT:
          ((Numberbox) Gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.01F);
          ((Numberbox) Gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.01F);
          break;
        case DOWN:
          break;
        case LEFT:
          break;
        case RIGHT:
          break;
        case java.awt.event.KeyEvent.VK_F1:
          break;
        default:
          break;
      }
    }
  }

  public void keyReleased() {

    super.keyReleased();

    if (!PropagateKeyboardEvents && !(key != CODED && key == 's')) {
      return;
    }

    if (key == CODED) {
      switch (keyCode) {
        case CONTROL:
          ((Numberbox) Gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.0001F);
          ((Numberbox) Gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.0001F);
          break;
        case SHIFT:
          ((Numberbox) Gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.001F);
          ((Numberbox) Gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.001F);
          break;
        case UP:
          break;
        case DOWN:
          break;
        case LEFT:
          break;
        case RIGHT:
          break;
        case java.awt.event.KeyEvent.VK_F1:
          break;
        default:
          break;
      }
    }
  }
}