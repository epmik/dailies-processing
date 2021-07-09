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
// import Utility.*;
import Utility.Color20210703;
import Utility.Easing;
import Utility.Math;

public class daily_20210703_a extends AbstractDaily 
{
  private boolean _enableWarp = false;
  private int _warpStrength = 2;

  private boolean _blackBackground = false;

  private int _numberOfColorFields = 3;
  private List<ColorField> _colorFieldList = new ArrayList<ColorField>();

  private PGraphics _tempCanvas;

  private IRandomGenerator _colorRandomGenerator;
  
  // private List<PImage> _colorPalettes = new ArrayList<PImage>();
  // private int _colorPaletteIndex;

  private int _baseColor;

  private static final int ColorPickOptionComplementary = 0;
  private static final int ColorPickOptionSplitComplementary = 1;
  private static final int ColorPickOptionTriadic = 2;
  private static final int ColorPickOptionTetradic = 3;
  private static final int ColorPickOptionAnalagous = 4;
  private static final int ColorPickOptionMonochrome = 5;

  private static final List<Integer> ColorPickOptions = Arrays.asList(ColorPickOptionComplementary, ColorPickOptionSplitComplementary,
      ColorPickOptionTriadic, ColorPickOptionTetradic, ColorPickOptionAnalagous, ColorPickOptionMonochrome);
  
  private int _selectedColorPickOption = ColorPickOptionComplementary;

  public class ColorField
  {
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
    public INoiseGenerator ColorNoiseGenerator;
    public INoiseGenerator CurveNoiseGenerator;
    public INoiseGenerator CurveInsetNoiseGenerator;

    public int Color;
    
    // public List<Integer> ColorList = new ArrayList<Integer>();
    
    public ColorField() 
    {
      this(new java.util.Date().getTime());
    }

    public ColorField(long seed) 
    {
      ColorNoiseGenerator = new OpenSimplexNoiseGenerator(seed);
      ColorNoiseGenerator.Octaves(2);
      ColorNoiseGenerator.AllowNegativeValues(false);
      ColorNoiseGenerator.ClampValues(true);
      ColorNoiseGenerator.InputMultiplier(0.85, 0.85, 1.0);

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
    }
    
    // public void AddColor(int color)
    // {
    //   ColorList.add(color);
    // }
  }

  public daily_20210703_a() 
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

    _tempCanvas = createGraphics(Canvas.width, Canvas.height, Renderer);

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    Gui.addNumberbox("_selectedColorPickOption")
        .setPosition(guiX, guiY).setLabel("Color Pick Mode")
        .setScrollSensitivity(1).setMultiplier(1)
        .setRange(0, ColorPickOptions.size() - 1)
        .setValue(_selectedColorPickOption);

    guiY += guiOffsetY;

    Gui.addNumberbox("_numberOfColorFields").setPosition(guiX, guiY).setLabel("Number of Color Fields")
        .setValue(_numberOfColorFields).setScrollSensitivity(1).setMultiplier(1).setRange(1, 40);

    guiY += guiOffsetY;

    Gui.addToggle("_enableWarp").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_enableWarp);

    guiY += guiOffsetY;

    Gui.addNumberbox("_warpStrength").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_warpStrength)
        .setScrollSensitivity(1).setMultiplier(1).setRange(0, 200);

    guiY += guiOffsetY;

    RandomBaseColor();

    // for (var i = 1; i < 4; i++)
    // {
    //   var p = loadImage(PathCombine(ClassFolderInput(), "palette" + i + ".png"));

    //   p.loadPixels();

    //   _colorPalettes.add(p);
    // }

    // _colorPaletteIndex = RandomGenerator.Value(0, _colorPalettes.size());

    SetupColorFields(false);

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  private void RandomBaseColor()
  {
    _baseColor = Color20210703.Random();
  }
  
  private void SetupColorFields(boolean reset)
  {
    if(_colorFieldList.size() >= _numberOfColorFields && !reset)
    {
      return;
    }

    if (reset) 
    {
      _colorFieldList.clear();
    }

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

    for (var i = _colorFieldList.size(); i < _numberOfColorFields; i++) {

      var c = new ColorField();

      PositionColorField(c, Canvas);
      ColorColorField(c, colors[RandomGenerator.Value(0, colors.length)], Canvas);

      _colorFieldList.add(c);
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
  
  private ColorField ColorColorField(ColorField colorField, int color, PGraphics canvas)
  {
    colorField.Color = color;

    return colorField;
  }

  @Override
  public void Update(double elapsedTime) 
  {
    SetupColorFields(false);
    RandomGenerator.ReSeed(RandomGenerator.Seed());
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(_blackBackground ? 0 : 255);

    Canvas.endDraw();

    for (var i = 0; i < _numberOfColorFields; i++)
    {
      var colorField = _colorFieldList.get(i);

      _tempCanvas.beginDraw();
      
      _tempCanvas.clear();
      
      _tempCanvas.endDraw();
      
      DrawColorField(colorField,_tempCanvas);

      // ModulateColors(_tempCanvas, colorField.ColorList, colorField.ColorNoiseGenerator);

      Canvas.beginDraw();

      Canvas.image(_tempCanvas, 0, 0);

      Canvas.endDraw();
    }

    if (_enableWarp) {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
 
  public void DrawColorField(ColorField colorField, PGraphics canvas)
  {
    _colorRandomGenerator.ReSeed(_colorRandomGenerator.Seed());

    var deviation1 = RandomGenerator.Value(colorField.MinCurveDeviation, colorField.MaxCurveDeviation);
    var deviation2 = RandomGenerator.Value(colorField.MinCurveDeviation, colorField.MaxCurveDeviation);

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

        var curve1 = (int)(colorField.CurveNoiseGenerator.Value(x, colorField.Y) * deviation1 * colorField.Height);
        var curve2 = (int)(colorField.CurveNoiseGenerator.Value(x, colorField.Y + colorField.Height) * deviation2 * colorField.Height);
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

        var curve1 = (int)(colorField.CurveNoiseGenerator.Value(colorField.X, y) * deviation1 * colorField.Width);
        var curve2 = (int)(colorField.CurveNoiseGenerator.Value(colorField.X + colorField.Width, y) * deviation2 * colorField.Width);
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
    
  private void ModulateColors(PGraphics canvas, List<Integer> colors, INoiseGenerator noiseGenerator) 
  {
    canvas.beginDraw();

    canvas.loadPixels();

    for (var y = 0; y < canvas.height; y++) 
    {
      for (var x = 0; x < canvas.width; x++) {
        var index = (int) (y * canvas.height + x);

        if (canvas.pixels[index] == 0) {
          continue;
        }

        canvas.pixels[index] = color(colors.get((int) (noiseGenerator.Value(x, y) * (colors.size() - 1))));
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

    RandomBaseColor();

    System.out.println(Color20210703.Hue(_baseColor));
    
    SetupColorFields(true);
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