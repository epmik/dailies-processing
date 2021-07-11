package Utility;

import java.util.ArrayList;

import com.jogamp.opengl.FBObject.RenderAttachment;

import Procedural.RandomGenerator;
import Procedural.Interfaces.IRandomGenerator;

public class ColorPicker 
{
  private static IRandomGenerator DefaultRandomGenerator = new RandomGenerator();

  private static int _defaultFromColor = Color.HsbToInt(1.0, 0.0, 0.0, 1.0);
  private static int _defaultToColor = Color.HsbToInt(0.0, 1.0, 1.0, 1.0);

  private class WeightedColorPicker 
  {
    private double _weight = 1.0;
    private double _summedWeight = 1.0;

    private double _fromHue = 0.0;
    private double _toHue = 1.0;
    private double _fromSaturation = 0.0;
    private double _toSaturation = 1.0;
    private double _fromBrightness = 0.0;
    private double _toBrightness = 1.0;
    private double _fromAlpha = 1.0;
    private double _toAlpha = 1.0;

    private IRandomGenerator _randomGenerator;

    public WeightedColorPicker(double weight, int fromColor, int toColor, IRandomGenerator randomGenerator) 
    {
      var fromHsba = Color.IntToHsb(fromColor);
      var toHsba = Color.IntToHsb(toColor);

      _randomGenerator = randomGenerator;

      _weight = weight;

      _fromHue = fromHsba[0];
      _fromSaturation = fromHsba[1];
      _fromBrightness = fromHsba[2];
      _fromAlpha = fromHsba[3];

      _toHue = toHsba[0];
      _toSaturation = toHsba[1];
      _toBrightness = toHsba[2];
      _toAlpha = toHsba[3];
    }

    private int RandomColor() 
    {
      var h = RandomHue();
      var s = RandomSaturation();
      var b = RandomBrightness();
      var a = RandomAlpha();

      return Color.HsbToInt(h, s, b, a);
    }
    
    private double RandomHue() {
      return _randomGenerator.Value(_fromHue, _toHue);
    }
  
    private double RandomSaturation() {
      return _randomGenerator.Value(_fromSaturation, _toSaturation);
    }
  
    private double RandomBrightness() {
      return _randomGenerator.Value(_fromBrightness, _toBrightness);
    }
  
    private double RandomAlpha() {
      return _randomGenerator.Value(_fromAlpha, _toAlpha);
    }
  }

  private ArrayList<WeightedColorPicker> _weightedColorPickers = new ArrayList<WeightedColorPicker>();

  public ColorPicker() {
    this(DefaultRandomGenerator);
  }

  public ColorPicker(int color) 
  {
    this(color, color, DefaultRandomGenerator);
  }

  public ColorPicker(int fromColor, int toColor) 
  {
    this(fromColor, toColor, DefaultRandomGenerator);
  }

  public ColorPicker(IRandomGenerator randomGenerator) 
  {
    this(_defaultFromColor, _defaultToColor, randomGenerator);
  }

  public ColorPicker(int color, IRandomGenerator randomGenerator) 
  {
    this(color, color, randomGenerator);
  }

  public ColorPicker(int fromColor, int toColor, IRandomGenerator randomGenerator) 
  {
    Reset(fromColor, toColor, randomGenerator);
  }


  public void Reset() {
    Reset(DefaultRandomGenerator);
  }

  public void Reset(int color) 
  {
    Reset(color, color, DefaultRandomGenerator);
  }

  public void Reset(int fromColor, int toColor) 
  {
    Reset(fromColor, toColor, DefaultRandomGenerator);
  }

  public void Reset(IRandomGenerator randomGenerator) 
  {
    Reset(_defaultFromColor, _defaultToColor, randomGenerator);
  }

  public void Reset(int color, IRandomGenerator randomGenerator) 
  {
    Reset(color, color, randomGenerator);
  }

  public void Reset(int fromColor, int toColor, IRandomGenerator randomGenerator) 
  {
    _weightedColorPickers.clear();

    AddWeightedColorPicker(1.0, fromColor, toColor, randomGenerator);
  }
  

  public void AddWeightedColorPicker(double weight, IRandomGenerator randomGenerator) 
  {
    AddWeightedColorPicker(weight, _defaultFromColor, _defaultToColor, randomGenerator);
  }

