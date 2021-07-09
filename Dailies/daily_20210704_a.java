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
import Utility.Easing;
import Utility.Math;

public class daily_20210704_a extends AbstractDaily 
{
  private boolean _drawMask = true;

  private boolean _enableWarp = true;
  private int _warpStrength = 4;

  // private boolean _enableModulate = false;
  // private float _modulateStrength = 0.03F;

  // private boolean _enableBlend = true;

  // private int _numberOfColorFields = 4;

  private PImage _gapMaskImage;
  private PGraphics _tempCanvas;

  private int _totalMarks = 5000;
  private int _marksPerFrameCount = 100;
  private int _marksCount = 0;

  // private IRandomGenerator _colorRandomGenerator;

  // private int _baseColor;

  // private static final int ColorPickOptionComplementary = 0;
  // private static final int ColorPickOptionSplitComplementary = 1;
  // private static final int ColorPickOptionTriadic = 2;
  // private static final int ColorPickOptionTetradic = 3;
  // private static final int ColorPickOptionAnalagous = 4;
  // private static final int ColorPickOptionMonochrome = 5;

  // private static final List<Integer> ColorPickOptions = Arrays.asList(ColorPickOptionComplementary, ColorPickOptionSplitComplementary,
  //     ColorPickOptionTriadic, ColorPickOptionTetradic, ColorPickOptionAnalagous, ColorPickOptionMonochrome);
  
  // private int _selectedColorPickOption = ColorPickOptionTriadic;

  // public int _minMinLineThickness = 1;
  // public int _maxMinLineThickness = _minMinLineThickness + 3;
  // public int _minMaxLineThickness = 4;
  // public int _maxMaxLineThickness = _minMaxLineThickness * 4;

  // public int _mnMinLineDeviation = 0;
  // public int _maxMinLineDeviation = 3;
  // public int _minMaxLineDeviation = 4;
  // public int _maxMaxLineDeviation = 16;

  private int _centerX;
  private int _centerY;
  private double _topDistanceToCenterY;
  private double _bottomDistanceToCenterY;
  private float _gapTopFactor = 2.75F;
  private float _gapBottomFactor = 1.10F;

  private INoiseGenerator _gapNoiseGenerator;

  private IRandomGenerator _positionRandomGenerator;

  private boolean _drawGapMask = true;

  public daily_20210704_a() 
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

    _centerX = Canvas.width / 2;
    _centerY = Canvas.height / 2;
    _topDistanceToCenterY = _centerY;
    _bottomDistanceToCenterY = Canvas.height - _centerY;

    _gapNoiseGenerator = new OpenSimplexNoiseGenerator();
    _gapNoiseGenerator.ClampValues(true);
    _gapNoiseGenerator.AllowNegativeValues(true);
    _gapNoiseGenerator.Octaves(6);
    _gapNoiseGenerator.InputMultiplier(0.0025, 0.0025, 1);

    _positionRandomGenerator = new RandomGenerator();

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    // Gui.addToggle("_drawMask")
    //     .setPosition(guiX, guiY).setLabel("Draw Mask")
    //     .setValue(_drawMask);

    // guiY += guiOffsetY;

    Gui.addNumberbox("_gapTopFactor")
        .setPosition(guiX, guiY).setLabel("Gap Top Factor")
        .setScrollSensitivity(0.1F).setMultiplier(0.1F)
        .setRange(0.0F, 5.0F)
        .setValue(_gapTopFactor);

    guiY += guiOffsetY;

    Gui.addNumberbox("_gapBottomFactor")
        .setPosition(guiX, guiY).setLabel("Gap Bottom Factor")
        .setScrollSensitivity(0.1F).setMultiplier(0.1F)
        .setRange(0.0F, 5.0F)
        .setValue(_gapBottomFactor);

    guiY += guiOffsetY;

