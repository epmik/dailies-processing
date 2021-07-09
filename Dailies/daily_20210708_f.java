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

public class daily_20210708_f extends AbstractDaily 
{
  private float _directionalLightRotation;
  private PeasyCam _camera;
  private boolean _displayAxis;
  private boolean _enableLights = false;
  private boolean _enableColorPerObject = true;

  private int _numberOfLines = 200;
  private float _minLineWeight = 1.0f;
  private float _maxLineWeight = 12.0f;
  private float _minLineLength = 4.0f;
  private float _maxLineLength = 50.0f;
  private float _minLineOffset = 80.0f;
  private float _maxLineOffset = 380.0f;
  private float _minLineLongitude = 0.0f;
  private float _maxLineLongitude = 360.0f;
  private float _minLineLatitude = 0.0f;
  private float _maxLineLatitude = 180.0f;

  private int _numberOfLinesAngled = 200;
  private float _minLineAngledWeight = 1.0f;
  private float _maxLineAngledWeight = 12.0f;
  private float _minLineAngledLength = 4.0f;
  private float _maxLineAngledLength = 50.0f;
  private float _minLineAngledOffset = 0.0f;
  private float _maxLineAngledOffset = 380.0f;
  private float _minLineAngledLongitude = 0.0f;
  private float _maxLineAngledLongitude = 360.0f;
  private float _minLineAngledLatitude = 0.0f;
  private float _maxLineAngledLatitude = 180.0f;

  private int _numberOfDots = 400;
  private float _minDotWeight = 1.0f;
  private float _maxDotWeight = 16.0f;
  private float _minDotOffset = 0.0f;
  private float _maxDotOffset = 120.0f;
  private float _minDotLongitude = 0.0f;
  private float _maxDotLongitude = 360.0f;
  private float _minDotLatitude = 0.0f;
  private float _maxDotLatitude = 180.0f;

  private int _numberOfTriangles = 100;
  private float _minTriangleWidth = 8.0f;
  private float _maxTriangleWidth = 200.0f;
  private float _minTriangleRatio = 0.01f;
  private float _maxTriangleRatio = 0.15f;
  private float _minTriangleOffset = 0.0f;
  private float _maxTriangleOffset = 200.0f;
  private float _minTriangleLongitude = 0.0f;
  private float _maxTriangleLongitude = 360.0f;
  private float _minTriangleLatitude = 0.0f;
  private float _maxTriangleLatitude = 180.0f;

  private int _from1Color = color(200, 200, 200);
  private controlP5.ColorPicker _from1ColorColorPicker;
  private int _to1Color = color(10, 10, 10);
  private controlP5.ColorPicker _to1ColorColorPicker;

  private int _from2Color = color(255, 179, 0);
  private controlP5.ColorPicker _from2ColorColorPicker;
  private float _from2Weight = 0.1f;

  private int _from3Color = color(255, 179, 0);
  private controlP5.ColorPicker _from3ColorColorPicker;
  private float _from3Weight = 0.1f;

  private CallbackListener _resetCallbackListener;

  private ArrayList<IExplode> _explodeArrayList = new ArrayList<IExplode>();

  private ColorPicker _colorPicker;

  private int _selectedPreset = 0;

  public daily_20210708_f() 
  {
    super();

    Renderer = P3D;
    ScreenWidth = 1024;
    ScreenHeight = 1024;
    SketchResolutionMultiplier = 1.0;
    _colorPicker = new ColorPicker();
  }

    @Override
  public void setup() 
  {
    super.setup();

    SetupCameraAndPerspective(Canvas);

    _camera.setActive(false);

    Preset(_selectedPreset);

    SetupGui();

    SetupRandomExplodeObjects();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }
 
