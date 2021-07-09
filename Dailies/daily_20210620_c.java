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

public class daily_20210620_c extends AbstractDaily 
{
  private boolean _enableWarp;
  private float _warpDistance;
  private float _noiseInputMultiplierX;
  private float _noiseInputMultiplierY;
  private float _ellipseRadiusMultiplier;
  private float _innerEllipseRadius;
  private float _outerEllipseRadius;

  public daily_20210620_c() 
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
    // Canvas.background(255);

    var minradius = _innerEllipseRadius - (_innerEllipseRadius * _ellipseRadiusMultiplier);
    var maxradius = _outerEllipseRadius - (_outerEllipseRadius * _ellipseRadiusMultiplier);
    var yspan = Canvas.height - (Canvas.height * _ellipseRadiusMultiplier);

    DrawEllipses(16, minradius, maxradius, Canvas.width / 2, yspan);

    if (_enableWarp) {
      Warp();
    }

    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }

  private void DrawEllipses(int count, double minradius, double maxradius, int x, double yspan) {
    var r = 167;
    var g = 6;
    var b = 32;
    var deepRed = color(r, g, b);

    for (var i = 0; i < count; i++) {
      var yoffset = (Canvas.height / 2) - (yspan / 2);
      var radius = RandomGenerator.Value(minradius, maxradius);
      var y = (float) RandomGenerator.Value(yoffset, yoffset + yspan);

      var d = Easing.OutCubic(1D - Math.Abs((Canvas.height / 2) - y) / (double) (Canvas.height / 2));

      radius *= d;

      Canvas.stroke(0);
      Canvas.strokeWeight((float) (radius * 0.1D));
      Canvas.fill(deepRed);
      Canvas.ellipse(Canvas.width / 2, y, (float) radius, (float) radius);
    }
  }

  private void Warp() {
    Canvas.loadPixels();

    var temp = createImage(Canvas.width, Canvas.height, ARGB);

    temp.loadPixels();

    for (var y = 0; y < Canvas.height; y++) {
      for (var x = 0; x < Canvas.width; x++) {
        var angle = NoiseGenerator.Value(x, y) * Math.Pi * 2D;

        var xx = x + (int) (Math.Cos(angle) * _warpDistance);
        var yy = y + (int) (Math.Sin(angle) * _warpDistance);

        if (xx < 0) {
          xx = 0;
        } else if (xx >= Canvas.width) {
          xx = Canvas.width - 1;
        }

        if (yy < 0) {
          yy = 0;
        } else if (yy >= Canvas.height) {
          yy = Canvas.height - 1;
        }

        var color = Canvas.pixels[(int) (yy * Canvas.height + xx)];

        temp.pixels[(int) (y * temp.height + x)] = color;
      }
    }

    temp.updatePixels();

    Canvas.image(temp, 0, 0);

    Canvas.updatePixels();
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