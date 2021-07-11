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

public class daily_20210706_c extends AbstractDaily 
{
  private boolean _randomLeafAngle = true;

  private boolean _enableAttractor = false;

  private boolean _enableWarp = false;
  private int _warpStrength = 1;

  private boolean _enableModulate = false;
  private float _modulateStrength = 0.15F;

  private boolean _enableOverlay = false;
  private int _overlayColor = color(43, 90, 243);

  private PGraphics _tempCanvas;

  private int _backgroundColor;

  private INoiseGenerator _backgroundNoiseGenerator;
  private IRandomGenerator _leafColorRandomGenerator;
  private INoiseGenerator _leafNoiseGenerator;
  private INoiseGenerator _leafHeightNoiseGenerator;
  private INoiseGenerator _leafMidribNoiseGenerator;

  private float _leafNoiseMultiplier = 0.0050F;

  private float _minLeafHeight = 80F;
  private float _maxLeafHeight = 160F;

  private float _minHalfwidth = 0.40F;
  private float _maxHalfwidth = 0.60F;
  private float _minHalfVariation = 0.10F;
  private float _maxHalfVariation = 0.50F;

  private float _xdeviationFactor = 0.05F;

  private int _numberOfLeaves = 1;

  public daily_20210706_c() 
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
    _leafNoiseGenerator.InputMultiplier(_leafNoiseMultiplier, _leafNoiseMultiplier, 1.0);

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

    // Gui.addNumberbox("_numberOfLeaves")
    //     .setPosition(guiX, guiY).setLabel("Number Of Leaves")
    //     .setRange(100, 1000)
    //     .setScrollSensitivity(1F).setMultiplier(1F)
    //     .setColorLabel(color(0))
    //     .setValue(_numberOfLeaves);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_minLeafHeight")
    //     .setPosition(guiX, guiY).setLabel("Min Leaf Height")
    //     .setRange(20, 160)
    //     .setScrollSensitivity(1F).setMultiplier(1F)
    //     .setColorLabel(color(0))
    //     .setValue(_minLeafHeight);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_maxLeafHeight")
    //     .setPosition(guiX, guiY).setLabel("Max Leaf Height")
    //     .setRange(20, 160)
    //     .setScrollSensitivity(1F).setMultiplier(1F)
    //     .setColorLabel(color(0))
    //     .setValue(_maxLeafHeight);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_leafNoiseMultiplier")
    //     .setPosition(guiX, guiY).setLabel("Leaf Noise Multiplier")
    //     .setDecimalPrecision(4)
    //     .setRange(0.0F, 2.0F)
    //     .setScrollSensitivity(0.0001F).setMultiplier(0.0001F)
    //     .setColorLabel(color(0))
    //     .setValue(_leafNoiseMultiplier);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_minHalfwidth")
    //     .setPosition(guiX, guiY).setLabel("Min Half Width")
    //     .setDecimalPrecision(2)
    //     .setRange(0.00F, 1.5F)
    //     .setScrollSensitivity(0.01F).setMultiplier(0.01F)
    //     .setColorLabel(color(0))
    //     .setValue(_minHalfwidth);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_maxHalfwidth")
    //     .setPosition(guiX, guiY).setLabel("Max Half Width")
    //     .setDecimalPrecision(2)
    //     .setRange(0.00F, 1.5F)
    //     .setScrollSensitivity(0.01F).setMultiplier(0.01F)
    //     .setColorLabel(color(0))
    //     .setValue(_maxHalfwidth);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_minHalfVariation")
    //     .setPosition(guiX, guiY).setLabel("Min Half Variation")
    //     .setDecimalPrecision(2)
    //     .setRange(0.00F, 1.5F)
    //     .setScrollSensitivity(0.01F).setMultiplier(0.01F)
    //     .setColorLabel(color(0))
    //     .setValue(_minHalfVariation);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_maxHalfVariation")
    //     .setPosition(guiX, guiY).setLabel("Max Half Variation")
    //     .setDecimalPrecision(2)
    //     .setRange(0.00F, 1.5F)
    //     .setScrollSensitivity(0.01F).setMultiplier(0.01F)
    //     .setColorLabel(color(0))
    //     .setValue(_maxHalfVariation);

