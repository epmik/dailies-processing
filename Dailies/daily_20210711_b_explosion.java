package Dailies;

// import java.util.ArrayList;
// import java.util.List;
import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.util.ElementScanner6;

import org.eclipse.collections.api.list.primitive.DoubleList;

import Dailies.Daily202107.AbstractExplodable;
import Dailies.Daily202107.AbstractExplodableComponent;
import Dailies.Daily202107.ExplodableAngledLine;
import Dailies.Daily202107.ExplodableAngledLineComponent;
import Dailies.Daily202107.ExplodableDot;
import Dailies.Daily202107.ExplodableDotComponent;
import Dailies.Daily202107.ExplodableLine;
import Dailies.Daily202107.ExplodableLineComponent;
import Dailies.Daily202107.ExplodableTriangle;
import Dailies.Daily202107.ExplodableTriangleComponent;
import Dailies.Daily202107.ICreateExplodableListener;
import Dailies.Daily202107.IExplodableComponent;
import Geometry.Icosahedron;
import Geometry.Point3D;
//import processing.opengl.*;
import controlP5.*;
import jogamp.graph.font.typecast.ot.table.PcltTable;
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

public class daily_20210711_b_explosion extends AbstractDaily 
{
  private static ColorPicker WhiteColorPicker = new ColorPicker(Color.RgbToInt(255));

  public class ExplodableIcosahedronCloud extends AbstractExplodable
  {

    private int _numberOfInnerClouds = 3;
    private int _icosahedronDetail = 1;
    private float _minRadius = 160.0F;
    private float _maxRadius = 200.0F;
    private INoiseGenerator _noiseGenerator;
    private IRandomGenerator _randomGenerator;
    private Icosahedron _icosahedron;
    private ColorPicker _colorPicker;
    public float _innerOffsetFactor = 0.5f;
    public float _radiusNoiseFactor = 0.5f;
    public boolean _enableBlendAdd = false;
    public boolean _enableBlendSubtract = false;

    public ExplodableIcosahedronCloud() 
    {
      super();
      
      _noiseGenerator = new SuperSimplexNoiseGenerator();
      _noiseGenerator.AllowNegativeValues(false);
      _noiseGenerator.ClampValues(true);
      _noiseGenerator.InputMultiplier(1.75);

      _randomGenerator = new RandomGenerator();

      CreateIcosahedron();
    }

    public int NumberOfClouds() {
      return _numberOfInnerClouds;
    }

    public void NumberOfClouds(int numberOfClouds) {
      _numberOfInnerClouds = numberOfClouds;
    }

    public int IcosahedronDetail() {
      return _icosahedronDetail;
    }

    public void IcosahedronDetail(int icosahedronDetail) {
      _icosahedronDetail = icosahedronDetail;
      CreateIcosahedron();
    }

    public int NumberOfInnerClouds() {
      return _numberOfInnerClouds;
    }

    public void NumberOfInnerClouds(int numberOfInnerClouds) {
      _numberOfInnerClouds = numberOfInnerClouds;
    }

    public float MinRadius() {
      return _minRadius;
    }

    public void MinRadius(float minRadius) {
      _minRadius = minRadius;
    }

    public float MaxRadius() {
      return _maxRadius;
    }

    public void MaxRadius(float maxRadius) {
      _maxRadius = maxRadius;
    }

    public INoiseGenerator NoiseGenerator() {
      return _noiseGenerator;
    }

    public void NoiseGenerator(INoiseGenerator noiseGenerator) {
      _noiseGenerator = noiseGenerator;
    }

    public ColorPicker ColorPicker() {
      return _colorPicker;
    }

    public void ColorPicker(ColorPicker colorPicker) {
      _colorPicker = colorPicker;
    }

    private void CreateIcosahedron()
    {
      _icosahedron = Icosahedron.Create(_icosahedronDetail);
    }

