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

public class daily_20210625_b extends AbstractDaily 
{
  private boolean _warpSky;
  private boolean _warpLand;
  private float _warpSkyStrength;
  private float _warpLandStrength;
  private int _patchCount;
  private float _noiseInputMultiplierX;
  private float _noiseInputMultiplierY;
  private PImage _backgroundColorPick;
  private PGraphics _backgroundCanvas;
  private PGraphics _foregroundCanvas;
  private PImage _cloudColorPick;
  private OpenSimplexNoiseGenerator _backgroundNoiseGenerator;

  public daily_20210625_b() 
  {
    super();

    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 1.0;
    _warpSky = true;
    _warpLand = true;
    _warpSkyStrength = (float) (50 * SketchResolutionMultiplier);
    _warpLandStrength = (float) (20 * SketchResolutionMultiplier);
    _patchCount = 16;
    _noiseInputMultiplierX = (float)(0.0040 * (1.0 / SketchResolutionMultiplier));
    _noiseInputMultiplierY = (float)(0.0040 * (1.0 / SketchResolutionMultiplier));
  }

  @Override
  public void setup() 
  {
    super.setup();

    _backgroundCanvas = createGraphics(Canvas.width, Canvas.height, P2D);
    _foregroundCanvas = createGraphics(Canvas.width, Canvas.height, P2D);

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

    Gui.addToggle("_warpSky").setPosition(guiX, guiY).setLabel("Warp sky").setValue(_warpSky);

    guiY += guiOffsetY;

    Gui.addSlider("_warpSkyStrength").setPosition(guiX, guiY).setLabel("Warp sky strength").setRange(0, (int)(ScreenWidth * SketchResolutionMultiplier))
        .setValue((float) _warpSkyStrength);

    guiY += guiOffsetY;

    Gui.addToggle("_warpLand").setPosition(guiX, guiY).setLabel("Warp land").setValue(_warpLand);

    guiY += guiOffsetY;

    Gui.addSlider("_warpLandStrength").setPosition(guiX, guiY).setLabel("Warp land strength").setRange(0, (int)(ScreenWidth * SketchResolutionMultiplier))
        .setValue((float) _warpLandStrength);

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

    var canvasPadding = (int) (4 * SketchResolutionMultiplier);// Canvas.width / 16;

    var cloudColorPickRow = RandomGenerator.Value(0, _cloudColorPick.height);

    for (var p = 0; p < _patchCount; p++) 
    {
      var x = RandomGenerator.Value(canvasPadding, Canvas.width - canvasPadding);
      var y = RandomGenerator.Value(canvasPadding, Canvas.height / 3 * 2);

      var size = 1.55F * (1.0F - ((float) y / (float) Canvas.height)) * SketchResolutionMultiplier;

      DrawPatch(Canvas, 8, x, y, (int) (25 * size), (int) (125 * size), cloudColorPickRow);
    }

    Canvas.endDraw();

    if (_warpSky) {
      Warp(Canvas, _warpSkyStrength, NoiseGenerator);
    }

    DrawLandscape(_foregroundCanvas, NoiseGenerator, cloudColorPickRow);

    if (_warpLand) {
      Warp(_foregroundCanvas, _warpLandStrength, NoiseGenerator);
    }

    Canvas.beginDraw();

    Canvas.image(_foregroundCanvas, 0, 0);

    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void DrawLandscape(PGraphics canvas, INoiseGenerator noiseGenerator, int colorRow)
  {
    var t = noiseGenerator.AllowNegativeValues();

    noiseGenerator.AllowNegativeValues(true);

    var xsize = 80;
    var ysize = 40;
    var xoffset = 80;
    var yoffset = 20;
    var y = (int) (canvas.height / 4.0 * 3.0);

    DrawLandscapeBackground(canvas, noiseGenerator, colorRow);

    canvas.beginDraw();

    while (y < canvas.height + 40) 
    {
      var x = (int) (-xsize * xsize);

      canvas.beginShape(QUAD_STRIP);

      DrawTwoOffsetVertices(x, y, xsize, ysize, xoffset, yoffset, colorRow, canvas, noiseGenerator);

      while (x < canvas.width + xsize + xsize) 
      {
        x += xsize;

        DrawTwoOffsetVertices(x, y, xsize, ysize, xoffset, yoffset, colorRow, canvas, noiseGenerator);
      }

      canvas.endShape();
      
      y += ysize;

      xsize += xsize;
      ysize += ysize;

      xoffset += xoffset;
      yoffset += yoffset;
    }

    canvas.endDraw();

    noiseGenerator.AllowNegativeValues(t);
  }
  
  private void DrawLandscapeBackground(PGraphics canvas, INoiseGenerator noiseGenerator, int colorRow)
  {
    var t = noiseGenerator.AllowNegativeValues();

    noiseGenerator.AllowNegativeValues(true);

    var xsize = 80;
    var xoffset = 80;
    var yoffset = 40;
    var y = (int) (canvas.height / 4.0 * 3.0);

    canvas.beginDraw();

    canvas.clear();

    var x = (int) (-xsize * xsize);

    canvas.beginShape(QUAD_STRIP);

    var color = PickEllipseColor(colorRow);

    var xo = (int)(noiseGenerator.Value(x, y) * xoffset);
    var yo = (int)(noiseGenerator.Value(x, y + 1.3) * yoffset);

    canvas.noStroke();

    canvas.fill(color);

    canvas.vertex(x + xo, y + yo);
    canvas.vertex(x + xo, canvas.height);

    while (x < canvas.width + xsize + xsize) 
    {
      x += xsize;

      canvas.vertex(x + xo, y + yo);
      canvas.vertex(x + xo, canvas.height);
    }

    canvas.endShape();

    canvas.endDraw();

    noiseGenerator.AllowNegativeValues(t);
  }
  
  private void DrawTwoOffsetVertices(
    int x, int y, 
    int xsize, int ysize,
    int xoffset, int yoffset,
    int colorRow,
    PGraphics canvas, INoiseGenerator noiseGenerator) 
  {
    var color = PickEllipseColor(colorRow);

    var xo = (int)(noiseGenerator.Value(x, y) * xoffset);
    var yo = (int)(noiseGenerator.Value(x, y + 1.3) * yoffset);

    canvas.noStroke();

    canvas.fill(color);

    canvas.vertex(x + xo, y + yo);

    y += yoffset;

    xo = (int)(noiseGenerator.Value(x, y) * xoffset);
    yo = (int)(noiseGenerator.Value(x, y + 1.3) * yoffset);

    color = PickEllipseColor(colorRow);

    canvas.fill(color);

    canvas.vertex(x + xo, y + yo);
}
  
  private void DrawBackgroundGradient(PGraphics canvas, INoiseGenerator noiseGenerator) 
  {
    canvas.beginDraw();

    canvas.clear();

    canvas.endDraw();
    
    var row = RandomGenerator.Value(0, _backgroundColorPick.height);

    var top = _backgroundColorPick.pixels[row * _backgroundColorPick.width + 0];
    var bottom = _backgroundColorPick.pixels[row * _backgroundColorPick.width + 1];

    DrawGradient(canvas, top, bottom, 0, 0, canvas.width, canvas.height, false);

    if (_warpSky) {
      Warp(canvas, _warpSkyStrength * 2, noiseGenerator);
    }
  }
  
  private void DrawGradient(PGraphics canvas, int c1, int c2, int x, int y, float w, float h, Boolean xaxis) 
  {
    canvas.beginDraw();

    canvas.noFill();
  
    if (!xaxis) 
    {  // Top to bottom gradient
      for (var i = y; i <= y + h; i++) 
      {
        var inter = map(i, y, y+h, 0, 1);
        var c = lerpColor(c1, c2, inter);
        canvas.stroke(c);
        canvas.line(x, i, x+w, i);
      }
    }  
    else 
    { // Left to right gradient
      for (var i = x; i <= x + w; i++) {
        var inter = map(i, x, x + w, 0, 1);
        var c = lerpColor(c1, c2, inter);
        canvas.stroke(c);
        canvas.line(i, y, i, y + h);
      }
    }
    
    canvas.endDraw();
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

    var border = (int) RandomGenerator.Value(minRadius * 0.5, maxRadius * 0.5);

    var borderRgba = Color20210703.IntToRgba(PickEllipseColor(patchId));

    var ellipseRgba = Color20210703.IntToRgba(PickEllipseColor(patchId));

    var rxy = new int[numberOfEllipses * 3];

    for (var e = 0; e < numberOfEllipses; e++) 
    {
      var radius = RandomGenerator.Value(minRadius, maxRadius);

      rxy[e * 3] = radius;
      rxy[e * 3 + 1] = x;
      rxy[e * 3 + 2] = y;

      var angle = RandomGenerator.Value(0, Math.Pi * 2.0);

      radius = RandomGenerator.Value(minRadius - border, maxRadius - border);

      x += (int) (Math.Cos(angle) * radius);
      y += (int) (Math.Sin(angle) * radius);
    }

    for (var e = 0; e < numberOfEllipses; e++) 
    {
      var radius = rxy[e * 3];
      x = rxy[e * 3 + 1];
      y = rxy[e * 3 + 2];

      var opacity = CalculateOpacity(y, graphics.height);

      var borderColor = color(borderRgba[0], borderRgba[1], borderRgba[2], (int) (opacity * 255));

      graphics.fill(borderColor);
      graphics.ellipse(x, y, radius, radius);
    }

    for (var e = 0; e < numberOfEllipses; e++) 
    {
      var radius = rxy[e * 3];
      x = rxy[e * 3 + 1];
      y = rxy[e * 3 + 2];

      var opacity = CalculateOpacity(y, graphics.height);

      var ellipseColor = color(ellipseRgba[0], ellipseRgba[1], ellipseRgba[2], (int) Math.Clamp(opacity * opacity * 255, 0, 255));

      graphics.fill(ellipseColor);
      graphics.ellipse(x, y, radius - border, radius - border);
    }
  }
  
  private float CalculateOpacity(int y, int h) 
  {
    return (float)Math.Min(0.75, Math.Max(0.25, 1.0F - ((float) y / (float) h)));
  }
    
  private int PickEllipseColor(int row) 
  {
    var index = row * _cloudColorPick.width + (int)RandomGenerator.Value(_cloudColorPick.width);

    return _cloudColorPick.pixels[index];
  }
  
  private void Warp(PGraphics canvas, float warpStrength, INoiseGenerator noiseGenerator) 
  {
    canvas.beginDraw();

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

    // SaveFrame(temp);

    // SaveFrame(canvas);

    canvas.clear();

    canvas.image(temp, 0, 0, temp.width, temp.height);

    canvas.endDraw();

    // SaveFrame(canvas);
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