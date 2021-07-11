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

public class daily_20210704_b extends AbstractDaily 
{
  private boolean _enableWarp = false;
  private int _warpStrength = 4;

  private boolean _enableModulate = false;
  private float _modulateStrength = 0.03F;

  private boolean _enableBlend = true;

  private int _numberOfColorFields = 4;
  private List<ColorField> _colorFieldList = new ArrayList<ColorField>();

  private PGraphics _tempCanvas;

  private IRandomGenerator _colorRandomGenerator;

  private int _baseColor;

  private static final int ColorPickOptionComplementary = 0;
  private static final int ColorPickOptionSplitComplementary = 1;
  private static final int ColorPickOptionTriadic = 2;
  private static final int ColorPickOptionTetradic = 3;
  private static final int ColorPickOptionAnalagous = 4;
  private static final int ColorPickOptionMonochrome = 5;

  private static final List<Integer> ColorPickOptions = Arrays.asList(ColorPickOptionComplementary, ColorPickOptionSplitComplementary,
      ColorPickOptionTriadic, ColorPickOptionTetradic, ColorPickOptionAnalagous, ColorPickOptionMonochrome);
  
  private int _selectedColorPickOption = ColorPickOptionTriadic;

  public int _minMinLineThickness = 1;
  public int _maxMinLineThickness = _minMinLineThickness + 3;
  public int _minMaxLineThickness = 4;
  public int _maxMaxLineThickness = _minMaxLineThickness * 4;

  public int _mnMinLineDeviation = 0;
  public int _maxMinLineDeviation = 3;
  public int _minMaxLineDeviation = 4;
  public int _maxMaxLineDeviation = 16;

  public daily_20210704_b() 
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

    _colorRandomGenerator = new RandomGenerator(0);

    _tempCanvas = CreateAndSetupGraphics();

    var changeColorFieldsThicknessAndDeviationCallbackListener = new CallbackListener() {
      public void controlEvent(CallbackEvent theEvent) 
      {       
        _maxMinLineThickness = _minMinLineThickness + 3;
        
        if (_minMaxLineThickness < _maxMinLineThickness)
        {
          _minMaxLineThickness = _maxMinLineThickness;
        }
        
        _maxMaxLineThickness = _minMaxLineThickness * 4;
          
        SetupColorFieldsThicknessAndDeviation();
      }
    };

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    Gui.addToggle("_enableModulate").setPosition(guiX, guiY).setLabel("Enable Modulate").setValue(_enableModulate);

    guiY += guiOffsetY;

    Gui.addToggle("_enableBlend").setPosition(guiX, guiY).setLabel("Enable Blend").setValue(_enableBlend);

    guiY += guiOffsetY;

    Gui.addNumberbox("_modulateStrength")
        .setPosition(guiX, guiY).setLabel("Modulate Strength")
        .setScrollSensitivity(0.01F).setMultiplier(0.01F)
        .setRange(0.0F, 1.0F)
        .setValue(_modulateStrength);

    guiY += guiOffsetY;

    Gui.addNumberbox("_selectedColorPickOption")
        .setPosition(guiX, guiY).setLabel("Color Pick Mode")
        .setScrollSensitivity(1).setMultiplier(1)
        .setRange(0, ColorPickOptions.size() - 1)
        .setValue(_selectedColorPickOption);

    guiY += guiOffsetY;

    // Gui.addNumberbox("_numberOfColorFields").setPosition(guiX, guiY).setLabel("Number of Color Fields")
    //     .setValue(_numberOfColorFields).setScrollSensitivity(1).setMultiplier(1).setRange(1, 40);

    // guiY += guiOffsetY;

    Gui.addNumberbox("_minMinLineThickness").setPosition(guiX, guiY).setLabel("Min minimum line thickness")
        .setValue(_minMinLineThickness).setScrollSensitivity(1).setMultiplier(1).setRange(1, 128)
        .onChange(changeColorFieldsThicknessAndDeviationCallbackListener);

    guiY += guiOffsetY;

