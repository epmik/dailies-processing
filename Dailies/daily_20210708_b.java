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
import peasy.*;

import Procedural.*;
import Procedural.Interfaces.INoiseGenerator;
import Procedural.Interfaces.IRandomGenerator;
import Utility.Color;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// import Utility.*;
import Utility.Color20210703;
import Utility.ColorPicker;
import Utility.Easing;
import Utility.Lists;
import Utility.Math;

public class daily_20210708_b extends AbstractDaily 
{
  private float _directionalLightRotation;
  private PeasyCam _camera;
  private boolean _displayAxis;
  private boolean _enableLights = true;

  private int _numberOfLines = 400;
  private float _minLineWeight = 1.0f;
  private float _maxLineWeight = 12.0f;
  private float _minLineLength = 80.0f;
  private float _maxLineLength = 320.0f;
  private float _minLineOffset = 160.0f;
  private float _maxLineOffset = 1200.0f;

  private int _numberOfDots = 400;
  private float _minDotWeight = 1.0f;
  private float _maxDotWeight = 16.0f;
  private float _minDotOffset = 160.0f;
  private float _maxDotOffset = 1200.0f;

  private int _numberOfTriangles = 200;
  private float _minTriangleWidth = 8.0f;
  private float _maxTriangleWidth = 800.0f;
  private float _minTriangleRatio = 0.01f;
  private float _maxTriangleRatio = 0.40f;
  private float _minTriangleOffset = 80.0f;
  private float _maxTriangleOffset = 1200.0f;

  private int _fromColor = color(255);
  private controlP5.ColorPicker _fromColorColorPicker;
  private int _toColor = color(0);
  private controlP5.ColorPicker _toColorColorPicker;

  private CallbackListener _resetCallbackListener;

  private ArrayList<IExplode> _explodeArrayList = new ArrayList<IExplode>();

  private ArrayList<ColorPicker> _colorPickerArrayList = new ArrayList<ColorPicker>();

  public daily_20210708_b() 
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

    _fromColor = Color.HsbToInt(0.0, 0.0, RandomGenerator.Value(0.00, 0.15));
    _toColor = Color.HsbToInt(0.0, 0.0, RandomGenerator.Value(0.00, 0.15));

    SetupCameraAndPerspective(Canvas);

    _camera.setActive(false);

    SetupGui();
    
