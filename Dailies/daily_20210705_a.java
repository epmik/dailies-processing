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

import org.eclipse.collections.api.list.primitive.DoubleList;

import Geometry.Icosahedron;
import Geometry.Point3D;
//import processing.opengl.*;
import controlP5.*;
// import peasy.*;

import Procedural.*;
import Procedural.Interfaces.INoiseGenerator;
import Procedural.Interfaces.IRandomGenerator;
import Utility.Color;
// import Utility.*;
import Utility.Color20210703;
import Utility.ColorPicker;
import Utility.Easing;
import Utility.Math;

public class daily_20210705_a extends AbstractDaily 
{
  private boolean _enableWarp = true;
  private int _warpStrength = 4;

  private boolean _enableModulate = false;
  private float _modulateStrength = 0.03F;

  private boolean _enableBlend = true;

  private PGraphics _tempCanvas;

  private int _backgroundColor;

  private INoiseGenerator _backgroundNoiseGenerator;
  private IRandomGenerator _leafColorRandomGenerator;
  private INoiseGenerator _leafNoiseGenerator;
  private INoiseGenerator _leafHeightNoiseGenerator;
  private INoiseGenerator _leafMidribNoiseGenerator;

  public daily_20210705_a() 
  {
    super();

    Renderer = P3D;
    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 1.0;
  }

    @Override
  public void setup() 
  {
    super.setup();

    _backgroundNoiseGenerator = new SuperSimplexNoiseGenerator();
    _backgroundNoiseGenerator.Octaves(1);
    _backgroundNoiseGenerator.AllowNegativeValues(true);
    _backgroundNoiseGenerator.ClampValues(true);
    _backgroundNoiseGenerator.InputMultiplier(0.95, 0.95, 1.0);

    _leafColorRandomGenerator = new RandomGenerator();

    _leafNoiseGenerator = new SuperSimplexNoiseGenerator();
    _leafNoiseGenerator.Octaves(4);
    _leafNoiseGenerator.AllowNegativeValues(true);
    _leafNoiseGenerator.ClampValues(true);
    _leafNoiseGenerator.InputMultiplier(0.0125, 0.0125, 1.0);

    _leafHeightNoiseGenerator = new SuperSimplexNoiseGenerator();
    _leafHeightNoiseGenerator.Octaves(2);
    _leafHeightNoiseGenerator.AllowNegativeValues(false);
    _leafHeightNoiseGenerator.ClampValues(true);
    _leafHeightNoiseGenerator.InputMultiplier(0.0075, 0.0075, 1.0);

    _leafMidribNoiseGenerator = new SuperSimplexNoiseGenerator();
    _leafMidribNoiseGenerator.Octaves(1);
    _leafMidribNoiseGenerator.AllowNegativeValues(true);
    _leafMidribNoiseGenerator.ClampValues(true);
    _leafMidribNoiseGenerator.InputMultiplier(0.005, 0.005, 1.0);

    _tempCanvas = CreateAndSetupGraphics();

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    Gui.addToggle("_enableWarp").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_enableWarp);

    guiY += guiOffsetY;

