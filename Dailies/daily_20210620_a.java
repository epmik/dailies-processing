package Dailies;

// import java.util.ArrayList;
// import java.util.List;
import processing.core.*;
import processing.event.MouseEvent;

import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.eclipse.collections.api.list.primitive.DoubleList;

//import processing.opengl.*;
import controlP5.*;

import Procedural.*;
// import Utility.*;
import Utility.Color20210703;
import Utility.Easing;
import Utility.Math;

public class daily_20210620_a extends PApplet {
  private int ScreenWidth = 1024;
  private int ScreenHeight = 1024;
  private double SketchResolutionMultiplier = 2.0;

  private OpenSimplexNoiseGenerator _noiseGenerator;
  private RandomGenerator _randomGenerator;

  private PGraphics _canvas;

  private ControlP5 _gui;

  // private Instant StartTime = Instant.now();
  private Instant LastUpdateTime = Instant.now();

  private boolean _enableWarp = true;
  private float _warpDistance = (float) (40 * SketchResolutionMultiplier);
  private float _noiseInputMultiplierX = 0.0020F;
  private float _noiseInputMultiplierY = 0.0015F;
  private float _ellipseRadiusMultiplier = 0.15F;
  // private int _ellipseRadiusMultiplierStepCount = 0;
  private float _innerEllipseRadius = (float) (300 * SketchResolutionMultiplier);
  private float _outerEllipseRadius = (float) (400 * SketchResolutionMultiplier);

  public boolean PropagateKeyboardEvents = true;
  public boolean PropagateMouseEvents = true;

  public void settings() {
    // StartTime = Instant.now();

    size(ScreenWidth, ScreenHeight, P2D);

    smooth(16);

  }

  @Override
  public void setup() {
    super.setup();

    _noiseGenerator = new OpenSimplexNoiseGenerator(0);
    _noiseGenerator.Octaves(4);
    _noiseGenerator.ClampValues(true);
    _noiseGenerator.AllowNegativeValues(false);
    _noiseGenerator.InputMultiplier(_noiseInputMultiplierX, _noiseInputMultiplierY, 1.0);

    _randomGenerator = new RandomGenerator(0L);

    _canvas = createGraphics((int) (ScreenWidth * SketchResolutionMultiplier),
        (int) (ScreenHeight * SketchResolutionMultiplier), P2D);

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    _gui = new ControlP5(this);
    // _gui.setAutoDraw(true);

    _gui.addCallback(new GuiCallbackListener(this));

    _gui.addToggle("_enableWarp").setPosition(guiX, guiY).setLabel("Enable Warp").setValue(_enableWarp);

    guiY += guiOffsetY;

    _gui.addSlider("_warpDistance").setPosition(guiX, guiY).setLabel("Warp distance").setRange(0, 200)
        .setValue((float) _warpDistance);

    guiY += guiOffsetY;

    _gui.addNumberbox("_noiseInputMultiplierX").setPosition(guiX, guiY).setLabel("Noise input multiplier X")
        .setRange(0.0001F, 0.4000F).setDecimalPrecision(4).setScrollSensitivity(0.0001F)
        .setValue(_noiseInputMultiplierX);

    guiY += guiOffsetY;

    _gui.addNumberbox("_noiseInputMultiplierY").setPosition(guiX, guiY).setLabel("Noise input multiplier Y")
        .setRange(0.0001F, 0.4000F).setDecimalPrecision(4).setScrollSensitivity(0.0001F)
        .setValue(_noiseInputMultiplierY);

    guiY += guiOffsetY;

    _gui.addNumberbox("_ellipseRadiusMultiplier").setPosition(guiX, guiY).setLabel("Ellipse radius multiplier")
        .setRange(-0.975F, 0.975F).setDecimalPrecision(3).setScrollSensitivity(0.025F)
        .setValue(_ellipseRadiusMultiplier);

    guiY += guiOffsetY;

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }

  private void Update(float elapsedTime) {

  }

  @Override
  public void draw() {
    _noiseGenerator.InputMultiplier(_noiseInputMultiplierX, _noiseInputMultiplierY, 1.0);

    var now = Instant.now();

    Update((float) ((double) Duration.between(LastUpdateTime, now).toMillis() / 1000.0));

    LastUpdateTime = now;

    background(0);

    _canvas.beginDraw();

    _canvas.clear();
    // _canvas.background(255);

    _randomGenerator.ReSeed(_randomGenerator.Seed());

    // var factor = _ellipseRadiusMultiplier;
    var minradius = _innerEllipseRadius - (_innerEllipseRadius * _ellipseRadiusMultiplier);
    var maxradius = _outerEllipseRadius - (_outerEllipseRadius * _ellipseRadiusMultiplier);
    var yspan = _canvas.height - (_canvas.height * _ellipseRadiusMultiplier);

    // System.out.println(_ellipseRadiusMultiplier);
    // System.out.println(yspan);

    drawEllipses(16, minradius, maxradius, _canvas.width / 2, yspan);

    if (_enableWarp) {
      Warp();
    }

    _canvas.endDraw();

    image(_canvas, 0, 0, ScreenWidth, ScreenHeight);
  }

