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

import Geometry.Icosahedron;
import Geometry.Point3D;
//import processing.opengl.*;
import controlP5.*;
// import peasy.*;

import Procedural.*;
import Procedural.Interfaces.INoiseGenerator;
// import Utility.*;
import Utility.Color20210703;
import Utility.Easing;
import Utility.Math;

public class daily_20210630_f extends AbstractDaily 
{
  private boolean _enableWarp = true;
  private int _warpStrength = 50;

  private boolean _blackBackground = false;
  private boolean _enableFillColor = true;
  private boolean _enableStrokeColor = false;
  private boolean _enableFixedRadius = false;

  private int _numberOfTriangles = 32;
  private int _strokeWeight = 0;

  private int _minRadius = (int)(ScreenWidth * 0.5 * SketchResolutionMultiplier * 0.35);
  private int _maxRadius = (int) (ScreenWidth * 0.5 * SketchResolutionMultiplier * 0.90);
  
  private PImage _colorPick;
  private int _colorPickRow;

  public daily_20210630_f() 
  {
    super();

    Renderer = P3D;
    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 1.0;

    // _camera = new PeasyCam(this, 1000);
    // _camera.setMinimumDistance(0.01);
    // _camera.setMaximumDistance(5000);
    // _noiseInputMultiplierX = (float)(0.0040 * (1.0 / SketchResolutionMultiplier));
    // _noiseInputMultiplierY = (float)(0.0040 * (1.0 / SketchResolutionMultiplier));
  }

  @Override
  public void setup() 
  {
    super.setup();

    // NoiseGenerator = new OpenSimplexNoiseGenerator(0);
    // NoiseGenerator.Octaves(8);
    // NoiseGenerator.ClampValues(true);
    // NoiseGenerator.AllowNegativeValues(false);
    // NoiseGenerator.InputMultiplier(_xNoiseMultiplier, _yNoiseMultiplier, 1.0);

    // RandomGenerator = new RandomGenerator(0L);

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    // Gui.addToggle("_enableColor").setPosition(guiX, guiY).setLabel("Enable Color").setValue(_enableColor);

    // guiY += guiOffsetY;

    Gui.addToggle("_blackBackground").setPosition(guiX, guiY).setLabel("Black Background").setValue(_blackBackground);

    guiY += guiOffsetY;

    Gui.addToggle("_enableFillColor").setPosition(guiX, guiY).setLabel("Enable Fill Color").setValue(_enableFillColor);

    guiY += guiOffsetY;

    // Gui.addToggle("_enableStrokeColor").setPosition(guiX, guiY).setLabel("Enable Stroke Color").setValue(_enableStrokeColor);

    // guiY += guiOffsetY;

    Gui.addToggle("_enableFixedRadius").setPosition(guiX, guiY).setLabel("Enable Fixed Radius").setValue(_enableFixedRadius);

    guiY += guiOffsetY;

    Gui.addToggle("_enableWarp").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_enableWarp);

    guiY += guiOffsetY;

    // Gui.addToggle("_enableWarp").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_enableWarp);

    // guiY += guiOffsetY;

    // Gui.addSlider("_warpStrength").setPosition(guiX, guiY).setLabel("Warp strength").setRange(0, Canvas.width)
    //     .setValue(_warpStrength);

    // guiY += guiOffsetY;

    Gui.addNumberbox("_numberOfTriangles").setPosition(guiX, guiY).setLabel("Number of Triangles")
        .setRange(1, 100)
        .setScrollSensitivity(1)
        .setMultiplier(1)
        .setValue(_numberOfTriangles);

    guiY += guiOffsetY;

    Gui.addNumberbox("_strokeWeight").setPosition(guiX, guiY).setLabel("Stroke Weight")
        .setRange(0, 100)
        .setScrollSensitivity(1)
        .setMultiplier(1)
    .setValue(_strokeWeight);

    guiY += guiOffsetY;