    // Gui.addNumberbox("_maxMinLineThickness").setPosition(guiX, guiY).setLabel("Max minimum line thickness")
    //     .setValue(_maxMinLineThickness).setScrollSensitivity(1).setMultiplier(1).setRange(_maxMinLineThickness, _maxMinLineThickness * 4)
    //     .onChange(changeColorFieldsThicknessAndDeviationCallbackListener);

    // guiY += guiOffsetY;

    Gui.addNumberbox("_minMaxLineThickness").setPosition(guiX, guiY).setLabel("Min maximum line thickness")
        .setValue(_minMaxLineThickness).setScrollSensitivity(1).setMultiplier(1).setRange(1, 512)
        .onChange(changeColorFieldsThicknessAndDeviationCallbackListener);

    guiY += guiOffsetY;

    RandomBaseColor();

    // for (var i = 1; i < 4; i++)
    // {
    //   var p = loadImage(PathCombine(ClassFolderInput(), "palette" + i + ".png"));

    //   p.loadPixels();

    //   _colorPalettes.add(p);
    // }

    // _colorPaletteIndex = RandomGenerator.Value(0, _colorPalettes.size());

    SetupAndPositionColorFields(false);
    SetupColorFieldsThicknessAndDeviation();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  private void RandomBaseColor()
  {
    _baseColor = Color20210703.Random();
  }
  
  private void SetupAndPositionColorFields(boolean reset)
  {
    if(_colorFieldList.size() >= _numberOfColorFields && !reset)
    {
      return;
    }

    if (reset) 
    {
      _colorFieldList.clear();
    }

    for (var i = _colorFieldList.size(); i < _numberOfColorFields; i++) {

      var c = new ColorField();

      PositionColorField(c, Canvas);

      _colorFieldList.add(c);
    }
  }
  
  private void ColorColorFields()
  {
    int[] colors;

    switch(_selectedColorPickOption)
    {
      case ColorPickOptionComplementary:
        colors = Color20210703.Complementary(_baseColor);
        break;
        case ColorPickOptionTriadic:
        colors = Color20210703.Triadic(_baseColor);
        break;
        case ColorPickOptionMonochrome:
        colors = Color20210703.Monochrome(_baseColor);
        break;
        case ColorPickOptionSplitComplementary:
        colors = Color20210703.SplitComplementary(_baseColor);
        break;
        case ColorPickOptionTetradic:
        colors = Color20210703.Tetradic(_baseColor);
        break;
      case ColorPickOptionAnalagous:
        colors = Color20210703.Analagous(_baseColor);
        break;
      default:
        throw new UnsupportedOperationException("Found unknown _selectedColorPickOption value: " + _selectedColorPickOption);
    }

    for (var colorField : _colorFieldList) 
    {
      ColorColorField(colorField, colors[RandomGenerator.Value(0, colors.length)]);
    }
  }
  
  private void SetupColorFieldsThicknessAndDeviation()
  {
    if (_maxMinLineThickness < _minMinLineThickness) {
      _maxMinLineThickness = _minMinLineThickness;
    }

    if (_maxMaxLineThickness < _minMaxLineThickness) {
      _maxMaxLineThickness = _minMaxLineThickness;
    }
    
    if (_minMaxLineThickness < _minMinLineThickness) {
      _minMaxLineThickness = _minMinLineThickness;
    }

    for (var colorField : _colorFieldList) 
    {
      colorField.MinLineThickness = RandomGenerator.Value(_minMinLineThickness, _maxMinLineThickness);
      colorField.MaxLineThickness = RandomGenerator.Value(_minMaxLineThickness, _maxMaxLineThickness);
    }
  }
  
  private ColorField PositionColorField(ColorField colorField, PGraphics canvas)
  {
    var xmargin = canvas.width / 20;
    var ymargin = canvas.height / 20;

    colorField.Width = RandomGenerator.Value(canvas.width / 4, canvas.width / 4 * 2);
    colorField.Height = RandomGenerator.Value(canvas.height / 4, canvas.height / 4 * 2);

    colorField.X = RandomGenerator.Value(xmargin, canvas.width - xmargin - colorField.Width);
    colorField.Y = RandomGenerator.Value(ymargin, canvas.height - ymargin - colorField.Height);

    colorField.Vertical = RandomGenerator.Boolean();

    return colorField;
  }
  