  private void drawEllipses(int count, double minradius, double maxradius, int x, double yspan) {
    var r = 167;
    var g = 6;
    var b = 32;
    var deepRed = color(r, g, b);

    for (var i = 0; i < count; i++) {
      var yoffset = (_canvas.height / 2) - (yspan / 2);
      var radius = _randomGenerator.Value(minradius, maxradius);
      var y = (float) _randomGenerator.Value(yoffset, yoffset + yspan);

      var d = Easing.OutCubic(1D - Math.Abs((_canvas.height / 2) - y) / (double) (_canvas.height / 2));

      radius *= d;

      _canvas.stroke(0);
      _canvas.strokeWeight((float) (radius * 0.1D));
      _canvas.fill(deepRed);
      _canvas.ellipse(_canvas.width / 2, y, (float) radius, (float) radius);
    }
  }

  private void Warp() {
    _canvas.loadPixels();

    var temp = createImage(_canvas.width, _canvas.height, ARGB);

    temp.loadPixels();

    for (var y = 0; y < _canvas.height; y++) {
      for (var x = 0; x < _canvas.width; x++) {
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

  public void mousePressed() {
    // System.out.println("mousePressed");
    if (!PropagateMouseEvents) {
      return;
    }

    // cursor(HAND);
  }

  public void mouseDragged() {
    // System.out.println("mouseDragged");
    // cursor(HAND);
    if (!PropagateMouseEvents) {
      return;
    }
  }

  public void mouseReleased() {
    // cursor(ARROW);
    if (!PropagateMouseEvents) {
      return;
    }
    _noiseGenerator.ReSeed();
    _randomGenerator.ReSeed();
  }

  public void mouseWheel(MouseEvent event) {
    if (!PropagateMouseEvents) {
      return;
    }
    // System.out.println(event.getCount());

    // System.out.println(ViewportZoomFactor);
  }

  public void keyPressed() {
    if (!PropagateKeyboardEvents) {
      return;
    }

    if (key == CODED) {
      switch (keyCode) {
        case CONTROL:
          ((Numberbox) _gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.001F);
          ((Numberbox) _gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.001F);
          break;
        case SHIFT:
          ((Numberbox) _gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.01F);
          ((Numberbox) _gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.01F);
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

    if (!PropagateKeyboardEvents && !(key != CODED && key == 's')) {
      return;
    }

    if (key == CODED) {
      switch (keyCode) {
        case CONTROL:
          ((Numberbox) _gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.0001F);
          ((Numberbox) _gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.0001F);
          break;
        case SHIFT:
          ((Numberbox) _gui.getController("_noiseInputMultiplierX")).setScrollSensitivity(0.001F);
          ((Numberbox) _gui.getController("_noiseInputMultiplierY")).setScrollSensitivity(0.001F);
          break;
        case UP:
          // _ellipseRadiusMultiplierStepCount++;
          // ((Numberbox)_gui.getController("_ellipseRadiusMultiplier"))
          //   .setValue(_ellipseRadiusMultiplierStepCount * _ellipseRadiusMultiplier);
          break;
        case DOWN:
          // _ellipseRadiusMultiplierStepCount--;
          // ((Numberbox)_gui.getController("_ellipseRadiusMultiplier"))
          //   .setValue(_ellipseRadiusMultiplierStepCount * _ellipseRadiusMultiplier);
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
          _canvas.save("output/" + dateFormat.format(java.util.Calendar.getInstance().getTime()) + "."
              + this.getClass().getSimpleName() + ".png");
          break;
        default:
          break;
      }
    }
  }

  class GuiCallbackListener implements CallbackListener 
  {
    private daily_20210620_a _parent;

    public GuiCallbackListener(daily_20210620_a parent) 
    {
      super();
      _parent = parent;
    }

    // int col;
    public void controlEvent(CallbackEvent e) 
    {
      // var c = e.getController();

      // System.out.println(c.getName() + " controlEvent");

      var a = e.getAction();

      switch (a) 
      {
        case (ControlP5.ACTION_LEAVE):
          _parent.PropagateKeyboardEvents = true;
          _parent.PropagateMouseEvents = true;
          break;
        case(ControlP5.ACTION_ENTER):
        _parent.PropagateKeyboardEvents = false;
        _parent.PropagateMouseEvents = false;
        break;
      }    

      // c.getName();

      // if (e.isFrom()) {
      //   println("this event was triggered by Controller n1");
      // }

      // println("i got an event from mySlider, " +
      //         "changing background color to "+
      //         theEvent.getController().getValue());
      // col = (int)theEvent.getController().getValue();
    }
  }

}


// class GuiControlListener implements ControlListener 
// {  
//   private PApplet _parent;

//   public GuiControlListener(PApplet parent) 
//   {
//     super();
//     _parent = parent;
//   }

//   // int col;
//   public void controlEvent(ControlEvent e) 
//   {
//     //var c = e.getController();
//   }
// }