    SetupRandomExplodeObjects();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }
  
  private void SetupGui() 
  {
    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    _resetCallbackListener = new CallbackListener() 
    {
      public void controlEvent(CallbackEvent event) 
      {
        _explodeArrayList.clear();
        SetupRandomExplodeObjects();
      }
    };

    Gui.addToggle("_enableLights").setPosition(guiX, guiY).setLabel("Enable Lights").setValue(_enableLights);

    guiY += guiOffsetY;

    Gui.addToggle("_displayAxis").setPosition(guiX, guiY).setLabel("Display Axis").setValue(_displayAxis);

    guiY += guiOffsetY;

    _fromColorColorPicker = Gui.addColorPicker("_fromColor").setPosition(guiX, guiY).setLabel("From Color").setColorValue(_fromColor);

    guiY += guiOffsetY;
    guiY += guiOffsetY;

    _toColorColorPicker = Gui.addColorPicker("_toColor").setPosition(guiX, guiY).setLabel("To Color").setColorValue(_toColor);

    guiY += guiOffsetY;
    guiY += guiOffsetY;
    
    SetupLineSettingsGui(guiOffsetY, guiOffsetY, guiOffsetY);

    SetupDotsSettingsGui(guiOffsetY, guiOffsetY, guiOffsetY);

    SetupTriangleSettingsGui(guiOffsetY, guiOffsetY, guiOffsetY);
  }
  
  private void SetupLineSettingsGui(int x, int y, int yoffset) 
  {
    var tabtitle = "Line Settings";

    Gui.addNumberbox("_numberOfLines")
        .setPosition(x, y).setLabel("Number Of Lines")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_numberOfLines)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minLineWeight")
        .setPosition(x, y).setLabel("Min Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_minLineWeight)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineWeight")
        .setPosition(x, y).setLabel("Max Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_maxLineWeight)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minLineLength")
        .setPosition(x, y).setLabel("Min Line Length")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 800)
        .setValue(_minLineLength)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineLength")
        .setPosition(x, y).setLabel("Max Line Length")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 800)
        .setValue(_maxLineLength)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
    
    Gui.addNumberbox("_minLineOffset")
        .setPosition(x, y).setLabel("Min Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 800)
        .setValue(_minLineOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineOffset")
        .setPosition(x, y).setLabel("Max Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 800)
        .setValue(_maxLineOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
  }
  
  private void SetupDotsSettingsGui(int x, int y, int yoffset) 
  {
    var tabtitle = "Dot Settings";

    Gui.addNumberbox("_numberOfDots")
        .setPosition(x, y).setLabel("Number Of Dots")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 1600)
        .setValue(_numberOfDots)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minDotWeight")
        .setPosition(x, y).setLabel("Min Dots Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_minDotWeight)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxDotWeight")
        .setPosition(x, y).setLabel("Max Dots Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_maxDotWeight)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
    
    Gui.addNumberbox("_minDotOffset")
        .setPosition(x, y).setLabel("Min Dots Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 800)
        .setValue(_minDotOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxDotOffset")
        .setPosition(x, y).setLabel("Max Dots Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 800)
        .setValue(_maxDotOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
  }
    
  private void SetupTriangleSettingsGui(int x, int y, int yoffset) 
  {
    var tabtitle = "Triangle Settings";

    Gui.addNumberbox("_numberOfTriangles")
        .setPosition(x, y).setLabel("Number Of Triangles")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 1500)
        .setValue(_numberOfTriangles)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minTriangleWidth")
        .setPosition(x, y).setLabel("Min Triangle Width")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(8, 800)
        .setValue(_minTriangleWidth)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxTriangleWidth")
        .setPosition(x, y).setLabel("Max Triangle Width")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(8, 800)
        .setValue(_maxTriangleWidth)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minTriangleRatio")
        .setPosition(x, y).setLabel("Min Triangle Ratio")
        .setDecimalPrecision(2)
        .setScrollSensitivity(0.01F).setMultiplier(0.01F)
        .setRange(0.01f, 2.00f)
        .setValue(_minTriangleRatio)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxTriangleRatio")
        .setPosition(x, y).setLabel("Max Triangle Ratio")
        .setDecimalPrecision(2)
        .setScrollSensitivity(0.01F).setMultiplier(0.01F)
        .setRange(0.01f, 2.00f)
        .setValue(_maxTriangleRatio)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
    
    Gui.addNumberbox("_minTriangleOffset")
        .setPosition(x, y).setLabel("Min Triangle Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 800)
        .setValue(_minTriangleOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxTriangleOffset")
        .setPosition(x, y).setLabel("Max Triangle Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 800)
        .setValue(_maxTriangleOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
  }
  
  private void SetupCameraAndPerspective(PGraphics canvas)
  {
    SetupCameraAndPerspective(canvas, false);
  }
  
  private void SetupCameraAndPerspective(PGraphics canvas, boolean orthographic)
  {
    var fov = (float) (Math.Pi / 3);
    var z = (float) ((Canvas.height / 2.0F) / Math.Tan(fov / 2.0));

    if (_camera == null) {
      _camera = new PeasyCam(this, canvas, z);
    }

    _camera.setMinimumDistance(z / 10.0F);
    _camera.setMaximumDistance(z * 1000.0F);
    _camera.setFreeRotationMode();
    _camera.lookAt(canvas.width / 2.0F, canvas.height / 2.0F, 0, z);

    if (orthographic) {
      Canvas.ortho(-Canvas.width / 2, Canvas.width / 2, -Canvas.height / 2, Canvas.height / 2);
    } else {
      Canvas.perspective(fov, (float) (Canvas.width) / (float) (Canvas.height), z / 10.0F, z * 1000.0F);
    }
  }
  
  private void SetupRandomExplodeObjects()
  {
    for (var i = 0; i < _numberOfLines; i++)
    {
      var line = RandomLine(_minLineLength, _maxLineLength, _minLineOffset, _maxLineOffset, RandomGenerator);

      line.Weight((float) RandomGenerator.Value(_minLineWeight, _maxLineWeight));

      _explodeArrayList.add(line);
    }

    for (var i = 0; i < _numberOfDots; i++)
    {
      var dot = RandomDot(_minDotWeight, _maxDotWeight, _minDotOffset, _maxDotOffset, RandomGenerator);

      _explodeArrayList.add(dot);
    }


    for (var i = 0; i < _numberOfTriangles; i++)
    {
      var triangle = RandomTriangle(_minTriangleWidth, _maxTriangleWidth, _minTriangleRatio, _maxTriangleRatio,
          _minTriangleOffset, _maxTriangleOffset, RandomGenerator);

      _explodeArrayList.add(triangle);
    }
  
    ColorizeExplodeObjects();
  }
  
  private void ColorizeExplodeObjects()
  {
    _fromColor = _fromColorColorPicker.getColorValue();
    _toColor = _toColorColorPicker.getColorValue();

    _colorPickerArrayList.clear();

    _colorPickerArrayList.add(
        new ColorPicker(
          _fromColor,
          _toColor,
          RandomGenerator));
        
     for (var e : _explodeArrayList)
     {
       e.Colorize(Lists.Random(_colorPickerArrayList));

      //  var c = e.getClass();
      
      //  if (Line.class.getTypeName() == c.getTypeName())
      //  {
      //   var colorPicker = Lists.Random(_colorPickerArrayList);

      //   ((Line)e).ColorOrigin(colorPicker.RandomColor());
      //   ((Line) e).ColorExtent(colorPicker.RandomColor());
        
      //   continue;
      //  }
      
      //  if (Dot.class.getTypeName() == c.getTypeName())
      //  {
      //   var colorPicker = Lists.Random(_colorPickerArrayList);

      //   ((Dot)e).Color(colorPicker.RandomColor());
        
      //   continue;
      //  }
      
      //  if (Dot.class.getTypeName() == c.getTypeName())
      //  {
      //   var colorPicker = Lists.Random(_colorPickerArrayList);

      //   ((Dot)e).Color(colorPicker.RandomColor());
        
      //   continue;
      //  }
     }
  }

  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
    NoiseGenerator.ReSeed(NoiseGenerator.Seed());

    // _boxRotation += 0.010;
    _directionalLightRotation += 0.025;

    var fromColor = _fromColorColorPicker.getColorValue();
    var toColor = _toColorColorPicker.getColorValue();

    if(_fromColor != fromColor || _toColor != toColor)
    {
      ColorizeExplodeObjects();
    }
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(255);

    Canvas.strokeWeight(1);
    Canvas.stroke(255);
    Canvas.fill(125);

    if (_enableLights)
    {
      Canvas.lights();

      Canvas.ambientLight(5, 5, 5);
  
      // Canvas.directionalLight(243, 102, 126, (float) Math.cos(_directionalLightRotation), (float) Math.cos(_directionalLightRotation),
      //     (float) Math.sin(_directionalLightRotation));
  
      // Canvas.directionalLight(240, 226, 0, (float) Math.cos(_directionalLightRotation), (float) Math.cos(_directionalLightRotation),
      //   (float) Math.sin(_directionalLightRotation));
  
      Canvas.directionalLight(151, 128, 251, (float) Math.Cos(_directionalLightRotation), (float) Math.Cos(_directionalLightRotation),
        (float) Math.Sin(_directionalLightRotation));
    }
    else
    {
      Canvas.noLights();
    }

    {
      Canvas.pushMatrix();

      Canvas.translate(Canvas.width / 2, Canvas.height / 2, -100F);

      if(_displayAxis)
      {
        DrawAxis();
      }
      
      {
        Canvas.pushMatrix();
      
        for (var explode : _explodeArrayList) 
        {
          explode.Draw(Canvas);
        }
  
        Canvas.popMatrix();
      }
      
      Canvas.popMatrix();
    }
 
    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);

    // noLoop();
  }
 
  private void DrawAxis() 
  {
    Canvas.pushMatrix();

    Canvas.strokeCap(ROUND);

    Canvas.beginShape(LINES);

    Canvas.noFill();

    Canvas.strokeWeight(1);
    Canvas.stroke(255, 0, 0);
    Canvas.vertex(0, 0, 0);
    Canvas.vertex(500, 0, 0);

    Canvas.strokeWeight(1);
    Canvas.stroke(0, 255, 0);
    Canvas.vertex(0, 0, 0);
    Canvas.vertex(0, 500, 0);

    Canvas.strokeWeight(1);
    Canvas.stroke(0, 0, 255);
    Canvas.vertex(0, 0, 0);
    Canvas.vertex(0, 0, 500);

    Canvas.endShape();

    Canvas.popMatrix();
  }
 
  public void keyPressed()
  {
    super.keyPressed();

    if (key == CODED && keyCode == SHIFT)
    {
      _camera.setActive(true);
    }
  }
 
  public void keyReleased()
  {
    super.keyReleased();

    if (key == CODED && keyCode == SHIFT)
    {
      _camera.setActive(false);
    }
  }

  public void mouseReleased() 
  {
    super.mouseReleased();

    if (!PropagateMouseEvents) {
      return;
    }

    if (mouseButton == RIGHT) 
    {
      _explodeArrayList.clear();
      SetupRandomExplodeObjects();
    }
  }
 
  Line RandomLine(float minlength, float maxlength, float minoffset, float maxoffset, IRandomGenerator randomGenerator)
  {
    return RandomLine(
      (float)randomGenerator.Value(minlength, maxlength),
      (float)Math.Map(Easing.InCubic(randomGenerator.Value()), minoffset, maxoffset), 
      randomGenerator);
  }
  
  Line RandomLine(float length, float offset, IRandomGenerator randomGenerator)
  {
    var line = new Line();
    
    var ll = Math.RandomSphericalCoordinates(randomGenerator);

    line.Offset(offset);
    line.Length(length);
    line.Longitude((float)ll[0]);
    line.Latitude((float)ll[1]);

    return line;
  }
 
  Dot RandomDot(float minweight, float maxweight, float minoffset, float maxoffset, IRandomGenerator randomGenerator)
  {
    return RandomDot((float)randomGenerator.Value(minweight, maxweight), (float)Math.Map(Easing.InCubic(randomGenerator.Value()), minoffset, maxoffset),
        randomGenerator);
  }
  
  Dot RandomDot(float weight, float offset, IRandomGenerator randomGenerator)
  {
    var dot = new Dot();
    
    var p = Math.RandomPointOnSphere(offset, randomGenerator);

    dot.X((float)p[0]);
    dot.Y((float)p[1]);
    dot.Z((float)p[2]);
    
    dot.Weight(weight);

    return dot;
  }
  
  Triangle RandomTriangle(float minwidth, float maxwidth, float minratio, float maxratio, float minoffset, float maxoffset, IRandomGenerator randomGenerator)
  {
    return RandomTriangle(
        (float) randomGenerator.Value(minwidth, maxwidth),
        (float) randomGenerator.Value(minratio, maxratio), 
        (float)Math.Map(Easing.InCubic(randomGenerator.Value()), minoffset, maxoffset), 
      randomGenerator);
  }
  
  Triangle RandomTriangle(float width, float ratio, float offset, IRandomGenerator randomGenerator)
  {
    var triangle = new Triangle();

    triangle.Longitude((float)randomGenerator.Value(0, Math.Pi * 2));
    triangle.Latitude((float) randomGenerator.Value(0, Math.Pi * 2));

    triangle.Offset(offset);
    
    triangle.Point1(0, 0, 0);
    triangle.Point2(width, 0, 0);
    triangle.Point3((float)randomGenerator.Value(0, width + width * 0.33), width * ratio, 0);

    return triangle;
  }

  interface IExplode 
  {
    void Draw(PGraphics canvas);
    void Colorize(ColorPicker colorPicker);
  }
  
  public abstract class AbstractExplode implements IExplode
  {
    protected float _x;
    protected float _y;
    protected float _z;

    public float X() {
      return _x;
    }

    public void X(float x) {
      _x = x;
    }

    public float Y() {
      return _y;
    }

    public void Y(float y) {
      _y = y;
    }

    public float Z() {
      return _z;
    }

    public void Z(float z) {
      _z = z;
    }

    public abstract void Draw(PGraphics canvas);

    public abstract void Colorize(ColorPicker colorPicker);
  }

  class Line extends AbstractExplode
  {
    private float _length = 1.0F;
    private float _offset;
    private float _longitude;
    private float _latitude;
    private float _weight = 1.0F;
    private int _colorOrigin = color(0);
    private int _colorExtent = color(255);
    private float[] _unitvector = null;
    private float[] _offsetvector = null;
    private float[] _vector = null;

    public Line() {
    }

    public float Length() {
      return _length;
    }

    public void Length(float length) {
      _unitvector = null;
      _length = length;
    }

    public float Offset() {
      return _offset;
    }

    public void Offset(float offset) {
      _unitvector = null;
      _offset = offset;
    }

    public float Weight() {
      return _weight;
    }

    public void Weight(float weight) {
      _weight = weight;
    }

    public float Longitude() {
      return _longitude;
    }

    public void Longitude(float longitude) {
      _unitvector = null;
      _longitude = longitude;
    }

    public float Latitude() {
      return _latitude;
    }

    public void Latitude(float latitude) {
      _unitvector = null;
      _latitude = latitude;
    }

    public int ColorOrigin() {
      return _colorOrigin;
    }

    public void ColorOrigin(int colorOrigin) {
      _colorOrigin = colorOrigin;
    }

    public int ColorExtent() {
      return _colorExtent;
    }

    public void ColorExtent(int colorExtent) {
      _colorExtent = colorExtent;
    }

    public void Draw(PGraphics canvas)
    {
      if (_unitvector == null) {
        var v = Math.VectorFromSphericalCoordinates(_longitude, _latitude);

        _unitvector = new float[] { (float) v[0], (float) v[1], (float) v[2] };
        _vector = new float[] { _unitvector[0] * _length, _unitvector[1] * _length, _unitvector[2] * _length };
        _offsetvector = new float[] { _unitvector[0] * _offset, _unitvector[1] * _offset, _unitvector[2] * _offset };
      }

      canvas.beginShape(LINES);

      canvas.strokeWeight(_weight);
      canvas.noFill();

      canvas.stroke(_colorOrigin);
      canvas.vertex(_x + _offsetvector[0], _y + _offsetvector[1], _z + _offsetvector[2]);

      canvas.stroke(_colorExtent);
      canvas.vertex(_x + _offsetvector[0] + _vector[0], _y + _offsetvector[1] + _vector[1],
          _z + _offsetvector[2] + _vector[2]);

      canvas.endShape();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      ColorOrigin(colorPicker.RandomColor());
      ColorExtent(colorPicker.RandomColor());
    }
  }

  class Dot extends AbstractExplode {

    private float _weight = 1.0F;
    private int _color = color(0);

    public Dot() {
    }

    public float Weight() {
      return _weight;
    }

    public void Weight(float weight) {
      _weight = weight;
    }

    public int Color() {
      return _color;
    }

    public void Color(int color) {
      _color = color;
    }

    public void Draw(PGraphics canvas) {
      canvas.beginShape(POINTS);

      canvas.strokeWeight(_weight);
      canvas.noFill();

      canvas.stroke(_color);
      canvas.vertex(_x, _y, _z);

      canvas.endShape();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      Color(colorPicker.RandomColor());
    }
  }

  class Triangle extends AbstractExplode
  {
    private float[][] _points = new float[3][3];
    private float _longitude;
    private float _latitude;
    private int[] _colors = new int[3];
    private float _offset;

    public float Offset() {
      return _offset;
    }

    public void Offset(float offset) {
      _offset = offset;
    }

    public float[] Point1()
    {
      return _points[0];
    }

    public void Point1(float[] xyz)
    {
      Point1(xyz[0], xyz[1], xyz[2]);
    }

    public void Point1(float x, float y, float z)
    {
      _points[0][0] = x;
      _points[0][1] = y;
      _points[0][2] = z;
    }

    public float[] Point2()
    {
      return _points[1];
    }

    public void Point2(float[] xyz)
    {
      Point2(xyz[0], xyz[1], xyz[2]);
    }

    public void Point2(float x, float y, float z)
    {
      _points[1][0] = x;
      _points[1][1] = y;
      _points[1][2] = z;
    }

    public float[] Point3()
    {
      return _points[2];
    }

    public void Point3(float[] xyz)
    {
      Point3(xyz[0], xyz[1], xyz[2]);
    }

    public void Point3(float x, float y, float z)
    {
      _points[2][0] = x;
      _points[2][1] = y;
      _points[2][2] = z;
    }

    public float Longitude()
    {
      return _longitude;
    }

    public void Longitude(float longitude)
    {
      _longitude = longitude;
    }

    public float Latitude()
    {
      return _latitude;
    }

    public void Latitude(float latitude)
    {
      _latitude = latitude;
    }

    public int[] Colors()
    {
      return _colors;
    }

    public void Colors(int[] colors)
    {
      Colors(colors[0], colors[1], colors[2]);
    }

    public void Colors(int a, int b, int c)
    {
      _colors[0] = a;
      _colors[1] = b;
      _colors[2] = c;
    }

    public void Draw(PGraphics canvas)
    {
      canvas.pushMatrix();

      canvas.rotateY(_longitude);
      canvas.rotateZ(_latitude);
      canvas.translate(_offset, 0, 0);

      canvas.beginShape(TRIANGLES);
  
      canvas.noStroke();

      for (var i = 0; i < 3; i++)
      {
        canvas.fill(_colors[i]);
        canvas.vertex(_points[i][0], _points[i][1], _points[i][2]);
      }

      canvas.endShape();   
  
      canvas.popMatrix();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      Colors(colorPicker.RandomColor(), colorPicker.RandomColor(), colorPicker.RandomColor());
    }
  } 
}
