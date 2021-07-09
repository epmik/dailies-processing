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

public class daily_20210624_b extends AbstractDaily 
{
  private boolean _enableWarp;
  private float _warpStrength;
  private int _minCloudSize;
  private int _maxCloudSize;
  private int _lineWidth;
  private int _patchCount;
  private float _noiseInputMultiplierX;
  private float _noiseInputMultiplierY;
  private PImage _backgroundColorPick;
  private PGraphics _backgroundCanvas;
  private PImage _cloudColorPick;
  private OpenSimplexNoiseGenerator _backgroundNoiseGenerator;

  public daily_20210624_b() 
  {
    super();

    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 4;//4.0;
    _enableWarp = true;
    _warpStrength = (float) (50 * SketchResolutionMultiplier);
    _lineWidth = (int) (10 * SketchResolutionMultiplier);
    _patchCount = 16;
    _noiseInputMultiplierX = (float)(0.0040 * (1.0 / SketchResolutionMultiplier));
    _noiseInputMultiplierY = (float)(0.0040 * (1.0 / SketchResolutionMultiplier));
    _minCloudSize = (int)(50 * SketchResolutionMultiplier);
    _maxCloudSize = _minCloudSize * 5;
  }

  @Override
  public void setup() 
  {
    super.setup();

    _backgroundCanvas = createGraphics(Canvas.width, Canvas.height, P2D);

    NoiseGenerator = new OpenSimplexNoiseGenerator(0);
    NoiseGenerator.Octaves(4);
    NoiseGenerator.ClampValues(true);
    NoiseGenerator.AllowNegativeValues(false);
    NoiseGenerator.InputMultiplier(_noiseInputMultiplierX, _noiseInputMultiplierY, 1.0);

    _backgroundNoiseGenerator = new OpenSimplexNoiseGenerator(0);
    _backgroundNoiseGenerator.Octaves(1);
    _backgroundNoiseGenerator.ClampValues(true);
    _backgroundNoiseGenerator.AllowNegativeValues(false);
    _backgroundNoiseGenerator.InputMultiplier(0.00020F, 0.00020F, 1.0);

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

    Gui.addSlider("_patchCount").setPosition(guiX, guiY).setLabel("Patch count").setRange(1, 100)
        .setValue(_patchCount);

    guiY += guiOffsetY;
  
    _cloudColorPick = loadImage(PathCombine(ClassFolderInput(), "clouds.png"));
    _cloudColorPick.loadPixels();

    _backgroundColorPick = loadImage(PathCombine(ClassFolderInput(), "background.png"));
    _backgroundColorPick.loadPixels();

    DrawBackgroundGradient(_backgroundCanvas, _backgroundNoiseGenerator);

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

    Canvas.image(_backgroundCanvas, 0, 0, _backgroundCanvas.width, _backgroundCanvas.height);

    var canvasPadding = (int)(4 * SketchResolutionMultiplier);// Canvas.width / 16;

    var _cloudColorPickRow = RandomGenerator.Value(0, _cloudColorPick.height);

    for (var p = 0; p < _patchCount; p++) 
    {
      var x = RandomGenerator.Value(canvasPadding, Canvas.width - canvasPadding);
      var y = RandomGenerator.Value(canvasPadding, Canvas.height / 3 * 2);

      var opacity = 1.0F - ((float) y / (float) Canvas.height);
      var size = opacity * 1.55F;

      DrawPatch(Canvas, 8, x, y, (int) (25 * SketchResolutionMultiplier * size), (int) (125 * SketchResolutionMultiplier * size), _cloudColorPickRow, opacity);
    }

    if (_enableWarp) {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void DrawBackgroundGradient(PGraphics canvas, INoiseGenerator noiseGenerator) 
  {
    canvas.beginDraw();

    canvas.clear();
    
    var row = RandomGenerator.Value(0, _backgroundColorPick.height);

    var top = _backgroundColorPick.pixels[row * _backgroundColorPick.width + 0];
    var bottom = _backgroundColorPick.pixels[row * _backgroundColorPick.width + 1];

    DrawGradient(canvas, top, bottom, 0, 0, canvas.width, canvas.height, false);

    Warp(canvas, _warpStrength * 4, noiseGenerator);

    canvas.endDraw();

    // SaveFrame(canvas);
  }
  
  private void DrawGradient(PGraphics graphics, int c1, int c2, int x, int y, float w, float h, Boolean xaxis) 
  {
    graphics.noFill();
  
    if (!xaxis) 
    {  // Top to bottom gradient
      for (var i = y; i <= y + h; i++) 
      {
        var inter = map(i, y, y+h, 0, 1);
        var c = lerpColor(c1, c2, inter);
        graphics.stroke(c);
        graphics.line(x, i, x+w, i);
      }
    }  
    else 
    {  // Left to right gradient
      for (var i = x; i <= x + w; i++) 
      {
        var inter = map(i, x, x+w, 0, 1);
        var c = lerpColor(c1, c2, inter);
        graphics.stroke(c);
        graphics.line(i, y, i, y+h);
      }
    }
  }
  
  private void DrawPatch(
      PGraphics graphics,
      int numberOfEllipses,
      int x, int y, 
      int minRadius,
      int maxRadius,
      int patchId,
      float opacity)
  {
    graphics.noStroke();

    graphics.ellipseMode(RADIUS);

    var border = (int) RandomGenerator.Value(minRadius * 0.5, maxRadius * 0.5);
    
    var rgba = Color20210703.IntToRgba(PickPaletteColor(patchId));
    var borderColor = color(rgba[0], rgba[1], rgba[2], (int) (opacity * 255));

    rgba = Color20210703.IntToRgba(PickPaletteColor(patchId));
    var ellipseColor = color(rgba[0], rgba[1], rgba[2], (int)Math.Clamp(opacity * opacity * 255, 0, 255));

    var rxy = new int[numberOfEllipses * 3];

    for (var e = 0; e < numberOfEllipses; e++) 
    {
      var radius = RandomGenerator.Value(minRadius, maxRadius);

      rxy[e * 3] = radius;
      rxy[e * 3 + 1] = x;
      rxy[e * 3 + 2] = y;

      var angle = RandomGenerator.Value(0, Math.Pi * 2.0);

      radius = RandomGenerator.Value(minRadius - border, maxRadius - border);

      x += (int)(Math.Cos(angle) * radius);
      y += (int)(Math.Sin(angle) * radius);
    }

    for (var e = 0; e < numberOfEllipses; e++) 
    {
      var radius = rxy[e * 3];
      x = rxy[e * 3 + 1];
      y = rxy[e * 3 + 2];
      
      graphics.fill(borderColor);
      graphics.ellipse(x, y, radius, radius);
    }

    for (var e = 0; e < numberOfEllipses; e++) 
    {
      var radius = rxy[e * 3];
      x = rxy[e * 3 + 1];
      y = rxy[e * 3 + 2];
      
      graphics.fill(ellipseColor);
      graphics.ellipse(x, y, radius - border, radius - border);
    }
  }
    
  private int PickPaletteColor(int row) 
  {
    var index = row * _cloudColorPick.width + (int)RandomGenerator.Value(_cloudColorPick.width);

    return _cloudColorPick.pixels[index];
  }
  
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

    DrawBackgroundGradient(_backgroundCanvas, _backgroundNoiseGenerator);
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