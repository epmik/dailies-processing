package Dailies.Daily202107;

import processing.core.*;
import controlP5.*;
import Procedural.*;
import Utility.Color;
import Utility.ColorPicker;

public class ExplodableDotComponent extends AbstractExplodableComponent<ExplodableDot>
{
  public float _minWeight = 1.0f;
  public float _maxWeight = 16.0f;

  public int _from1Color = Color.RgbToInt(200, 200, 200);
  private controlP5.ColorPicker _from1ColorColorPicker;

  public int _from2Color = Color.RgbToInt(255, 179, 0);
  private controlP5.ColorPicker _from2ColorColorPicker;
  public float _from2Weight = 0.1f;

  public int _from3Color = Color.RgbToInt(255, 179, 0);
  private controlP5.ColorPicker _from3ColorColorPicker;
  public float _from3Weight = 0.1f;

  public ColorPicker _colorPicker;

  public ExplodableDotComponent() 
  {
    super();

    RandomGenerator = new RandomGenerator();
    
    _colorPicker = new ColorPicker(_from1Color, RandomGenerator);
    _colorPicker.AddWeightedColorPicker(_from2Weight, _from2Color, RandomGenerator);
    _colorPicker.AddWeightedColorPicker(_from3Weight, _from3Color, RandomGenerator);
  }
  
  public void SetupGui(ControlP5 gui, String tabtitle, int x, int y, int yoffset) 
  {
    var guiOnChangeListener = new CallbackListener() 
    {
      public void controlEvent(CallbackEvent event) 
      {
        _minWeight = gui.getController(Guid + "_minWeight").getValue();
        _maxWeight = gui.getController(Guid + "_maxWeight").getValue();

        BindDefaultProperties(gui);

        Reset();
      }
    };

    y = SetupGuiForDefaultProperties(gui, tabtitle, guiOnChangeListener, x, y, yoffset);

    y += yoffset;

    gui.addNumberbox(Guid + "_minWeight")
        .setPosition(x, y).setLabel("Min Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_minWeight)
        .moveTo(tabtitle)
        .onChange(guiOnChangeListener);

    y += yoffset;

    gui.addNumberbox(Guid + "_maxWeight")
        .setPosition(x, y).setLabel("Max Line Weight")
        .setScrollSensitivity(1F).setMultiplier(1F)
        .setRange(1, 128)
        .setValue(_maxWeight)
        .moveTo(tabtitle)
        .onChange(guiOnChangeListener);

    y += yoffset;
   
    _from1ColorColorPicker = gui.addColorPicker(Guid + "_from1Color").setPosition(x, y)
        .setLabel("First From Color")
        .moveTo(tabtitle).setColorValue(_from1Color);

    y += yoffset;
    y += yoffset;
      
    _from2ColorColorPicker = gui.addColorPicker(Guid + "_from2Color").setPosition(x, y).setLabel("Second Color")
        .moveTo(tabtitle)
    .setColorValue(_from2Color);

    y += yoffset;
    y += yoffset;

    gui.addNumberbox(Guid + "_from2Weight").setPosition(x, y).setLabel("Second Color Weight")
      .setRange(0.0025f, 2.00f)
      .setDecimalPrecision(4)
      .setScrollSensitivity(0.0025f)
        .setMultiplier(0.0025f)
        .moveTo(tabtitle)
        .setValue(_from2Weight)
        .onChange(guiOnChangeListener);

    y += yoffset;

    _from3ColorColorPicker = gui.addColorPicker(Guid + "_from3Color").setPosition(x, y)
          .setLabel("Third Color")
          .moveTo(tabtitle)
          .setColorValue(_from3Color);

    y += yoffset;
    y += yoffset;
  
    gui.addNumberbox(Guid + "_from3Weight").setPosition(x, y).setLabel("Third Color Weight")
      .setRange(0.0025f, 2.00f)
      .setDecimalPrecision(4)
      .setScrollSensitivity(0.0025f)
        .setMultiplier(0.0025f)
        .moveTo(tabtitle)
        .setValue(_from3Weight)
        .onChange(guiOnChangeListener);
        
  }
 
  public void Update(double elapsedTime) 
  {
    RandomGenerator.ReSeed(RandomGenerator.Seed());

    var from1Color = _from1ColorColorPicker.getColorValue();
    var from2Color = _from2ColorColorPicker.getColorValue();
    var from3Color = _from3ColorColorPicker.getColorValue();

    if(_from1Color != from1Color || _from2Color != from2Color || _from3Color != from3Color)
    {
      Colorize();
    }
  }

  public void Draw(PGraphics canvas, double elapsedTime) 
  {
    canvas.beginDraw();

    {
      canvas.pushMatrix();

      canvas.translate(_x, _y, _z);

      for (var explodable : _explodables) {
        explodable.Draw(canvas);
      }

      canvas.popMatrix();
    }

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
    _from2Color = _from2ColorColorPicker.getColorValue();
    _from3Color = _from3ColorColorPicker.getColorValue();

    _colorPicker.Reset(_from1Color, RandomGenerator);
    _colorPicker.AddWeightedColorPicker(_from2Weight, _from2Color, RandomGenerator);
    _colorPicker.AddWeightedColorPicker(_from3Weight, _from3Color, RandomGenerator);
        
     for (var explodable : _explodables)
     {
        explodable.Colorize(_colorPicker);
     }
  }
}