  public void AddWeightedColorPicker(double weight, int color, IRandomGenerator randomGenerator) 
  {
    AddWeightedColorPicker(weight, color, color, randomGenerator);
  }

  public void AddWeightedColorPicker(double weight, int fromColor, int toColor, IRandomGenerator randomGenerator) 
  {
    _weightedColorPickers.add(new WeightedColorPicker(weight, fromColor, toColor, randomGenerator));

    NormalizeWeights();
  }

  private void NormalizeWeights()
  {
    var sum = 0.0;

    for (var s : _weightedColorPickers) {
      sum += s._weight;
    }

    var f = 1.0 / sum;

    var t = 0.0;

    for (var s : _weightedColorPickers) {
      s._summedWeight = t;
      t += s._weight * f;
    }
  }


  // private void BaseColor(int baseColor) {
  //   ColorRange(baseColor, baseColor);
  // }

  // private void ColorRange(int fromColor, int toColor) {
  //   var fromHsba = Color.IntToHsba(fromColor);
  //   var toHsba = Color.IntToHsba(toColor);

  //   _fromHue = fromHsba[0];
  //   _fromSaturation = fromHsba[1];
  //   _fromBrightness = fromHsba[2];
  //   _fromAlpha = fromHsba[3];

  //   _toHue = toHsba[0];
  //   _toSaturation = toHsba[1];
  //   _toBrightness = toHsba[2];
  //   _toAlpha = toHsba[3];
  // }

  // public void HueRange(int fromHue, int toHue) {
  //   HueRange(Color.DegreesToDouble(fromHue), Color.DegreesToDouble(toHue));
  // }

  // public void HueRange(double fromHue, double toHue) {
  //   _fromHue = fromHue;
  //   _toHue = toHue;
  // }

  // public void SaturationRange(int fromSaturation, int toSaturation) {
  //   SaturationRange(fromSaturation * 0.01, toSaturation * 0.01);
  // }

  // public void SaturationRange(double fromSaturation, double toSaturation) {
  //   _fromSaturation = fromSaturation;
  //   _toSaturation = toSaturation;
  // }

  // public void BrightnessRange(int fromBrightness, int toBrightness) {
  //   BrightnessRange(fromBrightness * 0.01, toBrightness * 0.01);
  // }

  // public void BrightnessRange(double fromBrightness, double toBrightness) {
  //   _fromBrightness = fromBrightness;
  //   _toBrightness = toBrightness;
  // }

  // public void AlphaRange(int fromAlpha, int toAlpha) {
  //   AlphaRange(fromAlpha * 0.01, toAlpha * 0.01);
  // }

  // public void AlphaRange(double fromAlpha, double toAlpha) {
  //   _fromAlpha = fromAlpha;
  //   _toAlpha = toAlpha;
  // }

  public static ColorPicker FromRgb(double r, double g, double b) {
    return FromRgb(r, g, b, 1.0, DefaultRandomGenerator);
  }

  public static ColorPicker FromRgb(double r, double g, double b, double a) {
    return new ColorPicker(Color.RgbToInt(r, g, b, a), DefaultRandomGenerator);
  }

  public static ColorPicker FromHsb(double h, double s, double b) {
    return FromHsb(h, s, b, 1.0, DefaultRandomGenerator);
  }

  public static ColorPicker FromHsb(double h, double s, double b, double a) {
    return new ColorPicker(Color.HsbToInt(h, s, b, a), DefaultRandomGenerator);
  }

  public static ColorPicker FromRgb(double r, double g, double b, IRandomGenerator randomGenerator) {
    return FromRgb(r, g, b, 1.0, randomGenerator);
  }

  public static ColorPicker FromRgb(double r, double g, double b, double a, IRandomGenerator randomGenerator) {
    return new ColorPicker(Color.RgbToInt(r, g, b, a), randomGenerator);
  }

  public static ColorPicker FromHsb(double h, double s, double b, IRandomGenerator randomGenerator) {
    return FromHsb(h, s, b, 1.0, randomGenerator);
  }

  public static ColorPicker FromHsb(double h, double s, double b, double a, IRandomGenerator randomGenerator) {
    return new ColorPicker(Color.HsbToInt(h, s, b, a), randomGenerator);
  }

  // public static ColorPicker FromHsb(double hfrom, double sfrom, double bfrom, double hto, double sto, double bto) {
  //   return new ColorPicker(Color.HsbToInt(hfrom, sfrom, bfrom, 1.0), Color.HsbToInt(hto, sto, bto, 1.0));
  // }