    Gui.addNumberbox("_warpStrength").setPosition(guiX, guiY).setLabel("Warp Strength")
        .setRange(0, 100)
        .setScrollSensitivity(1)
        .setMultiplier(1)
    .setValue(_warpStrength);

    guiY += guiOffsetY;

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_xNoiseMultiplier").setPosition(guiX, guiY).setLabel("X position noise multiplier")
    //     .setRange(0.0001F, 2.0000F)
    //     .setDecimalPrecision(4)
    //     .setScrollSensitivity(0.0001F)
    //     .setMultiplier(0.01F)
    //     .setValue(_xNoiseMultiplier);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_yNoiseMultiplier").setPosition(guiX, guiY).setLabel("Y position noise multiplier")
    //     .setRange(0.0001F, 2.0000F)
    //     .setDecimalPrecision(4)
    //     .setScrollSensitivity(0.0001F)
    //     .setMultiplier(0.01F)
    //     .setValue(_yNoiseMultiplier);

    // guiY += guiOffsetY;

    // Gui.addSlider("_yPositionOffsetStrenth").setPosition(guiX, guiY).setLabel("Y position offset strenth").setRange(0.0F, (float)(ScreenWidth * SketchResolutionMultiplier))
    //     .setValue(_yPositionOffsetStrenth);

    // guiY += guiOffsetY;

    // Gui.addSlider("_yPositionNoiseMultiplier").setPosition(guiX, guiY).setLabel("Y position noise multiplier")
    //     .setRange(0.0001F, 0.9000F)
    //     .setDecimalPrecision(5)
    //     .setScrollSensitivity(0.0001F)
    //     .setValue(_yPositionNoiseMultiplier);

    // guiY += guiOffsetY;

    // Gui.addSlider("_zPositionOffsetStrenth").setPosition(guiX, guiY).setLabel("Z position offset strenth").setRange(0.0F, (float)(ScreenWidth * SketchResolutionMultiplier))
    //     .setValue(_zPositionOffsetStrenth);

    // guiY += guiOffsetY;

    // Gui.addSlider("_zPositionNoiseMultiplier").setPosition(guiX, guiY).setLabel("Z position noise multiplier")
    //     .setRange(0.0001F, 0.9000F)
    //     .setDecimalPrecision(5)
    //     .setScrollSensitivity(0.0001F)
    //     .setValue(_zPositionNoiseMultiplier);

    // guiY += guiOffsetY;

    // Gui.addToggle("_warpLand").setPosition(guiX, guiY).setLabel("Warp land").setValue(_warpLand);

    // guiY += guiOffsetY;

    // Gui.addSlider("_warpLandStrength").setPosition(guiX, guiY).setLabel("Warp land strength").setRange(0, (int)(ScreenWidth * SketchResolutionMultiplier))
    //     .setValue((float) _warpLandStrength);

    // guiY += guiOffsetY;

    // Gui.addSlider("_patchCount").setPosition(guiX, guiY).setLabel("Patch count").setRange(1, 100)
    //     .setValue(_patchCount);

    // guiY += guiOffsetY;
  
    // _backgroundColorPick = loadImage(PathCombine(ClassFolderInput(), "background.png"));
    // _backgroundColorPick.loadPixels();

    // DrawBackgroundGradient(_backgroundCanvas, _backgroundNoiseGenerator);

     _colorPick = loadImage(ResolveInputFile("colors.png"));
     _colorPick.loadPixels();