    Gui.addNumberbox("_warpStrength")
        .setPosition(guiX, guiY).setLabel("Warp Strength")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0F, 400)
        .setValue(_warpStrength);

    guiY += guiOffsetY;

    // Gui.addToggle("_enableModulate").setPosition(guiX, guiY).setLabel("Enable Modulate").setValue(_enableModulate);

    // guiY += guiOffsetY;

    // Gui.addToggle("_enableBlend").setPosition(guiX, guiY).setLabel("Enable Blend").setValue(_enableBlend);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_modulateStrength")
    //     .setPosition(guiX, guiY).setLabel("Modulate Strength")
    //     .setScrollSensitivity(0.01F).setMultiplier(0.01F)
    //     .setRange(0.0F, 1.0F)
    //     .setValue(_modulateStrength);

    // guiY += guiOffsetY;

    RandomBackgroundColor();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  private void RandomBackgroundColor()
  {
    _backgroundColor = Color.RandomColor(RandomGenerator.Value(0.05, 0.15), RandomGenerator.Value(0.85, 1.00));
  }
  
  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(255);

    Canvas.endDraw();

    DrawBackground(_backgroundNoiseGenerator, Canvas);

    Canvas.beginDraw();

    for (var l = 0; l < 250; l++)
    {
      Canvas.pushMatrix();

      var x = RandomGenerator.Value(width / 10, width - width / 10);
      var y = RandomGenerator.Value(height / 10, height - height / 10);

      Canvas.translate(x, y);

      Canvas.rotate((float) RandomGenerator.Value(0, Math.Pi * 2.0));
      
      var h = 20 + _leafHeightNoiseGenerator.Value(x, y) * 160;
  
      DrawLeaf(
          h,
          _leafNoiseGenerator, 
          _leafMidribNoiseGenerator, 
          RandomGenerator, 
        Canvas);
  
      Canvas.popMatrix();
    }


    Canvas.endDraw();

    if (_enableWarp) {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void DrawBackground(INoiseGenerator noiseGenerator, PGraphics canvas)
  {
    _backgroundNoiseGenerator.Octaves(1);
    _backgroundNoiseGenerator.ClampValues(true);
    _backgroundNoiseGenerator.InputMultiplier(0.95, 0.95, 1.0);

    var factor = 0.05;

    canvas.beginDraw();

    canvas.noStroke();

    canvas.fill(_backgroundColor);

    canvas.rect(0, 0, canvas.width, canvas.height);

    canvas.endDraw();

    canvas.beginDraw();

    canvas.loadPixels();

    for (var y = 0; y < canvas.height; y++) {
      for (var x = 0; x < canvas.width; x++) {
        var index = y * canvas.width + x;

        // var c = canvas.pixels[index];

        var n = noiseGenerator.Value(x, y);

        if (n > -factor && n < factor) {
          var hsba = Color.IntToHsba(canvas.pixels[index]);

          canvas.pixels[index] = Color.HsbToInt(Color.HueAdjust(hsba[0], 10, 10),
              Color.SaturationAdjust(hsba[1], 0.075, 0.075), Color.BrightnessAdjust(hsba[2], 0.075, 0.075), hsba[3]);
        }
      }
    }

    canvas.updatePixels();

    canvas.endDraw();
  }

  private int RandomLeafGreen(IRandomGenerator randomGenerator)
  {
    var hue = Color.DegreesToDouble(78);
    var variation = Color.DegreesToDouble(10);

    var h = randomGenerator.Value(Color.WrapHue(hue - variation), Color.WrapHue(hue + variation));
    var s = randomGenerator.Value(0.25, 0.75);
    var b = randomGenerator.Value(0.75, 0.95);

    return Color.HsbToInt(h, s, b);
  }
 
  private void DrawLeaf(
    double height,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator, 
    IRandomGenerator randomGenerator, 
    PGraphics canvas)
  {
    var middle = randomGenerator.Value(0 + height * 0.50, height - height * 0.25);
    var halfwidth = height * randomGenerator.Value(0.20, 0.40) * 0.5;

    var colors = ColorPicker.Monochromatic(RandomLeafGreen(randomGenerator));

    var lcolor = colors[randomGenerator.Value(colors.length)];
    var rcolor = colors[randomGenerator.Value(colors.length)];

    var xdeviation = height * randomGenerator.Value(0.05, 0.015);

    canvas.strokeWeight(1);

    canvas.noFill();

    canvas.beginShape(LINES);

    for (var h = 0.0F; h < height; h++)
    {
      var top = h < middle;

      var f = top ? h / middle : (height - h) / (height - middle);

      // f = Easing.EaseInOutCubic(f);
      // f = Easing.EaseInOutCircular(f);
      var e = Easing.OutQuadratic(f);

      var nleft = e * leafNoiseGenerator.Value(f + 1.18, h + 8.32);
      var nright = e * leafNoiseGenerator.Value(f + 4.78, h + 7.22);

      var d = xdeviation * leafMidribNoiseGenerator.Value(e + 8.42, h + 3.24);

      var x = -(float) ((e * halfwidth) + (nleft * halfwidth * 0.50));

      canvas.stroke(lcolor);
      canvas.vertex(x, h);
      canvas.vertex((float) Math.Max(x, d), h);

      x = (float)((e * halfwidth) + (nright * halfwidth * 0.50));
      
      canvas.stroke(rcolor);
      canvas.vertex((float) Math.Min(x, d), h);
      canvas.vertex(x, h);
    }

    canvas.endShape();
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
    
  private void ModulateColors(PGraphics canvas, double factor, INoiseGenerator noiseGenerator) 
  {
    noiseGenerator.AllowNegativeValues(true);
    noiseGenerator.ClampValues(true);

    canvas.beginDraw();

    canvas.loadPixels();

    for (var y = 0; y < canvas.height; y++) 
    {
      for (var x = 0; x < canvas.width; x++) 
      {
        var index = (int) (y * canvas.height + x);

        if (canvas.pixels[index] == 0) {
          continue;
        }

        var n = noiseGenerator.Value(x, y);

        if(n > -factor && n < factor)
        {
          var hsba = Color.IntToHsba(canvas.pixels[index]);
          //canvas.pixels[index] = Color.HsbaToInt(Color.HueAdjust(hsba[0], 90), hsba[1], hsba[2], hsba[3]);
          //canvas.pixels[index] = Color.HsbaToInt(hsba[0], hsba[1], Color.BrightnessAdjust(hsba[2], 0.10), hsba[3]);
          canvas.pixels[index] = Color.HsbToInt(Color.HueAdjust(hsba[0], 5), hsba[1], Color.BrightnessAdjust(hsba[2], 0.075), hsba[3]);
        }
      }
    }
    
    canvas.updatePixels();

    canvas.endDraw();
  }
    
  private void BlendColors(PGraphics bottom, PGraphics top, INoiseGenerator maskGenerator, INoiseGenerator transparencyGenerator) 
  {
    maskGenerator.AllowNegativeValues(false);
    maskGenerator.ClampValues(true);

    transparencyGenerator.AllowNegativeValues(false);
    transparencyGenerator.ClampValues(true);

    bottom.beginDraw();
    bottom.loadPixels();

    top.beginDraw();
    top.loadPixels();

    var white = color(255, 255, 255);

    for (var y = 0; y < bottom.height; y++) 
    {
      for (var x = 0; x < bottom.width; x++) 
      {
        var index = (int) (y * bottom.height + x);

        if (bottom.pixels[index] != 0 && bottom.pixels[index] != white && top.pixels[index] != 0) 
        {
          var rgba = Color.IntToRgba(top.pixels[index]);

          var a = (int) (maskGenerator.Value(x, y) * 255.0);
          
          if (a < 25)
          {
            a = 25;
          }
  
          top.pixels[index] = Color.RgbToInt(rgba[0], rgba[1], rgba[2], a);
          // top.pixels[index] = Color.RgbaToInt(rgba[0], rgba[1], rgba[2], (int)((double)rgba[3] * 0.5));
        }

      }
    }
    
    top.updatePixels();
    top.endDraw();

    //bottom.updatePixels();
    bottom.endDraw();
  }
 
  public void mouseReleased() {

    super.mouseReleased();

    if (!PropagateMouseEvents) {
      return;
    }

    NoiseGenerator.ReSeed();
    RandomGenerator.ReSeed();

    _backgroundNoiseGenerator.ReSeed();

    RandomBackgroundColor();
  }

}