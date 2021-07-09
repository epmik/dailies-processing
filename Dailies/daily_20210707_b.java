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
// import Utility.*;
import Utility.Color20210703;
import Utility.ColorPicker;
import Utility.Easing;
import Utility.Math;

public class daily_20210707_b extends AbstractDaily 
{
  private float _boxRotation;
  private float _directionalLightRotation;
  private PeasyCam _camera;
  private boolean _displayAxis;
  private boolean _disableCamera;

  private int _numberOfLines = 8;
  private float _minLineWeight = 1.0f;
  private float _maxLineWeight = 12.0f;
  private float _minLineLength = 80.0f;
  private float _maxLineLength = 320.0f;
  private float _minLineOffset = 0.0f;
  private float _maxLineOffset = 160.0f;

  private int _numberOfDots = 400;
  private int _numberOfTriangles = 100;

  private CallbackListener _resetCallbackListener;

  private ArrayList<IExplode> _explodeArrayList = new ArrayList<IExplode>();

  public daily_20210707_b() 
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

    SetupCameraAndPerspective(Canvas);

    SetupRandomExplodeObjects();

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

    Gui.addToggle("_disableCamera").setPosition(guiX, guiY).setLabel("Disable Camera").setValue(_disableCamera)
      .addCallback(new CallbackListener() 
      {
        public void controlEvent(CallbackEvent event) 
        {
          _camera.setActive(!_disableCamera);
        }
      });

    guiY += guiOffsetY;

    Gui.addToggle("_displayAxis").setPosition(guiX, guiY).setLabel("Display Axis").setValue(_displayAxis);

    guiY += guiOffsetY;

