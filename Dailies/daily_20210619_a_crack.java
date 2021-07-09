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
import Utility.Easing;
import Utility.Math;

public class daily_20210619_a_crack extends PApplet {
  private int ScreenWidth = 768;
  private int ScreenHeight = 768;
  private double SketchResolutionMultiplier = 1.0;

  private OpenSimplexNoiseGenerator _noiseGenerator;
  private RandomGenerator _randomGenerator;

  private PGraphics _canvas;

  private ControlP5 _gui;

  // private Instant StartTime = Instant.now();
  private Instant LastUpdateTime = Instant.now();

  private boolean _enableWarp = true;
  private float _warpDistance = (float) (24 * SketchResolutionMultiplier);
  private float _noiseInputMultiplierX = 0.200F;
  private float _noiseInputMultiplierY = 0.200F;

  private List<String> _presetsList;
  private int _presetsListIndex;

  private static String _presetsScrollableListName = "_presetsScrollableListItem";

  public void settings() 
  {
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

    _randomGenerator = new RandomGenerator(0);

    _canvas = createGraphics((int) (ScreenWidth * SketchResolutionMultiplier),
        (int) (ScreenHeight * SketchResolutionMultiplier), P2D);

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

    _gui = new ControlP5(this);
    // _gui.setAutoDraw(true);

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

    _presetsList = Arrays.asList("100 | 0.0085 | 0.0085", "117 | 0.07 | 0.10", "117 | 0.06 | 0.00", "200 | 0.90 | 0.90",
        "100 | 0.07 | 0.00", "200 | 0.00 | 0.05", "25 | 0.02 | 0.10", "150 | 0.02 | 0.10", "24 | 0.0002 | 0.0002");

    _presetsListIndex = 0;

    // LoadPreset();

    System.out.println("setup done");

    LastUpdateTime = Instant.now();
  }
  