  // public static ColorPicker FromHsb(double hfrom, double sfrom, double bfrom, double afrom, double hto, double sto,
  //     double bto, double ato) {
  //   return new ColorPicker(Color.HsbToInt(hfrom, sfrom, bfrom, afrom), Color.HsbToInt(hto, sto, bto, ato));
  // }

  // public static int RandomColor()
  // {
  //   return RandomColor(RandomGenerator);
  // }

  // public static int RandomColor(IRandomGenerator randomGenerator)
  // {
  //   var h = randomGenerator.Value(0.0, 1.0);
  //   var s = randomGenerator.Value(0.0, 1.0);
  //   var b = randomGenerator.Value(0.0, 1.0);

  //   return Color.HsbToInt(h, s, b);
  // }

  // public int RandomColor() {
  //   return RandomColor(DefaultRandomGenerator);
  // }

  public int RandomColor() 
  {
    return PickWeightedColorPicker().RandomColor();
  }
  
  private WeightedColorPicker PickWeightedColorPicker()
  {
    if (_weightedColorPickers.size() > 1) 
    {
      var w = _weightedColorPickers.get(0)._randomGenerator.Value();

      for (var i = _weightedColorPickers.size() - 1; i > 0; i--) 
      {
        var weightedColorPicker = _weightedColorPickers.get(i);
  
        if (weightedColorPicker._summedWeight < w) 
        {
          return weightedColorPicker;
        }
      }
    }
    
    return _weightedColorPickers.get(0);
  }



  // public static int RandomColorFromSb(double s, double b)
  // {
  //   return RandomColorFromSb(s, b, RandomGenerator);
  // }

  // public static int RandomColorFromSb(double s, double b, IRandomGenerator randomGenerator)
  // {
  //   var h = randomGenerator.Value(0.0, 1.0);

  //   return Color.HsbToInt(h, s, b);
  // }

  // public static int RandomFromH(int hue, int variation)
  // {
  //   return RandomFromH(Color.DegreesToDouble(hue), Color.DegreesToDouble(variation));
  // }

  // public static int RandomFromH(int hue, int variation, IRandomGenerator randomGenerator)
  // {
  //   return RandomFromH(Color.DegreesToDouble(hue), Color.DegreesToDouble(variation), randomGenerator);
  // }

  // public static int RandomFromH(double hue, double variation)
  // {
  //   return RandomFromH(hue, variation, RandomGenerator);
  // }

  // public static int RandomFromH(double hue, double variation, IRandomGenerator randomGenerator)
  // {
  //   var h = randomGenerator.Value(Color.WrapHue(hue - variation), Color.WrapHue(hue + variation));
  //   var s = randomGenerator.Value(0.0, 1.0);
  //   var b = randomGenerator.Value(0.0, 1.0);

  //   return Color.HsbToInt(h, s, b);
  // }

  // public int[] Monochromatic() 
  // {
  //   return Monochromatic(DefaultRandomGenerator);
  // }

  private int[] Monochromatic(WeightedColorPicker colorPicker) 
  {
    var h = colorPicker.RandomHue();
    var s = colorPicker.RandomSaturation();
    var b = colorPicker.RandomBrightness();
    var a = 1.0;

    var colors = new int[5];

    colors[0] = Color.HsbToInt(h, s, Color.ClampBrightness(b - 0.50), a);
    colors[1] = Color.HsbToInt(h, Color.ClampSaturation(s - 0.30), b, a);
    colors[2] = Color.HsbToInt(h, s, b, a);
    colors[3] = Color.HsbToInt(h, Color.ClampSaturation(s - 0.30), Color.ClampBrightness(b - 0.50), a);
    colors[4] = Color.HsbToInt(h, s, Color.ClampBrightness(b - 0.20), a);

    return colors;
  }

  public int[] Monochromatic() 
  {
    return Monochromatic(PickWeightedColorPicker());
  }

  public static int[] Monochromatic(int baseColor) 
  {
    return Monochromatic(baseColor, DefaultRandomGenerator);
  }

  public static int[] Monochromatic(int baseColor, IRandomGenerator randomGenerator) 
  {
    return new ColorPicker(baseColor, randomGenerator).Monochromatic();
  }

}