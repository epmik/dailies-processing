package Dailies;

import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.time.Duration;
import java.time.Instant;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import controlP5.*;

import Procedural.*;

public abstract class AbstractDaily extends PApplet {

  public int ScreenWidth = 768;
  public int ScreenHeight = 768;
  public double SketchResolutionMultiplier = 1.0;

  public OpenSimplexNoiseGenerator NoiseGenerator;
  public RandomGenerator RandomGenerator;

  public PGraphics Canvas;

  public ControlP5 Gui;

  public Instant StartTime = Instant.now();
  public Instant LastUpdateTime = Instant.now();

  public boolean PropagateKeyboardEvents = true;
  public boolean PropagateMouseEvents = true;

  public boolean ResetOnRightMouseRelease = true;

  protected String Renderer = P2D;

  @Override
  public void settings() 
  {
    super.settings();

    StartTime = Instant.now();

    size(ScreenWidth, ScreenHeight, P2D);

    smooth(16);
  }

  @Override
  public void setup() 
  {
    super.setup();

    NoiseGenerator = new OpenSimplexNoiseGenerator();
    RandomGenerator = new RandomGenerator();

    Gui = new ControlP5(this);
    Canvas = CreateAndSetupGraphics();

    System.out.println("AbstractDaily.setup done");

    LastUpdateTime = Instant.now();
  }
  
  protected PGraphics CreateAndSetupGraphics()
  {
    return CreateAndSetupGraphics((int) (ScreenWidth * SketchResolutionMultiplier), (int) (ScreenHeight * SketchResolutionMultiplier));
  }
  
  protected PGraphics CreateAndSetupGraphics(int width, int height)
  {
    return CreateAndSetupGraphics(width, height, Renderer);
  }
  
  protected PGraphics CreateAndSetupGraphics(int width, int height, String renderen)
  {
    var g = createGraphics(width, height, renderen);

    g.beginDraw();

    g.clear();

    g.noStroke();

    g.fill(0);

    g.rect(0, 0, g.width, g.height);

    g.endDraw();

    return g;
  }

  @Override
  public void draw() 
  {
    var now = Instant.now();

    var elapsedTime = ((double) Duration.between(LastUpdateTime, now).toMillis() / 1000.0);

    LastUpdateTime = now;

    Update(elapsedTime);

    Draw(elapsedTime);
  }

  public void Update(double elapsedTime) {

  }

  public void Draw(double elapsedTime) {

  }

  public void mousePressed(MouseEvent event) {
    if (!PropagateMouseEvents) {
      return;
    }
  }

  public void mouseDragged(MouseEvent event) {
    if (!PropagateMouseEvents) {
      return;
    }
  }

  public void mouseReleased(MouseEvent event) 
  {
    if (!PropagateMouseEvents) {
      return;
    }

    if (ResetOnRightMouseRelease && event.getButton() == PConstants.RIGHT)
    {
      NoiseGenerator.ReSeed();
      RandomGenerator.ReSeed();
    }
  }

  public void mouseWheel(MouseEvent event) {
    if (!PropagateMouseEvents) {
      return;
    }
  }