     _colorPickRow = RandomGenerator.Value(0, _colorPick.height);

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  @Override
  public void Update(double elapsedTime) 
  {
    // NoiseGenerator.InputMultiplier(_xNoiseMultiplier, _yNoiseMultiplier, 1.0);
    RandomGenerator.ReSeed(RandomGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    // float fov = (float)(Math.PI / 3);
    // float cameraZ = (float)((Canvas.height / 2.0) / Math.tan(fov / 2.0));
    // Canvas.perspective(fov, (float) (Canvas.width) / (float) (Canvas.height), cameraZ / 10.0F, cameraZ * 10.0F);
    // Canvas.ortho(-Canvas.width/2, Canvas.width/2, -Canvas.height/2, Canvas.height/2);

    Canvas.beginDraw();

    Canvas.blendMode(REPLACE);

    Canvas.clear();
    Canvas.background(_blackBackground ? 0 : 255);

    DrawTriangles(Canvas);

    Canvas.endDraw();

    if (_enableWarp) {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void DrawTriangles(PGraphics canvas) 
  {
    var radius = RandomGenerator.Value(_enableFixedRadius ? _maxRadius : _minRadius, _maxRadius);

    var points = new Point3D[2];

    for (var i = 0; i < points.length; i++) {
      points[i] = RandomPoint(radius, canvas);
    }

    for (var i = 0; i < _numberOfTriangles; i++) 
    {
      points = DrawTriangle(points[0], points[1], canvas);
    }
  }
  
  private Point3D RandomPoint(int radius, PGraphics canvas)
  {
    var angle = RandomGenerator.Value(0, Math.Pi * 2.0);

    return new Point3D(
      (canvas.width / 2) + Math.Cos(angle) * radius,
      (canvas.height / 2) + Math.Sin(angle) * radius, 
      1);
  }
  
  private Point3D[] DrawTriangle(Point3D p1, Point3D p2, PGraphics canvas) 
  {
    var radius = RandomGenerator.Value(_enableFixedRadius ? _maxRadius : _minRadius, _maxRadius);
      
    var p3 = RandomPoint(radius, canvas);

    canvas.strokeJoin(BEVEL);
    canvas.strokeWeight(_strokeWeight);
    canvas.stroke(255);
    if (_enableFillColor)
    {
      canvas.fill(PickFillColor());
    }
    else {
      canvas.fill(0);
    }
    canvas.beginShape(TRIANGLE);
    
    canvas.vertex(p1.X, p1.Y);
    canvas.vertex(p2.X, p2.Y);
    canvas.vertex(p3.X, p3.Y);

    canvas.endShape();

    return new Point3D[] { p2, p3 };
  }

  private int PickFillColor() 
  {
    var index = _colorPickRow * _colorPick.width + (int)(RandomGenerator.Value(0, _colorPick.width));

    var rgba = Color20210703.IntToRgba(_colorPick.pixels[index]);

    return color(rgba[0], rgba[1], rgba[2], 255);
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

    _colorPickRow = RandomGenerator.Value(0, _colorPick.height);
    NoiseGenerator.ReSeed();
    RandomGenerator.ReSeed();
  }

  public void keyPressed() {

    super.keyPressed();

    if (!PropagateKeyboardEvents) {
      return;
    }

    if (key == CODED) {
      switch (keyCode) {
        case CONTROL:
          ((Numberbox) Gui.getController("_xNoiseMultiplier")).setScrollSensitivity(0.01F);
          ((Numberbox) Gui.getController("_yNoiseMultiplier")).setScrollSensitivity(0.01F);
          break;
        case SHIFT:
        ((Numberbox) Gui.getController("_xNoiseMultiplier")).setScrollSensitivity(0.1F);
        ((Numberbox) Gui.getController("_yNoiseMultiplier")).setScrollSensitivity(0.1F);
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
        ((Numberbox) Gui.getController("_xNoiseMultiplier")).setScrollSensitivity(0.001F);
        ((Numberbox) Gui.getController("_yNoiseMultiplier")).setScrollSensitivity(0.001F);
        break;
        case SHIFT:
        ((Numberbox) Gui.getController("_xNoiseMultiplier")).setScrollSensitivity(0.01F);
        ((Numberbox) Gui.getController("_yNoiseMultiplier")).setScrollSensitivity(0.01F);
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