  private void Preset(int preset)
  {
    switch(preset)
    {
      case 1:
        _numberOfLines = 1400;
        _minLineWeight = 1.0f;
        _maxLineWeight = 8.0f;
        _minLineLength = 4.0f;
        _maxLineLength = 12.0f;
        _minLineOffset = 80.0f;
        _maxLineOffset = 420.0f;
        _minLineLongitude = 0.0f;
        _maxLineLongitude = 360.0f;
        _minLineLatitude = 12.0f;
        _maxLineLatitude = 170.0f;

        _numberOfLinesAngled = 200;
        _minLineAngledWeight = 1.0f;
        _maxLineAngledWeight = 9.0f;
        _minLineAngledLength = 24.0f;
        _maxLineAngledLength = 50.0f;
        _minLineAngledOffset = 0.0f;
        _maxLineAngledOffset = 380.0f;
        _minLineAngledLongitude = 0.0f;
        _maxLineAngledLongitude = 360.0f;
        _minLineAngledLatitude = 12.0f;
        _maxLineAngledLatitude = 175.0f;

        _numberOfDots = 960;
        _minDotWeight = 1.0f;
        _maxDotWeight = 16.0f;
        _minDotOffset = 0.0f;
        _maxDotOffset = 420.0f;
        _minDotLongitude = 0.0f;
        _maxDotLongitude = 360.0f;
        _minDotLatitude = 0.0f;
        _maxDotLatitude = 160.0f;

        _numberOfTriangles = 340;
        _minTriangleWidth = 8.0f;
        _maxTriangleWidth = 200.0f;
        _minTriangleRatio = 0.01f;
        _maxTriangleRatio = 0.08f;
        _minTriangleOffset = 40.0f;
        _maxTriangleOffset = 280.0f;
        _minTriangleLongitude = 0.0f;
        _maxTriangleLongitude = 360.0f;
        _minTriangleLatitude = 200.0f;
        _maxTriangleLatitude = 360.0f;

        _from1Color = color(200, 200, 200);
        _to1Color = color(10, 10, 10);
        _from2Color = color(255, 179, 0);
        _from2Weight = 0.1f;
        _from3Color = color(255, 179, 0);
        _from3Weight = 0.1f;
            break;
      case 2:
      _numberOfLines = 1400;
      _minLineWeight = 20.0f;
      _maxLineWeight = 50.0f;
      _minLineLength = 10.0f;
      _maxLineLength = 12.0f;
      _minLineOffset = 80.0f;
      _maxLineOffset = 420.0f;
      _minLineLongitude = 0.0f;
      _maxLineLongitude = 360.0f;
      _minLineLatitude = 135.0f;
      _maxLineLatitude = 170.0f;

      _numberOfLinesAngled = 200;
      _minLineAngledWeight = 10.0f;
      _maxLineAngledWeight = 24.0f;
      _minLineAngledLength = 24.0f;
      _maxLineAngledLength = 50.0f;
      _minLineAngledOffset = 0.0f;
      _maxLineAngledOffset = 380.0f;
      _minLineAngledLongitude = 0.0f;
      _maxLineAngledLongitude = 360.0f;
      _minLineAngledLatitude = 12.0f;
      _maxLineAngledLatitude = 175.0f;

      _numberOfDots = 960;
      _minDotWeight = 10.0f;
      _maxDotWeight = 20.0f;
      _minDotOffset = 0.0f;
      _maxDotOffset = 420.0f;
      _minDotLongitude = 0.0f;
      _maxDotLongitude = 360.0f;
      _minDotLatitude = 0.0f;
      _maxDotLatitude = 160.0f;

      _numberOfTriangles = 340;
      _minTriangleWidth = 14.0f;
      _maxTriangleWidth = 244.0f;
      _minTriangleRatio = 0.13f;
      _maxTriangleRatio = 0.30f;
      _minTriangleOffset = 140.0f;
      _maxTriangleOffset = 420.0f;
      _minTriangleLongitude = 0.0f;
      _maxTriangleLongitude = 360.0f;
      _minTriangleLatitude = 220.0f;
      _maxTriangleLatitude = 360.0f;

      _from1Color = color(200, 200, 200);
      _to1Color = color(10, 10, 10);
      _from2Color = color(255, 179, 0);
      _from2Weight = 0.1f;
      _from3Color = color(255, 179, 0);
      _from3Weight = 0.1f;
          break;
      case 3:
      _numberOfLines = 200;
      _minLineWeight = 1.0f;
      _maxLineWeight = 1.0f;
      _minLineLength = 200.0f;
      _maxLineLength = 280.0f;
      _minLineOffset = 100.0f;
      _maxLineOffset = 400.0f;
      _minLineLongitude = 0.0f;
      _maxLineLongitude = 360.0f;
      _minLineLatitude = 0.0f;
      _maxLineLatitude = 180.0f;

      _numberOfLinesAngled = 220;
      _minLineAngledWeight = 1.0f;
      _maxLineAngledWeight = 2.0f;
      _minLineAngledLength = 120.0f;
      _maxLineAngledLength = 180.0f;
      _minLineAngledOffset = 0.0f;
      _maxLineAngledOffset = 380.0f;
      _minLineAngledLongitude = 0.0f;
      _maxLineAngledLongitude = 360.0f;
      _minLineAngledLatitude = 12.0f;
      _maxLineAngledLatitude = 175.0f;

      _numberOfDots = 400;
      _minDotWeight = 4.0f;
      _maxDotWeight = 8.0f;
      _minDotOffset = 100.0f;
      _maxDotOffset = 420.0f;
      _minDotLongitude = 0.0f;
      _maxDotLongitude = 360.0f;
      _minDotLatitude = 0.0f;
      _maxDotLatitude = 180.0f;

      _numberOfTriangles = 400;
      _minTriangleWidth = 8.0f;
      _maxTriangleWidth = 420.0f;
      _minTriangleRatio = 0.01f;
      _maxTriangleRatio = 0.01f;
      _minTriangleOffset = 90.0f;
      _maxTriangleOffset = 200.0f;
      _minTriangleLongitude = 0.0f;
      _maxTriangleLongitude = 360.0f;
      _minTriangleLatitude = 190.0f;
      _maxTriangleLatitude = 360.0f;

      _from1Color = color(200, 200, 200);
      _to1Color = color(10, 10, 10);
      _from2Color = color(255, 179, 0);
      _from2Weight = 0.1f;
      _from3Color = color(255, 179, 0);
      _from3Weight = 0.1f;
          break;
          default:
      _numberOfLines = 200;
      _minLineWeight = 1.0f;
      _maxLineWeight = 12.0f;
      _minLineLength = 4.0f;
      _maxLineLength = 50.0f;
      _minLineOffset = 80.0f;
      _maxLineOffset = 380.0f;
      _minLineLongitude = 0.0f;
      _maxLineLongitude = 360.0f;
      _minLineLatitude = 0.0f;
      _maxLineLatitude = 180.0f;

      _numberOfLinesAngled = 200;
      _minLineAngledWeight = 1.0f;
      _maxLineAngledWeight = 12.0f;
      _minLineAngledLength = 4.0f;
      _maxLineAngledLength = 50.0f;
      _minLineAngledOffset = 0.0f;
      _maxLineAngledOffset = 380.0f;
      _minLineAngledLongitude = 0.0f;
      _maxLineAngledLongitude = 360.0f;
      _minLineAngledLatitude = 0.0f;
      _maxLineAngledLatitude = 180.0f;

      _numberOfDots = 400;
      _minDotWeight = 1.0f;
      _maxDotWeight = 16.0f;
      _minDotOffset = 0.0f;
      _maxDotOffset = 120.0f;
      _minDotLongitude = 0.0f;
      _maxDotLongitude = 360.0f;
      _minDotLatitude = 0.0f;
      _maxDotLatitude = 180.0f;

      _numberOfTriangles = 100;
      _minTriangleWidth = 8.0f;
      _maxTriangleWidth = 200.0f;
      _minTriangleRatio = 0.01f;
      _maxTriangleRatio = 0.15f;
      _minTriangleOffset = 0.0f;
      _maxTriangleOffset = 200.0f;
      _minTriangleLongitude = 0.0f;
      _maxTriangleLongitude = 360.0f;
      _minTriangleLatitude = 0.0f;
      _maxTriangleLatitude = 180.0f;

      _from1Color = color(200, 200, 200);
      _to1Color = color(10, 10, 10);
      _from2Color = color(255, 179, 0);
      _from2Weight = 0.1f;
      _from3Color = color(255, 179, 0);
      _from3Weight = 0.1f;
      break;
    }
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

    var _colorizeCallbackListener = new CallbackListener() 
    {
      public void controlEvent(CallbackEvent event) 
      {
        ColorizeExplodeObjects();
      }
    };

    Gui.addToggle("_enableLights").setPosition(guiX, guiY).setLabel("Enable Lights").setValue(_enableLights);

    guiY += guiOffsetY;

    Gui.addToggle("_displayAxis").setPosition(guiX, guiY).setLabel("Display Axis").setValue(_displayAxis);

    guiY += guiOffsetY;

    Gui.addToggle("_enableColorPerObject").setPosition(guiX, guiY).setLabel("Enable Color Per Object")
        .setValue(_enableColorPerObject)
        .addCallback(_colorizeCallbackListener);

    guiY += guiOffsetY;

    _from1ColorColorPicker = Gui.addColorPicker("_from1Color").setPosition(guiX, guiY).setLabel("First From Color").setColorValue(_from1Color);

    guiY += guiOffsetY;
    guiY += guiOffsetY;

    _to1ColorColorPicker = Gui.addColorPicker("_to1Color").setPosition(guiX, guiY).setLabel("First To Color").setColorValue(_to1Color);

    guiY += guiOffsetY;
    guiY += guiOffsetY;

    _from2ColorColorPicker = Gui.addColorPicker("_from2Color").setPosition(guiX, guiY).setLabel("Second Color").setColorValue(_from2Color);

    guiY += guiOffsetY;
    guiY += guiOffsetY;

    Gui.addNumberbox("_from2Weight").setPosition(guiX, guiY).setLabel("Second Color Weight")
      .setRange(0.0025f, 2.00f)
      .setDecimalPrecision(4)
      .setScrollSensitivity(0.0025f)
      .setMultiplier(0.0025f)
      .setValue(_from2Weight)
      .addCallback(_colorizeCallbackListener);

    guiY += guiOffsetY;

    _from3ColorColorPicker = Gui.addColorPicker("_from3Color").setPosition(guiX, guiY).setLabel("Third Color").setColorValue(_from3Color);

    guiY += guiOffsetY;
    guiY += guiOffsetY;

    Gui.addNumberbox("_from3Weight").setPosition(guiX, guiY).setLabel("Third Color Weight")
      .setRange(0.0025f, 2.00f)
      .setDecimalPrecision(4)
      .setScrollSensitivity(0.0025f)
      .setMultiplier(0.0025f)
      .setValue(_from3Weight)
      .addCallback(_colorizeCallbackListener);

    guiY += guiOffsetY;
    
    SetupLineSettingsGui(guiOffsetY, guiOffsetY, guiOffsetY);

    SetupLineAngledSettingsGui(guiOffsetY, guiOffsetY, guiOffsetY);

    SetupDotsSettingsGui(guiOffsetY, guiOffsetY, guiOffsetY);

    SetupTriangleSettingsGui(guiOffsetY, guiOffsetY, guiOffsetY);
  }
  
