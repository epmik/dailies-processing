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
import Procedural.Interfaces.INoiseGenerator;
// import Utility.*;
import Utility.Color20210703;
import Utility.Easing;
import Utility.Math;

public class daily_20210622_d extends AbstractDaily 
{
  private boolean _enableWarp;
  private float _warpStrength;
  private int _lineWidth;
  private int _patchCount;
  private float _noiseInputMultiplierX;
  private float _noiseInputMultiplierY;
  private PImage _palette;

  public daily_20210622_d() 
  {
    super();

    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 2.0;
    _enableWarp = true;
    _warpStrength = (float) (265 * SketchResolutionMultiplier);
    _lineWidth = (int) (10 * SketchResolutionMultiplier);
    _patchCount = 12;
    _noiseInputMultiplierX = 0.0020F;
    _noiseInputMultiplierY = 0.0020F;
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

    Gui.addSlider("_warpStrength").setPosition(guiX, guiY).setLabel("Warp strength").setRange(0, (int)(ScreenWidth * SketchResolutionMultiplier))
        .setValue((float) _warpStrength);

    guiY += guiOffsetY;

    Gui.addSlider("_lineWidth").setPosition(guiX, guiY).setLabel("Line width").setRange(3, (int)(100 * SketchResolutionMultiplier))
        .setValue(_lineWidth);

    guiY += guiOffsetY;

    Gui.addSlider("patchCount").setPosition(guiX, guiY).setLabel("Patch count").setRange(1, 100)
        .setValue(_patchCount);

    guiY += guiOffsetY;

    // Gui.addNumberbox("_noiseInputMultiplierX").setPosition(guiX, guiY).setLabel("Noise input multiplier X")
    //     .setRange(0.0001F, 0.4000F).setDecimalPrecision(4).setScrollSensitivity(0.0001F)
    //     .setValue(_noiseInputMultiplierX);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_noiseInputMultiplierY").setPosition(guiX, guiY).setLabel("Noise input multiplier Y")
    //     .setRange(0.0001F, 0.4000F).setDecimalPrecision(4).setScrollSensitivity(0.0001F)
    //     .setValue(_noiseInputMultiplierY);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_ellipseRadiusMultiplier").setPosition(guiX, guiY).setLabel("Ellipse radius multiplier")
    //     .setRange(-0.975F, 0.975F).setDecimalPrecision(3).setScrollSensitivity(0.025F)
    //     .setValue(_ellipseRadiusMultiplier);

    // guiY += guiOffsetY;

    _palette = loadImage("input/daily_20210622/palette.png");
    _palette.loadPixels();

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

    Canvas.background(PickPaletteColor());

    var canvasPadding = 4;// Canvas.width / 16;
    var minPatchWidth = Canvas.width / 6;
    var maxPatchWidth = Canvas.width / 2;
    var minPatchHeight = Canvas.width / 6;
    var maxPatchHeight = Canvas.height / 2;
    var minLineWidth = _lineWidth - (_lineWidth / 2);
    var maxLineWidth = _lineWidth + (_lineWidth / 2);
    var minOffsetX = _lineWidth;
    var maxOffsetX = _lineWidth * 3;
    var minOffsetY = _lineWidth;
    var maxOffsetY = _lineWidth * 3;

    for (var p = 0; p < _patchCount; p++)
    {
      var w = RandomGenerator.Value(minPatchWidth, maxPatchWidth);
      var h = RandomGenerator.Value(minPatchHeight, maxPatchHeight);

      var x = RandomGenerator.Value(canvasPadding, Canvas.width - w - canvasPadding);
      var y = RandomGenerator.Value(canvasPadding, Canvas.height - h - canvasPadding);

      DrawPatch(Canvas, x, y, w, h, RandomGenerator.Boolean(), RandomGenerator.Value(minLineWidth, maxLineWidth),
          RandomGenerator.Value(minOffsetX, maxOffsetX), RandomGenerator.Value(minOffsetY, maxOffsetY), p);
    }
    
    if (_enableWarp) 
    {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void DrawPatch(
      PGraphics graphics, 
      int x, int y, 
      int w, int h, 
      boolean horizontal,
      int lineWidth,
      int maxOffsetX,
      int maxOffsetY,
      int patchId)
  {
    graphics.noStroke();

    if (horizontal)
    {

      for (var i = y; i < y + h; i += lineWidth) 
      {
        var offsetX = (int) RandomGenerator.Value(-maxOffsetX, maxOffsetX);
        var offsetY = (int) RandomGenerator.Value(-maxOffsetY, maxOffsetY);

        graphics.fill(PickPaletteColor(patchId));
        graphics.rect(x + offsetX, i + offsetY, w, lineWidth);
      }
      
      return;
    }
    
    for (var i = x; i < x + w; i += lineWidth) 
    {
      var offsetX = (int)RandomGenerator.Value(-maxOffsetX, maxOffsetX);
      var offsetY = (int)RandomGenerator.Value(-maxOffsetY, maxOffsetY);
  
      graphics.fill(PickPaletteColor(patchId));
      graphics.rect(i + offsetX, y + offsetY, lineWidth, h);
    }
  }
  
  private int PickPaletteColor(int patchId) {

    var row = patchId % _palette.height;

    var index = row * _palette.width + (int)RandomGenerator.Value(_palette.width);

    return _palette.pixels[index];
  }
  
  private int PickPaletteColor() {

    var index = (int)RandomGenerator.Value(_palette.pixels.length);

    return _palette.pixels[index];
  }

  // private void DrawEllipses(PGraphics canvas, int count, double minradius, double maxradius, int x, double yspan) 
  // {
  //   var r = 167;
  //   var g = 6;
  //   var b = 32;
  //   var deepRed = color(r, g, b);

  //   for (var i = 0; i < count; i++) 
  //   {
  //     var yoffset = (canvas.height / 2) - (yspan / 2);
  //     var radius = RandomGenerator.Value(minradius, maxradius);
  //     var y = (float) RandomGenerator.Value(yoffset, yoffset + yspan);

  //     var d = Easing.EaseOutCubic(1D - Math.abs((canvas.height / 2) - y) / (double) (canvas.height / 2));

  //     radius *= d;

  //     canvas.stroke(0);
  //     canvas.strokeWeight((float) (radius * 0.1D));
  //     canvas.fill(deepRed);
  //     canvas.ellipse(canvas.width / 2, y, (float) radius, (float) radius);
  //   }
  // }

  private void Warp(PGraphics canvas, float warpStrength, INoiseGenerator noiseGenerator) 
  {
    canvas.loadPixels();

    var temp = createImage(canvas.width, canvas.height, ARGB);

    temp.loadPixels();

    for (var y = 0; y < canvas.height; y++) 
    {
      for (var x = 0; x < canvas.width; x++) 
      {
        var angle = noiseGenerator.Value(x, y) * Math.Pi * 2D;

        var xx = x + (int) (Math.Cos(angle) * warpStrength);
        var yy = y + (int) (Math.Sin(angle) * warpStrength);

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