    _tempCanvas = CreateAndSetupGraphics();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    if (_drawGapMask) 
    {      
      _drawGapMask = false;

      background(0);

      DrawGapMask(_tempCanvas, RandomGenerator, _gapNoiseGenerator);

      if (_enableWarp) 
      {
        Warp(_tempCanvas, _warpStrength, NoiseGenerator);
      }

      _gapMaskImage = _tempCanvas.get();

      _gapMaskImage.loadPixels();

      Canvas.beginDraw();

      Canvas.background(255);

      if (_drawMask)
      {
        Canvas.image(_gapMaskImage, 0, 0);
      }

      Canvas.endDraw();
    }

    _tempCanvas.beginDraw();

    _tempCanvas.clear();

    for (var i = 0; i < _marksPerFrameCount && _marksCount < _totalMarks; i++, _marksCount++) 
    {
      DrawRandomDotOnMask(_tempCanvas, _gapMaskImage, _positionRandomGenerator);
    }

    _tempCanvas.endDraw();

    if (_enableWarp) 
    {
      Warp(_tempCanvas, _warpStrength, NoiseGenerator);
    }
    
    Canvas.beginDraw();
    
    Canvas.image(_tempCanvas, 0, 0);
    
    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  public void DrawGapMask(PGraphics canvas, IRandomGenerator randomGenerator, INoiseGenerator gapNoiseGenerator) 
  {
    var gap = (double) canvas.width / 8.0;
    var mingap = (double) canvas.width / 32.0;

    canvas.beginDraw();

    canvas.clear();

    canvas.noStroke();

    canvas.fill(0);

    for (var y = 0; y < canvas.height;)
    {
      var top = y < _centerY;

      var h = randomGenerator.Value(1, 12);
      var offset = randomGenerator.Value(0, 4);

      var d = (double)(top ? _centerY - y : y - _centerY);

      var t = (double)(top ? d / _topDistanceToCenterY : d / _bottomDistanceToCenterY);

      var f = mingap + Math.Pow((gap * Ease(Easing::InQuadratic, t)), (top ? t * _gapTopFactor : t * _gapBottomFactor));
      // var f = mingap + Math.pow((top ? _gapTopFactor : _gapBottomFactor) * (gap * Ease(Easing::EaseInQuadratic, t)), (top ? t * 2.0 : t * 1.2));

      var leftf = (int)(gap * gapNoiseGenerator.Value(_centerX - f, y));
      var rightf = (int)(gap * gapNoiseGenerator.Value(_centerX + f, y));
      
      canvas.rect(0, y, (int) (_centerX - f + leftf), h);

      canvas.rect((int)(_centerX + f + rightf), y, canvas.width , h);

      y += h + offset;
    }

    canvas.endDraw();
  }
  
  public void DrawRandomDotOnMask(PGraphics canvas, PImage mask, IRandomGenerator randomGenerator) 
  {
    var type = randomGenerator.Value(3);

    var w = randomGenerator.Value(2, 20);
    var h = randomGenerator.Value(2, 20);

    var xy = RandomPositionOnMask(mask, randomGenerator);

    canvas.beginDraw();

    canvas.noStroke();

    canvas.fill(0);

    switch(type)
    {
      case 0:
        canvas.rect(xy[0], xy[1], w, h);
        break;
      case 1:
        canvas.ellipse(xy[0], xy[1], w, h);
        break;
      default:
        canvas.circle(xy[0], xy[1], w);
        break;
    }

    canvas.endDraw();
  }

  private int[] RandomPositionOnMask(PImage mask, IRandomGenerator randomGenerator)
  {
    var x = randomGenerator.Value(mask.width);
    var y = randomGenerator.Value(mask.width);

    var i = (int) (y * mask.width + x);

    var c = mask.pixels[i];

    var a = Color.Alpha(c);

    if(a > 0.5)
    {
      return new int[] { x, y };
    }

    return RandomPositionOnMask(mask, randomGenerator);
  }
  
  public double Ease(Easing.IEasingFunction easingFunction, double time) 
  {
    return easingFunction.Ease(time);
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

    _drawGapMask = true;
    _gapNoiseGenerator.ReSeed();
    _positionRandomGenerator.ReSeed();
    NoiseGenerator.ReSeed();
    RandomGenerator.ReSeed();
  }
}