  public void keyPressed(KeyEvent event) {
    if (!PropagateKeyboardEvents) {
      return;
    }

    if (event.getKey() == PConstants.CODED) {
      switch (event.getKeyCode()) {
        case CONTROL:
          break;
        case SHIFT:
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

  public void keyReleased(KeyEvent event) {

    if (!PropagateKeyboardEvents && !(key != CODED && key == 's')) {
      return;
    }

    if (event.getKey() == PConstants.CODED) {
      switch (event.getKeyCode()) {
        case CONTROL:
          break;
        case SHIFT:
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
    } else {
      switch (event.getKey()) {
        case 's':
          SaveFrame(Canvas);
          break;
        default:
          break;
      }
    }
  }

  protected String ClassName() 
  {
    return this.getClass().getSimpleName();
  }
  // protected void CreateClassDirectories() 
  // {
  //   CreateDirectory(ClassFolder());
  //   CreateDirectory(ClassFolderInput());
  //   CreateDirectory(ClassFolderOutput());
  // }

  protected boolean CreateClassOutputDirectory() 
  {
    var directory = new File(PathCombine(InputDirectory(), ClassName()));

    return directory.mkdir();
  }

  // protected String ClassFolder() 
  // {
  //   return "data" + File.separator + ClassName();
  // }

  protected String InputDirectory() 
  {
    return "data" + File.separator  + "in";
  }

  protected String OutputDirectory() 
  {
    return "data" + File.separator  + "out";
  }

  public static String PathCombine(String... paths)
  {
      File file = new File(paths[0]);

      for (int i = 1; i < paths.length; i++) 
      {
          file = new File(file, paths[i]);
      }

      return file.getPath();
  }

  public static boolean FileExists(String path)
  {
    var file = new File(path);

    return file.exists() && !file.isDirectory();
  }

  protected String ResolveInputFile(String file)
  {
    var name = ClassName();

    // first try to load from a directory whose name is identical to the class name
    var destination = PathCombine(InputDirectory(), PathCombine(name, file));

    try 
    {

      if (FileExists(destination)) {
        return destination;
      }
      
      CreateClassOutputDirectory();

      // try to load from a directory whose name is identical to the generic part of the class name: only the 'daily_' and date part like 'daily_20210622'

      name = name.substring(0, "daily_yyyyMMdd".length());
      var path = PathCombine(InputDirectory(), PathCombine(name, file));

      if (FileExists(path)) {
        CopyFile(path, destination);
        return destination;
      }

      // try to load from a directory whose name is identical to the generic part of the class name, without the day part: only the 'daily_' and date part like 'daily_202106'

      name = name.substring(0, "daily_yyyyMM".length());
      path = PathCombine(InputDirectory(), PathCombine(name, file));

      if (FileExists(path)) {
        CopyFile(path, destination);
        return destination;
      }

      // try to load from a directory whose name is identical to the generic part of the class name, without the month/day part: only the 'daily_' and date part like 'daily_2021'

      name = name.substring(0, "daily_yyyy".length());
      path = PathCombine(InputDirectory(), PathCombine(name, file));

      if (FileExists(path)) {
        CopyFile(path, destination);
        return destination;
      }

      // try to load from the input directory

      path = PathCombine(InputDirectory(), file);

      if (FileExists(path)) {
        CopyFile(path, destination);
        return destination;
      }

      throw new FileNotFoundException("ResolveInputFile(String) could not resolve the file: " + file);
    } 
    catch (IOException e) 
    {
      System.out.println(e.getMessage());

      return file;
    }
  }
  
  private void CopyFile(String source, String destination) throws IOException
  {
    Files.copy(new File(source).toPath(), new File(destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
  }
  
  protected void SaveFrame(PImage canvas) 
  {
    DateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd.HHmmss.SSS");

    SaveFrame(canvas, PathCombine(OutputDirectory(), dateFormat.format(java.util.Calendar.getInstance().getTime()) + "." + ClassName() + ".png"));
  }
  
  protected void SaveFrame(PImage canvas, String path) 
  {
    canvas.save(path);
  }

  protected class GuiCallbackListener implements CallbackListener {
    private AbstractDaily _parent;

    public GuiCallbackListener(AbstractDaily parent) {
      super();
      _parent = parent;
    }

    // int col;
    public void controlEvent(CallbackEvent e) {
      // var c = e.getController();

      // System.out.println(c.getName() + " controlEvent");

      var a = e.getAction();

      switch (a) {
        case (ControlP5.ACTION_LEAVE):
          _parent.PropagateKeyboardEvents = true;
          _parent.PropagateMouseEvents = true;
          // System.out.println(_parent.PropagateMouseEvents);
          break;
        case (ControlP5.ACTION_ENTER):
          _parent.PropagateKeyboardEvents = false;
          _parent.PropagateMouseEvents = false;
          // System.out.println(_parent.PropagateMouseEvents);
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

  protected class GuiControlListener implements ControlListener 
  {  
    private AbstractDaily _parent;
  
    public GuiControlListener(AbstractDaily parent) 
    {
      super();
      _parent = parent;
    }
  
    // int col;
    public void controlEvent(ControlEvent e) 
    {
      //var c = e.getController();
    }
  }
}