    Gui.addNumberbox("_numberOfLines")
        .setPosition(guiX, guiY).setLabel("Number Of Lines")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1000)
        .setValue(_numberOfLines)
        .addCallback(_resetCallbackListener);

    guiY += guiOffsetY;

    Gui.addNumberbox("_minLineWeight")
        .setPosition(guiX, guiY).setLabel("Min Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 16)
        .setValue(_minLineWeight)
        .addCallback(_resetCallbackListener);

    guiY += guiOffsetY;

    Gui.addNumberbox("_maxLineWeight")
        .setPosition(guiX, guiY).setLabel("Max Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 64)
        .setValue(_maxLineWeight)
        .addCallback(_resetCallbackListener);

    guiY += guiOffsetY;

    Gui.addNumberbox("_minLineLength")
        .setPosition(guiX, guiY).setLabel("Min Line Length")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 600)
        .setValue(_minLineLength)
        .addCallback(_resetCallbackListener);

    guiY += guiOffsetY;

    Gui.addNumberbox("_maxLineLength")
        .setPosition(guiX, guiY).setLabel("Max Line Length")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 600)
        .setValue(_maxLineLength)
        .addCallback(_resetCallbackListener);

    guiY += guiOffsetY;
    
    Gui.addNumberbox("_minLineOffset")
        .setPosition(guiX, guiY).setLabel("Min Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 600)
        .setValue(_minLineOffset)
        .addCallback(_resetCallbackListener);

    guiY += guiOffsetY;

    Gui.addNumberbox("_maxLineOffset")
        .setPosition(guiX, guiY).setLabel("Max Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 600)
        .setValue(_maxLineOffset)
        .addCallback(_resetCallbackListener);

    guiY += guiOffsetY;

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
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
      
      line.ColorOrigin(Color.HsbToInt(Color.DegreesToDouble(RandomGenerator.Value(13, 48)), RandomGenerator.Value(0.9, 1.0), RandomGenerator.Value(0.50, 1.0)));
      line.ColorExtent(Color.HsbToInt(Color.DegreesToDouble(RandomGenerator.Value(13, 48)), RandomGenerator.Value(0.9, 1.0), RandomGenerator.Value(0.50, 1.0)));

      _explodeArrayList.add(line);
    }

    // for (var i = 0; i < 400; i++)
    // {
    //   var dot = RandomDot(20, 400, 1, 8, RandomGenerator);

    //   dot.Color(Color.HsbToInt(Color.DegreesToDouble(RandomGenerator.Value(13, 48)), RandomGenerator.Value(0.9, 1.0),
    //       RandomGenerator.Value(0.50, 1.0)));

    //   _explodeArrayList.add(dot);
    // }

    // for (var i = 0; i < 100; i++)
    // {
    //   var triangle = RandomTriangle(20F, 400F, 0.05F, 0.15F, RandomGenerator);

    //   triangle.Colors(
    //     Color.HsbToInt(Color.DegreesToDouble(RandomGenerator.Value(13, 48)), RandomGenerator.Value(0.9, 1.0), RandomGenerator.Value(0.50, 1.0)),
    //     Color.HsbToInt(Color.DegreesToDouble(RandomGenerator.Value(13, 48)), RandomGenerator.Value(0.9, 1.0), RandomGenerator.Value(0.50, 1.0)),
    //     Color.HsbToInt(Color.DegreesToDouble(RandomGenerator.Value(13, 48)), RandomGenerator.Value(0.9, 1.0), RandomGenerator.Value(0.50, 1.0))
    //     );

    //   _explodeArrayList.add(triangle);
    // }
  
  }

  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
    NoiseGenerator.ReSeed(NoiseGenerator.Seed());

    // _boxRotation += 0.010;
    _directionalLightRotation += 0.025;
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

    Canvas.lights();

    Canvas.ambientLight(5, 5, 5);
    Canvas.directionalLight(243, 102, 126, (float) Math.Cos(_directionalLightRotation), 0,
        (float) Math.Sin(_directionalLightRotation));

    {
      Canvas.pushMatrix();

      Canvas.translate(Canvas.width / 2, Canvas.height / 2, -100F);

      if(_displayAxis)
      {
        DrawAxis();
      }
      
      {
        Canvas.pushMatrix();
      
        for (var explode : _explodeArrayList) {
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
      (float)randomGenerator.Value(minoffset, maxoffset), 
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
 
  Dot RandomDot(float minradius, float maxradius, int minweight, int maxweight, IRandomGenerator randomGenerator)
  {
    return RandomDot((float) randomGenerator.Value(minradius, maxradius), randomGenerator.Value(minweight, maxweight),
        randomGenerator);
  }
  
  Dot RandomDot(float radius, int weight, IRandomGenerator randomGenerator)
  {
    var dot = new Dot();
    
    var p = Math.RandomPointOnSphere(radius, randomGenerator);

    dot.X((float)p[0]);
    dot.Y((float)p[1]);
    dot.Z((float) p[2]);
    
    dot.Weight(weight);

    return dot;
  }
  
  Triangle RandomTriangle(float minwidth, float maxwidth, float minratio, float maxratio, IRandomGenerator randomGenerator)
  {
    return RandomTriangle((float)randomGenerator.Value(minwidth, maxwidth), (float)randomGenerator.Value(minratio, maxratio), randomGenerator);
  }
  
  Triangle RandomTriangle(float width, float ratio, IRandomGenerator randomGenerator)
  {
    var triangle = new Triangle();

    triangle.Longitude((float)randomGenerator.Value(0, Math.Pi * 2));
    triangle.Latitude((float)randomGenerator.Value(0, Math.Pi));
    
    triangle.Point1(0, 0, 0);
    triangle.Point2(width, 0, 0);
    triangle.Point3((float)randomGenerator.Value(-width * 0.33, width + width * 0.33), width * ratio, 0);

    return triangle;
  }

  interface IExplode 
  {
    void Draw(PGraphics canvas);
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
      if(_unitvector == null)
      {
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
      canvas.vertex(_x + _offsetvector[0] + _vector[0], _y + _offsetvector[1] + _vector[1], _z + _offsetvector[2] + _vector[2]);

      canvas.endShape();
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
  }

  class Triangle extends AbstractExplode
  {
    private float[][] _points = new float[3][3];
    private float _longitude;
    private float _latitude;
    private int[] _colors = new int[3];

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
      // canvas.rotateZ(_latitude);

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
  } 
}