  private void SetupLineSettingsGui(int x, int y, int yoffset) 
  {
    var tabtitle = "Line Settings";

    Gui.addNumberbox("_numberOfLines")
        .setPosition(x, y).setLabel("Number Of Lines")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 3200)
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
        .setRange(1, 1600)
        .setValue(_minLineLength)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineLength")
        .setPosition(x, y).setLabel("Max Line Length")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 1600)
        .setValue(_maxLineLength)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
    
    Gui.addNumberbox("_minLineOffset")
        .setPosition(x, y).setLabel("Min Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_minLineOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineOffset")
        .setPosition(x, y).setLabel("Max Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_maxLineOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

        y += yoffset;
    
        Gui.addNumberbox("_minLineLongitude")
            .setPosition(x, y).setLabel("Min Line Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minLineLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxLineLongitude")
            .setPosition(x, y).setLabel("Max Line Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxLineLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);

        y += yoffset;

        Gui.addNumberbox("_minLineLatitude")
            .setPosition(x, y).setLabel("Min Line Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minLineLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxLineLatitude")
            .setPosition(x, y).setLabel("Max Line Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxLineLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
  }
  
  private void SetupLineAngledSettingsGui(int x, int y, int yoffset) 
  {
    var tabtitle = "Line Angled Settings";

    Gui.addNumberbox("_numberOfLinesAngled")
        .setPosition(x, y).setLabel("Number Of Lines")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 3200)
        .setValue(_numberOfLinesAngled)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minLineAngledWeight")
        .setPosition(x, y).setLabel("Min Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_minLineAngledWeight)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineAngledWeight")
        .setPosition(x, y).setLabel("Max Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_maxLineAngledWeight)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minLineAngledLength")
        .setPosition(x, y).setLabel("Min Line Length")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 1600)
        .setValue(_minLineAngledLength)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineAngledLength")
        .setPosition(x, y).setLabel("Max Line Length")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 1600)
        .setValue(_maxLineAngledLength)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;
    
    Gui.addNumberbox("_minLineAngledOffset")
        .setPosition(x, y).setLabel("Min Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_minLineAngledOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxLineAngledOffset")
        .setPosition(x, y).setLabel("Max Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_maxLineAngledOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

        y += yoffset;
    
        Gui.addNumberbox("_minLineAngledLongitude")
            .setPosition(x, y).setLabel("Min Line Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minLineAngledLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxLineAngledLongitude")
            .setPosition(x, y).setLabel("Max Line Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxLineAngledLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);

        y += yoffset;

        Gui.addNumberbox("_minLineAngledLatitude")
            .setPosition(x, y).setLabel("Min Line Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minLineAngledLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxLineAngledLatitude")
            .setPosition(x, y).setLabel("Max Line Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxLineAngledLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
  }
    
  private void SetupDotsSettingsGui(int x, int y, int yoffset) 
  {
    var tabtitle = "Dot Settings";

    Gui.addNumberbox("_numberOfDots")
        .setPosition(x, y).setLabel("Number Of Dots")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 3200)
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
        .setRange(0, 1600)
        .setValue(_minDotOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxDotOffset")
        .setPosition(x, y).setLabel("Max Dots Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_maxDotOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

        y += yoffset;
    
        Gui.addNumberbox("_minDotLongitude")
            .setPosition(x, y).setLabel("Min Dot Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minDotLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxDotLongitude")
            .setPosition(x, y).setLabel("Max Dot Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxDotLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);

        y += yoffset;

        Gui.addNumberbox("_minDotLatitude")
            .setPosition(x, y).setLabel("Min Dot Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minDotLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxDotLatitude")
            .setPosition(x, y).setLabel("Max Dot Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxDotLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
  }
    
  private void SetupTriangleSettingsGui(int x, int y, int yoffset) 
  {
    var tabtitle = "Triangle Settings";

    Gui.addNumberbox("_numberOfTriangles")
        .setPosition(x, y).setLabel("Number Of Triangles")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_numberOfTriangles)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_minTriangleWidth")
        .setPosition(x, y).setLabel("Min Triangle Width")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(8, 1600)
        .setValue(_minTriangleWidth)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxTriangleWidth")
        .setPosition(x, y).setLabel("Max Triangle Width")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(8, 1600)
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
        .setRange(0, 1600)
        .setValue(_minTriangleOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

    y += yoffset;

    Gui.addNumberbox("_maxTriangleOffset")
        .setPosition(x, y).setLabel("Max Triangle Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_maxTriangleOffset)
        .moveTo(tabtitle)
        .addCallback(_resetCallbackListener);

        y += yoffset;
    
        Gui.addNumberbox("_minTriangleLongitude")
            .setPosition(x, y).setLabel("Min Triangle Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minTriangleLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxTriangleLongitude")
            .setPosition(x, y).setLabel("Max Triangle Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxTriangleLongitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);

        y += yoffset;

        Gui.addNumberbox("_minTriangleLatitude")
            .setPosition(x, y).setLabel("Min Triangle Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minTriangleLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
    
        y += yoffset;
    
        Gui.addNumberbox("_maxTriangleLatitude")
            .setPosition(x, y).setLabel("Max Triangle Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxTriangleLatitude)
            .moveTo(tabtitle)
            .addCallback(_resetCallbackListener);
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
      var line = RandomLine(RandomGenerator);

      _explodeArrayList.add(line);
    }

    for (var i = 0; i < _numberOfLinesAngled; i++)
    {
      var line = RandomLineAngled(RandomGenerator);

      _explodeArrayList.add(line);
    }

    for (var i = 0; i < _numberOfDots; i++)
    {
      var dot = RandomDot(RandomGenerator);

      _explodeArrayList.add(dot);
    }


    for (var i = 0; i < _numberOfTriangles; i++)
    {
      var triangle = RandomTriangle(RandomGenerator);

      _explodeArrayList.add(triangle);
    }
  
    ColorizeExplodeObjects();
  }
  
  private void ColorizeExplodeObjects()
  {
    _from1Color = _from1ColorColorPicker.getColorValue();
    _to1Color = _to1ColorColorPicker.getColorValue();

    _from2Color = _from2ColorColorPicker.getColorValue();

    _from3Color = _from3ColorColorPicker.getColorValue();

    _colorPicker.Reset(_from1Color, _to1Color, RandomGenerator);
    _colorPicker.AddWeightedColorPicker(_from2Weight, _from2Color, RandomGenerator);
    _colorPicker.AddWeightedColorPicker(_from3Weight, _from3Color, RandomGenerator);
        
     for (var e : _explodeArrayList)
     {
       e.Colorize(_colorPicker);
     }
  }

  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
    NoiseGenerator.ReSeed(NoiseGenerator.Seed());

    // _boxRotation += 0.010;
    _directionalLightRotation += 0.025;

    var from1Color = _from1ColorColorPicker.getColorValue();
    var to1Color = _to1ColorColorPicker.getColorValue();
    var from2Color = _from2ColorColorPicker.getColorValue();
    var from3Color = _from3ColorColorPicker.getColorValue();

    if(_from1Color != from1Color || _to1Color != to1Color || _from2Color != from2Color || _from3Color != from3Color)
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
  
      Canvas.directionalLight(128, 128, 128, (float)Math.Cos(_directionalLightRotation), 0.0f, (float)Math.Sin(_directionalLightRotation));
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

    if (keyCode == java.awt.event.KeyEvent.VK_TAB)
    {
      _selectedPreset++;
      if (_selectedPreset > 3) {
        _selectedPreset = 0;
      }
      Preset(_selectedPreset);
      _explodeArrayList.clear();
      SetupRandomExplodeObjects();
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
   
  Line RandomLine(IRandomGenerator randomGenerator)
  {
    var line = new Line();

    line.Longitude((float) randomGenerator.Value(Math.ToRadians(_minLineLongitude), Math.ToRadians(_maxLineLongitude)));
    line.Latitude((float) randomGenerator.Value(Math.ToRadians(_minLineLatitude), Math.ToRadians(_maxLineLatitude)));

    line.Offset((float)Math.Map(Easing.InCubic(randomGenerator.Value()), _minLineOffset, _maxLineOffset));

    line.Length((float)randomGenerator.Value(_minLineLength, _maxLineLength));
    
    line.Weight((float) RandomGenerator.Value(_minLineWeight, _maxLineWeight));

    return line;
  }
  
  LineAngled RandomLineAngled(IRandomGenerator randomGenerator)
  {
    var line = new LineAngled();

    line.Longitude((float) randomGenerator.Value(Math.ToRadians(_minLineAngledLongitude), Math.ToRadians(_maxLineAngledLongitude)));
    line.Latitude((float) randomGenerator.Value(Math.ToRadians(_minLineAngledLatitude), Math.ToRadians(_maxLineAngledLatitude)));

    line.Offset((float)Math.Map(Easing.InCubic(randomGenerator.Value()), _minLineAngledOffset, _maxLineAngledOffset));

    line.Length((float)randomGenerator.Value(_minLineAngledLength, _maxLineAngledLength));
    
    line.Weight((float) RandomGenerator.Value(_minLineAngledWeight, _maxLineAngledWeight));

    return line;
  }
   
  Dot RandomDot(IRandomGenerator randomGenerator)
  {
    var dot = new Dot();

    dot.Longitude((float)randomGenerator.Value(Math.ToRadians(_minDotLongitude), Math.ToRadians(_maxDotLongitude)));
    dot.Latitude((float) randomGenerator.Value(Math.ToRadians(_minDotLatitude), Math.ToRadians(_maxDotLatitude)));

    dot.Offset((float)Math.Map(Easing.InCubic(randomGenerator.Value()), _minDotOffset, _maxDotOffset));
    dot.Weight((float) randomGenerator.Value(_minDotWeight, _maxDotWeight));

    return dot;
  }
   
  Triangle RandomTriangle(IRandomGenerator randomGenerator)
  {
    var triangle = new Triangle();

    var width = (float) randomGenerator.Value(_minTriangleWidth, _maxTriangleWidth);

    var ratio = (float) randomGenerator.Value(_minTriangleRatio, _maxTriangleRatio);

    triangle.Longitude((float)randomGenerator.Value(Math.ToRadians(_minTriangleLongitude), Math.ToRadians(_maxTriangleLongitude)));
    triangle.Latitude((float) randomGenerator.Value(Math.ToRadians(_minTriangleLatitude), Math.ToRadians(_maxTriangleLatitude)));

    triangle.Offset((float)Math.Map(Easing.InCubic(randomGenerator.Value()), _minTriangleOffset, _maxTriangleOffset));
    
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
    protected float _longitude;
    protected float _latitude;
    protected float _offset;
    protected float[] _direction;

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

    public float Offset() {
      return _offset;
    }

    public void Offset(float offset) {
      _offset = offset;
    }

    public float Longitude()
    {
      return _longitude;
    }

    public void Longitude(float longitude)
    {
      _longitude = longitude;
      ComputeDirectionVector();
    }

    public float Latitude()
    {
      return _latitude;
    }

    public void Latitude(float latitude)
    {
      _latitude = latitude;
      ComputeDirectionVector();
    }

    public abstract void Draw(PGraphics canvas);

    public abstract void Colorize(ColorPicker colorPicker);

    protected void ComputeDirectionVector()
    {
      var v = Math.VectorFromSphericalCoordinates(_longitude, _latitude);

      _direction = new float[] { (float) v[0], (float) v[1], (float) v[2] };
    }
  }

  class Line extends AbstractExplode
  {
    protected float _length = 1.0F;
    protected float _weight = 1.0F;
    protected int _colorOrigin = color(0);
    protected int _colorExtent = color(255);

    public Line() {
    }

    public float Length() {
      return _length;
    }

    public void Length(float length) {
      _length = length;
    }

    public float Weight() {
      return _weight;
    }

    public void Weight(float weight) {
      _weight = weight;
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
      canvas.pushMatrix();

      canvas.rotateY(_longitude);
      canvas.rotateZ(_latitude);
      canvas.translate(_offset, 0, 0);

      canvas.beginShape(LINES);
  
      canvas.strokeWeight(_weight);
      canvas.noFill();

      canvas.stroke(_colorOrigin);
      canvas.vertex(_x, _y, _z);

      canvas.stroke(_colorExtent);
      canvas.vertex(_x + _length, _y, _z);

      canvas.endShape();   
  
      canvas.popMatrix();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      if (_enableColorPerObject)
      {
        var c = colorPicker.RandomColor();
        ColorOrigin(c);
        ColorExtent(c);
      }
      else {
        ColorOrigin(colorPicker.RandomColor());
        ColorExtent(colorPicker.RandomColor());
      }
    }
  }

  class LineAngled extends Line
  {
    private float[] _randomvector = null;

    public LineAngled() {
    }

    @Override
    public void Length(float length)
    {
      _length = length;
      ComputeVectors();
    }

    @Override
    public void Longitude(float longitude)
    {
      _longitude = longitude;
      ComputeVectors();
    }

    @Override
    public void Latitude(float latitude)
    {
      _latitude = latitude;
      ComputeVectors();
    }

    private void ComputeVectors()
    {
      super.ComputeDirectionVector();
      _randomvector = new float[] { _direction[0] * _length, _direction[1] * _length, _direction[2] * _length };
    }

    public void Draw(PGraphics canvas)
    {
      canvas.pushMatrix();

      canvas.rotateY(_longitude);
      canvas.rotateZ(_latitude);
      canvas.translate(_offset, 0, 0);

      canvas.beginShape(LINES);
  
      canvas.strokeWeight(_weight);
      canvas.noFill();

      canvas.stroke(_colorOrigin);
      canvas.vertex(_x, _y, _z);

      canvas.stroke(_colorExtent);
      // canvas.vertex(_x + _length, _y, _z);
      canvas.vertex(_x + _length + _randomvector[0], _y + _randomvector[1], _z + _randomvector[2]);

      canvas.endShape();   
  
      canvas.popMatrix();


      // canvas.beginShape(LINES);

      // canvas.strokeWeight(_weight);
      // canvas.noFill();

      // canvas.stroke(_colorOrigin);
      // canvas.vertex(_x + _offsetvector[0], _y + _offsetvector[1], _z + _offsetvector[2]);

      // canvas.stroke(_colorExtent);
      // canvas.vertex(_x + _offsetvector[0] + _vector[0], _y + _offsetvector[1] + _vector[1],
      //     _z + _offsetvector[2] + _vector[2]);

      // canvas.endShape();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      if (_enableColorPerObject)
      {
        var c = colorPicker.RandomColor();
        ColorOrigin(c);
        ColorExtent(c);
      }
      else {
        ColorOrigin(colorPicker.RandomColor());
        ColorExtent(colorPicker.RandomColor());
      }
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

    public void Draw(PGraphics canvas) 
    {
      canvas.pushMatrix();

      canvas.rotateY(_longitude);
      canvas.rotateZ(_latitude);
      canvas.translate(_offset, 0, 0);

      canvas.beginShape(POINTS);
  
      canvas.strokeWeight(_weight);
      canvas.stroke(_color);
      canvas.noFill();

      canvas.vertex(_x, _y, _z);

      canvas.endShape();   
  
      canvas.popMatrix();
    }
    
    public void Colorize(ColorPicker colorPicker)
    {
      Color(colorPicker.RandomColor());
    }
  }

  class Triangle extends AbstractExplode
  {
    private float[][] _points = new float[3][3];
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
      if (_enableColorPerObject)
      {
        var c = colorPicker.RandomColor();
        Colors(c, c, c);
      }
      else 
      {
        Colors(colorPicker.RandomColor(), colorPicker.RandomColor(), colorPicker.RandomColor());
      }
    }
  } 
}