  private void LoadPreset()
  {
    var s = _presetsList.get(_presetsListIndex);

    var v = s.split(" | ");

    _warpDistance = parseFloat(v[0]);
    _noiseInputMultiplierX = parseFloat(v[2]);
    _noiseInputMultiplierY = parseFloat(v[4]);
    
    _gui.getController("_warpDistance").setValue(_warpDistance);
    _gui.getController("_noiseInputMultiplierX").setValue(_noiseInputMultiplierX);
    _gui.getController("_noiseInputMultiplierY").setValue(_noiseInputMultiplierY);
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

    var innerEllipseRadius = (int) (50 * SketchResolutionMultiplier);
    var outerEllipseRadius = (int) (100 * SketchResolutionMultiplier);

    var r = 167;
    var g = 6;
    var b = 32;
    var a = (float) (25.5D / (double) (outerEllipseRadius - innerEllipseRadius));
    var deepYellow = color(255, 180, 0);
    var deepRed = color(r, g, b);

    _canvas.beginDraw();

    _canvas.clear();
    _canvas.background(255);

    _randomGenerator.ReSeed(_randomGenerator.Seed());

    var minradius = 50;//_canvas.width * 0.5;
    var maxradius = 100;//_canvas.width * 0.75;
    var yspan = _canvas.height * 0.85;

    for (var i = 0; i < 1; i++) 
    {
      drawEllipses(16, minradius, maxradius, _canvas.width / 2, yspan);

      minradius *= 0.75;
      maxradius *= 0.75;
      yspan *= 0.75;
    }

    if (_enableWarp) {
      _canvas.loadPixels();

      var temp = createImage(_canvas.width, _canvas.height, RGB);

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

    _canvas.endDraw();

    image(_canvas, 0, 0, ScreenWidth, ScreenHeight);
  }
  
  private void drawEllipses(int count, double minradius, double maxradius, int x, double yspan)
  {
    var r = 167;
    var g = 6;
    var b = 32;
    var deepRed = color(r, g, b);

    for (var i = 0; i < count; i++) 
    {
      var yoffset = (_canvas.height - yspan) / 1;
      var radius = _randomGenerator.Value(minradius, maxradius);
      var y = (float) _randomGenerator.Value(yoffset, _canvas.height - yoffset);

      var d = Easing.OutCubic(1D - Math.Abs((_canvas.height / 2) - y) / (double) (_canvas.height / 2));

      radius *= d;

      _canvas.stroke(0);
      _canvas.strokeWeight((float) (radius * 0.1D));
      _canvas.fill(deepRed);
      _canvas.ellipse(_canvas.width / 2, y, (float) radius, (float) radius);
    }
  }

  public void mousePressed() {
    // System.out.println("mousePressed");

    cursor(HAND);
  }

  public void mouseDragged() {
    // System.out.println("mouseDragged");

    cursor(HAND);
  }

  public void mouseReleased() {
    cursor(ARROW);
    _noiseGenerator.ReSeed();
    _randomGenerator.ReSeed();
  }

  public void mouseWheel(MouseEvent event) {
    // System.out.println(event.getCount());

    // System.out.println(ViewportZoomFactor);
  }

  public void keyPressed() {
    if (key == CODED) {
      switch (keyCode) {
        case CONTROL:
          ((Numberbox)_gui.getController("_noiseInputMultiplierX"))
            .setScrollSensitivity(0.001F);
          ((Numberbox)_gui.getController("_noiseInputMultiplierY"))
            .setScrollSensitivity(0.001F);
          break;
        case SHIFT:
          ((Numberbox)_gui.getController("_noiseInputMultiplierX"))
            .setScrollSensitivity(0.01F);
          ((Numberbox)_gui.getController("_noiseInputMultiplierY"))
            .setScrollSensitivity(0.01F);
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
    if (key == CODED) {
      switch (keyCode) {
        case CONTROL:
          ((Numberbox)_gui.getController("_noiseInputMultiplierX"))
            .setScrollSensitivity(0.0001F);
          ((Numberbox)_gui.getController("_noiseInputMultiplierY"))
            .setScrollSensitivity(0.0001F);
          break;
        case SHIFT:
          ((Numberbox)_gui.getController("_noiseInputMultiplierX"))
            .setScrollSensitivity(0.001F);
          ((Numberbox)_gui.getController("_noiseInputMultiplierY"))
            .setScrollSensitivity(0.001F);
          break;
        case UP:
          _presetsListIndex++;
          if (_presetsListIndex >= _presetsList.size()) {
            _presetsListIndex = 0;
          }
          LoadPreset();
          break;
        case DOWN:
        _presetsListIndex--;
        if (_presetsListIndex < 0) {
          _presetsListIndex = _presetsList.size() - 1;
        }
      LoadPreset();
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
    } else {
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

  // //ControlP5 callback - method name must be the same as the string parameter of cp5.addScrollableList()
  // void dropdown(int n) 
  // {
  //     System.out.println("dropdown");
  //     var selected = _gui.get(ScrollableList.class, _presetsScrollableListName).getItem(n);
  // }

  // void controlEvent(ControlEvent e)
  // {
  //   if (!e.isController()) {
  //     return;
  //   }


  //   if (e.getController().getName() == _presetsScrollableListName) 
  //   {
  //     var controller = (ScrollableList)e.getController();

  //     //var v = controller.g();

  //     // v.split(" | ");
  //   }
  // }
}


class GuiControlListener implements ControlListener 
{
  // int col;
  public void controlEvent(ControlEvent e) 
  {
    //var c = e.getController();
  }
}

class GuiCallbackListener implements CallbackListener 
{
  // int col;
  public void controlEvent(CallbackEvent e) 
  {
    // var c = e.getController();

    // System.out.println(c.getName() + " controlEvent");

    //var a = e.getAction();

    // switch (a) 
    // {
    //   // case (ControlP5.ACTION_START_DRAG):
    //   //   _sketch.DisableViewportDragging();
    //   //   break;
    //   //   case(ControlP5.ACTION_END_DRAG):
    //   //   _sketch.EnableViewportDragging();
    //   //   break;
    //     case(ControlP5.ACTION_LEAVE):
    //     _sketch.EnableViewportDragging();
    //     _sketch.EnableViewportScrolling();
    //     break;
    //     case(ControlP5.ACTION_ENTER):
    //     _sketch.DisableViewportDragging();
    //     _sketch.DisableViewportScrolling();
    //     break;
    // }    

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