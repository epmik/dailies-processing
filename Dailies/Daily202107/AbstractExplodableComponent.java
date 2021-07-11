package Dailies.Daily202107;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Procedural.*;
import Procedural.Interfaces.IRandomGenerator;
import controlP5.CallbackListener;
import controlP5.ControlP5;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public abstract class AbstractExplodableComponent<TExplodable extends IExplodable> implements IExplodableComponent
{
  public IRandomGenerator RandomGenerator = new RandomGenerator();

  protected UUID Guid = UUID.randomUUID();

  protected float _x;
  protected float _y;
  protected float _z;

  public float _minOffset = 0.0f;
  public float _maxOffset = 320.0f;
  public float _minLongitude = 0.0f;
  public float _maxLongitude = 360.0f;
  public float _minLatitude = 0.0f;
  public float _maxLatitude = 360.0f;

  public boolean _isTransparent = false;

  protected int _numberOfExplodables = 1;
  protected List<TExplodable> _explodables = new ArrayList<TExplodable>();

  protected ICreateExplodableListener<TExplodable, IExplodableComponent> _createExplodableListener;

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

  public boolean IsTransparent()
  {
    return _isTransparent;
  }

  public int NumberOfExplodables()
  {
    return _numberOfExplodables;
  }

  public void NumberOfExplodables(int numberOfExplodables)
  {
    _numberOfExplodables = numberOfExplodables;
  }

  public List<TExplodable> Explodables()
  {
    return _explodables;
  }

  public <TExplodableComponent extends IExplodableComponent> void CreateExplodables(
    ICreateExplodableListener<TExplodable, TExplodableComponent> createExplodableListener)
  {
    _createExplodableListener = (ICreateExplodableListener<TExplodable, IExplodableComponent>) createExplodableListener;

    for (var i = 0; i < _numberOfExplodables; i++)
    {
      _explodables.add(_createExplodableListener.Create(this));
    }
  }

  public abstract void Update(double elapsedTime);

  public abstract void Draw(PGraphics canvas, double elapsedTime);

  protected void BindDefaultProperties(ControlP5 gui)
  {
    _numberOfExplodables = (int)gui.getController(Guid + "_numberOfExplodables").getValue();
    _minOffset = gui.getController(Guid + "_minOffset").getValue();
    _maxOffset = gui.getController(Guid + "_maxOffset").getValue();
    _minLongitude = gui.getController(Guid + "_minLongitude").getValue();
    _maxLongitude = gui.getController(Guid + "_maxLongitude").getValue();
    _minLatitude = gui.getController(Guid + "_minLatitude").getValue();
    _maxLatitude = gui.getController(Guid + "_maxLatitude").getValue();
  }
  
  protected int SetupGuiForDefaultProperties(ControlP5 gui, String tabtitle, CallbackListener guiOnChangeListener, int x, int y, int yoffset) 
  {
    gui.addNumberbox(Guid + "_numberOfExplodables")
        .setPosition(x, y).setLabel("Number Of Lines")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 3200)
        .setValue(_numberOfExplodables)
        .moveTo(tabtitle)
        .onChange(guiOnChangeListener);

    y += yoffset;
    
    gui.addNumberbox(Guid + "_minOffset")
        .setPosition(x, y).setLabel("Min Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_minOffset)
        .moveTo(tabtitle)
        .onChange(guiOnChangeListener);

    y += yoffset;

    gui.addNumberbox(Guid + "_maxOffset")
        .setPosition(x, y).setLabel("Max Line Offset")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(0, 1600)
        .setValue(_maxOffset)
        .moveTo(tabtitle)
        .onChange(guiOnChangeListener);

        y += yoffset;
    
        gui.addNumberbox(Guid + "_minLongitude")
            .setPosition(x, y).setLabel("Min Line Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minLongitude)
            .moveTo(tabtitle)
            .onChange(guiOnChangeListener);
    
        y += yoffset;
    
        gui.addNumberbox(Guid + "_maxLongitude")
            .setPosition(x, y).setLabel("Max Line Longitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_maxLongitude)
            .moveTo(tabtitle)
            .onChange(guiOnChangeListener);

        y += yoffset;

        gui.addNumberbox(Guid + "_minLatitude")
            .setPosition(x, y).setLabel("Min Line Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .setValue(_minLatitude)
            .moveTo(tabtitle)
            .onChange(guiOnChangeListener);
    
        y += yoffset;
    
        gui.addNumberbox(Guid + "_maxLatitude")
            .setPosition(x, y).setLabel("Max Line Latitude")
            .setScrollSensitivity(1F).setMultiplier(1F)
            .setRange(0, 360)
            .moveTo(tabtitle)
            .setValue(_maxLatitude)
            .onChange(guiOnChangeListener);
            
        return y;       
  }

  public void Reset()
  {

  }
  
  public void HandleKeyPress(KeyEvent event)
  {
  }
 
  public void HandleKeyRelease(KeyEvent event)
  {
  }

  public void HandleMouseRelease(MouseEvent event) 
  {
    if (event.getButton() == PConstants.RIGHT) 
    {
      Reset();
    }
  }  

}
