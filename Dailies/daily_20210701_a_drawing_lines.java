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
// import Utility.*;
import Utility.Color20210703;
import Utility.Easing;
import Utility.Math;

public class daily_20210701_a_drawing_lines extends AbstractDaily 
{
  private boolean _enableWarp = true;
  private int _warpStrength = 4;

  private boolean _blackBackground = false;

  private int _numberOfColorFields = 3;
  private List<ColorField> _colorFieldList = new ArrayList<ColorField>();

  private PGraphics _tempCanvas;
  
  private PImage _colorPick;

  public class ColorField
  {
    public int X;
    public int Y;
    public int Width;
    public int Height;
    public boolean Vertical = false;
    public int LineThickness = 2;
    public float CurveDeviation = 0.15F;
    public float CurveInsetDeviation = 0.05F;
    public INoiseGenerator ColorNoiseGenerator;
    public INoiseGenerator CurveNoiseGenerator;
    public INoiseGenerator CurveInsetNoiseGenerator;
    public List<Integer> ColorList = new ArrayList<Integer>();
    
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
      CurveInsetNoiseGenerator.InputMultiplier(0.2, 0.2, 1.0);
    }
    
    public void AddColor(int color)
    {
      ColorList.add(color);
    }

    public void Draw(PGraphics canvas)
    {
      canvas.beginDraw();

      canvas.noStroke();

      canvas.beginShape(QUAD);

      if (Vertical)
      {
        for(var x = X; x < X + Width; x += LineThickness)
        {
          var curve1 = (int)(CurveNoiseGenerator.Value(x, Y) * CurveDeviation * Height);
          var curve2 = (int)(CurveNoiseGenerator.Value(x + LineThickness, Y) * CurveDeviation * Height);
          curve1 += (int)(CurveInsetNoiseGenerator.Value(x + LineThickness, Y + Height) * CurveInsetDeviation * Height);
          curve2 += (int) (CurveInsetNoiseGenerator.Value(x, Y + Height) * CurveInsetDeviation * Height);

          canvas.fill(0);
          
          canvas.vertex(x, Y + curve1);
          canvas.vertex(x + LineThickness, Y + curve1);
          canvas.vertex(x + LineThickness, Y + curve2 + Height);
          canvas.vertex(x, Y + curve2 + Height);
        }
      }
      else
      {
        for(var y = Y; y < Y + Height; y += LineThickness)
        {
          var curve1 = (int)(CurveNoiseGenerator.Value(X, y) * CurveDeviation * Width);
          var curve2 = (int)(CurveNoiseGenerator.Value(X + Width, y) * CurveDeviation * Width);
          curve1 += (int)(CurveInsetNoiseGenerator.Value(X, y) * CurveInsetDeviation * Width);
          curve2 += (int) (CurveInsetNoiseGenerator.Value(X + Width, y) * CurveInsetDeviation * Width);

          canvas.fill(0);
          
          canvas.vertex(X + curve1, y);
          canvas.vertex(X + curve2 + Width, y);
          canvas.vertex(X + curve2 + Width, y + LineThickness);
          canvas.vertex(X + curve1, y + LineThickness);
        }
      }

      canvas.endShape();

      canvas.endDraw();
    }
  }

  public daily_20210701_a_drawing_lines() 
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

    _tempCanvas = createGraphics(Canvas.width, Canvas.height, Renderer);

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    Gui.addToggle("_blackBackground").setPosition(guiX, guiY).setLabel("Black Background").setValue(_blackBackground);

    guiY += guiOffsetY;

    Gui.addToggle("_enableWarp").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_enableWarp);

    guiY += guiOffsetY;

    Gui.addNumberbox("_warpStrength").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_warpStrength)
      .setScrollSensitivity(1).setMultiplier(1).setRange(0, 200);

    guiY += guiOffsetY;

    _colorPick = loadImage(PathCombine(ClassFolderInput(), "colors.png"));
    _colorPick.loadPixels();

    for (var i = 0; i < _numberOfColorFields; i++) {
      
      var c = new ColorField(i);

      PositionColorField(c, Canvas);
      ColorColorField(c, Canvas);

      _colorFieldList.add(c);
    }

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }
  
  private ColorField PositionColorField(ColorField colorField, PGraphics canvas)
  {
    colorField.X = RandomGenerator.Value(canvas.width / 4, canvas.width / 2);
    colorField.Y = RandomGenerator.Value(canvas.height / 4, canvas.height / 2);
    colorField.Width = RandomGenerator.Value(canvas.width / 4, canvas.width / 2);
    colorField.Height = RandomGenerator.Value(canvas.height / 4, canvas.height / 2);
    colorField.Vertical = RandomGenerator.Boolean();

    return colorField;
  }
  
  private ColorField ColorColorField(ColorField colorField, PGraphics canvas)
  {
    var row = (int) RandomGenerator.Value(0, _colorPick.height);

    // colorField.ColorList.clear();

    colorField.ColorList.add(_colorPick.pixels[row * _colorPick.width + 0]);
    colorField.ColorList.add(_colorPick.pixels[row * _colorPick.width + 1]);
    colorField.ColorList.add(_colorPick.pixels[row * _colorPick.width + 2]);
    colorField.ColorList.add(_colorPick.pixels[row * _colorPick.width + 3]);

    return colorField;
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

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(_blackBackground ? 0 : 255);

    Canvas.endDraw();

    for (var colorField : _colorFieldList)
    {
      _tempCanvas.beginDraw();
      
      _tempCanvas.clear();
      
      _tempCanvas.endDraw();
      
      colorField.Draw(_tempCanvas);

      ModulateColors(_tempCanvas, colorField.ColorList, colorField.ColorNoiseGenerator);

      Canvas.beginDraw();

      Canvas.image(_tempCanvas, 0, 0);

      Canvas.endDraw();
    }

    if (_enableWarp) {
      Warp(Canvas, _warpStrength, NoiseGenerator);
    }

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
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

    for (var colorField : _colorFieldList) 
    {
      PositionColorField(colorField, Canvas);
      ColorColorField(colorField, Canvas);
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