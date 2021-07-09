package Dailies;

// import java.util.ArrayList;
// import java.util.List;
import processing.core.*;
import processing.event.MouseEvent;

import java.awt.Color;
import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;

//import processing.opengl.*;
import controlP5.*;
import peasy.*;

import Procedural.*;
import Utility.*;
import Utility.Math;

public class daily_20210617_b extends PApplet
{
  private int ScreenWidth = 736;
  private int ScreenHeight = 736;
  private double SketchResolutionMultiplier = 1.0;
  
  private OpenSimplexNoiseGenerator _noiseGenerator;
  private RandomGenerator _randomGenerator;

  private PGraphics _canvas;

  private ControlP5 _gui;

  private Instant StartTime = Instant.now();
  private Instant LastUpdateTime = Instant.now();

  private boolean _enableWarp = true;
  private float _warpDistance = (float)(120D * SketchResolutionMultiplier);
  private float _noiseInputMultiplierX = 0.0085F;
  private float _noiseInputMultiplierY = 0.0085F;

  public void settings() 
  {
      StartTime = Instant.now();

      size(ScreenWidth, ScreenHeight, P2D);

      smooth(16);

  }
   
	@Override
  public void setup() 
  {
    super.setup();

    _noiseGenerator = new OpenSimplexNoiseGenerator();
    _noiseGenerator.Octaves(4);
    _noiseGenerator.ClampValues(true);
    _noiseGenerator.AllowNegativeValues(false);
    _noiseGenerator.InputMultiplier(_noiseInputMultiplierX / SketchResolutionMultiplier, _noiseInputMultiplierY / SketchResolutionMultiplier, 1.0);

    _randomGenerator = new RandomGenerator(0);

    _canvas = createGraphics((int)(ScreenWidth * SketchResolutionMultiplier), (int)(ScreenHeight * SketchResolutionMultiplier), P2D);
    
    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    _gui = new ControlP5(this);
    _gui.setAutoDraw(true);

    _gui.addToggle("_enableWarp")
      .setPosition(guiX, guiY)
      .setLabel("Enable Warp")
      .setValue(_enableWarp);    

    guiY += guiOffsetY;
 
    _gui.addSlider("_warpDistance")
      .setPosition(guiX, guiY)
      .setSize(200, guiOffsetY * 1)
      .setLabel("Warp distance")
      .setRange(0, 200)
      .setScrollSensitivity(0.25f)
      .setValue((float) _warpDistance);

    guiY += guiOffsetY + guiOffsetY + guiOffsetY;
 
    _gui.addSlider("_noiseInputMultiplierX")
      .setPosition(guiX, guiY)
      .setLabel("Noise input multiplier X")
      .setRange(0.0001F, 0.9000F)
      .setValue(_noiseInputMultiplierX);

    guiY += guiOffsetY;
 
    _gui.addSlider("_noiseInputMultiplierY")
      .setPosition(guiX, guiY)
      .setLabel("Noise input multiplier Y")
      .setRange(0.0001F, 0.9000F)
      .setValue(_noiseInputMultiplierY);

    guiY += guiOffsetY;
      
    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }  
  
  private void Update(float elapsedTime) 
  {

  }
     
	@Override
  public void draw() 
  {
    _noiseGenerator.InputMultiplier(_noiseInputMultiplierX, _noiseInputMultiplierY, 1.0);

    var now = Instant.now();

    Update((float) ((double) Duration.between(LastUpdateTime, now).toMillis() / 1000.0));

    LastUpdateTime = now;

    background(0);

    _canvas.beginDraw();

    _canvas.clear();
    _canvas.background(255);
    
    var innerEllipseRadius = (int)(200 * SketchResolutionMultiplier);
    var outerEllipseRadius = (int)(400 * SketchResolutionMultiplier);

    _canvas.noStroke();

    for (var i = outerEllipseRadius; i > innerEllipseRadius; i--)
    {
      var c = (int) Math.Map(i - innerEllipseRadius, 0, outerEllipseRadius - innerEllipseRadius, 0, 255);
      _canvas.fill(c, c, c);
      _canvas.ellipse(_canvas.width / 2, _canvas.height / 2, i, i);
    }
    
    _canvas.fill(0);
    _canvas.ellipse(_canvas.width / 2, _canvas.height / 2, innerEllipseRadius, innerEllipseRadius);

    if (_enableWarp) 
    {
      _canvas.loadPixels();

      var temp = createImage(_canvas.width, _canvas.height, RGB);

      temp.loadPixels();

      for (var y = 0; y < _canvas.height; y++) 
      {
        for (var x = 0; x < _canvas.width; x++) 
        {
          var angle = _noiseGenerator.Value(x, y) * Math.Pi * 2D;

          var xx = x + (int) (Math.Cos(angle) * _warpDistance);
          var yy = y + (int) (Math.Sin(angle) * _warpDistance);

          if (xx < 0) {
            xx = 0;
          } else if (xx >= _canvas.width) {
            xx = _canvas.width - 1;
          }

          if (yy < 0) {
            yy = 0;
          } else if (yy >= _canvas.height) {
            yy = _canvas.height - 1;
          }

          var color = _canvas.pixels[(int) (yy * _canvas.height + xx)];

          temp.pixels[(int) (y * temp.height + x)] = color;
        }
      }

      temp.updatePixels();

      _canvas.image(temp, 0, 0);

      _canvas.updatePixels();
    }

    _canvas.endDraw();

    image(_canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  

  public void mousePressed() 
  {
    // System.out.println("mousePressed");
    
    cursor(HAND);
  }    

  public void mouseDragged() 
  {
    // System.out.println("mouseDragged");

    cursor(HAND);
  }   
  
  public void mouseReleased() 
  {
    cursor(ARROW);
  }

  public void mouseWheel(MouseEvent event) 
  {
    // System.out.println(event.getCount());

    // System.out.println(ViewportZoomFactor);
  }

  public void keyPressed() 
  {
    if (key == CODED) 
    {
      switch (keyCode) {
        case CONTROL:
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
 
  public void keyReleased() 
  {
    if (key == CODED) 
    {
      switch (keyCode) {
        case CONTROL:
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
    else 
    {
      switch (key) {
        case 's':
          DateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
          _canvas.save("output/" + dateFormat.format(java.util.Calendar.getInstance().getTime()) + "." + this.getClass().getSimpleName() + ".png");
          break;
        default:
          break;
      }
    }
  }    
}