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

import Procedural.*;
import Procedural.Interfaces.INoiseGenerator;
// import Utility.*;
import Utility.Color20210703;
import Utility.Easing;
import Utility.Math;

public class daily_20210627_b extends AbstractDaily 
{
  private PImage _skinColorPick;
  private int _skinColorRow;
  private OpenSimplexNoiseGenerator _radiusNoiseGenerator;
  private float _radiusNoiseMultiplier;

  public daily_20210627_b() 
  {
    super();

    Renderer = P2D;
    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 1.0;
    _radiusNoiseMultiplier = 0.0005F;
  }

  @Override
  public void setup() 
  {
    super.setup();

    // _backgroundCanvas = createGraphics(Canvas.width, Canvas.height, P2D);
    // _foregroundCanvas = createGraphics(Canvas.width, Canvas.height, P2D);

    _radiusNoiseGenerator = new OpenSimplexNoiseGenerator(0);
    _radiusNoiseGenerator.Octaves(4);
    _radiusNoiseGenerator.ClampValues(true);
    _radiusNoiseGenerator.AllowNegativeValues(true);
    _radiusNoiseGenerator.InputMultiplier(_radiusNoiseMultiplier);

    RandomGenerator = new RandomGenerator(0L);

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    // Gui.addToggle("_warpSky").setPosition(guiX, guiY).setLabel("Warp sky").setValue(_warpSky);

    // guiY += guiOffsetY;

    // Gui.addSlider("_xPositionOffsetStrenth").setPosition(guiX, guiY).setLabel("X position offset strenth").setRange(0.0F, (float)(ScreenWidth * SketchResolutionMultiplier))
    //     .setValue(_xPositionOffsetStrenth);

    // guiY += guiOffsetY;

    Gui.addSlider("_radiusNoiseMultiplier").setPosition(guiX, guiY).setLabel("Radius noise multiplier")
        .setRange(0.0001F, 0.9000F)
        .setDecimalPrecision(5)
        .setScrollSensitivity(0.0001F)
        .setValue(_radiusNoiseMultiplier);

    guiY += guiOffsetY;

    // Gui.addToggle("_warpLand").setPosition(guiX, guiY).setLabel("Warp land").setValue(_warpLand);

    // guiY += guiOffsetY;

    // Gui.addSlider("_warpLandStrength").setPosition(guiX, guiY).setLabel("Warp land strength").setRange(0, (int)(ScreenWidth * SketchResolutionMultiplier))
    //     .setValue((float) _warpLandStrength);

    // guiY += guiOffsetY;

    // Gui.addSlider("_patchCount").setPosition(guiX, guiY).setLabel("Patch count").setRange(1, 100)
    //     .setValue(_patchCount);

    // guiY += guiOffsetY;
  
     _skinColorPick = loadImage(ResolveInputFile("skin.png"));
     _skinColorPick.loadPixels();

     _skinColorRow = RandomGenerator.Value(0, _skinColorPick.height);

    // _backgroundColorPick = loadImage(PathCombine(ClassFolderInput(), "background.png"));
    // _backgroundColorPick.loadPixels();

    // DrawBackgroundGradient(_backgroundCanvas, _backgroundNoiseGenerator);

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  @Override
  public void Update(double elapsedTime) 
  {
    _radiusNoiseGenerator.InputMultiplier(_radiusNoiseMultiplier);
    RandomGenerator.ReSeed(RandomGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    var xcenter = Canvas.width / 2;
    var ycenter = Canvas.height / 2;
    var segmentCount = 10;
    var totalSegmentsHeight = (double)Canvas.height / 2.0D;
    var segmentHeight = (int)(totalSegmentsHeight / (double)segmentCount);
    var w = totalSegmentsHeight * 0.05D;

    // float fov = (float)(Math.PI / 3);
    // float cameraZ = (float)((Canvas.height / 2.0) / Math.tan(fov / 2.0));
    // Canvas.perspective(fov, (float) (Canvas.width) / (float) (Canvas.height), cameraZ / 10.0F, cameraZ * 10.0F);
    // Canvas.ortho(-Canvas.width/2, Canvas.width/2, -Canvas.height/2, Canvas.height/2);

    Canvas.beginDraw();

    Canvas.clear();
    Canvas.background(255);

    Canvas.beginShape(QUAD_STRIP);

    var y = (int) ((Canvas.height - (totalSegmentsHeight)) / 2.0D);

    var m = 0.85;
    var mp = 0.15;
    
    for (var i = 0; i <= segmentCount; i++) 
    {
      var factor = 1.0D - (Math.Abs(ycenter - y) / (totalSegmentsHeight * 0.5D));
      factor = Easing.OutQuadratic(factor);

      var xoffset = (int) ((w * factor) * m);
      
      m += mp;

      if (m >= 1.0)
      {
        mp = -mp;
      }

      if (m < 0.5)
      {
        m = 0.5;
      }

      Canvas.vertex(xcenter + xoffset, y);
      Canvas.vertex(xcenter - xoffset, y);

      y += segmentHeight;
    }

    Canvas.endShape();

    //Canvas.background(255);

    // Canvas.pushMatrix();
    // Canvas.translate(Canvas.width / 2, Canvas.height / 2, 0);
    // Canvas.rotateX(-PI / 6);
    // Canvas.rotateY(PI / 3);
    // Canvas.box(160);
    // Canvas.popMatrix();

    // Canvas.image(_backgroundCanvas, 0, 0, _backgroundCanvas.width, _backgroundCanvas.height);

    // Canvas.pushMatrix();
    // Canvas.translate(Canvas.width / 2, Canvas.height / 2, 0);
    // DrawIcosahedron(_icosahedron, Canvas);
    // Canvas.popMatrix();
    
    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  // private void DrawIcosahedron(Icosahedron icosahedron, PGraphics canvas)
  // {
  //   var radius = 250.0f;

  //   canvas.push();

  //   canvas.strokeWeight(1);
  //   canvas.noFill();
  //   canvas.stroke(255);

  //   canvas.hint(DISABLE_DEPTH_TEST);

  //   canvas.beginShape(TRIANGLES);

  //   for (var i = 0; i < icosahedron.TriangleIndices.size(); i += 3) {
  //     DrawVertex(icosahedron.Positions.get(icosahedron.TriangleIndices.get(i)), radius, canvas);
  //     DrawVertex(icosahedron.Positions.get(icosahedron.TriangleIndices.get(i + 1)), radius, canvas);
  //     DrawVertex(icosahedron.Positions.get(icosahedron.TriangleIndices.get(i + 2)), radius, canvas);
  //   }

  //   canvas.endShape();

  //   canvas.pop();
  // }
  
  // private void DrawVertex(Point3D p, float radius, PGraphics canvas) 
  // {
  //   var x = p.X * radius;
  //   var y = p.Y * radius;
  //   var z = p.Z * radius;

  //   canvas.fill(PickSkinColor(_skinColorRow, _colorNoiseGenerator.Value(x, y)));

  //   var xoffset = (float) (_radiusNoiseGenerator.Value(x, y) * _xPositionOffsetStrenth);
  //   var yoffset = (float) (_yPositionNoiseGenerator.Value(y, z) * _yPositionOffsetStrenth);
  //   var zoffset = (float) (_zPositionNoiseGenerator.Value(x, z) * _zPositionOffsetStrenth);

  //   canvas.vertex(x + xoffset, y + yoffset, z + zoffset);
  // }
     
  // private int PickSkinColor(int row) 
  // {
  //   var index = row * _skinColorPick.width + (int)RandomGenerator.Value(_skinColorPick.width);

  //   return _skinColorPick.pixels[index];
  // }
     
  // private int PickSkinColor(int row, double factor) 
  // {
  //   var index = row * _skinColorPick.width + (int) (factor * _skinColorPick.width);
    
  //   var rgba = Color.IntToRgba(_skinColorPick.pixels[index]);
    
  //   return color(rgba[0], rgba[1], rgba[2], (int)(factor * 255F));
  // }
  
  // private void Warp(PGraphics canvas, float warpStrength, INoiseGenerator noiseGenerator) 
  // {
  //   canvas.beginDraw();

  //   canvas.loadPixels();

  //   var temp = createImage(canvas.width, canvas.height, ARGB);

  //   temp.loadPixels();

  //   for (var y = 0; y < canvas.height; y++) 
  //   {
  //     for (var x = 0; x < canvas.width; x++) 
  //     {
  //       var angle = noiseGenerator.Value(x, y) * Math.PI * 2D;

  //       var xx = x + (int) (Math.cos(angle) * warpStrength);
  //       var yy = y + (int) (Math.sin(angle) * warpStrength);

  //       if (xx < 0) {
  //         xx = 0;
  //       } else if (xx >= canvas.width) {
  //         xx = canvas.width - 1;
  //       }

  //       if (yy < 0) {
  //         yy = 0;
  //       } else if (yy >= canvas.height) {
  //         yy = canvas.height - 1;
  //       }

  //       var color = canvas.pixels[(int) (yy * canvas.height + xx)];

  //       temp.pixels[(int) (y * temp.height + x)] = color;
  //     }
  //   }

  //   temp.updatePixels();

  //   // SaveFrame(temp);

  //   // SaveFrame(canvas);

  //   canvas.clear();

  //   canvas.image(temp, 0, 0, temp.width, temp.height);

  //   canvas.endDraw();

  //   // SaveFrame(canvas);
  // }

  public void mouseReleased() {
    
    super.mouseReleased();

    if (!PropagateMouseEvents) {
      return;
    }

    RandomGenerator.ReSeed();
    _skinColorRow = RandomGenerator.Value(0, _skinColorPick.height);
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