    public void Draw(PGraphics canvas) 
    {
      _randomGenerator.ReSeed(_randomGenerator.Seed());

      canvas.pushMatrix();

      // canvas.rotateY(_longitude);
      // canvas.rotateZ(_latitude);
      // canvas.translate(_offset, 0, 0);

      var radius = (float) _randomGenerator.Value(_minRadius, _maxRadius);
      var radiusDropoff = 0.9f;
      // var maxalpha = (float)(1.0 / _numberOfInnerClouds);

      for (var i = _numberOfInnerClouds - 1; i > -1; i--)
      {
        var color = _colorPicker.RandomColor();
        //canvas.blendMode(PConstants.BLEND);

        // if (_enableBlendAdd && i < _numberOfInnerClouds - 1) 
        // {
        //   color = WhiteColorPicker.RandomColor();
        //   canvas.blendMode(PConstants.ADD);
        // }
        // else if (_enableBlendSubtract && i < _numberOfInnerClouds - 1) 
        // {
        //   // color = WhiteColorPicker.RandomColor();
        //   canvas.blendMode(PConstants.SUBTRACT);
        // }



        var o = radius * _innerOffsetFactor;

        var x = (float)_randomGenerator.Value(-o, o);
        var y = (float)_randomGenerator.Value(-o, o);
        var z = (float)_randomGenerator.Value(-o, o);
        
        DrawIcosahedron(canvas, x, y, z, radius, (float) _randomGenerator.Value() * radius);
        
        radius *= radiusDropoff;
      }

      canvas.popMatrix();
    }
 
    private void DrawIcosahedron(PGraphics canvas, float x, float y, float z, float radius, float random)
    {
      canvas.pushMatrix();
  
      canvas.noStroke();

      // canvas.rotateY(_rotationFactor);
      // canvas.rotateX(_rotationFactor);
      canvas.translate(x, y, z);
  
      canvas.beginShape(PConstants.TRIANGLES);
  
      for (var i = 0; i < _icosahedron.TriangleIndices.size(); i += 3) 
      {
        var p = _icosahedron.Positions.get(_icosahedron.TriangleIndices.get(i));
        
        var visible = _noiseGenerator.Value(p.X + random, p.Y + random, p.Z + random) < 0.40;

        if (!visible) 
        {
          continue;
        }
  
        canvas.fill(_colorPicker.RandomColor());
    
        DrawVertex(p, radius, canvas);
        DrawVertex(_icosahedron.Positions.get(_icosahedron.TriangleIndices.get(i + 1)), radius, canvas);
        DrawVertex(_icosahedron.Positions.get(_icosahedron.TriangleIndices.get(i + 2)), radius, canvas);
      }
  
      canvas.endShape();
  
      canvas.popMatrix();
    }
    
    private void DrawVertex(Point3D p, float radius, PGraphics canvas) 
    {
      var x = p.X * radius;
      var y = p.Y * radius;
      var z = p.Z * radius;

      var n = _noiseGenerator.Value(x, y, z);

      radius += radius * n * _radiusNoiseFactor;

      x = p.X * radius;
      y = p.Y * radius;
      z = p.Z * radius;

      canvas.vertex(x, y, z);
    }
       
    public void Colorize(ColorPicker colorPicker)
    {
      _colorPicker = colorPicker;
    }
  }

  public class ExplodableIcosahedronCloudComponent extends AbstractExplodableComponent<ExplodableIcosahedronCloud>
  {
    public float _minRadius = 10.0f;
    public float _maxRadius = 400.0f;
  
    public int _minIcosahedronDetail = 1;
    public int _maxIcosahedronDetail = 2;
  
    public int _minNumberOfInnerClouds = 2;
    public int _maxNumberOfInnerClouds = 4;
  
    // public int _from1Color = Color.RgbToInt(9, 45, 75);
    public int _from1Color = Color.RgbToInt(220, 220, 220);
    private controlP5.ColorPicker _from1ColorColorPicker;
  
    // public int _to1Color = Color.RgbToInt(2, 24, 45);
    public int _to1Color = Color.RgbToInt(160, 160, 160);
    private controlP5.ColorPicker _to1ColorColorPicker;
  
    // public int _from2Color = Color.RgbToInt(155, 155, 155, 125);
    // private controlP5.ColorPicker _from2ColorColorPicker;
    // public float _from2Weight = 0.1f;
  
    // public int _from3Color = Color.RgbToInt(255, 179, 0, 125);
    // private controlP5.ColorPicker _from3ColorColorPicker;
    // public float _from3Weight = 0.1f;
  
