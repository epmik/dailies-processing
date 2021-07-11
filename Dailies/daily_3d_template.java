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

public class daily_3d_template extends AbstractDaily 
{
  private float _boxRotation;
  private float _directionalLightRotation;
  private PeasyCam _camera;

  public daily_3d_template() 
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

    var guiOffsetY = 40;
    var guiX = guiOffsetY;
    var guiY = guiOffsetY;

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
 
  @Override
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());
    NoiseGenerator.ReSeed(NoiseGenerator.Seed());

    _boxRotation += 0.010;
    _directionalLightRotation += 0.025;
  }

  @Override
  public void Draw(double elapsedTime) 
  {
    background(0);

    Canvas.beginDraw();

    Canvas.clear();

    Canvas.background(178);

    Canvas.strokeWeight(1);

    Canvas.lights();
    Canvas.ambientLight(5, 5, 5);
    Canvas.directionalLight(243, 102, 126, (float)Math.Cos(_directionalLightRotation), 0, (float)Math.Sin(_directionalLightRotation));
    
    Canvas.pushMatrix();
    Canvas.translate(500F, Canvas.height * 0.35F, -200F);
    Canvas.fill(164);
    Canvas.noStroke();
    Canvas.sphere(200);
    Canvas.popMatrix();

    Canvas.pushMatrix();
    Canvas.translate(130, height/2, 0);
    Canvas.rotateY(1.25F);
    Canvas.rotateX(-0.4F);
    Canvas.noStroke();
    Canvas.fill(125, 125, 125, 125);
    Canvas.box(100);
    Canvas.popMatrix();
  
    Canvas.pushMatrix();
    Canvas.translate(Canvas.width / 2, Canvas.height / 2, 0);
    Canvas.rotateX(PI / 9);
    Canvas.rotateY(PI / 5 + _boxRotation);
    Canvas.noStroke();
    Canvas.fill(64, 64, 64, 64);
    Canvas.box(150);
    Canvas.popMatrix();
    
    Canvas.endDraw();

    image(Canvas, 0, 0, ScreenWidth, ScreenHeight);
  } 

  public void mouseReleased() 
  {
    super.mouseReleased();

    if (!PropagateMouseEvents) {
      return;
    }

    // if (mouseButton == RIGHT) 
    // {
    //   NoiseGenerator.ReSeed();
    //   RandomGenerator.ReSeed();
    // }
  }
}
