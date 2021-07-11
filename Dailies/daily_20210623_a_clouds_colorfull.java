package Dailies;

// import java.util.ArrayList;
// import java.util.List;
import processing.core.*;
import processing.event.MouseEvent;

import java.io.IOException;
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

public class daily_20210623_a_clouds_colorfull extends AbstractDaily 
{
  private boolean _enableWarp;
  private float _warpStrength;
  private int _lineWidth;
  private int _patchCount;
  private float _noiseInputMultiplierX;
  private float _noiseInputMultiplierY;
  private PImage _background;
  private PImage _palette;

  public daily_20210623_a_clouds_colorfull() 
  {
    super();

    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 2.0;
    _enableWarp = false;
    _warpStrength = (float) (50 * SketchResolutionMultiplier);
    _lineWidth = (int) (10 * SketchResolutionMultiplier);
    _patchCount = 8;
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

    Gui.addSlider("_patchCount").setPosition(guiX, guiY).setLabel("Patch count").setRange(1, 100)
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

    _palette = loadImage(ResolveInputFile("clouds.png"));
    _palette.loadPixels();

    _background = loadImage(ResolveInputFile("background.png"));
    _background.loadPixels();

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

    Canvas.background(PickBackgroundColor());

    var canvasPadding = 4;// Canvas.width / 16;

    for (var p = 0; p < _patchCount; p++)
    {
      var x = RandomGenerator.Value(canvasPadding, Canvas.width - canvasPadding);
      var y = RandomGenerator.Value(canvasPadding, Canvas.height / 3 * 2);

      DrawPatch(Canvas, 8, x, y, 50, 250, p);
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
      int numberOfEllipses,
      int x, int y, 
      int minRadius,
      int maxRadius,
      int patchId)
  {
    graphics.noStroke();

    graphics.ellipseMode(RADIUS);

    var border = (int)RandomGenerator.Value(minRadius * 0.5, maxRadius * 0.5);
    var borderColor = PickPaletteColor(patchId);
    var ellipseColor = PickPaletteColor(patchId);

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
    
  private int PickPaletteColor(int patchId) {

    var row = patchId % _palette.height;

    var index = row * _palette.width + (int)RandomGenerator.Value(_palette.width);

    return _palette.pixels[index];
  }
  
  private int PickBackgroundColor() 
  {
    return PickColor(_background);
  }
  
  private int PickPaletteColor() 
  {
    return PickColor(_palette);
  }
  
  private int PickColor(PImage image) {

    var index = (int)RandomGenerator.Value(image.pixels.length);

    return image.pixels[index];
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