    public ColorPicker _colorPicker;

    public float _innerOffsetFactor = 0.5f;
    public float _rotationFactor = 0f;
    public float _radiusNoiseFactor = 0.5f;
    public float _positionNoiseFactor = 0f;
    
  
    public ExplodableIcosahedronCloudComponent() 
    {
      super();
  
      RandomGenerator = new RandomGenerator();

      _isTransparent = true;
      
      _colorPicker = new ColorPicker(_from1Color, _to1Color, RandomGenerator);
      // _colorPicker.AddWeightedColorPicker(_from2Weight, _from2Color, RandomGenerator);
      // _colorPicker.AddWeightedColorPicker(_from3Weight, _from3Color, RandomGenerator);
    }
    
    public void SetupGui(ControlP5 gui, String tabtitle, int x, int y, int yoffset) 
    {
      var guiOnChangeListener = new CallbackListener() 
      {
        public void controlEvent(CallbackEvent event) 
        {
          _minRadius = gui.getController(Guid + "_minRadius").getValue();
          _maxRadius = gui.getController(Guid + "_maxRadius").getValue();
          _minIcosahedronDetail = (int)gui.getController(Guid + "_minIcosahedronDetail").getValue();
          _maxIcosahedronDetail = (int)gui.getController(Guid + "_maxIcosahedronDetail").getValue();
          _innerOffsetFactor = gui.getController(Guid + "_innerOffsetFactor").getValue();
          _rotationFactor = gui.getController(Guid + "_rotationFactor").getValue();
          _radiusNoiseFactor = gui.getController(Guid + "_radiusNoiseFactor").getValue();
          _positionNoiseFactor = gui.getController(Guid + "_positionNoiseFactor").getValue();
  
          BindDefaultProperties(gui);
  
          Reset();
        }
      };
  
      y = SetupGuiForDefaultProperties(gui, tabtitle, guiOnChangeListener, x, y, yoffset);
  
      y += yoffset;
  
      gui.addNumberbox(Guid + "_innerOffsetFactor")
          .setPosition(x, y).setLabel("Min Radius")
          .setScrollSensitivity(1F).setMultiplier(1F)
          .setRange(10, 800)
          .setValue(_innerOffsetFactor)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);

      y += yoffset;
  
      gui.addNumberbox(Guid + "_rotationFactor")
          .setPosition(x, y).setLabel("Rotation Factor")
          .setScrollSensitivity(0.01F).setMultiplier(0.01F)
          .setRange(0f, (float)Math.Pi2)
          .setValue(_rotationFactor)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);
  
      y += yoffset;
  