  private ColorField ColorColorField(ColorField colorField, int color)
  {
    colorField.Color = color;

    return colorField;
  }

  @Override
  public void Update(double elapsedTime) 
  {
    SetupAndPositionColorFields(false);
    ColorColorFields();

    RandomGenerator.ReSeed(RandomGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    var line = new Line();

    line.FromX = 100.00;
    line.FromY = 100.00;
    line.FromThickness = 1;
    line.FromColor = color(255, 0, 0, 210);

    line.ToX = 400.00;
    line.ToY = 100.00;
    line.ToThickness = 1;
    line.ToColor = color(255, 255, 0, 75);

    background(0);

    _tempCanvas.beginDraw();

    _tempCanvas.clear();

    _tempCanvas.background(255);

    _tempCanvas.stroke(0);
    _tempCanvas.fill(0);
    _tempCanvas.circle((float)line.FromX, (float)line.FromY, 5);
    _tempCanvas.circle((float) line.ToX, (float) line.ToY, 5);
    
    _tempCanvas.endDraw();

    DrawLine(line, _tempCanvas);

    SaveFrame(_tempCanvas);

    Canvas.beginDraw();

    Canvas.image(_tempCanvas, 0, 0, ScreenWidth, ScreenHeight);

    Canvas.endDraw();

    // for (var i = 0; i < _numberOfColorFields; i++)
    // {
    //   var colorField = _colorFieldList.get(i);

    //   _tempCanvas.beginDraw();
      
    //   _tempCanvas.clear();
      
    //   _tempCanvas.endDraw();
      
    //   DrawColorField(colorField, _tempCanvas);

    //   if (_enableModulate)
    //   {
    //     ModulateColors(_tempCanvas, _modulateStrength, colorField.ColorModulateNoiseGenerator);
    //   }

    //   if (_enableBlend)
    //   {
    //     BlendColors(Canvas, _tempCanvas, colorField.BlendMaskNoiseGenerator, colorField.BlendTransparencyNoiseGenerator);
    //   }

    //   Canvas.beginDraw();

    //   Canvas.image(_tempCanvas, 0, 0);

    //   Canvas.endDraw();
    // }

    if (_enableWarp) {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
 
  public void DrawLine(Line line, PGraphics canvas)
  {
    var p = line.Perpendicular();
    p = line.Normalize(p[0], p[1]);
    var halfFromThickness = line.FromThickness * 0.5;
    var halfToThickness = line.ToThickness * 0.5;

    canvas.beginDraw();

    canvas.noStroke();

    canvas.beginShape(QUAD);

    canvas.fill(line.FromColor);

    canvas.vertex((float)(line.FromX + (p[0] * halfFromThickness)), (float)(line.FromY + (p[1] * halfFromThickness)));
    canvas.vertex((float)(line.FromX - (p[0] * halfFromThickness)), (float)(line.FromY - (p[1] * halfFromThickness)));

    canvas.fill(line.ToColor);

    canvas.vertex((float)(line.ToX - (p[0] * halfToThickness)), (float)(line.ToY - (p[1] * halfToThickness)));
    canvas.vertex((float)(line.ToX + (p[0] * halfToThickness)), (float)(line.ToY + (p[1] * halfToThickness)));

    canvas.endShape();

    canvas.endDraw();
  }
  
  public static double[] PerpendicularClockwise(double x ,double y)
  {
    return new double[] { y, -x };
  }

  public void DrawColorField(ColorField colorField, PGraphics canvas)
  {
    _colorRandomGenerator.ReSeed(_colorRandomGenerator.Seed());

    var curvedeviation1 = RandomGenerator.Value(colorField.MinCurveDeviation, colorField.MaxCurveDeviation);
    var curvedeviation2 = RandomGenerator.Value(colorField.MinCurveDeviation, colorField.MaxCurveDeviation);

    var colors = Color20210703.Monochrome(colorField.Color);

    canvas.beginDraw();

    canvas.noStroke();

    canvas.beginShape(QUAD);

    if (colorField.Vertical)
    {
      for(var x = colorField.X; x < colorField.X + colorField.Width; )
      {
        var thickness = RandomGenerator.Value(colorField.MinLineThickness, colorField.MaxLineThickness);

        var xdeviation1 = RandomGenerator.Value(colorField.MinLineDeviation, colorField.MaxLineDeviation);
        xdeviation1 = RandomGenerator.Boolean() ? -xdeviation1 : xdeviation1;

        var xdeviation2 = RandomGenerator.Value(colorField.MinLineDeviation, colorField.MaxLineDeviation);
        xdeviation2 = RandomGenerator.Boolean() ? -xdeviation2 : xdeviation2;

        var curve1 = (int)(colorField.CurveNoiseGenerator.Value(x, colorField.Y) * curvedeviation1 * colorField.Height);
        var curve2 = (int)(colorField.CurveNoiseGenerator.Value(x, colorField.Y + colorField.Height) * curvedeviation2 * colorField.Height);
        curve1 += (int)(colorField.CurveInsetNoiseGenerator.Value(x, colorField.Y) * colorField.CurveInsetDeviation * colorField.Height);
        curve2 += (int) (colorField.CurveInsetNoiseGenerator.Value(x, colorField.Y + colorField.Height) * colorField.CurveInsetDeviation * colorField.Height);
        
        var x1 = x + xdeviation1;
        var x2 = x + xdeviation2;
        
        canvas.fill(colors[_colorRandomGenerator.Value(colors.length)]);
        
        canvas.vertex(x1, colorField.Y + curve1);
        canvas.vertex(x1 + thickness, colorField.Y + curve1);
        canvas.vertex(x2 + thickness, colorField.Y + curve2 + colorField.Height);
        canvas.vertex(x2, colorField.Y + curve2 + colorField.Height);

        x += thickness;
      }
    }
    else
    {
      for(var y = colorField.Y; y < colorField.Y + colorField.Height; )
      {
        var thickness = RandomGenerator.Value(colorField.MinLineThickness, colorField.MaxLineThickness);
        var ydeviation1 = RandomGenerator.Value(colorField.MinLineDeviation, colorField.MaxLineDeviation);
        ydeviation1 = RandomGenerator.Boolean() ? -ydeviation1 : ydeviation1;
        var ydeviation2 = RandomGenerator.Value(colorField.MinLineDeviation, colorField.MaxLineDeviation);
        ydeviation2 = RandomGenerator.Boolean() ? -ydeviation2 : ydeviation2;

        var curve1 = (int)(colorField.CurveNoiseGenerator.Value(colorField.X, y) * curvedeviation1 * colorField.Width);
        var curve2 = (int)(colorField.CurveNoiseGenerator.Value(colorField.X + colorField.Width, y) * curvedeviation2 * colorField.Width);
        curve1 += (int)(colorField.CurveInsetNoiseGenerator.Value(colorField.X, y) * colorField.CurveInsetDeviation * colorField.Width);
        curve2 += (int) (colorField.CurveInsetNoiseGenerator.Value(colorField.X + colorField.Width, y) * colorField.CurveInsetDeviation * colorField.Width);

        var y1 = y + ydeviation1;
        var y2 = y + ydeviation2;

        canvas.fill(colors[_colorRandomGenerator.Value(colors.length)]);
        
        canvas.vertex(colorField.X + curve1, y1);
        canvas.vertex(colorField.X + curve2 + colorField.Width, y1);
        canvas.vertex(colorField.X + curve2 + colorField.Width, y2 + thickness);
        canvas.vertex(colorField.X + curve1, y2 + thickness);

        y += thickness;
      }
    }

    canvas.endShape();

    canvas.endDraw();
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

    for (var y = 0; y < bottom.height; y++) 
    {
      for (var x = 0; x < bottom.width; x++) 
      {
        var index = (int) (y * bottom.height + x);

        if (bottom.pixels[index] != 0 && bottom.pixels[index] != white && top.pixels[index] != 0) 
        {
          var rgba = Color.IntToRgb(top.pixels[index]);

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

    RandomBaseColor();

    SetupAndPositionColorFields(true);

    SetupColorFieldsThicknessAndDeviation();
  }

  public class Line 
  {
    public double FromX;
    public double FromY;
    public double ToX;
    public double ToY;
    public double FromThickness = 1;
    public double ToThickness = 1;
    public int FromColor = color(0);
    public int ToColor = color(0);

    public double[] Direction()
    {
      return new double[] { ToX - FromX, ToY - FromY };
    }

    public double[] Perpendicular()
    {
      return new double[] { ToY - FromY, -(ToX - FromX) };
    }

    public double Length()
    {
      var d = Direction();

      return Math.Sqrt(d[0] * d[0] + d[1] * d[1]);
    }

    public double Length(double x, double y)
    {
      return Math.Sqrt(x * x + y * y);
    }

    public double[] Normalize(double x, double y)
    {
      var l = 1.0 / Length(x, y);

      return new double[] { x * l, y * l };
    }
  }

  public class ColorField {
    public int X;
    public int Y;
    public int Width;
    public int Height;
    public boolean Vertical = false;
    public int MinLineThickness = 2;
    public int MaxLineThickness = 5;
    public int MinLineDeviation = 0;
    public int MaxLineDeviation = 3;
    public float MinCurveDeviation = 0.05F;
    public float MaxCurveDeviation = 0.20F;
    public float CurveInsetDeviation = 0.05F;
    public INoiseGenerator ColorModulateNoiseGenerator;
    public INoiseGenerator CurveNoiseGenerator;
    public INoiseGenerator CurveInsetNoiseGenerator;

    public INoiseGenerator BlendMaskNoiseGenerator;
    public INoiseGenerator BlendTransparencyNoiseGenerator;

    public int Color;

    // public List<Integer> ColorList = new ArrayList<Integer>();

    public ColorField() {
      this(new java.util.Date().getTime());
    }

    public ColorField(long seed) {
      ColorModulateNoiseGenerator = new OpenSimplexNoiseGenerator(seed);
      ColorModulateNoiseGenerator.Octaves(8);
      ColorModulateNoiseGenerator.AllowNegativeValues(true);
      ColorModulateNoiseGenerator.ClampValues(true);
      ColorModulateNoiseGenerator.InputMultiplier(0.655, 0.655, 1.0);

      CurveNoiseGenerator = new OpenSimplexNoiseGenerator(seed);
      CurveNoiseGenerator.Octaves(2);
      CurveNoiseGenerator.AllowNegativeValues(true);
      CurveNoiseGenerator.ClampValues(true);
      CurveNoiseGenerator.InputMultiplier(0.0065, 0.0065, 1.0);

      CurveInsetNoiseGenerator = new OpenSimplexNoiseGenerator(seed);
      CurveInsetNoiseGenerator.Octaves(8);
      CurveInsetNoiseGenerator.AllowNegativeValues(true);
      CurveInsetNoiseGenerator.ClampValues(true);
      CurveInsetNoiseGenerator.InputMultiplier(0.075, 0.075, 1.0);

      BlendMaskNoiseGenerator = new OpenSimplexNoiseGenerator(seed);
      BlendMaskNoiseGenerator.Octaves(4);
      BlendMaskNoiseGenerator.AllowNegativeValues(false);
      BlendMaskNoiseGenerator.ClampValues(true);
      BlendMaskNoiseGenerator.InputMultiplier(0.0050, 0.0050, 1.0);

      BlendTransparencyNoiseGenerator = new OpenSimplexNoiseGenerator(seed);
      BlendTransparencyNoiseGenerator.Octaves(4);
      BlendTransparencyNoiseGenerator.AllowNegativeValues(false);
      BlendTransparencyNoiseGenerator.ClampValues(true);
      BlendTransparencyNoiseGenerator.InputMultiplier(0.0005, 0.0005, 1.0);
    }
  }

}