    // guiY += guiOffsetY;

    // Gui.addNumberbox("_xdeviationFactor")
    //     .setPosition(guiX, guiY).setLabel("X Deviation Factor")
    //     .setDecimalPrecision(2)
    //     .setRange(0.00F, 1.0F)
    //     .setScrollSensitivity(0.01F).setMultiplier(0.01F)
    //     .setColorLabel(color(0))
    //     .setValue(_xdeviationFactor);

    // guiY += guiOffsetY;

    // Gui.getTab("default").setColorBackground(color(255, 0, 0, 125));

    RandomBackgroundColor();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  private void RandomBackgroundColor()
  {
    _backgroundColor = color(236, 235, 218);
    // _backgroundColor = ColorPicker.RandomColorFromSb(RandomGenerator.Value(0.80, 0.90), RandomGenerator.Value(0.80, 0.90));
  }
  
  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
    NoiseGenerator.ReSeed(NoiseGenerator.Seed());
    _backgroundNoiseGenerator.ReSeed(_backgroundNoiseGenerator.Seed());
    _leafColorRandomGenerator.ReSeed(_leafColorRandomGenerator.Seed());
    _leafHeightNoiseGenerator.ReSeed(_leafHeightNoiseGenerator.Seed());
    _leafMidribNoiseGenerator.ReSeed(_leafMidribNoiseGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    var xcenter = Canvas.width / 2;
    var ycenter = Canvas.height / 2;

    var hwidth = Canvas.width * 0.42; 

    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(255);

    Canvas.endDraw();

    // DrawBackground(_backgroundNoiseGenerator, Canvas);

    for (var l = 0; l < _numberOfLeaves; l++)
    {
      var a = RandomGenerator.Value(0, Math.Pi * 2);

      var x = Math.Cos(a);
      var y = Math.Sin(a);

      var r = RandomGenerator.Value(0.0, hwidth);

      if (_enableAttractor)
      {
        r *= Easing.InCubic(r / hwidth);
      }

      x *= r;
      y *= r;

      x += xcenter;
      y += ycenter;

      Canvas.beginDraw();

      Canvas.pushMatrix();

      Canvas.translate(Canvas.width / 2, Canvas.height / 2);
  
      DrawShapedLeaf1(
          0,
          0,
          0,
          (int)(Canvas.width * 0.45),
          _leafNoiseGenerator, 
          _leafMidribNoiseGenerator, 
          RandomGenerator, 
        Canvas);


      Canvas.rotate((float) (Math.Pi / 2));

        DrawShapedLeaf1(
          0,
          0,
          0,
          (int)(Canvas.width * 0.45),
          _leafNoiseGenerator, 
          _leafMidribNoiseGenerator, 
          RandomGenerator, 
            Canvas);

        Canvas.rotate((float)(Math.Pi / 2));
            
        DrawShapedLeaf1(
          0,
          0,
          0,
          (int)(Canvas.width * 0.45),
          _leafNoiseGenerator, 
          _leafMidribNoiseGenerator, 
          RandomGenerator, 
            Canvas);
        
        Canvas.rotate((float)(Math.Pi / 2));
            
        DrawShapedLeaf1(
          0,
          0,
          0,
          (int)(Canvas.width * 0.45),
          _leafNoiseGenerator, 
          _leafMidribNoiseGenerator, 
          RandomGenerator, 
            Canvas);

        Canvas.popMatrix();

      Canvas.endDraw();
    }

    if (_enableWarp) {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    if (_enableModulate) {
      ModulateColors(Canvas, _modulateStrength, _backgroundNoiseGenerator);
    }

    if (_enableOverlay) {
      Overlay(Canvas, _overlayColor);
    }

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void DrawBackground(INoiseGenerator noiseGenerator, PGraphics canvas)
  {
    _backgroundNoiseGenerator.Octaves(1);
    _backgroundNoiseGenerator.ClampValues(true);
    _backgroundNoiseGenerator.InputMultiplier(0.25, 0.25, 1.0);

    var factor = 0.05;

    canvas.beginDraw();

    canvas.noStroke();

    canvas.fill(_backgroundColor);

    canvas.rect(0, 0, canvas.width, canvas.height);

    canvas.endDraw();

    canvas.beginDraw();

    canvas.loadPixels();

    for (var y = 0; y < canvas.height; y++) 
    {
      for (var x = 0; x < canvas.width; x++) 
      {
        var index = y * canvas.width + x;

        var n = noiseGenerator.Value(x, y);

        if (n > -factor && n < factor) 
        {
          var hsba = Color.IntToHsb(canvas.pixels[index]);

          canvas.pixels[index] = Color.HsbToInt(Color.HueAdjust(hsba[0], 10, 10),
              Color.SaturationAdjust(hsba[1], 0.055, 0.055), Color.BrightnessAdjust(hsba[2], 0.055, 0.055), hsba[3]);
        }
      }
    }

    canvas.updatePixels();

    canvas.endDraw();
  }

  private int RandomLeafColor(IRandomGenerator randomGenerator)
  {
    var hues = new int[] { 78, 40, 10, 200 };

    var hue = Color.DegreesToDouble(randomGenerator.Value(hues.length));
    var variation = Color.DegreesToDouble(10);

    var h = randomGenerator.Value(Color.WrapHue(hue - variation), Color.WrapHue(hue + variation));
    var s = randomGenerator.Value(0.75, 0.85);
    var b = randomGenerator.Value(0.75, 0.85);

    return Color.HsbToInt(h, s, b);

    // return color(236, 235, 218);
  }
 
  private void DrawLeaf(
    double x,
    double y,
    double angle,
    double height,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator, 
    IRandomGenerator randomGenerator, 
    PGraphics canvas)
  {
    var maxheight = 160;
    var m = maxheight / height;

    _leafNoiseGenerator.Octaves(4);
    _leafNoiseGenerator.InputMultiplier(_leafNoiseMultiplier * m, _leafNoiseMultiplier * m, 1.0);

    _leafMidribNoiseGenerator.Octaves(4);
    _leafMidribNoiseGenerator.InputMultiplier(0.0085 * m, 0.0085 * m, 1.0);

    var middle = randomGenerator.Value(0 + height * 0.50, height - height * 0.25);
    var halfWidth = height * randomGenerator.Value(_minHalfwidth, _maxHalfwidth);
    var halfvariation = height * randomGenerator.Value(_minHalfVariation, _maxHalfVariation);

    var midribDeviation = height * randomGenerator.Value(-_xdeviationFactor, _xdeviationFactor);

    DrawLeafLine(x, y, height, middle, halfWidth, halfvariation, midribDeviation, leafNoiseGenerator,
        leafMidribNoiseGenerator, randomGenerator, canvas);
  }
 

  private void DrawRandomShapedLeaf(
    double x,
    double y,
    double angle,
    double height,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator, 
    IRandomGenerator randomGenerator, 
    PGraphics canvas)
  {
    var l = randomGenerator.Value(4);

    if (l == 0) {
      DrawShapedLeaf1(x, y, angle, height, leafNoiseGenerator, leafMidribNoiseGenerator, randomGenerator, canvas);
    }

    if (l == 1) {
      DrawShapedLeaf2(x, y, angle, height, leafNoiseGenerator, leafMidribNoiseGenerator, randomGenerator, canvas);
    }

    if (l == 2) {
      DrawShapedLeaf3(x, y, angle, height, leafNoiseGenerator, leafMidribNoiseGenerator, randomGenerator, canvas);
    }

    if (l == 3) {
      DrawShapedLeaf4(x, y, angle, height, leafNoiseGenerator, leafMidribNoiseGenerator, randomGenerator, canvas);
    }

    DrawLeaf(x, y, angle, height, leafNoiseGenerator, leafMidribNoiseGenerator, randomGenerator, canvas);
  }
 

  private void DrawShapedLeaf1(
    double x,
    double y,
    double angle,
    double height,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator, 
    IRandomGenerator randomGenerator, 
    PGraphics canvas)
  {
    //height = 30 + (50 * leafNoiseGenerator.Value(x, y));
    var maxheight = 400;
    var m = maxheight / height;
    var leafNoiseMultiplier = 0.002;

    _leafNoiseGenerator.Octaves(4);
    _leafNoiseGenerator.InputMultiplier(leafNoiseMultiplier * m, leafNoiseMultiplier * m, 1.0);

    _leafMidribNoiseGenerator.Octaves(4);
    _leafMidribNoiseGenerator.InputMultiplier(0.0085 * m, 0.0085 * m, 1.0);

    var middle = randomGenerator.Value(height * 0.45, height * 0.55);
    var halfWidth = randomGenerator.Value(height * 0.35, height * 0.45);
    var halfvariation = randomGenerator.Value(height * 0.08, height * 0.12);

    var midribDeviation = randomGenerator.Value(-0.777, -0.577);

    DrawLeafLine(x, y, height, middle, halfWidth, halfvariation, midribDeviation, leafNoiseGenerator,
        leafMidribNoiseGenerator, randomGenerator, canvas);
  }
 

  private void DrawShapedLeaf2(
    double x,
    double y,
    double angle,
    double height,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator, 
    IRandomGenerator randomGenerator, 
    PGraphics canvas)
  {
    height = randomGenerator.Value(110, 140);
    var maxheight = 140;
    var m = maxheight / height;
    var leafNoiseMultiplier = 0.005;

    _leafNoiseGenerator.Octaves(4);
    _leafNoiseGenerator.InputMultiplier(leafNoiseMultiplier * m, leafNoiseMultiplier * m, 1.0);

    _leafMidribNoiseGenerator.Octaves(4);
    _leafMidribNoiseGenerator.InputMultiplier(0.0085 * m, 0.0085 * m, 1.0);

    var middle = randomGenerator.Value(85, 105);
    var halfWidth = randomGenerator.Value(46, 58);
    var halfvariation = randomGenerator.Value(25, 35);

    var midribDeviation = randomGenerator.Value(-0.550, -0.650);

    DrawLeafLine(x, y, height, middle, halfWidth, halfvariation, midribDeviation, leafNoiseGenerator,
        leafMidribNoiseGenerator, randomGenerator, canvas);
  }
 

  private void DrawShapedLeaf3(
    double x,
    double y,
    double angle,
    double height,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator, 
    IRandomGenerator randomGenerator, 
    PGraphics canvas)
  {
    height = randomGenerator.Value(150, 170);
    var maxheight = 170;
    var m = maxheight / height;
    var leafNoiseMultiplier = 0.003;

    _leafNoiseGenerator.Octaves(4);
    _leafNoiseGenerator.InputMultiplier(leafNoiseMultiplier * m, leafNoiseMultiplier * m, 1.0);

    _leafMidribNoiseGenerator.Octaves(4);
    _leafMidribNoiseGenerator.InputMultiplier(0.0085 * m, 0.0085 * m, 1.0);

    var middle = randomGenerator.Value(65.0, 85.0);
    var halfWidth = randomGenerator.Value(18.0, 24.0);
    var halfvariation = randomGenerator.Value(2.0, 3.0);

    var midribDeviation = randomGenerator.Value(6.50, 7.50);

    DrawLeafLine(x, y, height, middle, halfWidth, halfvariation, midribDeviation, leafNoiseGenerator,
        leafMidribNoiseGenerator, randomGenerator, canvas);
  }

  
  private void DrawShapedLeaf4(
    double x,
    double y,
    double angle,
    double height,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator, 
    IRandomGenerator randomGenerator, 
    PGraphics canvas)
  {
    height = randomGenerator.Value(120, 140);
    var maxheight = 140;
    var m = maxheight / height;
    var leafNoiseMultiplier = 0.010;

    _leafNoiseGenerator.Octaves(4);
    _leafNoiseGenerator.InputMultiplier(leafNoiseMultiplier * m, leafNoiseMultiplier * m, 1.0);

    _leafMidribNoiseGenerator.Octaves(4);
    _leafMidribNoiseGenerator.InputMultiplier(0.0085 * m, 0.0085 * m, 1.0);

    var middle = randomGenerator.Value(83.0, 103.0);
    var halfWidth = randomGenerator.Value(22.0, 30.0);
    var halfvariation = randomGenerator.Value(4.5, 6.5);

    var midribDeviation = randomGenerator.Value(1.20, 1.8);

    DrawLeafLine(x, y, height, middle, halfWidth, halfvariation, midribDeviation, leafNoiseGenerator,
        leafMidribNoiseGenerator, randomGenerator, canvas);
  }

  
 

  private void DrawLeafLine(
    double x,
    double y,
    double height,
    double middle,
    double halfWidth,
    double halfvariation,
    double midribDeviation,
    INoiseGenerator leafNoiseGenerator,
    INoiseGenerator leafMidribNoiseGenerator,
    IRandomGenerator randomGenerator,
    PGraphics canvas)
  {

    x += randomGenerator.Value(-canvas.width, canvas.width);
    x += randomGenerator.Value(-canvas.height, canvas.height);

    var colors = ColorPicker.Monochromatic(RandomLeafColor(randomGenerator));

    var lcolor = colors[4];
    var ccolor = colors[2];
    var rcolor = colors[2];

    canvas.strokeWeight(1);

    canvas.noFill();

    canvas.beginShape(LINES);

    for (var h = 0.0F; h < height; h++) {
      var top = h < middle;

      var f = top ? h / middle : (height - h) / (height - middle);

      // f = Easing.EaseInOutCubic(f);
      // f = Easing.EaseInOutCircular(f);
      var e = Easing.OutQuadratic(f);

      var nleft = leafNoiseGenerator.Value(x + f + 1.18, y + h + 8.32);
      var nright = leafNoiseGenerator.Value(x - f + 4.78, y - h + 7.22);

      var d = midribDeviation * leafMidribNoiseGenerator.Value(x + f + 8.42, y + h + 3.24);

      var xl = (float) (e * ((halfWidth) + (nleft * halfvariation)));
      var xr = (float) (e * ((halfWidth) + (nright * halfvariation)));

      if (xl < 0) {
         xl = (float)halfWidth;
       }

       if (xr < 0) {
         xr = (float)halfWidth;
       }

      xl = -xl;

      var yy = -h;

      if (d < xl) {
        d = xl;
      }

      if (d > xr) {
        d = xr;
      }

      canvas.stroke(lcolor);
      canvas.vertex(xl, yy);

      canvas.stroke(ccolor);
      canvas.vertex((float) d, yy);

      canvas.stroke(ccolor);
      canvas.vertex((float) d, yy);

      canvas.stroke(rcolor);
      canvas.vertex(xr, yy);
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
          var hsba = Color.IntToHsb(canvas.pixels[index]);
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

    for (var y = 0; y < bottom.height; y++) {
      for (var x = 0; x < bottom.width; x++) {
        var index = (int) (y * bottom.height + x);

        if (bottom.pixels[index] != 0 && bottom.pixels[index] != white && top.pixels[index] != 0) {
          
          var rgba = Color.IntToRgb(top.pixels[index]);

          var a = (int) (maskGenerator.Value(x, y) * 255.0);

          if (a < 25) {
            a = 25;
          }

          top.pixels[index] = Color.RgbToInt(rgba[0], rgba[1], rgba[2], a);
        }

      }
    }

    top.updatePixels();
    top.endDraw();

    //bottom.updatePixels();
    bottom.endDraw();
  }
 
  private void Overlay(PGraphics canvas, int overlay)
  {
    var overlayRgba = Color.IntToRgb(overlay);

    canvas.beginDraw();
    
    canvas.loadPixels();

    for (var y = 0; y < canvas.height; y++)
    {
      for (var x = 0; x < canvas.width; x++)
      {
        var index = y * canvas.width + x;

        var c = canvas.pixels[index];

        var rgba = Color.IntToRgb(c);

        rgba[0] = rgba[0] < overlayRgba[0] ? overlayRgba[0] : rgba[0];
        rgba[1] = rgba[1] < overlayRgba[1] ? overlayRgba[1] : rgba[1];
        rgba[2] = rgba[2] < overlayRgba[2] ? overlayRgba[2] : rgba[2];

        canvas.pixels[index] = Color.RgbToInt(rgba[0], rgba[1], rgba[2], rgba[3]);
      }
    }
    
    canvas.updatePixels();

    canvas.endDraw();
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