      gui.addNumberbox(Guid + "_radiusNoiseFactor")
          .setPosition(x, y).setLabel("Radius Factor")
          .setScrollSensitivity(0.01F).setMultiplier(0.01F)
          .setRange(0f, 2f)
          .setValue(_radiusNoiseFactor)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);

      y += yoffset;

      gui.addNumberbox(Guid + "_positionNoiseFactor")
          .setPosition(x, y).setLabel("Position Factor")
          .setScrollSensitivity(0.01F).setMultiplier(0.01F)
          .setRange(0f, 2f)
          .setValue(_positionNoiseFactor)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);

      y += yoffset;

      gui.addNumberbox(Guid + "_minRadius")
          .setPosition(x, y).setLabel("Min Radius")
          .setScrollSensitivity(1F).setMultiplier(1F)
          .setRange(10, 800)
          .setValue(_minRadius)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);
                          
      y += yoffset;
  
      gui.addNumberbox(Guid + "_maxRadius")
          .setPosition(x, y).setLabel("Max Radius")
          .setScrollSensitivity(1F).setMultiplier(1F)
          .setRange(10, 800)
          .setValue(_maxRadius)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);
  
      y += yoffset;
  
      gui.addNumberbox(Guid + "_minIcosahedronDetail")
          .setPosition(x, y).setLabel("Min Ico Detail")
          .setScrollSensitivity(1F).setMultiplier(1F)
          .setRange(1, 3)
          .setValue(_minIcosahedronDetail)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);
  
      y += yoffset;
  
      gui.addNumberbox(Guid + "_maxIcosahedronDetail")
          .setPosition(x, y).setLabel("Max Ico Detail")
          .setScrollSensitivity(1F).setMultiplier(1F)
          .setRange(1, 3)
          .setValue(_maxIcosahedronDetail)
          .moveTo(tabtitle)
          .onChange(guiOnChangeListener);
  
      y += yoffset;
     
      _from1ColorColorPicker = gui.addColorPicker(Guid + "_from1Color").setPosition(x, y)
          .setLabel("First From Color")
          .moveTo(tabtitle).setColorValue(_from1Color);
  
      y += yoffset;
      y += yoffset;
     
      _to1ColorColorPicker = gui.addColorPicker(Guid + "_to1Color").setPosition(x, y)
          .setLabel("First To Color")
          .moveTo(tabtitle).setColorValue(_to1Color);
  
      y += yoffset;
      y += yoffset;
    }
   
    public void Update(double elapsedTime) 
    {
      RandomGenerator.ReSeed(RandomGenerator.Seed());
  
      var from1Color = _from1ColorColorPicker.getColorValue();
      var to1Color = _to1ColorColorPicker.getColorValue();
  
      if(_from1Color != from1Color || _to1Color != to1Color)
      {
        Colorize();
      }
    }
  
    public void Draw(PGraphics canvas, double elapsedTime) 
    {
      canvas.beginDraw();

      canvas.push();

      // canvas.blendMode(PConstants.BLEND);
  
      canvas.hint(PConstants.ENABLE_DEPTH_TEST);

      {
        canvas.pushMatrix();

        canvas.translate(_x, _y, _z);

        for (var explodable : _explodables) {
          explodable.Draw(canvas);
        }

        canvas.popMatrix();
      }
      
      canvas.pop();
  
      canvas.endDraw();
    }
  
    public void Reset()
    {
      _explodables.clear();
  
      CreateExplodables(_createExplodableListener);
    }
  
    private void Colorize()
    {
      _from1Color = _from1ColorColorPicker.getColorValue();
      _to1Color = _to1ColorColorPicker.getColorValue();
  
      _colorPicker.Reset(_from1Color, _to1Color, RandomGenerator);
      // _colorPicker.AddWeightedColorPicker(_from2Weight, _from2Color, RandomGenerator);
      // _colorPicker.AddWeightedColorPicker(_from3Weight, _from3Color, RandomGenerator);
          
       for (var explodable : _explodables)
       {
          explodable.Colorize(_colorPicker);
       }
    }
  }
  
  private float _directionalLightRotation;
  private PeasyCam _camera;
  private boolean _displayAxis;
  private boolean _enableLights;

  private List<IExplodableComponent> _explodableComponents = new ArrayList<IExplodableComponent>();

  public daily_20210711_b_explosion() 
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

    // Gui.setAutoDraw(false);

    SetupCamera(Canvas);

    SetupProjectionMatrix(Canvas, false);

    _camera.setActive(false);

    var _explodableLineComponent = new ExplodableLineComponent();
    _explodableComponents.add(_explodableLineComponent);
    _explodableLineComponent.NumberOfExplodables(200);
    _explodableLineComponent.X(Canvas.width / 2);
    _explodableLineComponent.Y(Canvas.height / 2);
    _explodableLineComponent.Z(0);

    _explodableLineComponent.CreateExplodables(new ICreateExplodableListener<ExplodableLine, ExplodableLineComponent>() 
    {
      public ExplodableLine Create(ExplodableLineComponent component)
      {
        var line = new ExplodableLine();
    
        line.Longitude((float) component.RandomGenerator.Value(Math.ToRadians(component._minLongitude), Math.ToRadians(component._maxLongitude)));
        line.Latitude((float) component.RandomGenerator.Value(Math.ToRadians(component._minLatitude), Math.ToRadians(component._maxLatitude)));
    
        line.Offset((float)Math.Map(Easing.InCubic(component.RandomGenerator.Value()), component._minOffset, component._maxOffset));
    
        line.Length((float)component.RandomGenerator.Value(component._minLength, component._maxLength));
        
        line.Weight((float) component.RandomGenerator.Value(component._minWeight, component._maxWeight));

        line.EnableVertexColors(component._enableVertexColors);

        line.Colorize(component._colorPicker);
    
        return line;
      }
    });

    var _explodableAngledLineComponent = new ExplodableAngledLineComponent();
    _explodableComponents.add(_explodableAngledLineComponent);
    _explodableAngledLineComponent.NumberOfExplodables(200);
    _explodableAngledLineComponent.X(Canvas.width / 2);
    _explodableAngledLineComponent.Y(Canvas.height / 2);
    _explodableAngledLineComponent.Z(0);

    _explodableAngledLineComponent.CreateExplodables(new ICreateExplodableListener<ExplodableAngledLine, ExplodableAngledLineComponent>() 
    {
      public ExplodableAngledLine Create(ExplodableAngledLineComponent component)
      {
        var line = new ExplodableAngledLine();
    
        line.Longitude((float) component.RandomGenerator.Value(Math.ToRadians(component._minLongitude), Math.ToRadians(component._maxLongitude)));
        line.Latitude((float) component.RandomGenerator.Value(Math.ToRadians(component._minLatitude), Math.ToRadians(component._maxLatitude)));
    
        line.Offset((float)Math.Map(Easing.InCubic(component.RandomGenerator.Value()), component._minOffset, component._maxOffset));
    
        line.Length((float)component.RandomGenerator.Value(component._minLength, component._maxLength));
        
        line.Weight((float) component.RandomGenerator.Value(component._minWeight, component._maxWeight));

        line.EnableVertexColors(component._enableVertexColors);

        line.Colorize(component._colorPicker);
    
        return line;
      }
    });

    var _explodableDotComponent = new ExplodableDotComponent();
    _explodableComponents.add(_explodableDotComponent);
    _explodableDotComponent.NumberOfExplodables(400);
    _explodableDotComponent.X(Canvas.width / 2);
    _explodableDotComponent.Y(Canvas.height / 2);
    _explodableDotComponent.Z(0);

    _explodableDotComponent.CreateExplodables(new ICreateExplodableListener<ExplodableDot, ExplodableDotComponent>() 
    {
      public ExplodableDot Create(ExplodableDotComponent component)
      {
        var dot = new ExplodableDot();

        dot.Longitude((float)component.RandomGenerator.Value(Math.ToRadians(component._minLongitude), Math.ToRadians(component._maxLongitude)));
        dot.Latitude((float) component.RandomGenerator.Value(Math.ToRadians(component._minLatitude), Math.ToRadians(component._maxLatitude)));
    
        dot.Offset((float)Math.Map(Easing.InCubic(component.RandomGenerator.Value()), component._minOffset, component._maxOffset));
        dot.Weight((float) component.RandomGenerator.Value(component._minWeight, component._maxWeight));

        dot.Colorize(component._colorPicker);
    
        return dot;
      }
    });

    var _explodableTriangleComponent = new ExplodableTriangleComponent();
    _explodableComponents.add(_explodableTriangleComponent);
    _explodableTriangleComponent.NumberOfExplodables(100);
    _explodableTriangleComponent.X(Canvas.width / 2);
    _explodableTriangleComponent.Y(Canvas.height / 2);
    _explodableTriangleComponent.Z(0);

    _explodableTriangleComponent.CreateExplodables(new ICreateExplodableListener<ExplodableTriangle, ExplodableTriangleComponent>() 
    {
      public ExplodableTriangle Create(ExplodableTriangleComponent component)
      {
        var triangle = new ExplodableTriangle();

        var width = (float) component.RandomGenerator.Value(component._minWidth, component._maxWidth);

        var ratio = (float) component.RandomGenerator.Value(component._minRatio, component._maxRatio);

        triangle.Longitude((float)component.RandomGenerator.Value(Math.ToRadians(component._minLongitude), Math.ToRadians(component._maxLongitude)));
        triangle.Latitude((float) component.RandomGenerator.Value(Math.ToRadians(component._minLatitude), Math.ToRadians(component._maxLatitude)));

        triangle.Offset((float)Math.Map(Easing.InCubic(component.RandomGenerator.Value()), component._minOffset, component._maxOffset));
        
        triangle.Point1(0, 0, 0);
        triangle.Point2(width, 0, 0);
        triangle.Point3((float)component.RandomGenerator.Value(0, width + width * 0.33), width * ratio, 0);


        triangle.Colorize(component._colorPicker);
    
        return triangle;
      }
    });

    var _explodableIcosahedronCloudComponent = new ExplodableIcosahedronCloudComponent();
    _explodableComponents.add(_explodableIcosahedronCloudComponent);
    _explodableIcosahedronCloudComponent.NumberOfExplodables(1);
    _explodableIcosahedronCloudComponent._minIcosahedronDetail = 3;
    _explodableIcosahedronCloudComponent._maxIcosahedronDetail = 3;
    _explodableIcosahedronCloudComponent._minRadius = 10;
    _explodableIcosahedronCloudComponent._maxRadius = 30;
    // _explodableIcosahedronCloudComponent._minOffset = 0;
    // _explodableIcosahedronCloudComponent._maxOffset = 0;
    _explodableIcosahedronCloudComponent._minNumberOfInnerClouds = 2;
    _explodableIcosahedronCloudComponent._maxNumberOfInnerClouds = 4;
    _explodableIcosahedronCloudComponent.X(Canvas.width / 2);
    _explodableIcosahedronCloudComponent.Y(Canvas.height / 2);
    _explodableIcosahedronCloudComponent.Z(0);

    _explodableIcosahedronCloudComponent.CreateExplodables(new ICreateExplodableListener<ExplodableIcosahedronCloud, ExplodableIcosahedronCloudComponent>() 
    {
      public ExplodableIcosahedronCloud Create(ExplodableIcosahedronCloudComponent component)
      {
        var triangle = new ExplodableIcosahedronCloud();

        triangle.MinRadius((float) component.RandomGenerator.Value(component._minRadius, component._maxRadius));
        triangle.MaxRadius((float) component.RandomGenerator.Value(triangle.MinRadius(), component._maxRadius));

        triangle.Longitude((float)component.RandomGenerator.Value(Math.ToRadians(component._minLongitude), Math.ToRadians(component._maxLongitude)));
        triangle.Latitude((float) component.RandomGenerator.Value(Math.ToRadians(component._minLatitude), Math.ToRadians(component._maxLatitude)));

        // triangle.Offset((float) Math.Map(Easing.InCubic(component.RandomGenerator.Value()), component._minOffset,
        //     component._maxOffset));

        triangle.IcosahedronDetail((int) component.RandomGenerator.Value(component._minIcosahedronDetail, component._maxIcosahedronDetail));
        triangle.NumberOfInnerClouds((int) component.RandomGenerator.Value(component._minNumberOfInnerClouds,
            component._maxNumberOfInnerClouds));

            triangle._innerOffsetFactor = component._innerOffsetFactor;
            triangle._radiusNoiseFactor = component._radiusNoiseFactor;

        // triangle.NumberOfInnerClouds(1);
     
        triangle.Colorize(component._colorPicker);
    
        return triangle;
      }
    });

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    Gui.addToggle("_enableLights").setPosition(guiX, guiY).setLabel("Enable Lights").setValue(_enableLights);

    guiY += guiOffsetY;

    Gui.addToggle("_displayAxis").setPosition(guiX, guiY).setLabel("Display Axis").setValue(_displayAxis);

    guiY = guiOffsetY;

    _explodableLineComponent.SetupGui(Gui, "Line settings", guiOffsetY, guiOffsetY, guiOffsetY);
    _explodableAngledLineComponent.SetupGui(Gui, "Angled Line settings", guiOffsetY, guiOffsetY, guiOffsetY);
    _explodableDotComponent.SetupGui(Gui, "Dot settings", guiOffsetY, guiOffsetY, guiOffsetY);
    _explodableTriangleComponent.SetupGui(Gui, "Triangle settings", guiOffsetY, guiOffsetY, guiOffsetY);
    _explodableIcosahedronCloudComponent.SetupGui(Gui, "Cloud settings", guiOffsetY, guiOffsetY, guiOffsetY);

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }
    
  private void SetupProjectionMatrix(PGraphics canvas, boolean orthographic)
  {
    var fov = (float) (Math.Pi / 3);
    var z = (float) ((Canvas.height / 2.0F) / Math.Tan(fov / 2.0));

    if (orthographic) {
      Canvas.ortho(-Canvas.width / 2, Canvas.width / 2, -Canvas.height / 2, Canvas.height / 2);
    } else {
      Canvas.perspective(fov, (float) (Canvas.width) / (float) (Canvas.height), z / 1000.0F, z * 10000.0F);
    }
  }
  
  private void SetupCamera(PGraphics canvas)
  {
    var fov = (float) (Math.Pi / 3);
    var z = (float) ((Canvas.height / 2.0F) / Math.Tan(fov / 2.0));

    if (_camera == null) {
      _camera = new PeasyCam(this, canvas, z);
    }

    _camera.setMinimumDistance(z / 1000.0F);
    _camera.setMaximumDistance(z * 10000.0F);
    _camera.setFreeRotationMode();
    _camera.lookAt(canvas.width / 2.0F, canvas.height / 2.0F, 0, z);
  }

  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
    NoiseGenerator.ReSeed(NoiseGenerator.Seed());

    for (var explodableComponent : _explodableComponents)
    {
      explodableComponent.Update(elapsedTime);
    }

    _directionalLightRotation += 0.025;
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    // SetupProjectionMatrix(Canvas, false);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(127);
    Canvas.background(74, 176, 248);

    Canvas.strokeWeight(1);
    Canvas.stroke(255);
    Canvas.fill(125);

    if (_enableLights) {
      Canvas.lights();

      Canvas.ambientLight(5, 5, 5);

      Canvas.directionalLight(128, 128, 128, (float) Math.Cos(_directionalLightRotation), 0.0f,
          (float) Math.Sin(_directionalLightRotation));
    } else {
      Canvas.noLights();
    }

    if (_displayAxis) {
      Canvas.pushMatrix();

      Canvas.translate(Canvas.width / 2, Canvas.height / 2, -100F);
      DrawAxis();
      Canvas.popMatrix();
    }

    Canvas.endDraw();

    // for (var explodableComponent : _explodableComponents) 
    // {
    //   if (explodableComponent.IsTransparent()) {
    //     continue;
    //   }
    //   explodableComponent.Draw(Canvas, elapsedTime);
    // }

    for (var explodableComponent : _explodableComponents) 
    {
      // if (!explodableComponent.IsTransparent())
      // {
      //   continue;
      // }
      explodableComponent.Draw(Canvas, elapsedTime);
    }
    
    // DrawGui(elapsedTime);

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
 
  // private void DrawGui(double elapsedTime)
  // {
  //   Canvas.push();

  //   Canvas.hint(PConstants.DISABLE_DEPTH_TEST);

  //   Canvas.pushMatrix();

  //   SetupProjectionMatrix(Canvas, true);
    
  //   Canvas.resetMatrix();

  //   Canvas.beginDraw();

  //   Canvas.noStroke();
  //   Canvas.fill(7, 45, 72, 125);
  //   Canvas.rect(-Canvas.width / 2, -Canvas.height / 2, 240, Canvas.height);

  //   Canvas.endDraw();

  //   // Gui.draw();

  //   Canvas.popMatrix();

  //   Canvas.pop();
  // }
  
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
 
  public void keyPressed(KeyEvent event)
  {
    super.keyPressed(event);

    if (event.getKey() == PConstants.CODED && event.getKeyCode() == PConstants.SHIFT)
    {
      _camera.setActive(true);
    }

    for (var explodableComponent : _explodableComponents)
    {
      explodableComponent.HandleKeyPress(event);
    }
  }
 
  public void keyReleased(KeyEvent event)
  {
    super.keyReleased(event);

    if (event.getKey() == PConstants.CODED && event.getKeyCode() == PConstants.SHIFT)
    {
      _camera.setActive(false);
    }

    for (var explodableComponent : _explodableComponents)
    {
      explodableComponent.HandleKeyRelease(event);
    }

  }

  public void mouseReleased(MouseEvent event) 
  {
    super.mouseReleased(event);

    if (!PropagateMouseEvents) {
      return;
    }

    for (var explodableComponent : _explodableComponents)
    {
      explodableComponent.HandleMouseRelease(event);
    }
  }

}
